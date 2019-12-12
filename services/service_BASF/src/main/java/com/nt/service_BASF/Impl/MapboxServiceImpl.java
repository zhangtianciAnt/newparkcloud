package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Mapbox;
import com.nt.service_BASF.MapboxServices;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class MapboxServiceImpl implements MapboxServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Mapbox get(Mapbox mapbox) throws Exception {
        Query query = CustmizeQuery(mapbox);
        mapbox = mongoTemplate.findOne(query, Mapbox.class);
        return mapbox;
    }

}
