package com.bird.common.realtime.domain;

import com.bird.common.web.mapper.BaseEntity;
import lombok.Getter;

import java.util.Date;

/**
 * @author 张朋
 * @version 1.0
 * @desc 资讯消息
 * @date 2020/6/30 11:21
 */
@Getter
public class RealTimeMess extends BaseEntity {


    private String appType;

    /**
     * 消息ID
     */
    private Long messageId;


    /**
     * 消息关联业务标识
     */
    private Long notiId;

    /**
     * 消息接收人
     */
    private Long receUserId;

    /**
     * 接收人发布的资讯，资讯ID
     */
    private Long rtId;

    /**
     * 接收人发布的资讯，资讯类型(新闻、圈子)
     */
    private String rtType;

    /**
     * 接收人发布的资讯，信息流类型(资讯、圈子、评论)
     */
    private String infoType;


    /**
     * 消息发送人
     */
    private Long sendUserId;
    private String sendUserName;
    private String sendOrgName;
    private String sendHeadImg;


    /**
     * 通知类型  点赞、评论
     */
    private String messageType;

    /***阅读状态*/
    private String readStatus;

    /**
     * 通知时间
     */
    private Date notiTime;

    /**
     * 消息体： json格式、评论、点赞内容
     */
    private String content;

    /**
     * 资讯引用（标题）、json格式
     */
    private String realTime;


    public RealTimeMess setAppType(String appType) {
        this.appType = appType;
        return this;
    }

    public RealTimeMess setNotiId(Long notiId) {
        this.notiId = notiId;
        return this;
    }

    public RealTimeMess setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
        return this;
    }

    public RealTimeMess setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
        return this;
    }

    public RealTimeMess setSendOrgName(String sendOrgName) {
        this.sendOrgName = sendOrgName;
        return this;
    }

    public RealTimeMess setContent(String content) {
        this.content = content;
        return this;
    }

    public RealTimeMess setRealTime(String realTime) {
        this.realTime = realTime;
        return this;
    }

    public RealTimeMess setReceUserId(Long receUserId) {
        this.receUserId = receUserId;
        return this;
    }

    public RealTimeMess setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public RealTimeMess setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }

    public RealTimeMess setRtType(String rtType) {
        this.rtType = rtType;
        return this;
    }

    public RealTimeMess setInfoType(String infoType) {
        this.infoType = infoType;
        return this;
    }

    public RealTimeMess setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public RealTimeMess setSendHeadImg(String sendHeadImg) {
        this.sendHeadImg = sendHeadImg;
        return this;
    }

    public RealTimeMess setReadStatus(String readStatus) {
        this.readStatus = readStatus;
        return this;
    }

    public RealTimeMess setNotiTime(Date notiTime) {
        this.notiTime = notiTime;
        return this;
    }
}
