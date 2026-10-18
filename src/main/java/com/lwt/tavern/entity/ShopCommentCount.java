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
 * 商品评论数量
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("`shop_comment_count`")
public class ShopCommentCount implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long shopId;
    /**
     * 评论数量
     */
    private Long commentCount;
    /**
     * 发布时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
