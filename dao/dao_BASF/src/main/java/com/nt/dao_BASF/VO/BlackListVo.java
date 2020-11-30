package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: BlackListVo
 * @Author: Wxz
 * @Description: BlackListVo
 * @Date: 2019/11/22 14:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackListVo extends BaseModel {

    /**
     * 组合ID
     */
    private String combinationid;

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
    private Date violationstime;

    /**
     * 违规类型
     */
    private String violationtype;

    /**
     *车牌号
     */
    private String vehiclenumber;

    /**
     *车辆类型
     */
    private String vehicletype;

    /**
     *物流服务商
     */
    private String supplier;

    /**
     *进场目的
     */
    private String transtype;

    /**
     *入厂时间
     */
    private Date intime;

    /**
     *出厂时间
     */
    private Date outtime;

    /**
     *目的地
     */
    private String destination;

    /**
     *联系部门
     */
    private String linkdepartment;

}
