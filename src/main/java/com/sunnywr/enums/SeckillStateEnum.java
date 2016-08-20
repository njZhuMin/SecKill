package com.sunnywr.enums;

/**
 * 枚举秒杀状态数据字段
 */
public enum  SeckillStateEnum {
    SUCCESS(1, "seckill success"),
    END(0, "seckill closed"),
    REPEAT_KILL(-1, "repeat seckill"),
    INNER_ERROR(-2, "inner error"),
    DATA_REWRITE(-3, "invalid url");

    private int state;

    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateEnum stateOf(int index) {
        for(SeckillStateEnum state : values()) {
            if(state.getState() == index)
                return state;
        }
        return null;
    }
}
