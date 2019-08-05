package com.nt.service_ServiceProvider.Impl;

import com.nt.dao_ServiceProvider.OnlineApplication;
import com.nt.service_ServiceProvider.OnlineApplicationService;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class OnlineApplicationServiceImpl implements OnlineApplicationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApiResult insert(@RequestBody OnlineApplication onlineApplication, TokenModel tokenModel) {
        onlineApplication.preInsert(tokenModel);
        mongoTemplate.insert(onlineApplication);
        if (onlineApplication.get_id() == null || "".equals(onlineApplication.get_id())) {
            return ApiResult.fail(MsgConstants.FAIL);
        } else {
            return ApiResult.success(MsgConstants.SUCCESS);
        }
    }

    @Override
    public List<OnlineApplication> select(OnlineApplication onlineApplication, TokenModel tokenMode) {
        List<OnlineApplication> onlineApplications = new ArrayList<OnlineApplication>();
        if (onlineApplication.get_id() == null || "".equals(onlineApplication.get_id())) {
            // select all
            onlineApplications = mongoTemplate.findAll(OnlineApplication.class);
        } else {
            // select by id
            Query query = new Query(Criteria.where("_id").is(onlineApplication.get_id()));
            onlineApplications = mongoTemplate.find(query, OnlineApplication.class);
        }
        return onlineApplications;
    }
}
