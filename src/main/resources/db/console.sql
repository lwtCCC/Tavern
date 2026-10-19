
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` BIGINT NOT NULL COMMENT '用户id',
                                      `username` VARCHAR(100) NOT NULL COMMENT '用户名',
                                      `password` VARCHAR(100) NOT NULL COMMENT '密码哈希',
                                      `avatar` VARCHAR(255) NOT NULL COMMENT '头像地址',
                                      `profile` VARCHAR(255) NOT NULL COMMENT '简介',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '上次活跃时间',
                                      `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_username` (`username`),
                                      UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


CREATE TABLE IF NOT EXISTS `delete_user` (
                                      `id` BIGINT NOT NULL COMMENT '用户id',
                                      `username` VARCHAR(100) NOT NULL COMMENT '用户名',
                                      `password` VARCHAR(100) NOT NULL COMMENT '密码哈希',
                                      `avatar` VARCHAR(255) NOT NULL COMMENT '头像地址',
                                      `profile` VARCHAR(255) NOT NULL COMMENT '简介',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '上次活跃时间',
                                      `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_username` (`username`),
                                      UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表_删除';


CREATE TABLE IF NOT EXISTS `game_model` (
                                      `id` BIGINT NOT NULL COMMENT '游戏模型id',
                                      `user_id` BIGINT NOT NULL COMMENT '用户id',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `level` INT NOT NULL DEFAULT 0 COMMENT '等级',
                                      `exe` INT NOT NULL DEFAULT 0 COMMENT '目前经验',
                                      `next_level` INT NOT NULL COMMENT '下一级所需的累计经验',
                                      `gold` BIGINT NOT NULL DEFAULT 0 COMMENT '虚拟货币',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏模型';


-- 点赞，收藏，浏览量，评论数，热度
CREATE TABLE IF NOT EXISTS `book` (
                                      `id` BIGINT NOT NULL COMMENT '书籍id',
                                      `book_name` VARCHAR(255) NOT NULL COMMENT '书名',
                                      `user_id` BIGINT NOT NULL COMMENT '作者用户id',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `description` VARCHAR(255) COMMENT '文章描述',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

CREATE TABLE IF NOT EXISTS `delete_book` (
                                      `id` BIGINT NOT NULL COMMENT '书籍id',
                                      `book_name` VARCHAR(255) NOT NULL COMMENT '书名',
                                      `user_id` BIGINT NOT NULL COMMENT '作者用户id',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `description` VARCHAR(255) COMMENT '文章描述',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表_删除';

CREATE TABLE IF NOT EXISTS `entity_view` (
                                      `entity_id` BIGINT NOT NULL COMMENT '实体id',
                                      `view` BIGINT NOT NULL DEFAULT 0 COMMENT '观看数',
                                      `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0文章，1商品',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

