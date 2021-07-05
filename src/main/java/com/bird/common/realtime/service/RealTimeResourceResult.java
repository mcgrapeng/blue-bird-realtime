package com.bird.common.realtime.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bird.common.realtime.domain.RealTimeMents;
import com.bird.common.realtime.enums.MetaTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/5/26 10:16
 */
@Getter
@Setter
public class RealTimeResourceResult {

    /**
     * 文件
     */
    protected List<FileResource> fResources;

    /**
     * 资讯
     */
    protected RealTimeResource rResource;

    /**
     * 视频
     */
    protected VideoResource vResource;


    /**
     * 构建资源
     *
     * @param input
     */
    public void buildResource(RealTimeMents input) {
        String metaType = input.getMetaType();
        if (metaType.equals(MetaTypeEnum.CONTENT.name())) {
            String rtResources = input.getRtResources();
            if (StringUtils.isNotBlank(rtResources)) {
                RealTimeResult.RealTimeResource rt = JSON.parseObject(rtResources
                        , RealTimeResult.RealTimeResource.class);
                this.setRResource(rt);
            }
        } else if (metaType.equals(MetaTypeEnum.PHOTO.name())) {
            String piResources = input.getPiResources();
            List<RealTimeResult.FileResource> rs = JSONObject.parseArray(piResources
                    , RealTimeResult.FileResource.class);
            this.setFResources(rs);
        } else if (metaType.equals(MetaTypeEnum.VIDEO.name())) {
            String viResources = input.getViResources();
            RealTimeResult.VideoResource vi = JSON.parseObject(viResources
                    , RealTimeResult.VideoResource.class);
            this.setVResource(vi);
        }
    }


    /**
     * 文件单元
     */
    @Getter
    @Setter
    public static class FileResource {
        /**
         * 顺序
         */
        private Integer ord;
        /**
         * 名称
         */
        private String name;
        /**
         * 路径
         */
        private String url;
        /**
         * KEY
         */
        private String key;
    }


    /**
     * 文件单元
     */
    @Getter
    @Setter
    public static class VideoResource {
        /**
         * 资讯名称
         */
        private String name;

        /**
         * 资讯ID
         */
        private Long rtId;

        /**
         * 路径
         */
        private String play;

        /**
         * 视频截图
         */
        private String cover;

        /**
         * 时长
         */
        private Integer duration;

        /**
         * 宽
         */
        private Integer width;

        /**
         * 高
         */
        private Integer height;
    }


    /**
     * 资讯单元
     */
    @Getter
    @Setter
    public static class RealTimeResource {
        /***资讯发布人*/
        private String userName;

        /***资讯发布人头像*/
        private String sendHeadImg;

        /**
         * 资讯发布人所属组织
         */
        private String sendOrgName;
        /**
         * 信息流标题
         */
        private String title;
        /**
         * 信息内容
         */
        private String content;
        /**
         * 首图
         */
        private String firstImg;


        /**
         * 媒体类型
         */
        private String metaType;

        /**
         * 资讯ID
         */
        private Long rtId;
        /**
         * 信息流类型
         */
        private String infoType;
        /**
         * 资讯类型
         */
        private String rtType;
        /**
         * 资讯子类型
         */
        private String secondRtType;


        public static RealTimeResource of() {
            return new RealTimeResource();
        }


        public RealTimeResource setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public RealTimeResource setSendHeadImg(String sendHeadImg) {
            this.sendHeadImg = sendHeadImg;
            return this;
        }

        public RealTimeResource setTitle(String title) {
            this.title = title;
            return this;
        }

        public RealTimeResource setContent(String content) {
            this.content = content;
            return this;
        }


        public RealTimeResource setFirstImg(String firstImg) {
            this.firstImg = firstImg;
            return this;
        }

        public RealTimeResource setRtId(Long rtId) {
            this.rtId = rtId;
            return this;
        }

        public RealTimeResource setInfoType(String infoType) {
            this.infoType = infoType;
            return this;
        }

        public RealTimeResource setRtType(String rtType) {
            this.rtType = rtType;
            return this;
        }

        public RealTimeResource setSecondRtType(String secondRtType) {
            this.secondRtType = secondRtType;
            return this;
        }

        public RealTimeResource setMetaType(String metaType) {
            this.metaType = metaType;
            return this;
        }

        public RealTimeResource setSendOrgName(String sendOrgName) {
            this.sendOrgName = sendOrgName;
            return this;
        }
    }

}
