package com.jeiko.shortlink_demo.project.utils;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.jeiko.shortlink_demo.project.common.constant.ShortLinkConstant;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Optional;

/**
 * 短链接工具类
 */
public class LinkUtils {
    /**
     * 获取短链接的缓存有效时间
     * @param validate 短链接的有效期标识 0：永久 1：有效时间
     * @return 缓存有效时间
     */
    public static long getCacheValidateTime(Date validate) {
        return Optional.ofNullable(validate)
                .map(each -> DateUtil.between(new Date(), each, DateUnit.MS))
                .orElse(ShortLinkConstant.DEFAULT_CACHE_VALIDATE_TIME);
    }

    /**
     * 获取用户真实IP
     * @param request 用户请求
     * @return 用户真实 IP
     */
    public static String getActualIp(HttpServletRequest request) {
        // TODO 重构获取用户真实 IP 方法
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
