package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.OtherFour;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OtherFourService {

    void deleteotherfour(OtherFour otherFour, TokenModel tokenModel)throws Exception;

    List<String> importUserotherfour(String Givingid,HttpServletRequest request, TokenModel tokenModel) throws Exception ;

}
