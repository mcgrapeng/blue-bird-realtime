package com.bird.common.realtime.mapper;

import com.bird.common.realtime.domain.RealTimeRead;
import com.bird.common.web.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface RealTimeReadMapper extends BaseMapper<Long,RealTimeRead> {


    List<RealTimeRead> listOrderBy(Map<String, Object> params);

    int sumReadTimes(@Param("appType") String appType , @Param("rtId") long rid);
}