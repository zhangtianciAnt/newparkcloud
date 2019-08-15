package com.nt.service_ServiceProvider.Impl;

import com.mongodb.client.result.UpdateResult;
import com.nt.dao_ServiceProvider.PMInformationDelivery;
import com.nt.service_ServiceProvider.PMInformationDeliveryService;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PMInformationDeliveryServiceImpl implements PMInformationDeliveryService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PMInformationDelivery> select(PMInformationDelivery pmInformationDelivery) {
        List<PMInformationDelivery> pmInformationDeliveries = new ArrayList<PMInformationDelivery>();
        Query query = new Query(Criteria.where("status").is("0"));
        if (pmInformationDelivery.get_id() == null || "".equals(pmInformationDelivery.get_id())) {
            // selectAll
            pmInformationDeliveries = mongoTemplate.find(query, PMInformationDelivery.class);
        } else {
            // selectById
            query.addCriteria(Criteria.where("_id").is(pmInformationDelivery.get_id()));
            pmInformationDeliveries = mongoTemplate.find(query, PMInformationDelivery.class);
        }
        return pmInformationDeliveries;
    }

    @Override
    public List<PMInformationDelivery> selectByid(TokenModel tokenModel) {
        List<PMInformationDelivery> pmInformationDeliveries = new ArrayList<PMInformationDelivery>();
        List<PMInformationDelivery> result = new ArrayList<PMInformationDelivery>();
        Query query = new Query(Criteria.where("status").is("0"));
        pmInformationDeliveries = mongoTemplate.find(query, PMInformationDelivery.class);
        for (PMInformationDelivery pd : pmInformationDeliveries) {
            if (pd.getServiceProvider().get_id().equals(tokenModel.getUserId())) {
                result.add(pd);
            }
        }
        return result;
    }

    @Override
    public ApiResult insert(PMInformationDelivery pmInformationDelivery, TokenModel tokenModel) {
        pmInformationDelivery.preInsert(tokenModel);
        pmInformationDelivery.setPublicationStatus("0");
        mongoTemplate.insert(pmInformationDelivery);
        if (pmInformationDelivery.get_id() != null && !"".equals(pmInformationDelivery.get_id())) {
            return ApiResult.success(MsgConstants.SUCCESS);
        } else {
            return ApiResult.fail(MsgConstants.FAIL);
        }
    }

    @Override
    public ApiResult update(PMInformationDelivery pmInformationDelivery, TokenModel tokenModel) {
        pmInformationDelivery.preUpdate(tokenModel);
        Query query = new Query(Criteria.where("_id").is(pmInformationDelivery.get_id()));
        Update update = new Update();
        update.set("title", pmInformationDelivery.getTitle());
        update.set("explain", pmInformationDelivery.getExplain());
        update.set("cover", pmInformationDelivery.getCover());
        update.set("publicationState", pmInformationDelivery.getPublicationStatus());
        update.set("auditStatus", pmInformationDelivery.getAuditStatus());
        update.set("serviceProvider", pmInformationDelivery.getServiceProvider());
        update.set("status", pmInformationDelivery.getStatus());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, PMInformationDelivery.class);
        if (updateResult.getModifiedCount() > 0) {
            return ApiResult.success(MsgConstants.SUCCESS);
        } else {
            return ApiResult.fail(MsgConstants.FAIL);
        }
    }
}
