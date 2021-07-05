package com.bird.common.realtime.mapper;

import com.bird.common.realtime.domain.RealTimeTran;
import com.bird.common.web.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RealTimeTranMapper extends BaseMapper<Long,RealTimeTran> {

    int sumTranTimes(@Param("appType") String appType ,@Param("rtId") long rid);
}