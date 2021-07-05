package com.bird.common.realtime.service.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/12/10 12:49
 */
@Setter
@Getter
public class CommentClose {

    @NotBlank(message = "参数不合法，应用类型丢失")
    private String appType;
    /**
     * 资讯ID
     */
    @NotNull(message = "参数不合法，资讯ID丢失")
    private Long rtId;

    /**
     * 评论ID
     */
    @NotNull(message = "参数不合法，评论ID丢失")
    private Long cid;

    /**
     * 评论根ID（根评论：资讯下的一级评论）（根评论ID：一级评论的cid） (一级评论的时候 ，rootId可以给0)
     * 二级评论起，rootId为上级评论的cid（或rootId）
     */
    @NotNull(message = "参数不合法，评论根ID丢失")
    private Long rootId;
}
