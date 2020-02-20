package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class DeviceListVo implements Comparable<DeviceListVo>{

    /**
     * 项目id
     */
    private String id;

    /**
     * 机房编号
     */
    private String machineroomid;

    /**
     * 所属机柜编号
     */
    private String cabinetid;

    /**
     * 所属机柜名称
     */
    private String cabinetname;

    /**
     * 设备所属机柜的槽位编号
     */
    private String cabinetslotid;

    /**
     * 机房名
     */
    private String machineroomname;


    private String devicekey;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 设备类型
     */
    private String devicetype;

    /**
     * 当前用户
     */
    private String currentuser;

    /**
     * 当前登录用户
     */
    private String currentloginuser;

    /**
     * 所属公司
     */
    private String companyid;

    /**
     * 设备状态
     */
    private String devicestatus;

    /**
     * 设备位置
     */
    private String machineroomaddress;

    /**
     * 资源单板ID
     */
    private String boardid;

    /**
     * 资源单板数量
     */
    private String boardcnt;

    /**
     * 资源单板信息列表
     */
    private List<String> boardList;

    /**
     * 设备编号List
     */
    private List<String> deviceidList;

    @Override
    public int compareTo(DeviceListVo deviceListVo) {
        String deviceid1 = this.getDeviceid();
        String deviceid2 = deviceListVo.getDeviceid();
        return deviceid1.compareTo(deviceid2);
    }
}
