package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.utils.dao.TokenModel;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 *
 */
public interface LogManagementService {

    void insert(LogManagement logmanagement, TokenModel tokenModel)throws Exception;

    public List<LogManagement> getDataList(LogManagement logmanagemenr,HttpServletRequest request) throws Exception;


    void update(LogManagement logmanagement, TokenModel tokenModel)throws Exception;

    LogManagement One(String Logmanagement_id) throws Exception;
}
