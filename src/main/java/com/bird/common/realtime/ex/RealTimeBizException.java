package com.bird.common.realtime.ex;

import com.bird.common.web.ex.birdCommonException;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2020/7/15 15:43
 */
public class RealTimeBizException extends birdCommonException {

    public static final int SUCCESS = 200;

    public static final int FAILED = 500;


    public static final RealTimeBizException PARAM_ERR = new RealTimeBizException(
            400,  "参数不合法！");


    public static final RealTimeBizException SIGNED = new RealTimeBizException(
            800100,  "您已签到！");
    public static final RealTimeBizException EVENT_FINISH = new RealTimeBizException(
            800200,  "会议已结束！");
    public static final RealTimeBizException EVENT_START = new RealTimeBizException(
            800201,  "会议未开始！");
    public static final RealTimeBizException EVENT_STARTED = new RealTimeBizException(
            800202,  "会议已开始！");
    public static final RealTimeBizException EVENT_LEAVE = new RealTimeBizException(
            800300,  "会议已请假！");
    public static final RealTimeBizException UN_INVITE = new RealTimeBizException(
            800400,  "暂不支持非受邀用户参会！");
    public static final RealTimeBizException NON_APPLY = new RealTimeBizException(
            800500,  "您尚未报名！");
    public static final RealTimeBizException APPLY = new RealTimeBizException(
            800600,  "您已报名，不必重复报名！");
    public static final RealTimeBizException MEET_RECORD_EXISTS = new RealTimeBizException(
            800700,  "当前会议记录已存在，不必重复添加！");
    public static final RealTimeBizException EVENT_ABSENCE = new RealTimeBizException(
            800800,  "会议已缺席！");


    public static final RealTimeBizException OPE_ERR = new RealTimeBizException(
            900,  "当前操作不合法！");


    public static final RealTimeBizException TASK_EXECUTOR_MISSING = new RealTimeBizException(
            600100,  "任务执行人缺失！");
    public static final RealTimeBizException TASK_EXECUTOR_RESULT_MISSING = new RealTimeBizException(
            600101,  "任务执行结果缺失！");
    public static final RealTimeBizException TASK_INVALID = new RealTimeBizException(
            600102,  "任务执行完成或已取消！");
    public static final RealTimeBizException TASK_RUN_EXISTS = new RealTimeBizException(
            600103,  "任务指派信息已存在！");
    public static final RealTimeBizException TASK_STATUS_INVALID = new RealTimeBizException(
            600104,  "任务状态已失效！");
    public static final RealTimeBizException ROLE_OUT_OF_RANGE = new RealTimeBizException(
            600105,  "当前角色不支持此范围的组织！");
    public static final RealTimeBizException TASK_RUN_MISSING = new RealTimeBizException(
            600106,  "任务指派信息缺失！");
    public static final RealTimeBizException ORG_CHILD_ERROR = new RealTimeBizException(
            600107,  "当前组织下不允许新建组织！");
    public static final RealTimeBizException ORG_ROOT_ERROR = new RealTimeBizException(
            600108,  "不允许删除顶级组织！");


    public static final RealTimeBizException ORG_EXIST_MEMBER_ERROR = new RealTimeBizException(
            7001,  "组织下已有用户信息，请删除相关人员再来操作！");
    public static final RealTimeBizException ORG_EXIST_CHILD_ERROR = new RealTimeBizException(
            7002,  "删除该部门如包含下级部门将一并删除，是否继续？");
    public static final RealTimeBizException ORG_NAME_EXIST_ERROR = new RealTimeBizException(
            7003,  "该部门名称已存在！");
    public static final RealTimeBizException CONTENT_SENSITIVE = new RealTimeBizException(
            7004,  "内容含有敏感词！");


    public static final RealTimeBizException THEME_START = new RealTimeBizException(
            900201,  "活动尚未开始！");
    public static final RealTimeBizException THEME_END = new RealTimeBizException(
            900202,  "活动已经结束！");


    public static final RealTimeBizException TASK_END_TIME_INVALID = new RealTimeBizException(
            1000101,  "任务截至时间已经过期！");

    public static final RealTimeBizException COMMENT_ERR = new RealTimeBizException(
            30009,  "评论参数异常！");



    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 具体异常码
     */
    protected int code;

    public RealTimeBizException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public RealTimeBizException() {
        super();
    }

    public RealTimeBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public RealTimeBizException(Throwable cause) {
        super(cause);
    }

    public RealTimeBizException(String message) {
        super(message);
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    /**urity
     * 实例化异常
     *
     * @param msgFormat
     * @param args
     * @return
     */
    public birdCommonException newInstance(String msgFormat, Object... args) {
        return new birdCommonException(this.code, msgFormat, args);
    }
}
