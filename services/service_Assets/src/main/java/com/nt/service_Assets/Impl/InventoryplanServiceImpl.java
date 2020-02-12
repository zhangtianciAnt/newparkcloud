package com.nt.service_Assets.Impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        List<Inventoryplan> rst = inventoryplanMapper.select(inventoryplan);
        for(Inventoryplan item:rst){
            InventoryResults r = new InventoryResults();
            r.setInventoryplan_id(item.getInventoryplan_id());
            List<InventoryResults> rs = inventoryResultsMapper.select(r);
            item.setTotalnumber(String.valueOf(rs.size()));
            item.setInquantity(String.valueOf(rs.stream().filter(i->(i.getResult().equals("2"))).count()));
            item.setUnquantity(String.valueOf(rs.stream().filter(i->(i.getResult().equals("1"))).count()));
        }

        return rst;
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
                if(inventoryRangeVo.getInventoryplan().getInventorycycle().equals(getinvent) && getsta.equals("0")){
                    inventoryplanMapper.delete(inv);
                }
            }
        }
        BeanUtils.copyProperties(inventoryRangeVo.getInventoryplan(), inventoryplan);
        inventoryplan.preInsert(tokenModel);
        inventoryplan.setInventoryplan_id(inventoryRangeid);
        List<InventoryRange> inventList = inventoryRangeVo.getInventoryRange();
        inventoryplanMapper.insertSelective(inventoryplan);
        if (inventList != null) {
            List<InventoryRange> insertList = new ArrayList<>();
            int rowindex = 0;
            for (InventoryRange inve : inventList) {
                rowindex = rowindex + 1;
                inve.preInsert(tokenModel);
                inve.setInventoryrange_id(UUID.randomUUID().toString());
                inve.setInventoryplan_id(inventoryRangeid);
                inve.setRowindex(rowindex);
//                inventoryRangeMapper.insertSelective(inve);
                insertList.add(inve);
            }
            inventoryRangeMapper.insertRangeList(insertList);
        }
        List<InventoryResults> invresuList = inventoryRangeVo.getInventoryResults();
        if (invresuList != null) {
            List<InventoryResults> insertList = new ArrayList<>();
            int rowindex = 0;
            for (InventoryResults invreu : invresuList) {
                rowindex = rowindex + 1;
                invreu.preInsert(tokenModel);
                invreu.setInventoryresults_id(UUID.randomUUID().toString());
                invreu.setInventoryplan_id(inventoryRangeid);
                invreu.setResult("1");
                invreu.setRowindex(rowindex);
//                inventoryResultsMapper.insertSelective(invreu);
                insertList.add(invreu);
            }
            inventoryResultsMapper.insertResultsList(insertList);
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

    @Override
    public int check(Inventoryplan inventoryplan) throws Exception {
        Inventoryplan condition = new Inventoryplan();
        condition.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        List<Inventoryplan> rst = inventoryplanMapper.select(condition);
        int count = 0;
        Date st = DateUtil.parse(inventoryplan.getInventorycycle().split("~")[0]);
        Date ed = DateUtil.parse(inventoryplan.getInventorycycle().split("~")[1]);
        for(Inventoryplan item:rst){
            if(StrUtil.isNotBlank(inventoryplan.getInventoryplan_id())){
                if(inventoryplan.getInventoryplan_id().equals(item.getInventoryplan_id())){
                    continue;
                }
            }
            String[] ts = item.getInventorycycle().split("~");
            if(ts.length == 2){
                Date st1 = DateUtil.parse(ts[0]);
                Date ed1 = DateUtil.parse(ts[1]);
                if(DateUtil.between(st, ed1, DateUnit.DAY,false) >= 0 && DateUtil.between(ed, st1, DateUnit.DAY,false) <= 0){
                    return 1;
                }
            }
        }
        return 0;
    }
}
