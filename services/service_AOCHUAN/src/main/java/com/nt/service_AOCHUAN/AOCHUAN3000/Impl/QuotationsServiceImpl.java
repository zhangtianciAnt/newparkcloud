package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.QuoAndEnq;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.EnquiryMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ReturngoodsMapper;
import com.nt.service_AOCHUAN.AOCHUAN8000.Impl.ContractNumber;
import com.nt.service_AOCHUAN.AOCHUAN8000.mapper.NumberMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.utils.*;
import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private EnquiryMapper enquiryMapper;

    @Autowired
    private ContractNumber contractNumber;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    /**session操作类*/
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;
    @Autowired
    TodoNoticeMapper todoNoticeMapper;


    @Override
    public List<Quotations> get(Quotations quotations) throws Exception {
        return quotationsMapper.select(quotations);
    }

    @Override
    public Quotations getOne(String id) throws Exception {
        Quotations quotations = new Quotations();
        Enquiry enquiry = new Enquiry();
        enquiry.setQuotations_id(id);
        quotations = quotationsMapper.selectByPrimaryKey(id);
        List<Enquiry> enquiries = enquiryMapper.select(enquiry);
        for (Enquiry _enquiry:
        enquiries) {
         String[] document = _enquiry.getDoc().length() > 0 ? _enquiry.getDoc().split(","):new String[0];
         _enquiry.setDocument(document);
        }
        quotations.setEnquiry(enquiries);
        return quotations;
    }

    @Override
    public List<QuoAndEnq> getForSupplier(String id) throws Exception {
        return quotationsMapper.getForSupplier(id);
    }

    @Override
    public List<Quotations> getForCustomer(String id) throws Exception {
        return quotationsMapper.getForCustomer(id);
    }

    @Override
    public void update(Quotations quotations, TokenModel tokenModel) throws Exception {
              quotations.preUpdate(tokenModel);
              if(quotations.isNotice()){
                   ToDoNotice(tokenModel,quotations);
                   quotations.setType(quotations.getType() + 1);
               }
              quotations.preUpdate(tokenModel);
              quotationsMapper.updateByPrimaryKeySelective(quotations);
        insertEnquiry(quotations.getEnquiry(),quotations.getQuotations_id());
        //ToDoNotice(tokenModel,quotations.getQuotations_id());
    }

    @Override
    public void insert(Quotations quotations, TokenModel tokenModel) throws Exception {
        String number = contractNumber.getContractNumber("PT001001","quotations");
        quotations.setQuotationsno(number);
        quotations.setQuotations_id(UUID.randomUUID().toString());
        quotations.preInsert(tokenModel);
        if(quotations.isNotice()){
            ToDoNotice(tokenModel,quotations);
            quotations.setType(1);
        }
        quotationsMapper.insert(quotations);
        insertEnquiry(quotations.getEnquiry(),quotations.getQuotations_id());
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
            List<MembersVo> membersVos =  roleService.getMembers("5eba6f09e52fa718db632696");
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
                if(webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()) != null &&
                        webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()).stream().findFirst().isPresent()){
                    String sessionId=webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()).stream().findFirst().get();

                    ToDoNotice condition = new ToDoNotice();
                    condition.setOwner(toDoNotice.getOwner());
                    condition.setStatus(AuthConstants.TODO_STATUS_TODO);
                    //condition.setType(toDoNotice.getType());
                    List<ToDoNotice> list = todoNoticeMapper.select(condition);

                    messagingTemplate.convertAndSendToUser(sessionId,"/topicMessage/subscribe",list,createHeaders(sessionId));
                }
            }
        }else if(quotations.getType() == 1){
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【销售报价】：您有一条询价需要处理。");
            toDoNotice.setInitiator(tokenModel.getUserId());
            toDoNotice.setContent("询报价编号【" +quotations.getQuotationsno()+"】");
            toDoNotice.setDataid(quotations.getQuotations_id());
            toDoNotice.setUrl("/AOCHUAN3001FormView");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(quotations.getSaleresponsibility());
            toDoNoticeService.save(toDoNotice);
        }
    }

    private void insertEnquiry(List<Enquiry> enquiryList,String quotationsId){
        Enquiry enquiry = new Enquiry();
        enquiry.setQuotations_id(quotationsId);
        enquiryMapper.delete(enquiry);
         if(enquiryList!= null && enquiryList.size() > 0){
             for (Enquiry _enquiry:
             enquiryList) {
                 _enquiry.setEnquiry_id(UUID.randomUUID().toString());
                 _enquiry.setQuotations_id(quotationsId);
             String document = _enquiry.getDocument().length > 0 ? StringUtils.join(_enquiry.getDocument()) : "";
             _enquiry.setDoc(document);
             }
             enquiryMapper.insertEnquiryList(enquiryList);
         }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
