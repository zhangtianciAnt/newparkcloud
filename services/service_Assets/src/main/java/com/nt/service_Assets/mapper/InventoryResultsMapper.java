package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.InventoryResults;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface InventoryResultsMapper extends MyMapper<InventoryResults> {
    int insertResultsList(@Param("list") List<InventoryResults> insertList);


    List<InventoryResults> selectInResults(List<Map<String,String>> inventoryrangeidlist);
}
