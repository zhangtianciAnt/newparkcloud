package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Firealarm;
import com.nt.service_BASF.FirealarmServices;
import com.nt.service_BASF.mapper.FirealarmMapper;
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
 * @ClassName:
 * @Author: Wxz
 * @Description: BASF接警单模块实现类
 * @Date: 2019/11/12 10:50
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FirealarmServicesImpl implements FirealarmServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private FirealarmMapper firealarmMapper;

    /**
     * @param firealarm
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取接警单列表
     * @Return java.util.List<Firealarm>
     * @Date 2019/11/12 10:54
     */
    @Override
    public List<Firealarm> list() throws Exception {
        Firealarm firealarm = new Firealarm();
        return firealarmMapper.select(firealarm);
    }

    /**
     * @param firealarm
     * @param tokenModel
     * @Method insert
     * @Author Wxz
     * @Version 1.0
     * @Description 创建接警单
     * @Return void
     * @Date 2019/11/12 10:55
     */
    @Override
    public void insert(Firealarm firealarm, TokenModel tokenModel) throws Exception {
        firealarm.preInsert(tokenModel);
        firealarm.setFirealarmid(UUID.randomUUID().toString());
        firealarmMapper.insert(firealarm);
    }

    /**
     * @param firealarm
     * @Method Delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除接警单
     * @Return void
     * @Date 2019/11/12 11：06
     */
    @Override
    public void delete(Firealarm firealarm) throws Exception {
        //逻辑删除（status -> "1"）
        firealarmMapper.updateByPrimaryKeySelective(firealarm);
    }

    /**
     * @param firealarmid
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 获取接警单详情
     * @Return com.nt.dao_BASF.Firealarm
     * @Date 2019/11/12 11：07
     */
    @Override
    public Firealarm one(String firealarmid) throws Exception {
        return firealarmMapper.selectByPrimaryKey(firealarmid);
    }

    /**
     * @param firealarm
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新接警单详情
     * @Return void
     * @Date 2019/11/12 11：07
     */
    @Override
    public void update(Firealarm firealarm, TokenModel tokenModel) throws Exception {
        firealarm.preUpdate(tokenModel);
        firealarmMapper.updateByPrimaryKey(firealarm);
    }
}
