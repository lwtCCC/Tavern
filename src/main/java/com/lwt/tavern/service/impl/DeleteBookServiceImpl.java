package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.entity.DeleteBook;
import com.lwt.tavern.mapper.DeleteBookMapper;
import com.lwt.tavern.service.DeleteBookService;
import org.springframework.stereotype.Service;

@Service
public class DeleteBookServiceImpl extends ServiceImpl<DeleteBookMapper, DeleteBook> implements DeleteBookService {
}
