package com.nt.service_Assets;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Vo.InventoryRangeVo;
import com.nt.dao_Assets.InventoryResults;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface InventoryplanService {

    List<Inventoryplan> get(Inventoryplan inventoryplan) throws Exception;

    void insert(InventoryRangeVo inventoryRangeVo, TokenModel tokenModel) throws Exception;

    void update(InventoryRangeVo inventoryRangeVo, TokenModel tokenModel) throws Exception;

    List<Assets> selectAll(Assets assets) throws Exception;

    void isDelInventory(Inventoryplan inventoryplan) throws Exception;

    InventoryRangeVo selectById(String inventoryrangeid) throws Exception;

    List<InventoryResults> selectByResult(String inventoryresultsid) throws Exception;

}
