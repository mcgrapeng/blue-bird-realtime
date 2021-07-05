package com.bird.common.realtime.service;

import com.bird.common.realtime.RealTime;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;

public interface IRealTimeManageService {


    /**
     * 列表
     *
     * @param param
     * @param cond
     * @return
     */
    PageBean<RealTimeManageResult> page(PageParam param, Condition cond);


    /**
     * 新增
     *
     * @param title        标题
     * @param content      内容
     * @param infoType     信息流类型
     * @param rtType       资讯类型
     * @param secondRtType 资讯子类型
     * @param isRec        是否推荐
     */
    Long addRealTime(long orgId, String orgName, String parentOrgName,String title, String content, String firstImg , String infoType, String rtType, String secondRtType, String isRec);


    /**
     * 新增
     * @param realTime
     */
    void addRealTime(RealTime  realTime);


    /**
     * 修改
     *
     * @param rid     资讯ID
     * @param title   标题
     * @param content 内容
     * @param isRec   是否推荐
     */
    void updRealTime(long rid, String title, String content, String firstImg , String isRec, long orgId, String orgName);

    /**
     * 删除
     *
     * @param rid
     */
    void delRealTime(long rid);


    /**
     * 置顶
     *
     * @param rid
     */
    void topRealTime(long rid, String top);


    /**
     * 推荐
     *
     * @param rid
     */
    void recRealTime(long rid, String rec);


    /**
     * 关联视频
     *
     * @param rid
     * @param play
     * @param cover
     * @param duration
     * @param width
     * @param height
     */
    void updRealTimeVideo(String appType , long rid, String play, String cover, Integer duration, Integer width, Integer height);


    RealTimeManageResult getRealTime(Long rtId);


    void closeRealTime(long rid);



}
