package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.FlexibleWork;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FlexibleWorkService {

    //查看
    List<FlexibleWork> getFlexibleWork(FlexibleWork flexiblework)throws Exception;

    public FlexibleWork One(String flexibleworkid)throws  Exception;

    //创建
    public void insert(FlexibleWork flexiblework,TokenModel tokenModel)throws  Exception;

    //修改
    public void updateFlexibleWork(FlexibleWork flexiblework,TokenModel tokenModel)throws  Exception;

}


