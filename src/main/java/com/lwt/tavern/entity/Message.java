package com.lwt.tavern.entity;

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
@TableName("`message`")
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long entityId;
    private Integer type;
    private String title;
    private String content;
    private Integer actionType;
    private Integer isRead;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
