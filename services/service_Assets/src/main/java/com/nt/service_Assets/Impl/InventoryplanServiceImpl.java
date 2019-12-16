package com.nt.service_Assets.Impl;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Vo.InventoryplanVo;
import com.nt.service_Assets.InventoryplanService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Assets.mapper.InventoryplanMapper;
import com.nt.utils.AuthConstants;
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
        List<Inventoryplan> inventList = inventoryplanMapper.select(inventoryplan);
        if(inventList != null){
            for(Inventoryplan inv : inventList){
                String getinvent = inv.getInventorycycle();
                String getsta = inv.getStatus();
                if(inventoryplanVo.getInventoryplan().getInventorycycle().equals(getinvent) && !getsta.equals("2")){
                    inventoryplanMapper.delete(inv);
                }
            }
        }
        BeanUtils.copyProperties(inventoryplanVo.getInventoryplan(), inventoryplan);
        inventoryplan.preInsert(tokenModel);
        inventoryplan.setInventoryplan_id(inventoryplanid);
        Assets aa = new Assets();
        aa.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        List<Assets> aalist = assetsMapper.select(aa);
        inventoryplan.setTotalnumber(String.valueOf(aalist.size()));
        List<Assets> assetsList = inventoryplanVo.getAssets();
        inventoryplan.setInquantity(String.valueOf(assetsList.size()));
        inventoryplan.setUnquantity(String.valueOf(aalist.size() - assetsList.size()));
        inventoryplanMapper.insertSelective(inventoryplan);
        if (assetsList != null) {
            int rowindex = 0;
            for (Assets ass : assetsList) {
                rowindex = rowindex + 1;
                ass.preInsert(tokenModel);
                ass.setAssets_id(UUID.randomUUID().toString());
                ass.setInventoryplan_id(inventoryplanid);
                ass.setStatus("4");
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
        String inventoryplanid = inventoryplan.getInventoryplan_id();
        Assets asp = new Assets();
        asp.setInventoryplan_id(inventoryplanid);
        List<Assets> alist = assetsMapper.select(asp);
        if(alist != null){
            int account = 0;
            for(Assets as : alist){
                if(!as.getResult().equals("")){
                    account ++;
                }
            }
            if(account == alist.size()) {
                inventoryplan.setStatus("2");
                inventoryplan.setInventoryplan_id(inventoryplanid);
                inventoryplanMapper.updateByPrimaryKeySelective(inventoryplan);
            }
        }
        List<Assets> assetsList = inventoryplanVo.getAssets();
        int rowindex = 0;
        if (assetsList != null) {
            for (Assets ass : assetsList) {
                rowindex = rowindex + 1;
                ass.preInsert(tokenModel);
                ass.setInventoryplan_id(inventoryplanid);
                ass.setRowindex(rowindex);
                ass.setStatus("4");
                assetsMapper.updateByPrimaryKey(ass);
            }
        }
    }

    @Override
    public List<Assets> selectAll(Assets assets) throws Exception {
        return assetsMapper.select(assets);
    }

    @Override
    public void isDelInventory(Inventoryplan inventoryplan) throws Exception {
        inventoryplanMapper.delete(inventoryplan);
    }

    @Override
    public InventoryplanVo selectById(String inventoryplanid) throws Exception {

        InventoryplanVo inventoryplanVo = new InventoryplanVo();
        Assets assets = new Assets();
        assets.setInventoryplan_id(inventoryplanid);
        assets.setStatus("4");
        List<Assets> assetsList = assetsMapper.select(assets);
        assetsList = assetsList.stream().sorted(Comparator.comparing(Assets::getRowindex)).collect(Collectors.toList());
        Inventoryplan inventoryplan = inventoryplanMapper.selectByPrimaryKey(inventoryplanid);
        inventoryplanVo.setInventoryplan(inventoryplan);
        inventoryplanVo.setAssets(assetsList);
        return inventoryplanVo;
    }

}
