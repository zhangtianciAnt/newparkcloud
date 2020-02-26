package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.service_pfans.PFANS1000.PersonnelplanService;
import com.nt.service_pfans.PFANS1000.mapper.PersonnelplanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnelplanServiceImpl implements PersonnelplanService {

    @Autowired
    private PersonnelplanMapper personnelplanMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<CustomerInfo> SelectCustomer(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("userinfo.groupid").is(id);
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        return customerInfos;
    }
}
