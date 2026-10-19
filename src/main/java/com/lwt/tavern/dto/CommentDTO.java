package com.lwt.tavern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {
    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 是否是根评论
     */
    private Long root;
    /**
     * 父评论
     */
    private Long parentId;
    /**
     * 评论内容
     */
    private String content;
}
