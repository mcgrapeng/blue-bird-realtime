package com.bird.common.realtime.domain;

import lombok.Getter;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/29 12:56
 */
@Getter
public class RealTimeRead extends RealTime {


    private Long readId;
    /**
     * 浏览用户
     */
    private Long readUserId;

    private String readUserName;
    /**
     * 浏览人所属组织
     */
    private String readOrgName;


    private Integer readTimes;

    private String ip;


    public RealTimeRead setReadId(Long readId) {
        this.readId = readId;
        return this;
    }

    public RealTimeRead setReadUserId(Long readUserId) {
        this.readUserId = readUserId;
        return this;
    }

    public RealTimeRead setReadUserName(String readUserName) {
        this.readUserName = readUserName;
        return this;
    }

    public RealTimeRead setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }

    public RealTimeRead setRtType(String rtType) {
        this.rtType = rtType;
        return this;
    }

    public RealTimeRead setInfoType(String infoType) {
        this.infoType = infoType;
        return this;
    }

    public RealTimeRead setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public RealTimeRead setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RealTimeRead setReadOrgName(String readOrgName) {
        this.readOrgName = readOrgName;
        return this;
    }


    public RealTimeRead setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
        return this;
    }

    public RealTimeRead setIp(String ip) {
        this.ip = ip;
        return this;
    }
}
