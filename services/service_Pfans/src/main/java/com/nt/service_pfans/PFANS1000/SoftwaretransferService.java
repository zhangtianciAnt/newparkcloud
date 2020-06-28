package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo2;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SoftwaretransferService {

    void insert(SoftwaretransferVo softwaretransferVo, TokenModel tokenModel) throws Exception;

    void updateSoftwaretransfer(SoftwaretransferVo softwaretransferVo, TokenModel tokenModel) throws Exception;

    SoftwaretransferVo selectById(String softwaretransferid) throws Exception;

//    List<Softwaretransfer> getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception;

    List<SoftwaretransferVo2>  getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception;
}
