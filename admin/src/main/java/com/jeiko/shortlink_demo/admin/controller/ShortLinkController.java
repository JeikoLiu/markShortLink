package com.jeiko.shortlink_demo.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.admin.remote.dto.ShortLinkActualRemoteService;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkBaseInfoRespDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkBatchCreateRespDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.jeiko.shortlink_demo.admin.utils.EasyExcelWebUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接后管控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    /**
     * 已重构为 SpringCloud Feign 调用
     */
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public BaseResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkActualRemoteService.createShortLink(requestParam);
    }

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        BaseResult<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接-SaaS短链接系统", ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public BaseResult<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortLink(requestParam);
        return ResultUtils.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public BaseResult<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam);
    }


}
