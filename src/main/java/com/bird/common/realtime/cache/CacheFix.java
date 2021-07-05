package com.bird.common.realtime.cache;

import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.service.comment.RealTimeCommService;
import com.bird.common.realtime.service.like.RealTimeLikeService;
import com.bird.common.realtime.service.read.RealTimeReadService;
import com.bird.common.web.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/11/19 15:40
 */
@Component
public class CacheFix {


    @Autowired
    private RealTimeReadService readService;

    @Autowired
    private RealTimeCommService commService;

   /* @Autowired
    private RealTimeTranService tranService;*/

    @Autowired
    private RealTimeLikeService likeService;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 重置
     *
     * @param key
     */
    public void reset(String type, String appType, String key) {

        if (type.equals(Conts.RealTimeCount.READ.name())) {
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.READ.name(), appType, key);
            redisUtils.del(_key);
        } else if (type.equals(Conts.RealTimeCount.TRAN.name())) {
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.TRAN.name(), appType, key);
            redisUtils.del(_key);
        } else if (type.equals(Conts.RealTimeCount.LIKE.name())) {
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.LIKE.name(), appType, key);
            redisUtils.del(_key);
        } else if (type.equals(Conts.RealTimeCount.COMM.name())) {
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.COMM.name(), appType, key);
            redisUtils.del(_key);
        }
    }


    /**
     * 修复
     *
     * @param key
     */
    public void fix(String type, String appType, String key) {

        if (type.equals(Conts.RealTimeCount.READ.name())) {
            int readTotal = readService.readTotal(Long.parseLong(key));
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.READ.name(), key);
            redisUtils.set(_key, readTotal);
        } else if (type.equals(Conts.RealTimeCount.TRAN.name())) {
           /* int tranTotal = tranService.tranTotal(Long.parseLong(key));
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.TRAN.name(), appType,Long.parseLong(key));
            redisUtils.set(_key, tranTotal);*/
        } else if (type.equals(Conts.RealTimeCount.LIKE.name())) {
            int likeTotal = likeService.likeTotal(Long.parseLong(key));
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.LIKE.name(), appType, key);
            redisUtils.set(_key, likeTotal);
        } else if (type.equals(Conts.RealTimeCount.COMM.name())) {
            int commTotal = commService.commTotal(Long.parseLong(key));
            String _key = String.format(Conts.REDIS_KEY
                    , Conts.RealTimeCount.COMM.name(), appType, key);
            redisUtils.set(_key, commTotal);
        }
    }

}
