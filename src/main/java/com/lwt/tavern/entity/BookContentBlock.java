package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 段落
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName(value = "`book_content_block`")
public class BookContentBlock implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 段落id
     */
    private Long id;
    /**
     * 所属的书籍id
     */
    private Long bookId;
    /**
     * 在书类的顺序
     */
    private Integer blockIndex;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 段评类型，用于渲染markdown
     */
    private String blockType;
    /**
     * 类型，如语言：java，python之类的
     */
    private Map<String, Object> attributes;
}
