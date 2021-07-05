package com.bird.common.realtime.service.comment;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.bird.common.realtime.cache.CacheCallback;
import com.bird.common.realtime.conts.Conts;
import com.bird.common.realtime.domain.RealTime;
import com.bird.common.realtime.domain.RealTimeComment;
import com.bird.common.realtime.enums.InfoTypeEnum;
import com.bird.common.realtime.ex.RealTimeBizException;
import com.bird.common.realtime.mapper.RealTimeCommentMapper;
import com.bird.common.realtime.service.RealTimeFactory;
import com.bird.common.realtime.service.like.RealTimeLikeService;
import com.bird.common.realtime.service.noti.RealTimeMessContext;
import com.bird.common.security.WebUtils;
import com.bird.common.web.enums.PublicEnum;
import com.bird.common.web.ex.birdCommonException;
import com.bird.common.web.ex.SSOException;
import com.bird.common.web.filter.sensitive.SensitiveFilter;
import com.bird.common.web.mapper.BaseService;
import com.bird.common.web.mapper.Condition;
import com.bird.common.web.mapper.Direction;
import com.bird.common.web.page.PageBean;
import com.bird.common.web.page.PageParam;
import com.bird.common.web.security.JWTHelper;
import com.bird.common.web.thread.ContextAwarePoolExecutor;
import com.bird.common.web.utils.SnowflakeIdWorker;
import com.bird.sso.api.IUserQueryFutureService;
import com.bird.sso.api.domain.SSOUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/6/5 19:10
 */
@Slf4j
@Service
public class RealTimeCommService extends BaseService<Long, RealTimeComment> {

    @Autowired
    private RealTimeCommentMapper mapper;

    @Autowired
    private CacheCallback cacheCallback;

    @Reference(check = false, version = "1.0")
    private IUserQueryFutureService userQueryService;

    @Autowired
    private ContextAwarePoolExecutor threadPoolTaskExecutor;

    @Autowired
    private RealTimeLikeService realTimeLikeService;

    @Autowired
    private RealTimeFactory factory;

    @Autowired
    private RealTimeCommQueryService realTimeCommQueryService;


    @Autowired
    private RealTimeMessContext messContext;


    /**
     * 评论列表
     *
     * @return
     */
    public PageBean<CommentResult> tree(PageParam pageParam, long rid) {

        String appType = WebUtils.getAppType();

        Condition cond = Condition.of();
        cond.add("appType", appType);
        cond.add("rtId", rid);
        cond.add("pid", 0L);
        cond.add("isClose", PublicEnum.N.name());
        cond.addSort("create_time", Direction.DESC.name());
        PageBean<RealTimeComment> pageBean = listPage(pageParam, cond);

        if (pageBean.getTotalCount() == 0) {
            return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage());
        }

        Long userId = WebUtils.getUserId();

        long finalUserId = userId;
        List<CommentResult> l = pageBean.getRecordList().stream().map(input -> {
            CommentResult n = CommentResult.of()
                    .setCid(input.getCid())
                    .setPid(input.getPid())
                    .setRootId(input.getRootId())
                    .setRtId(input.getRtId())
                    .setContent(input.getContent())
                    .setCommentUserName(input.getCommentUserName())
                    .setRevNum(cacheCallback.getCOMM(appType, rid, input.getRootId()))
                    .setIsOwner(finalUserId == -1 ? PublicEnum.N.name() : (String.valueOf(finalUserId).equals(String.valueOf(input.getCommentUserId()))
                            ? PublicEnum.Y.name() : PublicEnum.N.name()))
                    .setIsLike(finalUserId == -1 ? PublicEnum.N.name() : (realTimeLikeService.isRealTimeLike(input.getCid(), finalUserId)
                            ? PublicEnum.Y.name() : PublicEnum.N.name()))
                    .setLikeNum(cacheCallback.get(Conts.RealTimeCount.LIKE.name()
                            , appType
                            , input.getCid()))
                    .setInfoType(input.getInfoType())
                    .setCommentUserId(input.getCommentUserId())
                    .setCommentUserHeadImg(input.getCommentUserHeadImg())
                    .setCommentTime(input.getCreateTime());
            return n;

        }).collect(Collectors.toList());

