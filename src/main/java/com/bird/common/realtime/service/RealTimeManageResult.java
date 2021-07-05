package com.bird.common.realtime.service;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/22 12:16
 */
@Setter
@Getter
public class RealTimeManageResult extends RealTimeResourceResult {

    private String title;

    private Long rtId;

    private String infoType;

    private String rtType;

    private String secondRtType;


    private Long orgId;
    /**
     * 发布人所属组织
     */
    private String orgName;

    private String parentOrgName;

    private String infoSource;

    /**
     * 发布人
     */
    private String userName;

    /**
     * 新闻内容
     */
    private String content;

    /**
     * 发布时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date publishTime;


    /**是否置顶*/
    private String isTop;

    /**是否推荐*/
    private String isRec;


    /**是否关闭*/
    private String isClose;


    /**
     * 首图
     */
    private String firstImg;


    /**
     * 阅读数、点赞数、评论数、转发数
     */
    private String readNum;
    private String likeNum;
    private String commNum;
    private String tranNum;


    /**
     * 媒体类型
     */
    private String metaType;

}
