package com.bird.common.realtime.service.noti;

import com.alibaba.fastjson.JSON;
import com.bird.common.realtime.domain.*;
import com.bird.common.realtime.ex.RealTimeBizException;
import com.bird.common.realtime.mapper.RealTimeMessMapper;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.realtime.service.comment.CommentResult;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/28 12:42
 */
@Component
public class RealTimeMessPersist {

    @Autowired
    private RealTimeMessMapper newsMessMapper;


    /**
     * 通知持久化
     *
     * @param realTime
     * @param messageContent
     * @param notiId
     * @param messageType
     * @param sendUserId
     * @param receUserId
     * @param sendUserName
     * @param sendUserHeadImg
     * @param sendOrgName
     */
    @Transactional(rollbackFor = Exception.class)
    protected void addMessage(RealTime realTime, RealTimeMessage.MessageContent messageContent
            , long notiId, String messageType
            , long sendUserId, long receUserId, String sendUserName, String sendUserHeadImg, String sendOrgName) {


        RealTimeResult.RealTimeResource realTimeResource;
        if (realTime instanceof RealTimeNews) {
            realTimeResource = RealTimeResult.RealTimeResource.of()
                    .setRtId(realTime.getRtId())
                    .setFirstImg(realTime.getFirstImg())
                    .setTitle(realTime.getTitle())
                    .setInfoType(realTime.getInfoType())
                    .setRtType(realTime.getRtType())
                    .setUserName(realTime.getUserName())
                    .setSendHeadImg(realTime.getUserHeadImg());
        } else if (realTime instanceof RealTimeMents) {
            realTimeResource = RealTimeResult.RealTimeResource.of()
                    .setRtId(realTime.getRtId())
                    .setFirstImg(realTime.getFirstImg())
                    .setTitle(StringUtils.isBlank(realTime.getTitle()) ? ((RealTimeMents) realTime).getContent()
                            : realTime.getTitle())
                    .setInfoType(realTime.getInfoType())
                    .setRtType(realTime.getRtType())
                    .setUserName(realTime.getUserName())
                    .setSendHeadImg(realTime.getUserHeadImg());
        } else {
            throw RealTimeBizException.PARAM_ILLEGAL;
        }

        RealTimeMess message = new RealTimeMess();
        message.setAppType(realTime.getAppType());
        message.setMessageId(SnowflakeIdWorker.build(9L).nextId());
        message.setNotiId(notiId);

        message.setInfoType(realTime.getInfoType());
        message.setRtId(realTime.getRtId());
        message.setRealTime(JSON.toJSONString(realTimeResource));

        message.setContent(JSON.toJSONString(messageContent));
        message.setMessageType(messageType);
        message.setReadStatus(PublicEnum.N.name());
        message.setNotiTime(Date.from(Instant.now()));

        message.setSendOrgName(sendOrgName);
        message.setSendUserId(sendUserId);
        message.setSendUserName(sendUserName);
        message.setSendHeadImg(sendUserHeadImg);

        message.setReceUserId(receUserId);

        message.setCreater(sendUserName);
        message.setCreateTime(Date.from(Instant.now()));

        newsMessMapper.insert(message);
    }


    /**
     * 通知持久化
     *
     * @param comment
     * @param notiContent
     * @param notiId
     * @param messageType
     * @param sendUserId
     * @param receUserId
     * @param sendUserName
     * @param sendUserHeadImg
     * @param sendOrgName
     */
    @Transactional(rollbackFor = Exception.class)
    protected void addMessage(RealTimeComment comment, String notiContent
            , long notiId, String messageType
            , long sendUserId, long receUserId, String sendUserName, String sendUserHeadImg, String sendOrgName) {

        CommentResult realTimeComment = CommentResult.of().setAppType(comment.getAppType())
                .setCid(comment.getCid())
                .setContent(comment.getContent())
                .setCommentUserId(comment.getCommentUserId())
                .setInfoType(comment.getInfoType())
                .setRtId(comment.getRtId());

        RealTimeMessage.MessageContent messageContent = RealTimeMessage.MessageContent.of();
        messageContent.setCommContent(notiContent);

        RealTimeMess message = new RealTimeMess();
        message.setAppType(comment.getAppType());
        message.setMessageId(SnowflakeIdWorker.build(9L).nextId());
        message.setNotiId(notiId);

        message.setInfoType(comment.getInfoType());
        message.setRtId(comment.getRtId());
        message.setRealTime(JSON.toJSONString(realTimeComment));

        message.setContent(JSON.toJSONString(messageContent));
        message.setMessageType(messageType);
        message.setReadStatus(PublicEnum.N.name());
        message.setNotiTime(Date.from(Instant.now()));

        message.setSendOrgName(sendOrgName);
        message.setSendUserId(sendUserId);
        message.setSendUserName(sendUserName);
        message.setSendHeadImg(sendUserHeadImg);

        message.setReceUserId(receUserId);

        message.setCreater(sendUserName);
        message.setCreateTime(Date.from(Instant.now()));

        newsMessMapper.insert(message);
    }
}
