package com.bird.common.realtime.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bird.common.realtime.domain.RealTimeMents;
import com.bird.common.realtime.enums.MentsTypeEnum;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.realtime.service.ment.IRealTimeMentCallback;
import com.bird.common.realtime.service.ment.RealTimeMentForm;
import com.bird.common.realtime.service.ment.RealTimeMentService;
import com.bird.common.realtime.service.tran.RealTimeTranForm;
import com.bird.common.realtime.service.tran.RealTimeTranService;
import com.bird.common.web.RES;
import com.bird.common.web.annotation.RequestLimit;
import com.bird.common.web.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/10 17:13
 */
@Slf4j
@RequestMapping(value = "/rt-ment", produces = "application/json;charset=UTF-8")
@Controller
public class RealTimeMentController {


    @Autowired
    private RealTimeMentService momentsService;


    @Autowired
    private RealTimeTranService realTimeTranService;


    /**
     * 发布帖子、圈子等
     *
     * @param momentsReq
     * @return
     */
    @RequestLimit(count = 2, time = 1000)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RES<String> addMoments(@RequestBody @Validated
                                          RealTimeMentForm momentsReq) {

        momentsService.addMoments(momentsReq.getRtId(), momentsReq.getInfoType(), momentsReq.getRtType()
                , momentsReq.getMetaType()
                , MentsTypeEnum.NEW.name()
                , ments -> {
                    List<RealTimeResult.FileResource> fileResources = momentsReq.getFResources();
                    if (CollectionUtils.isNotEmpty(fileResources)) {
                        ments.setFirstImg(fileResources.get(0).getUrl());
                        ments.setPiResources(JSON.toJSONString(fileResources));
                    }
                }
                , momentsReq.getTitle()
                , momentsReq.getContent());
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }


    /**
     * 转发资讯
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/tran", method = RequestMethod.POST)
    @ResponseBody
    public RES<String> transmitRealTime(@RequestBody RealTimeTranForm form) {
        realTimeTranService.transmitRealTime(form.getInfoType(), form.getRtId(), form.getTitle(), form.getContent());
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }

    /**
     * 删除帖子、圈子等
     *
     * @param mid
     * @return
     */
    @RequestMapping(value = "/delete/{mid}", method = RequestMethod.DELETE)
    @ResponseBody
    public RES<String> delRealTime(@PathVariable Long mid) {
        momentsService.delMoments(mid);
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }

}
