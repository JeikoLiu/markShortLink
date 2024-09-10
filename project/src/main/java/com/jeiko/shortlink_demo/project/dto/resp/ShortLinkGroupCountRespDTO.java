package com.jeiko.shortlink_demo.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接分组及组内数量查询返回实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkGroupCountRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接数量
     */
    private Integer shortLinkCount;
}
