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
 * 书籍类
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`book`")
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 书本id
     */
    private Long id;
    /**
     * 书名
     */
    private String bookName;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 基础描述
     */
    private String description;
    private Long view;
    private Long likeCount;
    private Long starCount;
    private Long hot;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
