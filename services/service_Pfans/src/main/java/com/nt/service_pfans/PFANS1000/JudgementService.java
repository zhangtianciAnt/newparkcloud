package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface JudgementService {

    //查看
    List<Judgement> getJudgement(Judgement judgement) throws Exception;

    public Judgement One(String judgementid) throws Exception;

    //修改
    public void updateJudgement(Judgement judgement, TokenModel tokenModel) throws Exception;

    //创建
    public void insert(Judgement judgement, TokenModel tokenModel)throws Exception;

    //计算金额
    public List<Judgement> getJudgementList(Judgement judgement, HttpServletRequest request) throws Exception;

}
