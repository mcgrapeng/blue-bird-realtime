package com.bird.common.realtime.service.comment;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;

import java.util.Date;

/**
 * @author 张朋
 * @version 1.0
 * @desc  评论返回实体
 * @date 2020/12/10 12:51
 */
@Getter
public class CommentResult {


    private String appType;

    /**
     * 评论ID
     */
    private Long cid;

    /**
     * 父级ID
     */
    private Long pid;

    /**
     * 评论详情ID
     */
    private Long rootId;

    /**
     * 资讯ID
     */
    private Long rtId;

    /**
     * 评论用户ID
     */
    private Long commentUserId;

    /**
     * 评论用户名
     */
    private String commentUserName;

    /**
     * 评论用户头像
     */
    private String commentUserHeadImg;

    /**
     * 评论用户所属组织
     */
    private String commentOrgName;

    /**
     * 评论时间
     */
    @JSONField(format = "MM-dd HH:mm")
    private Date commentTime;


    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date commentFullTime;

    /**
     * 评论内容
     */
    private String content;
    /**
     * 信息流类型
     */
    private String infoType;

    /**
     * 回复数
     */
    private Long revNum;

    private Long likeNum;

    private String isOwner;

    private String isLike;

    /**
     * 被回复（发表人）
     */
    private String repliedCommentUserName;


    private String isClose;


    private CommentResult() {
    }

    public static CommentResult of() {
        return new CommentResult();
    }


    public CommentResult setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
        return this;
    }


    public CommentResult setRevNum(Long revNum) {
        this.revNum = revNum;
        return this;
    }


    public CommentResult setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    public CommentResult setPid(Long pid) {
        this.pid = pid;
        return this;
    }

    public CommentResult setAppType(String appType) {
        this.appType = appType;
        return this;
    }

    public CommentResult setCommentUserId(Long commentUserId) {
        this.commentUserId = commentUserId;
        return this;
    }

    public CommentResult setIsOwner(String isOwner) {
        this.isOwner = isOwner;
        return this;
    }

    public CommentResult setContent(String content) {
        this.content = content;
        return this;
    }

    public CommentResult setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
        return this;
    }

    public CommentResult setRepliedCommentUserName(String repliedCommentUserName) {
        this.repliedCommentUserName = repliedCommentUserName;
        return this;
    }

    public CommentResult setCommentUserHeadImg(String commentUserHeadImg) {
        this.commentUserHeadImg = commentUserHeadImg;
        return this;
    }

    public CommentResult setIsLike(String isLike) {
        this.isLike = isLike;
        return this;
    }

    public CommentResult setRootId(Long rootId) {
        this.rootId = rootId;
        return this;
    }

    public CommentResult setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
        return this;
    }

    public CommentResult setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }

    public CommentResult setInfoType(String infoType) {
        this.infoType = infoType;
        return this;
    }

    public CommentResult setCommentOrgName(String commentOrgName) {
        this.commentOrgName = commentOrgName;
        return this;
    }

    public CommentResult setCommentFullTime(Date commentFullTime) {
        this.commentFullTime = commentFullTime;
        return this;
    }

    public CommentResult setIsClose(String isClose) {
        this.isClose = isClose;
        return this;
    }
}
