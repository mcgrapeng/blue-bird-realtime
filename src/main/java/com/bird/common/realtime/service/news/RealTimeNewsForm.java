package com.bird.common.realtime.service.news;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/22 12:19
 */
@Getter
@Setter
public class RealTimeNewsForm {

    @NotNull(message = "资讯ID不能为空！",groups = {Upd.class})
    private Long rtId;
    @NotBlank(message = "资讯标题不能为空！",groups = {Add.class,Upd.class})
    private String title;
    @NotBlank(message = "资讯内容不能为空！",groups = {Add.class,Upd.class})
    private String content;
    @NotBlank(message = "信息流类型不能为空！",groups = {Add.class})
    private String infoType;
    @NotBlank(message = "资讯类型不能为空！",groups = {Add.class})
    private String rtType;


    private String firstImg;

    private long orgId;
    private String orgName;
    private String parentOrgName;
    private String secondRtType;
    private String isRec;


    public interface Add{}
    public interface Upd{}

}
