package com.nt.service_BASF;

import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.utils.dao.TokenModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF90905Services
 * @Author: GJ
 * @Description:人员清点接口
 * @Date: 2020/03/30 16:36
 * @Version: 1.0
 */
public interface PersonnelPermissionsServices {

    //获取人员清点列表
    List<PersonnelPermissions> list() throws Exception;

    //保存部门列表
    void insert(ArrayList<PersonnelPermissions> personnelPermissions, TokenModel tokenModel) throws Exception;
}
