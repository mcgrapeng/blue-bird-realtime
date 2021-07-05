package com.bird.common.realtime.domain;

import lombok.Getter;

@Getter
public class RealTimeTran extends RealTime {


    private Long tranId;

    private Long tranUserId;

    private String tranUserName;

    private String tranUserHeadImg;

    /**
     * 转发人所属组织
     */
    private String tranOrgName;

    private Integer tranTimes;

    private String ip;


    public RealTimeTran setTranId(Long tranId) {
        this.tranId = tranId;
        return this;
    }

    public RealTimeTran setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public RealTimeTran setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }


    public RealTimeTran setRtType(String rtType) {
        this.rtType = rtType;
        return this;
    }

    public RealTimeTran setTranUserId(Long tranUserId) {
        this.tranUserId = tranUserId;
        return this;
    }

    public RealTimeTran setTranUserName(String tranUserName) {
        this.tranUserName = tranUserName;
        return this;
    }

    public RealTimeTran setInfoType(String infoType) {
        this.infoType = infoType;
        return this;
    }

    public RealTimeTran setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RealTimeTran setTranOrgName(String tranOrgName) {
        this.tranOrgName = tranOrgName;
        return this;
    }

    public RealTimeTran setTranTimes(Integer tranTimes) {
        this.tranTimes = tranTimes;
        return this;
    }

    public RealTimeTran setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public RealTimeTran setTranUserHeadImg(String tranUserHeadImg) {
        this.tranUserHeadImg = tranUserHeadImg;
        return this;
    }
}