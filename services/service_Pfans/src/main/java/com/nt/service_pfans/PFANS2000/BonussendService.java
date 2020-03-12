package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BonussendService {

    List<Bonussend> getListType(Bonussend bonussend) throws Exception;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    List<Bonussend> List(Bonussend bonussend,TokenModel tokenModel) throws Exception;

    List<Bonussend> get(Bonussend bonussend) throws Exception;
}
