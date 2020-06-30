package com.nt.service_AOCHUAN.AOCHUAN5000;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.Totalmoney;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FinSalesService {

    //获取财务-销售数据
    List<FinSales> getFinSalesList(FinSales finSales) throws Exception;

    //更新
    void update(FinSales finSales, TokenModel tokenModel) throws Exception;

    //存在Check
    Boolean existCheck(FinSales finSales) throws Exception;

    //唯一性Check
    Boolean uniqueCheck(FinSales finSales) throws Exception;

    //获取财务-销售数据
    List<Totalmoney> getHK() throws Exception;
//    String[] getHK1() throws  Exception;

    //更新走货表
    void updateTransportGood(FinSales finSales, TokenModel tokenModel) throws Exception;

    //弹窗更新数据
    void updateall(List<FinSales> finSales, TokenModel tokenModel) throws Exception;

    //弹窗更新数据未回款
    void updateallw(List<FinPurchase> finPurchase, TokenModel tokenModel) throws Exception;
}
