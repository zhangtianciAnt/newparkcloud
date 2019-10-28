package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CasgiftApplyService {

    //查看
    List<CasgiftApply> getCasgiftApply(CasgiftApply casgiftapply) throws Exception;

    public CasgiftApply One(String casgiftapplyid) throws Exception;

    //修改
    public void updateCasgiftApply(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception;

    //创建
    public void insert(CasgiftApply casgiftapply, TokenModel tokenModel)throws Exception;

    //计算金额
    public List<CasgiftApply> getCasgiftApplyList(CasgiftApply casgiftapply, HttpServletRequest request) throws Exception;

}
