package com.nt.service_Org.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LogDeadlineOrgMapper {
    @Select("select right(my_LogDeadline(#{nums}),2)")
    String getLogDeadline(@Param("nums") Integer nums);
}
