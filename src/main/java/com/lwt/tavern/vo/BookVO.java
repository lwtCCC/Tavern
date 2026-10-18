package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookVO {
    /**
     * 书本id
     */
    private Long id;
    /**
     * 书名
     */
    private String bookName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 封面
     */
    private String cover;
    /**
     * 书的内容(分段)
     */
    private List<BookContentBlockVO> bookContentBlockVO;
}
