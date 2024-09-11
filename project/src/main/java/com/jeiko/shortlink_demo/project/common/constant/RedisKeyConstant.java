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
}
