package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.service_pfans.PFANS3000.PurchaseService;
import com.nt.service_pfans.PFANS3000.mapper.PurchaseMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public List<Purchase> getPurchase(Purchase purchase) {
        return purchaseMapper.select(purchase);
    }

    @Override
    public List<Purchase> getPurchaselist(Purchase purchase) {
        return purchaseMapper.select(purchase);
    }

    @Override
    public Purchase One(String purchase_id) throws Exception {
        return purchaseMapper.selectByPrimaryKey(purchase_id);
    }

    @Override
    public void updatePurchase(Purchase purchase, TokenModel tokenModel) throws Exception {
        purchase.preUpdate(tokenModel);
        purchaseMapper.updateByPrimaryKey(purchase);
    }

    @Override
    public void insert(Purchase purchase, TokenModel tokenModel) throws Exception {

        //add-ws-根据当前年月日从001开始增加采购编号
        List<Purchase> purchaselist = purchaseMapper.selectAll();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String year = sf1.format(date);
        int number = 0;
        String Numbers = "";
        String no = "";
        if(purchaselist.size()>0){
            for(Purchase purcha :purchaselist){
                if(purcha.getPurnumbers()!="" && purcha.getPurnumbers()!=null){
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(purcha.getPurnumbers(), 2,8));
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
        Numbers = "CG"+year+ no;
        //add-ws-根据当前年月日从001开始增加采购编号
        purchase.preInsert(tokenModel);
        purchase.setPurnumbers(Numbers);
        purchase.setPurchase_id(UUID.randomUUID().toString()) ;
        purchaseMapper.insert(purchase);
    }
}
