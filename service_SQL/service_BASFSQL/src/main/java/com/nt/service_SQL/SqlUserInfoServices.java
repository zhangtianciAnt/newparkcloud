package com.nt.service_SQL;

import com.nt.dao_SQL.SqlUserInfo;

import java.util.List;

public interface SqlUserInfoServices {

    List<SqlUserInfo> list() throws Exception;
}
