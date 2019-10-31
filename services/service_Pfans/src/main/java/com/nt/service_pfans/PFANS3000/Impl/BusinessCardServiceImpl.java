package com.nt.service_pfans.PFANS3000.Impl;
import com.nt.dao_Pfans.PFANS3000.BusinessCard;
import com.nt.service_pfans.PFANS3000.BusinessCardService;
import com.nt.service_pfans.PFANS3000.mapper.BusinessCardMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class BusinessCardServiceImpl implements BusinessCardService {

    @Autowired
    private BusinessCardMapper businesscardMapper;

    @Override
    public List<BusinessCard> getBusinessCard(BusinessCard businesscard) throws Exception {
        return businesscardMapper.select(businesscard);
    }

    @Override
    public List<BusinessCard> getBusinessCardlist(BusinessCard businesscard) throws Exception {
        return businesscardMapper.select(businesscard);
    }

    @Override
    public BusinessCard One(String businesscardid) throws Exception {

        return businesscardMapper.selectByPrimaryKey(businesscardid);
    }

    @Override
    public void insertBusinessCard(BusinessCard businesscard, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(businesscard)){
            businesscard.preInsert(tokenModel);
            businesscard.setBusinesscardid(UUID.randomUUID().toString());
            businesscardMapper.insert(businesscard);
        }
    }


    @Override
    public void updateBusinessCard(BusinessCard businesscard, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(businesscard)){
            businesscard.preUpdate(tokenModel);
            businesscardMapper.updateByPrimaryKey(businesscard);
        }
    }
}
