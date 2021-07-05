package com.bird.common.realtime.mapper;

import com.bird.common.realtime.domain.RealTimeMess;
import com.bird.common.web.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Mapper
public interface RealTimeMessMapper extends BaseMapper<Long, RealTimeMess> {

    void updateReadStatus(Map<String, Object> params);
}