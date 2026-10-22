package com.lwt.tavern.entity.delete;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品（删除用）
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`delete_shop`")
public class DeleteShop implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long id;
    /**
     * 用户id（商品所属用户）
     */
    private Long userId;
    /**
     * 商品名字
     */
    private String name;
    /**
     * 商品价格
     */
    private Long price;
    /**
     * 存量
     */
    private Integer stock;
    /**
     * 状态，0上架，1下架
     */
    private Short status;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品购买所需的最低等级
     */
    private Integer minLevel;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
