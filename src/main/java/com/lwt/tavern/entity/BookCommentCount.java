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
 * 段评数量(评论计数)
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(chain = true)
@TableName("`book_comment_count`")
public class BookCommentCount implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 内容块id
     */
    private Long id;
    /**
     * 段评的评论数量
     */
    private Long commentCount;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
