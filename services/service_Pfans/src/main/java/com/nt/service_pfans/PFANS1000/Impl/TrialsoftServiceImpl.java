package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Trialsoft;
import com.nt.service_pfans.PFANS1000.TrialsoftService;
import com.nt.service_pfans.PFANS1000.mapper.TrialsoftMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class TrialsoftServiceImpl implements TrialsoftService {

    @Autowired
    private TrialsoftMapper trialsoftMapper;

    @Override
    public List<Trialsoft> getTrialsoft(Trialsoft trialsoft) {
        return trialsoftMapper.select(trialsoft);
    }

    @Override
    public Trialsoft One(String trialsoft_id) throws Exception {
        return trialsoftMapper.selectByPrimaryKey(trialsoft_id);
    }

    @Override
    public void updateTrialsoft(Trialsoft trialsoft, TokenModel tokenModel) throws Exception {
        trialsoft.preUpdate(tokenModel);
        trialsoftMapper.updateByPrimaryKey(trialsoft);
    }

    @Override
    public void insert(Trialsoft trialsoft, TokenModel tokenModel) throws Exception {
        trialsoft.preInsert(tokenModel);
        trialsoft.setTrialsoft_id(UUID.randomUUID().toString());
        trialsoftMapper.insert(trialsoft);
    }

}
