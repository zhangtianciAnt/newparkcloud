package com.nt.dao_BASF.VO;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname VehicleinfoForDowann
 * @Description 门禁系统返回车辆信息
 * @Date 2020/9/8 15:30
 * @Author skaixx
 */
@Data
public class VehicleinfoForDowann {

    private String $id;

    private String TruckNo;

    private String Drivername;

    private String DriverID;

    private String Escortname;

    private String EscortID;

    private String Vehicle;

    private String Supplier;

    private String Transporter;

    private String TransType;

    private String LinkDepartment;

    private String GoodsName;

    private BigDecimal Weight;

    private DateTime EnterDateTime;
}
