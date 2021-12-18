package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.EntrustSupport;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface EntrustSupportService {

    List<EntrustSupport> getList(EntrustSupport entrustSupport) throws Exception;

    void updList(List<EntrustSupport> entrustSupport, TokenModel tokenModel) throws Exception;
}
