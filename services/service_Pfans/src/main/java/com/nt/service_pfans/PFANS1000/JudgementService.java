package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.Vo.JudgementVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface JudgementService {
    public List<Judgement> selectJudgement() throws Exception;

    List<Judgement> getJudgement(Judgement judgement) throws Exception;

    public JudgementVo One(String judgementid) throws Exception;

    public void updateJudgement(JudgementVo judgementVo, TokenModel tokenModel) throws Exception;

    public void updateJudgementDetail(JudgementVo judgementVo, TokenModel tokenModel) throws Exception;

    public void insert(JudgementVo judgementVo, TokenModel tokenModel)throws Exception;

    public void createJudgementDetail(JudgementVo judgementVo, TokenModel tokenModel)throws Exception;

    public List<Judgement> getJudgementList(Judgement judgement, HttpServletRequest request) throws Exception;

    //region scc add 10/28 其他业务决裁逻辑删除 from
    void juddelete(Judgement judgement, TokenModel tokenModel) throws Exception;
    //endregion scc add 10/28 其他业务决裁逻辑删除 to

    //region   add  ml  220112  检索  from
    List<Judgement> getJudgementSearch(Judgement judgement) throws Exception;
    //endregion   add  ml  220112  检索  to

}
