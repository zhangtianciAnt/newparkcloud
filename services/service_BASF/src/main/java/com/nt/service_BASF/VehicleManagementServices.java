package com.nt.service_BASF;

import com.nt.dao_BASF.VehicleManagement;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF10204Services
 * @Author: Wxz
 * @Description:消防车辆管理接口
 * @Date: 2019/11/13 15:55
 * @Version: 1.0
 */
public interface VehicleManagementServices {

    //获取车辆状态列表
    List<VehicleManagement> list() throws Exception;
    //更新车辆状态
    void update(VehicleManagement vehicleManagement, TokenModel tokenModel)throws Exception;
}
