package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DepartmentAccountService {

    //查看
    List<DepartmentAccount> selectBygroupid(String year, String groupid) throws Exception;
    //创建
    public void insert() throws Exception;
}
