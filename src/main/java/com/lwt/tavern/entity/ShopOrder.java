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
 * 订单
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`shop_order`")
public class ShopOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订购id
     */
    private Long id;
    /**
     * 订单用户id
     */
    private Long userId;
    /**
     * 具体商品id
     */
    private Long shopId;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 单价
     */
    private Long unitPrice;
    /**
     * 总价
     */
    private Long totalPrice;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
