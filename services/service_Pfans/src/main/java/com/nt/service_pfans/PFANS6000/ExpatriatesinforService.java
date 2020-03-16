package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ExpatriatesinforService {

    List<Expatriatesinfor> getexpatriatesinfor(Expatriatesinfor expatriatesinfor) throws Exception;

    public Expatriatesinfor getexpatriatesinforApplyOne(String expatriatesinfor_id) throws Exception;

    public void updateexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception;


    public void createexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception;


    List<String> expimport(HttpServletRequest request, TokenModel tokenModel) throws Exception;
}
