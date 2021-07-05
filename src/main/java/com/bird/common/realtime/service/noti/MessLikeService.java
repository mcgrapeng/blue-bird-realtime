package com.bird.common.realtime.service.noti;

import com.alibaba.fastjson.JSON;
import com.bird.common.realtime.domain.RealTime;
import com.bird.common.realtime.domain.RealTimeComment;
import com.bird.common.realtime.domain.RealTimeLike;
import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.enums.NotiEnum;
import com.bird.common.realtime.enums.RtTypeEnum;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.realtime.service.comment.RealTimeCommQueryService;
import com.bird.common.realtime.service.like.RealTimeLikeQueryService;
import com.bird.common.security.WebUtils;
import com.bird.common.web.enums.AppEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 张朋
 * @version 1.0
 * @desc 点赞通知
 * @date 2020/8/13 12:16
 */
@Slf4j
@Service
public class MessLikeService implements IRealTimeMessService {


    @Autowired
    private RealTimeLikeQueryService realTimeLikeQueryService;

    @Autowired
    private RealTimeCommQueryService realTimeCommQueryService;

    @Autowired
    private RealTimeMessPersist persist;

    @Autowired
    private RealTimeFactory factory;

    @Autowired
    private RealTimeMessPushService pushService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notifyMessage(long likeId, boolean isPush) {
        log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>进入点赞通知逻辑");
        RealTimeLike like = realTimeLikeQueryService.findRealTimeLikeByLikeId(WebUtils.getAppType(),likeId);
        if (ObjectUtils.isEmpty(like)) {
            return;
        }
        log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>like={}", JSON.toJSONString(like));
        Long receUserId;
        if (like.getInfoType().equals(InfoTypeEnum.COMM.name())) {

            RealTimeComment comment = realTimeCommQueryService.findCommentByCid(WebUtils.getAppType(),like.getRtId());
            if (ObjectUtils.isEmpty(comment)) {
                return;
            }

            //评论自赞不发消息
            if (like.getLikeUserId().intValue() == comment.getCommentUserId().intValue()) {
                return;
            }

            persist.addMessage(comment, StringUtils.join("点赞了你的", RtTypeEnum.getDesc(like.getRtType()))
                    , likeId, NotiEnum.LIKE.name(), like.getLikeUserId(), comment.getCommentUserId()
                    , like.getLikeUserName(), like.getLikeUserHeadImg(), like.getLikeOrgName());

            receUserId = comment.getCommentUserId();
        } else {

            RealTime realTime = factory.getRealTimeQueryService(like.getInfoType())
                    .findRealTimeByRtId(like.getAppType(), like.getRtId());

            log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>realTime={}", JSON.toJSONString(realTime));
            if (ObjectUtils.isEmpty(realTime)) {
                return;
            }

            //资讯自赞不发消息
            if (like.getLikeUserId().longValue() == realTime.getUserId().longValue()) {
                return;
            }

            log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>通过校验");
            RealTimeMessage.MessageContent messageContent = RealTimeMessage.MessageContent.of();
            messageContent.setLikeContent(StringUtils.join("点赞了你的", RtTypeEnum.getDesc(like.getRtType())));

            persist.addMessage(realTime, messageContent
                    , likeId, NotiEnum.LIKE.name(), like.getLikeUserId(), realTime.getUserId()
                    , like.getLikeUserName(), like.getLikeUserHeadImg(), like.getLikeOrgName());

            receUserId = realTime.getUserId();
        }

        if (like.getInfoType().equals(InfoTypeEnum.COMM.name())
                || like.getInfoType().equals(InfoTypeEnum.SELF.name())) {
            if (isPush) {
                pushService.pushMessage(receUserId, AppEnum.APP_PB.getDesc(), "您有一个新的点赞~");
            }
        }
    }
}




