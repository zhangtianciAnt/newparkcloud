package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.ExpatriatesinforDetail;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import com.nt.dao_Pfans.PFANS6000.Vo.PricesetVo;
import com.nt.service_pfans.PFANS6000.PricesetService;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforDetailMapper;
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
    @Autowired
    private ExpatriatesinforDetailMapper expatriatesinforDetailMapper;

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
                PricesetVo a = new PricesetVo(new PricesetGroup(),new ArrayList<Priceset>());
                Priceset conditon = new Priceset();
                conditon.setPricesetgroup_id(item.getPricesetgroup_id());
                a.setMain(item);

                List<Priceset> list = pricesetMapper.select(conditon);

                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                expatriatesinfor.setWhetherentry("BP006001");
                List<Expatriatesinfor> expatriatesinforlist = expatriatesinforMapper.select(expatriatesinfor);
                for(Expatriatesinfor expatriatesinforItem:expatriatesinforlist){

                    List<Priceset> pl = list.stream().filter(pli -> (expatriatesinforItem.getExpatriatesinfor_id().equals(pli.getUser_id())  )).collect(Collectors.toList());

                    if(pl.size() > 0){
                        for(Priceset ip:pl){
                            Priceset priceset = new Priceset();
                            BeanUtil.copyProperties(ip,priceset);
                            a.getDetail().add(priceset);
                        }
                        List<Priceset> p = pl.stream().filter(pli -> (expatriatesinforItem.getGroup_id().equals(pli.getGroupid()))).collect(Collectors.toList());
                        if(p.size() ==0)
                        {
                            //查询履历
                            List<ExpatriatesinforDetail> expatriatesinforDetails =new ArrayList<>();
                            //画面传过来的月份
                            expatriatesinforDetails = expatriatesinforDetailMapper.selectByDate(pricesetGroup.getPd_date(),expatriatesinforItem.getExpatriatesinfor_id());
                            if(expatriatesinforDetails.size()>0)
                            {
                                for(ExpatriatesinforDetail eDetail:expatriatesinforDetails)
                                {
                                    Priceset priceset = new Priceset();
                                    priceset.setGroupid(eDetail.getGroup_id());
                                    priceset.setUser_id(eDetail.getExpatriatesinfor_id());
                                    priceset.setUsername(expatriatesinforItem.getExpname());
                                    priceset.setGraduation(expatriatesinforItem.getGraduation_year());
                                    priceset.setCompany(expatriatesinforItem.getSuppliername());
                                    a.getDetail().add(priceset);
                                }
                            }
                        }
                    }
                    else{
                        Priceset priceset = new Priceset();
                        priceset.setGroupid(expatriatesinforItem.getGroup_id());
                        priceset.setUser_id(expatriatesinforItem.getExpatriatesinfor_id());
                        priceset.setUsername(expatriatesinforItem.getExpname());
                        priceset.setGraduation(expatriatesinforItem.getGraduation_year());
                        priceset.setCompany(expatriatesinforItem.getSuppliername());
                        a.getDetail().add(priceset);
                    }
                }
                rst.add(a);
            }
        }
        else{
            pricesetGroup.setPd_date(DateUtil.format(DateUtil.offset(DateUtil.parse(pricesetGroup.getPd_date() + "-01"), DateField.MONTH,-1),"yyyy-MM"));
            ms = pricesetGroupMapper.select(pricesetGroup);
            if(ms.size() > 0){
                for(PricesetGroup item:ms){
                    Priceset conditon = new Priceset();
                    conditon.setPricesetgroup_id(item.getPricesetgroup_id());
                    List<Priceset> list = pricesetMapper.select(conditon);

                    PricesetVo a = new PricesetVo(new PricesetGroup(),new ArrayList<Priceset>());

                    a.getMain().setPd_date(DateUtil.format(DateUtil.offset(DateUtil.parse(pricesetGroup.getPd_date() + "-01"), DateField.MONTH,1),"yyyy-MM"));

                    Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                    expatriatesinfor.setWhetherentry("BP006001");
                    List<Expatriatesinfor> expatriatesinforlist = expatriatesinforMapper.select(expatriatesinfor);
                    //画面传过来的月份
                    String pddate = DateUtil.format(DateUtil.offset(DateUtil.parse(pricesetGroup.getPd_date() + "-01"), DateField.MONTH,1),"yyyy-MM");
                    for(Expatriatesinfor expatriatesinforItem:expatriatesinforlist){
                        //查询履历
                        List<ExpatriatesinforDetail> expatriatesinforDetails =new ArrayList<>();
                        expatriatesinforDetails = expatriatesinforDetailMapper.selectByDate(pddate,expatriatesinfor.getExpatriatesinfor_id());
                        if(expatriatesinforDetails.size()>0)
                        {
                            for(ExpatriatesinforDetail eDetail:expatriatesinforDetails)
                            {
                                Priceset priceset = new Priceset();
                                priceset.setGroupid(eDetail.getGroup_id());
                                priceset.setUser_id(eDetail.getExpatriatesinfor_id());
                                priceset.setUsername(expatriatesinforItem.getExpname());
                                priceset.setGraduation(expatriatesinforItem.getGraduation_year());
                                priceset.setCompany(expatriatesinforItem.getSuppliername());
                                a.getDetail().add(priceset);
                            }
                        }
                        else
                        {
                            Priceset priceset = new Priceset();
                            List<Priceset> pl = list.stream().filter(pli -> expatriatesinforItem.getExpatriatesinfor_id().equals(pli.getUser_id())).collect(Collectors.toList());

                            priceset.setUser_id(expatriatesinforItem.getExpatriatesinfor_id());
                            priceset.setUsername(expatriatesinforItem.getExpname());
                            priceset.setGroupid(expatriatesinforItem.getGroup_id());
                            priceset.setGraduation(expatriatesinforItem.getGraduation_year());
                            priceset.setCompany(expatriatesinforItem.getSuppliername());
                            if(pl.size() > 0){
                                BeanUtil.copyProperties(pl.get(0),priceset);
                            }
                            priceset.setPriceset_id("");
                            priceset.setPricesetgroup_id("");
                            a.getDetail().add(priceset);
                        }
                    }
                    rst.add(a);
                }
            }
            else{
                PricesetVo a = new PricesetVo(new PricesetGroup(),new ArrayList<Priceset>());

                a.getMain().setPd_date(DateUtil.format(DateUtil.offset(DateUtil.parse(pricesetGroup.getPd_date() + "-01"), DateField.MONTH,1),"yyyy-MM"));

                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                expatriatesinfor.setWhetherentry("BP006001");
                List<Expatriatesinfor> expatriatesinforlist = expatriatesinforMapper.select(expatriatesinfor);
                for(Expatriatesinfor expatriatesinforItem:expatriatesinforlist){
                    Priceset priceset = new Priceset();
                    priceset.setUser_id(expatriatesinforItem.getExpatriatesinfor_id());
                    priceset.setGroupid(expatriatesinforItem.getGroup_id());
                    priceset.setUsername(expatriatesinforItem.getExpname());
                    priceset.setGraduation(expatriatesinforItem.getGraduation_year());
                    priceset.setCompany(expatriatesinforItem.getSuppliername());
                    a.getDetail().add(priceset);
                }
                rst.add(a);
            }
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
     * @param pricesetVo
     * @param tokenModel
     * @throws Exception
     */
    @Override
    public void updatepriceset(PricesetVo pricesetVo, TokenModel tokenModel) throws Exception {

        if(StrUtil.isNotBlank(pricesetVo.getMain().getPricesetgroup_id())){
            pricesetVo.getMain().preUpdate(tokenModel);
            pricesetGroupMapper.updateByPrimaryKeySelective(pricesetVo.getMain());
        }else{
            pricesetVo.getMain().preInsert(tokenModel);
            pricesetVo.getMain().setPricesetgroup_id(UUID.randomUUID().toString());
            pricesetGroupMapper.insert(pricesetVo.getMain());
        }

        for(Priceset priceset:pricesetVo.getDetail()){
            if(StrUtil.isNotBlank(priceset.getPriceset_id())){
                priceset.preUpdate(tokenModel);
                pricesetMapper.updateByPrimaryKeySelective(priceset);
            }else{
                priceset.preInsert(tokenModel);
                priceset.setPriceset_id(UUID.randomUUID().toString());
                priceset.setPricesetgroup_id(pricesetVo.getMain().getPricesetgroup_id());
                pricesetMapper.insert(priceset);
            }

        }
    }
}
