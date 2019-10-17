package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.FlexibleWork;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FlexibleWorkService {

    //查看
    List<FlexibleWork> getFlexibleWork(FlexibleWork flexibleWork)throws Exception;

    //创建
    void insertFlexibleWork(FlexibleWork flexibleWork,TokenModel tokenModel)throws  Exception;

    //修改
    void updateFlexibleWork(FlexibleWork flexibleWork,TokenModel tokenModel)throws  Exception;

}


