-- 定义秒杀存储过程
DELIMITER $$
-- 定义存储过程
-- ROW_COUNT()：返回上一条修改类型的SQL影响的行数
--            0：未修改数据；>0：修改的行数；<0：SQL错误未执行修改
CREATE PROCEDURE seckill.execute_seckill(
        in v_seckill_id BIGINT, in v_phone BIGINT,
        in v_kill_time TIMESTAMP, out r_result INT)
    BEGIN
        DECLARE insert_count INT DEFAULT 0;
        START TRANSACTION;
        INSERT IGNORE INTO success_killed(seckill_id, user_phone, create_time, state)
            VALUES(v_seckill_id, v_phone, v_kill_time, 0);
        SELECT ROW_COUNT() INTO insert_count;
        IF (insert_count = 0) THEN
            ROLLBACK;
            SET r_result = -1;
        ELSEIF (insert_count < 0) THEN
            ROLLBACK;
            SET r_result = -2;
        ELSE
            UPDATE seckill SET number = number - 1
                WHERE seckill_id = v_seckill_id
                AND end_time > v_kill_time
                AND start_time < v_kill_time
                AND number > 0;
        SELECT ROW_COUNT() INTO insert_count;
            IF (insert_count = 0) THEN
                ROLLBACK;
                SET r_result = 0;
            ELSEIF (insert_count < 0) THEN
                ROLLBACK;
                SET r_result = -2;
            ELSE
                COMMIT;
                SET r_result = 1;
            END IF;
        END IF;
    END;
$$
-- 存储过程定义结束
DELIMITER ;


SET @r_result = -3;
-- 调用存储过程
CALL execute_seckill(1003, 12345123456, NOW(), @r_result);
-- 获取结果
SELECT @r_result;

