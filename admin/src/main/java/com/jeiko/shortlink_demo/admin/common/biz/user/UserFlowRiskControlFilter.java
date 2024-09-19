package com.jeiko.shortlink_demo.admin.common.biz.user;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.jeiko.shortlink_demo.admin.common.convention.exception.ClientException;
import com.jeiko.shortlink_demo.admin.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.admin.config.UserFlowRiskControlConfiguration;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static com.jeiko.shortlink_demo.admin.common.convention.errorcode.BaseErrorCode.FLOW_LIMIT_ERROR;

/**
 * 用户操作流量风控过滤器
 */
@Slf4j
@RequiredArgsConstructor
public class UserFlowRiskControlFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserFlowRiskControlConfiguration userFlowRiskControlConfiguration;

    private static final String USER_FLOW_RISK_CONTROL_LUA_SCRIPT_PATH = "lua/user_flow_risk_control.lua";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 加载lua脚本文件
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(USER_FLOW_RISK_CONTROL_LUA_SCRIPT_PATH)));
        redisScript.setResultType(Long.class);
        String username = Optional.ofNullable(UserContext.getUsername()).orElse("other");
        Long result = null;
        try {
            result = stringRedisTemplate.execute(redisScript, Lists.newArrayList(username), userFlowRiskControlConfiguration.getTimeWindow());
        } catch (Throwable ex) {
            log.error("执行用户请求流量限制LUA脚本出错", ex);
            try {
                returnJson((HttpServletResponse) response, JSON.toJSONString(ResultUtils.failure(new ClientException(FLOW_LIMIT_ERROR))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (result == null || result > userFlowRiskControlConfiguration.getMaxAccessCount()) {
            try {
                returnJson((HttpServletResponse) response, JSON.toJSONString(ResultUtils.failure(new ClientException(FLOW_LIMIT_ERROR))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        }
    }
}
