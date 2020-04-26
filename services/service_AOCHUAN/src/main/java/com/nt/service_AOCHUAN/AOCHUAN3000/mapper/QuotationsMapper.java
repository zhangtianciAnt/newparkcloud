package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.QuotationsAndEnquiry;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QuotationsMapper extends MyMapper<Quotations> {
    @Select("select count(*) from quotations")
     int select_Count();

    @Select("select qu.quotationsno,qu.productch,qu.producten,qu.account,en.quotedprice,en.salesquotation,qu.quote from quotations as qu inner join enquiry as en on qu.QUOTATIONS_ID = en.quotations_id where en.supplierid = #{id}")
    List<QuotationsAndEnquiry> getForSupplier(@Param("id") String id);

    @Select("select QUOTATIONSNO,PRODUCTCH,PRODUCTEN,ACCOUNT,QUOTE from quotations WHERE ACCOUNTID = #{id}")
    List<Quotations> getForCustomer(@Param("id") String id);
}
