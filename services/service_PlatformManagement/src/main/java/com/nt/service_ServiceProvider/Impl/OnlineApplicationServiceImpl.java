package com.nt.service_ServiceProvider.Impl;

import com.nt.dao_ServiceProvider.OnlineApplication;
import com.nt.service_ServiceProvider.OnlineApplicationService;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class OnlineApplicationServiceImpl implements OnlineApplicationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApiResult insert(OnlineApplication onlineApplication, TokenModel tokenModel) {
        onlineApplication.preInsert(tokenModel);
        mongoTemplate.insert(onlineApplication);
        if (onlineApplication.get_id() == null || "".equals(onlineApplication.get_id())) {
            return ApiResult.fail(MsgConstants.FAIL);
        } else {
            return ApiResult.success(MsgConstants.SUCCESS);
        }
    }
}
