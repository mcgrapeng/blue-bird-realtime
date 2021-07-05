package com.bird.common.realtime.controller;

import com.alibaba.fastjson.JSON;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.web.RES;
import com.bird.common.web.enums.ResultEnum;
import com.bird.common.web.ex.birdCommonException;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/10 17:13
 */
@Slf4j
@RequestMapping(value = "/rt", produces = "application/json;charset=UTF-8")
@Controller
public class RealTimeController {

    @Autowired
    private RealTimeFactory factory;


    /**
     * 资讯详情
     *
     * @param infoType
     * @param rtId
     * @return
     */
    @RequestMapping(value = {"", "/open-query"},method = RequestMethod.GET)
    @ResponseBody
    public RES<RealTimeResult> execute(
            @RequestParam(value = "infoType", required = false) String infoType
            , @RequestParam(value = "rtId", required = false) Long rtId) {

        if (StringUtils.isBlank(infoType) || ObjectUtils.isEmpty(rtId)) {
            throw birdCommonException.PARAM_ILLEGAL;
        }

        return RES.of(ResultEnum.处理成功.code, factory.getInstance(infoType)
                .execute(infoType, rtId), ResultEnum.处理成功.name());
    }


    /**
     * 资讯列表
     *
     * @param pageNum
     * @param pageSize
     * @param infoType
     * @param rtType
     * @param secondRtType
     * @return
     */
    @RequestMapping(value = {"/page", "/open-query/page"}, method = RequestMethod.GET)
    @ResponseBody
    public RES<PageBean<RealTimeResult>> execute(
            @RequestParam(value = "pageNum", required = false) Integer pageNum
            , @RequestParam(value = "pageSize", required = false) Integer pageSize
            , @RequestParam(value = "infoType", required = false) String infoType
            , @RequestParam(value = "rtType", required = false) String rtType
            , @RequestParam(value = "isTop", required = false) String isTop
            , @RequestParam(value = "orgIds", required = false) List<Long> orgIds
            , @RequestParam(value = "secondRtType", required = false) String secondRtType) {


        log.info(">>>>>>>>>>>orgIds={}", JSON.toJSONString(orgIds));

        String app = WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP);

        if (StringUtils.isBlank(infoType) || StringUtils.isBlank(app)) {
            throw birdCommonException.PARAM_ILLEGAL;
        }

        Condition cond = Condition.of();
        cond.add("infoType", infoType);
        cond.add("isTop", isTop);
        cond.add("orgIds", orgIds);
        cond.add("rtType", rtType);
        cond.add("secondRtType", secondRtType);
        cond.add("appType", app);
        return RES.of(ResultEnum.处理成功.code, factory.getInstance(infoType)
                .execute(new PageParam(pageNum, pageSize), cond), ResultEnum.处理成功.name());
    }


    /**
     * 我的资讯
     *
     * @param pageNum
     * @param pageSize
     * @param infoType
     * @param rtType
     * @param secondRtType
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public RES<PageBean<RealTimeResult>> user(@RequestParam(value = "pageNum", required = false) Integer pageNum
            , @RequestParam(value = "pageSize", required = false) Integer pageSize
            , @RequestParam(value = "infoType", required = false) String infoType
            , @RequestParam(value = "rtType", required = false) String rtType
            , @RequestParam(value = "secondRtType", required = false) String secondRtType) {

        String app = WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP);

        if (StringUtils.isBlank(infoType) || StringUtils.isBlank(app)) {
            throw birdCommonException.PARAM_ILLEGAL;
        }

        Condition cond = Condition.of();
        cond.add("appType", app);
        cond.add("infoType", infoType);
        cond.add("rtType", rtType);
        cond.add("secondRtType", secondRtType);
        cond.add("ssoUserId", com.bird.common.security.WebUtils.getUserId());
        return RES.of(ResultEnum.处理成功.code, factory.getInstance(infoType)
                .execute(new PageParam(pageNum, pageSize), cond), ResultEnum.处理成功.name());
    }


}
