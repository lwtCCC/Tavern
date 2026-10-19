package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.query.PageQuery;
import com.lwt.tavern.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    /**
     * 分页查询，用于首页第一次展示
     */
    @GetMapping("/page/hot")
    public Result pageBookByHot(PageQuery pageQuery) {
        return bookService.pageBookByHot(pageQuery);
    }

    /**
     * 分页查询，用于首页第一次展示
     */
    @GetMapping("/page/time")
    public Result pageBookByTime(PageQuery pageQuery) {
        return bookService.pageBookByTime(pageQuery);
    }

    /**
     * 查看具体的书
     * @param id 书的id
     * @return 书的具体信息
     */
    @GetMapping("/{id}")
    public Result queryBookById(@PathVariable("id") Long id){
        return bookService.queryBookById(id);
    }

    /**
     * 保存写好的书
     * @param bookDTO 包含书名，图片地址，内容
     * @return 保存结果
     */
    @PostMapping
    public Result saveBook(@RequestBody BookDTO bookDTO){
        return bookService.saveBook(bookDTO);
    }

    /**
     * 删除自己的书
     * @param id 具体的书的id
     * @return 删除结果
     */
    @PutMapping("/{id}")
    public Result deleteBookById(@PathVariable Long id){
        return bookService.deleteBookById(id);
    }

    /**
     * 查询自己的书，按热度
     * @return 分页查询结果
     */
    @GetMapping("/me/hot")
    public Result queryMyBooksByHot(PageQuery pageQuery){
        return bookService.queryMyBooksByHot(pageQuery);
    }

    /**
     * 查询自己的书，按时间
     * @return 分页查询结果
     */
    @GetMapping("/me/time")
    public Result queryMyBooksByTime(PageQuery pageQuery){
        return bookService.queryMyBooksByTime(pageQuery);
    }

    //搜索es
}
