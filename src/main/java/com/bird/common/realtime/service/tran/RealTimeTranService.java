package com.bird.common.realtime.service.tran;

import com.alibaba.fastjson.JSON;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.domain.RealTime;
import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.enums.MentsTypeEnum;
import com.bird.common.realtime.enums.MetaTypeEnum;
import com.bird.common.realtime.enums.RtTypeEnum;
import com.bird.common.realtime.mapper.RealTimeTranMapper;
import com.bird.common.realtime.service.RealTimeAsyncService;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.realtime.service.ment.RealTimeMentService;
import com.bird.common.security.WebUtils;
import com.bird.common.web.security.JWTHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/29 21:17
 */
@Slf4j
@Service
public class RealTimeTranService {

    @Autowired
    private RealTimeTranMapper newsTranMapper;

    @Autowired
    private RealTimeMentService momentsService;

    @Autowired
    private CacheCallback cacheCallback;

    @Autowired
    private RealTimeFactory factory;

    @Autowired
    private RealTimeAsyncService realTimeAsyncService;


    /**
     * 转发资讯至党建圈
     *
     * @param rid
     * @param content
     */
    @Transactional(rollbackFor = Exception.class)
    public void transmitRealTime(String infoFlowType, long rid, String title, String content) {
        String appType = WebUtils.getAppType();
        RealTimeResult.RealTimeResource momentsRealTimeResource = new RealTimeResult.RealTimeResource();

        RealTime realTime = factory.getRealTimeQueryService(infoFlowType).findRealTimeByRtId(appType, rid);
        momentsRealTimeResource.setFirstImg(realTime.getFirstImg());
        momentsRealTimeResource.setTitle(realTime.getTitle());
        momentsRealTimeResource.setRtId(realTime.getRtId());
        momentsRealTimeResource.setInfoType(infoFlowType);
        momentsRealTimeResource.setRtType(realTime.getRtType());
        momentsRealTimeResource.setUserName(realTime.getUserName());

        momentsService.addMoments(null, InfoTypeEnum.SELF.name(), RtTypeEnum.MENT.name(), MetaTypeEnum.CONTENT.name(), MentsTypeEnum.FOR.name()
                , ments -> {
                    if (ObjectUtils.isNotEmpty(momentsRealTimeResource)) {
                        ments.setFirstImg(momentsRealTimeResource.getFirstImg());
                        ments.setRtResources(JSON.toJSONString(momentsRealTimeResource));
                    }
                }, title, content);

        cacheCallback.incrTRAN(appType, rid);


        realTimeAsyncService.addRealTimeTran(appType,WebUtils.getUserId(),realTime.getRtId()
                , realTime.getUserId(), realTime.getUserName(), infoFlowType, realTime.getRtType());
    }


    public int tranTotal(long rid) {
        return newsTranMapper.sumTranTimes(WebUtils.getAppType(), rid);
    }


}
