package com.nt.service_Org.Impl;

import com.nt.dao_Org.Information;
import com.nt.service_Org.InformationService;
import com.nt.utils.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Information information) throws Exception {
            mongoTemplate.save(information);
    }

    @Override
    public List<Information> get(Information information) throws Exception {
        Query query = CustmizeQuery(information);
        return mongoTemplate.find(query, Information.class);
    }
}
