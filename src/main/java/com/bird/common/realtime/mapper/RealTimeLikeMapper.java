package com.bird.common.realtime.mapper;

import com.bird.common.realtime.domain.RealTimeLike;
import com.bird.common.web.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RealTimeLikeMapper extends BaseMapper<Long,RealTimeLike> {

}