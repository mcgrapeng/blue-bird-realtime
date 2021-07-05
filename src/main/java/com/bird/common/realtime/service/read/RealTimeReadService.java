package com.bird.common.realtime.service.read;

import com.bird.common.realtime.domain.RealTimeRead;
import com.bird.common.realtime.mapper.RealTimeReadMapper;
import com.bird.common.security.WebUtils;
import com.bird.common.web.security.JWTHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/24 14:52
 */
@Service
public class RealTimeReadService {


    @Autowired
    private RealTimeReadMapper newsReadMapper;


    @Autowired
    private RealTimeReadManageService readManageService;


    /**
     * 添加浏览记录
     *
     * @param rid
     * @param userId
     */
    public void addRealTimeRead(String appType ,String infoType, String rtType, long rid, long userId,  String realName , long realtimeUserId , String realtimeUserName) {
        RealTimeRead read = readManageService.getRead(appType,rid, userId);
        if (ObjectUtils.isEmpty(read)) {
            readManageService.doRealTimeRead( appType , rid, infoType, rtType, userId,realName,realtimeUserId,realtimeUserName);
        } else {
            readManageService.updRealTimeReadTimes(appType,rid, userId, read.getReadTimes() + 1);
        }
    }


    /**
     * 资讯阅读总数
     *
     * @param rid
     * @return
     */
    public int readTotal(long rid) {
        return newsReadMapper.sumReadTimes(WebUtils.getAppType(), rid);
    }
}
