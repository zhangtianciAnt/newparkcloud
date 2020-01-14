package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.OtherOne;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface OtherOneMapper extends MyMapper<OtherOne> {
    int insertOtherOne(@Param("otherones") List<OtherOne> otherOnes);
}
