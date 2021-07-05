package com.bird.common.realtime.enums;

/**
 * 信息流类型
 */
public enum InfoTypeEnum {

    SELF("自媒"),
    NEWS("官媒"),
    COMM("评论")
    ;


    private String desc;


    public String getDesc() {
        return desc;
    }


    InfoTypeEnum(String desc) {
        this.desc = desc;
    }
}
