package com.nt.service_pfans.PFANS4000.Impl;

import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.service_pfans.PFANS4000.SealService;
import com.nt.service_pfans.PFANS4000.mapper.SealMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class SealServiceImpl implements SealService {
    @Autowired
    private SealMapper sealMapper;
    @Override
    public List<Seal> list(Seal seal) throws Exception {
        return sealMapper.select(seal);
    }
    @Override
    public void insert(Seal seal, TokenModel tokenModel) throws Exception {
        seal.preInsert(tokenModel);
        seal.setSealid(UUID.randomUUID().toString());
        sealMapper.insert(seal);
    }
    @Override
    public void upd(Seal seal, TokenModel tokenModel) throws Exception {
        seal.preUpdate(tokenModel);
        sealMapper.updateByPrimaryKey(seal);
    }
    @Override
    public Seal One(String sealid) throws Exception {
        return sealMapper.selectByPrimaryKey(sealid);
    }
}
