package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entry.Book;
import com.lwt.tavern.entry.PageBean;

public interface BookService extends IService<Book> {
    Result queryBookById(Long id);

    Result saveBook(Book book);


    Result deleteBookById(Long id);

    Result pageList(Integer pageNum, Integer pageSize);
}
