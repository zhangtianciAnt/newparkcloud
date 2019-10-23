package com.nt.service_pfans.PFANS8000.Impl;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.service_pfans.PFANS8000.InformationDeliveryService;
import com.nt.service_pfans.PFANS8000.mapper.InformationDeliveryMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class InformationDeliveryServiceImpl implements InformationDeliveryService {

    @Autowired
    private InformationDeliveryMapper informationDeliveryMapper;

    @Override
    public List<InformationDelivery> getInformation(TokenModel tokenModel) throws Exception {
        InformationDelivery information = new InformationDelivery();
        information.setOwners(tokenModel.getOwnerList());
        List<InformationDelivery> informationDeliveries = informationDeliveryMapper.select(information);
        informationDeliveries = informationDeliveries.stream().sorted((a, b) -> b.getCreateon().compareTo(a.getCreateon())).collect(Collectors.toList());
        return informationDeliveries;
    }

    @Override
    public void insertInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws Exception {

        informationDelivery.preInsert(tokenModel);
        informationDelivery.setInformationid(UUID.randomUUID().toString());
        informationDeliveryMapper.insertSelective(informationDelivery);

    }

    @Override
    public void updateInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws Exception {
        informationDelivery.preUpdate(tokenModel);
        informationDeliveryMapper.updateByPrimaryKeySelective(informationDelivery);
    }

    @Override
    public List<InformationDelivery> getOneInformation(String information, TokenModel tokenModel) throws Exception {
        InformationDelivery informationDelivery = informationDeliveryMapper.selectByPrimaryKey(information);
        List<InformationDelivery> informationDeliverys = new ArrayList<InformationDelivery>();
        informationDeliverys.add(informationDelivery);
        return informationDeliverys;
    }

}
