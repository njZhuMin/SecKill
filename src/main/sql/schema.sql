CREATE DATABASE seckill;
USE seckill;

CREATE TABLE seckill(
    seckill_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
    name VARCHAR(120) NOT NULL COMMENT '商品名称',
    number INT NOT NULL COMMENT '库存数量',
    start_time TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
    end_time TIMESTAMP NOt NULL COMMENT '秒杀结束时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 创建索引
    PRIMARY KEY (seckill_id),
    KEY idx_start_time(start_time),
    KEY idx_end_time(end_time),
    KEY idx_create_time(create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据
INSERT INTO
    seckill(name, number, start_time, end_time)
VALUES
    ('1000元秒杀iPhone',100,'2016-08-15 00:00:00','2016-08-16 00:00:00'),
    ('500元秒杀iPad',200,'2016-08-15 00:00:00','2016-08-16 00:00:00'),
    ('300元秒杀小米',300,'2016-08-15 00:00:00','2016-08-16 00:00:00'),
    ('200元秒杀红米Note',400,'2016-08-15 00:00:00','2016-08-16 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证的相关信息
CREATE TABLE success_killed(
    seckill_id BIGINT NOT NULL COMMENT '秒杀商品id',
    user_phone BIGINT NOT NULL COMMENT '用户手机号',
    state TINYINT NOT NULL DEFAULT -1 COMMENT '状态标志：-1无效，0：购买成功，1：已付款，2：已发货',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    /*联合主键，防止用户重复秒杀*/
    PRIMARY KEY (seckill_id, user_phone),
    KEY idx_create_time(create_time)
) ENGINE=InnoDB CHARSET=utf8 COMMENT='秒杀成功明细表';