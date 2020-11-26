package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS2000.PersonalCostYears;
import com.nt.dao_Pfans.PFANS2000.Recruit;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PersonalCostService {

    List<PersonalCostYears> getPerCostYarList(PersonalCostYears personalCostYears) throws Exception;

    public PersonalCost insertPenalcost(String year, TokenModel tokenModel) throws Exception;

    public List<PersonalCost> getPersonalCost(String groupid,String yearsantid) throws Exception;

    public void upPersonalCost(List<PersonalCost> personalCostList, TokenModel tokenModel) throws Exception;

}
