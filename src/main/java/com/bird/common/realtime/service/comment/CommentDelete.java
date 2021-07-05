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
public class CommentDelete {

    /**
     * 资讯ID
     */
    @NotNull(message = "参数不合法，资讯ID丢失")
    private Long rid;

    /**
     * 评论父ID
     */
    @NotNull(message = "参数不合法，评论父ID丢失")
    private Long cid;


    private Long rootId;
}
