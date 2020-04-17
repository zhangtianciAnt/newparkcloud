package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Deviceinformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAndSqlUserinfoVo {

    //设备信息
    private Deviceinformation deviceinformation;

    /**
     * 紧急集合点
     */
    private int sqlUserInfoCnt;
}
