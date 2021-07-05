package com.bird.common.realtime.controller;

import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.tools.video.VideoCallbackController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/3/29 17:00
 */
@Slf4j
@RequestMapping(value = "/rt-video")
@Controller
public class RealTimeVideoController extends VideoCallbackController {


    @Autowired
    private RealTimeFactory factory;


    @Override
    public void execute(String appType ,Long rid, String infoType, String play, String cover, Integer duration, Integer width, Integer height) {
        log.info(">>>>>>>>>>execute>>>>>>>>>>视频数据入库开始>>>>>>>>>>>>>>>>>>");
        factory.getRealTimeManageService(infoType).updRealTimeVideo(appType,rid, play
                , cover, duration, width, height);
    }
}
