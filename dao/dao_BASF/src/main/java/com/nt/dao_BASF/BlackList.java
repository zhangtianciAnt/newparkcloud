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
@Table(name = "blacklist")
public class BlackList extends BaseModel {

    @Id
    private String blacklistid;

    /**
     * 驾驶员姓名
     */
    private String drivername;

    /**
     * 驾驶员身份证号
     */
    private String driveridnumber;


    /**
     * 违规时间
     */
    private String violationstime;

    /**
     * 违规类型
     */
    private String violationtype;

}
