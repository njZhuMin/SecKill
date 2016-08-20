package com.sunnywr.service;

import com.sunnywr.dto.Exposer;
import com.sunnywr.dto.SeckillExecution;
import com.sunnywr.entity.Seckill;
import com.sunnywr.exception.RepeatKillException;
import com.sunnywr.exception.SeckillCloseException;
import com.sunnywr.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在“使用者”的角度设计接口
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录，展示列表
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单条秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时间内输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId 秒杀Id
     * @param userPhone 用户身份
     * @param md5 校验url是否被篡改
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;
}
