package com.bird.common.realtime.mapper;


import com.bird.common.realtime.domain.RealTimeContent;
import com.bird.common.web.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RealTimeContentMapper extends BaseMapper<Long,RealTimeContent> {

}