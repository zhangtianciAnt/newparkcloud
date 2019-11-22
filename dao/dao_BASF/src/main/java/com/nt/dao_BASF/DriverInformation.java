package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: DriverInformation
 * @Author: Wxz
 * @Description: DriverInformation
 * @Date: 2019/11/22 14:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "driverinformation")
public class DriverInformation extends BaseModel {

    @Id
    private String driverid;

    /**
     * 驾驶员姓名
     */
    private String drivername;

    /**
     * 驾驶员身份证号
     */
    private String driveridnumber;

    /**
     * 黑名单
     */
    private String driverblacklist;

    /**
     * 违规时间
     */
    private String drivertime;

    /**
     * 违规类型
     */
    private String violationtype;

}
