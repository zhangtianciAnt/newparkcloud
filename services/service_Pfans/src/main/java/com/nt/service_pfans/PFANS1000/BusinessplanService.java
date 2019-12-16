package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface BusinessplanService {

    List<Businessplan> get(Businessplan businessplan) throws Exception;

    public BusinessplanVo selectById(String businessplanid) throws Exception;

    public void insertBusinessplanVo(BusinessplanVo businessplanvo, TokenModel tokenModel) throws Exception;

    public void updateBusinessplanVo(BusinessplanVo businessplanvo, TokenModel tokenModel) throws Exception;

}
