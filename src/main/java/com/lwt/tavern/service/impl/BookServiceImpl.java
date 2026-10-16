package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.MarkdownSegmenter;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.SnowFlake;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.dto.user.UserDTO;
import com.lwt.tavern.entry.Book;
import com.lwt.tavern.entry.BookContentBlock;
import com.lwt.tavern.entry.User;
import com.lwt.tavern.mapper.BookContentBlockMapper;
import com.lwt.tavern.mapper.BookMapper;
import com.lwt.tavern.service.BookContentBlockService;
import com.lwt.tavern.service.BookService;
import com.lwt.tavern.service.UserService;
import com.lwt.tavern.vo.BookContentBlockVO;
import com.lwt.tavern.vo.BookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lwt.tavern.common.RedisConstants.CACHE_BOOK_KEY;
import static com.lwt.tavern.common.RedisConstants.CACHE_BOOK_TTL;


@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserService userService;
    private final SnowFlake bookSnowFlake;
    private final BookContentBlockService bookContentBlockService;
    private final MarkdownService markdownService;
    private final MarkdownSegmenter segmenter;
    private final SnowFlake bookContentBlockSnowFlake;

    public BookServiceImpl(
            @Qualifier("bookSnowFlake") SnowFlake bookSnowFlake, StringRedisTemplate stringRedisTemplate,
            UserService userService, BookContentBlockService bookContentBlockService,
            MarkdownService markdownService, MarkdownSegmenter segmenter,
            @Qualifier("bookContentBlockSnowFlake") SnowFlake bookContentBlockSnowFlake) {
        this.bookSnowFlake = bookSnowFlake;
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
        this.bookContentBlockService = bookContentBlockService;
        this.markdownService = markdownService;
        this.segmenter = segmenter;
        this.bookContentBlockSnowFlake = bookContentBlockSnowFlake;
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
        //2.redis有没有该书的缓存
        String json = stringRedisTemplate.opsForValue().get(CACHE_BOOK_KEY + id);
        if (StrUtil.isNotBlank(json)){
            //使用VO展示数据
            BookVO bookVO = JSONUtil.toBean(json, BookVO.class);
            return Result.success(bookVO);
        }
        //3.没有就去查询数据库
        Book book = query().eq("id", id).one();
        if (BeanUtil.isEmpty(book)){
            return Result.fail("该书不存在");
        }
        if (book.getDeleted() == 1){
            return Result.fail("该书已下架");
        }

        //暂时不处理作者查不到的问题
        User user = userService.query().eq("id", book.getUserId()).one();
        //书的内容 书的段评
        List<BookContentBlockVO> contentBlockVOList = bookContentBlockService
                .query().eq("book_id", id)
                .orderByAsc("block_index")
                .list()
                .stream()
                .map(content-> {
                    BookContentBlockVO vo = BeanUtil.copyProperties(content, BookContentBlockVO.class);
                    String html = markdownService.toHtml(vo.getContent());
                    vo.setContent(html);
                    return vo;}
                )
                .collect(Collectors.toList());
        BookVO bookVO = BeanUtil.copyProperties(book, BookVO.class);
        bookVO.setUser(user);
        bookVO.setBookContentBlockVO(contentBlockVOList);
        String jsonStr = JSONUtil.toJsonStr(bookVO);
        stringRedisTemplate.opsForValue()
                .set(
                        CACHE_BOOK_KEY + id,
                        jsonStr,
                        CACHE_BOOK_TTL,
                        TimeUnit.MINUTES
                );
        //返回视图VO
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
                bookId, bookDTO.getBookName(), 0L, 0L,
                UserHolder.getUser().getId(), 0L, 0L,
                LocalDateTime.now(), LocalDateTime.now(), (short) 0,//逻辑删除
                bookDTO.getCover(), bookDTO.getDescription()
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

        return Result.success("文章保存成功");
    }

    @Override
    public Result deleteBookById(Long id) {

        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Book::getId, id)
                .set(Book::getDeleted, 1);
        update(null,updateWrapper);
        //TODO 查询所有数据并覆盖原有数据
        return Result.success();
    }

    @Override
    public Result pageList(Integer pageNum, Integer pageSize) {
        Page<Book> page = new Page<>();
        Page<Book> bookList = page(page);
        return Result.success(bookList);
    }

    @Override
    public Result queryMyBooksByHot() {
        UserDTO userDTO = UserHolder.getUser();
        Long userId = userDTO.getId();

        //TODO 分页
        List<Book> list = query().eq("user_id", userId).eq("deleted", 0).orderByAsc("hot").list();

        return Result.success(list);
    }

    @Override
    public Result queryMyBooksByTime() {
        UserDTO userDTO = UserHolder.getUser();
        Long userId = userDTO.getId();

        //TODO 分页
        List<Book> list = query().eq("user_id", userId).eq("deleted", 0).orderByAsc("create_time").list();

        return Result.success(list);
    }
}
