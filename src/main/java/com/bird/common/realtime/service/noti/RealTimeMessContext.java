package com.bird.common.realtime.service.noti;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/13 12:33
 */
@Slf4j
@Service
public class RealTimeMessContext {


    @Autowired
    private RealTimeMessFactory messFactory;



    /**
     * 消息通知
     * @param strategy
     * @param notiId
     */
    @Async
    public void notifyMessage(String strategy, long notiId, boolean isPush) {
        messFactory.getMessBean(strategy).notifyMessage(notiId,isPush);
    }


    public enum Strategy {
        COMM,
        LIKE;
    }
}
