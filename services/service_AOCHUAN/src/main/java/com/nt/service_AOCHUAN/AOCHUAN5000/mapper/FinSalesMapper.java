package com.nt.service_AOCHUAN.AOCHUAN5000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinSalesMapper extends MyMapper<FinSales> {

    //获取财务-销售数据
    List<FinSales> getFinSalesListByArrivalStatus(@Param("arrivalStatus") String arrivalStatus, @Param("status") String status);

    //存在Check
    public List<FinSales> existCheck(@Param("id") String id,@Param("status") String status);

    //唯一性Check
    public List<FinSales> uniqueCheck(@Param("id") String id,@Param("contractnumber") String contractnumber);
}
