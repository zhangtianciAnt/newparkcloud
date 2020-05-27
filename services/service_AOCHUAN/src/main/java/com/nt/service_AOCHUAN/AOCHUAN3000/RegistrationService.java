package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Reg_Record;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Registration;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface RegistrationService {
    //获取列表
    List<Registration> getRegList();

    //根据主键查询
    Registration getReg(String id);

    //根据注册表id查询
    List<Reg_Record> getRecordList(String id);

    //新建
    Boolean insert(Object object, TokenModel tokenModel) throws Exception;

    //更新
    Boolean update(Object object, TokenModel tokenModel) throws Exception;

    //删除
    Boolean del(Registration registration, TokenModel tokenModel);

    //记录表删除
    Boolean recordDel(Object object, TokenModel tokenModel) throws Exception;

    //存在Check
    Boolean recordIsExist(String id);
}
