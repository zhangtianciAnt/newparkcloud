package com.nt.service_AOCHUAN.AOCHUAN5000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinPurchaseMapper extends MyMapper<FinPurchase> {

    //存在Check
    public List<FinPurchase> existCheck(@Param("id") String id, @Param("status") String status);

    //唯一性Check
    public List<FinPurchase> uniqueCheck(@Param("id") String id,@Param("contractnumber") String contractnumber);
}
