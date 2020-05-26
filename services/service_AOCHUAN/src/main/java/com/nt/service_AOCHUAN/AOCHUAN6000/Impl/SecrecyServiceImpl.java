package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;


import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;

import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN6000.SecrecyService;


import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.SecrecyMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
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

    @Autowired
    TodoNoticeMapper todoNoticeMapper;

    @Autowired
    private RoleService roleService;


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
        if (secrecy.isNotice()) {
            ToDoNotice(secrecy, tokenModel);
            secrecy.setType(secrecy.getType()+1);
        }
        secrecy.preUpdate(tokenModel);
        secrecyMapper.updateByPrimaryKey(secrecy);
    }

    @Override
    public void insert(Secrecy secrecy, TokenModel tokenModel) throws Exception {
        secrecy.preInsert(tokenModel);
        secrecy.setSecrecy_id(UUID.randomUUID().toString());
        if (secrecy.isNotice()) {
            ToDoNotice(secrecy, tokenModel);
            secrecy.setType(1);
        }
        secrecyMapper.insert(secrecy);
    }

    @Override
    public void delete(String id) throws Exception {
        Secrecy secrecy = new Secrecy();
        secrecy.setSecrecy_id(id);
        secrecy.setStatus("1");
        secrecyMapper.updateByPrimaryKey(secrecy);
    }


    //系统服务（4月1日）
    @Scheduled(cron="* * 1 * * ?")
    public void changestatus() throws Exception {

            List<Secrecy> secrelist = secrecyMapper.selectsecrecy();
            for( Secrecy secrecy1: secrelist){
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setNoticeid(UUID.randomUUID().toString());
                toDoNotice.setTitle("【保密文件】：您有一条保密文件已到期需要处理。");
                toDoNotice.setInitiator(secrecy1.getResponsible());  /*发起人*/
                toDoNotice.setCreateby(secrecy1.getResponsible());
                toDoNotice.setCreateon(new Date());   /*创建时间*/
                toDoNotice.setStatus("0");
                toDoNotice.setContent("归档文件编号【" + secrecy1.getNo() + "】");
                toDoNotice.setDataid(secrecy1.getSecrecy_id());
                toDoNotice.setUrl("/AOCHUAN6007FormView");
                //toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(secrecy1.getResponsible());
                toDoNotice.setOwner("5ecc8476f1560b1e2ccda352");
                toDoNoticeService.save(toDoNotice);
        }


    }


    //生成代办
    @Async
    public void ToDoNotice(Secrecy secrecy, TokenModel tokenModel) throws Exception {
        // 创建代办
//            List<Secrecy> secrelist = secrecyMapper.selectsecrecy();
//            for( Secrecy secrecy1: secrelist){
//                ToDoNotice toDoNotice = new ToDoNotice();
//                toDoNotice.setNoticeid(UUID.randomUUID().toString());
//
//                toDoNotice.setTitle("【保密文件】：您有一条保密文件已到期需要处理。");
//               toDoNotice.setInitiator(secrecy1.getResponsible());  /*发起人*/
//               toDoNotice.setCreateby(secrecy1.getResponsible());
//                toDoNotice.setCreateon(new Date());   /*创建时间*/
//                toDoNotice.setStatus("0");
//                toDoNotice.setContent("归档文件编号【" + secrecy1.getNo() + "】");
//                toDoNotice.setDataid(secrecy1.getSecrecy_id());
//                toDoNotice.setUrl("/AOCHUAN6007FormView");
//                //toDoNotice.preInsert(tokenModel);
//                toDoNotice.setOwner(secrecy1.getResponsible());
//                toDoNoticeService.save(toDoNotice);
//            }

        if (secrecy.getType() == 0) {
            List<MembersVo> membersVos = roleService.getMembers("5ecc8476f1560b1e2ccda352");
            for (MembersVo membersVo :
                    membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【保密文件】：您有一条保密文件需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("保密文件编号【" + secrecy.getNo() + "】");
                toDoNotice.setDataid(secrecy.getSecrecy_id());
                toDoNotice.setUrl("/AOCHUAN6007FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }



    }
}







