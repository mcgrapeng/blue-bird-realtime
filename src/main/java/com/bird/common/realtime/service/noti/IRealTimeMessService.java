package com.bird.common.realtime.service.noti;

public interface IRealTimeMessService {

    /**
     * 消息通知（引用资源：资讯、评论）
     *
     * @param notiId      点赞ID 、评论ID
     //* @param messageType 通知模式 （评论、点赞）
     * @param isPush  是否需要推送
     */
    void notifyMessage(long notiId,   boolean isPush);

}
