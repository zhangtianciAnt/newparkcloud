package com.nt.service_AOCHUAN.AOCHUAN5000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.Totalmoney;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FinSalesMapper extends MyMapper<FinSales> {

    //存在Check
    public List<FinSales> existCheck(@Param("id") String id,@Param("status") String status);

    //唯一性Check
    public List<FinSales> uniqueCheck(@Param("id") String id,@Param("contractnumber") String contractnumber);

    //更新走货
    void updateTransportGood(@Param("arrivaltime") Date arrivaltime,@Param("modifyby") String modifyby, @Param("id") String id);

    public List<Totalmoney> getHK();
}
