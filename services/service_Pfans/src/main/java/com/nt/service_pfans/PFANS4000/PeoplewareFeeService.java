package com.nt.service_pfans.PFANS4000;

import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface PeoplewareFeeService {

    List<PeoplewareFee> getPeopleWareList(PeoplewareFee peoplewarefee) throws Exception;

    List<String> importPeopleWare(HttpServletRequest request, TokenModel tokenModel) throws Exception ;
}
