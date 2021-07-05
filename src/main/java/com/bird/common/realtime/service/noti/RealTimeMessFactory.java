package com.bird.common.realtime.service.noti;

import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.web.ex.birdCommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/13 14:30
 */
@Component
public class RealTimeMessFactory {


    @Autowired
    private MessCommService messCommService;

    @Autowired
    private MessLikeService messLikeService;

    /**
     * @param messageType  通知模式
     * @return
     */
    public IRealTimeMessService getMessBean(String messageType) {
        if (messageType.equals(RealTimeMessContext.Strategy.COMM.name())) {
            return messCommService;
        } else if (messageType.equals(RealTimeMessContext.Strategy.LIKE.name())) {
            return messLikeService;
        }
        throw birdCommonException.NOT_FOUND;
    }

}
