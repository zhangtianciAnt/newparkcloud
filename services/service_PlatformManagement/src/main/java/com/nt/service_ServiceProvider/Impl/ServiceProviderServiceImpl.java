package com.nt.service_ServiceProvider.Impl;

import com.mongodb.client.result.UpdateResult;
import com.nt.dao_ServiceProvider.ServiceProvider;
import com.nt.service_ServiceProvider.ServiceProviderService;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApiResult insert(ServiceProvider serviceProvider, TokenModel tokenModel) {
        serviceProvider.preInsert(tokenModel);
        mongoTemplate.insert(serviceProvider);
        if (serviceProvider.get_id() == null || "".equals(serviceProvider.get_id())) {
            return ApiResult.fail();
        } else {
            return ApiResult.success();
        }
    }

    @Override
    public ApiResult audit(ServiceProvider serviceProvider, TokenModel tokenModel) {
        serviceProvider.preUpdate(tokenModel);
        Query query = new Query(Criteria.where("_id").is(serviceProvider.get_id()));
        Update update = new Update();
        update.set("auditStatus", serviceProvider.getAuditStatus());
        update.set("modifyby", serviceProvider.getModifyby());
        update.set("modifyon", serviceProvider.getModifyon());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ServiceProvider.class);
        if (updateResult.getModifiedCount() > 0) {
            return ApiResult.success();
        } else {
            return ApiResult.fail();
        }
    }

    @Override
    public List<ServiceProvider> select(ServiceProvider serviceProvider, TokenModel tokenModel) {
        List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
        if (serviceProvider.get_id() == null || "".equals(serviceProvider.get_id())) {
            // select all
            serviceProviders = mongoTemplate.findAll(ServiceProvider.class);
        } else {
            // select by id
            Query query = new Query(Criteria.where("_id").is(serviceProvider.get_id()));
            serviceProviders = mongoTemplate.find(query, ServiceProvider.class);
        }
        return serviceProviders;
    }
}
