package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReimbursementMapper extends MyMapper<Reimbursement> {

    //删除-项目表
    void deleteFromReimbursementByDoubleKey(@Param("modifyby") String modifyby, @Param("id") String id, @Param("status") String status);
    //存在Check
    public List<Reimbursement> existCheck(@Param("id") String id,@Param("status") String status);
    //唯一性Check
    public List<Reimbursement> uniqueCheck(@Param("id") String id,@Param("reim_no") String reim_no);
}
