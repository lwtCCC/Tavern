package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
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
     * @param book 书的dto
     * @return 保存结果
     */
    @PostMapping
    public Result saveBook(@RequestBody Book book){
        return bookService.saveBook(book);
    }

    @PutMapping("/{id}")
    public Result deleteBookById(@PathVariable Long id){
        return bookService.deleteBookById(id);
    }

    @GetMapping("/page")
    public Result pageBook(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        return bookService.pageList(pageNum,pageSize);
    }
}
