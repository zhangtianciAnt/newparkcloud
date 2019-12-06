package com.nt.service_Assets;

import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.Vo.InventoryplanVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface InventoryplanService {

    List<Inventoryplan> get(Inventoryplan inventoryplan) throws Exception;

    void insert(InventoryplanVo inventoryplanVo, TokenModel tokenModel) throws Exception;

    void update(InventoryplanVo inventoryplanVo, TokenModel tokenModel) throws Exception;

    List<Assets> selectAll(Assets assets) throws Exception;


}
