package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.Logmanagement;
import com.nt.utils.dao.TokenModel;


/**
 *
 */
public interface LogmanagementService {

    public void insert(Logmanagement logmanagement, TokenModel tokenModel)throws Exception;
}
