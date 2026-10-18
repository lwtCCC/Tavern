package com.lwt.tavern.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShopWithUpdateDTO {
    private Long id;
    private Long userId;
    private String name;
    private Long price;
    private Integer stock;
    private Short status;
    private String cover;
    private String description;
    private Integer minLevel;
    private Short deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
