package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Flexiblework;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FlexibleworkService {
    //获取
    List<Flexiblework> getFlexiblework() throws Exception;
    //创建
    void insertFlexiblework(Flexiblework flexiblework, TokenModel tokenModel) throws  Exception;
}
