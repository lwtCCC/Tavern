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
 * 实体观看数
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`entity_view`")
public class EntityView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 点赞观看数
     */
    private Long view;
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
