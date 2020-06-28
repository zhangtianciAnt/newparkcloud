package com.nt.dao_Pfans.PFANS1000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwaretransferVo2 {
    /**
     * 申请人
     */
    private String softwaretransfer_id;

    /**
     * 申请人
     */
    private String user_id;

    /**
     * center
     */
    private String center_id;

    /**
     * group
     */
    private String group_id;

    /**
     * team
     */
    private String team_id;

    /**
     * 转出部门
     */
    private String ferrygroup_id;

    /**
     * 转入部门
     */
    private String tubegroup_id;

    /**
     * 资产管理番号
     */
    private String management;

    /**
     * 资产名称
     */
    private String assetname;

    /**
     * 创建时间
     */
    private Date createon;

    /**
     * 审批状态
     */
    private String status;


}
