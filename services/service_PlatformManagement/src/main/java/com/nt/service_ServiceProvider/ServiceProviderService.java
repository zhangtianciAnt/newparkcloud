package com.nt.service_ServiceProvider;

import com.nt.dao_ServiceProvider.ServiceProvider;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ServiceProviderService {
    /**
     * 创建联系人
     */
    ApiResult insert(ServiceProvider serviceProvider, TokenModel tokenModel);

    /**
     * 审核联系人
     */
    ApiResult audit(ServiceProvider serviceProvider, TokenModel tokenModel);

    /**
     * 查询联系人
     */
    List<ServiceProvider> select(ServiceProvider serviceProvider,TokenModel tokenModel );

}
