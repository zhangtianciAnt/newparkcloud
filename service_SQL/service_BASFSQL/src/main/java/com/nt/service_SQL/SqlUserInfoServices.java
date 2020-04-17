package com.nt.service_SQL;

import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlUserInfo;
import com.nt.dao_SQL.SqlViewDepartment;

import java.util.List;

public interface SqlUserInfoServices {

    List<SqlUserInfo> list() throws Exception;

    List<SqlAPBCardHolder> selectapbcard() throws Exception;

    SqlViewDepartment selectdepartmentid(String recnum) throws Exception;

    List<SqlAPBCardHolder> selectapbcardholder() throws Exception;

    List<SqlViewDepartment> selectdepartment() throws Exception;
}
