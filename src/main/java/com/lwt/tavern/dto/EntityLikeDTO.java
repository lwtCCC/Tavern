package com.lwt.tavern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityLikeDTO {
    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 点赞用户id
     */
    private Long userId;
    /**
     * 实体类型：0书，1商品
     */
    private Short entityType;
    /**
     * 行为：0取消点赞，1点赞
     */
    private Integer status;
}
