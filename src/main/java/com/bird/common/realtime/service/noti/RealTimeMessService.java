package com.bird.common.realtime.service.noti;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.bird.common.realtime.domain.RealTimeMess;
import com.bird.common.realtime.mapper.RealTimeMessMapper;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.security.WebUtils;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.mapper.BaseService;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.mapper.Direction;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.thread.ContextAwarePoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/30 11:41
 */
@Slf4j
@Service
public class RealTimeMessService extends BaseService<Long, RealTimeMess> {

    @Autowired
    private RealTimeMessMapper newsMessMapper;

    @Autowired
    private ContextAwarePoolExecutor threadPoolTaskExecutor;


    /**
     * (接收者)批量读取消息
     *
     * @param receUserId
     * @param messageType
     */
    @Transactional(rollbackFor = Exception.class)
    public void readMessage(String appType , long receUserId, String messageType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("receUserId", receUserId);
        params.put("messageType", messageType);
        params.put("readStatus", PublicEnum.Y.name());
        newsMessMapper.updateReadStatus(params);
    }


    /**
     * (接收者)未读消息数量
     *
     * @param receUserId
     * @param messageType
     * @return
     */
    public int unReadMessageCount(Long receUserId, String messageType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getAppType());
        params.put("receUserId", receUserId);
        params.put("messageType", messageType);
        params.put("readStatus", PublicEnum.N.name());
        return newsMessMapper.count(params);
    }


    /**
     * 删除通知
     *
     * @param messageId
     * @param messageType
     * @param sendUserId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delRealTimeMessage(String messageId, String messageType, long sendUserId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getAppType());
        params.put("messageId", messageId);
        params.put("messageType", messageType);
        params.put("sendUserId", sendUserId);
        newsMessMapper.deleteBy(params);
    }


    /**
     * 删除通知
     *
     * @param rtId
     * @param messageType
     * @param sendUserId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delRealTimeMessage(long rtId, String messageType, long sendUserId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getAppType());
        params.put("rtId", rtId);
        params.put("messageType", messageType);
        params.put("sendUserId", sendUserId);
        newsMessMapper.deleteBy(params);
    }


    /**
     * 用户资讯消息列表
     *
     * @param pageParam
     * @param userId
     * @return
     */
    public PageBean<RealTimeMessage> listMessage(PageParam pageParam, long userId, String messageType) {
        Condition cond = Condition.of();
        String appType = WebUtils.getAppType();
        cond.add("appType", appType);
        cond.add("receUserId", userId);
        cond.add("messageType", messageType);
        cond.addSort("create_time", Direction.DESC.name());

        PageBean<RealTimeMess> pageBean = listPage(pageParam, cond);


        if (pageBean.getTotalCount() == 0) {
            return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage());
        }


        List<RealTimeMessage> l = pageBean.getRecordList().stream().map(input -> {
            RealTimeMessage realTimeMessage = new RealTimeMessage.MessageBuilder()
                    .setAppType(input.getAppType())
                    .setMessageType(input.getMessageType())
                    .setMessageId(input.getMessageId())
                    .setNotiId(input.getNotiId())
                    .setSendUserId(input.getSendUserId())
                    .setSendUserName(input.getSendUserName())
                    .setSendOrgName(input.getSendOrgName())
                    .setContent(JSON.parseObject(input.getContent()
                            , RealTimeMessage.MessageContent.class))
                    .setRealTime(JSON.parseObject(input.getRealTime()
                            , RealTimeResult.RealTimeResource.class))
                    .build();

            realTimeMessage.setSendTime(input.getCreateTime());
            realTimeMessage.setSendHeadImg(input.getSendHeadImg());
            return realTimeMessage;
        }).collect(Collectors.toList());


        threadPoolTaskExecutor.execute(() -> readMessage(appType,userId, messageType));

        return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage(), pageBean.getTotalCount()
                , l);
    }
}
