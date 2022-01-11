package com.nt.service_pfans.PFANS5000.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LogDeadlineMapper {
    @Select("select right(my_LogDeadline(#{nums}),2)")
    String getLogDeadline(@Param("nums") Integer nums);
}
