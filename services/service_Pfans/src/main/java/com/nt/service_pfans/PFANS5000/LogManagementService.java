package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementStatusVo;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 *
 */
public interface LogManagementService {

    void insert(LogManagement logmanagement, TokenModel tokenModel)throws Exception;

    public List<LogManagement> getDataList(LogManagement logmanagemenr) throws Exception;

    public List<LogManagement> gettlist() throws Exception;

    public List<LogmanagementStatusVo> getTimestart(String project_id) throws Exception;

    void updateTimestart(LogmanagementStatusVo LogmanagementStatusVo)throws Exception;

    void update(LogManagement logmanagement, TokenModel tokenModel)throws Exception;

    LogManagement One(String Logmanagement_id) throws Exception;
//
//    //项目工数动态生成表格
//    List<LogManagement> getWeek(LogManagement logManagement) throws Exception;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    String downloadUserModel() throws LogicalException;
}
