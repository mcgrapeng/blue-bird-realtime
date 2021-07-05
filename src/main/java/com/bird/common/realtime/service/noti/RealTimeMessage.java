package com.bird.common.realtime.service.noti;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.web.ex.birdCommonException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author 张朋
 * @version 1.0
 * @desc 消息体
 * @date 2021/3/23 11:42
 */
@Slf4j
@Getter
public class RealTimeMessage {

    private String appType;
    /**
     * 消息ID
     */
    private Long messageId;
    /**
     * 业务标识   cid,likeId
     */
    private Long notiId;
    /**
     * 通知类型 点赞、评论
     */
    private String messageType;

    /**
     * 消息发送方
     */
    private Long    sendUserId;
    private String  sendUserName;
    private String  sendOrgName;
    private String  sendHeadImg;
    @JSONField(format = "MM-dd HH:mm")
    private Date    sendTime;
    /**
     * 消息接收方
     */
    private Long    receUserId;
    /**
     * 消息实体
     */
    private MessageContent content;
    /**
     * 资讯实体
     */
    private RealTimeResult.RealTimeResource realTime;


    public RealTimeMessage(MessageBuilder builder) {
        this.notiId = builder.notiId;
        this.messageId = builder.messageId;
        this.sendUserId = builder.sendUserId;
        this.sendUserName = builder.sendUserName;
        this.sendOrgName = builder.sendOrgName;
        this.messageType = builder.messageType;
        this.content = builder.content;
        this.appType = builder.appType;
        this.realTime = builder.realTime;
    }


    private RealTimeMessage() {
    }


    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }

    public void setRealTime(RealTimeResult.RealTimeResource realTime) {
        this.realTime = realTime;
    }

    public void setSendHeadImg(String sendHeadImg) {
        this.sendHeadImg = sendHeadImg;
    }

    public void setReceUserId(Long receUserId) {
        this.receUserId = receUserId;
    }



    /**
     * 资讯通知
     */
    @Setter
    @Getter
    public static class MessageContent {
        /**
         * 回复，评论内容
         */
        private String commContent;

        /**
         * 被回复（发表）
         */
        private String byReply;
        /**
         * 评论人
         */
        private String byReplyUserName;

        /**
         * 评论人ID
         */
        private Long byReplyUserId;


        /**
         * 点赞文案
         */
        private String likeContent;


        public static MessageContent of(){
            return new MessageContent();
        }
    }


    /**
     * 消息构建器
     */
    public static class MessageBuilder {
        private String appType;
        private Long   notiId;
        private Long   messageId;
        private Long   sendUserId;
        private String sendUserName;
        private String sendOrgName;

        private Long   receUserId;

        private String messageType;

        private MessageContent content;

        private RealTimeResult.RealTimeResource realTime;

        public MessageBuilder setNotiId(Long notiId) {
            this.notiId = notiId;
            return MessageBuilder.this;
        }

        public MessageBuilder setSendUserId(Long sendUserId) {
            this.sendUserId = sendUserId;
            return MessageBuilder.this;
        }

        public MessageBuilder setSendUserName(String sendUserName) {
            this.sendUserName = sendUserName;
            return MessageBuilder.this;
        }

        public MessageBuilder setSendOrgName(String sendOrgName) {
            this.sendOrgName = sendOrgName;
            return MessageBuilder.this;
        }

        public MessageBuilder setMessageId(Long messageId) {
            this.messageId = messageId;
            return MessageBuilder.this;
        }

        public MessageBuilder setMessageType(String messageType) {
            this.messageType = messageType;
            return MessageBuilder.this;
        }

        public MessageBuilder setContent(MessageContent content) {
            this.content = content;
            return MessageBuilder.this;
        }

        public MessageBuilder setReceUserId(Long receUserId) {
            this.receUserId = receUserId;
            return MessageBuilder.this;
        }

        public MessageBuilder setRealTime(RealTimeResult.RealTimeResource realTime) {
            this.realTime = realTime;
            return MessageBuilder.this;
        }

        public MessageBuilder setAppType(String appType) {
            this.appType = appType;
            return MessageBuilder.this;
        }

        public static MessageBuilder of(){
            return new MessageBuilder();
        }


        public RealTimeMessage build() {
           /* if (ObjectUtils.isEmpty(this.sendUserId)
                    || StringUtils.isBlank(this.appType)
                    || ObjectUtils.isEmpty(this.notiId)
                    || StringUtils.isBlank(this.sendUserName)
                    || ObjectUtils.isEmpty(this.receUserId)
                    || StringUtils.isBlank(this.sendOrgName)
                    || ObjectUtils.isEmpty(this.messageId)
                    || StringUtils.isBlank(this.messageType)
                    || ObjectUtils.isEmpty(this.content)
                    || ObjectUtils.isEmpty(this.realTime)) {

                log.error("###############notiId={},sendUserId={}," +
                                "sendUserName={},sendOrgName={}," +
                                "messageId={},messageType={},content={}"
                        , notiId, sendUserId, sendUserName, sendOrgName, messageId
                        , messageType, JSON.toJSONString(content));

                throw birdCommonException.FAILED_500;
            }*/
            return new RealTimeMessage(MessageBuilder.this);
        }
    }
}
