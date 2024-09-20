package com.jeiko.shortlink_demo.project.common.constant;

/**
 * Redis KEY 常量类
 */
public class RedisKeyConstant {
    /**
     * 短链接跳转前缀 KEY
     */
    public static final String GOTO_SHORT_LINK_KEY = "short-link:goto:%s";
    /**
     * 短链接跳转锁前缀 KEY
     */
    public static final String LOCK_GOTO_SHORT_LINK_KEY = "short-link:lock:goto:%s";
    /**
     * 短链接跳转空值前缀 KEY
     */
    public static final String GOTO_ISNULL_SHORT_LINK_KEY = "short-link:goto:is-null:%s";

    /**
     * 短链接修改分组 ID 锁前缀 Key
     */
    public static final String LOCK_GID_UPDATE_KEY = "short-link:lock:update-gid:%s";

    /**
     * 短链接延迟队列消费统计 Key
     */
    public static final String DELAY_QUEUE_STATS_KEY = "short-link:delay-queue:stats";

    /**
     * 短链接统计判断是否新用户缓存标识
     */
    public static final String SHORT_LINK_STATS_UV_KEY = "short-link:stats:uv:";

    /**
     * 短链接统计判断是否新 IP 缓存标识
     */
    public static final String SHORT_LINK_STATS_UIP_KEY = "short-link:stats:uip:";
}
