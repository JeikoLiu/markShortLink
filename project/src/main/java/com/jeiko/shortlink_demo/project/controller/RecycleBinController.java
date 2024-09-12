package com.jeiko.shortlink_demo.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeiko.shortlink_demo.project.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.project.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.project.dto.req.RecycleBinRecoverReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.RecycleBinSaveReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkPageRespDTO;
import com.jeiko.shortlink_demo.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 回收站管理控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    /**
     * 保存至回收站
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    public BaseResult<Void> save(@RequestBody RecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return ResultUtils.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    public BaseResult<IPage<ShortLinkPageRespDTO>> page(@RequestParam("gidList")List<String> gidList, @RequestParam("size") Integer size, @RequestParam("current") Integer current) {
        ShortLinkRecycleBinPageReqDTO requestParam = new ShortLinkRecycleBinPageReqDTO();
        requestParam.setGidList(gidList);
        requestParam.setCurrent(current);
        requestParam.setSize(size);
        return ResultUtils.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    public BaseResult<Void> recover(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverShortLink(requestParam);
        return ResultUtils.success();
    }

}
