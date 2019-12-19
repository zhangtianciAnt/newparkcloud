package com.nt.dao_Assets.Vo;

import com.nt.dao_Assets.InventoryRange;
import com.nt.dao_Assets.InventoryResults;
import com.nt.dao_Assets.Inventoryplan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventoryplanVo")
public class InventoryRangeVo {
    /**
     * 盘点列表
     */
    private Inventoryplan inventoryplan;

    /**
     * 盘点范围
     */
    private List<InventoryRange> inventoryRange;

    /**
     * 盘点结果
     */
    private List<InventoryResults> inventoryResults;

}
