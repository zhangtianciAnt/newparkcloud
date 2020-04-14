package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import com.nt.dao_Pfans.PFANS6000.Vo.PricesetVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PricesetService {

    //获取单价设定列表
    List<PricesetVo> gettlist(PricesetGroup pricesetGroup) throws Exception;

    List<Priceset> getPricesetList(Priceset priceset) throws Exception;

    //单价设定生成
    public Priceset pricesetgenerate(String priceset_id) throws Exception;

    //单价设定修改
    public void updatepriceset(PricesetVo pricesetVo, TokenModel tokenModel) throws Exception;

}
