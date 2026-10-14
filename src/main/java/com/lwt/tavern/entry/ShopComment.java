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
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("`shop_comment`")
public class ShopComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long shopId;
    private Long userId;
    private Long parentId;
    private Long reply2CommentId;
    private String content;
    private Short deleted;
    private LocalDateTime createTime;
}
