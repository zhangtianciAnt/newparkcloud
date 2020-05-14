package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;


import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;

import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN6000.SecrecyService;

//import com.nt.service_AOCHUAN.AOCHUAN6000.SelectSecrecy;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.SecrecyMapper;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
@EnableScheduling
public class SecrecyServiceImpl implements SecrecyService {

    @Autowired
    private SecrecyMapper secrecyMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;




    @Override
    public List<Secrecy> get(Secrecy secrecy) throws Exception {
        return secrecyMapper.select(secrecy);
    }

    @Override
    public Secrecy One(String id) throws Exception {
        return secrecyMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Secrecy secrecy, TokenModel tokenModel) throws Exception {
        secrecy.preUpdate(tokenModel);
        secrecyMapper.updateByPrimaryKey(secrecy);

    }

    @Override
    public void insert(Secrecy secrecy, TokenModel tokenModel) throws Exception {
        secrecy.preInsert(tokenModel);
        secrecy.setSecrecy_id(UUID.randomUUID().toString());
        secrecyMapper.insert(secrecy);
    }

    @Override
    public void delete(String id) throws Exception {
        Secrecy secrecy=new Secrecy();
        secrecy.setSecrecy_id(id);
        secrecy.setStatus("1");
        secrecyMapper.updateByPrimaryKey(secrecy);
    }


    //系统服务（4月1日）
    @Scheduled(cron="59 * * * * ?")
    public void changestatus() throws Exception {
       // List<Secrecy> secrelist = secrecyMapper.selectsecrecy();
        ToDoNotice();
//        for( Secrecy secrecy: secrelist){
//
//        secrecy.setType("1");
//         secrecyMapper.updateByPrimaryKey(secrecy);
//        }

    }


    //生成代办
    @Async
    public void ToDoNotice() throws Exception {
        // 创建代办
            List<Secrecy> secrelist = secrecyMapper.selectsecrecy();
            for( Secrecy secrecy1: secrelist){
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("你有已归档文件");
                //toDoNotice.setInitiator(secrecy.getResponsible()); /*发起人*/
                //toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("归档文件编号【" + secrecy1.getNo() + "】");
                toDoNotice.setDataid(secrecy1.getSecrecy_id());
                toDoNotice.setUrl("/AOCHUAN6007FormView");
                toDoNotice.setStatus("2");
               //toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(secrecy1.getResponsible());
                toDoNoticeService.save(toDoNotice);
            }


    }


}
