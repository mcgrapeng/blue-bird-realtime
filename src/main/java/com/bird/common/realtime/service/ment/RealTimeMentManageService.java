package com.bird.common.realtime.service.ment;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.bird.common.realtime.RealTime;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTimeMents;
import com.bird.common.realtime.mapper.RealTimeMentMapper;
import com.bird.common.realtime.service.IRealTimeManageService;
import com.bird.common.realtime.service.RealTimeManageResult;
import com.bird.common.realtime.service.comment.RealTimeCommService;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.mapper.BaseService;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/10 16:43
 */
@Slf4j
@Service
public class RealTimeMentManageService extends BaseService<Long, RealTimeMents> implements IRealTimeManageService {

    @Autowired
    private RealTimeMentMapper mapper;

    @Autowired
    private RealTimeCommService commentService;

    @Autowired
    private CacheCallback cacheCallback;


    @Override
    public PageBean<RealTimeManageResult> page(PageParam param, Condition cond) {
        PageBean<RealTimeMents> pageBean = listPage(param, cond);
        if (pageBean.getTotalCount() == 0) {
            return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage());
        }

        List<RealTimeManageResult> data = pageBean.getRecordList().stream().map(input -> {

            Long like = cacheCallback.get(Conts.RealTimeCount.LIKE.name(), input.getAppType()
                    , input.getRtId());
            Long comm = cacheCallback.get(Conts.RealTimeCount.COMM.name(), StringUtils.join(input.getAppType(),":FULL")
                    , input.getRtId());

            RealTimeManageResult result = new RealTimeManageResult();
            result.setInfoType(input.getInfoType());
            result.setOrgId(input.getOrgId());
            result.setOrgName(input.getOrgName());
            result.setPublishTime(input.getCreateTime());
            result.setRtId(input.getRtId());
            result.setContent(input.getContent());
            result.setRtType(input.getRtType());
            result.setSecondRtType(input.getSecondRtType());
            result.setTitle(input.getTitle());
            result.setUserName(input.getUserName());
            result.setIsClose(input.getIsClose());
            result.setFirstImg(input.getFirstImg());
            result.setLikeNum(String.valueOf(like));
            result.setCommNum(String.valueOf(comm));
            result.setMetaType(input.getMetaType());
            result.buildResource(input);
            return result;
        }).collect(Collectors.toList());

        return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage(), pageBean.getTotalCount(), data);
    }

    @Override
    public Long addRealTime(long orgId, String orgName,String parentOrgName, String title, String content, String firstImg, String infoType, String rtType, String secondRtType, String isRec) {

        return 0L;
    }

    @Override
    public void addRealTime(RealTime realTime) {

    }

    @Override
    public void updRealTime(long rid, String title, String content, String firstImg, String isRec, long orgId, String orgName) {

    }


    @Override
    public void delRealTime(long rid) {

    }

    @Override
    public void topRealTime(long rid, String top) {

    }

    @Override
    public void recRealTime(long rid, String rec) {

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updRealTimeVideo(String appType, long rid, String play, String cover, Integer duration, Integer width, Integer height) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType",appType);
        params.put("rtId", rid);
        JSONObject j = new JSONObject();
        j.put("play", play);
        j.put("cover", cover);
        j.put("duration", duration);
        j.put("width", width);
        j.put("height", height);
        params.put("viResources", j.toJSONString());
        params.put("firstImg", cover);
        try {
            mapper.updateBy(params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public RealTimeManageResult getRealTime(Long rtId) {
        return null;
    }

    @Override
    public void closeRealTime(long rid) {
        String authorization = JWTHelper.getAuthorization();
        String appType = JWTHelper.getAppType(authorization);
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType",  appType);
        params.put("rtId", rid);
        params.put("isClose", PublicEnum.Y.name());
        mapper.updateBy(params);
    }
}
