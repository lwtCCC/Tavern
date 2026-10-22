package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.component.MarkdownService;
import com.lwt.tavern.common.MarkdownSegmenter;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.SnowFlake;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.component.LikeRedisService;
import com.lwt.tavern.component.StarRedisService;
import com.lwt.tavern.dto.EntityLikeDTO;
import com.lwt.tavern.dto.EntityStarDTO;
import com.lwt.tavern.dto.MessageDTO;
import com.lwt.tavern.dto.PageDTO;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.dto.user.UserDTO;
import com.lwt.tavern.entity.*;
import com.lwt.tavern.entity.delete.DeleteBook;
import com.lwt.tavern.mapper.*;
import com.lwt.tavern.query.PageQuery;
import com.lwt.tavern.service.*;
import com.lwt.tavern.vo.BasicBookVO;
import com.lwt.tavern.vo.BookContentBlockVO;
import com.lwt.tavern.vo.BookVO;
import com.lwt.tavern.vo.BookWithMEVO;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lwt.tavern.common.RedisConstants.*;
import static com.lwt.tavern.config.RabbitMqConfig.*;

@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final StringRedisTemplate redisTemplate;
    private final SnowFlake bookSnowFlake;
    private final BookContentBlockService bookContentBlockService;
    private final MarkdownService markdownService;
    private final MarkdownSegmenter segmenter;
    private final SnowFlake bookContentBlockSnowFlake;
    private final UserMapper userMapper;
    private final UserService userService;
    private final EntityLikeService entityLikeService;
    private final EntityStarService entityStarService;
    private final RabbitTemplate rabbitTemplate;
    private final LikeRedisService  likeRedisService;
    private final StarRedisService starRedisService;

    public BookServiceImpl(
            @Qualifier("bookSnowFlake") SnowFlake bookSnowFlake, StringRedisTemplate stringRedisTemplate,
            BookContentBlockService bookContentBlockService,
            MarkdownService markdownService, MarkdownSegmenter segmenter,
            @Qualifier("bookContentBlockSnowFlake") SnowFlake bookContentBlockSnowFlake,
            UserMapper userMapper, UserService userService,
            EntityLikeService entityLikeService, EntityStarService entityStarService,
            RabbitTemplate rabbitTemplate, LikeRedisService likeRedisService,
            StarRedisService starRedisService) {
        this.bookSnowFlake = bookSnowFlake;
        this.redisTemplate = stringRedisTemplate;
        this.bookContentBlockService = bookContentBlockService;
        this.markdownService = markdownService;
        this.segmenter = segmenter;
        this.bookContentBlockSnowFlake = bookContentBlockSnowFlake;
        this.userMapper = userMapper;
        this.userService = userService;
        this.entityLikeService = entityLikeService;
        this.entityStarService = entityStarService;
        this.rabbitTemplate = rabbitTemplate;
        this.likeRedisService = likeRedisService;
        this.starRedisService = starRedisService;
    }

    /**
     * 查询具体的书籍
     * @param id 书的id
     * @return 书的vo，包含用户信息，书的基础信息，书的段落信息，不包括段落的评论信息
     */
    @Override
    public Result queryBookById(Long id) {
        //1.id合法性
        if (id == null || id < 0){
            return Result.fail("非法数据id");
        }

        //2.查询数据库是否存在该书
        Book book = query().eq("id", id).one();
        if (BeanUtil.isEmpty(book)){
            return Result.fail("该书不存在");
        }

        String key = BOOK_DETAIL_KEY + id;
        Map<@NonNull Object, Object> entries = redisTemplate.opsForHash().entries(key);

        BookVO bookVO;
        //TODO 上锁
        if (entries == null){
            //3.填充书的内容
            //按书本id查出所有关联的段落，再按照index进行排序
            List<BookContentBlockVO> contentBlockVOList = bookContentBlockService
                    .query().eq("book_id", id)
                    .orderByAsc("block_index")
                    .list()
                    .stream()
                    .map(content-> {
                        BookContentBlockVO vo = BeanUtil.copyProperties(content, BookContentBlockVO.class);
                        String html = markdownService.toHtml(vo.getContent());
                        vo.setContent(html);
                        return vo;
                    })
                    .collect(Collectors.toList());
            bookVO = BeanUtil.copyProperties(book, BookVO.class);
            bookVO.setBookContentBlockVO(contentBlockVOList);

            Map<String, Object> bookMap = BeanUtil.beanToMap(bookVO,
                    new HashMap<>(),
                    new CopyOptions()
                            .ignoreNullValue()
                            .setFieldValueEditor((name,value)
                                    ->value.toString()));

            redisTemplate.opsForHash().putAll(key,bookMap);
            redisTemplate.expire(key,BOOK_DETAIL_TTL, TimeUnit.SECONDS);//点赞收藏浏览量全在这里
        }

        bookVO = BeanUtil.fillBeanWithMap(entries, new BookVO(), true);

        EntityLike isLike = entityLikeService.query().eq("entity_id", id).eq("is_like",1).one();
        EntityStar isStar = entityStarService.query().eq("entity_id", id).eq("is_star",1).one();
        BookWithMEVO vo = BeanUtil.copyProperties(bookVO, BookWithMEVO.class);
        vo.setIsLike(isLike == null?0:1);
        vo.setIsStar(isStar == null?0:1);

        //4.返回视图VO
        return Result.success(vo);
    }

    /**
     * 保存书籍
     * @param bookDTO 书名，图片地址, 内容，基础描述
     * @return 保存消息
     */
    @Override
    public Result saveBook(BookDTO bookDTO) {
        //1.前端数据合法性校验
        if (BeanUtil.isEmpty(bookDTO)){
            return Result.fail("请假查必填内容是否都有填写");
        }

        //2.填充数据
        long bookId = bookSnowFlake.nextId();
        Book book = new Book(
                bookId, bookDTO.getBookName(),
                UserHolder.getUser().getId(), bookDTO.getDescription(),0L,0L,0L,0L,
                LocalDateTime.now(), LocalDateTime.now()
        );

        //3.保存
        //先保存基础的内容
        save(book);
        //再保存详细的内容
        //拆分内容模块为段，再批量保存
        long id = bookContentBlockSnowFlake.nextId();
        List<BookContentBlock> segment = segmenter.segment(bookDTO.getContent(), bookId, id);
        bookContentBlockService.saveBatch(segment);

        return Result.success("文章保存成功");
    }

    /**
     * 查询所有的人的书，默认按照热度排名
     * @param pageQuery 需要当前页数，每页条数，排序字段，是否降序，书名，用户名，描述
     * @return 分页查询结果
     */
    @Override
    public Result pageBook(PageQuery pageQuery) {
        //page:book:hot:desc:1
        String key = PAGE_BOOK_KEY + (pageQuery.getIsHot() ? "hot":"time")
                + (pageQuery.getIsAsc()?"asc":"desc") + pageQuery.getPageNo();

        String s = redisTemplate.opsForValue().get(key);
        List<BasicBookVO> voList;
        //1.设置查询参数，关联热度表
        Page<Book> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        if (s == null){
            //设置查询条件
            LambdaQueryChainWrapper<Book> eq = lambdaQuery()
                    .orderByDesc(pageQuery.getIsHot()?Book::getHot:Book::getCreateTime);
            //3.进行查询，获取热度前几
            page(page, eq);
            //4.获取相关的实体id
            List<Long> ids = page.getRecords().stream().map(Book::getId).toList();
            //获取书（实体）
            List<Book> books = page.getRecords();

            //再通过书获取用户id集合和点赞和收藏和阅览
            Set<Long> userIds = books.stream().map(Book::getUserId).collect(Collectors.toSet());

            //userId::user
            Map<Long, User> userMap = userMapper
                    .selectByIds(userIds).stream().collect(Collectors.toMap(User::getId, u->u));

            //组装
            voList = books.stream().map(book -> {
                BasicBookVO vo = BeanUtil.copyProperties(book, BasicBookVO.class);
                Long id = vo.getId();
                //作者信息填充
                User user = userMap.get(vo.getUserId());
                vo.setUsername(user.getUsername());
                vo.setAvatar(user.getAvatar());
                return vo;
            }).toList();

            String jsonStr = JSONUtil.toJsonStr(voList);

            redisTemplate.opsForValue().set(key,jsonStr,PAGE_BOOK_TTL, TimeUnit.SECONDS);
        }

        voList = JSONUtil.toList(s, BasicBookVO.class);

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicBookVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    /**
     * 删除自己的文章
     * @param id 传递书籍id
     * @return 返回结果
     */
    @Override
    public Result deleteBookById(Long id) {

        //先查询
        Book one = lambdaQuery()
                .eq(Book::getId, id)
                .eq(Book::getUserId, UserHolder.getUser().getId())
                .one();

        //再保存
        DeleteBook deleteBook = BeanUtil.copyProperties(one, DeleteBook.class);

        //再删除
        remove(
                new LambdaQueryWrapper<Book>().eq(Book::getId, id)
        );
        //返回分页再发一个请求
        return Result.success();
    }

    /**
     * 快速查询我的书，用热度排名
     * @return 分页查询结果
     */
    @Override
    public Result queryMyBooks(PageQuery pageQuery) {

        //1.设置查询参数，关联热度表
        Page<Book> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        //设置查询条件
        LambdaQueryChainWrapper<Book> eq = lambdaQuery()
                .orderByDesc(pageQuery.getIsHot()?Book::getHot:Book::getCreateTime);
        //3.进行查询，获取热度前几
        page(page, eq);
        //4.获取相关的实体id
        List<Long> ids = page.getRecords().stream().map(Book::getId).toList();
        //获取书（实体）
        List<Book> books = page.getRecords();

        Long userId = UserHolder.getUser().getId();
        User user = userService.query().eq("id", userId).one();

        //组装
        List<BasicBookVO> voList = books.stream().map(book -> {
            BasicBookVO vo = BeanUtil.copyProperties(book, BasicBookVO.class);
            Long id = vo.getId();
            //作者信息填充
            vo.setUsername(user.getUsername());
            vo.setAvatar(user.getAvatar());
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicBookVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result likeBook(EntityLikeDTO dto) {
        UserDTO user = UserHolder.getUser();
        if (user == null) {
            return Result.fail("");
        }
        Long userId = user.getId();
        if (!userId.equals(dto.getUserId())) {
            return Result.fail("");
        }

        //执行lua脚本
        Long entityId = dto.getEntityId();
        Short entityType = dto.getEntityType();
        Boolean isLike = dto.getIsLike();

        Long execute = likeRedisService.execute(
                userId, entityId, entityType, isLike
        );

        //创建消息
        MessageDTO message = MessageDTO.builder().id(entityId).userId(userId)
                .type(entityType).action(isLike).timestamp(LocalDateTime.now()).build();

        String jsonStr = JSONUtil.toJsonStr(message);

        //后续交给mq异步处理
        rabbitTemplate.convertAndSend(
                LIKE_EXCHANGE, LIKE_KEY, jsonStr
        );

        return Result.success();
    }

    @Override
    public Result starBook(EntityStarDTO dto) {

        UserDTO user = UserHolder.getUser();
        if (user == null) {
            return Result.fail("");
        }
        Long userId = user.getId();
        if (!userId.equals(dto.getUserId())) {
            return Result.fail("");
        }

        //执行lua脚本
        Long entityId = dto.getEntityId();
        Short entityType = dto.getEntityType();
        Boolean isStar = dto.getIsStar();

        Long execute = starRedisService.execute(
                userId, entityId, entityType, isStar
        );

        //创建消息
        MessageDTO message = MessageDTO.builder().id(entityId).userId(userId)
                .type(entityType).action(isStar).timestamp(LocalDateTime.now()).build();

        String jsonStr = JSONUtil.toJsonStr(message);

        //后续交给mq异步处理
        rabbitTemplate.convertAndSend(
                STAR_EXCHANGE, STAR_KEY, jsonStr
        );

        return Result.success();
    }
}