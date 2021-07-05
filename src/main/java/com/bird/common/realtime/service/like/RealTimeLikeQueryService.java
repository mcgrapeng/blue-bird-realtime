package com.bird.common.realtime.service.like;

import com.google.common.collect.Maps;
import com.bird.common.realtime.domain.RealTimeLike;
import com.bird.common.realtime.mapper.RealTimeLikeMapper;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/3/23 19:11
 */
@Slf4j
@Service
public class RealTimeLikeQueryService {

    @Autowired
    private RealTimeLikeMapper newsLikeMapper;


    public RealTimeLike findRealTimeLikeByRidAndLikeUserId(long rid, long likeUserId) {
        String appType = com.bird.common.security.WebUtils.getAppType();
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        params.put("likeUserId", likeUserId);
        return newsLikeMapper.get(params);
    }


    public RealTimeLike findRealTimeLikeByLikeId(String appType , long likeId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("likeId", likeId);
        return newsLikeMapper.get(params);
    }
}
