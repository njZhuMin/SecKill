package com.sunnywr.dto;

import com.google.common.base.MoreObjects;
import com.sunnywr.entity.SuccessKilled;
import com.sunnywr.enums.SeckillStateEnum;

/**
 * 封装执行秒杀的结果
 */
public class SeckillExecution {

    // 秒杀Id
    private long seckillId;

    // 执行结果状态
    private int state;

    // 状态表示
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    /**
     * 秒杀成功，带秒杀成功对象的构造方法
     * @param seckillId
     * @param stateEnum
     * @param successKilled
     */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    /**
     * 秒杀失败，不带秒杀成功对象的构造方法
     * @param seckillId
     * @param stateEnum
     */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("seckillId", seckillId)
                .add("state", state)
                .add("stateInfo", stateInfo)
                .add("successKilled", successKilled)
                .toString();
    }
}
