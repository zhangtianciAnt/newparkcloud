package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: DeviceinfoVo
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/7
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceinfoVo {

    /**
     * 设备ID
     */
    private String id;

    /**
     * 机房ID
     */
    private String machineroomid;

    /**
     * 机柜ID
     */
    private String cabinetid;

    /**
     * 所在机柜槽位
     */
    private String cabinetslotid;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 设备类型
     */
    private String devicetype;

    /**
     * 设备所属公司ID
     */
    private String companyid;

    /**
     * 设备内的板卡列表
     */
    private List<BoardinfoListVo> boardinfoList;
}
