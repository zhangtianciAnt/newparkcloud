package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: BlackListVo
 * @Author: MYT
 * @Description: BlackListVo
 * @Date: 2021/11/30 14:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackListApiVo {

    /**
     * 违规类型
     */
    private String violationtype;

    /**
     *驾驶员名字
     */
    private String drivername;

    /**
     *身份证号码
     */
    private String driveridnumber;

}
