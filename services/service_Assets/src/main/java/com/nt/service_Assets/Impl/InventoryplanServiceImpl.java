package com.nt.service_Assets.Impl;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.InventoryRange;
import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.InventoryResults;
import com.nt.dao_Assets.Vo.InventoryRangeVo;
import com.nt.service_Assets.InventoryplanService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Assets.mapper.InventoryplanMapper;
import com.nt.service_Assets.mapper.InventoryRangeMapper;
import com.nt.service_Assets.mapper.InventoryResultsMapper;
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

    @Autowired
    private InventoryRangeMapper inventoryRangeMapper;

    @Autowired
    private InventoryResultsMapper inventoryResultsMapper;

    @Override
    public List<Inventoryplan> get(Inventoryplan inventoryplan) throws Exception {
        return inventoryplanMapper.select(inventoryplan);
    }

    @Override
    public void insert(InventoryRangeVo inventoryRangeVo, TokenModel tokenModel) throws Exception {
        String inventoryRangeid = UUID.randomUUID().toString();
        Inventoryplan inventoryplan = new Inventoryplan();
        List<Inventoryplan> invtList = inventoryplanMapper.select(inventoryplan);
        if(invtList != null){
            for(Inventoryplan inv : invtList){
                String getinvent = inv.getInventorycycle();
                String getsta = inv.getStatus();
                if(inventoryRangeVo.getInventoryplan().getInventorycycle().equals(getinvent) && !getsta.equals("0")){
                    inventoryplanMapper.delete(inv);
                }
            }
        }
        BeanUtils.copyProperties(inventoryRangeVo.getInventoryplan(), inventoryplan);
        inventoryplan.preInsert(tokenModel);
        inventoryplan.setInventoryplan_id(inventoryRangeid);
        Assets aa = new Assets();
        List<Assets> aalist = assetsMapper.select(aa);
        inventoryplan.setTotalnumber(String.valueOf(aalist.size()));
        List<InventoryRange> inventList = inventoryRangeVo.getInventoryRange();
        inventoryplan.setInquantity(String.valueOf(inventList.size()));
        inventoryplan.setUnquantity(String.valueOf(aalist.size() - inventList.size()));
        inventoryplanMapper.insertSelective(inventoryplan);
        if (inventList != null) {
            int rowindex = 0;
            for (InventoryRange inve : inventList) {
                rowindex = rowindex + 1;
                inve.preInsert(tokenModel);
                inve.setInventoryrange_id(UUID.randomUUID().toString());
                inve.setInventoryplan_id(inventoryRangeid);
                inve.setRowindex(rowindex);
                inventoryRangeMapper.insertSelective(inve);
            }
        }
        List<InventoryResults> invresuList = inventoryRangeVo.getInventoryResults();
        if (invresuList != null) {
            int rowindex = 0;
            for (InventoryResults invreu : invresuList) {
                rowindex = rowindex + 1;
                invreu.preInsert(tokenModel);
                invreu.setInventoryresults_id(UUID.randomUUID().toString());
                invreu.setInventoryplan_id(inventoryRangeid);
                invreu.setResult("1");
                invreu.setRowindex(rowindex);
                inventoryResultsMapper.insertSelective(invreu);
            }
        }
    }

    @Override
    public void update(InventoryRangeVo inventoryRangeVo, TokenModel tokenModel) throws Exception {
        Inventoryplan inventoryplan = new Inventoryplan();
        BeanUtils.copyProperties(inventoryRangeVo.getInventoryplan(), inventoryplan);
        inventoryplan.preUpdate(tokenModel);
        inventoryplanMapper.updateByPrimaryKey(inventoryplan);
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
    public InventoryRangeVo selectById(String inventoryRangeid) throws Exception {

        InventoryRangeVo inventoryRangeVo = new InventoryRangeVo();
        InventoryRange inventoryRange = new InventoryRange();
        inventoryRange.setInventoryplan_id(inventoryRangeid);
        List<InventoryRange> inveList = inventoryRangeMapper.select(inventoryRange);
        inveList = inveList.stream().sorted(Comparator.comparing(InventoryRange::getRowindex)).collect(Collectors.toList());
        Inventoryplan inventoryplan = inventoryplanMapper.selectByPrimaryKey(inventoryRangeid);
        inventoryRangeVo.setInventoryplan(inventoryplan);
        inventoryRangeVo.setInventoryRange(inveList);
        return inventoryRangeVo;
    }

    @Override
    public List<InventoryResults> selectByResult(String inventoryresultsid) throws Exception {
        InventoryResults inventoryResults = new InventoryResults();
        inventoryResults.setInventoryplan_id(inventoryresultsid);
        return inventoryResultsMapper.select(inventoryResults);
    }

}
