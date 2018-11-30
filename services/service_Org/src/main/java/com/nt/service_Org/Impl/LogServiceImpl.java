package com.nt.service_Org.Impl;

import com.nt.dao_Org.Log;
import com.nt.service_Org.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Log log) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(log.getType()));
        query.addCriteria(Criteria.where("owner").is(log.getOwner()));
        List<Log> rst = mongoTemplate.find(query,Log.class);
        if(rst.size() > 0){
            rst.get(0).getLogs().addAll(log.getLogs());
            mongoTemplate.save(rst.get(0));
        }else{
            mongoTemplate.save(log);
        }

    }
}
