package com.nt.service_pfans.PFANS4000;

import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.dao_Pfans.PFANS4000.SealDetail;
import com.nt.dao_Pfans.PFANS4000.Vo.SealVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SealService {

    void insert(Seal seal, TokenModel tokenModel) throws Exception;

    Seal createbook(Seal seal, TokenModel tokenModel) throws Exception;

    //获取异常申请列表信息
    SealVo list(Seal seal) throws Exception;

    void upd(Seal seal, TokenModel tokenModel) throws Exception;
    //add-ws-12/21-印章盖印
    void insertnamedialog(String sealdetailname, String sealdetaildate, TokenModel tokenModel) throws Exception;

    List<SealDetail>selectcognition() throws Exception;

    void insertrecognition(String sealid,TokenModel tokenModel) throws Exception;
    //add-ws-12/21-印章盖印
    //根据id获取数据
    Seal One(String sealid) throws Exception;
}
