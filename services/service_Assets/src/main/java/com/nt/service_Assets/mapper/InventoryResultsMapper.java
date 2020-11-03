package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.InventoryResults;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InventoryResultsMapper extends MyMapper<InventoryResults> {
    int insertResultsList(@Param("list") List<InventoryResults> insertList);

    @Select("select * from inventoryresults where barcode =  #{barcode} and inventoryplan_id = #{inventoryplan_id}")
    InventoryResults selectInResults(@Param("barcode") String barcode,@Param("inventoryplan_id")String inventoryplan_id);
}
