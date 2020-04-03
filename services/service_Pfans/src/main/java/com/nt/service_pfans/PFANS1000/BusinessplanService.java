package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.PersonPlanTable;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface BusinessplanService {

    List<Businessplan> get(Businessplan businessplan) throws Exception;

    public Businessplan selectById(String businessplanid) throws Exception;

    public void insertBusinessplan(Businessplan businessplan, TokenModel tokenModel) throws Exception;

    public void updateBusinessplanVo(Businessplan businessplan, TokenModel tokenModel) throws Exception;

    String[] getPersonPlan(String year, String groupid) throws Exception;
}
