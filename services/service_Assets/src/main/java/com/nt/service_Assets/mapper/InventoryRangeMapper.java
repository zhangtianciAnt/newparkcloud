package com.nt.service_Assets.mapper;

import com.nt.dao_Assets.InventoryRange;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryRangeMapper extends MyMapper<InventoryRange> {
    int insertRangeList(@Param("list") List<InventoryRange> inventList);
}
