package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.FlexibleWork;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FlexibleWorkService {

    List<FlexibleWork> getFlexibleWork(FlexibleWork flexiblework)throws Exception;

    public FlexibleWork One(String flexibleworkid)throws  Exception;

    public void insert(FlexibleWork flexiblework,TokenModel tokenModel)throws  Exception;

    public void updateFlexibleWork(FlexibleWork flexiblework,TokenModel tokenModel)throws  Exception;

}


