package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SoftwaretransferService {

    //新建
    void insert(SoftwaretransferVo softwaretransferVo, TokenModel tokenModel) throws Exception;

    //编辑
    void updateSoftwaretransfer(SoftwaretransferVo softwaretransferVo, TokenModel tokenModel) throws Exception;

    //按id查询
    SoftwaretransferVo selectById(String softwaretransferid) throws Exception;

    List<Softwaretransfer> getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception;
}
