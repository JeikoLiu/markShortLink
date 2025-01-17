package com.jeiko.shortlink_demo.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeiko.shortlink_demo.project.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.project.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkStatsReqDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkStatsRespDTO;
import com.jeiko.shortlink_demo.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public BaseResult<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
//        @RequestParam("gid") String gid, @RequestParam("fullShortUrl") String fullShortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate
//        ShortLinkStatsReqDTO requestParam = new ShortLinkStatsReqDTO();
//        requestParam.setGid(gid);
//        requestParam.setFullShortUrl(fullShortUrl);
//        requestParam.setStartDate(startDate);
//        requestParam.setEndDate(endDate);
        return ResultUtils.success(shortLinkStatsService.oneShortLinkStats(requestParam));
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/v1/stats/group")
    public BaseResult<ShortLinkStatsRespDTO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        return ResultUtils.success(shortLinkStatsService.groupShortLinkStats(requestParam));
    }

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    public BaseResult<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return ResultUtils.success(shortLinkStatsService.shortLinkStatsAccessRecord(requestParam));
    }

    @GetMapping("/api/short-link/v1/stats/access-record/group")
    public BaseResult<IPage<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        return ResultUtils.success(shortLinkStatsService.groupShortLinkStatsAccessRecord(requestParam));
    }
}
