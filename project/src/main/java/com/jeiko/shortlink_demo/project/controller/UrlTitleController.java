package com.jeiko.shortlink_demo.project.controller;

import com.jeiko.shortlink_demo.project.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.project.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.project.service.UrlTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlTitleController {

    private final UrlTitleService urlTitleService;

    /**
     * 根据url过去目标网站的标题
     */
    @GetMapping("/api/short-link/v1/title")
    public BaseResult<String> getTitleByUrl(@RequestParam("url") String url) {
        return ResultUtils.success(urlTitleService.getTitleByUrl(url));
    }
}
