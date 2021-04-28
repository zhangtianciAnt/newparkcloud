package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PersonalCostService {


    public List<PersonalCost> getPersonalCost(String groupid,String yearsantid) throws Exception;

    public List<String> getGroupId(String yearsantid) throws Exception;

    public List<Dictionary> getChangeRanks() throws Exception;

    public List<PersonalCostBmSum> gettableBm(String yearsantid) throws Exception;

    public List<PersonalCostGsSum> gettableGs(String yearsantid) throws Exception;

    public List<PersonalCostRb> gettableRb(String yearsantid) throws Exception;





    List<PersonalCostYears> getPerCostYarList(PersonalCostYears personalCostYears) throws Exception;

    public PersonalCost insertPenalcost(String year, TokenModel tokenModel) throws Exception;


    public void upPersonalCost(List<PersonalCost> personalCostList, TokenModel tokenModel) throws Exception;

    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-start
    List<PersonalCost> getFuzzyQuery (String yearsantid,String username,String allotmentAnt,String group_id,String rnAnt) throws Exception;
    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-end
}
