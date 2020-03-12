package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Appreciation;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppreciationService {




    void deleteteappreciation(Appreciation appreciation, TokenModel tokenModel)throws Exception;

    List<String> importUserappreciation(String Givingid,HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    List<Appreciation> get(Appreciation appreciation) throws Exception;

    void update (Appreciation appreciation,TokenModel tokenModel) throws Exception;

    void insert(Appreciation appreciation,TokenModel tokenModel) throws Exception;
}
