package com.nt.service_ServiceProvider.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.service_ServiceProvider.ServiceProviderService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<CustomerInfo> select(CustomerInfo customerInfo, TokenModel tokenModel) {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is("2"));
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        return customerInfos;
    }
}
