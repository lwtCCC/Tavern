package com.lwt.tavern.vo;

import lombok.Data;

@Data
public class GameModelWithMeVO {
    /**
     * 游戏模型id
     */
    private Long id;
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
