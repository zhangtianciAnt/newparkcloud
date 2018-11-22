package com.nt.service_Org.Impl;

import com.nt.dao_Org.Log;
import com.nt.service_Org.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Log log) throws Exception {
        // 执行更新操作
        mongoTemplate.save(log);
    }
}
