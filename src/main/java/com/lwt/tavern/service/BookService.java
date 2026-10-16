package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.entry.Book;

public interface BookService extends IService<Book> {
    Result queryBookById(Long id);

    Result saveBook(BookDTO bookDTO);


    Result deleteBookById(Long id);

    Result pageList(Integer pageNum, Integer pageSize);

    Result queryMyBooksByHot();

    Result queryMyBooksByTime();
}
