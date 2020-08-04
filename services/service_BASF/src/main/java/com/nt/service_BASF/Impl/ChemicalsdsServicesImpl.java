package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Chemicalsds;
import com.nt.dao_BASF.Emergencyplan;
import com.nt.service_BASF.ChemicalsdsServices;
import com.nt.service_BASF.EmergencyplanServices;
import com.nt.service_BASF.mapper.ChemicalsdsMapper;
import com.nt.service_BASF.mapper.EmergencyplanMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:ChemicalsdsServicesImpl
 * @Author: Y
 * @Description: BASF化学品SDS模块实现类
 * @Date: 2019/11/18 17:15
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChemicalsdsServicesImpl implements ChemicalsdsServices {

    private static Logger log = LoggerFactory.getLogger(ChemicalsdsServicesImpl.class);

    @Autowired
    private ChemicalsdsMapper chemicalsdsMapper;

    /**
     * @Method list
     * @Author Y
     * @Version 1.0
     * @Description 获取化学品SDS数据列表
     * @Return java.util.List<Chemicalsds>
     * @Date 2019/11/18 17:17
     */

    @Override
    public List<Chemicalsds> list() throws Exception {
        Chemicalsds chemicalsds = new Chemicalsds();
        return chemicalsdsMapper.select(chemicalsds);
    }

    /**
     * @param tokenModel
     * @Method insert
     * @Author Y
     * @Version 1.0
     * @Description 创建化学品SDS
     * @Return void
     * @Date 2019/11/18 17:18
     */

    @Override
    public void insert(Chemicalsds chemicalsds, TokenModel tokenModel) throws Exception {
        chemicalsds.preInsert(tokenModel);
        chemicalsds.setChemicalsdsid(UUID.randomUUID().toString());
        chemicalsds.setCreationtime(new Date());
        chemicalsdsMapper.insert(chemicalsds);
    }

    /**
     * @Method Delete
     * @Author Y
     * @Version 1.0
     * @Description 删除化学品SDS
     * @Return void
     * @Date 2019/11/18 17：22
     */

    @Override
    public void delete(Chemicalsds chemicalsds) throws Exception {
        //逻辑删除（status -> "1"）
        chemicalsdsMapper.updateByPrimaryKeySelective(chemicalsds);

    }

    /**
     * @param chemicalsdsid
     * @Method one
     * @Author Y
     * @Version 1.0
     * @Description 获取化学品SDS详情
     * @Return com.nt.dao_BASF.Chemicalsds
     * @Date 2019/11/18 17：24
     */

    @Override
    public Chemicalsds one(String chemicalsdsid) throws Exception {
        return chemicalsdsMapper.selectByPrimaryKey(chemicalsdsid);
    }


    /**
     * @param tokenModel
     * @Method update
     * @Author Y
     * @Version 1.0
     * @Description 更新化学品SDS详情
     * @Return void
     * @Date 2019/11/18 17：24
     */
    @Override
    public void update(Chemicalsds chemicalsds, TokenModel tokenModel) throws Exception {
        chemicalsds.preUpdate(tokenModel);
        chemicalsdsMapper.updateByPrimaryKey(chemicalsds);
    }
}

