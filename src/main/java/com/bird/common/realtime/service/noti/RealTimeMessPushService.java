package com.bird.common.realtime.service.noti;

import com.google.common.collect.Lists;
import com.bird.common.web.ex.birdCommonException;
import com.bird.common.web.push.Push;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/3/24 12:00
 */
@Slf4j
@Component
public class RealTimeMessPushService {

    @Autowired
    private Push push;

    /**
     * 消息推送
     *
     * @param receUserId
     * @param title
     * @param content
     */
    public void pushMessage(Long receUserId, String title, String content) {
        log.info("##########开始推送###########ReceUserId={}", receUserId);
        if (ObjectUtils.isEmpty(receUserId)) {
            throw birdCommonException.PARAM_ILLEGAL;
        }
        //推送
        push.sendMsg(Lists.newArrayList(receUserId), title, content);
    }
}
