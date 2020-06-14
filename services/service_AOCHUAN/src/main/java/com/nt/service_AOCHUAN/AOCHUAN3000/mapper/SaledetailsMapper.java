package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Saledetails;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaledetailsMapper extends MyMapper<Saledetails> {
    int insertSaledetailsList(@Param("list") List<Saledetails> EnquiryList);
}
