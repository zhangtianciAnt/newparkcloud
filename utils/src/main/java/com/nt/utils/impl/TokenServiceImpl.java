package com.nt.utils.impl;

import cn.hutool.crypto.SecureUtil;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public TokenModel setToken(TokenModel tokenModel) {
        tokenModel.setToken(SecureUtil.md5(tokenModel.getUserId()));
        tokenModel.setExpireDate(new Date());
        mongoTemplate.save(tokenModel);
        return tokenModel;
    }

    @Override
    public Boolean validToken(HttpServletRequest request) {
        String token = request.getHeader(AuthConstants.AUTH_TOKEN);
        Query query = new Query(Criteria.where("token").is(token));
        TokenModel rst = mongoTemplate.findOne(query, TokenModel.class);
        return rst != null;
    }

    @Override
    public TokenModel getToken(HttpServletRequest request) {
        String token = request.getHeader(AuthConstants.AUTH_TOKEN);
        Query query = new Query(Criteria.where("token").is(token));
        TokenModel rst = mongoTemplate.findOne(query, TokenModel.class);
        return rst;
    }

    @Override
    public void clearToken(HttpServletRequest request) {
        String token = request.getHeader(AuthConstants.AUTH_TOKEN);
        Query query = new Query(Criteria.where("token").is(token));
        mongoTemplate.remove(query,TokenModel.class);
    }
}
