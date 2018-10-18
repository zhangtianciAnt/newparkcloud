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

import static com.nt.utils.MongoObject.CustmizeQuery;
import static com.nt.utils.MongoObject.CustmizeUpdate;

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
        Query query = CustmizeQuery(user);
        query.addCriteria(Criteria.where("_id").is("4a678267-4753-4616-8589-ea7cc53713bf"));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }

    @Override
    public void up(User user) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("4a678267-4753-4616-8589-ea7cc53713bf"));
        Update update = CustmizeUpdate(user,true);
        mongoTemplate.updateMulti(query, update, "user");

    }
}
