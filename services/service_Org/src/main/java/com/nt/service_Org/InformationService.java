package com.nt.service_Org;


import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Information;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

public interface InformationService {

    //保存
    void save(Information information, TokenModel tokenModel) throws Exception;

    //获取
    List<Information> get(Information information) throws Exception;
    //导出excel
    void importexcel(String id, HttpServletRequest request) throws Exception;

    //获取
    List<CustomerInfo> getcustomerinfo() throws Exception;
}
