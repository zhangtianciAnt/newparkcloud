package com.nt.service_pfans.PFANS8000;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface InformationDeliveryService {
    //获取
    List<InformationDelivery> getInformation(TokenModel tokenModel) throws Exception;
    //创建
    void insertInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws  Exception;
    //更新
    void updateInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws Exception;
    //获取一条
    List<InformationDelivery> getOneInformation(String information,TokenModel tokenModel) throws Exception;

    //获取
    List<InformationDelivery> getListType(InformationDelivery informationDelivery) throws Exception;


}
