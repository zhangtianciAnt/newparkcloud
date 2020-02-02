package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: UserAuthListVo
 * @Description: 用户权限及设备列表信息Vo
 * @Author: MYT
 * @CreateDate: 2020/2/1
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthListVo {

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 获取信息权限
     */
    private String infoauth;

    /**
     * 文件管理权限
     */
    private String fileauth;

    /**
     * 权限管理权限
     */
    private String authmanage;

    /**
     * 设备权限
     */
    private String machineauth;

}
