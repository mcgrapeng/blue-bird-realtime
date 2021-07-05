package com.bird.common.realtime.domain;

import com.bird.common.web.mapper.BaseEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 资讯基础类
 */

@Slf4j
@Getter
public class RealTime extends BaseEntity {

    private String appType;

    /**
     * 资讯id
     */
    protected Long rtId;

    /**
     * 资讯类型
     */
    protected String rtType;


    /**
     * 资讯子类
     */
    private String secondRtType;

    /**
     * 信息流类型
     */
    protected String infoType;


    /**
     * 信息流发布人ID
     */
    protected Long userId;


    /**
     * 信息流发布人名称
     */
    protected String userName;


    protected String userHeadImg;


    /**信息流发布人所属组织*/
    protected Long orgId;

    protected String orgName;

    protected Long parentOrgId;

    protected String parentOrgName;

    /**信息流标题*/
    protected String title;

    /**首图*/
    protected String firstImg;

    /**是否置顶*/
    protected String isTop;

    /**是否推荐*/
    protected String isRec;

    protected Date topTime;


    public RealTime setAppType(String appType) {
        this.appType = appType;
        return this;
    }

    public RealTime setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }

    public RealTime setRtType(String rtType) {
        this.rtType = rtType;
        return this;
    }

    public RealTime setInfoType(String infoType) {
        this.infoType = infoType;
        return this;
    }

    public RealTime setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public RealTime setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RealTime setOrgId(Long orgId) {
        this.orgId = orgId;
        return this;
    }

    public RealTime setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public RealTime setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
        return this;
    }

    public RealTime setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
        return this;
    }

    public RealTime setTitle(String title) {
        this.title = title;
        return this;
    }

    public RealTime setFirstImg(String firstImg) {
        this.firstImg = firstImg;
        return this;
    }

    public RealTime setIsTop(String isTop) {
        this.isTop = isTop;
        return this;
    }

    public RealTime setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
        return this;
    }


    public RealTime setSecondRtType(String secondRtType) {
        this.secondRtType = secondRtType;
        return this;
    }

    public RealTime setIsRec(String isRec) {
        this.isRec = isRec;
        return this;
    }

    public RealTime setTopTime(Date topTime) {
        this.topTime = topTime;
        return this;
    }
}