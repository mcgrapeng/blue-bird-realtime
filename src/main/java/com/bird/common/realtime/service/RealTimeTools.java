package com.bird.common.realtime.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/8/25 16:35
 */
@Component
public final class RealTimeTools {


    /**
     * 获取操作数
     * @param count
     * @return
     */
    public String getRealTimeOperationNum(Number count) {
        String ret = "0";
        if (count instanceof Long) {
            if (null == count) {
                return ret;
            } else if (count.longValue() >= 10000L) {
                double c = count.doubleValue() / 10000;
                ret = StringUtils.join(c, "w");
            } else {
                ret = count.toString();
            }
        } else {
            if (null == count) {
                return ret;
            } else if (count.intValue() >= 10000) {
                double c = count.doubleValue() / 10000;
                ret = StringUtils.join(c, "w");
            } else {
                ret = count.toString();
            }
        }
        return ret;
    }
}
