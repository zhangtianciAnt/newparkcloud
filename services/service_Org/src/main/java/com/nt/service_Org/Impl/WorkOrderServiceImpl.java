package com.nt.service_Org.Impl;

import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Org.WorkOrder;
import com.nt.service_Org.UserService;
import com.nt.service_Org.WorkOrderService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserService userservice;

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


            //来源  1.PC后台；2.微信服务号
            if(workorder.getSource().equals("2")&&workorder.getWorkorderlog()==null)
            {
                String createby = tokenModel.getUserId();

                UserVo userVo = userservice.getAccountCustomerById(createby);
                String createname = userVo.getCustomerInfo().getUserinfo().getCustomername();
                String createphoto = userVo.getCustomerInfo().getUserinfo().getMobilenumber();

                workorder.setContacts(createname);
                workorder.setPhonenumber(createphoto);
                //工单日志
                List<WorkOrder.WorkOrderLog> wloginfos = new ArrayList<WorkOrder.WorkOrderLog>();
                WorkOrder.WorkOrderLog log = new WorkOrder.WorkOrderLog();
                log.setUsername(createname);
                log.setWorkorderstatus("1");
                log.setActiondescribe("新建工单");
                wloginfos.add(log);
                workorder.setWorkorderlog(wloginfos);

                workorder.preInsert(tokenModel);
            }



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
