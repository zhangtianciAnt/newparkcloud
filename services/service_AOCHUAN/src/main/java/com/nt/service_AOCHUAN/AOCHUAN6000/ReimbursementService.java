package com.nt.service_AOCHUAN.AOCHUAN6000;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ReimbursementService {

    //获取费用主表
    List<Reimbursement> getReimbursementList(Reimbursement reimbursement) throws Exception;
    //获取费用明细表
    List<ReimbursementDetail> getReimbursementDetailList(ReimbursementDetail reimbursementDetail) throws Exception;
    //新建
    void insert(Object object, TokenModel tokenModel)throws Exception;
    //更新
    void update(Object object, TokenModel tokenModel) throws Exception;
    //删除
    void delete(Object object, TokenModel tokenModel) throws Exception;
    //存在Check
    Boolean existCheck(Object object) throws Exception;
    //唯一性Check
    Boolean uniqueCheck(Reimbursement reimbursement) throws Exception;
}
