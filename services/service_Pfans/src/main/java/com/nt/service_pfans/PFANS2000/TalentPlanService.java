package com.nt.service_pfans.PFANS2000;


import com.nt.dao_Pfans.PFANS2000.TalentPlan;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface TalentPlanService {
    public void insert(TalentPlan talentPlan, TokenModel tokenModel)throws Exception;
    public List<TalentPlan> list(TalentPlan talentPlan) throws Exception;
    public void upd(TalentPlan talentPlan, TokenModel tokenModel)throws Exception;
    TalentPlan One(String talentplan_id) throws Exception;
}
