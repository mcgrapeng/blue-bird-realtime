package com.bird.common.realtime.domain;

import lombok.Getter;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/29 16:10
 */
@Getter
public class RealTimeNews extends RealTime {

    /**
     * 阅读数
     */
    private Long readNum;

    /**
     * 点赞数
     */
    private Long likeNum;

    /**
     * 转发数
     */
    private Long tranNum;

    /**
     * 评论数
     */
    private Long commNum;




    public RealTimeNews setReadNum(Long readNum) {
        this.readNum = readNum;
        return this;
    }

    public RealTimeNews setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
        return this;
    }

    public RealTimeNews setTranNum(Long tranNum) {
        this.tranNum = tranNum;
        return this;
    }

    public RealTimeNews setCommNum(Long commNum) {
        this.commNum = commNum;
        return this;
    }

    public RealTimeNews setIsTop(String isTop) {
        this.isTop = isTop;
        return this;
    }

}
