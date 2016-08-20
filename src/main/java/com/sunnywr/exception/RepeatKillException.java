package com.sunnywr.exception;

/**
 * 重复秒杀异常（RUNTIME）
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
