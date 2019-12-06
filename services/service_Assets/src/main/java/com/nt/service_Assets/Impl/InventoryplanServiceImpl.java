package com.nt.service_Assets.Impl;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Vo.InventoryplanVo;
import com.nt.service_Assets.InventoryplanService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Assets.mapper.InventoryplanMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class InventoryplanServiceImpl implements InventoryplanService {

    @Autowired
    private InventoryplanMapper inventoryplanMapper;
    @Autowired
    private AssetsMapper assetsMapper;

    @Override
    public List<Inventoryplan> get(Inventoryplan inventoryplan) throws Exception {
        return inventoryplanMapper.select(inventoryplan);
    }

    @Override
    public void insert(InventoryplanVo inventoryplanVo, TokenModel tokenModel) throws Exception {
        String inventoryplanid = UUID.randomUUID().toString();
        Inventoryplan inventoryplan = new Inventoryplan();
        BeanUtils.copyProperties(inventoryplanVo.getInventoryplan(), inventoryplan);
        inventoryplan.preInsert(tokenModel);
        inventoryplan.setInventoryplan_id(inventoryplanid);
        List<Assets> assetsList = inventoryplanVo.getAssets();
        if (assetsList != null) {
            int rowindex = 0;
            for (Assets ass : assetsList) {
                rowindex += rowindex;
                ass.preInsert(tokenModel);
                ass.setAssets_id(UUID.randomUUID().toString());
                ass.setInventoryplan_id(inventoryplanid);
                ass.setRowindex(rowindex);
                assetsMapper.insertSelective(ass);
            }
        }
    }

    @Override
    public void update(InventoryplanVo inventoryplanVo, TokenModel tokenModel) throws Exception {
        Inventoryplan inventoryplan = new Inventoryplan();
        BeanUtils.copyProperties(inventoryplanVo.getInventoryplan(), inventoryplan);
        inventoryplan.preUpdate(tokenModel);
        inventoryplanMapper.updateByPrimaryKey(inventoryplan);
        String inventoryplanid = UUID.randomUUID().toString();
        Assets assets = new Assets();
        assets.setInventoryplan_id(inventoryplanid);
        assetsMapper.delete(assets);
        List<Assets> assetsList = inventoryplanVo.getAssets();
        if (assetsList != null) {
            int rowindex = 0;
            for (Assets ass : assetsList) {
                rowindex += rowindex;
                ass.preInsert(tokenModel);
                ass.setAssets_id(UUID.randomUUID().toString());
                ass.setInventoryplan_id(inventoryplanid);
                ass.setRowindex(rowindex);
                assetsMapper.insertSelective(ass);
            }
        }
    }

    @Override
    public List<Assets> selectAll(Assets assets) throws Exception {
        return assetsMapper.select(assets);
    }


}
