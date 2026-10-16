package com.lwt.tavern.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    /**
     * 书名
     */
    private String bookName;
    /**
     * 封面
     */
    private String cover;
    /**
     * 内容（service拆分）
     */
    private String content;
    /**
     * 基础描述
     */
    private String description;
}
