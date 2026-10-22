package com.lwt.tavern.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MessageDTO {
    /**
     * 实体id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 实体类型
     */
    private Short type;
    /**
     * 行为
     */
    private Boolean action;
    private LocalDateTime timestamp;
}
