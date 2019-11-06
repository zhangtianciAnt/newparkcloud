package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Purchase;
import com.nt.service_pfans.PFANS2000.PurchaseService;
import com.nt.service_pfans.PFANS2000.mapper.PurchaseMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper  purchaseMapper;

    @Override
    public List<Purchase> getPurchase(Purchase purchase) {

        return purchaseMapper.select(purchase);
    }
    @Override
    public Purchase One(String purchase_id) throws Exception {

        return purchaseMapper.selectByPrimaryKey(purchase_id);
    }

    @Override
    public void updatePurchase(Purchase purchase, TokenModel tokenModel) throws Exception {
        purchaseMapper.updateByPrimaryKeySelective(purchase);
    }

    @Override
    public void insert(Purchase purchase, TokenModel tokenModel) throws Exception {

        purchase.preInsert(tokenModel);
        purchase.setPurchase_id(UUID.randomUUID().toString()) ;
        purchaseMapper.insert(purchase);
    }

}
