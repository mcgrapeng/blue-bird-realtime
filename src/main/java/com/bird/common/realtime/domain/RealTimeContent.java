package com.bird.common.realtime.domain;

import com.bird.common.web.mapper.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RealTimeContent extends BaseEntity {

    /**新闻id*/
    private Long rtId;

    /**新闻内容*/
    private String content;


    public RealTimeContent setRtId(Long rtId) {
        this.rtId = rtId;
        return this;
    }

    public RealTimeContent setContent(String content) {
        this.content = content;
        return this;
    }
}