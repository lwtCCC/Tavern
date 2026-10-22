package com.lwt.tavern.entity.delete;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 书（删除用）
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Accessors(chain = true)
@TableName("`delete_book`")
public class DeleteBook implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 书本id
     */
    private Long id;
    /**
     * 书名
     */
    private String bookName;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 基础描述
     */
    private String description;
}
