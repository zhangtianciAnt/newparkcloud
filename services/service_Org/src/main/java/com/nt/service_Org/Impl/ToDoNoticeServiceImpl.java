package com.nt.service_Org.Impl;

import com.nt.dao_Org.Log;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.ToDoNotice.Notices;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class ToDoNoticeServiceImpl implements ToDoNoticeService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @方法名：save
     * @描述：消息保存
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice]
     * @返回值：
     */
    @Override
    public void save(ToDoNotice toDoNotice) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(toDoNotice.getType()));
        query.addCriteria(Criteria.where("owner").is(toDoNotice.getOwner()));
        List<ToDoNotice> rst = mongoTemplate.find(query,ToDoNotice.class);
        if(rst.size() > 0){
           if(rst.get(0).getStatus().equals(AuthConstants.TODONOTICE_TYPE_TODO)){
               rst.get(0).getToDoInfos().addAll(toDoNotice.getToDoInfos());

           }else{
               rst.get(0).getNotices().addAll(toDoNotice.getNotices());
           }
            mongoTemplate.save(rst.get(0));
        }else{
            mongoTemplate.save(toDoNotice);
        }
    }

    /**
     * @方法名：get
     * @描述：获取消息列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice]
     * @返回值：toDoNotice
     */
    @Override
    public List<ToDoNotice> get(ToDoNotice toDoNotice) throws Exception {
        Query query = CustmizeQuery(toDoNotice);
        return mongoTemplate.find(query, ToDoNotice.class);
    }

    /**
     * @方法名：updateNoticesStatus
     * @描述：更新已阅
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice]
     * @返回值：
     */
    @Override
    public void updateNoticesStatus(ToDoNotice toDoNotice) throws Exception {
            mongoTemplate.save(toDoNotice);
    }
}
