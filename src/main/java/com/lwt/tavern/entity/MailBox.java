package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 私信
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("`mailbox`")
public class MailBox implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 信件id
     */
    private Long id;
    /**
     * 发信人
     */
    private Long userId;
    /**
     * 信的内容
     */
    private String mailContent;
    /**
     * 送达时间
     */
    private LocalDateTime sendTime;
    /**
     * 接收人id
     */
    private Long sendId;
    /**
     * 发信者是否可见
     */
    private Short deletedUser;
    /**
     * 收信人是否可见
     */
    private Short deletedSend;
}
