package com.bird.common.realtime.service;

import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.service.ment.RealTimeMentManageService;
import com.bird.common.realtime.service.ment.RealTimeMentService;
import com.bird.common.realtime.service.news.RealTimeNewsManageService;
import com.bird.common.realtime.service.news.RealTimeNewsService;
import com.bird.common.web.ex.birdCommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/25 17:47
 */
@Slf4j
@Component
public class RealTimeFactory {


    @Autowired
    private RealTimeNewsService newsService;


    @Autowired
    private RealTimeMentService mentService;


    @Autowired
    private RealTimeNewsManageService realTimeNewsManageService;

    @Autowired
    private RealTimeMentManageService realTimeMentManageService;

    public IRealTimeService getInstance(String infoType) {

        if (infoType.equalsIgnoreCase(InfoTypeEnum.NEWS.name())) {
            return newsService;
        } else if (infoType.equalsIgnoreCase(InfoTypeEnum.SELF.name())) {
            return mentService;
        }
        log.error("######################信息流执行器不合法~###################");
        throw birdCommonException.NOT_FOUND;
    }


    public IRealTimeQueryService getRealTimeQueryService(String infoType) {
        if (infoType.equalsIgnoreCase(InfoTypeEnum.NEWS.name())) {
            return newsService;
        } else if (infoType.equalsIgnoreCase(InfoTypeEnum.SELF.name())) {
            return mentService;
        }
        log.error("######################信息流执行器不合法~###################");
        throw birdCommonException.NOT_FOUND;
    }


    public IRealTimeManageService getRealTimeManageService(String infoType) {
        if (infoType.equalsIgnoreCase(InfoTypeEnum.NEWS.name())) {
            return realTimeNewsManageService;
        } else if (infoType.equalsIgnoreCase(InfoTypeEnum.SELF.name())) {
            return realTimeMentManageService;
        }
        log.error("######################信息流执行器不合法~###################");
        throw birdCommonException.NOT_FOUND;
    }

}
