package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentVO {

    /**
     * 评论id
     */
    private Long id;
    /**
     * 书本id
     */
    private Long entityId;
    /**
     * 内容块id
     */
    private Long root;
    /**
     * 评论用户id
     */
    private Long userId;
    private String username;
    /**
     * 父评论
     */
    private Long parentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 点赞数量
     */
    private Long likeCount;
    /**
     *  是否点赞
     */
    private Integer like;
}
