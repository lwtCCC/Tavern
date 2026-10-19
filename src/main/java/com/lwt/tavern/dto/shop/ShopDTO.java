package com.lwt.tavern.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopDTO {

    private Long id;

    private String name;

    private String description;

    private Long price;

    private Integer stock;

    private Short status;

    private Integer minLevel;
}
