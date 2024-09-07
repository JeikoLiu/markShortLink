package com.jeiko.shortlink_demo.admin.dto.req;

import lombok.Data;

@Data
public class ShortLinkGroupSortReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序
     */
    private Integer sortOrder;
}
