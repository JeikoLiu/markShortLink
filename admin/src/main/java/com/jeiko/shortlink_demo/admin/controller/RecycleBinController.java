package com.jeiko.shortlink_demo.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.admin.remote.dto.ShortLinkRemoteService;
import com.jeiko.shortlink_demo.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.jeiko.shortlink_demo.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站管理控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * 保存至回收站
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public BaseResult<Void> save(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkRemoteService.saveRecycleBin(requestParam);
        return ResultUtils.success();
    }

    /**
     * 回收站短链接分页查询
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public BaseResult<IPage<ShortLinkPageRespDTO>> page(@RequestBody ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageShortLink(requestParam);
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public BaseResult<Void> recover(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkRemoteService.recoverShortLink(requestParam);
        return ResultUtils.success();
    }

    /**
     * 移除短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    public BaseResult<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        shortLinkRemoteService.removeRecycleBin(requestParam);
        return ResultUtils.success();
    }

}
