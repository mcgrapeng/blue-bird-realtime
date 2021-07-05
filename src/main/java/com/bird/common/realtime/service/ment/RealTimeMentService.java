package com.bird.common.realtime.service.ment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTimeMents;
import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.enums.MetaTypeEnum;
import com.bird.common.realtime.ex.RealTimeBizException;
import com.bird.common.realtime.mapper.RealTimeMentMapper;
import com.bird.common.realtime.service.IRealTimeQueryService;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.realtime.service.RealTimeService;
import com.bird.common.realtime.service.comment.RealTimeCommService;
import com.bird.common.realtime.service.like.RealTimeLikeService;
import com.bird.common.realtime.service.read.RealTimeReadManageService;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.ex.SSOException;
import com.bird.common.web.filter.sensitive.SensitiveFilter;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.SnowflakeIdWorker;
import com.bird.common.web.utils.WebUtils;
import com.bird.sso.api.IUserQueryFutureService;
import com.bird.sso.api.domain.SSOUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/10 16:43
 */
@Slf4j
@Service
public class RealTimeMentService extends RealTimeService<RealTimeMents> implements IRealTimeQueryService<RealTimeMents> {

    @Autowired
    private RealTimeMentMapper mapper;

    @Autowired
    private RealTimeCommService commentService;

    @Autowired
    private RealTimeLikeService realTimeLikeService;

    @Autowired
    private RealTimeReadManageService realTimeReadManageService;

    @Reference(check = false, version = "1.0")
    private IUserQueryFutureService userQueryService;


    /**
     * 发圈子、帖子
     *
     * @param rtId
     * @param infoType
     * @param rtType
     * @param mentStyle
     * @param metaType
     * @param callback
     * @param title
     * @param content
     */
    public void addMoments(Long rtId, String infoType, String rtType, String metaType, String mentStyle
            , IRealTimeMentCallback callback, String title, String content) {

        if (StringUtils.isNotBlank(content)) {
            // 使用默认单例（加载默认词典）
            SensitiveFilter filter = SensitiveFilter.DEFAULT;
            // 进行过滤
            String filted = filter.filter(content, '*');

            if (!filted.equals(content)) {
                throw RealTimeBizException.CONTENT_SENSITIVE;
            }
        }


        Long userId = com.bird.common.security.WebUtils.getUserId();
        String appType = com.bird.common.security.WebUtils.getAppType();

        CompletableFuture<SSOUser> ssoUser = userQueryService.getFutureSSOUser(appType
                , userId);


        RealTimeMents moments = new RealTimeMents();

        callback.buildResource(moments);

        moments.setAppType(appType);
        moments.setType(mentStyle);
        moments.setInfoType(infoType);
        moments.setRtType(rtType);
        moments.setRtId(ObjectUtils.isEmpty(rtId) ? SnowflakeIdWorker.build(2L).nextId() : rtId);

        moments.setIsClose(PublicEnum.N.name());
        moments.setTitle(title);
        moments.setContent(content);
        moments.setIsRec(PublicEnum.N.name());
        moments.setIsTop(PublicEnum.N.name());
        moments.setMetaType(metaType);


        SSOUser user = null;
        try {
            user = ssoUser.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        if (ObjectUtils.isEmpty(user)) {
            throw com.bird.sso.api.ex.SSOException.USER_NO_EXITS;
        }

        String realName = user.getRealName();
        String headImg = user.getHeadImg();
        String orgName = user.getOrgName();
        String username = user.getUserName();
        Long orgId = user.getOrgId();
        String parentOrgName = user.getParentOrgName();

        moments.setUserId(userId);
        moments.setUserName(realName);
        moments.setUserHeadImg(headImg);
        moments.setOrgId(orgId);
        moments.setOrgName(orgName);
        moments.setParentOrgName(parentOrgName);
        moments.setCreater(username);
        moments.setCreateTime(Date.from(Instant.now()));
        mapper.insert(moments);
    }


    /**
     * 根据userId查询
     *
     * @param pageParam
     * @param userId
     * @return
     */
    public PageBean<RealTimeResult> listMomentsByUserId(PageParam pageParam, long userId) {
        Condition cond = Condition.of();
        cond.add("appType", WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP));
        cond.add("userId", userId);
        cond.add("isClose", PublicEnum.N.name());
        return page(pageParam, cond);
    }


    @Override
    public RealTimeMents findRealTimeByRtId(String appType, long rtId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rtId);
        RealTimeMents otPartyMent = mapper.get(params);

        if (ObjectUtils.isEmpty(otPartyMent)) return null;


