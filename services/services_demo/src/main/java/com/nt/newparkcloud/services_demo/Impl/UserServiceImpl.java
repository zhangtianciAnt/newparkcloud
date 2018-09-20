package com.nt.newparkcloud.services_demo.Impl;

import com.nt.newparkcloud.dao.dao_demo.User;
import com.nt.newparkcloud.services_demo.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(User user) throws Exception {
        mongoTemplate.save(user);
    }

    @Override
    public List<User> get(User user) throws Exception {
        Query query = new Query(Criteria.where("userName").is(user.getUserName()));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
