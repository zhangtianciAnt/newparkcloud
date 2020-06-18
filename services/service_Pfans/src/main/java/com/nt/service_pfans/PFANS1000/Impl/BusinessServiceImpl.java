package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private TravelContentMapper travelcontentMapper;

    @Override
    public List<Business> get(Business business) throws Exception {
        return businessMapper.select(business);
    }

    @Override
    public List<Business> getBuse() throws Exception {
        return businessMapper.getBuse();
    }

    @Override
    public BusinessVo selectById(String businessid) throws Exception {
        BusinessVo busVo = new BusinessVo();
        TravelContent travelcontent = new TravelContent();
        travelcontent.setBusinessid(businessid);
        List<TravelContent> travelcontentlist = travelcontentMapper.select(travelcontent);
        travelcontentlist = travelcontentlist.stream().sorted(Comparator.comparing(TravelContent::getRowindex)).collect(Collectors.toList());
        Business Bus = businessMapper.selectByPrimaryKey(businessid);
        busVo.setBusiness(Bus);
        busVo.setTravelcontent(travelcontentlist);
        return busVo;
    }

    @Override
    public void updateBusinessVo(BusinessVo businessVo, TokenModel tokenModel) throws Exception {
        Business business = new Business();
        BeanUtils.copyProperties(businessVo.getBusiness(), business);
        business.preUpdate(tokenModel);
        businessMapper.updateByPrimaryKey(business);
        String businessid = business.getBusiness_id();
        TravelContent travel = new TravelContent();
        travel.setBusinessid(businessid);
        travelcontentMapper.delete(travel);
        List<TravelContent> travelcontentlist = businessVo.getTravelcontent();
        if (travelcontentlist != null) {
            int rowindex = 0;
            for (TravelContent travelcontent : travelcontentlist) {
                rowindex = rowindex + 1;
                travelcontent.preInsert(tokenModel);
                travelcontent.setTravelcontent_id(UUID.randomUUID().toString());
                travelcontent.setBusinessid(businessid);
                travelcontent.setRowindex(rowindex);
                travelcontentMapper.insertSelective(travelcontent);
            }
        }
    }

    @Override
    public void insertBusinessVo(BusinessVo businessVo, TokenModel tokenModel) throws Exception {
        String businessid = UUID.randomUUID().toString();
        Business business = new Business();
        List<Business> businessList = businessMapper.selectAll();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String new_date = sdf1.format(date);
        int number = 0;
        String number_str = "";
        String no_str = "";
        if(businessList.size() > 0){
            for(Business busin : businessList){
                if(busin.getBusiness_number() != "" && busin.getBusiness_number() != null){
                    String checkNumber = StringUtils.uncapitalize(StringUtils.substring(busin.getBusiness_number(),0,8));
                    if(new_date.equals(checkNumber)){
                        number = number + 1;
                    }
                }
            }
            if(number <= 8){
                no_str = "0" + (number +1);
            }else{
                no_str = String.valueOf(number + 1);
            }
        }else{
            no_str = "01";
        }
        number_str = new_date + no_str;
        BeanUtils.copyProperties(businessVo.getBusiness(), business);
        business.preInsert(tokenModel);
        business.setBusiness_id(businessid);
        business.setBusiness_number(number_str);
        businessMapper.insertSelective(business);
        List<TravelContent> travelcontentlist = businessVo.getTravelcontent();
        if (travelcontentlist != null) {
            int rowindex = 0;
            for (TravelContent travelcontent : travelcontentlist) {
                rowindex = rowindex + 1;
                travelcontent.preInsert(tokenModel);
                travelcontent.setTravelcontent_id(UUID.randomUUID().toString());
                travelcontent.setBusinessid(businessid);
                travelcontent.setRowindex(rowindex);
                travelcontentMapper.insertSelective(travelcontent);
            }
        }
    }

}
