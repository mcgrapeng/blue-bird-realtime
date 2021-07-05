package com.bird.common.realtime.mapper;


import com.bird.common.realtime.domain.RealTimeComment;
import com.bird.common.web.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RealTimeCommentMapper extends BaseMapper<Long,RealTimeComment> {

    void updateClose(@Param("cid") Long cid , @Param("appType") String appType ,@Param("isClose")  String isClose ,@Param("editor") String editor);
}