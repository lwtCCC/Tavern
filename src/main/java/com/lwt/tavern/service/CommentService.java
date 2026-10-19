package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.CommentDTO;
import com.lwt.tavern.entity.Comment;
import com.lwt.tavern.query.PageCommentQuery;

public interface CommentService extends IService<Comment> {
    Result getRootComment(PageCommentQuery query);

    Result addComment(CommentDTO commentDTO);

    Result deleteComment(Long id);
}
