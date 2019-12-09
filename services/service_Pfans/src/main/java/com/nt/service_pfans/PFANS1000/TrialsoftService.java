package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Trialsoft;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface TrialsoftService {

    List<Trialsoft> getTrialsoft(Trialsoft trialsoft)throws Exception;

    public Trialsoft One(String trialsoft_id)throws  Exception;

    public void insert(Trialsoft trialsoft, TokenModel tokenModel)throws  Exception;

    public void updateTrialsoft(Trialsoft trialsoft, TokenModel tokenModel)throws  Exception;
}
