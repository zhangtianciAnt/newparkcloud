package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.utils.dao.TokenModel;

import java.util.List;


/**
 *
 */
public interface LogManagementService {

    void insert(LogManagement logmanagement, TokenModel tokenModel)throws Exception;

    //获取列表
    List<LogManagement> getDataList();
}
