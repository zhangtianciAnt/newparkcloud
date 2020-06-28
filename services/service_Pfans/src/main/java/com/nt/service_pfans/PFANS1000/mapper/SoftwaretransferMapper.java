package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo2;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SoftwaretransferMapper extends MyMapper<Softwaretransfer>{
    List<SoftwaretransferVo2> getSoftware(@Param("user") String user);
}
