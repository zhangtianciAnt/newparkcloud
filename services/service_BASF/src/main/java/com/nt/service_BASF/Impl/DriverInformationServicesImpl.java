package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.DriverInformation;
import com.nt.service_BASF.DriverInformationServices;
import com.nt.service_BASF.mapper.DriverInformationMapper;
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
 * @ClassName: DriverInformationServicesImpl
 * @Author: Wxz
 * @Description: DriverInformationServicesImpl
 * @Date: 2019/11/22 15:02
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DriverInformationServicesImpl implements DriverInformationServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private DriverInformationMapper driverInformationMapper;

    /**
     * @param driverInformation
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取驾驶员列表
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/22 15:05
     */
    @Override
    public List<DriverInformation> list(DriverInformation driverInformation) throws Exception {
        return driverInformationMapper.select(driverInformation);
    }

    /**
     * @param driverInformation
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 查询是否为黑名单
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/28 9:36
     */
    @Override
    public boolean checkblack(String driverIdNo) throws Exception {
        DriverInformation driverInformation = new DriverInformation();
        driverInformation.setDriveridnumber(driverIdNo);
        driverInformation = driverInformationMapper.select(driverInformation).get(0);
        if ("1".equals(driverInformation.getDriverblacklist())) {
            return true;
        }
        return false;
    }


    /**
     * @param driverInformation
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新驾驶员信息
     * @Return void
     * @Date 2019/11/22 15：11
     */
    @Override
    public void update(DriverInformation driverInformation, TokenModel tokenModel) throws Exception {
        driverInformation.preUpdate(tokenModel);
        driverInformationMapper.updateByPrimaryKeySelective(driverInformation);
    }
}
