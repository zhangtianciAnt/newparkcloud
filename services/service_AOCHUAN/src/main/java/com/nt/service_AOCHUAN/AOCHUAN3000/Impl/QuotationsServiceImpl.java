package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class QuotationsServiceImpl implements QuotationsService {

    @Autowired
    private QuotationsMapper quotationsMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private RoleService roleService;

    @Override
    public List<Quotations> get() throws Exception {
        return quotationsMapper.selectAll();
    }

    @Override
    public Quotations getOne(String id) throws Exception {
        return quotationsMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Quotations quotations, TokenModel tokenModel) throws Exception {
              quotations.preUpdate(tokenModel);
              if(quotations.isNotice()){
                   ToDoNotice(tokenModel,quotations);
                   quotations.setType(quotations.getType() + 1);
               }
              quotationsMapper.updateByPrimaryKeySelective(quotations);
        //ToDoNotice(tokenModel,quotations.getQuotations_id());
    }

    @Override
    public void insert(Quotations quotations, TokenModel tokenModel) throws Exception {
        StringBuffer sb = new StringBuffer("YW");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());
        int count =  quotationsMapper.select_Count() + 1;
        String str = String.format("%07d", count);
        sb.append(date).append(str);
        String id = UUID.randomUUID().toString();
        quotations.setQuotationsno(sb.toString());
        quotations.setQuotations_id(id);
        quotations.preInsert(tokenModel);
        if(quotations.isNotice()){
            ToDoNotice(tokenModel,quotations);
            quotations.setType(1);
        }
        quotationsMapper.insert(quotations);
    }

    @Override
    public void delete(String id) throws Exception {
        quotationsMapper.deleteByPrimaryKey(id);
    }

    //生成代办
    @Async
    public void ToDoNotice(TokenModel tokenModel,Quotations quotations) throws Exception{
        // 创建代办
        if(quotations.getType() == 0){
            List<MembersVo> membersVos =  roleService.getMembers("5e96adfa96c5744860b31a00");
            for (MembersVo membersVo:
            membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【采购询价】：您有一条询价需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("询报价编号【" +quotations.getQuotationsno()+"】");
                toDoNotice.setDataid(quotations.getQuotations_id());
                toDoNotice.setUrl("/AOCHUAN3001FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
            }
        }else if(quotations.getType() == 1){
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【采购询价】：您有一条询价需要处理。");
            toDoNotice.setInitiator(tokenModel.getUserId());
            toDoNotice.setContent("询报价编号【" +quotations.getQuotationsno()+"】");
            toDoNotice.setDataid(quotations.getQuotations_id());
            toDoNotice.setUrl("/AOCHUAN3001FormView");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(quotations.getSaleresponsibility());
            toDoNoticeService.save(toDoNotice);
        }

    }
}
