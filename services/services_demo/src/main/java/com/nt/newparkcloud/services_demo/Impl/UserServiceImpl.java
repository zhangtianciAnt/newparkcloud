package com.nt.newparkcloud.services_demo.Impl;

import com.nt.newparkcloud.dao.dao_demo.Org;
import com.nt.newparkcloud.dao.dao_demo.OrgTree;
import com.nt.newparkcloud.dao.dao_demo.User;
import com.nt.newparkcloud.dao.dao_demo.UserAccount;
import com.nt.newparkcloud.services_demo.UserService;
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
    public Org get(Org org) throws Exception {

        return new Org();
    }
}
