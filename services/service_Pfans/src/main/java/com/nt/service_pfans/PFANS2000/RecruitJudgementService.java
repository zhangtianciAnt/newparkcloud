package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.RecruitJudgement;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface RecruitJudgementService {

   List<RecruitJudgement> get(TokenModel tokenModel) throws  Exception;

    List<RecruitJudgement> getOne(String id, TokenModel tokenModel) throws  Exception;

   void insert(RecruitJudgement recruitJudgement, TokenModel tokenModel) throws  Exception;

   void update(RecruitJudgement recruitJudgement, TokenModel tokenModel) throws  Exception;

}
