package com.nt.dao_BASF;

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
 * @ClassName: Vehicleinformation
 * @Author: Wxz
 * @Description: Vehicleinformation
 * @Date: 2019/11/14 11:39
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicleinformation")
public class Vehicleinformation extends BaseModel {
    @Id
    private String vehicleinformationid;

    /**
     *车牌号
     */
    private String vehiclenumber;

    /**
     *驾驶员
     */
    private String driver;

    /**
     *驾驶员身份证
     */
    private String driverid;

    /**
     *押运员
     */
    private String escortname;

    /**
     * 押运员身份证
     */
    private String escortid;

    /**
     *车辆类型
     */
    private String vehicletype;

    /**
     *物流服务商
     */
    private String supplier;

    /**
     *运输公司名称
     */
    private String transporter;

    /**
     *进场目的
     */
    private String transtype;

    /**
     *联系部门
     */
    private String linkdepartment;

    /**
     *物料名称
     */
    private String goodsname;

    /**
     *车辆（挂车）核定载重量
     */
    private String weight;

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
     *是否是危险品车辆
     * 0：是  1：否
     */
    private String ishazardous;

    /**
     *GPS
     */
    private String gps;

    /**
     *实时速度
     */
    private String speed;

}
