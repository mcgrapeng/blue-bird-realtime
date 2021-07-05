package com.bird.common.realtime.controller.manage;

import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.realtime.service.RealTimeManageResult;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.realtime.service.news.RealTimeNewsForm;
import com.bird.common.realtime.service.news.RealTimeNewsManageService;
import com.bird.common.web.RES;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.enums.ResultEnum;
import com.bird.common.web.ex.birdCommonException;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.mapper.Direction;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 张朋
 * @version 1.0
 * @desc 后台管理
 * @date 2020/12/10 17:13
 */
@Slf4j
@RequestMapping(value = "/rt-manage", produces = "application/json;charset=UTF-8")
@Controller
public class RealTimeManageController {


    @Autowired
    private RealTimeFactory factory;

    @Autowired
    private RealTimeNewsManageService realTimeNewsManageService;


    //------------------后台管理------------------------

    /**
     * 发布资讯
     *
     * @return
     */
    @RequestMapping(value = "/distribute", method = RequestMethod.POST)
    @ResponseBody
    public RES<Long> distributeNews(@RequestBody @Validated(RealTimeNewsForm.Add.class) RealTimeNewsForm form) {


        return RES.of(ResultEnum.处理成功.code, factory.getRealTimeManageService(InfoTypeEnum.NEWS.name()).addRealTime(
                form.getOrgId(),
                form.getOrgName(),
                form.getParentOrgName(),
                form.getTitle(),
                form.getContent(),
                form.getFirstImg(),
                form.getInfoType(),
                form.getRtType(),
                form.getSecondRtType(),
                form.getIsRec()
        ), ResultEnum.处理成功.name());
    }


    /**
     * 修正资讯
     *
     * @return
     */
    @RequestMapping(value = "/revise", method = RequestMethod.PUT)
    @ResponseBody
    public RES<RealTimeResult> reviseNews(@RequestBody @Validated(RealTimeNewsForm.Upd.class) RealTimeNewsForm form) {
        factory.getRealTimeManageService(InfoTypeEnum.NEWS.name()).updRealTime(
                form.getRtId(),
                form.getTitle(),
                form.getContent(),
                form.getFirstImg(),
                form.getIsRec(),
                form.getOrgId()
                , form.getOrgName()
        );
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }


    /**
     * 删除资讯
     *
     * @param rtId
     * @return
     */
    @RequestMapping(value = "/delete/{rtId}", method = RequestMethod.DELETE)
    @ResponseBody
    public RES<RealTimeResult> deleteNews(@PathVariable(value = "rtId"
            , required = false) Long rtId) {
        factory.getRealTimeManageService(InfoTypeEnum.NEWS.name()).delRealTime(rtId);
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }


    /**
     * 获取资讯
     *
     * @param rtId
     * @return
     */
    @RequestMapping(value = {"/get/{rtId}"}, method = RequestMethod.GET)
    @ResponseBody
    public RES<RealTimeManageResult> getNews(@PathVariable(value = "rtId"
            , required = false) Long rtId) {
        return RES.of(ResultEnum.处理成功.code, factory.getRealTimeManageService(InfoTypeEnum.NEWS.name()).getRealTime(rtId), ResultEnum.处理成功.name());
    }

    /**
     * 资讯列表
     *
     * @param pageNum
     * @param pageSize
     * @param infoType
     * @param rtType
     * @param orgIds       "11,22,22,11,"
     * @param secondRtType
     * @return
     */
    @RequestMapping(value = {"/page"}, method = RequestMethod.GET)
    @ResponseBody
    public RES<PageBean<RealTimeManageResult>> execute(
            @RequestParam(value = "pageNum", required = false) Integer pageNum
            , @RequestParam(value = "pageSize", required = false) Integer pageSize
            , @RequestParam(value = "infoType", required = false) String infoType
            , @RequestParam(value = "rtType", required = false) String rtType
            , @RequestParam(value = "secondRtType", required = false) String secondRtType
            , @RequestParam(value = "startSubmitTime", required = false) Date startSubmitTime
            , @RequestParam(value = "endSubmitTime", required = false) Date endSubmitTime
            //, @RequestParam(value = "orgId", required = false) Long orgId
            , @RequestParam(value = "orgIds", required = false) List<Long> orgIds
            , @RequestParam(value = "title", required = false) String title
            , @RequestParam(value = "isClose", required = false) String isClose) {

        String app = WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP);

        if (StringUtils.isBlank(app)) {
            throw birdCommonException.PARAM_ILLEGAL;
        }

        Condition cond = Condition.of();
        cond.add("infoType", infoType);
        cond.add("rtType", rtType);
        cond.add("secondRtType", secondRtType);
        cond.add("startSubmitTime", startSubmitTime);
        cond.add("endSubmitTime", endSubmitTime);
       // cond.add("orgId", orgId);
        cond.add("orgIds", orgIds);
        cond.add("isClose", isClose);
        if (StringUtils.isNotBlank(title)) {
            cond.add("title", StringUtils.join("%", title, "%"));
        }
        cond.add("appType", app);
        cond.addSort("create_time", Direction.DESC.name());

        return RES.of(ResultEnum.处理成功.code, factory.getRealTimeManageService(infoType).page(new PageParam(pageNum, pageSize), cond), ResultEnum.处理成功.name());
    }


    /**
     * 关闭资讯
     *
     * @param rtId
     * @return
     */
    @RequestMapping(value = "/close/{infoType}/{rtId}", method = RequestMethod.PUT)
    @ResponseBody
    public RES<RealTimeResult> closeRealTime(@PathVariable(value = "infoType"
            , required = false) String infoType, @PathVariable(value = "rtId"
            , required = false) Long rtId) {
        factory.getRealTimeManageService(infoType).closeRealTime(rtId);
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }


    /**
     * 置顶资讯
     *
     * @param infoType
     * @param rtId
     * @return
     */
    @RequestMapping(value = "/top/{infoType}/{rtId}", method = RequestMethod.PUT)
    @ResponseBody
    public RES<String> topRealTime(@PathVariable(value = "infoType", required = false) String infoType, @PathVariable(value = "rtId"
            , required = false) Long rtId
            , @RequestParam(value = "status", required = false) String status) {//新闻置顶

        factory.getRealTimeManageService(infoType).topRealTime(rtId, status);
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }


    @RequestMapping(value = "/top-count", method = RequestMethod.GET)
    @ResponseBody
    public RES<Integer> topCount() {
        return RES.of(ResultEnum.处理成功.code, realTimeNewsManageService.topCount(), ResultEnum.处理成功.name());
    }

}
