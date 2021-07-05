package com.bird.common.realtime.service;

import com.bird.common.realtime.domain.RealTime;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/28 18:24
 */
public interface IRealTimeCallback<T extends RealTime> {

    void doCreate(T realTime);
}
