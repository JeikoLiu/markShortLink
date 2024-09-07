package com.jeiko.shortlink_demo.admin.controller;

import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.ShortLinkGroupListRespDTO;
import com.jeiko.shortlink_demo.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增短链接分组
     */
    @PostMapping("/api/short-link/admin/v1/group")
    public BaseResult<Void> saveGroup(@RequestBody ShortLinkGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return ResultUtils.success();
    }

    /**
     * 查询用户短链接分组集合
     * @return 用户短链接分组集合
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public BaseResult<List<ShortLinkGroupListRespDTO>> listGroup() {
        return ResultUtils.success(groupService.listGroup());
    }

}
