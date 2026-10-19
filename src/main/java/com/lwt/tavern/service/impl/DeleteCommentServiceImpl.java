package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.entity.DeleteComment;
import com.lwt.tavern.mapper.DeleteCommentMapper;
import com.lwt.tavern.service.DeleteCommentService;
import org.springframework.stereotype.Service;

@Service
public class DeleteCommentServiceImpl extends ServiceImpl<DeleteCommentMapper,DeleteComment> implements DeleteCommentService {
}
