package com.bird.common.realtime.service;

import com.bird.common.realtime.service.read.RealTimeReadService;
import com.bird.common.realtime.service.tran.RealTimeTranManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/11/25 16:49
 */
@Service
public class RealTimeAsyncService {


    @Autowired
    private RealTimeReadService realTimeReadService;

    @Autowired
    private RealTimeTranManageService realTimeTranService;


    @Async
    public void addRealTimeRead(String appType , String infoType,  String rtType,long rid, long userId,  String realName , long realtimeUserId , String realtimeUserName) {
        realTimeReadService.addRealTimeRead(appType,infoType,rtType, rid, userId,realName,realtimeUserId , realtimeUserName);
    }


    @Async
    public void addRealTimeTran(String appType,long tranUserId ,long rid, long realTimeUserId
            , String realTimeUserName, String infoType,String rtType) {
        realTimeTranService.addRealTimeTran(appType,tranUserId,rid, realTimeUserId, realTimeUserName, infoType,rtType);
    }
}
