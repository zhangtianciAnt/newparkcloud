package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Environment;
import com.nt.dao_BASF.Pimspoint;
import com.nt.service_BASF.EnvironmentServices;
import com.nt.service_BASF.mapper.EnvironmentMapper;
import com.nt.service_BASF.mapper.PimsPointMapper;
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
 * @ClassName:BASF10301ServicesImpl
 * @Author: WXL
 * @Description: BASF数据监控模块实现类
 * @Date: 2019/11/14 15:50
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EnvironmentServicesImpl implements EnvironmentServices {

    private static Logger log = LoggerFactory.getLogger(EnvironmentServicesImpl.class);

    @Autowired
    private PimsPointMapper pimsPointMapper;

    /**
     * @Method list
     * @Author MYT
     * @Version 1.0
     * @Description 获取PIMS点位信息列表
     * @Return java.util.List<Environment>
     * @Date 2019/11/14 15:54
     */
    @Override
    public List<Pimspoint> list() throws Exception {
        Pimspoint pimspoint = new Pimspoint();
        return pimsPointMapper.select(pimspoint);
    }

    /**
     * @param pimspointId
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 获取PIMS点位信息详情
     * @Return com.nt.dao_BASF.Environment
     * @Date 2019/11/12 11：07
     */
    @Override
    public Pimspoint one(String pimspointid) throws Exception {
        return pimsPointMapper.selectByPrimaryKey(pimspointid);
    }


    /**
     * @param pimspoint
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新PIMS点位信息
     * @Return void
     * @Date 2019/11/12 11：07
     */
    @Override
    public void update(Pimspoint pimspoint, TokenModel tokenModel) throws Exception {
        pimspoint.preUpdate(tokenModel);
        pimsPointMapper.updateByPrimaryKey(pimspoint);
    }

}
