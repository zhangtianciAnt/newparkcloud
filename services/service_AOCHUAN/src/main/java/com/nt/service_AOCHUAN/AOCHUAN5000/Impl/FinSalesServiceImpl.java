package com.nt.service_AOCHUAN.AOCHUAN5000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinSalesMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            List<FinSales> resultLst =  finSalesMapper.existCheck(finSales.getSales_id(),"0");
            if (resultLst.isEmpty()){
                return false;
            }
        return true;
    }

    //唯一性Check
    @Override
    public Boolean uniqueCheck(FinSales finSales) throws Exception {
        List<FinSales> resultLst = finSalesMapper.uniqueCheck(finSales.getSales_id(), finSales.getContractnumber());

        if (resultLst.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public List<FinSales> getHK() throws Exception {
        return finSalesMapper.getHK();
    }
}
