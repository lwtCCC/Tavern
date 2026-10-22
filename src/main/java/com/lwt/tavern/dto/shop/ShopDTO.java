package com.lwt.tavern.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopDTO {

    private Long id;
    private Long userId;

    private String shopName;

    private String description;

    private Long unitPrice;

    private Integer stock;

    private Short status;

    private Integer minLevel;
}
