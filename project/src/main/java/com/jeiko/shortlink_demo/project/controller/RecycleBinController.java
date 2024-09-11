package com.jeiko.shortlink_demo.project.controller;

import com.jeiko.shortlink_demo.project.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.project.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.project.dto.req.RecycleBinSaveReqDTO;
import com.jeiko.shortlink_demo.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
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
     * 保存至回收站
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    public BaseResult<Void> save(@RequestBody RecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return ResultUtils.success();
    }

}
