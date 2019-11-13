package com.nt.service_BASF.Impl;
import com.nt.dao_BASF.Commandrecord;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.nt.service_BASF.CommandrecordServices;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class CommandrecordServiceImpl implements CommandrecordServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @方法名：save
     * @描述：信息发布保存
     * @创建日期：2018/12/13
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
     * @描述：获取信息发布列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：Information
     */
    @Override
    public List<Commandrecord> get(Commandrecord commandrecord) throws Exception {
        //共同带权限查询
        Query query = CustmizeQuery(commandrecord);
        query.with(new Sort(Sort.Direction.DESC, "releasetime"));
        if (commandrecord.getCurrentPage() != null && commandrecord.getPageSize() != null) {

            query.skip((commandrecord.getCurrentPage() - 1) * commandrecord.getPageSize());
            query.limit(commandrecord.getPageSize());
        }
//        //优化查询速度
//        if (commandrecord.getHttpOriginType() != null && commandrecord.getHttpOriginType().equals(AuthConstants.LOG_EQUIPMENT_PC)) {
//
//            query.fields().exclude("accidentlocation");
//        }
        return mongoTemplate.find(query, Commandrecord.class);

    }
}
