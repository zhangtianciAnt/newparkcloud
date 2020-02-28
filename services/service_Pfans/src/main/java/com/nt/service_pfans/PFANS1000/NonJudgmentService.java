package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface NonJudgmentService {
    List<NonJudgment> get(NonJudgment nonJudgment) throws Exception;
    NonJudgment one(String nonjumend_id) throws Exception;
    void  update(NonJudgment nonJudgment,TokenModel tokenModel) throws  Exception;

}
