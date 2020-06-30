package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.Totalmoney;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinSalesMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.util.List;

@Service
public class FinSalesServiceImpl implements FinSalesService {

    @Autowired
    private FinSalesMapper finSalesMapper;

    //获取财务-销售数据
    @Override
    public List<FinSales> getFinSalesList(FinSales finSales) throws Exception {
        return finSalesMapper.select(finSales);
    }

    //更新
    @Override
    public void update(FinSales finSales, TokenModel tokenModel) throws Exception {

        finSales.preUpdate(tokenModel);
        finSalesMapper.updateByPrimaryKeySelective(finSales);
    }

    //存在Check
    @Override
    public Boolean existCheck(FinSales finSales) throws Exception {

        List<FinSales> resultLst = finSalesMapper.existCheck(finSales.getSales_id(), "0");
        if (resultLst.isEmpty()) {
            return false;
        }
        return true;
    }

    //唯一性Check
    @Override
    public Boolean uniqueCheck(FinSales finSales) throws Exception {
        List<FinSales> resultLst = finSalesMapper.uniqueCheck(finSales.getSales_id(), finSales.getContractnumber());

        if (resultLst.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public List<Totalmoney> getHK() throws Exception {
        return finSalesMapper.getHK();
    }

    //更新走货
    @Override
    public void updateTransportGood(FinSales finSales, TokenModel tokenModel) throws Exception {

        finSales.preUpdate(tokenModel);
        finSalesMapper.updateTransportGood(finSales.getArrivaltime(), finSales.getModifyby(), finSales.getTransportgood_id());
    }

    //弹窗更新已回款
    @Override
    public void updateall(List<FinSales> finSales, TokenModel tokenModel) throws Exception {
        for (int i = 0; i < finSales.size(); i++) {
            finSales.get(i).preUpdate(tokenModel);
            finSalesMapper.toUpDateReFunded(finSales.get(i).getEx_rate(), finSales.get(i).getAmountreceived(), finSales.get(i).getInvoiceamount(), finSales.get(i).getBillingtime(), finSales.get(i).getCommissionamounta(), finSales.get(i).getContractnumber());
        }
    }
    //弹窗更新未回款
    @Override
    public void updateallw(List<FinPurchase> finPurchase, TokenModel tokenModel) throws Exception {
        for (int i = 0; i < finPurchase.size(); i++) {
            finPurchase.get(i).preUpdate(tokenModel);
            finSalesMapper.toUpDateReFundedw(finPurchase.get(i).getEx_rate(), finPurchase.get(i).getAmountreceived(), finPurchase.get(i).getInvoiceamount(), finPurchase.get(i).getBillingtime(), finPurchase.get(i).getCommissionamounta(), finPurchase.get(i).getContractnumber());
        }
    }
}
