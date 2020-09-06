package com.nt.dao_BASF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * 电子围栏设备状态
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Electronicfencestatus {
    @Id
    private String id;
    private String deviceinformationid;
    private String cameraid;
    private Integer warningstatus;
    private Integer shieldstatus;
}
