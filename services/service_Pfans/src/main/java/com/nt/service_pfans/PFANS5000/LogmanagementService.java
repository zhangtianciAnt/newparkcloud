package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.Logmanagement;
import com.nt.utils.dao.TokenModel;

import java.util.List;


/**
 *
 */
public interface LogmanagementService {

    void insert(Logmanagement logmanagement, TokenModel tokenModel)throws Exception;

    //获取列表
    List<Logmanagement> getDataList();
}
