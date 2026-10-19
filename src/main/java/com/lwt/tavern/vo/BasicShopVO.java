package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicShopVO {
    private Long id;
    private String shopName;
    private String description;
    private String image;
    private Long userId;
    private String username;
    /**
     * 阅览量
     */
    private Long viewCount;
    /**
     * 点赞数量
     */
    private Long likeCount;
    /**
     * 收藏数量
     */
    private Long starCount;

    /**
     *  是否点赞
     */
    private Integer like;
    /**
     * 是否收藏
     */
    private Integer star;
}
