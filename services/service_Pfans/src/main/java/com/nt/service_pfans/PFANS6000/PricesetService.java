package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PricesetService {

    //获取单价设定列表
    List<Priceset> getpriceset(Priceset priceset) throws Exception;

    //单价设定生成
    public Priceset pricesetgenerate(String pricesetid) throws Exception;

    //单价设定修改
    public void updatepriceset(List<Priceset> priceset, TokenModel tokenModel) throws Exception;

}
