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
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(chain = true)
@TableName("`book_comment`")
public class BookComment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long bookId;
    private Long blockId;
    private Long parentId;
    private Long userId;
    private String content;
    private Short deleted;
    private LocalDateTime createTime;
    private Long reply2CommentId;
}
