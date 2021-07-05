package com.bird.common.realtime.service.ment;

import com.bird.common.realtime.enums.MetaTypeEnum;
import com.bird.common.realtime.ex.RealTimeBizException;
import com.bird.common.realtime.service.RealTimeResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/16 20:52
 */
@Setter
@Getter
public class RealTimeMentForm {
    /**
     * 附件资源
     */
    private List<RealTimeResult.FileResource> fResources;

    /**
     * 引用资讯
     */
    @Deprecated
    private RealTimeResult.RealTimeResource rResources;

    /**
     * 媒体类型
     */
    @NotBlank(message = "信息流类型不能为空！")
    private String infoType;

    /**
     * 自媒体子类型
     */
    @NotBlank(message = "资讯类型不能为空！")
    private String rtType;


    /**
     * 标题
     */
    private String title;


    /**
     * 内容
     */
    //@NotBlank(message = "资讯内容不能为空！")
    private String content;



    private Long rtId;

    /**
     * 媒体类型
     */
    @NotBlank(message = "媒体类型不能为空！")
    private String metaType;


    public String getMetaType() {
        if (metaType.equals(MetaTypeEnum.PHOTO.name())) {
            if (CollectionUtils.isEmpty(fResources)) {
                throw RealTimeBizException.PARAM_ERR;
            }
        } else if (metaType.equals(MetaTypeEnum.CONTENT.name())) {
            if (StringUtils.isBlank(content)) {
                throw RealTimeBizException.PARAM_ERR;
            }
        }
        return metaType;
    }
}
