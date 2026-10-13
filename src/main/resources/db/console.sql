# 创建专用的数据库
create database Tavern;
# 使用数据库
use Tavern;

CREATE TABLE IF NOT EXISTS `user` (
                                      `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                                      `username`    VARCHAR(100) NOT NULL COMMENT '用户名',
                                      `password`    VARCHAR(100) NOT NULL COMMENT '密码哈希',
                                      `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
                                      `profile`     VARCHAR(255) DEFAULT NULL COMMENT '简介',
                                      `status`      TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '账号状态：0-禁用 1-正常',
                                      `is_deleted`  TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-已删除',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

