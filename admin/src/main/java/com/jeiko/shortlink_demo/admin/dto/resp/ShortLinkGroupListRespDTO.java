package com.jeiko.shortlink_demo.admin.dto.resp;

import lombok.Data;

/**
 * 短链接分组功能返回实体对象
 */
@Data
public class ShortLinkGroupListRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组排序
     */
    private Integer sortOrder;
}
