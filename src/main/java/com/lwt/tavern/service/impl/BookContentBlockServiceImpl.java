package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.entity.BookContentBlock;
import com.lwt.tavern.mapper.BookContentBlockMapper;
import com.lwt.tavern.service.BookContentBlockService;
import org.springframework.stereotype.Service;

@Service
public class BookContentBlockServiceImpl extends ServiceImpl<BookContentBlockMapper,BookContentBlock> implements BookContentBlockService {
}