        otPartyMent.setInfoType(InfoTypeEnum.SELF.name());
        return otPartyMent;
    }


    /**
     * 删除党建圈
     *
     * @param mid
     */
    @Transactional(rollbackFor = Exception.class)
    public void delMoments(long mid) {
        Map<String, Object> params = Maps.newHashMap();
        String appType = WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP);
        params.put("appType", appType);
        params.put("rtId", mid);
        mapper.deleteBy(params);


        threadPoolTaskExecutor.execute(() -> {
            //删除评论
            commentService.deleteCommentByRid(appType,mid);
            //删除赞
            realTimeLikeService.delGiveLike(appType,mid);
            //删除阅读记录
            realTimeReadManageService.delRealTimeRead(appType,mid);
        });

        //不支持转发，不需要删除转发记录
    }

    @Override
    protected RealTimeResult getRealTime(String appType, long rtId) {

        RealTimeMents m = findRealTimeByRtId(appType, rtId);

        RealTimeResult ms = RealTimeResult.builder()
                .title(m.getTitle())
                .content(m.getContent())
                .rtId(m.getRtId())
                .userId(m.getUserId())
                .userName(m.getUserName())
                .publishTime(m.getCreateTime())
                .mentStyle(m.getType())
                .infoType(InfoTypeEnum.SELF.name())
                .rtType(m.getRtType())
                .build();

        if (m.getMetaType().equals(MetaTypeEnum.CONTENT.name())) {
            String rtResources = m.getRtResources();
            if (StringUtils.isNotBlank(rtResources)) {
                RealTimeResult.RealTimeResource rt = JSON.parseObject(rtResources
                        , RealTimeResult.RealTimeResource.class);
                ms.setRResource(rt);
            }
        } else if (m.getMetaType().equals(MetaTypeEnum.PHOTO.name())) {
            String piResources = m.getPiResources();
            List<RealTimeResult.FileResource> rs = JSONObject.parseArray(piResources
                    , RealTimeResult.FileResource.class);
            ms.setFResources(rs);
        } else if (m.getMetaType().equals(MetaTypeEnum.VIDEO.name())) {
            String viResources = m.getViResources();
            RealTimeResult.VideoResource vi = JSON.parseObject(viResources
                    , RealTimeResult.VideoResource.class);
            ms.setVResource(vi);
        }

        ms.setMetaType(m.getMetaType());
        ms.setUserHeadImg(m.getUserHeadImg());
        ms.setOrgName(StringUtils.isBlank(m.getParentOrgName()) ? m.getOrgName()
                : StringUtils.join(m.getParentOrgName(), m.getOrgName()));
        return ms;
    }

    @Override
    protected PageBean<RealTimeResult> page(PageParam param, Condition cond) {

        cond.add("isClose", PublicEnum.N.name());
        PageBean<RealTimeMents> pageBean = listPage(param, cond);

        if (pageBean.getTotalCount() == 0) {
            return PageBean.of(pageBean.getCurrentPage(), pageBean.getCurrentPage());
        }

        Long  userId = com.bird.common.security.WebUtils.getUserId();
        String appType = cond.getStringParamValue("appType");
        Long finalUserId = userId;
        List<RealTimeResult> l = pageBean.getRecordList().stream().map(input -> {

            Long like = cacheCallback.get(Conts.RealTimeCount.LIKE.name()
                    , appType, input.getRtId());
            Long comm = cacheCallback.get(Conts.RealTimeCount.COMM.name()
                    , appType, input.getRtId());
            Long tran = cacheCallback.get(Conts.RealTimeCount.TRAN.name()
                    , appType, input.getRtId());
            Long read = cacheCallback.get(Conts.RealTimeCount.READ.name()
                    , appType, input.getRtId());

            RealTimeResult m = RealTimeResult.builder()
                    .rtId(input.getRtId())
                    .title(input.getTitle())
                    .content(input.getContent())
                    .isTop(input.getIsTop())
                    .isRec(input.getIsRec())
                    .orgName(StringUtils.isBlank(input.getParentOrgName()) ? input.getOrgName()
                            : StringUtils.join(input.getParentOrgName(), input.getOrgName()))
                    .userName(input.getUserName())
                    .userHeadImg(input.getUserHeadImg())
                    .orgName(StringUtils.isNotBlank(input.getParentOrgName())
                            ? StringUtils.join(input.getParentOrgName(),input.getOrgName()) : input.getOrgName())
                    .likeNum(realTimeTools.getRealTimeOperationNum(like))
                    .commNum(realTimeTools.getRealTimeOperationNum(comm))
                    .tranNum(realTimeTools.getRealTimeOperationNum(tran))
                    .readNum(realTimeTools.getRealTimeOperationNum(read))
                    .isLike(finalUserId == -1 ? PublicEnum.N.name() :
                            (realTimeLikeService.isRealTimeLike(input.getRtId()
                                    , finalUserId)
                                    ? PublicEnum.Y.name() : PublicEnum.N.name()))
                    .isOwner(String.valueOf(finalUserId)
                            .equals(input.getUserId().toString())
                            ? PublicEnum.Y.name() : PublicEnum.N.name())
                    .isRec(input.getIsRec())
                    .mentStyle(input.getType())
                    .metaType(input.getMetaType())
                    .publishTime(input.getCreateTime())
                    .infoType(InfoTypeEnum.SELF.name())
                    .build();

            m.buildResource(input);
            return m;
        }).collect(Collectors.toList());

        return new PageBean(pageBean.getCurrentPage(), pageBean.getNumPerPage()
                , pageBean.getTotalCount()
                , l);
    }
}
