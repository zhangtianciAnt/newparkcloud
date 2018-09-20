package com.nt.newparkcloud.utils.impl;

import cn.hutool.crypto.SecureUtil;
import com.nt.newparkcloud.utils.TokenModel;
import com.nt.newparkcloud.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public TokenModel setToken(TokenModel tokenModel) {
        tokenModel.setToken(SecureUtil.md5(tokenModel.getToken()));
        tokenModel.setDate(new Date());
        mongoTemplate.save(tokenModel);
        return tokenModel;
    }

    @Override
    public Boolean validToken(String token) {
        Query query = new Query(Criteria.where("token").is(token));
        TokenModel rst = mongoTemplate.findOne(query, TokenModel.class);
        return rst != null;
    }
}
