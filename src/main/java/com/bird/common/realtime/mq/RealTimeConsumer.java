package com.bird.common.realtime.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bird.common.mq.message.CommonMessage;
import com.bird.common.realtime.RealTime;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.sso.api.domain.SSOUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/6/22 15:14
 */
@Component
@Slf4j
public class RealTimeConsumer {

    @Autowired
    private RealTimeFactory factory;

    @KafkaListener(
            topics = {"APP_REALTIME_REALTIME_PUBLISH"},
            groupId = "realtime",
            concurrency = "2"
    )
    public void receiveMessageTopic_publish(CommonMessage message, Acknowledgment acknowledgment) {
        log.info("#############[receiveMessageTopic_publish]####################receive a message from kafka server start");
        log.info("\r\n receive a message from kafka server:{}", JSON.toJSONString(message));
        CommonMessage.Payload payload = message.getPayload();
        if (ObjectUtils.isEmpty(payload)) {
            acknowledgment.acknowledge();
        } else {
            RealTime realTime = JSONObject.parseObject(payload.toJSON(), RealTime.class);
            if (StringUtils.isBlank(realTime.getAppType())) {
                acknowledgment.acknowledge();
            } else {
                try {
                    factory.getRealTimeManageService(realTime.getInfoType()).addRealTime(realTime);
                } catch (DuplicateKeyException var6) {
                    return;
                }

                log.info("receive a message from kafka server end");
                acknowledgment.acknowledge();
            }
        }
    }
}
