package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.service_pfans.PFANS6000.PricesetService;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
//        for(int i = 0; i < priceset.size(); i++){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            Priceset price = priceset.get(i);
//
//            Priceset prices = new Priceset();
//            prices.setPricesetid(priceset.get(i).getPricesetid());
//            List<Priceset> pricesetList = pricesetMapper.select(prices);
//
//            String AssesstimeUpYear_s = (priceset.get(i).getAssesstime().substring(0, 4));
//            String AssesstimeUpMonth_s = (priceset.get(i).getAssesstime().substring(5, 7));
//            String AssesstimeUp_s = AssesstimeUpYear_s + AssesstimeUpMonth_s;
//            int priceYearMounthUp_i = Integer.parseInt(AssesstimeUp_s);
//
//            for(int j = 0; j < pricesetList.size(); j++){
//                if(pricesetList.get(j).getStatus().equals("0")){
//                    String AssesstimeStYear_s = (pricesetList.get(j).getAssesstime().substring(0, 4));
//                    String AssesstimeStMonth_s = (pricesetList.get(j).getAssesstime().substring(5, 7));
//                    String AssesstimeSt_s = AssesstimeUpYear_s + AssesstimeUpMonth_s;
//                    int priceYearMounthSt_i = Integer.parseInt(AssesstimeSt_s);
//
//                    if(priceYearMounthUp_i == priceYearMounthSt_i){
//                        pricesetMapper.updateByPrimaryKeySelective(price);
//                    }else{
//                        pricesetList.get(j).setStatus("1");
//                        price.setStatus("0");
//                        pricesetMapper.insert(price);
//                    }
//                }
//            }

//            price.preUpdate(tokenModel);
//            pricesetMapper.updateByPrimaryKeySelective(price);
//        }
    }
}
