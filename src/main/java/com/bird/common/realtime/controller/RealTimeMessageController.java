package com.bird.common.realtime.controller;

import com.bird.common.realtime.service.noti.RealTimeMessService;
import com.bird.common.realtime.service.noti.RealTimeMessage;
import com.bird.common.security.WebUtils;
import com.bird.common.web.RES;
import com.bird.common.web.enums.ResultEnum;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 张朋
 * @version 1.0
 * @desc  资讯消息通知
 * @date 2020/12/10 17:13
 */
@Slf4j
@RequestMapping(value = "/rt-noti", produces = "application/json;charset=UTF-8")
@Controller
public class RealTimeMessageController {

    @Autowired
    private RealTimeMessService realTimeMessService;

    /**
     * 我的消息提示列表
     *
     * @param type  通知模式  （点赞、评论）
     * @param pageParam
     * @return
     */
    @RequestMapping(value = "/list-message/{type}", method = RequestMethod.GET)
    @ResponseBody
    public RES<PageBean<RealTimeMessage>> listUserMesaage(@PathVariable String type, PageParam pageParam) {
        return RES.of(ResultEnum.处理成功.code, realTimeMessService.listMessage(pageParam
                , WebUtils.getUserId(), type), ResultEnum.处理成功.name());
    }


    /**
     * 未读消息数
     * @param type
     * @return
     */
    @RequestMapping(value = "/unread-message-count/{type}", method = RequestMethod.GET)
    @ResponseBody
    public RES<Integer> getUnReadMessageCount(@PathVariable String type) {
        log.info(">>>>>>>>>>>>>>>未读消息,type={}", type);
        return RES.of(ResultEnum.处理成功.code, realTimeMessService.unReadMessageCount(WebUtils.getUserId()
                , type), ResultEnum.处理成功.name());
    }

}
