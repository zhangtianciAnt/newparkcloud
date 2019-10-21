package com.nt.service_pfans.PFANS8000.Impl;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.service_pfans.PFANS8000.InformationDeliveryService;
import com.nt.service_pfans.PFANS8000.mapper.InformationDeliveryMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class InformationDeliveryServiceImpl implements InformationDeliveryService {

    @Autowired
    private InformationDeliveryMapper informationDeliveryMapper;

    @Override
    public List<InformationDelivery> getInformation(TokenModel tokenModel) {
        List<String> owners = tokenModel.getOwnerList();
        List<InformationDelivery> informationDeliveries = informationDeliveryMapper.getInformation(owners);
         if(informationDeliveries.isEmpty()){
             return null;
         }
        return informationDeliveries;
    }

    @Override
    public void insertInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(informationDelivery)){
            informationDelivery.preInsert(tokenModel);
            informationDelivery.setInformationid(UUID.randomUUID().toString());
            informationDeliveryMapper.insertSelective(informationDelivery);
        }
    }

    @Override
    public void updateInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws Exception{
        if(!StringUtils.isEmpty(informationDelivery)){
            informationDelivery.preUpdate(tokenModel);
            informationDeliveryMapper.updateByPrimaryKeySelective(informationDelivery);
        }
    }

    @Override
    public List<InformationDelivery> getOneInformation(String information, TokenModel tokenModel) throws Exception {
        InformationDelivery informationDelivery  = informationDeliveryMapper.selectByPrimaryKey(information);
        List<InformationDelivery> informationDeliverys = new ArrayList<InformationDelivery>();
        informationDeliverys.add(informationDelivery);
        if(informationDeliverys.isEmpty()){
            return null;
        }
        return informationDeliverys;
    }

}
