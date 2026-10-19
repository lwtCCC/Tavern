package com.lwt.tavern.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShopOrderDTO {

    /**
     * 具体商品id
     */
    private Long shopId;
    /**
     * 购买数量
     */
    private Integer quantity;
}
