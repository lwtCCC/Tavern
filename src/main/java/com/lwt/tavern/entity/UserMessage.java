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
 * 用户消息（不包括私信）
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`user_message`")
public class UserMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 触发消息的用id，比如谁购买了我的商品，谁点赞了我的文章
     */
    private Long sendId;
    /**
     * 消息类型：1点赞，2收藏，3商品，4系统
     */
    private Integer messageType;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 关联对象类型：1书，2商品，3订单
     */
    private Integer targetType;
    /**
     * 关联对象id
     */
    private Long targetId;
    /**
     * 阅读状态：0未读，1已读
     */
    private Short read;
    /**
     * 发送时间
     */
    private LocalDateTime createTime;
}
