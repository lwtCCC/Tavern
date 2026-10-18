package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopBasicVO {
    private Long id;
    private String shopName;
    private String description;
    private String image;
}
