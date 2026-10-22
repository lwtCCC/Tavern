package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.EntityLikeDTO;
import com.lwt.tavern.dto.EntityStarDTO;
import com.lwt.tavern.dto.book.BookDTO;
import com.lwt.tavern.entity.Book;
import com.lwt.tavern.query.PageQuery;

public interface BookService extends IService<Book> {

    Result pageBook(PageQuery pageQuery);

    Result queryBookById(Long id);

    Result saveBook(BookDTO bookDTO);

    Result deleteBookById(Long id);

    Result queryMyBooks(PageQuery pageQuery);

    Result likeBook(EntityLikeDTO dto);

    Result starBook(EntityStarDTO dto);
}
