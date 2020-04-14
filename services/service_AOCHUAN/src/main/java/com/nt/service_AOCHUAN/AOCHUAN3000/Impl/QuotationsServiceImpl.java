package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuotationsServiceImpl implements QuotationsService {

    @Autowired
    private QuotationsMapper quotationsMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

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
              quotationsMapper.updateByPrimaryKeySelective(quotations);
        //ToDoNotice(tokenModel,quotations.getQuotations_id());
    }

    @Override
    public void insert(Quotations quotations, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        quotations.setQuotations_id(id);
        quotations.preInsert(tokenModel);
        quotationsMapper.insert(quotations);
    }

    @Override
    public void delete(String id) throws Exception {
        quotationsMapper.deleteByPrimaryKey(id);
    }

    //生成代办
    public void ToDoNotice(TokenModel tokenModel,String id) throws Exception{
        // 创建代办
        ToDoNotice toDoNotice = new ToDoNotice();
        List<String> params = new ArrayList<String>();
        //params.add(workflowname);
        toDoNotice.setTitle("11111111");
        toDoNotice.setInitiator(tokenModel.getUserId());
        toDoNotice.setContent("2222222222");
        toDoNotice.setDataid(id);
        toDoNotice.setUrl("/AOCHUAN3001FormView");
        //toDoNotice.setWorkflowurl(workFlowurl);
        toDoNotice.preInsert(tokenModel);
        toDoNotice.setOwner(tokenModel.getUserId());
        toDoNoticeService.save(toDoNotice);
    }
}
