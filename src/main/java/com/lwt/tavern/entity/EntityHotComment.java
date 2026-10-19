package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`entity_hot`")
public class EntityHotComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 具体id
     */
    private Long id;
    /**
     * 关联的实体id
     */
    private Long entityId;
    /**
     * 热度
     */
    private Long hot;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
