package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicBookVO {
    /**
     * 书本id
     */
    private Long id;
    /**
     * 书名
     */
    private String bookName;
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
     * 封面
     */
    private String cover;
    /**
     * 描述信息
     */
    private String description;

    /**
     * 作者名称
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;

    /**
     *  是否点赞
     */
    private Integer like;
    /**
     * 是否收藏
     */
    private Integer star;
}
