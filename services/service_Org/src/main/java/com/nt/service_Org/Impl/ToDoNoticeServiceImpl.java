package com.nt.service_Org.Impl;

import com.nt.dao_Org.Log;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoNoticeServiceImpl implements ToDoNoticeService {

    @Autowired
    private MongoTemplate mongoTemplate;

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
}
