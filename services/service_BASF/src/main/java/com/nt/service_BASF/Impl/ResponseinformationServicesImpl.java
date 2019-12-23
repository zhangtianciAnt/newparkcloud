package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Responseinformation;
import com.nt.service_BASF.ResponseinformationServices;
import com.nt.service_BASF.mapper.ResponseinformationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: ResponseinformationServicesImpl
 * @Author: Newtouch
 * @Description: 应急预案响应信息实现类
 * @Date: 2019/12/19 16:30
 * @Version: 1.0
 */
@Service
public class ResponseinformationServicesImpl implements ResponseinformationServices {

    @Autowired
    private ResponseinformationMapper responseinformationMapper;

    @Override
    public List<Responseinformation> list() throws Exception {
        Responseinformation responseinformation = new Responseinformation();
        return responseinformationMapper.select(responseinformation);
    }

    @Override
    public Responseinformation getone(String responseinformationid) throws Exception {
        return responseinformationMapper.selectByPrimaryKey(responseinformationid);
    }

    @Override
    public void insert(TokenModel tokenModel, Responseinformation responseinformation) throws Exception {
        responseinformation.preInsert(tokenModel);
        responseinformation.setResponseinformationid(UUID.randomUUID().toString());
        responseinformationMapper.insert(responseinformation);
    }

    @Override
    public void update(TokenModel tokenModel, Responseinformation responseinformation) throws Exception {
        responseinformation.preUpdate(tokenModel);
        responseinformationMapper.updateByPrimaryKey(responseinformation);
    }

    @Override
    public void delete(TokenModel tokenModel, Responseinformation responseinformation) throws Exception {
        responseinformation.preUpdate(tokenModel);
        responseinformationMapper.updateByPrimaryKeySelective(responseinformation);
    }
}
