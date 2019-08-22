package com.nt.service_ServiceProvider.Impl;

import com.nt.dao_ServiceProvider.OnlineApplication;
import com.nt.dao_ServiceProvider.PMInformationDelivery;
import com.nt.dao_ServiceProvider.ServiceProvider;
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
            return ApiResult.fail();
        } else {
            return ApiResult.success();
        }
    }

    @Override
    public List<OnlineApplication> select(OnlineApplication onlineApplication, TokenModel tokenMode) {
        List<OnlineApplication> onlineApplications = new ArrayList<OnlineApplication>();
        if (onlineApplication.get_id() == null || "".equals(onlineApplication.get_id())) {
            // select all
            onlineApplications = mongoTemplate.findAll(OnlineApplication.class);
            for (OnlineApplication item : onlineApplications) {
                // 查询信息发布详情
                Query query = new Query(Criteria.where("_id").is(item.getPmInformationDelivery_id()));
                PMInformationDelivery pmInformationDelivery = mongoTemplate.findOne(query, PMInformationDelivery.class);
                // 查询服务商详情
                query = new Query(Criteria.where("_id").is(item.getServiceProvider_id()));
                ServiceProvider serviceProvider = mongoTemplate.findOne(query, ServiceProvider.class);

                item.setPmInformationDelivery(pmInformationDelivery);
                item.setServiceProvider(serviceProvider);
            }
        } else {
            // select by id
            Query query = new Query(Criteria.where("_id").is(onlineApplication.get_id()));
            onlineApplications = mongoTemplate.find(query, OnlineApplication.class);
        }
        return onlineApplications;
    }

    @Override
    public List<OnlineApplication> selectByCreateBy(TokenModel tokenMode) {
        List<OnlineApplication> onlineApplications = new ArrayList<OnlineApplication>();
        Query query = new Query(Criteria.where("createby").is(tokenMode.getUserId()));
        onlineApplications = mongoTemplate.find(query, OnlineApplication.class);

        for (OnlineApplication item : onlineApplications) {
            // 查询信息发布详情
            query = new Query(Criteria.where("_id").is(item.getPmInformationDelivery_id()));
            PMInformationDelivery pmInformationDelivery = mongoTemplate.findOne(query, PMInformationDelivery.class);
            // 查询服务商详情
            query = new Query(Criteria.where("_id").is(item.getServiceProvider_id()));
            ServiceProvider serviceProvider = mongoTemplate.findOne(query, ServiceProvider.class);

            item.setPmInformationDelivery(pmInformationDelivery);
            item.setServiceProvider(serviceProvider);
        }


        return onlineApplications;
    }
}
