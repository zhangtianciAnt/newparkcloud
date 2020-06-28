package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Staffexitproce;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StaffexitprocedureService {
    //列表查询
    List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception;

    public List<Staffexitproce> getList(Staffexitproce staffexitproce, TokenModel tokenModel) throws Exception;

    public List<Staffexitprocedure> getList2(Staffexitprocedure staffexitprocedure, TokenModel tokenModel) throws Exception;
    //新建
    void insert(StaffexitprocedureVo StaffexitprocedureVo, TokenModel tokenModel) throws Exception;

    //编辑
    void update(StaffexitprocedureVo StaffexitprocedureVo, TokenModel tokenModel) throws Exception;

    //按id查询
    StaffexitprocedureVo selectById(String staffexitprocedureid) throws Exception;
    //add-ws-6/16-禅道106
    void delete (Staffexitprocedure staffexitprocedure) throws Exception;
    //add-ws-6/16-禅道106


    //新建
    void insert2(StaffexitprocedureVo StaffexitprocedureVo, TokenModel tokenModel) throws Exception;

    //编辑
    void update2(StaffexitprocedureVo StaffexitprocedureVo, TokenModel tokenModel) throws Exception;

    List<Staffexitproce> get2(Staffexitproce staffexitproce) throws Exception;

    StaffexitprocedureVo selectById2(String staffexitproceid) throws Exception;

}
