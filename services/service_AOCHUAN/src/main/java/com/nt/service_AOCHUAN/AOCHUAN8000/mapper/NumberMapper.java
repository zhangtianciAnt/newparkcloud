package com.nt.service_AOCHUAN.AOCHUAN8000.mapper;


import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.service_AOCHUAN.AOCHUAN8000.Impl.ContractNumber;
import com.nt.service_AOCHUAN.AOCHUAN8000.NumberSql;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface NumberMapper {
    @SelectProvider(type = NumberSql.class,method = "selectCounts")
    int selectCounts(@Param("tableName") String tableName);
}
