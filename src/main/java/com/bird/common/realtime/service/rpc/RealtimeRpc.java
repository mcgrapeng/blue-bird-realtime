package com.bird.common.realtime.service.rpc;

import com.bird.common.realtime.IRealTimeService;
import com.bird.common.realtime.RealTime;
import com.bird.common.realtime.service.RealTimeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/4/16 15:06
 */
@org.apache.dubbo.config.annotation.Service(interfaceName = "com.bird.common.realtime.IRealTimeService", protocol = "dubbo", version = "1.0", retries = 3
        , timeout = 60000, loadbalance = "random", executes = 200, actives = 0, cluster = "failover")
@Slf4j
public class RealtimeRpc implements IRealTimeService {

    @Autowired
    private RealTimeFactory factory;


    @Override
    public void distributeRealTime(RealTime realTime) {
        factory.getRealTimeManageService(realTime.getInfoType()).addRealTime(realTime);
    }
}
