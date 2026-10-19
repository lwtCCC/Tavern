package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShopVO {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 商品名字
     */
    private String name;
    /**
     * 商品价格
     */
    private Long price;
    /**
     * 商品库存
     */
    private Integer stock;
    /**
     * 商品状态
     */
    private Short status;
    /**
     * 商品图片
     */
    private String cover;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 限制购买的等级
     */
    private Integer minLevel;
    /**
     * 商品的更新时间
     */
    private LocalDateTime updateTime;
}
