package com.nt.service_SQL;

import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlUserInfo;
import com.nt.dao_SQL.SqlViewDepartment;

import java.util.List;

public interface SqlViewDepartmentServices {

    List<SqlViewDepartment> sqllist() throws Exception;
}
