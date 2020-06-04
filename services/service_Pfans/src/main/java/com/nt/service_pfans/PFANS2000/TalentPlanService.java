package com.nt.service_pfans.PFANS2000;


import com.nt.dao_Pfans.PFANS2000.TalentPlan;
import com.nt.dao_Pfans.PFANS2000.Vo.TalentPlanVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface TalentPlanService {
    public void insert(TalentPlan talentPlan, TokenModel tokenModel)throws Exception;
    public List<TalentPlan> list(TalentPlan talentPlan,TokenModel tokenModel) throws Exception;
    public void upd(TalentPlan talentPlan, TokenModel tokenModel)throws Exception;
    TalentPlan One(String talentplan_id) throws Exception;
    public void insertByOrg(TalentPlanVo TalentPlanVo, TokenModel tokenModel)throws Exception;
    //add-ws-6/4-禅道031-人才育成修改各人员查看数据范围修改
    public List<TalentPlan> getDataList(TalentPlan talentPlan) throws Exception;
    //add-ws-6/4-禅道031-人才育成修改各人员查看数据范围修改
}
