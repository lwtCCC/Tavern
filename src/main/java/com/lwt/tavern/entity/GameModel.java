package com.lwt.tavern.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 游戏模型：等级，金币
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`game_model`")
public class GameModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 游戏模型id
     */
    private Long id;
    /**
     * 用户名id
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
     * 等级
     */
    private Integer level;
    /**
     * 经验值
     */
    private Long exe;
    /**
     * 下一级所需的经验
     */
    private Long nextLevel;
    /**
     * 虚拟货币
     */
    private Long gold;
}
