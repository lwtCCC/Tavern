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
@TableName("`book`")
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String bookName;
    private Long hot;
    private Long viewCount;
    private Long userId;
    private Long likeCount;
    private Long starCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private boolean deleted;
    private String cover;
}
