package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.BusinessCard;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface BusinessCardService {
    //查看
    List<BusinessCard> getBusinessCard(BusinessCard businesscard) throws Exception;

    List<BusinessCard> getBusinessCardlist(BusinessCard businesscard) throws Exception;

    public BusinessCard One(String businesscardid) throws Exception;
    //增加
    public void insertBusinessCard(BusinessCard businesscard, TokenModel tokenModel)throws Exception;
    //修改
    public void updateBusinessCard(BusinessCard businesscard, TokenModel tokenModel)throws Exception;
}
