package com.bird.common.realtime.service.news;

import com.google.common.collect.Maps;
import com.bird.common.realtime.RealTime;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTimeContent;
import com.bird.common.realtime.domain.RealTimeNews;
import com.bird.common.realtime.mapper.RealTimeContentMapper;
import com.bird.common.realtime.mapper.RealTimeNewsMapper;
import com.bird.common.realtime.service.IRealTimeManageService;
import com.bird.common.realtime.service.RealTimeManageResult;
import com.bird.common.realtime.service.comment.RealTimeCommService;
import com.bird.common.realtime.service.like.RealTimeLikeService;
import com.bird.common.realtime.service.read.RealTimeReadManageService;
import com.bird.common.security.WebUtils;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.mapper.BaseService;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.thread.ContextAwarePoolExecutor;
import com.bird.common.web.utils.SnowflakeIdWorker;
import com.bird.sso.api.IUserQueryFutureService;
import com.bird.sso.api.domain.SSOUser;
import com.bird.sso.api.ex.SSOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/5 13:05
 */
@Service
@Slf4j
public class RealTimeNewsManageService extends BaseService<Long, RealTimeNews> implements IRealTimeManageService {

    @Autowired
    private RealTimeNewsMapper mapper;

    @Autowired
    private RealTimeContentMapper contentMapper;


    @Autowired
    private RealTimeCommService commService;

    @Autowired
    private RealTimeLikeService likeService;

    @Autowired
    private ContextAwarePoolExecutor executor;

    @Autowired
    private CacheCallback cacheCallback;


    @Autowired
    private RealTimeReadManageService realTimeReadManageService;

    @Reference(check = false, version = "1.0")
    private IUserQueryFutureService userQueryService;

    @Override
    public PageBean<RealTimeManageResult> page(PageParam param, Condition cond) {

        PageBean<RealTimeNews> pageBean = listPage(param, cond);
        if (pageBean.getTotalCount() == 0) {
            return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage());
        }

        List<RealTimeManageResult> data = pageBean.getRecordList().stream().map(input -> {

            Long read = cacheCallback.get(Conts.RealTimeCount.READ.name()
                    , input.getAppType(), input.getRtId());
            Long like = cacheCallback.get(Conts.RealTimeCount.LIKE.name(), input.getAppType()
                    , input.getRtId());
            Long comm = cacheCallback.get(Conts.RealTimeCount.COMM.name(), StringUtils.join(input.getAppType(), ":FULL")
                    , input.getRtId());
            Long tran = cacheCallback.get(Conts.RealTimeCount.TRAN.name(), input.getAppType()
                    , input.getRtId());

            RealTimeManageResult result = new RealTimeManageResult();
            result.setInfoType(input.getInfoType());
            result.setOrgName(input.getOrgName());
            result.setParentOrgName(input.getParentOrgName());
            result.setFirstImg(input.getFirstImg());
            result.setOrgId(input.getOrgId());
            result.setPublishTime(input.getCreateTime());
            result.setRtId(input.getRtId());
            result.setIsTop(input.getIsTop());
            result.setRtType(input.getRtType());
            result.setSecondRtType(input.getSecondRtType());
            result.setTitle(input.getTitle());
            result.setUserName(input.getUserName());
            result.setCommNum(String.valueOf(comm));
            result.setReadNum(String.valueOf(read));
            result.setLikeNum(String.valueOf(like));
            result.setTranNum(String.valueOf(tran));
            return result;
        }).collect(Collectors.toList());


