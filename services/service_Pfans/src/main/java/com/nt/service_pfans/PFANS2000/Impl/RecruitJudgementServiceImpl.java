package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.RecruitJudgement;
import com.nt.service_pfans.PFANS2000.RecruitJudgementService;
import com.nt.service_pfans.PFANS2000.mapper.RecruitJudgementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecruitJudgementServiceImpl implements RecruitJudgementService {

    @Autowired
    private RecruitJudgementMapper recruitJudgementMapper;

    @Override
    public List<RecruitJudgement> get(TokenModel tokenModel) throws Exception {
        List<String> owners = tokenModel.getOwnerList();
        List<RecruitJudgement> recruitJudgement = recruitJudgementMapper.getRecruitJudgement(owners);
        return recruitJudgement;
    }

    @Override
    public List<RecruitJudgement> getOne(String id, TokenModel tokenModel) throws Exception {
        RecruitJudgement recruitJudgement = recruitJudgementMapper.selectByPrimaryKey(id);
        List<RecruitJudgement> recruitJudgements = new ArrayList<RecruitJudgement>();
        recruitJudgements.add(recruitJudgement);
        return recruitJudgements;
    }

    @Override
    public void insert(RecruitJudgement recruitJudgement, TokenModel tokenModel) throws Exception {
        recruitJudgement.preInsert(tokenModel);
        recruitJudgement.setRecruitjudgement_id(UUID.randomUUID().toString());
        recruitJudgementMapper.insert(recruitJudgement);

    }

    @Override
    public void update(RecruitJudgement recruitJudgement, TokenModel tokenModel) throws Exception {
        recruitJudgement.preUpdate(tokenModel);
        recruitJudgementMapper.updateByPrimaryKeySelective(recruitJudgement);
    }
}
