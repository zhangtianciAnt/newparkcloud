package com.nt.dao_BASF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: CarInfoList
 * @Author: myt
 * @Description: CarInfoList
 * @Date: 2021/11/13 14:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInfoList {

    /**
     * 车牌号
     */
    private String truckNo;

    /**
     * 驾驶员姓名
     */
    private String drivername;

    /**
     * 手机号
     */
    private String phone;

}
