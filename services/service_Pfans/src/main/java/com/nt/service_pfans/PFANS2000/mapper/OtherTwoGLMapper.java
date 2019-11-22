package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


public interface OtherTwoGLMapper extends MyMapper<OtherTwo> {

    void getDataList(@Param("day") Date application_date);

}
