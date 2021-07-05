package com.bird.common.realtime.domain;

import com.bird.common.web.mapper.BaseEntity;
import lombok.Getter;

@Getter
public class RealTimeComment extends BaseEntity {

    private String appType;
    /**
     * 评论ID
     */
    private Long cid;

    private Long pid;

    private Long rootId;

    private String isClose;

    /**
     * 信息流类型
     */
    private String infoType;

    /**
     * 信息流(新闻、圈子、评论)发布人ID
     */
    private Long userId;
    /**
     * 信息流(新闻、圈子、评论)发布人名称
     */
    private String userName;

    /**
     * 评论人id
     */
    private Long commentUserId;

    private String commentUserName;

    private String commentUserHeadImg;

    private String commentOrgName;

    /**
     * 评论内容
     */
    private String content;


    /**
     * 信息流id
     */
    private Long rtId;
    /**
     * 资讯类型
     */
    private String rtType;

    /**
     * 资讯信息流类型
     */
    private String rtInfoType;


    public RealTimeComment(String isClose) {
        this.isClose = isClose;
    }

    public RealTimeComment setCommentUserId(Long commentUserId) {
        this.commentUserId = commentUserId;
        return this;
    }

    public RealTimeComment setContent(String content) {
        this.content = content;
        return this;
    }

    public RealTimeComment setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    public RealTimeComment setPid(Long pid) {
        this.pid = pid;
        return this;
    }

    public RealTimeComment setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
        return this;
    }

    public RealTimeComment setRootId(Long rootId) {
        this.rootId = rootId;
        return this;
    }


    public RealTimeComment setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }

    public RealTimeComment setInfoType(String infoType) {
        this.infoType = infoType;
        return this;
    }

    public RealTimeComment setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public RealTimeComment setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RealTimeComment setCommentOrgName(String commentOrgName) {
        this.commentOrgName = commentOrgName;
        return this;
    }

    public RealTimeComment setCommentUserHeadImg(String commentUserHeadImg) {
        this.commentUserHeadImg = commentUserHeadImg;
        return this;
    }

    public RealTimeComment setAppType(String appType) {
        this.appType = appType;
        return this;
    }


    public RealTimeComment setRtType(String rtType) {
        this.rtType = rtType;
        return this;
    }

    public RealTimeComment setRtInfoType(String rtInfoType) {
        this.rtInfoType = rtInfoType;
        return this;
    }
}