package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(chain = true)
@TableName("`comment`")
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 段评id
     */
    private Long id;
    /**
     * 书本id
     */
    private Long entityId;
    /**
     * 评论用户id
     */
    private Long userId;
    /**
     * 内容块id
     */
    private Long root;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 父评论
     */
    private Long parentId;
    private Long likeCount;
    private Long hot;
}
