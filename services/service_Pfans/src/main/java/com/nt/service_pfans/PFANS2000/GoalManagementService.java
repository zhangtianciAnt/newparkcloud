package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface GoalManagementService {
    public void insert(GoalManagement goalmanagement, TokenModel tokenModel)throws Exception;
    public List<GoalManagement> list(GoalManagement goalmanagement) throws Exception;
}
