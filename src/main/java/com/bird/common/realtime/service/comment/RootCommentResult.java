package com.bird.common.realtime.service.comment;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author 张朋
 * @version 1.0
 * @desc 评论详情
 * @date 2020/12/23 10:03
 */
@Getter
@Setter
public class RootCommentResult {

    private String appType;

    private Long cid;

    private Long pid;

    private Long rootId;

    private String commentUserName;

    private String commentUserHeadImg;

    @JSONField(format = "MM-dd HH:mm")
    private Date commentTime;

    private String isOwner;

    private String isLike;

    private String content;

    private Long likeNum;

    private Long revNum;
}
