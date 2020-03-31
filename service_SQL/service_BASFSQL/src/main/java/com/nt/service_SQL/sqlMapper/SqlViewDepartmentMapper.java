package com.nt.service_SQL.sqlMapper;

import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlUserInfo;
import com.nt.dao_SQL.SqlViewDepartment;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SqlViewDepartmentMapper {
    List<SqlViewDepartment> list();
}
