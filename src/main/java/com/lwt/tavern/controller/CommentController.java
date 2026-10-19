package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.CommentDTO;
import com.lwt.tavern.query.PageCommentQuery;
import com.lwt.tavern.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/hot")
    public Result getRootComment(PageCommentQuery query){
        return commentService.getRootComment(query);
    }

    @PostMapping("/public")
    public Result addComment(@RequestBody CommentDTO commentDTO){
        return commentService.addComment(commentDTO);
    }

    @DeleteMapping("/{id}")
    public Result deleteComment(@PathVariable Long id){
        return commentService.deleteComment(id);
    }

    
}
