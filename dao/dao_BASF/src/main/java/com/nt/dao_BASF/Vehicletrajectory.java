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
 * @ClassName: Vehicletrajectory
 * @Author: Wxz
 * @Description: Vehicletrajectory
 * @Date: 2019/11/14 11:40
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicletrajectory")
public class Vehicletrajectory extends BaseModel {

    @Id
    private String vehicletrajectoryid;

    /**
     * 主表外键
     */
    private String vehicleinformationid;

    /**
     *经度
     */
    private String longitude;

    /**
     *纬度
     */
    private String latitude;

    /**
     *方向
     */
    private String direction;

    /**
     * 速度
     */
    private String speed;

    /**
     * 违规类型
     */
    private String vehicleviolationtype;

}
