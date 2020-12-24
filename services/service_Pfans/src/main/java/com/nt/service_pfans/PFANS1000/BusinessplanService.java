package com.nt.service_pfans.PFANS1000;


import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.PersonPlanTable;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessGroupA1Vo;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessGroupA2Vo;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.dao_Pfans.PFANS1000.Vo.OrgTreeVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface BusinessplanService {

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    List<Businessplan> get(Businessplan businessplan) throws Exception;

    List<BusinessGroupA1Vo> getgroupA1(String year,String groupid) throws Exception;

    List<OrgTreeVo> getgroupcompanyen(String year) throws Exception;

    List<BusinessGroupA2Vo> getgroup(String year,String type) throws Exception;

    public Businessplan selectById(String businessplanid) throws Exception;

    public void insertBusinessplan(Businessplan businessplan, TokenModel tokenModel) throws Exception;

    public void updateBusinessplanVo(Businessplan businessplan, TokenModel tokenModel) throws Exception;

    String[] getPersonPlan(String year, String groupid) throws Exception;
}
