package com.bird.common.realtime.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/10 16:45
 */
@Getter
@Setter
public class RealTimeMents extends RealTime {


    private String content;


    /**
     * 类型：新建/转发
     */
    private String type;

    /**媒体资源
     * {
     *     "url":"XXXXX",
     *     "ord":1
     * }
     *
     *
     * */
    private String piResources;

    /**
     *
     * {
     *     "icon":"",
     *      "title":"",
     *     "url":""
     * }
     *
     */
    private String rtResources;



    private String viResources;


    private String metaType;


    private String isClose;

}
