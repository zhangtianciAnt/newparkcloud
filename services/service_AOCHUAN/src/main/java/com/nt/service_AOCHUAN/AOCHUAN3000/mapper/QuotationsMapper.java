package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.QuoAndEnq;
import com.nt.utils.MyMapper;
import com.nt.service_AOCHUAN.AOCHUAN8000.NumberSql;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface QuotationsMapper extends MyMapper<Quotations> {
    @Select("select count(*) from quotations")
     int select_Count();

    @Select("select qu.quotations_id,qu.quotationsno,qu.productch,qu.producten,qu.account,qu.type,en.quotedprice,en.salesquotation,qu.quote from quotations as qu inner join enquiry as en on qu.QUOTATIONS_ID = en.quotations_id where en.supplierid = #{id}")
    List<QuoAndEnq> getForSupplier(@Param("id") String id);

    @Select("select QUOTATIONS_ID,QUOTATIONSNO,PRODUCTCH,PRODUCTEN,ACCOUNT,QUOTE,TYPE from quotations WHERE ACCOUNTID = #{id}")
    List<Quotations> getForCustomer(@Param("id") String id);

}
