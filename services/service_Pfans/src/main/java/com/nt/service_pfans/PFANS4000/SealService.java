package com.nt.service_pfans.PFANS4000;

import com.nt.dao_Assets.Inventoryplan;
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

    //增加分页 ztc fr
    List<Seal> sealList(Seal seal) throws Exception;

    List<SealDetail> sealDetailList() throws Exception;
    //增加分页 ztc to

    void upd(Seal seal, TokenModel tokenModel) throws Exception;
    //add-ws-12/21-印章盖印
    void insertnamedialog(String sealdetailname, String sealdetaildate, TokenModel tokenModel) throws Exception;
    // 盖印监管者增加履历 ztc 0723 fr
    List<SealDetail> selectcognition() throws Exception;
    // 盖印监管者增加履历 ztc 0723 to

    void insertrecognition(String sealid,TokenModel tokenModel) throws Exception;
    //add-ws-12/21-印章盖印
    //根据id获取数据
    Seal One(String sealid) throws Exception;

    int selectEffective(SealDetail sealDetail) throws Exception;
    // 盖印监管者增加履历 ztc 0723 fr
    SealDetail getEffSeal(String newDateStr) throws Exception;
    // 盖印监管者增加履历 ztc 0723 to
}
