package com.nt.service_BASFSQL.mapper;

import com.nt.dao_BASFSQL.AccessLevel;
import com.nt.utils.MyMapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;


public interface AccessLevelMapper {
    List<AccessLevel> selectList();
}
