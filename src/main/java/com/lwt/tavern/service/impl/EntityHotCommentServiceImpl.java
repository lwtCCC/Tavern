package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.entity.EntityHotComment;
import com.lwt.tavern.mapper.EntityHotCommentMapper;
import com.lwt.tavern.service.EntityHotCommentService;
import org.springframework.stereotype.Service;

@Service
public class EntityHotCommentServiceImpl extends ServiceImpl<EntityHotCommentMapper, EntityHotComment> implements EntityHotCommentService {
}
