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
     *车辆所属公司
     */
    private String vehiclecompany;

    /**
     *车辆类型
     */
    private String vehicletype;

    /**
     *创建时间
     */
    private String createtime;

    /**
     * 更新时间
     */
    private String updatetime;



}
