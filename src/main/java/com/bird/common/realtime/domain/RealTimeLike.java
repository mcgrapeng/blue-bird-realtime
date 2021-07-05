package com.bird.common.realtime.domain;

import com.bird.common.web.mapper.BaseEntity;
import lombok.Getter;

@Getter
public class RealTimeLike extends BaseEntity {

    private String appType;

    private Long likeId;


    /**
     * 资讯id
     */
    private Long rtId;
    /**
     * 信息流类型
     */
    private String infoType;

    /**
     * 资讯类型
     */
    private String rtType;

    /**
     * 信息流发布人ID
     */
    private Long userId;
    /**
     * 信息流发布人名称
     */
    private String userName;


    /**
     * 点赞用户
     */
    private Long likeUserId;

    /**
     * 点赞用户名
     */
    private String likeUserName;

    /**
     * 点赞用户头像
     */
    private String likeUserHeadImg;

    /**
     * 点赞人所属组织
     */
    private String likeOrgName;


    public RealTimeLike setAppType(String appType) {
        this.appType = appType;
        return this;
    }

    public RealTimeLike setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public RealTimeLike setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }

    public RealTimeLike setLikeUserId(Long likeUserId) {
        this.likeUserId = likeUserId;
        return this;
    }

    public RealTimeLike setLikeUserName(String likeUserName) {
        this.likeUserName = likeUserName;
        return this;
    }


    public RealTimeLike setLikeId(Long likeId) {
        this.likeId = likeId;
        return this;
    }

    public RealTimeLike setInfoType(String infoType) {
        this.infoType = infoType;
        return this;
    }

    public RealTimeLike setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RealTimeLike setLikeOrgName(String likeOrgName) {
        this.likeOrgName = likeOrgName;
        return this;
    }

    public RealTimeLike setLikeUserHeadImg(String likeUserHeadImg) {
        this.likeUserHeadImg = likeUserHeadImg;
        return this;
    }

    public RealTimeLike setRtType(String rtType) {
        this.rtType = rtType;
        return this;
    }
}