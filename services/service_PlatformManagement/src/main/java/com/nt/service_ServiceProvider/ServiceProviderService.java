package com.nt.service_ServiceProvider;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_ServiceProvider.ServiceProvider;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ServiceProviderService {

    /**
     * 查询联系人
     */
    List<CustomerInfo> select(CustomerInfo customerInfo, TokenModel tokenModel);

}
