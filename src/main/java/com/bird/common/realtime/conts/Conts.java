package com.bird.common.realtime.conts;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/4 11:10
 */
public final class Conts {

    public static final String REDIS_KEY = "bird:REALTIME:%s:%s:%s";

    public static final String REDIS_COMMENT_KEY = "bird:REALTIME:REPLY:%s:%s:%s:%s";

    public static final String REDIS_USER_KEY = "bird:REALTIME:%s:%s:%s:%s";


    public enum RealTimeCount {
        COMM,
        TRAN,
        READ,
        LIKE;
    }
}
