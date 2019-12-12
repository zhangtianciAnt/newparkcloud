package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Trialsoft;
import com.nt.dao_Pfans.PFANS1000.Vo.TrialsoftVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface TrialsoftService {

    List<Trialsoft> getTrialsoft(Trialsoft trialsoft)throws Exception;

    public TrialsoftVo selectById(String trialsoft_id)throws  Exception;

    public void insert(TrialsoftVo trialsoftVo, TokenModel tokenModel)throws  Exception;

    public void update(TrialsoftVo trialsoftVo, TokenModel tokenModel)throws  Exception;
}
