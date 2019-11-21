package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.OtherFive;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OtherFiveService {


    List<OtherFive> listFive(OtherFive otherfive) throws Exception;


    void deleteFive(OtherFive otherfive, TokenModel tokenModel)throws Exception;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

}
