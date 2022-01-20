package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface NonJudgmentService {
    List<NonJudgment> get(NonJudgment nonJudgment) throws Exception;
    //   add  ml  211130  分页  from
    List<NonJudgment> getPage(NonJudgment nonJudgment) throws Exception;
    //   add  ml  211130  分页  to
    NonJudgment one(String nonjumend_id) throws Exception;
    void  update(NonJudgment nonJudgment,TokenModel tokenModel) throws  Exception;
}
