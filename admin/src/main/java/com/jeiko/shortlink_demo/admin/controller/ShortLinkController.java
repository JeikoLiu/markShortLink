package com.jeiko.shortlink_demo.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.admin.remote.dto.ShortLinkRemoteService;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.*;

/**
 * 短链接后管控制层
 */
@RestController
public class ShortLinkController {

    /**
     * 后续重构为 SpringCloud Feign 调用
     */
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public BaseResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * 修改短链接
     */
    @PutMapping("/api/short-link/admin/v1/update")
    public BaseResult<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkRemoteService.updateShortLink(requestParam);
        return ResultUtils.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public BaseResult<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }


}