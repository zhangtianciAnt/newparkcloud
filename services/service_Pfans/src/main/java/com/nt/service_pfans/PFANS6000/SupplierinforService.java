package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SupplierinforService {

    List<Supplierinfor> getsupplierinfor(Supplierinfor supplierinfor) throws Exception;

    public Supplierinfor getsupplierinforApplyOne(String supplierinfor_id) throws Exception;

    public void updatesupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception;

    public void createsupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception;

    public List<Supplierinfor> getSupplierNameList(Supplierinfor supplierinfor, HttpServletRequest request) throws Exception;

    List<String> supimport(HttpServletRequest request, TokenModel tokenModel) throws Exception;
}
