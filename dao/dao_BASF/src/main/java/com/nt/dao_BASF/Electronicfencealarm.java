package com.nt.dao_BASF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 电子围栏报警单 实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Electronicfencealarm {
    @Id
    //电子围栏报警单主键
    private String id;
    //deviceinformationid表主键
    private String deviceinformationid;
    //状态 0 可用 1 不可用
    private Integer status;
    private Date createon;
    private Date modifyon;

    private Integer type;
    private String result;
    private String remark;

    /**
     * 设备编号
     */
    private String deviceno;

    /**
     * 设备名称
     */
    private String devicename;

    private Integer shieldstatus;

    private Integer warningstatus;

    @Transient
    private Electronicfencestatus electronicfencestatus;
    @Transient
    private Deviceinformation deviceinformation;
}
