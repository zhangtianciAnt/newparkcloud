package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.Cooperinterview;
import com.nt.service_pfans.PFANS6000.CooperinterviewService;
import com.nt.service_pfans.PFANS6000.mapper.CooperinterviewMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CooperinterviewServiceImpl implements CooperinterviewService {

    @Autowired
    private CooperinterviewMapper cooperinterviewMapper;


    @Override
    public List<Cooperinterview> getcooperinterview(Cooperinterview cooperinterview) throws Exception {
        return cooperinterviewMapper.select(cooperinterview);
    }

    @Override
    public Cooperinterview getcooperinterviewApplyOne(String cooperinterview_id) throws Exception {
        return cooperinterviewMapper.selectByPrimaryKey(cooperinterview_id);
    }

    @Override
    public void updatecooperinterviewApply(Cooperinterview cooperinterview, TokenModel tokenModel) throws Exception {
        cooperinterviewMapper.updateByPrimaryKeySelective(cooperinterview);
    }

    @Override
    public void createcooperinterviewApply(Cooperinterview cooperinterview, TokenModel tokenModel) throws Exception {
        cooperinterview.preInsert(tokenModel);
        cooperinterview.setCooperinterview_id(UUID.randomUUID().toString());
        cooperinterview.setCooperuserid(cooperinterview.getCooperinterview_id());
        cooperinterviewMapper.insert(cooperinterview);
    }

}
