package com.nt.service_ServiceProvider;

import com.nt.dao_ServiceProvider.PMInformationDelivery;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PMInformationDeliveryService {
    List<PMInformationDelivery> select(PMInformationDelivery pmInformationDelivery);

    ApiResult insert(PMInformationDelivery pmInformationDelivery, TokenModel tokenModel);

    ApiResult update(PMInformationDelivery pmInformationDelivery, TokenModel tokenModel);
}
