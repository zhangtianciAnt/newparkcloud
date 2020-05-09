package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TransportGoodMapper extends MyMapper<TransportGood> {
    @Select("select TRANSPORTGOOD_ID,CONTRACTNUMBER,PRODUCTEN,PRODUCTUS,CUSTOMER,AMOUNT,UNITPRICE,SALESAMOUNT,TYPE from transportgood where SUPPLIERID = #{id}")
    List<TransportGood> getForSupplier(@Param("id") String id);

    @Select("select TRANSPORTGOOD_ID,CONTRACTNUMBER,PRODUCTEN,PRODUCTUS,CUSTOMER,AMOUNT,UNITPRICE,SALESAMOUNT,TYPE from transportgood where CUSTOMERID = #{id}")
    List<TransportGood> getForCustomer(@Param("id") String id);

    @Select("SELECT * FROM `transportgood` where contractnumber = #{id} and (type = 1 or type = 2 or type = 3 or type = 0)")
    public TransportGood selectPeo(@Param("id") String id);

}
