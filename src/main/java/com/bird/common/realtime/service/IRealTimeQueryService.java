package com.bird.common.realtime.service;

import com.bird.common.realtime.domain.RealTime;

public interface IRealTimeQueryService<T extends RealTime> extends IRealTimeCommonService{

    /**
     * 资讯查询
     * @param rtId
     * @return
     */
    T findRealTimeByRtId(String appType , long rtId);
}
