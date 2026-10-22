package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`shop`")
public class Shop implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 商品id
     */
    private Long id;
    /**
     * 商品发布者id
     */
    private Long userId;
    /**
     * 商品名字
     */
    private String shopName;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品价格
     */
    private Long unitPrice;
    /**
     * 商品库存
     */
    private Integer stock;
    /**
     * 购买所需最低等级
     */
    private Integer minLevel;
    private Long likeCount;
    private Long view;
    private Long starCount;
    private Long commentCount;
    private Long hot;
    /**
     * 商品状态：0上架，1下架
     */
    private Short status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
