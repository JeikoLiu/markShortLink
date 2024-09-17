package com.jeiko.shortlink_demo.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeiko.shortlink_demo.project.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.project.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkCreateReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkPageReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkUpdateReqDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkCreateRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkGroupCountRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkPageRespDTO;
import com.jeiko.shortlink_demo.project.service.ShortLinkService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接服务控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    public BaseResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return ResultUtils.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/v1/create/batch")
    public BaseResult<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam) {
        return ResultUtils.success(shortLinkService.batchCreateShortLink(requestParam));
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/v1/page")
    public BaseResult<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return ResultUtils.success(shortLinkService.pageShortLink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/v1/update")
    public BaseResult<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return ResultUtils.success();
    }

    /**
     * 查询短链接分组及组内数量
     */
    @GetMapping("/api/short-link/v1/count")
    public BaseResult<List<ShortLinkGroupCountRespDTO>> countShortLinkListGroup(@RequestParam("requestParam") List<String> requestParam) {
        return ResultUtils.success(shortLinkService.countShortLinkListGroup(requestParam));
    }

    /**
     * 短链接跳转原始链接
     */
    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, ServletRequest request, ServletResponse response) {
        shortLinkService.restoreUrl(shortUri, request, response);
    }
}
