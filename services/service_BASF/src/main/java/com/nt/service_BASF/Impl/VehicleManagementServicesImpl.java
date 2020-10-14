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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

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
    public List<VehicleManagement> list(VehicleManagement vehicleManagement) throws Exception {
        return vehicleManagementMapper.select(vehicleManagement);
    }

    @Override
    public int insert(VehicleManagement vehicleManagement, TokenModel tokenModel) throws Exception {
        vehicleManagement.preInsert(tokenModel);
        vehicleManagement.setVehiclemanagementid(UUID.randomUUID().toString());
        return vehicleManagementMapper.insert(vehicleManagement);
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
        if (tokenModel != null) {
            vehicleManagement.preUpdate(tokenModel);
        }
        vehicleManagementMapper.updateByPrimaryKeySelective(vehicleManagement);
    }
}
