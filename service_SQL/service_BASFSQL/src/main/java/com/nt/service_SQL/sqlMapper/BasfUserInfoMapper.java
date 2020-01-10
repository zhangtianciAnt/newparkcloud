package com.nt.service_SQL.sqlMapper;

import java.util.List;

/**
 * basf门禁用户表mapper
 */
public interface BasfUserInfoMapper {
    int selectUsersCount();

    int selectContractorsCount();

    int selectVisitorsCount();

    List<Integer> selectDeviceUsersCount();
}
