package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.PurchaseApply;
import com.nt.dao_Pfans.PFANS1000.ShoppingDetailed;
import com.nt.dao_Pfans.PFANS1000.Vo.PurchaseApplyVo;
import com.nt.service_pfans.PFANS1000.PurchaseApplyService;
import com.nt.service_pfans.PFANS1000.mapper.PurchaseApplyMapper;
import com.nt.service_pfans.PFANS1000.mapper.ShoppingDetailedMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
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

    //列表查询
    @Override
    public List<PurchaseApply> get(PurchaseApply purchaseApply) throws Exception {
        return purchaseApplyMapper.select(purchaseApply);
    }

    @Override
    public List<PurchaseApply> selectPurchaseApply() throws Exception {
        return purchaseApplyMapper.selectPurchaseApply();
    }

    //按id查询
    @Override
    public PurchaseApplyVo selectById(String purchaseApplyid) throws Exception {
        PurchaseApplyVo pucaVo = new PurchaseApplyVo();
        ShoppingDetailed shoppingDetailed = new ShoppingDetailed();
        shoppingDetailed.setPurchaseapply_id(purchaseApplyid);
        List<ShoppingDetailed> shoppingDetailedlist = shoppingDetailedMapper.select(shoppingDetailed);
        shoppingDetailedlist = shoppingDetailedlist.stream().sorted(Comparator.comparing(ShoppingDetailed::getRowindex)).collect(Collectors.toList());
        PurchaseApply puca = purchaseApplyMapper.selectByPrimaryKey(purchaseApplyid);
        pucaVo.setPurchaseApply(puca);
        pucaVo.setShoppingDetailed(shoppingDetailedlist);
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
        List<ShoppingDetailed> shoppingDetailedlist = purchaseApplyVo.getShoppingDetailed();
        if (shoppingDetailedlist != null) {
            int rowindex = 0;
            for (ShoppingDetailed shoppingDetailed : shoppingDetailedlist) {
                rowindex = rowindex + 1;
                shoppingDetailed.preInsert(tokenModel);
                shoppingDetailed.setShoppingdetailed_id(UUID.randomUUID().toString());
                shoppingDetailed.setPurchaseapply_id(spurchaseApplyid);
                shoppingDetailed.setRowindex(rowindex);
                shoppingDetailedMapper.insertSelective(shoppingDetailed);
            }
        }
    }

    //新建
    @Override
    public void insert(PurchaseApplyVo purchaseApplyVo, TokenModel tokenModel) throws Exception {
        String purchaseApplyid = UUID.randomUUID().toString();
        PurchaseApply purchaseApply = new PurchaseApply();
        //add-ws-根据当前年月日从001开始增加费用编号
        List<PurchaseApply> purchaseApplylist = purchaseApplyMapper.selectAll();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String year = sf1.format(date);
        int number = 0;
        String Numbers = "";
        String no = "";
        if(purchaseApplylist.size()>0){
            for(PurchaseApply purchase :purchaseApplylist){
                if(purchase.getPurchasenumbers()!="" && purchase.getPurchasenumbers()!=null){
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(purchase.getPurchasenumbers(), 2,8));
                    if(Integer.valueOf(year).equals(Integer.valueOf(checknumber))){
                        number = number+1;
                    }
                }

            }
            if(number<=8){
                no="00"+(number + 1);
            }else{
                no="0"+(number + 1);
            }
        }else{
            no = "001";
        }
        Numbers = "QY"+year+ no;
        //add-ws-根据当前年月日从001开始增加费用编号
        BeanUtils.copyProperties(purchaseApplyVo.getPurchaseApply(), purchaseApply);
        purchaseApply.preInsert(tokenModel);
        purchaseApply.setPurchaseapply_id(purchaseApplyid);
        purchaseApply.setPurchasenumbers(Numbers);
        purchaseApplyMapper.insertSelective(purchaseApply);
        List<ShoppingDetailed> shoppingDetailedlist = purchaseApplyVo.getShoppingDetailed();
        if (shoppingDetailedlist != null) {
            int rowindex = 0;
            for (ShoppingDetailed shoppingDetailed : shoppingDetailedlist) {
                rowindex = rowindex + 1;
                shoppingDetailed.preInsert(tokenModel);
                shoppingDetailed.setShoppingdetailed_id(UUID.randomUUID().toString());
                shoppingDetailed.setPurchaseapply_id(purchaseApplyid);
                shoppingDetailed.setRowindex(rowindex);
                shoppingDetailedMapper.insertSelective(shoppingDetailed);
            }
        }
    }
}
