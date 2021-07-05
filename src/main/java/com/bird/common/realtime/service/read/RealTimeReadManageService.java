package com.bird.common.realtime.service.read;

import com.google.common.collect.Maps;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTimeRead;
import com.bird.common.realtime.mapper.RealTimeReadMapper;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.SnowflakeIdWorker;
import com.bird.common.web.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/24 14:52
 */
@Slf4j
@Service
public class RealTimeReadManageService {


    @Autowired
    private RealTimeReadMapper newsReadMapper;


    @Autowired
    private CacheCallback cacheCallback;

    /**
     * 添加阅读记录
     *
     * @param rid
     */
    public void doRealTimeRead(String appType , long rid, String infoType, String rtType, long userId, String realName, long realtimeUserId, String realtimeUserName) {
        RealTimeRead realTimeRead = new RealTimeRead();

        long did = SnowflakeIdWorker.build(6L).nextId();
        realTimeRead.setReadId(did);
        realTimeRead.setInfoType(infoType);
        realTimeRead.setRtType(rtType);
        realTimeRead.setReadUserId(userId);

        realTimeRead.setAppType(appType);

        realTimeRead.setReadUserName(realName);
        realTimeRead.setRtId(rid);
        realTimeRead.setReadTimes(1);
        realTimeRead.setIp(WebUtils.getIpAddress());
        realTimeRead.setUserId(realtimeUserId);
        realTimeRead.setUserName(realtimeUserName);

        realTimeRead.setCreater(realtimeUserName);
        realTimeRead.setCreateTime(Date.from(Instant.now()));

        try {
            newsReadMapper.insert(realTimeRead);
        } catch (DuplicateKeyException e) {
        }
    }


    /**
     * 获取最近一次阅读记录
     *
     * @param rid
     * @return
     */
    public RealTimeRead getRead(String appType ,long rid, long userId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        params.put("readUserId", userId);
        return newsReadMapper.get(params);
    }


    /**
     * 修改阅读次数
     *
     * @param rid
     * @param userId
     * @param readTimes
     */
    public void updRealTimeReadTimes(String appType , long rid, long userId, int readTimes) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        params.put("readUserId", userId);
        params.put("readTimes", readTimes);
        newsReadMapper.updateBy(params);
    }

    /**
     * 删除资讯阅读记录
     *
     * @param rid
     */
    public void delRealTimeRead(String appType , long rid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("rtId", rid);
        params.put("appType", appType);
        newsReadMapper.deleteBy(params);

        cacheCallback.del(Conts.RealTimeCount.READ.name(), appType, rid);
    }
}
