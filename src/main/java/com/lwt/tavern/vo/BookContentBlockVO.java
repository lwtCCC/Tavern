package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookContentBlockVO {
    /**
     * 内容id
     */
    private Long id;
    /**
     * 关联书籍id
     */
    private Long bookId;
    /**
     * 内容
     */
    private String content;
}
