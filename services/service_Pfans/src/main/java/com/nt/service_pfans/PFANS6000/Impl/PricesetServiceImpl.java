package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.service_pfans.PFANS6000.PricesetService;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PricesetServiceImpl implements PricesetService {

    @Autowired
    private PricesetMapper pricesetMapper;

    /**
     * 获取单价设定列表
     * @param priceset
     * @return
     * @throws Exception
     */
    @Override
    public List<Priceset> getpriceset(Priceset priceset) throws Exception {
        return pricesetMapper.select(priceset);
    }

    @Override
    public Priceset pricesetgenerate(String pricesetid) throws Exception {
        return null;
    }

    /**
     * 单价设定修改
     * @param priceset
     * @param tokenModel
     * @throws Exception
     */
    @Override
    public void updatepriceset(List<Priceset> priceset, TokenModel tokenModel) throws Exception {
        for(int i = 0; i < priceset.size(); i++){
            Priceset price = priceset.get(i);
            price.preUpdate(tokenModel);
            pricesetMapper.updateByPrimaryKeySelective(price);
        }
    }
}
