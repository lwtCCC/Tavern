package com.lwt.tavern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookContentBlockVO {

    private Long id;
    private Long bookId;
    private Integer blockIndex;
    private String content;
    private LocalDateTime createTime;
}
