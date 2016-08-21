package com.sunnywr.dao;

import com.sunnywr.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {

    /**
     * 减少库存
     * @param seckillId 秒杀id
     * @param killTime 秒杀成功时间
     * @return 表示更新行数记录
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 调用存储过程进行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);
}
