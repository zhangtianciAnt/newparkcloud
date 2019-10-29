package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Leaveoffice;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface LeaveofficeService {

    //列表查询
    List<Leaveoffice> get(Leaveoffice leaveoffice) throws Exception;

    //新建
    void create(Leaveoffice leaveoffice, TokenModel tokenModel) throws Exception;

    //编辑
    void update(Leaveoffice leaveoffice, TokenModel tokenModel) throws Exception;

    //按id查询
    Leaveoffice one(String leaveoffice_id) throws Exception;

}
