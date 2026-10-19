package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.MarkdownSegmenter;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.SnowFlake;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.PageDTO;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.entity.*;
import com.lwt.tavern.mapper.*;
import com.lwt.tavern.query.PageQuery;
import com.lwt.tavern.service.*;
import com.lwt.tavern.vo.BasicBookVO;
import com.lwt.tavern.vo.BookContentBlockVO;
import com.lwt.tavern.vo.BookVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final StringRedisTemplate stringRedisTemplate;
    private final SnowFlake bookSnowFlake;
    private final BookContentBlockService bookContentBlockService;
    private final MarkdownService markdownService;
    private final MarkdownSegmenter segmenter;
    private final SnowFlake bookContentBlockSnowFlake;
    private final EntityHotService entityHotService;
    private final BookMapper bookMapper;
    private final EntityLikeMapper entityLikeMapper;
    private final UserMapper userMapper;
    private final EntityStarMapper entityStarMapper;
    private final EntityLikeCountMapper entityLikeCountMapper;
    private final EntityStarCountMapper entityStarCountMapper;
    private final EntityViewMapper entityViewMapper;
    private final DeleteBookService deleteBookService;

    public BookServiceImpl(
            @Qualifier("bookSnowFlake") SnowFlake bookSnowFlake, StringRedisTemplate stringRedisTemplate,
            BookContentBlockService bookContentBlockService,
            MarkdownService markdownService, MarkdownSegmenter segmenter,
            @Qualifier("bookContentBlockSnowFlake") SnowFlake bookContentBlockSnowFlake,
            EntityHotService entityHotService, BookMapper bookMapper, EntityLikeMapper entityLikeMapper, UserMapper userMapper, EntityStarMapper entityStarMapper, EntityLikeCountMapper entityLikeCountMapper, EntityStarCountMapper entityStarCountMapper, EntityViewMapper entityViewMapper, DeleteBookService deleteBookService) {
        this.bookSnowFlake = bookSnowFlake;
        this.stringRedisTemplate = stringRedisTemplate;
        this.bookContentBlockService = bookContentBlockService;
        this.markdownService = markdownService;
        this.segmenter = segmenter;
        this.bookContentBlockSnowFlake = bookContentBlockSnowFlake;
        this.entityHotService = entityHotService;
        this.bookMapper = bookMapper;
        this.entityLikeMapper = entityLikeMapper;
        this.userMapper = userMapper;
        this.entityStarMapper = entityStarMapper;
        this.entityLikeCountMapper = entityLikeCountMapper;
        this.entityStarCountMapper = entityStarCountMapper;
        this.entityViewMapper = entityViewMapper;
        this.deleteBookService = deleteBookService;
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
            return Result.fail("非法数据");
        }

        //2.查询数据库是否存在该书
        Book book = query().eq("id", id).one();
        if (BeanUtil.isEmpty(book)){
            return Result.fail("该书不存在");
        }

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
        BookVO bookVO = BeanUtil.copyProperties(book, BookVO.class);
        bookVO.setBookContentBlockVO(contentBlockVOList);
        //4.返回视图VO
        return Result.success(bookVO);
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
                //雪花id
                bookId, bookDTO.getBookName(),
                UserHolder.getUser().getId(),
                LocalDateTime.now(), LocalDateTime.now(),
                bookDTO.getDescription()
        );

        //TODO 使用阿里云OSS存储图片
        //TODO 书的批量保存优化
        //3.保存
        //先保存基础的内容
        save(book);
        //再保存详细的内容
        //拆分内容模块为段，再批量保存
        long id = bookContentBlockSnowFlake.nextId();
        List<BookContentBlock> segment = segmenter.segment(bookDTO.getContent(), bookId, id);
        bookContentBlockService.saveBatch(segment);

        //TODO 在点赞，收藏等实体表添加字段

        return Result.success("文章保存成功");
    }

    /**
     * 查询所有的人的书，默认按照热度排名
     * @param pageQuery 需要当前页数，每页条数，排序字段，是否降序，书名，用户名，描述
     * @return 分页查询结果
     */
    @Override
    public Result pageBookByHot(PageQuery pageQuery) {
        //1.设置查询参数，关联热度表
        Page<EntityHot> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        //设置查询条件
        LambdaQueryChainWrapper<EntityHot> eq = entityHotService
                .lambdaQuery()
                .eq(EntityHot::getEntityType, 1).orderByDesc(EntityHot::getHot);
        //3.进行查询，获取热度前几
        return getPageByHotResult(page, eq);
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
        deleteBookService.save(deleteBook);

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
    public Result queryMyBooksByHot(PageQuery pageQuery) {

        //1.设置查询参数，关联热度表
        Page<EntityHot> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        //设置查询条件
        LambdaQueryChainWrapper<EntityHot> eq = entityHotService
                .lambdaQuery()
                .eq(EntityHot::getUserId, UserHolder.getUser().getId())
                .eq(EntityHot::getEntityType, 0).orderByDesc(EntityHot::getHot);
        return getPageByHotResult(page, eq);
    }

    private Result getPageByHotResult(Page<EntityHot> page, LambdaQueryChainWrapper<EntityHot> eq) {
        //3.进行查询，获取热度前几
        entityHotService.page(page, eq);
        //4.获取相关的实体id
        List<Long> entityIds = page.getRecords().stream().map(EntityHot::getEntityId).toList();
        //获取书（实体）
        List<Book> books = bookMapper.selectByIds(entityIds);

        //再通过书获取用户id集合和点赞和收藏和阅览
        Set<Long> userIds = books.stream().map(Book::getUserId).collect(Collectors.toSet());

        //userId::username
        Map<Long, String> userMap = userMapper
                .selectByIds(userIds).stream().collect(Collectors.toMap(User::getId, User::getUsername));

        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, entityIds)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        List<EntityStar> entityStars = entityStarMapper.selectList(
                new LambdaQueryWrapper<EntityStar>()
                        .eq(EntityStar::getUserId, UserHolder.getUser().getId())
                        .in(EntityStar::getEntityId, entityIds)
        );

        //收藏过的id列表
        List<Long> starList = entityStars.stream().map(EntityStar::getEntityId).toList();

        //点赞数
        Map<Long, Long> likeCountMap = entityLikeCountMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityLikeCount::getEntityId, EntityLikeCount::getLikeCount));

        //收藏数
        Map<Long, Long> starCountMap = entityStarCountMapper
                .selectByIds(entityIds).stream()
                .collect(Collectors.toMap(EntityStarCount::getEntityId, EntityStarCount::getStarCount));

        //浏览量
        Map<Long, Long> viewCountMap = entityViewMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityView::getEntityId, EntityView::getView));

        //组装
        List<BasicBookVO> voList = books.stream().map(book -> {
            BasicBookVO vo = BeanUtil.copyProperties(book, BasicBookVO.class);
            Long id = vo.getId();
            //作者信息填充
            String username = userMap.get(vo.getUserId());
            vo.setUsername(username);

            //点赞数填充
            Long likeCount = likeCountMap.get(id);
            Long starCount = starCountMap.get(id);
            Long viewCount = viewCountMap.get(id);
            vo.setLikeCount(likeCount);
            vo.setStarCount(starCount);
            vo.setViewCount(viewCount);

            //是否点赞
            vo.setLike(likeList.contains(id) ? 1 : 0);
            //是否收藏
            vo.setStar(starList.contains(id) ? 1 : 0);
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicBookVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    /**
     * 快速查询我的书，用时间排名
     * @return 分页查询结果
     */
    @Override
    public Result queryMyBooksByTime(PageQuery pageQuery) {
        //1.设置查询参数，关联热度表
        Page<Book> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        //设置查询条件
        LambdaQueryChainWrapper<Book> eq = lambdaQuery()
                .eq(Book::getUserId, UserHolder.getUser().getId())
                .orderByDesc(Book::getCreateTime);
        return getPageByTimeResult(page, eq);
    }

    private Result getPageByTimeResult(Page<Book> page, LambdaQueryChainWrapper<Book> eq) {
        //3.进行查询，获取热度前几
        page(page, eq);
        //4.获取相关的实体id
        List<Long> entityIds = page.getRecords().stream().map(Book::getId).toList();
        //获取书（实体）
        List<Book> books = page.getRecords();

        //再通过书获取用户id集合和点赞和收藏和阅览
        Set<Long> userIds = books.stream().map(Book::getUserId).collect(Collectors.toSet());

        //userId::username
        Map<Long, String> userMap = userMapper
                .selectByIds(userIds).stream().collect(Collectors.toMap(User::getId, User::getUsername));

        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, entityIds)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        List<EntityStar> entityStars = entityStarMapper.selectList(
                new LambdaQueryWrapper<EntityStar>()
                        .eq(EntityStar::getUserId, UserHolder.getUser().getId())
                        .in(EntityStar::getEntityId, entityIds)
        );

        //收藏过的id列表
        List<Long> starList = entityStars.stream().map(EntityStar::getEntityId).toList();

        //点赞数
        Map<Long, Long> likeCountMap = entityLikeCountMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityLikeCount::getEntityId, EntityLikeCount::getLikeCount));

        //收藏数
        Map<Long, Long> starCountMap = entityStarCountMapper
                .selectByIds(entityIds).stream()
                .collect(Collectors.toMap(EntityStarCount::getEntityId, EntityStarCount::getStarCount));

        //浏览量
        Map<Long, Long> viewCountMap = entityViewMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityView::getEntityId, EntityView::getView));

        //组装
        List<BasicBookVO> voList = books.stream().map(book -> {
            BasicBookVO vo = BeanUtil.copyProperties(book, BasicBookVO.class);
            Long id = vo.getId();
            //作者信息填充
            String username = userMap.get(vo.getUserId());
            vo.setUsername(username);

            //点赞数填充
            Long likeCount = likeCountMap.get(id);
            Long starCount = starCountMap.get(id);
            Long viewCount = viewCountMap.get(id);
            vo.setLikeCount(likeCount);
            vo.setStarCount(starCount);
            vo.setViewCount(viewCount);

            //是否点赞
            vo.setLike(likeList.contains(id) ? 1 : 0);
            //是否收藏
            vo.setStar(starList.contains(id) ? 1 : 0);
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicBookVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result pageBookByTime(PageQuery pageQuery) {
        //1.设置查询参数，关联热度表
        Page<Book> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        //设置查询条件
        LambdaQueryChainWrapper<Book> eq = lambdaQuery().orderByDesc(Book::getCreateTime);
        //3.进行查询，获取热度前几
        return getPageByTimeResult(page, eq);
    }

}
