package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS2000.Vo.StaffexitprocedureVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StaffexitprocedureService {
    //列表查询
    List<Staffexitprocedure> get(Staffexitprocedure staffexitprocedure) throws Exception;

    //新建
    void insert(StaffexitprocedureVo StaffexitprocedureVo, TokenModel tokenModel) throws Exception;

    //编辑
    void update(StaffexitprocedureVo StaffexitprocedureVo, TokenModel tokenModel) throws Exception;

    //按id查询
    StaffexitprocedureVo selectById(String staffexitprocedureid) throws Exception;

}