        return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage(), pageBean.getTotalCount(), data);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long addRealTime(long orgId, String orgName, String parentOrgName,String title
            , String content, String firstImg, String infoType, String rtType
            , String secondRtType, String isRec) {


        String appType = WebUtils.getAppType();
        Long userId = WebUtils.getUserId();
        CompletableFuture<SSOUser> ssoUser = userQueryService.getFutureSSOUser(appType
                , userId);


        RealTimeNews realTimeNews = new RealTimeNews();
        realTimeNews.setAppType(appType);
        realTimeNews.setRtId(SnowflakeIdWorker.build(7L).nextId());
        realTimeNews.setIsTop(PublicEnum.N.name());
        realTimeNews.setIsRec(isRec);
        realTimeNews.setInfoType(infoType);
        realTimeNews.setRtType(rtType);
        realTimeNews.setTitle(title);
        realTimeNews.setOrgId(orgId);
        realTimeNews.setOrgName(orgName);
        realTimeNews.setParentOrgName(parentOrgName);
        realTimeNews.setFirstImg(firstImg);
        realTimeNews.setUserId(userId);
        realTimeNews.setSecondRtType(secondRtType);
        realTimeNews.setCreateTime(Date.from(Instant.now()));


        SSOUser user = null;
        try {
            user = ssoUser.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        if (ObjectUtils.isEmpty(user)) {
            throw SSOException.USER_NO_EXITS;
        }

        String userName = user.getUserName();
        String realName = user.getRealName();

        realTimeNews.setUserName(realName);
        realTimeNews.setCreater(userName);

        mapper.insert(realTimeNews);


        RealTimeContent realTimeContent = new RealTimeContent();
        realTimeContent.setRtId(realTimeNews.getRtId());
        realTimeContent.setContent(content);
        realTimeContent.setCreater(userName);
        realTimeContent.setCreateTime(Date.from(Instant.now()));
        contentMapper.insert(realTimeContent);

        return realTimeNews.getRtId();
    }

    @Override
    public void addRealTime(RealTime realTime) {
        RealTimeNews realTimeNews = new RealTimeNews();
        realTimeNews.setAppType(realTime.getAppType());
        realTimeNews.setRtId(SnowflakeIdWorker.build(7L).nextId());
        realTimeNews.setIsTop(PublicEnum.N.name());
        realTimeNews.setIsRec(realTime.getIsRec());
        realTimeNews.setInfoType(realTime.getInfoType());
        realTimeNews.setRtType(realTime.getRtType());
        realTimeNews.setTitle(realTime.getTitle());
        realTimeNews.setOrgId(realTime.getOrgId());
        realTimeNews.setOrgName(realTime.getOrgName());
        realTimeNews.setParentOrgName(realTime.getParentOrgName());
        realTimeNews.setUserId(realTime.getUserId());
        realTimeNews.setUserName(realTime.getUserName());
        realTimeNews.setUserHeadImg(realTime.getUserHeadImg());
        realTimeNews.setSecondRtType(realTime.getSecondRtType());
        realTimeNews.setCreater(realTime.getUserName());
        realTimeNews.setCreateTime(Date.from(Instant.now()));
        realTimeNews.setFirstImg(realTime.getFirstImg());
        mapper.insert(realTimeNews);


        RealTimeContent realTimeContent = new RealTimeContent();
        realTimeContent.setRtId(realTimeNews.getRtId());
        realTimeContent.setContent(realTime.getContent());
        realTimeContent.setCreater(realTime.getUserName());
        realTimeContent.setCreateTime(Date.from(Instant.now()));
        contentMapper.insert(realTimeContent);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updRealTime(long rid, String title, String content, String firstImg, String isRec, long orgId, String orgName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getAppType());
        params.put("rtId", rid);
        params.put("title", title);
        params.put("isRec", isRec);
        params.put("orgId", orgId);
        params.put("orgName", orgName);
        params.put("firstImg", firstImg);
        mapper.updateBy(params);

        params.clear();
        params.put("rtId", rid);
        params.put("content", content);
        contentMapper.updateBy(params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delRealTime(long rid) {
        Map<String, Object> params = Maps.newHashMap();
        String appType = WebUtils.getAppType();
        params.put("appType", appType);
        params.put("rtId", rid);
        mapper.deleteBy(params);
        contentMapper.deleteBy(params);

        executor.execute(() -> {
            commService.deleteCommentByRid(appType,rid);
            likeService.delGiveLike(appType,rid);
            realTimeReadManageService.delRealTimeRead(appType,rid);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void topRealTime(long rid, String top) {
        Map<String, Object> params = Maps.newHashMap();
        String appType = WebUtils.getAppType();
        params.put("appType", appType);
        params.put("rtId", rid);
        params.put("isTop", top);
        params.put("topTime", Date.from(Instant.now()));
        mapper.updateBy(params);


      /*  if (top.equals(PublicEnum.Y.name())) {
            executor.execute(() -> {
                Map<String, Object> params1 = Maps.newHashMap();
                params1.put("appType", appType);
                params1.put("isTop", top);
                params1.put("infoType", InfoTypeEnum.NEWS.name());
                List<RealTimeNews> topList = mapper.list(params1);

                log.info(">>>>>>>>>>>>>>>>>>>>topList={}", JSON.toJSONString(topList));
                if (CollectionUtils.isNotEmpty(topList)
                        && CollectionUtils.size(topList) >= 3) {
                    List<RealTimeNews> sorted = topList.stream().sorted(Comparator.comparing(RealTimeNews::getTopTime)).collect(Collectors.toList());
                    RealTimeNews timeNews = sorted.get(0);
                    params1.clear();
                    params1.put("appType", appType);
                    params1.put("infoType", InfoTypeEnum.NEWS.name());
                    params1.put("rtId", timeNews.getRtId());
                    params1.put("isTop", PublicEnum.N.name());
                    mapper.updateBy(params1);
                }
            });
        }*/
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recRealTime(long rid, String rec) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getAppType());
        params.put("rtId", rid);
        params.put("rec", rec);
        mapper.updateBy(params);
    }

    @Override
    public void updRealTimeVideo(String appType, long rid, String play, String cover, Integer duration, Integer width, Integer height) {

    }


    @Override
    public RealTimeManageResult getRealTime(Long rtId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getAppType());
        params.put("rtId", rtId);
        RealTimeNews input = mapper.get(params);
        RealTimeContent realTimeContent = contentMapper.get(params);
        RealTimeManageResult result = new RealTimeManageResult();
        if (Objects.nonNull(input)) {
            result.setInfoType(input.getInfoType());
            result.setOrgId(input.getOrgId());
            result.setOrgName(input.getOrgName());
            result.setPublishTime(input.getCreateTime());
            result.setRtId(input.getRtId());
            result.setRtType(input.getRtType());
            result.setSecondRtType(input.getSecondRtType());
            result.setIsRec(input.getIsRec());
            result.setTitle(input.getTitle());
            result.setUserName(input.getUserName());
            result.setFirstImg(input.getFirstImg());
        }
        if (Objects.nonNull(realTimeContent)) {
            result.setContent(realTimeContent.getContent());
        }
        return result;
    }

    @Override
    public void closeRealTime(long rid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getAppType());
        params.put("rtId", rid);
        params.put("isClose", PublicEnum.Y.name());
        mapper.updateBy(params);
    }


    /**
     * 获取置顶的新闻数目
     *
     * @return
     */
    public Integer topCount() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("isTop", 'Y');
        List<RealTimeNews> topList = mapper.list(map);
        return CollectionUtils.isEmpty(topList) ? 0 : topList.size();
    }
}
