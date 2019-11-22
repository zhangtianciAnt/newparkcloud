package com.nt.service_BASF;

import com.nt.dao_BASF.Vehicleinformation;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: VehicleinformationServices
 * @Author: Wxz
 * @Description: VehicleInformationServices
 * @Date: 2019/11/14 13:11
 * @Version: 1.0
 */
public interface VehicleinformationServices {

    //获取车辆列表
    List<Vehicleinformation> list()throws Exception;

    //更新车辆信息
    void update(Vehicleinformation vehicleinformation, TokenModel tokenModel)throws Exception;
}
