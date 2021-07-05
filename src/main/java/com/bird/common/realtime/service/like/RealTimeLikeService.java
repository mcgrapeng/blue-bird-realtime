package com.bird.common.realtime.service.like;

import com.google.common.collect.Maps;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTime;
import com.bird.common.realtime.domain.RealTimeComment;
import com.bird.common.realtime.domain.RealTimeLike;
import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.mapper.RealTimeLikeMapper;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.realtime.service.comment.RealTimeCommQueryService;
import com.bird.common.realtime.service.noti.RealTimeMessContext;
import com.bird.common.security.WebUtils;
import com.bird.common.web.thread.ContextAwarePoolExecutor;
import com.bird.common.web.utils.SnowflakeIdWorker;
import com.bird.sso.api.IUserQueryFutureService;
import com.bird.sso.api.domain.SSOUser;
import com.bird.sso.api.ex.SSOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/29 9:30
 */
@Service
@Slf4j
public class RealTimeLikeService {

    @Autowired
    private RealTimeLikeMapper newsLikeMapper;

    @Autowired
    private CacheCallback cacheCallback;

    @Autowired
    private ContextAwarePoolExecutor threadPoolTaskExecutor;


    @Autowired
    private RealTimeCommQueryService commService;

    @Reference(check = false, version = "1.0")
    private IUserQueryFutureService userQueryService;
    @Autowired
    private RealTimeFactory factory;

    @Autowired
    private RealTimeMessContext messContext;

    /**
     * 点赞、取消
     *
     * @param rid
     */
    @Transactional(rollbackFor = Exception.class)
    public void giveLike(long rid, String infoType) {
        Long likeUserId = WebUtils.getUserId();
        String appType = WebUtils.getAppType();
        if (isRealTimeLike(rid, likeUserId)) {
            threadPoolTaskExecutor.execute(() -> {
                delGiveLike(appType,rid, likeUserId);
            });
        } else {

            CompletableFuture<SSOUser> ssoUser = userQueryService.getFutureSSOUser(appType
                    , likeUserId);

            long rtUserId;
            String rtUserName;
            String rtType;
            if (!infoType.equalsIgnoreCase(InfoTypeEnum.COMM.name())) {
                RealTime realTime = factory.getRealTimeQueryService(infoType)
                        .findRealTimeByRtId(appType
                                , rid);
                rtUserId = realTime.getUserId();
                rtUserName = realTime.getUserName();
                rtType = realTime.getRtType();
            } else {
                RealTimeComment comment = commService.findCommentByCid(WebUtils.getAppType(),rid);
                rtUserId = comment.getCommentUserId();
                rtUserName = comment.getCommentUserName();
                rtType = comment.getRtType();
            }

            log.info(">>>>>>>>>>>>>>>>>>>>点赞>>>>>>>>>>>>>>>>>>>>>.likeUserId={}", likeUserId);

            RealTimeLike otNewsLike = new RealTimeLike();

            SSOUser user = null;
            try {
                user = ssoUser.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }

            if (ObjectUtils.isEmpty(user)) {
                throw SSOException.USER_NO_EXITS;
            }

            String realName = user.getRealName();
            String headImg = user.getHeadImg();
            String orgName = user.getOrgName();
            String username = user.getUserName();

            otNewsLike.setRtId(rid)
                    .setUserId(rtUserId)
                    .setUserName(rtUserName)
                    .setInfoType(infoType)
                    .setRtType(rtType)
                    .setLikeUserName(realName)
                    .setLikeUserId(likeUserId)
                    .setLikeUserHeadImg(headImg)
                    .setLikeOrgName(orgName)
                    .setAppType(appType)
                    .setLikeId(SnowflakeIdWorker.build(4L).nextId());

            otNewsLike.setCreater(username);
            otNewsLike.setCreateTime(Date.from(Instant.now()));

            insertLike(otNewsLike);

            messContext.notifyMessage(RealTimeMessContext.Strategy.LIKE.name()
                    , otNewsLike.getLikeId(), Boolean.TRUE);
        }
    }


    /**
     * 是否点赞
     *
     * @param rid
     * @param userId
     * @return
     */
    public boolean isRealTimeLike(long rid, long userId) {
        String appType = WebUtils.getAppType();
        return cacheCallback.hasKey(Conts.RealTimeCount.LIKE.name(), appType, rid, userId) ? Boolean.TRUE
                : Boolean.FALSE;
    }


    /**
     * 删除赞
     *
     * @param rid
     */
    public void delGiveLike(String appType , long rid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        newsLikeMapper.deleteBy(params);

        cacheCallback.delMatch(Conts.RealTimeCount.LIKE.name(), WebUtils.getAppType(), rid);
    }

    /**
     * 点赞总数
     *
     * @param rid
     * @return
     */
    public int likeTotal(long rid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("rtId", rid);

        params.put("appType", WebUtils.getAppType());
        return newsLikeMapper.count(params);
    }

    /**
     * 添加
     *
     * @param otNewsLike
     */
    private void insertLike(RealTimeLike otNewsLike) {
        try {
            newsLikeMapper.insert(otNewsLike);
        } catch (DuplicateKeyException e) {
            log.error(e.getMessage(), e);
        }

        String appType = WebUtils.getAppType();

        cacheCallback.incrLIKE(appType, otNewsLike.getRtId(), otNewsLike.getLikeUserId());
    }

    /**
     * 删除
     *
     * @param rid
     * @param likeUserId
     */
    private void delGiveLike(String appType , long rid, long likeUserId) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        params.put("likeUserId", likeUserId);
        newsLikeMapper.deleteBy(params);

        cacheCallback.delLIKE(appType, rid, likeUserId);
    }

}
