package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: CurrentConnStatusVo
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/20
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentConnStatusVo {

    /**
     * 设备ID（主键）
     */
    private String id;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 当前连接状态（已连接、未连接、离线）
     */
    private String connstatus;
}
