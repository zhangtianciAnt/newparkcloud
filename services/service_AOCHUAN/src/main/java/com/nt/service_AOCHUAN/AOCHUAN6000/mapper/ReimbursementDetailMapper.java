package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReimbursementDetailMapper extends MyMapper<ReimbursementDetail> {

    //获取-记录列表
    public List<ReimbursementDetail> getReimbursementDetailList(@Param("remd_no") String remd_no,  @Param("status") String status);
    //删除-记录表
    void deleteFromReimbursementDetailByPrikey(@Param("modifyby") String modifyby,@Param("remd_no") String remd_no,@Param("id") String id,@Param("status") String status);
    //存在Check
    public List<ReimbursementDetail> existCheck(@Param("id") String id,@Param("status") String status);
}
