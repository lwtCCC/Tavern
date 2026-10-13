package com.lwt.tavern.entry;

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
@TableName("`shop_image`")
public class ShopImage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long shopId;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
