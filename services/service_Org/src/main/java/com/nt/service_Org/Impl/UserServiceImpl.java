package com.nt.service_Org.Impl;


import com.nt.dao_Org.*;
import com.nt.service_Org.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Org org) throws Exception {
        mongoTemplate.save(org);
    }

    @Override
    public void save(OrgTree orgTree) throws Exception {
        mongoTemplate.save(orgTree);
    }

    @Override
    public void save(UserAccount userAccount) throws Exception {
        mongoTemplate.save(userAccount);
    }

    @Override
    public void save(Tenant tenant) throws Exception {
        mongoTemplate.save(tenant);
    }

    @Override
    public void save(CustomerInfo customerInfo) throws Exception {
        mongoTemplate.save(customerInfo);
    }

    @Override
    public Org get(Org org) throws Exception {

        return new Org();
    }
}
