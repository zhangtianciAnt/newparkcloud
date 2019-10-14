package com.nt.service_pfans.PFANS8000;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface InformationDeliveryService {
    //获取
    List<InformationDelivery> getInformation() throws Exception;
    //创建
    void insertInformation(InformationDelivery informationDelivery, TokenModel tokenModel) throws  Exception;
}
