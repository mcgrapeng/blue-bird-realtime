package com.bird.common.realtime.service.news;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTimeContent;
import com.bird.common.realtime.domain.RealTimeNews;
import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.mapper.RealTimeNewsMapper;
import com.bird.common.realtime.mapper.RealTimeTranMapper;
import com.bird.common.realtime.service.IRealTimeQueryService;
import com.bird.common.realtime.service.RealTimeResult;
import com.bird.common.realtime.service.RealTimeService;
import com.bird.common.realtime.utils.FindImgUrlUtils;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.ex.birdCommonException;
import com.bird.common.web.ex.SSOException;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/5 13:05
 */
@Service
@Slf4j
public class RealTimeNewsService extends RealTimeService<RealTimeNews> implements IRealTimeQueryService<RealTimeNews> {

    @Autowired
    private RealTimeNewsMapper mapper;

    @Autowired
    private RealTimeTranMapper newsTranMapper;

    @Override
    public RealTimeResult getRealTime(String appType, long rtId) {

        Future<RealTimeContent> submit = threadPoolTaskExecutor
                .submit(() -> findContentByRtId(rtId));

        RealTimeNews n = findRealTimeByRtId(appType, rtId);
        if (n == null) {
            submit.cancel(true);
            throw birdCommonException.NOT_FOUND;
        }

        RealTimeContent nc = null;
        try {
            nc = submit.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        if (null == nc) {
            throw birdCommonException.NOT_FOUND;
        }

        return RealTimeResult
                .builder()
                .rtId(rtId)
                .publishTime(n.getCreateTime())
                .userName(n.getUserName())
                .orgName(StringUtils.isNotBlank(n.getParentOrgName())
                        ? StringUtils.join(n.getParentOrgName(),n.getOrgName()) : n.getOrgName())
                .userId(n.getUserId())
                .content(nc.getContent())
                .title(n.getTitle())
                .infoType(InfoTypeEnum.NEWS.name())
                .rtType(n.getRtType())
                .build();
    }

    @Override
    public PageBean<RealTimeResult> page(PageParam param, Condition cond) {
        PageBean<RealTimeNews> tPageBean = listPage(param, cond);
        if (tPageBean.getTotalCount() == 0) {
            return PageBean.of(tPageBean.getCurrentPage(), tPageBean.getNumPerPage());
        }

        String appType = cond.getStringParamValue("appType");

        long userId = com.bird.common.security.WebUtils.getUserId();

        List<RealTimeNews> recordList = tPageBean.getRecordList();
        List<Long> rids = recordList.stream().map(t -> t.getRtId())
                .collect(Collectors.toList());

        List<RealTimeContent> contents = listContentByRids(rids);

        Map<Long, RealTimeContent> data;

        if (CollectionUtils.isEmpty(contents)) {
            data = Maps.newHashMap();
        } else {
            data = contents.stream().collect(Collectors.toMap(RealTimeContent::getRtId
                    , a -> a, (k1, k2) -> k1));
        }

        long finalUserId = userId;
        List<RealTimeResult> l = recordList.stream().map(input -> {
            Long like = cacheCallback.get(Conts.RealTimeCount.LIKE.name()
                    , appType, input.getRtId());
            Long comm = cacheCallback.get(Conts.RealTimeCount.COMM.name()
                    , appType, input.getRtId());
            Long tran = cacheCallback.get(Conts.RealTimeCount.TRAN.name()
                    , appType, input.getRtId());
            Long read = cacheCallback.get(Conts.RealTimeCount.READ.name()
                    , appType, input.getRtId());

            Set<String> imgs;

            if (StringUtils.isBlank(input.getFirstImg())) {
                RealTimeContent content = data.get(input.getRtId());
                if (ObjectUtils.isNotEmpty(content)) {
                    imgs = FindImgUrlUtils.listImgStr(content.getContent());
                } else {
                    imgs = Sets.newHashSet();
                }
            } else {
                imgs = Sets.newHashSet();
                imgs.add(input.getFirstImg());
            }

            RealTimeResult i = RealTimeResult.builder()
                    .rtId(input.getRtId())
                    .title(input.getTitle())
                    .firstImg(imgs)
                    .isTop(input.getIsTop())
                    .isRec(input.getIsRec())
                    .userName(input.getUserName())
                    .orgName(StringUtils.isNotBlank(input.getParentOrgName())
                            ? StringUtils.join(input.getParentOrgName(),input.getOrgName()) : input.getOrgName())
                    .commNum(realTimeTools.getRealTimeOperationNum(comm))
                    .likeNum(realTimeTools.getRealTimeOperationNum(like))
                    .tranNum(realTimeTools.getRealTimeOperationNum(tran))
                    .readNum(realTimeTools.getRealTimeOperationNum(read))
                    .isLike(finalUserId == -1 ? PublicEnum.N.name() :
                            (realTimeLikeService.isRealTimeLike(input.getRtId()
                                    , finalUserId)
                                    ? PublicEnum.Y.name() : PublicEnum.N.name()))
                    .isOwner(String.valueOf(finalUserId)
                            .equals(input.getUserId().toString())
                            ? PublicEnum.Y.name() : PublicEnum.N.name())
                    .publishTime(input.getCreateTime())
                    .infoType(InfoTypeEnum.NEWS.name())
                    .build();
            return i;
        }).collect(Collectors.toList());
        return PageBean.of(tPageBean.getCurrentPage(), tPageBean.getNumPerPage()
                , tPageBean.getTotalCount(), l);
    }


    /**
     * 根据资源ID查询资讯
     *
     * @param rtId
     * @return
     */
    @Override
    public RealTimeNews findRealTimeByRtId(String appType, long rtId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rtId);
        RealTimeNews otPartyNews = mapper.get(params);

        if (ObjectUtils.isEmpty(otPartyNews)) return null;

        otPartyNews.setInfoType(InfoTypeEnum.NEWS.name());
        return otPartyNews;
    }


    /**
     * 删除资讯转发记录
     *
     * @param rid
     */
    private void delRealTimeTran(long rid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP));
        params.put("rtId", rid);
        newsTranMapper.deleteBy(params);

        cacheCallback.del(Conts.RealTimeCount.TRAN.name(), WebUtils.getHeader(JWTHelper.CLAIM_KEY_APP), rid);
    }

}
