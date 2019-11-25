package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Devicetrainer;
import com.nt.service_BASF.DevicetrainerServices;
import com.nt.service_BASF.mapper.DevicetrainerMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF21203ServicesImpl
 * @Author: WXL
 * @Description: BASF培训项目模块实现类
 * @Date: 2019/11/25 14:16
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DevicetrainerServicesImpl implements DevicetrainerServices {

    private static Logger log = LoggerFactory.getLogger(DevicetrainerServicesImpl.class);

    @Autowired
    private DevicetrainerMapper devicetrainerMapper;


    /**
     * @param Devicetrainer
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取培培训人员数据列表
     * @Return java.util.List<Devicetrainer>
     * @Date 2019/11/25 14:16
     */
    @Override
    public List<Devicetrainer> list() throws Exception {
        Devicetrainer devicetrainer = new Devicetrainer();
        return devicetrainerMapper.select(devicetrainer);
    }


    /**
     * @param devicetrainer
     * @param tokenModel
     * @Method insert
     * @Author WXL
     * @Version 1.0
     * @Description 创建培训人员数据
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void insert(Devicetrainer devicetrainer, TokenModel tokenModel) throws Exception {
        devicetrainer.preInsert(tokenModel);
        devicetrainer.setDevicetrainerid(UUID.randomUUID().toString());
        devicetrainerMapper.insert(devicetrainer);
    }


    /**
     * @param devicetrainer
     * @Method Delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除培训人员数据
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void delete(Devicetrainer devicetrainer) throws Exception {
        devicetrainerMapper.updateByPrimaryKeySelective(devicetrainer);
    }


    /**
     * @param devicetrainerid
     * @Method one
     * @Author WXL
     * @Version 1.0
     * @Description 获取数据培训人员详情
     * @Return com.nt.dao_BASF.Devicetrainer
     * @Date 2019/11/25 14:16
     */
    @Override
    public Devicetrainer one(String devicetrainerid) throws Exception {
        return devicetrainerMapper.selectByPrimaryKey(devicetrainerid);
    }


    /**
     * @param devicetrainer
     * @param tokenModel
     * @Method update
     * @Author WXL
     * @Version 1.0
     * @Description 更新培训人员详情
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void update(Devicetrainer devicetrainer, TokenModel tokenModel) throws Exception {
        devicetrainer.preUpdate(tokenModel);
        devicetrainerMapper.updateByPrimaryKey(devicetrainer);
    }

}
