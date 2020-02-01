package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: DeviceListVo
 * @Description: 资源列表Vo
 * @Author: MYT
 * @CreateDate: 2020/1/31
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceListVo {

    /**
     * 设备id
     */
    private String deviceid;

    /**
     * 设备位置
     */
    private String machineroomaddress;

    /**
     * 资源单板数量
     */
    private int boardcnt;

}
