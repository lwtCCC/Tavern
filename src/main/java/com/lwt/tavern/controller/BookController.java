package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.entry.Book;
import com.lwt.tavern.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

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
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result deleteBookById(@PathVariable Long id){
        return bookService.deleteBookById(id);
    }

    /**
     * 分页查询，用于首页第一次展示
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result pageBook(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        return bookService.pageList(pageNum,pageSize);
    }

    /**
     * 查询自己的书，按热度
     * @return
     */
    @GetMapping("/me/hot")
    public Result queryMyBooksByHot(){
        return bookService.queryMyBooksByHot();
    }

    /**
     * 查询自己的书，按时间
     * @return
     */
    @GetMapping("/me/hot")
    public Result queryMyBooksByTime(){
        return bookService.queryMyBooksByTime();
    }
}
