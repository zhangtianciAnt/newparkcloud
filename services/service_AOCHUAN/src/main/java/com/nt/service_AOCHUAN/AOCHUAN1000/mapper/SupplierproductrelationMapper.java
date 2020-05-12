package com.nt.service_AOCHUAN.AOCHUAN1000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SupplierproductrelationMapper extends MyMapper<Supplierproductrelation> {
    @Select("select SUPPLIERPRODUCTRELATION_ID,SUPPLIERBASEINFOR_ID,PRODUCTS_ID from supplierproductrelation WHERE SUPPLIERBASEINFOR_ID = #{supplierbaseinforId} order by createon")
    List<Supplierproductrelation> getBySupplierbaseinforId(@Param("supplierbaseinforId") String supplierbaseinforId);

    @Delete("delete from supplierproductrelation where SUPPLIERBASEINFOR_ID = #{baseinforId}")
    void deleteByBaseinforId(@Param("baseinforId") String baseinforId);
}
