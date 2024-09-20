package com.jeiko.shortlink_demo.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.remote.dto.ShortLinkActualRemoteService;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkGroupStatsReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkStatsReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkStatsRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接监控控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats")
    public BaseResult<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return shortLinkActualRemoteService.oneShortLinkStats(requestParam);
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/group")
    public BaseResult<ShortLinkStatsRespDTO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        return shortLinkActualRemoteService.groupShortLinkStats(requestParam);
    }

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record")
    public BaseResult<Page<ShortLinkStatsAccessRecordRespDTO>> shortLinkAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam){
        return shortLinkActualRemoteService.shortLinkStatsAccessRecord(requestParam);
    }

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record/group")
    public BaseResult<Page<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        return shortLinkActualRemoteService.groupShortLinkStatsAccessRecord(requestParam);
    }
}
