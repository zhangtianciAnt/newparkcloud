package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: DeviceListVo
 * @Description: 设备列表Vo
 * @Author: SKAIXX
 * @CreateDate: 2020/2/1
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceListVo {

    /**
     * 项目id
     */
    private String id;

    /**
     * 机房编号&机柜编号
     */
    private String machineroomid;

    /**
     * 机房名
     */
    private String machineroomname;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 设备类型
     */
    private String devicetype;

    /**
     * 所属公司
     */
    private String companyid;

    /**
     * 设备状态
     */
    private String devicestatus;
}
