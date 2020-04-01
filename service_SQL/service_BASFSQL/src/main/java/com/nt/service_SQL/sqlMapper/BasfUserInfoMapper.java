package com.nt.service_SQL.sqlMapper;

import com.nt.dao_SQL.SqlAPBCardHolder;

import java.util.List;

/**
 * basf门禁用户表mapper
 */
public interface BasfUserInfoMapper {
    int selectUsersCount();

    int selectContractorsCount();

    int selectVisitorsCount();

    List<Integer> selectDeviceUsersCount();

    List<SqlAPBCardHolder> selectDeviceUsersCnt();

    List<SqlAPBCardHolder> selectDeviceOutUsersCnt();
}
