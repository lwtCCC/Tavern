package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体收藏数量
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`entity_star_count`")
public class EntityStarCount implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 点赞用户id
     */
    private Long starCount;
    /**
     * 实体类型：0书，1商品
     */
    private Short entityType;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
