package com.bird.common.realtime.service;


import com.bird.common.web.mapper.Condition;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;

/**
 * @author 张朋
 * @version 1.0
 * @desc 信息流渲染引擎
 * @date 2020/6/5 13:24
 */
public interface IRealTimeService extends IRealTimeCommonService{


    /**
     * 信息流列表
     *
     * @param param
     * @param cond
     * @return
     */
    PageBean<RealTimeResult> execute(PageParam param, Condition cond);


    /**
     * 信息流详情
     *
     * @param rid
     * @return
     */
    RealTimeResult execute(String infoType, long rid) ;


    /**
     * 信息流详情
     * @param infoType
     * @param rid
     * @param userId
     * @return
     */
    RealTimeResult execute(String infoType,long rid, Long userId, String realName);


}
