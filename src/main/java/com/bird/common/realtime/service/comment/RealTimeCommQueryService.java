package com.bird.common.realtime.service.comment;

import com.google.common.collect.Maps;
import com.bird.common.realtime.domain.RealTimeComment;
import com.bird.common.realtime.mapper.RealTimeCommentMapper;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/3/23 16:49
 */
@Slf4j
@Service
public class RealTimeCommQueryService {

    @Autowired
    private RealTimeCommentMapper mapper;


    /**
     * 根据评论ID获取评论
     *
     * @param cid
     * @return
     */
    public RealTimeComment findCommentByCid(String appType , long cid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("cid", cid);
        RealTimeComment realTimeComment = mapper.get(params);
        if (ObjectUtils.isEmpty(realTimeComment)) return null;

        return realTimeComment;
    }


    /**
     * 父ID查询列表
     * @param pid
     * @return
     */
    public List<RealTimeComment> findCommentByPid(long pid) {
        Map<String, Object> params = Maps.newHashMap();
        String appType = com.bird.common.security.WebUtils.getAppType();
        params.put("appType", appType);
        params.put("pid", pid);
        return mapper.list(params);
    }


    /**
     * 评论详情下的评论数
     * @param rid
     * @param rootId
     * @return
     */
    public Integer commentCount(long rid, long rootId) {
        Map<String, Object> params = Maps.newHashMap();
        String appType = com.bird.common.security.WebUtils.getAppType();
        params.put("appType", appType);
        params.put("rootId", rootId);
        params.put("rtId", rid);
        params.put("isClose", PublicEnum.N.name());
        return mapper.count(params);
    }



    public List<RealTimeComment> findCommentByCids(List<Long> cids) {
        Map<String, Object> params = Maps.newHashMap();
        String appType = com.bird.common.security.WebUtils.getAppType();
        params.put("appType", appType);
        params.put("cids", cids);
        return mapper.list(params);
    }

}
