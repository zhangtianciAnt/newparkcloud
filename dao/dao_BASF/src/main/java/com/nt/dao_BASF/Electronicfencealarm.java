package com.nt.dao_BASF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 电子围栏报警单 实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Electronicfencealarm {
    //电子围栏报警单主键
    private String id;
    //deviceinformationid表主键
    private String deviceinformationid;
    //状态 0 可用 1 不可用
    private Integer STATUS;
    private Date CREATEON;
    private Date MODIFYON;
}
