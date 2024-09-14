package com.jeiko.shortlink_demo.project.controller;

import com.jeiko.shortlink_demo.project.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.project.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkStatsReqDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkStatsRespDTO;
import com.jeiko.shortlink_demo.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {
    private final ShortLinkStatsService shortLinkStatsService;

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats")
    public BaseResult<ShortLinkStatsRespDTO> shortLinkStats(@RequestBody ShortLinkStatsReqDTO requestParam) {
        return ResultUtils.success(shortLinkStatsService.oneShortLinkStats(requestParam));
    }
}
