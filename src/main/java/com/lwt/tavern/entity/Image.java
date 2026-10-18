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
 * 图片类
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`image`")
public class Image implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片id
     */
    private Long id;
    /**
     * 关联的实体id
     */
    private Long entityId;
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 展示顺序，数字越小越靠前
     */
    private Integer sortOrder;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
