package com.bird.common.realtime.service.ment;

import com.bird.common.realtime.domain.RealTimeMents;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/4/9 15:06
 */
public interface IRealTimeMentCallback {


    /**
     * 构建资源
     */
    void  buildResource(RealTimeMents ments);
}
