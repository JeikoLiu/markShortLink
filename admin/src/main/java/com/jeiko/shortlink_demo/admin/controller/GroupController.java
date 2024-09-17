package com.jeiko.shortlink_demo.admin.controller;

import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.jeiko.shortlink_demo.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.jeiko.shortlink_demo.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.ShortLinkGroupListRespDTO;
import com.jeiko.shortlink_demo.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户分组控制层
 */
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

    /**
     * 修改短链接分组名称
     */
    @PutMapping("/api/short-link/admin/v1/group")
    public BaseResult<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO requestParam) {
        groupService.updateGroup(requestParam);
        return ResultUtils.success();
    }

    /**
     * 删除短链接分组
     * @param gid 短链接标识
     */
     @DeleteMapping("/api/short-link/admin/v1/group")
    public BaseResult<Void> deleteGroup(@RequestParam String gid) {
        groupService.deleteGroup(gid);
        return ResultUtils.success();
     }

    /**
     * 短链接分组排序
     */
     @PostMapping("/api/short-link/admin/v1/sort")
    public BaseResult<Void> sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> requestParam) {
         groupService.sortGroup(requestParam);
         return ResultUtils.success();
     }

}
