package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.service_pfans.PFANS2000.AbNormalService;
import com.nt.service_pfans.PFANS2000.mapper.AbNormalMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AbNormalServiceImpl implements AbNormalService {

    @Autowired
    private AbNormalMapper abNormalMapper;

    @Override
    public List<AbNormal> list(AbNormal abNormal) throws Exception {
        return abNormalMapper.select(abNormal);
    }

    @Override
    public void insert(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preInsert(tokenModel);
        abNormal.setAbnormalid(UUID.randomUUID().toString());
        abNormalMapper.insert(abNormal);
    }

    @Override
    public void upd(AbNormal abNormal, TokenModel tokenModel) throws Exception {
        abNormal.preUpdate(tokenModel);
        abNormalMapper.updateByPrimaryKey(abNormal);
    }

    @Override
    public AbNormal One(String abnormalid) throws Exception {
        return abNormalMapper.selectByPrimaryKey(abnormalid);
    }
}
