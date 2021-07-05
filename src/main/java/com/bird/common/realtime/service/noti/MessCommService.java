package com.bird.common.realtime.service.noti;

import com.alibaba.fastjson.JSON;
import com.bird.common.realtime.domain.RealTime;
import com.bird.common.realtime.domain.RealTimeComment;
import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.enums.NotiEnum;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.realtime.service.comment.RealTimeCommQueryService;
import com.bird.common.security.WebUtils;
import com.bird.common.web.enums.AppEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 张朋
 * @version 1.0
 * @desc 评论通知
 * @date 2020/8/13 12:04
 */
@Slf4j
@Service
public class MessCommService implements IRealTimeMessService {

    @Autowired
    private RealTimeCommQueryService realTimeCommQueryService;

    @Autowired
    private RealTimeFactory factory;

    @Autowired
    private RealTimeMessPersist persist;

    @Autowired
    private RealTimeMessPushService pushService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notifyMessage(long cid, boolean isPush) {

        log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>进入评论通知逻辑");
        RealTimeComment comment = realTimeCommQueryService.findCommentByCid(WebUtils.getAppType(),cid);
        log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>comment={}", JSON.toJSONString(comment));
        if (ObjectUtils.isEmpty(comment)) {
            return;
        }

        RealTime realTime = factory.getRealTimeQueryService(comment.getRtInfoType())
                .findRealTimeByRtId(comment.getAppType(), comment.getRtId());

        log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>realTime={}", JSON.toJSONString(realTime));

        if (ObjectUtils.isEmpty(realTime)) {
            return;
        }


        if (comment.getPid() > 0L) {
            RealTimeComment commentParent = realTimeCommQueryService.findCommentByCid(comment.getAppType(),comment.getPid());

            log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>commentParent={}", JSON.toJSONString(commentParent));
            if (ObjectUtils.isEmpty(commentParent)) {
                return;
            }
            //评论自回复不发消息
            if (commentParent.getCommentUserId().intValue() == comment.getCommentUserId().intValue()) {
                return;
            }

            log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>通过校验");
            RealTimeMessage.MessageContent messageContent = RealTimeMessage.MessageContent.of();
            messageContent.setCommContent(comment.getContent());
            messageContent.setByReply(commentParent.getContent());
            messageContent.setByReplyUserName(commentParent.getCommentUserName());
            messageContent.setByReplyUserId(commentParent.getCommentUserId());


            persist.addMessage(realTime, messageContent
                    , cid, NotiEnum.COMM.name(), comment.getCommentUserId(), commentParent.getCommentUserId()
                    , comment.getCommentUserName(), comment.getCommentUserHeadImg(), comment.getCommentOrgName());

            if (isPush) {
                pushService.pushMessage(messageContent.getByReplyUserId(), AppEnum.APP_PB.getDesc(), "您有一条新的评论~");
            }
        } else {
            //资讯自评、自赞不发消息
            if (comment.getCommentUserId().longValue() == realTime.getUserId().longValue()) {
                return;
            }

            log.info(">>>>>>>>[notifyMessage]>>>>>>>>>>>>>>>>isSelf={}", comment.getCommentUserId().longValue() == realTime.getUserId().longValue());
            RealTimeMessage.MessageContent messageContent = RealTimeMessage.MessageContent.of();
            messageContent.setCommContent(comment.getContent());
            persist.addMessage(realTime, messageContent
                    , cid, NotiEnum.COMM.name(), comment.getCommentUserId(), realTime.getUserId()
                    , comment.getCommentUserName(), comment.getCommentUserHeadImg(), comment.getCommentOrgName());

            if (comment.getRtInfoType().equals(InfoTypeEnum.SELF.name())
                    && isPush) {
                pushService.pushMessage(realTime.getUserId(), AppEnum.APP_PB.getDesc(), "您有一条新的评论~");
            }
        }
    }
}
