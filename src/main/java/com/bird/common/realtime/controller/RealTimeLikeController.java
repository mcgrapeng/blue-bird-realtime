package com.bird.common.realtime.controller;

import com.bird.common.realtime.service.like.RealTimeLikeForm;
import com.bird.common.realtime.service.like.RealTimeLikeService;
import com.bird.common.web.RES;
import com.bird.common.web.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/10 17:13
 */
@Slf4j
@RequestMapping(value = "/rt-like", produces = "application/json;charset=UTF-8")
@Controller
public class RealTimeLikeController {


    @Autowired
    private RealTimeLikeService realTimeLikeService;


    /**
     * 点赞/取消
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RES<String> giveLike(@RequestBody @Validated RealTimeLikeForm req) {
        realTimeLikeService.giveLike(req.getRtId(), req.getInfoType());
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }
}
