package com.bird.common.realtime.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Maps;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTime;
import com.bird.common.realtime.domain.RealTimeContent;
import com.bird.common.realtime.mapper.RealTimeContentMapper;
import com.bird.common.realtime.service.like.RealTimeLikeService;
import com.bird.common.realtime.service.read.RealTimeReadService;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.mapper.BaseService;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.mapper.Direction;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.thread.ContextAwarePoolExecutor;
import com.bird.common.web.utils.WebUtils;
import com.bird.sso.api.IUserQueryFutureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/24 14:58
 */
@Slf4j
public abstract class RealTimeService<T extends RealTime> extends BaseService<Long, T> implements IRealTimeService {

    @Autowired
    protected ContextAwarePoolExecutor threadPoolTaskExecutor;

    @Autowired
    protected CacheCallback cacheCallback;

    @Autowired
    protected RealTimeReadService realTimeReadService;

    @Autowired
    protected RealTimeLikeService realTimeLikeService;

    @Autowired
    protected RealTimeTools realTimeTools;

    @Autowired
    protected RealTimeContentMapper contentMapper;


    @Autowired
    protected RealTimeAsyncService realTimeAsyncService;


    @Reference(check = false, version = "1.0")
    protected IUserQueryFutureService userQueryService;


    protected abstract RealTimeResult getRealTime(String appType, long rtId);


    protected abstract PageBean<RealTimeResult> page(PageParam param, Condition cond);


    @SentinelResource(value = "execute")
    @Override
    public PageBean<RealTimeResult> execute(PageParam param, Condition cond) {
        if (null == cond) {
            cond = Condition.of();
        }
        cond.addSort("create_time", Direction.DESC.name());
        return page(param, cond);
    }

    @Override
    public RealTimeResult execute(String infoType, long rtId) {
        Long userId = com.bird.common.security.WebUtils.getUserId();
        String realName = com.bird.common.security.WebUtils.getRealName();

        if(ObjectUtils.isEmpty(userId) || userId == -1){
            realName = "游客";
        }
        return execute(infoType, rtId, userId, realName);
    }

    @SentinelResource(value = "execute_list")
    @Override
    public RealTimeResult execute(String infoType, long rid, Long userId, String realName) {
        String appType = WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP);

        RealTimeResult realTime = getRealTime(appType, rid);

        Long read = cacheCallback.incrGet(Conts.RealTimeCount.READ.name()
                , appType, rid);
        Long like = cacheCallback.get(Conts.RealTimeCount.LIKE.name(), appType
                , rid);
        Long comm = cacheCallback.get(Conts.RealTimeCount.COMM.name(), appType
                , rid);
        Long tran = cacheCallback.get(Conts.RealTimeCount.TRAN.name(), appType
                , rid);

        realTime.setReadNum(realTimeTools.getRealTimeOperationNum(read));
        realTime.setCommNum(realTimeTools.getRealTimeOperationNum(comm));
        realTime.setLikeNum(realTimeTools.getRealTimeOperationNum(like));
        realTime.setTranNum(realTimeTools.getRealTimeOperationNum(tran));

        if (userId > -1) {
            realTime.setIsOwner(String.valueOf(userId).equals(String.valueOf(realTime.getUserId()))
                    ? PublicEnum.Y.name() : PublicEnum.N.name());
            realTime.setIsLike(realTimeLikeService.isRealTimeLike(rid, userId)
                    ? PublicEnum.Y.name() : PublicEnum.N.name());

            realTimeAsyncService.addRealTimeRead(appType,infoType, realTime.getRtType(), rid, userId, realName, realTime.getUserId(), realTime.getUserName());

        } else {
            realTime.setIsOwner(PublicEnum.N.name());
            realTime.setIsLike(PublicEnum.N.name());
        }
        return realTime;
    }


    protected RealTimeContent findContentByRtId(long rtId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("rtId", rtId);
        return contentMapper.get(params);
    }


    protected List<RealTimeContent> listContentByRids(List<Long> rids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("rids", rids);
        return contentMapper.list(params);
    }


    /**
     * 删除资讯内容
     *
     * @param rid
     */
    public void delRealTimeContent(long rid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("rtId", rid);
        contentMapper.deleteBy(params);
    }
}
