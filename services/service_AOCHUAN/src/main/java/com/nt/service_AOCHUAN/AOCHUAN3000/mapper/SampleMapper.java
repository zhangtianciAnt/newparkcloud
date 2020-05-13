package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SampleMapper extends MyMapper<Sample> {
    @Select("select * from sample WHERE SUPPLIERBASEINFOR_ID = #{id}")
    List<Sample> getForSupplier(@Param("id") String id);

    @Select("select * from sample WHERE CUSTOMERBASEINFOR_ID = #{id}")
    List<Sample> getForCustomer(@Param("id") String id);
}
