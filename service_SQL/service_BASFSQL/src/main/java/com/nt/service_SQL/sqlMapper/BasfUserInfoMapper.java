package com.nt.service_SQL.sqlMapper;

/**
 * basf门禁用户表mapper
 */
public interface BasfUserInfoMapper {
    int selectUsersCount();

    int selectContractorsCount();

    int selectVisitorsCount();
}
