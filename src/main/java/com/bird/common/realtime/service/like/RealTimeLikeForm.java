package com.bird.common.realtime.service.like;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/10 17:21
 */
@Setter
@Getter
public class RealTimeLikeForm {

    @NotNull(message = "资讯ID不能为空")
    private Long rtId;
    @NotBlank(message = "资讯类型不能为空")
    private String infoType;


}
