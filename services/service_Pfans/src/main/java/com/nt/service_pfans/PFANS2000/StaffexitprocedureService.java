package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface StaffexitprocedureService {
    //列表查询
    List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception;

    //新建
    void create(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception;
    //编辑

    void update(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception;

    //按id查询
    Staffexitprocedure one(String staffexitprocedure_id) throws Exception;

}
