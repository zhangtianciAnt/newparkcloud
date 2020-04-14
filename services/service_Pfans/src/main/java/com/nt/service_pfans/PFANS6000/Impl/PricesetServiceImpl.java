package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import com.nt.dao_Pfans.PFANS6000.Vo.PricesetVo;
import com.nt.service_pfans.PFANS6000.PricesetService;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetGroupMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PricesetServiceImpl implements PricesetService {

    @Autowired
    private PricesetMapper pricesetMapper;

    @Autowired
    private PricesetGroupMapper pricesetGroupMapper;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    /**
     * 获取单价设定列表
     *
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public List<PricesetVo> gettlist(PricesetGroup pricesetGroup) throws Exception {
        List<PricesetVo> rst = new ArrayList<PricesetVo>();
        List<PricesetGroup> ms = pricesetGroupMapper.select(pricesetGroup);

        if(ms.size() > 0){
            for(PricesetGroup item:ms){
                PricesetVo a = new PricesetVo();
                Priceset conditon = new Priceset();
                conditon.setPricesetgroup_id(item.getPricesetgroup_id());
                a.setMain(item);
                a.setDetail(pricesetMapper.select(conditon));

                rst.add(a);
            }
        }else{
            PricesetVo a = new PricesetVo(new PricesetGroup(),new ArrayList<Priceset>());

            a.getMain().setPd_date(pricesetGroup.getPd_date());

            Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
            expatriatesinfor.setWhetherentry("BP006001");
            List<Expatriatesinfor> expatriatesinforlist = expatriatesinforMapper.select(expatriatesinfor);
            for(Expatriatesinfor expatriatesinforItem:expatriatesinforlist){
                Priceset priceset = new Priceset();
                priceset.setUser_id(expatriatesinforItem.getExpatriatesinfor_id());
                priceset.setUsername(expatriatesinforItem.getExpname());
                priceset.setGraduation(expatriatesinforItem.getGraduation_year());
                priceset.setCompany(expatriatesinforItem.getSuppliername());
                a.getDetail().add(priceset);
            }
            rst.add(a);
        }

        return rst;
    }

    @Override
    public List<Priceset> getPricesetList(Priceset priceset) throws Exception {
        List<Priceset> pricesetList = pricesetMapper.select(priceset);
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.selectAll();
        pricesetList.forEach(item -> {
            item.setUsername(expatriatesinforList.stream().filter(subItem -> subItem.getExpatriatesinfor_id().equals(item.getUser_id())).collect(Collectors.toList()).get(0).getExpname());
        });
        return pricesetList;
    }


    @Override
    public Priceset pricesetgenerate(String pricesetid) throws Exception {
        return null;
    }

    /**
     * 单价设定修改
     *
     * @param priceset
     * @param tokenModel
     * @throws Exception
     */
    @Override
    public void updatepriceset(List<Priceset> priceset, TokenModel tokenModel) throws Exception {
        for (int i = 0; i < priceset.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Priceset price = priceset.get(i);

            Priceset prices = new Priceset();
            //用主键查询
            prices.setPriceset_id(priceset.get(i).getPriceset_id());
            List<Priceset> pricesetList = pricesetMapper.select(prices);

            String AssesstimeUpYear_s = (priceset.get(i).getAssesstime().substring(0, 4));
            String AssesstimeUpMonth_s = (priceset.get(i).getAssesstime().substring(5, 7));
            String AssesstimeUp_s = AssesstimeUpYear_s + AssesstimeUpMonth_s;

            for (int j = 0; j < pricesetList.size(); j++) {
                if (pricesetList.get(j).getStatus().equals("0")) {
                    String AssesstimeStYear_s = (pricesetList.get(j).getAssesstime().substring(0, 4));
                    String AssesstimeStMonth_s = (pricesetList.get(j).getAssesstime().substring(5, 7));
                    String AssesstimeSt_s = AssesstimeStYear_s + AssesstimeStMonth_s;
                    //如果月份相同，直接修改
                    if (AssesstimeUp_s.equals(AssesstimeSt_s)) {
                        price.preUpdate(tokenModel);
                        pricesetMapper.updateByPrimaryKey(price);
                    } else {
                        //如果月份不同， ，status为0
                        Priceset pricesetOld = pricesetList.get(j);
                        pricesetOld.setStatus("1");
                        pricesetOld.preUpdate(tokenModel);
                        pricesetMapper.updateByPrimaryKeySelective(pricesetOld);
                        price.preInsert(tokenModel);
                        price.setPriceset_id(UUID.randomUUID().toString());
                        price.setStatus("0");
                        pricesetMapper.insert(price);
                    }
                }
            }
        }
    }
}
