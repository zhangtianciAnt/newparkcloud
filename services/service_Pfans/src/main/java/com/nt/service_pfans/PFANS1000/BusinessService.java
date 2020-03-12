package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BusinessService {

    List<Business> get(Business business) throws Exception;
    List<Business> getBuse() throws Exception;
    public BusinessVo selectById(String businessid) throws Exception;

    public void insertBusinessVo(BusinessVo businessvo, TokenModel tokenModel)throws Exception;

    public void updateBusinessVo(BusinessVo businessvo, TokenModel tokenModel)throws Exception;

}
