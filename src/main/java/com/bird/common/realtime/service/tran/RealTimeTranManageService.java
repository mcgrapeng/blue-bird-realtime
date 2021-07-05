package com.bird.common.realtime.service.tran;

import com.google.common.collect.Maps;
import com.bird.common.realtime.domain.RealTimeTran;
import com.bird.common.realtime.mapper.RealTimeTranMapper;
import com.bird.common.security.WebUtils;
import com.bird.common.web.utils.SnowflakeIdWorker;
import com.bird.sso.api.IUserQueryFutureService;
import com.bird.sso.api.domain.SSOUser;
import com.bird.sso.api.ex.SSOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/11/25 17:13
 */
@Slf4j
@Service
public class RealTimeTranManageService {

    @Autowired
    private RealTimeTranMapper newsTranMapper;

    @Reference(check = false, version = "1.0")
    private IUserQueryFutureService userQueryService;

    /**
     * 添加转发记录
     *
     * @param rid
     * @param realTimeUserId
     * @param realTimeUserName
     * @param infoType
     */
    public void addRealTimeTran(String appType,long tranUserId , long rid, long realTimeUserId
            , String realTimeUserName, String infoType, String rtType) {
        RealTimeTran timeTran = getRealTimeTran(appType,rid, tranUserId);
        if (ObjectUtils.isEmpty(timeTran)) {
            doAddRealTimeTran(appType,rid, tranUserId, realTimeUserId
                    , realTimeUserName, infoType, rtType);
        } else {
            updRealTimeTran(appType,rid, tranUserId, timeTran.getTranTimes() == null ? 0 : timeTran.getTranTimes() + 1);
        }
    }


    /**
     * 添加转发记录
     *
     * @param rid
     * @param tranUserId
     * @param realTimeUserId
     * @param realTimeUserName
     * @param infoType
     */
    private void doAddRealTimeTran(String appType , long rid, long tranUserId, long realTimeUserId
            , String realTimeUserName, String infoType, String rtType) {

        CompletableFuture<SSOUser> futureSSOUser = userQueryService
                .getFutureSSOUser(appType
                        , realTimeUserId);


        RealTimeTran tran = new RealTimeTran();
        tran.setAppType(appType);
        tran.setTranId(SnowflakeIdWorker.build(5L).nextId());

        tran.setRtId(rid);
        tran.setUserId(realTimeUserId);
        tran.setUserName(realTimeUserName);
        tran.setInfoType(infoType);
        //转发的资讯类型
        tran.setRtType(rtType);
        tran.setTranUserId(tranUserId);
        tran.setCreateTime(Date.from(Instant.now()));
        tran.setIp(com.bird.common.web.utils.WebUtils.getIpAddress());
        tran.setTranTimes(1);


        SSOUser user = null;
        try {
            user = futureSSOUser.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        if (ObjectUtils.isEmpty(user)) {
            throw SSOException.USER_NO_EXITS;
        }

        String realName = user.getRealName();
        String username = user.getUserName();
        String orgName = user.getOrgName();
        String headImg = user.getHeadImg();

        tran.setTranUserName(realName);
        tran.setTranUserHeadImg(headImg);
        tran.setTranOrgName(orgName);
        tran.setCreater(username);

        try {
            newsTranMapper.insert(tran);
        } catch (DuplicateKeyException e) {
        }
    }


    private void updRealTimeTran(String appType , long rid, long tranUserId, int tranTimes) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        params.put("tranUserId", tranUserId);
        params.put("tranTimes", tranTimes);
        newsTranMapper.updateBy(params);
    }


    private RealTimeTran getRealTimeTran(String appType , long rid, long tranUserId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("appType", appType);
        params.put("rtId", rid);
        params.put("tranUserId", tranUserId);
        return newsTranMapper.get(params);
    }
}
