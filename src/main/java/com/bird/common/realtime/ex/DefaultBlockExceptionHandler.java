package com.bird.common.realtime.ex;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.bird.common.web.RES;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/5/12 11:38
 */
@ControllerAdvice(basePackages = "com.bird.common.realtime.controller") // 只处理该包下的 Controller 定义的接口
@Component
public class DefaultBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        throw e;
    }


    @ResponseBody
    @ExceptionHandler(value = BlockException.class)
    public RES blockExceptionHandler(BlockException blockException) {
        return RES.of(1024, blockException.getClass().getSimpleName());
    }

}
