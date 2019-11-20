package com.nt.service_BASF.Impl;
import com.nt.dao_BASF.Commandrecord;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.nt.service_BASF.CommandrecordServices;

import java.util.*;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class CommandrecordServiceImpl implements CommandrecordServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @方法名：save
     * @描述：接警指挥信息保存
     * @创建日期：2019/11/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：
     */
    @Override
    public void save(Commandrecord commandrecord, TokenModel tokenModel) throws Exception {
        if (commandrecord != null ) {
            commandrecord.preInsert(tokenModel);
            mongoTemplate.save(commandrecord);
        }
    }

    /**
     * @方法名：get
     * @描述：获取接警指挥信息
     * @创建日期：2019/11/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：Information
     * @return
     */
    @Override
    public Commandrecord get(String cid) throws Exception {
        //根据主键查询数据
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(cid));
        Commandrecord commandrecord1 = mongoTemplate.findOne(query, Commandrecord.class);
        return commandrecord1;
    }
}
