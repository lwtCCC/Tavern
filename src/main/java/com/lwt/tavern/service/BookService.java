package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.entity.Book;
import com.lwt.tavern.query.BookQuery;
import com.lwt.tavern.query.PageQuery;

public interface BookService extends IService<Book> {

    Result pageBookByHot(PageQuery pageQuery);

    Result queryBookById(Long id);

    Result saveBook(BookDTO bookDTO);

    Result deleteBookById(Long id);

    Result queryMyBooksByHot(PageQuery pageQuery);

    Result queryMyBooksByTime(PageQuery pageQuery);

    Result pageBookByTime(PageQuery pageQuery);
}
