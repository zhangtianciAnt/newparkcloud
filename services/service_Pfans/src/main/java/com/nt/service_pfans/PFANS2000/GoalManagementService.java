package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.GoalManagement;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GoalManagementService {
    public void insert(GoalManagement goalmanagement, TokenModel tokenModel)throws Exception;
    public List<GoalManagement> list(GoalManagement goalmanagement) throws Exception;
    public void upd(GoalManagement management, TokenModel tokenModel)throws Exception;
    GoalManagement One(String goalmanagement_id) throws Exception;

    List<GoalManagement> yearsCheck(GoalManagement goalmanagement) throws Exception;
}
