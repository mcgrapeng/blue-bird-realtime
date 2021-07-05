package com.bird.common.realtime.controller.manage;

import com.bird.common.realtime.ex.RealTimeBizException;
import com.bird.common.realtime.service.comment.CommentClose;
import com.bird.common.realtime.service.comment.CommentResult;
import com.bird.common.realtime.service.comment.RealTimeCommManageService;
import com.bird.common.web.RES;
import com.bird.common.web.enums.ResultEnum;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/5/20 14:51
 */
@Slf4j
@RequestMapping(value = "/ct-manage", produces = "application/json;charset=UTF-8")
@Controller
public class RealTimeCommentManageController {


    @Autowired
    private RealTimeCommManageService commManageService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public RES<PageBean<CommentResult>> page(
            @RequestParam(required = false) Integer pageNum
            , @RequestParam(required = false) Integer pageSize
            , @RequestParam(required = false) Long rtId
            , @RequestParam(required = false) Date startTime
            , @RequestParam(required = false) Date endTime
            , @RequestParam(required = false) String isClose
            , @RequestParam(required = false) String content) {

        if (ObjectUtils.isEmpty(rtId) || ObjectUtils.isEmpty(pageNum)) {
            throw RealTimeBizException.PARAM_ERR;
        }

        return RES.of(ResultEnum.处理成功.code, commManageService.page(new PageParam(pageNum, pageSize), rtId, startTime, endTime, isClose, content), ResultEnum.处理成功.name());
    }



    @RequestMapping(value = "/close", method = RequestMethod.PUT)
    @ResponseBody
    public RES<String> closeComment(@RequestBody CommentClose commentClose) {
        commManageService.closeComment(
                commentClose.getAppType() ,  commentClose.getCid(),  commentClose.getRootId() , commentClose.getRtId()
        );
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }
}
