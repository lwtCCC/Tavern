package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entry.Book;
import com.lwt.tavern.entry.User;
import com.lwt.tavern.mapper.BookMapper;
import com.lwt.tavern.service.BookService;
import com.lwt.tavern.service.UserService;
import com.lwt.tavern.vo.BookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.lwt.tavern.common.RedisConstants.CACHE_BOOK_KEY;

@RequiredArgsConstructor
@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserService userService;

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
            BookVO book = JSONUtil.toBean(json, BookVO.class);
            return Result.success(book);
        }
        //3.没有就去查询数据库
        Book book = query().eq("id", id).one();
        if (BeanUtil.isEmpty(book)){
            return Result.fail("该书已下架");
        }
        User user = userService.query().eq("id", book.getUserId()).one();
        if (BeanUtil.isEmpty(user)) {
            return Result.fail("用户消失");
        }
        BookVO bookVO = BeanUtil.copyProperties(book, BookVO.class);
        bookVO.setUser(user);
        String jsonStr = JSONUtil.toJsonStr(bookVO);
        stringRedisTemplate.opsForValue().set(CACHE_BOOK_KEY + id,jsonStr);

        return Result.success(book);
    }

    /**
     * 保存书籍
     * @param book 书
     * @return 保存消息
     */
    @Override
    public Result saveBook(Book book) {

        if (BeanUtil.isEmpty(book)){
            return Result.fail("书是空白的");
        }
        //TODO 书的id
        save(book);

        return Result.success();
    }

    @Override
    public Result deleteBookById(Long id) {

        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Book::getId, id)
                .set(Book::getDeleted, 1);
        update(null,updateWrapper);
        //TODO 覆盖原有数据
        return Result.success();
    }

    @Override
    public Result pageList(Integer pageNum, Integer pageSize) {
        Page<Book> page = new Page<>();
        Page<Book> bookList = page(page);
        return Result.success(bookList);
    }
}
