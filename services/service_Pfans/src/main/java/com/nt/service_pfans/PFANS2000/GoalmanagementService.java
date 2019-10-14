package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Goalmanagement;
import com.nt.utils.dao.TokenModel;

public interface GoalmanagementService {
    public void insert(Goalmanagement goalmanagement, TokenModel tokenModel)throws Exception;
}
