package com.nt.service_pfans.PFANS8000.Impl;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.service_pfans.PFANS8000.InformationDeliveryService;
import com.nt.service_pfans.PFANS8000.mapper.InformationDeliveryMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class InformationDeliveryServiceImpl implements InformationDeliveryService {

    @Autowired
    private InformationDeliveryMapper informationDeliveryMapper;

    @Override
    public List<InformationDelivery> getInformation() {
         if(informationDeliveryMapper.getInformation().isEmpty()){
             return null;
         }
        return informationDeliveryMapper.getInformation();
    }

    @Override
    public void insertInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws Exception {
        if(!informationDelivery.equals(null)){
            informationDelivery.preInsert(tokenModel);
            informationDelivery.setInformationid(UUID.randomUUID().toString());
            informationDeliveryMapper.insertSelective(informationDelivery);
        }
    }
}
