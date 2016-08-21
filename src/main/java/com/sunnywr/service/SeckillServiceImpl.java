package com.sunnywr.service;

import com.sunnywr.cache.RedisDao;
import com.sunnywr.dao.SeckillDao;
import com.sunnywr.dao.SuccessKilledDao;
import com.sunnywr.dto.Exposer;
import com.sunnywr.dto.SeckillExecution;
import com.sunnywr.entity.Seckill;
import com.sunnywr.entity.SuccessKilled;
import com.sunnywr.enums.SeckillStateEnum;
import com.sunnywr.exception.RepeatKillException;
import com.sunnywr.exception.SeckillCloseException;
import com.sunnywr.exception.SeckillException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    private final String salt = "hmfg^GFu572_987&*%#@!^EGklAOvbnP";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }


    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public Exposer exportSeckillUrl(long seckillId) {
        // 缓存优化
        // 1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null) {
            // 2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                // 3.放入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();
        // 不处在秒杀时间段
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
            return new Exposer(false, seckillId,
                    nowTime.getTime(), startTime.getTime(), endTime.getTime());

        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if(md5 == null || !md5.equals(getMD5(seckillId)))
            throw new SeckillException("seckill url invalid");

        // 执行秒杀逻辑：减少库存，记录购买
        Date nowTime = new Date();
        try {
            // 记录购买明细
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            // 返回0，插入冲突，重复秒杀
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减少库存
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新到数据库
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (RepeatKillException e1) {
            throw e1;
        } catch (SeckillCloseException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 编译时异常转为运行时异常
            throw new SeckillException("inner error: " + e.getMessage());
        }
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if(md5 == null || !md5.equals(getMD5(seckillId)))
            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);

        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行完执行过程之后result被赋值
        try {
            seckillDao.killByProcedure(map);
            // 获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if(result == 1) {
                SuccessKilled sk = successKilledDao.
                        queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
            } else {
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
