package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Confidential;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ConfidentialService {

    List<Confidential> getConfidential(Confidential confidential) throws Exception;

    public Confidential One(String confidentialid) throws Exception;

    public void update(Confidential confidential, TokenModel tokenModel) throws Exception;

    public void insert(Confidential confidential, TokenModel tokenModel)throws Exception;

}
