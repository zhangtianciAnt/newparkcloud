package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.dao_Pfans.PFANS2000.TalentPlan;
import com.nt.service_pfans.PFANS2000.TalentPlanService;
import com.nt.service_pfans.PFANS2000.mapper.TalentPlanMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class TalentPlanServiceImpl implements TalentPlanService {

    @Autowired
    private TalentPlanMapper talentPlanMapper;

    @Override
    public List<TalentPlan> list(TalentPlan talentPlan ) throws Exception {
        return talentPlanMapper.select(talentPlan);
    }
    @Override
    public TalentPlan One(String talentplan_id) throws Exception {

        TalentPlan log   =talentPlanMapper.selectByPrimaryKey(talentplan_id);
        return log;
    }

    @Override
    public void upd (TalentPlan talentPlan, TokenModel tokenModel) throws Exception {
        talentPlan.preUpdate(tokenModel);
        talentPlanMapper.updateByPrimaryKeySelective(talentPlan);
    }
    @Override
    public void insert(TalentPlan talentPlan, TokenModel tokenModel) throws Exception {
        talentPlan.preInsert(tokenModel);
        talentPlan.setTalentplan_id(UUID.randomUUID().toString());
        talentPlanMapper.insert(talentPlan);
    }
}
