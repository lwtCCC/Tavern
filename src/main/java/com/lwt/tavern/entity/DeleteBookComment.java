package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 段评类(删除用)
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(chain = true)
@TableName("`delete_book_comment`")
public class DeleteBookComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 段评id
     */
    private Long id;
    /**
     * 书本id
     */
    private Long bookId;
    /**
     * 内容块id
     */
    private Long blockId;
    /**
     * 父评论
     */
    private Long parentId;
    /**
     * 评论用户id
     */
    private Long userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 回复id
     */
    private Long reply2CommentId;
}
