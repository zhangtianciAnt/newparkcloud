package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.VehicleManagement;
import com.nt.service_BASF.VehicleManagementServices;
import com.nt.service_BASF.mapper.VehicleManagementMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF10204ServicesImpl
 * @Author: Wxz
 * @Description: BASF车辆状态模块实现类
 * @Date: 2019/11/13 16：28
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleManagementServicesImpl implements VehicleManagementServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private VehicleManagementMapper vehicleManagementMapper;

    /**
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆状态列表
     * @Return void
     * @Date 2019/11/13 16：15
     */
    @Override
    public List<VehicleManagement> list() throws Exception {
        VehicleManagement vehicleManagement = new VehicleManagement();
        return vehicleManagementMapper.select(vehicleManagement);
    }

    /**
     * @param vehicleManagement
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆状态
     * @Return void
     * @Date 2019/11/13 16：20
     */
    @Override
    public void update(VehicleManagement vehicleManagement, TokenModel tokenModel) throws Exception {
        vehicleManagement.preUpdate(tokenModel);
        vehicleManagementMapper.updateByPrimaryKey(vehicleManagement);
    }
}
