package com.nt.dao_Assets.Vo;

import com.nt.dao_Assets.Assets;
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
public class InventoryplanVo{
    /**
     * 盘点列表
     */
    private Inventoryplan inventoryplan;

    /**
     * 资产
     */
    private List<Assets> assets;

}
