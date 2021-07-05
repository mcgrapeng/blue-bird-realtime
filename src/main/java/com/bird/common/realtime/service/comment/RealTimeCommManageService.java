package com.bird.common.realtime.service.comment;

import com.bird.common.mq.birdKafkaExecutor;
import com.bird.common.mq.config.ProfileProperties;
import com.bird.common.mq.message.CommonMessage;
import com.bird.common.realtime.KafkaTopics;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.domain.RealTimeComment;
import com.bird.common.realtime.mapper.RealTimeCommentMapper;
import com.bird.common.security.WebUtils;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.mapper.BaseService;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.mapper.Direction;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/5/20 14:33
 */
@Slf4j
@Service
public class RealTimeCommManageService extends BaseService<Long, RealTimeComment> {

    @Autowired
    private RealTimeCommentMapper realTimeCommentMapper;


    @Autowired
    private CacheCallback cacheCallback;

    @Autowired
    private birdKafkaExecutor kafkaExecutor;

    @Autowired
    private ProfileProperties profileProperties;


    /**
     * pc搜索列表
     *
     * @param pageParam
     * @param rtId
     * @param startTime
     * @param endTime
     * @param isClose
     * @param content
     * @return
     */
    @Trace(operationName = "comment_page")
    public PageBean<CommentResult> page(PageParam pageParam
            , Long rtId, Date startTime, Date endTime, String isClose, String content) {
        ActiveSpan.tag("方法", "评论-分页");
        Condition cond = Condition.of();
        if (ObjectUtils.isNotEmpty(content)) {
            cond.add("content", StringUtils.join("%", content, "%"));
        }
        cond.add("startTime", startTime);
        cond.add("endTime", endTime);
        cond.add("isClose", isClose);
        cond.add("rtId", rtId);
        cond.addSort("create_time", Direction.DESC.name());

        PageBean<RealTimeComment> pageBean = listPage(pageParam, cond);
        if (pageBean.getTotalCount() == 0) {
            return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage());
        }

        List<CommentResult> l = pageBean.getRecordList().stream().map(input -> {
            CommentResult n = CommentResult.of()
                    .setCid(input.getCid())
                    .setAppType(input.getAppType())
                    .setRtId(input.getRtId())
                    .setRootId(input.getRootId())
                    .setIsClose(input.getIsClose())
                    .setContent(input.getContent())
                    .setCommentUserName(input.getCommentUserName())
                    .setCommentOrgName(input.getCommentOrgName())
                    .setCommentFullTime(input.getCreateTime());
            return n;
        }).collect(Collectors.toList());
        return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage(), pageBean.getTotalCount()
                , l);
    }


    /**
     * 关闭评论
     * @param appType
     * @param cid
     * @param rtId
     */
    @Trace(operationName = "close_comment")
    public void closeComment(String appType , Long cid, Long rootId ,  Long rtId){
        realTimeCommentMapper.updateClose(
                cid , appType ,PublicEnum.Y.name() ,WebUtils.getUserName()
        );

        if (String.valueOf(rootId).equals(String.valueOf(cid)) ) {
            cacheCallback.decrCOMM(appType, rtId);
        } else {
            cacheCallback.decrCOMM(appType, rtId, rootId);
        }
/*
        CommonMessage message = CommonMessage.CommonMessageBuilder.instance()
                .setMsgId(SnowflakeIdWorker.build(8).nextId())
                .setSendTime(Date.from(Instant.now()))
                .setPayload(
                        CommonMessage.Payload.instance().addData("rtId", rtId)
                                .addData("cid", cid)
                ).build();

        String topic = CommonMessage.buildTopic(profileProperties.getActive()
                , KafkaTopics.REALTIME_COMMENT_CLOSE, appType);

        kafkaExecutor.sendToKafkaStandardMessageAsync(topic
                , message);*/
    }

}
