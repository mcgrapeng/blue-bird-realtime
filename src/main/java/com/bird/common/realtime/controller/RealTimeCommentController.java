package com.bird.common.realtime.controller;

import com.bird.common.realtime.service.comment.*;
import com.bird.common.web.RES;
import com.bird.common.web.annotation.RequestLimit;
import com.bird.common.web.controller.BaseController;
import com.bird.common.web.enums.ResultEnum;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/10 17:14
 */
@Slf4j
@RequestMapping(value = "/rt-comment", produces = "application/json;charset=UTF-8")
@Controller
public class RealTimeCommentController extends BaseController {

    @Autowired
    private RealTimeCommService commentService;


    /**
     * 评论列表
     *
     * @param rid
     * @param pageParam
     * @return
     */
    @RequestMapping(value = {"/tree/{rid}", "/open-query/tree/{rid}"}, method = RequestMethod.GET)
    @ResponseBody
    public RES<PageBean<CommentResult>> tree(@PathVariable Long rid, PageParam pageParam) {
        return RES.of(ResultEnum.处理成功.code, commentService.tree(pageParam, rid), ResultEnum.处理成功.name());
    }


    /**
     * 根评论下评论列表（根评论：资讯下的一级评论）
     *
     * @param rid
     * @param rootId
     * @param pageParam
     * @return
     */
    @RequestMapping(value = "/view/{rid}/{rootId}", method = RequestMethod.GET)
    @ResponseBody
    public RES<PageBean<CommentResult>> view(@PathVariable Long rid, @PathVariable Long rootId, PageParam pageParam) {
        return RES.of(ResultEnum.处理成功.code, commentService.listCommentByRootId(rid, rootId, pageParam), ResultEnum.处理成功.name());
    }


    /**
     * 根评论详情
     *
     * @param rootId
     * @return
     */
    @RequestMapping(value = "/view/{rootId}", method = RequestMethod.GET)
    @ResponseBody
    public RES<RootCommentResult> viewRoot(@PathVariable Long rootId) {
        return RES.of(ResultEnum.处理成功.code, commentService.viewRootComment(rootId), ResultEnum.处理成功.name());
    }


    /**
     * 评论
     * @param comment
     * @return
     */
    @RequestLimit(count = 2, time = 1000)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RES<String> addComment(@RequestBody @Validated CommentCreate comment) {
        commentService.addComment(comment);
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }


    /**
     * 删除评论
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public RES<String> delComment(@RequestBody CommentDelete commentDelete) {
        commentService.delComment(commentDelete.getRid(), commentDelete.getRootId(),commentDelete.getCid());
        return RES.of(ResultEnum.处理成功.code, ResultEnum.处理成功.name());
    }


}
