package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CasgiftApplyService {

    List<CasgiftApply> getCasgiftApply(CasgiftApply casgiftapply) throws Exception;

    public CasgiftApply One(String casgiftapplyid) throws Exception;

    public void updateCasgiftApply(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception;

    public void insert(CasgiftApply casgiftapply, TokenModel tokenModel)throws Exception;

    public List<CasgiftApply> getCasgiftApplyList(CasgiftApply casgiftapply, HttpServletRequest request) throws Exception;

}
