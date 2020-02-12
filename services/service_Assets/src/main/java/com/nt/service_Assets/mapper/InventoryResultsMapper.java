package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.InventoryResults;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryResultsMapper extends MyMapper<InventoryResults> {
    int insertResultsList(@Param("list") List<InventoryResults> insertList);
}
