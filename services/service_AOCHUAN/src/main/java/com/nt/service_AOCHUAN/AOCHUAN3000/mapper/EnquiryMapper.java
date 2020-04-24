package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnquiryMapper extends MyMapper<Enquiry> {
    int insertEnquiryList(@Param("list") List<Enquiry> EnquiryList);
}
