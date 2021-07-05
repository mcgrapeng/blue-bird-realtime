package com.bird.common.realtime.cache;

import com.bird.common.realtime.conts.Conts;
import com.bird.common.web.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/29 10:35
 */
@Component
public class CacheCallback {

    @Autowired
    private RedisUtils redisUtils;


    public long get(String prefix, String appType, long rid) {
        Object o = redisUtils.get(String.format(Conts.REDIS_KEY
                , prefix, appType, rid));

        if (o instanceof Integer) {
            return ((Integer) o).longValue();
        } else if (o instanceof Long) {
            return ((Long) o).longValue();
        }
        return 0L;
    }


    public long incrGet(String prefix, String appType, long rid) {
        return redisUtils.incr(String.format(Conts.REDIS_KEY
                , prefix, appType, rid), 1L);
    }


    public void incrLIKE(String appType, long rid, long userId) {
        redisUtils.incr(String.format(Conts.REDIS_KEY
                , Conts.RealTimeCount.LIKE.name(), appType, rid), 1L);
        redisUtils.incr(String.format(Conts.REDIS_USER_KEY
                , Conts.RealTimeCount.LIKE.name(), appType, rid, userId), 1L);
    }

    public void decrLIKE(String appType, long rid, long userId) {
        redisUtils.decr(String.format(Conts.REDIS_KEY
                , Conts.RealTimeCount.LIKE.name(), appType, rid), 1L);
        redisUtils.decr(String.format(Conts.REDIS_USER_KEY
                , Conts.RealTimeCount.LIKE.name(), appType, rid, userId), 1L);
    }

    public void delLIKE(String appType, long rid, long userId) {
        redisUtils.del(String.format(Conts.REDIS_USER_KEY
                , Conts.RealTimeCount.LIKE.name(), appType, rid, userId));
        redisUtils.decr(String.format(Conts.REDIS_KEY
                , Conts.RealTimeCount.LIKE.name(), appType, rid), 1L);
    }


    public long getCOMM(String appType, long rid, long rootId) {
        Object o = redisUtils.get(String.format(Conts.REDIS_COMMENT_KEY
                , Conts.RealTimeCount.COMM.name(), appType, rid, rootId));
        if (o instanceof Integer) {
            return ((Integer) o).longValue();
        } else if (o instanceof Long) {
            return ((Long) o).longValue();
        }
        return 0L;
    }

    public void incrCOMM(String appType, long rid) {
        redisUtils.incr(String.format(Conts.REDIS_KEY
                , Conts.RealTimeCount.COMM.name(), appType, rid), 1L);
    }

    public void incrCOMM(String appType, long rid, long rootId) {
        redisUtils.incr(String.format(Conts.REDIS_COMMENT_KEY
                , Conts.RealTimeCount.COMM.name(), appType, rid, rootId), 1L);
    }

    public void decrCOMM(String appType, long rid, long rootId) {
        redisUtils.decr(String.format(Conts.REDIS_COMMENT_KEY
                , Conts.RealTimeCount.COMM.name(), appType, rid, rootId), 1L);
    }


    public void decrCOMM(String appType, long rid) {
        redisUtils.decr(String.format(Conts.REDIS_KEY
                , Conts.RealTimeCount.COMM.name(), appType, rid), 1L);
    }


    public void incrTRAN(String appType, long rid) {
        redisUtils.incr(String.format(Conts.REDIS_KEY
                , Conts.RealTimeCount.TRAN.name(), appType, rid), 1L);
    }


    public void del(String prefix, String appType, long rid) {
        redisUtils.del(String.format(Conts.REDIS_KEY
                , prefix, appType, rid));
    }


    public void delMatch(String prefix, String appType, long rid) {
        redisUtils.del(String.join(String.format(Conts.REDIS_KEY
                , prefix, appType, rid), "*"));
    }


    public boolean hasKey(String prefix, String appType, long rid, long userId) {
        return redisUtils.hasKey(String.format(Conts.REDIS_USER_KEY
                , prefix, appType, rid, userId));
    }
}
