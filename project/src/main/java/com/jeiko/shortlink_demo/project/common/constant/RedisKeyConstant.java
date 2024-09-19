package com.jeiko.shortlink_demo.project.common.constant;

/**
 * Redis KEY 常量类
 */
public class RedisKeyConstant {
    /**
     * 短链接跳转前缀 KEY
     */
    public static final String GOTO_SHORT_LINK_KEY = "short-link_goto_%s";
    /**
     * 短链接跳转锁前缀 KEY
     */
    public static final String LOCK_GOTO_SHORT_LINK_KEY = "lock_short-link_goto_%s";
    /**
     * 短链接跳转空值前缀 KEY
     */
    public static final String GOTO_ISNULL_SHORT_LINK_KEY = "short-link_goto_is-null_%s";

    /**
     * 短链接修改分组 ID 锁前缀 Key
     */
    public static final String LOCK_GID_UPDATE_KEY = "short-link_lock_update-gid_%s";

    /**
     * 短链接延迟队列消费统计 Key
     */
    public static final String DELAY_QUEUE_STATS_KEY = "short-link_delay-queue:stats";
}
