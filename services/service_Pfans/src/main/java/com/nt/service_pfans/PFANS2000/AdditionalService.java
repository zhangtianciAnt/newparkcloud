package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Additional;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface AdditionalService {

    void deleteadditional(Additional additional, TokenModel tokenModel)throws Exception;

    List<String> importUseradditional(String Givingid,HttpServletRequest request, TokenModel tokenModel) throws Exception ;
}
