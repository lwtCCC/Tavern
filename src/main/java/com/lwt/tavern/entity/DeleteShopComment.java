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
 * 商品评论（删除用）
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("`delete_shop_comment`")
public class DeleteShopComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    private Long id;
    /**
     * 商品id
     */
    private Long shopId;
    /**
     * 评论用户id
     */
    private Long userId;
    /**
     * 关联订单id（用于判断是否购买）
     */
    private Long orderId;
    /**
     * 父评论id，顶层为null
     */
    private Long parentId;
    /**
     * 实际回复id
     */
    private Long reply2CommentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 发布时间
     */
    private LocalDateTime createTime;
}
