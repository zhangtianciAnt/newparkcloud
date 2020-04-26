package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinPurchaseSerivce;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinPurchaseMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinPurchaseServiceImpl implements FinPurchaseSerivce {

    @Autowired
    FinPurchaseMapper finPurchaseMapper;

    //获取财务-采购数据
    @Override
    public List<FinPurchase> getFinPurchaseList(FinPurchase finPurchase) throws Exception {
        return finPurchaseMapper.select(finPurchase);
    }

    @Override
    public FinPurchase getForm(String id) throws Exception {
        return finPurchaseMapper.selectByPrimaryKey(id);
    }

    //更新
    @Override
    public void update(FinPurchase finPurchase, TokenModel tokenModel) throws Exception {

        finPurchase.preUpdate(tokenModel);
        finPurchaseMapper.updateByPrimaryKeySelective(finPurchase);
    }

    //存在Check
    @Override
    public Boolean existCheck(FinPurchase finPurchase) throws Exception {

        List<FinPurchase> resultLst =  finPurchaseMapper.existCheck(finPurchase.getPurchase_id(),"0");
        if (resultLst.isEmpty()){
            return false;
        }
        return true;
    }

    //唯一性Check
    @Override
    public Boolean uniqueCheck(FinPurchase finPurchase) throws Exception {
        List<FinPurchase> resultLst = finPurchaseMapper.uniqueCheck(finPurchase.getPurchase_id(), finPurchase.getContractnumber());

        if (resultLst.isEmpty()){
            return false;
        }
        return true;
    }
}