-- 主键唯一
CREATE TABLE IF NOT EXISTS `entity_hot` (
                                             `entity_id` BIGINT NOT NULL COMMENT '实体id',
                                             `hot` BIGINT NOT NULL DEFAULT 0 COMMENT '热度',
                                             `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0文章，1商品',
                                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                                 ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                             `user_id` BIGINT NOT NULL COMMENT '关联的用户id，用于做自己的热度查询',
                                             PRIMARY KEY (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

-- 主键不唯一
CREATE TABLE IF NOT EXISTS `entity_like` (
                                           `entity_id` BIGINT NOT NULL COMMENT '点赞实体id',
                                           `user_id` BIGINT NOT NULL COMMENT '点赞者id',
                                           `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0书，1商品',
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           UNIQUE KEY `uk_entity_user_type` (`entity_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体点赞表';

-- 主键唯一
CREATE TABLE IF NOT EXISTS `entity_like_count` (
                                             `entity_id` BIGINT NOT NULL COMMENT '实体id',
                                             `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞数',
                                             `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0书，1商品',
                                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                                 ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                             PRIMARY KEY (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体点赞表';

-- 主键不唯一
CREATE TABLE IF NOT EXISTS `entity_star` (
                                           `entity_id` BIGINT NOT NULL COMMENT '收藏实体id',
                                           `user_id` BIGINT NOT NULL COMMENT '收藏者id',
                                           `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型-0书，1商品',
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           UNIQUE KEY `uk_entity_user_type` (`entity_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体收藏表';

-- 主键唯一
CREATE TABLE IF NOT EXISTS `entity_star_count` (
                                             `entity_id` BIGINT NOT NULL COMMENT '实体id',
                                             `star_count` BIGINT NOT NULL DEFAULT 0 COMMENT '收藏数',
                                             `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型-0书，1商品',
                                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                                 ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                             PRIMARY KEY (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体收藏表';

/*CREATE TABLE IF NOT EXISTS `book_like` (
                                           `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '点赞记录id',
                                           `user_id` BIGINT UNSIGNED NOT NULL COMMENT '点赞者id',
                                           `book_id` BIGINT UNSIGNED NOT NULL COMMENT '被点赞的书籍id',
                                           `is_like` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0有效点赞，1取消点赞',
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
                                           KEY `idx_book_like` (`book_id`, `is_like`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍点赞表';*/

/*CREATE TABLE IF NOT EXISTS `book_star` (
                                           `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏记录id',
                                           `user_id` BIGINT UNSIGNED NOT NULL COMMENT '收藏者id',
                                           `book_id` BIGINT UNSIGNED NOT NULL COMMENT '被收藏的书籍id',
                                           `is_star` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0有效收藏，1取消收藏',
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `uk_user_book` (`user_id`, `book_id`),
                                           KEY `idx_book_star` (`book_id`, `is_star`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍收藏表';*/

CREATE TABLE IF NOT EXISTS `book_content_block` (
                                                    `id` BIGINT NOT NULL COMMENT '内容块id',
                                                    `book_id` BIGINT NOT NULL COMMENT '所属书籍id',
                                                    `block_index` INT NOT NULL COMMENT '书内顺序，从0或1开始均可，但必须统一',
                                                    `content` TEXT NOT NULL COMMENT 'Markdown解析后的内容块',
                                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                    `block_type`  VARCHAR(32) NOT NULL COMMENT '段评类型paragraph/heading/code/quote/image...',
                                                    `attributes`  JSON,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE KEY `uk_book_block_index` (`book_id`, `block_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍内容块表';


CREATE TABLE IF NOT EXISTS `book_comment` (
                                              `id` BIGINT NOT NULL COMMENT '段评id',
                                              `book_id` BIGINT NOT NULL COMMENT '所属书籍id',
                                              `block_id` BIGINT NOT NULL COMMENT '关联内容块id',
                                              `parent_id` BIGINT DEFAULT NULL COMMENT '顶层评论id，NULL表示顶层评论',
                                              `user_id` BIGINT NOT NULL COMMENT '发表者id',
                                              `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
                                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
                                              `reply_to_comment_id` BIGINT DEFAULT NULL COMMENT '实际回复的评论id',
                                              PRIMARY KEY (`id`),
                                              KEY `idx_block_parent_time` (`block_id`, `parent_id`, `create_time`, `id`),
                                              KEY `idx_book_time` (`book_id`, `create_time`, `id`),
                                              KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍段评表';

CREATE TABLE IF NOT EXISTS `delete_book_comment` (
                                              `id` BIGINT NOT NULL COMMENT '段评id',
                                              `book_id` BIGINT NOT NULL COMMENT '所属书籍id',
                                              `block_id` BIGINT NOT NULL COMMENT '关联内容块id',
                                              `parent_id` BIGINT DEFAULT NULL COMMENT '顶层评论id，NULL表示顶层评论',
                                              `user_id` BIGINT NOT NULL COMMENT '发表者id',
                                              `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
                                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
                                              `reply_to_comment_id` BIGINT DEFAULT NULL COMMENT '实际回复的评论id',
                                              PRIMARY KEY (`id`),
                                              KEY `idx_block_parent_time` (`block_id`, `parent_id`, `create_time`, `id`),
                                              KEY `idx_book_time` (`book_id`, `create_time`, `id`),
                                              KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍段评表_删除';

CREATE TABLE IF NOT EXISTS `book_comment_count` (
                                              `id` BIGINT NOT NULL COMMENT '内容块id',
                                              `comment_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论数量',
                                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
                                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                                  ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍段评表';

/*CREATE TABLE IF NOT EXISTS `shop` (
                                      `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品id',
                                      `shop_name` VARCHAR(255) NOT NULL COMMENT '商品名称',
                                      `user_id` BIGINT NOT NULL COMMENT '商家用户id',
                                      `price` BIGINT NOT NULL DEFAULT 999 COMMENT '价格，金币整数',
                                      `is_sell` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否售罄，0未售罄，1售罄',
                                      `number` INT NOT NULL DEFAULT 1 COMMENT '商品库存',
                                      `description` VARCHAR(255) NOT NULL COMMENT '商品描述',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `min_level` INT NOT NULL DEFAULT 0 COMMENT '所需最低等级',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城商品表';*/

CREATE TABLE `image` (
                         `id` BIGINT NOT NULL COMMENT '图片ID',
                         `entity_id` BIGINT NOT NULL COMMENT '实体ID',
                         `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0书籍，1商品，2头像',
                         `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL或对象存储路径',
                         `sort_order` INT NOT NULL DEFAULT 0 COMMENT '展示顺序，数值越小越靠前',
                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         PRIMARY KEY (`id`),
                         KEY `idx_shop_sort` (`entity_id`, `sort_order`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体图片表';

CREATE TABLE `shop` (
                        `id` BIGINT NOT NULL COMMENT '商品ID',
                        `user_id` BIGINT NOT NULL COMMENT '商品发布者ID',
                        `name` VARCHAR(255) NOT NULL COMMENT '商品名称',
                        `price` BIGINT NOT NULL COMMENT '商品价格，单位：gold',
                        `stock` INT NOT NULL DEFAULT 0 COMMENT '商品库存数量',
                        `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品状态：0下架，1上架',
                        `description` VARCHAR(255) COMMENT '商品描述',
                        `min_level` INT NOT NULL DEFAULT 0 COMMENT '购买所需最低等级',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                        KEY `idx_status_price` (`status`, `price`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE `delete_shop` (
                        `id` BIGINT NOT NULL COMMENT '商品ID',
                        `user_id` BIGINT NOT NULL COMMENT '商品发布者ID',
                        `name` VARCHAR(255) NOT NULL COMMENT '商品名称',
                        `price` BIGINT NOT NULL COMMENT '商品价格，单位：gold',
                        `stock` INT NOT NULL DEFAULT 0 COMMENT '商品库存数量',
                        `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品状态：0下架，1上架',
                        `description` VARCHAR(255) COMMENT '商品描述',
                        `min_level` INT NOT NULL DEFAULT 0 COMMENT '购买所需最低等级',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                        KEY `idx_status_price` (`status`, `price`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表_删除';

/*CREATE TABLE IF NOT EXISTS `shop_order` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单id',
                                            `user_id` BIGINT NOT NULL COMMENT '买家id',
                                            `shop_id` BIGINT NOT NULL COMMENT '商品id',
                                            `number` INT NOT NULL COMMENT '购买数量',
                                            `price` BIGINT NOT NULL COMMENT '下单时单价',
                                            `total_price` BIGINT NOT NULL COMMENT '订单总价',
                                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
                                            PRIMARY KEY (`id`),
                                            KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                                            KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城订单表';*/

CREATE TABLE `shop_order` (
                              `id` BIGINT NOT NULL COMMENT '订单ID',
                              `user_id` BIGINT NOT NULL COMMENT '购买用户ID',
                              `shop_id` BIGINT NOT NULL COMMENT '商品ID',
                              `quantity` INT NOT NULL COMMENT '购买数量',
                              `unit_price` BIGINT NOT NULL COMMENT '下单时商品单价，单位：gold',
                              `total_price` BIGINT NOT NULL COMMENT '订单总价，单位：gold',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                              KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品订单表';

CREATE TABLE `shop_comment` (
                                `id` BIGINT NOT NULL COMMENT '评论ID',
                                `shop_id` BIGINT NOT NULL COMMENT '商品ID',
                                `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
                                `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID，可用于判断是否购买',
                                `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID，顶级评论为NULL',
                                `reply_to_comment_id` BIGINT DEFAULT NULL COMMENT '直接回复的评论ID',
                                `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

                                PRIMARY KEY (`id`),
                                KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                                KEY `idx_order_id` (`order_id`),
                                KEY `idx_parent_time` (`parent_id`, `create_time`, `id`),
                                KEY `idx_reply_to_comment` (`reply_to_comment_id`),
                                KEY `idx_shop_time` (`shop_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论表';

CREATE TABLE `shop_comment_count` (
                                `shop_id` BIGINT NOT NULL COMMENT '商品ID',
                                `comment_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论数量',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论表';

CREATE TABLE `delete_shop_comment` (
                                `id` BIGINT NOT NULL COMMENT '评论ID',
                                `shop_id` BIGINT NOT NULL COMMENT '商品ID',
                                `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
                                `order_id` BIGINT DEFAULT NULL COMMENT '关联订单ID，可用于判断是否购买',
                                `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID，顶级评论为NULL',
                                `reply_to_comment_id` BIGINT DEFAULT NULL COMMENT '直接回复的评论ID',
                                `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

                                PRIMARY KEY (`id`),
                                KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                                KEY `idx_order_id` (`order_id`),
                                KEY `idx_parent_time` (`parent_id`, `create_time`, `id`),
                                KEY `idx_reply_to_comment` (`reply_to_comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论表_商品';

CREATE TABLE `user_message` (
                                `id` BIGINT NOT NULL COMMENT '消息ID',
                                `user_id` BIGINT NOT NULL COMMENT '接收消息的用户ID',
                                `sender_id` BIGINT DEFAULT NULL COMMENT '触发消息的用户ID，系统消息可为空',
                                `message_type` TINYINT UNSIGNED NOT NULL COMMENT '消息类型：1点赞，2收藏，3商品相关，4系统通知',
                                `title` VARCHAR(100) NOT NULL COMMENT '消息标题',
                                `content` VARCHAR(255) DEFAULT NULL COMMENT '消息内容',
                                `target_type` TINYINT UNSIGNED DEFAULT NULL COMMENT '关联对象类型：1文章，2商品，3订单',
                                `target_id` BIGINT DEFAULT NULL COMMENT '关联对象ID',
                                `is_read` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '阅读状态：0未读，1已读',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息创建时间',

                                PRIMARY KEY (`id`),
                                KEY `idx_user_is_read` (`user_id`, `is_read`, `create_time`, `id`),
                                KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                                KEY `idx_target` (`target_type`, `target_id`),
                                KEY `idx_sender_time` (`sender_id`, `create_time`, `id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户系统消息表';

CREATE TABLE IF NOT EXISTS `mailbox` (
                                         `id` BIGINT NOT NULL COMMENT '信件id',
                                         `user_id` BIGINT NOT NULL COMMENT '收信者id',
                                         `mail_content` VARCHAR(255) NOT NULL COMMENT '信件内容',
                                         `send_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                                         `send_id` BIGINT NOT NULL COMMENT '发信者id',
                                         `is_deleted_user` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收信者是否删除，0正常，1删除',
                                         `is_deleted_send` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送者是否删除，0正常，1删除',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_receiver_time` (`user_id`, `is_deleted_user`, `send_time`, `id`),
                                         KEY `idx_sender_time` (`send_id`, `is_deleted_send`, `send_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='私信表';

CREATE TABLE IF NOT EXISTS `comment` (
                                              `id` BIGINT NOT NULL COMMENT '评论id',
                                              `entity_id` BIGINT NOT NULL COMMENT '所属实体id,这里指书本或者商品',
                                              `root` BIGINT DEFAULT 0 COMMENT '0表示顶层评论，其余为回复',
                                              `user_id` BIGINT NOT NULL COMMENT '发表者id',
                                              `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
                                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
                                              `parent_id` BIGINT DEFAULT NULL COMMENT '父级别评论，0为根',
                                              PRIMARY KEY (`id`),
                                              KEY `idx_block_parent_time` (`entity_id`, `root`, `create_time`, `id`),
                                              KEY `idx_book_time` (`entity_id`, `create_time`, `id`),
                                              KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

CREATE TABLE IF NOT EXISTS `delete_comment` (
                                         `id` BIGINT NOT NULL COMMENT '评论id',
                                         `entity_id` BIGINT NOT NULL COMMENT '所属实体id,这里指书本或者商品',
                                         `root` BIGINT DEFAULT 0 COMMENT '0表示顶层评论，其余为回复',
                                         `user_id` BIGINT NOT NULL COMMENT '发表者id',
                                         `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
                                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
                                         `parent_id` BIGINT DEFAULT NULL COMMENT '父级别评论，0为根',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_block_parent_time` (`entity_id`, `root`, `create_time`, `id`),
                                         KEY `idx_book_time` (`entity_id`, `create_time`, `id`),
                                         KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表_删除';


CREATE TABLE IF NOT EXISTS `entity_hot_comment` (
                                            `id` BIGINT NOT NULL COMMENT '评论id，用于查找评论表的评论',
                                            `entity_id` BIGINT NOT NULL COMMENT '实体id，可以是书，商品，或者评论',
                                            `hot` BIGINT NOT NULL DEFAULT 0 COMMENT '热度',
                                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                                ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论热度表';