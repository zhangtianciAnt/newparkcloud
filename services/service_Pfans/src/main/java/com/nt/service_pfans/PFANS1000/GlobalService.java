package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Global;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface GlobalService {

    List<Global> getglobal(Global global) throws Exception;

    public Global getglobalApplyOne(String global_id) throws Exception;

    public void updateglobalApply(Global global, TokenModel tokenModel) throws Exception;

    public void createglobalApply(Global global, TokenModel tokenModel)throws Exception;

}
