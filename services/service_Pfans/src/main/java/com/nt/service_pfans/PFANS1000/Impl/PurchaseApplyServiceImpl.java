package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.PurchaseApply;
import com.nt.dao_Pfans.PFANS1000.ShoppingDetailed;
import com.nt.dao_Pfans.PFANS1000.Vo.PurchaseApplyVo;
import com.nt.service_pfans.PFANS1000.PurchaseApplyService;
import com.nt.service_pfans.PFANS1000.mapper.PurchaseApplyMapper;
import com.nt.service_pfans.PFANS1000.mapper.ShoppingDetailedMapper;
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
public class PurchaseApplyServiceImpl implements PurchaseApplyService {

    @Autowired
    private PurchaseApplyMapper purchaseApplyMapper;
    @Autowired
    private ShoppingDetailedMapper shoppingDetailedMapper;

    //按id查询
    @Override
    public PurchaseApplyVo selectById(String purchaseApplyid) throws Exception {
        PurchaseApplyVo pucaVo = new PurchaseApplyVo();
        ShoppingDetailed shoppingDetailed = new ShoppingDetailed();
        shoppingDetailed.setShoppingdetailed_id(purchaseApplyid);
        List<ShoppingDetailed> shoppingDetailedlist = shoppingDetailedMapper.select(shoppingDetailed);
        shoppingDetailedlist = shoppingDetailedlist.stream().sorted(Comparator.comparing(ShoppingDetailed::getRowindex)).collect(Collectors.toList());
        PurchaseApply puca = purchaseApplyMapper.selectByPrimaryKey(purchaseApplyid);
        pucaVo.setPurchaseApply(puca);
        pucaVo.setShoppingDetaileds(shoppingDetailedlist);
        return pucaVo;
    }

    //更新
    @Override
    public void update(PurchaseApplyVo purchaseApplyVo, TokenModel tokenModel) throws Exception {
        PurchaseApply purchaseApply = new PurchaseApply();
        BeanUtils.copyProperties(purchaseApplyVo.getPurchaseApply(), purchaseApply);
        purchaseApply.preUpdate(tokenModel);
        purchaseApplyMapper.updateByPrimaryKey(purchaseApply);
        String spurchaseApplyid = purchaseApply.getPurchaseapply_id();
        ShoppingDetailed spd = new ShoppingDetailed();
        spd.setPurchaseapply_id(spurchaseApplyid);
        shoppingDetailedMapper.delete(spd);
        List<ShoppingDetailed> shoppingDetailedlist = purchaseApplyVo.getShoppingDetaileds();
        if (shoppingDetailedlist != null) {
            int rowundex = 0;
            for (ShoppingDetailed shoppingDetailed : shoppingDetailedlist) {
                rowundex = rowundex + 1;
                shoppingDetailed.preInsert(tokenModel);
                shoppingDetailed.setShoppingdetailed_id(UUID.randomUUID().toString());
                shoppingDetailed.setPurchaseapply_id(spurchaseApplyid);
                shoppingDetailed.setRowindex(rowundex);
                shoppingDetailedMapper.insertSelective(shoppingDetailed);
            }
        }
    }

    //新建
    @Override
    public void insert(PurchaseApplyVo purchaseApplyVo, TokenModel tokenModel) throws Exception {
        String purchaseApplyid = UUID.randomUUID().toString();
        PurchaseApply purchaseApply = new PurchaseApply();
        BeanUtils.copyProperties(purchaseApplyVo.getPurchaseApply(), purchaseApply);
        purchaseApply.preInsert(tokenModel);
        purchaseApply.setPurchaseapply_id(purchaseApplyid);
        purchaseApplyMapper.insertSelective(purchaseApply);
        List<ShoppingDetailed> shoppingDetailedlist = purchaseApplyVo.getShoppingDetaileds();
        if (shoppingDetailedlist != null) {
            int rowundex = 0;
            for (ShoppingDetailed shoppingDetailed : shoppingDetailedlist) {
                rowundex = rowundex + 1;
                shoppingDetailed.preInsert(tokenModel);
                shoppingDetailed.setShoppingdetailed_id(UUID.randomUUID().toString());
                shoppingDetailed.setPurchaseapply_id(purchaseApplyid);
                shoppingDetailed.setRowindex(rowundex);
                shoppingDetailedMapper.insertSelective(shoppingDetailed);
            }
        }
    }
}
