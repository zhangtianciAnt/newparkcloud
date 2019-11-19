package com.nt.service_BASF.Impl;


import com.nt.dao_BASF.Emergencytemplate;
import com.nt.service_BASF.EmergencytemplateServices;
import com.nt.service_BASF.mapper.EmergencytemplateMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF10301ServicesImpl
 * @Author: WXL
 * @Description: BASF数据监控模块实现类
 * @Date: 2019/11/18 17:00
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmergencytemplateServicesImpl implements EmergencytemplateServices {

    private static Logger log = LoggerFactory.getLogger(EmergencytemplateServicesImpl.class);

    @Autowired
    private EmergencytemplateMapper emergencytemplateMapper;


    /**
     * @param Emergencytemplate
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取模板数据列表
     * @Return java.util.List<Environment>
     * @Date 2019/11/18 17:17
     */

    @Override
    public List<Emergencytemplate> list() throws Exception {
        Emergencytemplate emergencytemplate = new Emergencytemplate();
        return emergencytemplateMapper.select(emergencytemplate);
    }

    /**
     * @param emergencytemplate
     * @param tokenModel
     * @Method insert
     * @Author Wxz
     * @Version 1.0
     * @Description 创建模板数据
     * @Return void
     * @Date 2019/11/18 17:18
     */

    @Override
    public void insert(Emergencytemplate emergencytemplate, TokenModel tokenModel) throws Exception {
        emergencytemplate.preInsert(tokenModel);
        emergencytemplate.setTemplateid(UUID.randomUUID().toString());
        Date date = new Date();
        emergencytemplate.setTemplatetim(date);
        emergencytemplateMapper.insert(emergencytemplate);

    }

    /**
     * @param emergencytemplate
     * @Method Delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除模板数据
     * @Return void
     * @Date 2019/11/18 17：22
     */

    @Override
    public void delete(Emergencytemplate emergencytemplate) throws Exception {
        //逻辑删除（status -> "1"）
        emergencytemplateMapper.updateByPrimaryKeySelective(emergencytemplate);

    }

    /**
     * @param templateid
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 获取数据模板详情
     * @Return com.nt.dao_BASF.Emergencytemplate
     * @Date 2019/11/18 17：24
     */

    @Override
    public Emergencytemplate one(String templateid) throws Exception {
        return emergencytemplateMapper.selectByPrimaryKey(templateid);
    }


    /**
     * @param emergencytemplate
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新模板详情
     * @Return void
     * @Date 2019/11/18 17：24
     */

    @Override
    public void update(Emergencytemplate emergencytemplate, TokenModel tokenModel) throws Exception {
        emergencytemplate.preUpdate(tokenModel);
        emergencytemplateMapper.updateByPrimaryKey(emergencytemplate);
    }
}
