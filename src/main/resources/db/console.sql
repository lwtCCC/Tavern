
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` BIGINT NOT NULL COMMENT '用户id',
                                      `username` VARCHAR(100) NOT NULL COMMENT '用户名',
                                      `password` VARCHAR(100) NOT NULL COMMENT '密码哈希',
                                      `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
                                      `avatar` VARCHAR(255) NOT NULL COMMENT '头像地址',
                                      `profile` VARCHAR(255) NOT NULL COMMENT '简介',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '上次活跃时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_username` (`username`),
                                      UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


CREATE TABLE IF NOT EXISTS `game_model` (
                                      `id` BIGINT NOT NULL COMMENT '游戏模型id',
                                      `user_id` BIGINT NOT NULL COMMENT '用户id',
                                      `level` INT NOT NULL DEFAULT 0 COMMENT '等级',
                                      `exe` INT NOT NULL DEFAULT 0 COMMENT '目前经验',
                                      `next_level` INT NOT NULL COMMENT '下一级所需的累计经验',
                                      `gold` BIGINT NOT NULL DEFAULT 0 COMMENT '虚拟货币',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏模型';


-- 点赞，收藏，浏览量，评论数，热度
CREATE TABLE IF NOT EXISTS `book` (
                                      `id` BIGINT NOT NULL COMMENT '书籍id',
                                      `book_name` VARCHAR(255) NOT NULL COMMENT '书名',
                                      `user_id` BIGINT NOT NULL COMMENT '作者用户id',
                                      `description` VARCHAR(255) COMMENT '文章描述',
                                      `view` BIGINT NOT NULL DEFAULT 0 COMMENT '观看数',
                                      `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞数',
                                      `star_count` BIGINT NOT NULL DEFAULT 0 COMMENT '收藏数',
                                      `hot` BIGINT NOT NULL DEFAULT 0 COMMENT '热度',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                          ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

CREATE TABLE IF NOT EXISTS `book_content_block` (
                                                    `id` BIGINT NOT NULL COMMENT '内容id',
                                                    `book_id` BIGINT NOT NULL COMMENT '所属书籍id',
                                                    `block_index` INT NOT NULL COMMENT '书内顺序，从0或1开始均可，但必须统一',
                                                    `block_type`  VARCHAR(32) NOT NULL COMMENT '段评类型paragraph/heading/code/quote/image...',
                                                    `attributes`  JSON,
                                                    `content` TEXT NOT NULL COMMENT 'Markdown解析后的内容块',
                                                    `comment_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论数量',
                                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE KEY `uk_book_block_index` (`book_id`, `block_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍内容表';

CREATE TABLE IF NOT EXISTS `entity_like` (
                                           `entity_id` BIGINT NOT NULL COMMENT '点赞实体id',
                                           `user_id` BIGINT NOT NULL COMMENT '点赞者id',
                                           `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0书，1商品, 2评论',
                                           `is_like` TINYINT UNSIGNED NOT NULL COMMENT '是否点赞：0未点赞，1点赞',
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           UNIQUE KEY `uk_entity_user_type` (`entity_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体点赞表';

CREATE TABLE IF NOT EXISTS `entity_star` (
                                           `entity_id` BIGINT NOT NULL COMMENT '收藏实体id',
                                           `user_id` BIGINT NOT NULL COMMENT '收藏者id',
                                           `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0书，1商品',
                                           `is_like` TINYINT UNSIGNED NOT NULL COMMENT '是否点赞：0未点赞，1点赞',
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                               ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           UNIQUE KEY `uk_entity_user_type` (`entity_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体收藏表';


CREATE TABLE `shop` (
                        `id` BIGINT NOT NULL COMMENT '商品ID',
                        `user_id` BIGINT NOT NULL COMMENT '商家ID',
                        `shop_name` VARCHAR(255) NOT NULL COMMENT '商品名称',
                        `description` VARCHAR(255) COMMENT '商品描述',
                        `unit_price` BIGINT NOT NULL COMMENT '商品单价，单位：gold',
                        `stock` INT NOT NULL DEFAULT 0 COMMENT '商品库存数量',
                        `min_level` INT NOT NULL DEFAULT 0 COMMENT '购买所需最低等级',
                        `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞数',
                        `view` BIGINT NOT NULL DEFAULT 0 COMMENT '观看数',
                        `star_count` BIGINT NOT NULL DEFAULT 0 COMMENT '收藏数',
                        `comment_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论数量',
                        `hot` BIGINT NOT NULL DEFAULT 0 COMMENT '热度',
                        `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品状态：0上架，1下架',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                        KEY `idx_status_price` (`status`, `unit_price`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE `shop_order` (
                              `id` BIGINT NOT NULL COMMENT '订单ID',
                              `shop_id` BIGINT NOT NULL COMMENT '商品ID',
                              `user_id` BIGINT NOT NULL COMMENT '购买用户ID',
                              `quantity` INT NOT NULL COMMENT '购买数量',
                              `unit_price` BIGINT NOT NULL COMMENT '下单时商品单价，单位：gold',
                              `total_price` BIGINT NOT NULL COMMENT '订单总价，单位：gold',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_user_time` (`user_id`, `create_time`, `id`),
                              KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品订单表';

CREATE TABLE IF NOT EXISTS `comment` (
                                              `id` BIGINT NOT NULL COMMENT '评论id',
                                              `entity_id` BIGINT NOT NULL COMMENT '所属实体id,这里指书本或者商品',
                                              `user_id` BIGINT NOT NULL COMMENT '发表者id',
                                              `root` BIGINT DEFAULT 0 COMMENT '0表示顶层评论，其余为回复',
                                              `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
                                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
                                              `parent_id` BIGINT DEFAULT NULL COMMENT '父级别评论，0为根',
                                              `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞数',
                                              `comment_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论数量',
                                              `hot` BIGINT NOT NULL DEFAULT 0 COMMENT '热度',
                                              PRIMARY KEY (`id`),
                                              KEY `idx_block_parent_time` (`entity_id`, `root`, `create_time`, `id`),
                                              KEY `idx_book_time` (`entity_id`, `create_time`, `id`),
                                              KEY `idx_user_time` (`user_id`, `create_time`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';


CREATE TABLE `image` (
                         `id` BIGINT NOT NULL COMMENT '图片ID',
                         `entity_id` BIGINT NOT NULL COMMENT '实体ID',
                         `entity_type` TINYINT UNSIGNED NOT NULL COMMENT '实体类型：0封面，1内容，2商品图片',
                         `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL或对象存储路径',
                         `sort_order` INT NOT NULL DEFAULT 0 COMMENT '展示顺序，数值越小越靠前',
                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         PRIMARY KEY (`id`),
                         KEY `idx_shop_sort` (`entity_id`, `sort_order`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实体图片表';

CREATE TABLE `message` (
                                `id` BIGINT NOT NULL COMMENT '消息ID',
                                `sender_id` BIGINT DEFAULT NULL COMMENT '触发消息的用户ID，系统消息可为空',
                                `receive_id` BIGINT NOT NULL COMMENT '接收消息的用户ID',
                                `entity_id` BIGINT NOT NULL COMMENT '实体ID，私信实体id为0',
                                `type` TINYINT NOT NULL COMMENT '消息类型：1通知，2私信，3公告',
                                `title` VARCHAR(100) NOT NULL COMMENT '消息标题',
                                `content` VARCHAR(255) DEFAULT NULL COMMENT '消息内容',
                                `action_type` TINYINT DEFAULT NULL COMMENT '行为类型：1点赞，2收藏，3评论，4购买，5私信',
                                `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '阅读状态：0未读，1已读',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息创建时间',
                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`),
                                KEY `idx_user_is_read` (`receive_id`, `is_read`, `create_time`, `id`),
                                KEY `idx_user_time` (`receive_id`, `create_time`, `id`),
                                KEY `idx_sender_time` (`sender_id`, `create_time`, `id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';