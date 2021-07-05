package com.bird.common.realtime.service.tran;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/3/24 17:38
 */

@Getter
@Setter
public class RealTimeTranForm {

    @NotNull(message = "参数不合法，资讯ID丢失")
    private Long rtId;
    @NotBlank(message = "参数不合法，信息流类型丢失")
    private String infoType;

    private String title;
    @NotBlank(message = "参数不合法，信息流内容丢失")
    private String content;
}
