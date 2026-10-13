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
@Accessors
@TableName("`book_star`")
public class BookStar implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long bookId;
    private boolean stared;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
