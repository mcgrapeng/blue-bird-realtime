package com.bird.common.realtime.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.bird.common.realtime.domain.RealTimeMents;
import com.bird.common.realtime.enums.MetaTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/18 17:54
 */
@Builder
@Getter
@Setter
public class RealTimeResult extends RealTimeResourceResult{

    /**
     * 资讯ID
     */
    private Long rtId;

    /**
     * 信息流类型
     */
    private String infoType;

    /**
     * 资讯类型
     */
    private String rtType;

    /**
     * 资讯子类
     */
    private String secondRtType;

    /**
     * 标题
     */
    private String title;

    /**
     * 资讯内容
     */
    private String content;

    /**
     * 所属组织
     */
    private Long orgId;

    private String orgName;

    /**
     * 发布人
     */
    private Long userId;

    private String userName;

    private String userHeadImg;

    /**
     * 资讯图片集
     */
    private Set<String> firstImg;

    /**
     * 发布时间
     */
    @JSONField(format = "MM-dd HH:mm")
    private Date publishTime;

    /**
     * 是否置顶
     */
    private String isTop;

    /**
     * 是否推荐
     */
    private String isRec;


    /**
     * 是否点赞
     */
    private String isLike;

    /**
     * 是否自己
     */
    private String isOwner;

    /**
     * 阅读数、点赞数、评论数、转发数
     */
    private String readNum;
    private String likeNum;
    private String commNum;
    private String tranNum;


    /**
     * 自媒展示风格
     */
    private String mentStyle;

    /**
     * 媒体类型
     */
    private String metaType;

}
