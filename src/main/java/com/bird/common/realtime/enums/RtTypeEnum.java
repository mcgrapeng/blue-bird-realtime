package com.bird.common.realtime.enums;

/**
 * 信息流类型
 */
public enum RtTypeEnum {
    MENT("圈子"),
    COMM("评论"),
    STUD("学习"),
    STYL("风采"),
    NEWS("新闻");


    private String desc;


    public String getDesc() {
        return desc;
    }


    public static String getDesc(String code) {
        for (RtTypeEnum rtTypeEnum : RtTypeEnum.values()) {
            if (rtTypeEnum.name().equals(code)) {
                return rtTypeEnum.getDesc();
            }
        }
        return null;
    }


    RtTypeEnum(String desc) {
        this.desc = desc;
    }
}
