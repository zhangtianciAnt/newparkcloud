package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;


public interface AbNormalMapper extends MyMapper<AbNormal> {
    List<AbNormal> selectAbNormal(@Param("now") String now);
    List<AbNormal> selectAbNormalThisYear(AbNormal abNormal);

//    List<AbNormal> selectAbNormal1(@Param("") List<String>  );
}
