package com.nt.service_Org.Impl;

import com.nt.dao_Org.WorkOrder;
import com.nt.service_Org.WorkOrderService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

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
    public void save(WorkOrder workorder, TokenModel tokenModel) throws Exception {
        mongoTemplate.save(workorder);
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
    public List<WorkOrder> get(WorkOrder workorder) throws Exception {
        //共同带权限查询
        Query query = CustmizeQuery(workorder);
        query.with(new Sort(Sort.Direction.DESC,   "createon"));
        query.fields().exclude("photo");
        return mongoTemplate.find(query, WorkOrder.class);
    }

    /**
     * @方法名：getInfoById
     * @描述：根据id获取工单信息
     * @创建日期：2018/12/19
     * @作者：SUNXU
     * @参数：[id, request]
     * @返回值：workorder
     */
    @Override
    public WorkOrder getInfoById(String id) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        WorkOrder workorder = mongoTemplate.findOne(query, WorkOrder.class);
        return workorder;
    }
}
