package com.lwt.tavern.entry;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("`user`")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 简介
     */
    private String profile;
    /**
     * 逻辑删除
     */
    @TableField("is_deleted")
    private Short deleted;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 手机号
     */
    private String phone;
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
