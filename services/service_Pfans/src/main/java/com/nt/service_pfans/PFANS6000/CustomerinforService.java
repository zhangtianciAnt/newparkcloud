package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CustomerinforService {

    List<Customerinfor> getcustomerinfor(Customerinfor customerinfor) throws Exception;

    public Customerinfor getcustomerinforApplyOne(String customerinfor_id) throws Exception;

    public void updatecustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception;

    public void createcustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception;

}
