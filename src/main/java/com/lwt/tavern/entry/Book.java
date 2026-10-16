package com.lwt.tavern.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 热度
     */
    private Long hot;
    /**
     * 阅览量
     */
    private Long viewCount;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 点赞数量
     */
    private Long likeCount;
    /**
     * 收藏数量
     */
    private Long starCount;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 逻辑删除
     */
    private Short deleted;
    /**
     * 封面
     */
    private String cover;
    /**
     * 基础描述
     */
    private String description;
}
