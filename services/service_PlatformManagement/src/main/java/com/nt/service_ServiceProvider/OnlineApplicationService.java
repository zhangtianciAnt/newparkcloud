package com.nt.service_ServiceProvider;

import com.nt.dao_ServiceProvider.OnlineApplication;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

public interface OnlineApplicationService {
    ApiResult insert(OnlineApplication onlineApplication, TokenModel tokenModel);
}
