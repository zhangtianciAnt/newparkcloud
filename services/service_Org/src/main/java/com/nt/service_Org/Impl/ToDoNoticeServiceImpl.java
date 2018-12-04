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

    @Override
    public List<ToDoNotice> get(ToDoNotice toDoNotice) throws Exception {
        Query query = CustmizeQuery(toDoNotice);
        return mongoTemplate.find(query, ToDoNotice.class);
    }

    @Override
    public void updateNoticesStatus(ToDoNotice toDoNotice) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("notices.title").is(toDoNotice.getNotices().get(0).getTitle()));
        query.addCriteria(Criteria.where("notices.owner").is(toDoNotice.getNotices().get(0).getOwner()));
        List<Notices> rst = mongoTemplate.find(query,Notices.class);
        if(rst.size() > 0){
//            if(rst.get(0).getStatus().equals(AuthConstants.TODONOTICE_TYPE_TODO)){
//                rst.get(0).getToDoInfos().addAll(toDoNotice.getToDoInfos());
//
//            }else{
            rst.get(0).setTitle("2222");
            mongoTemplate.save(rst.get(0));






        }
    }
}