        return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage()
                , pageBean.getTotalCount()
                , l);
    }


    /**
     * 评论
     *
     * @param realTimeComment
     */
    @Transactional(rollbackFor = Exception.class)
    public void addComment(CommentCreate realTimeComment) {
        log.info("#############addComment################realTimeComment={}"
                , JSON.toJSONString(realTimeComment));

        // 使用默认单例（加载默认词典）
        SensitiveFilter filter = SensitiveFilter.DEFAULT;
        // 进行过滤
        String filted = filter.filter(realTimeComment.getContent(), '*');

        if (!filted.equals(realTimeComment.getContent())) {
            throw RealTimeBizException.CONTENT_SENSITIVE;
        }

        long cid = SnowflakeIdWorker.build(3L).nextId();
        long pid = realTimeComment.getPid();


        Long userId = WebUtils.getUserId();
        String appType = WebUtils.getAppType();

        CompletableFuture<SSOUser> ssoUser = userQueryService.getFutureSSOUser(appType
                , userId);


        Future<RealTime> submitRealTime = threadPoolTaskExecutor.submit(()
                -> factory.getRealTimeQueryService(realTimeComment.getInfoType())
                .findRealTimeByRtId(appType, realTimeComment.getRtId()));

        RealTimeComment comment = new RealTimeComment(PublicEnum.N.name());
        comment.setAppType(appType);
        comment.setCid(cid);
        comment.setPid(pid);
        comment.setCommentUserId(userId);
        comment.setRtType(realTimeComment.getRtType());
        comment.setRtId(realTimeComment.getRtId());
        comment.setRtInfoType(realTimeComment.getInfoType());

        comment.setCreateTime(Date.from(Instant.now()));
        comment.setContent(realTimeComment.getContent());
        comment.setRootId(realTimeComment.getRootId() == 0L ? cid : realTimeComment.getRootId());
        comment.setInfoType(InfoTypeEnum.COMM.name());

        SSOUser user = null;
        try {
            user = ssoUser.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        if (ObjectUtils.isEmpty(user)) {
            throw com.bird.sso.api.ex.SSOException.USER_NO_EXITS;
        }

        comment.setCreater(user.getUserName());
        comment.setCommentUserName(user.getRealName());
        comment.setCommentUserHeadImg(user.getHeadImg());
        comment.setCommentOrgName(StringUtils.isNotBlank(user.getParentOrgName())
                ? StringUtils.join(user.getParentOrgName(),user.getOrgName()) : user.getOrgName());
        RealTime realTime = null;
        try {
            realTime = submitRealTime.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        if (ObjectUtils.isEmpty(realTime)) {
            throw birdCommonException.NOT_FOUND;
        }
        comment.setUserId(realTime.getUserId());
        comment.setUserName(realTime.getUserName());

        mapper.insert(comment);

        if (comment.getPid() == 0L) {
            cacheCallback.incrCOMM(appType, comment.getRtId());
        } else {
            //if (comment.getRootId().longValue() == comment.getPid().longValue()) {
            cacheCallback.incrCOMM(appType, comment.getRtId(), comment.getRootId());
            // }
        }

        cacheCallback.incrCOMM(StringUtils.join(appType, ":FULL"), comment.getRtId());

        messContext.notifyMessage(RealTimeMessContext.Strategy.COMM.name()
                , comment.getCid(), Boolean.TRUE);
    }


    /**
     * 根评论详情
     *
     * @param rootId
     * @return
     */
    public RootCommentResult viewRootComment(long rootId) {
        RealTimeComment comment = realTimeCommQueryService.findCommentByCid(WebUtils.getAppType(),rootId);

        Long ssoUserId = WebUtils.getUserId();

        RootCommentResult result = new RootCommentResult();
        result.setCid(comment.getCid());
        result.setCommentTime(comment.getCreateTime());
        result.setCommentUserHeadImg(comment.getCommentUserHeadImg());
        result.setCommentUserName(comment.getCommentUserName());
        result.setPid(comment.getPid());
        result.setRootId(comment.getRootId());
        result.setIsOwner(ssoUserId == -1 ? PublicEnum.N.name() : (String.valueOf(ssoUserId).equals(String.valueOf(comment.getCommentUserId()))
                ? PublicEnum.Y.name() : PublicEnum.N.name()));

        result.setIsLike(ssoUserId == -1 ? PublicEnum.N.name() : (realTimeLikeService.isRealTimeLike(rootId, ssoUserId)
                ? PublicEnum.Y.name() : PublicEnum.N.name()));

        String appType = WebUtils.getAppType();
        result.setLikeNum(cacheCallback.get(Conts.RealTimeCount.LIKE.name(), appType
                , rootId));
        result.setRevNum(cacheCallback.getCOMM(appType
                , comment.getRtId(), rootId));
        result.setContent(comment.getContent());
        return result;
    }

    /**
     * 资讯评论详情列表
     *
     * @param rootId(评论详情的cid)
     * @return
     */
    public PageBean<CommentResult> listCommentByRootId(long rid, long rootId, PageParam pageParam) {
        String appType = WebUtils.getAppType();
        Long ssoUserId = WebUtils.getUserId();

        Condition condition = Condition.of();
        condition.add("appType", appType);
        condition.add("rtId", rid);
        condition.add("nePid", 0);//很重要 pid<>0
        condition.add("rootId", rootId);
        condition.add("isClose", PublicEnum.N.name());
        condition.addSort("create_time", Direction.DESC.name());
        PageBean<RealTimeComment> pageBean = listPage(pageParam
                , condition);

        if (pageBean.getTotalCount() == 0) {
            return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage());
        }


        List<Long> pids = pageBean.getRecordList().stream().filter(t ->
                t.getPid() != 0)
                .map(x -> x.getPid())
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(p -> p
                                ))), ArrayList::new));

        Map<Long, RealTimeComment> data;
        if (CollectionUtils.isNotEmpty(pids)) {
            List<RealTimeComment> comments = realTimeCommQueryService.findCommentByCids(pids);
            data = comments.stream().collect(Collectors.toMap(RealTimeComment::getCid
                    , a -> a, (k1, k2) -> k1));
        } else {
            data = Maps.newHashMap();
        }

        long finalSsoUserId = ssoUserId;
        List<CommentResult> l = pageBean.getRecordList().stream().map(input -> {
            CommentResult c = CommentResult.of()
                    .setCid(input.getCid())
                    .setPid(input.getPid())
                    .setRootId(input.getRootId())
                    .setRtId(input.getRtId())
                    .setContent(input.getContent())
                    .setCommentUserName(input.getCommentUserName())
                    .setRevNum(cacheCallback.getCOMM(appType, rid, input.getCid()))
                    .setIsOwner(finalSsoUserId == -1 ? PublicEnum.N.name() : (String.valueOf(finalSsoUserId).equals(String.valueOf(input.getCommentUserId()))
                            ? PublicEnum.Y.name() : PublicEnum.N.name()))
                    .setIsLike(finalSsoUserId == -1 ? PublicEnum.N.name() : (realTimeLikeService.isRealTimeLike(input.getCid(), finalSsoUserId)
                            ? PublicEnum.Y.name() : PublicEnum.N.name()))
                    .setLikeNum(cacheCallback.get(Conts.RealTimeCount.LIKE.name(), appType
                            , input.getCid()))
                    .setCommentUserId(input.getCommentUserId())
                    .setCommentUserHeadImg(input.getCommentUserHeadImg())
                    .setCommentTime(input.getCreateTime());

            RealTimeComment comment = data.get(input.getPid());
            if (ObjectUtils.isNotEmpty(comment)) {
                c.setRepliedCommentUserName(comment.getCommentUserName());
            }
            return c;
        }).collect(Collectors.toList());


        return PageBean.of(pageBean.getCurrentPage(), pageBean.getNumPerPage(),
                pageBean.getTotalCount()
                , l);
    }


    /**
     * 删除评论
     *
     * @param rid
     * @param cid
     */
    @Transactional(rollbackFor = Exception.class)
    public void delComment(long rid, Long rootId, long cid) {

        String appType =WebUtils.getAppType();

        if (String.valueOf(rootId).equals(String.valueOf(cid)) ) {
            cacheCallback.decrCOMM(appType, rid);
        } else {
            cacheCallback.decrCOMM(appType, rid, rootId);
        }


        threadPoolTaskExecutor.execute(() -> {
            delComment(appType,cid);
//            realTimeMessService.delRealTimeMessage(String.valueOf(cid)
//                    , InfoTypeEnum.COMM.name(), WebUtils.getSSOUser().getUserId());
        });
    }


    /**
     * 根据资讯ID删除评论
     *
     * @param rid
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCommentByRid(String appType ,long rid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("rtId", rid);
        params.put("appType", appType);
        mapper.deleteBy(params);

        //删除资讯的所有评论
        cacheCallback.delMatch(Conts.RealTimeCount.COMM.name(), appType, rid);
    }


    public int commTotal(long rid) {
        String appType = WebUtils.getAppType();

        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        return mapper.count(params);
    }


    private void delComment(String appType , long cid) {
        delCommentByCid(appType,cid);
        List<RealTimeComment> childs = realTimeCommQueryService.findCommentByPid(cid);
        if (CollectionUtils.isNotEmpty(childs)) {
            for (RealTimeComment r : childs) {
                delComment(appType,r.getCid());
            }
        }
    }


    /**
     * 删除评论
     *
     * @param cid
     */
    private void delCommentByCid(String appType , long cid) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("cid", cid);
        mapper.deleteBy(params);
    }


    @Component
    class TreeHandler implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        public TreeBuilder build(List<CommentResult> data) {
            TreeBuilder build = applicationContext.getBean(TreeBuilder.class);
            return build.build(data);
        }

        @Component
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        class TreeBuilder {

            private List<CommentResult> data;

            public TreeBuilder build(List<CommentResult> data) {
                this.data = data;
                return this;
            }

            public List<CommentResult> builTree() {
                List<CommentResult> tree = Lists.newArrayList();
                for (CommentResult node : getRootNode()) {
                    node = buildChilTree(node);
                    tree.add(node);
                }
                destory();
                return tree;
            }


            private CommentResult buildChilTree(CommentResult pNode) {
                List<CommentResult> chils = Lists.newArrayList();
                for (CommentResult node : this.data) {
                    if (node.getPid().equals(pNode.getCid())) {
                        chils.add(buildChilTree(node));
                    }
                }
                //pNode.setChilds(chils);
                //pNode.setRevNum(chils.size());
                chils.clear();
                return pNode;
            }

            private List<CommentResult> getRootNode() {
                List<CommentResult> rootMenuLists = Lists.newArrayList();
                for (CommentResult node : this.data) {
                    if (node.getPid() == 0) {
                        rootMenuLists.add(node);
                    }
                }
                return rootMenuLists;
            }

            private void destory() {
                if (CollectionUtils.isNotEmpty(this.data)) {
                    this.data.clear();
                    this.data = null;
                }
            }
        }
    }
}
