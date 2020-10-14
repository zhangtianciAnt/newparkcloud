package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TransportGoodService {
    List<TransportGood> get(TransportGood transportGood) throws Exception;

    TransportGood getOne(String id) throws Exception;

    List<TransportGood> getForSupplier(String id) throws Exception;

    List<TransportGood> getForCustomer(String id) throws Exception;

    void update(TransportGood quotations, TokenModel tokenModel) throws Exception;

    void insert(TransportGood quotations, TokenModel tokenModel) throws Exception;

    void delete(String id) throws Exception;

//    void insertCW(TransportGood transportGood, TokenModel token);
//
//    void insertHK(TransportGood transportGood, TokenModel token) throws Exception;

    void paymentCG(List<FinSales> finSales, TokenModel token);

    void paymentXS(List<FinPurchase> finPurchases, TokenModel token);

    //    导出
    boolean setExport(HttpServletResponse response , List<TransportGood> exportVo) throws Exception;

    List<TransportGood> getTransportInfo(String id) throws Exception;
}
