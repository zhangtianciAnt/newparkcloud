package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.Inventoryplan;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InventoryplanMapper extends MyMapper<Inventoryplan> {
    @Select("select inventoryplan_id,inventorycycle  from inventoryplan where status  in ('0','4') ")
    List<Inventoryplan> selectList();
}
