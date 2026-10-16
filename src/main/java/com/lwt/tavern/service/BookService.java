package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.entry.Book;
import com.lwt.tavern.query.BookQuery;

public interface BookService extends IService<Book> {

    Result pageBookByHot(BookQuery bookQuery);

    Result queryBookById(Long id);

    Result saveBook(BookDTO bookDTO);

    Result deleteBookById(Long id);

    Result queryMyBooksByHot();

    Result queryMyBooksByTime();

    Result queryMyBooks(BookQuery bookQuery);
}
