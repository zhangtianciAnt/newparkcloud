package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface JudgementService {

    List<Judgement> getJudgement(Judgement judgement) throws Exception;

    public Judgement One(String judgementid) throws Exception;

    public void updateJudgement(Judgement judgement, TokenModel tokenModel) throws Exception;

    public void insert(Judgement judgement, TokenModel tokenModel)throws Exception;

    public List<Judgement> getJudgementList(Judgement judgement, HttpServletRequest request) throws Exception;

}
