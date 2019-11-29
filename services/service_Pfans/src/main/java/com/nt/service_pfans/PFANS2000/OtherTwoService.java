package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OtherTwoService {





    void deleteteothertwo(OtherTwo othertwo, TokenModel tokenModel)throws Exception;

    List<String> importUserothertwo(String Givingid,HttpServletRequest request, TokenModel tokenModel) throws Exception ;

}
