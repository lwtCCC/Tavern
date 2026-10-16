package com.lwt.tavern.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName(value = "`book_content_block`")
public class BookContentBlock implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long bookId;
    private Integer blockIndex;
    private String content;
    private LocalDateTime createTime;
    private String blockType;

    private Map<String, Object> attributes;
}
