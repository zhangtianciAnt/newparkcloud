package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentAccountVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface DepartmentAccountService {

    //查看
    List<DepartmentAccountVo> selectBygroupid(String year, String groupid) throws Exception;
    //创建
    public void insert() throws Exception;

    Object getTable1051infoReport(String year, String department) throws Exception;
}
