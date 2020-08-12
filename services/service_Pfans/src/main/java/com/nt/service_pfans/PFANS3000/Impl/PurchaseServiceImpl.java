package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
import com.nt.service_pfans.PFANS3000.PurchaseService;
import com.nt.service_pfans.PFANS3000.mapper.PurchaseMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PublicExpenseService publicExpenseService;

    @Autowired
    private ContractapplicationMapper contractapplicationMapper;

    @Autowired
    private com.nt.service_pfans.PFANS1000.mapper.AwardMapper AwardMapper;

    @Autowired
    private LoanApplicationMapper  loanApplicationMapper;

    @Autowired
    private PublicExpenseMapper publicExpenseMapper;

    @Override
    public List<Purchase> getPurchase(Purchase purchase, TokenModel tokenModel) {
        List<Purchase> pList1 = new ArrayList<Purchase>();
        List<Purchase> pList2 = new ArrayList<Purchase>();
        pList1  =  purchaseMapper.select(purchase);
        //根据登陆用户id查看人员角色
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(tokenModel.getUserId()));
        List<UserAccount> userAccountlist = mongoTemplate.find(query,UserAccount.class);
        String roles = "";
        for(Role role : userAccountlist.get(0).getRoles()){
            roles = roles + role.getDescription();
        }
        //财务担当，财务部长,IT担当 审批完成的数据可以看见
        if(roles.contains("财务部长") || roles.contains("财务担当") || roles.contains("IT担当"))
        {
            pList2 = purchaseMapper.getPurchaseEnd();
            if(pList2.size()>0)
            {
                pList2 = pList2.stream().filter(item -> (item.getStatus().equals("4"))).collect(Collectors.toList());
                for(Purchase p:pList2)
                {
                    if(!pList1.contains(p))
                    {
                        pList1.add(p);
                    }
                }
            }
        }
        return pList1;
    }

    @Override
    public List<Purchase> getPurchaselist(Purchase purchase) {
        return purchaseMapper.select(purchase);
    }

    @Override
    public Purchase One(String purchase_id) throws Exception {
        return purchaseMapper.selectByPrimaryKey(purchase_id);
    }

    @Override
    public void updatePurchase(Purchase purchase, TokenModel tokenModel) throws Exception {
        if(purchase.getStatus().equals("0") || purchase.getStatus().equals("1") || purchase.getStatus().equals("2") || purchase.getStatus().equals("3"))
        {
            purchase.preUpdate(tokenModel);
            purchaseMapper.updateByPrimaryKey(purchase);
        }
        if(purchase.getStatus().equals("4")
                && (purchase.getYusuanbuzu() ==null || purchase.getYusuanbuzu().equals(""))
                && purchase.getStoragedate() == null
                && purchase.getCollectionday() == null
                && (purchase.getRecipients() == null || purchase.getRecipients().equals("")))
        {
            //审批结束的更新，状态是4，预算不足为空，入库日为空，领取日，领取人为空，精算日，精算金额为空
            purchase.preUpdate(tokenModel);
            purchaseMapper.updateByPrimaryKey(purchase);
            //发待办给采购担当
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【您有一个采购需处理】");
            toDoNotice.setInitiator(purchase.getUser_id());
            toDoNotice.setContent("有一个采购申请已经通过审批，需要您处理！");
            toDoNotice.setDataid(purchase.getPurchase_id());
            toDoNotice.setUrl("/PFANS3005FormView");
            toDoNotice.setWorkflowurl("/PFANS3005View");
            toDoNotice.preInsert(tokenModel);
            //给采购担当发待办
            List<MembersVo> rolelist = roleService.getMembers("5e7863d88f4316308435113b");
            if(rolelist.size()>0)
            {
                toDoNotice.setOwner(rolelist.get(0).getUserid());
            }
            toDoNoticeService.save(toDoNotice);
        }
        if(purchase.getStatus().equals("4")
                && (purchase.getYusuanbuzu() !=null && !purchase.getYusuanbuzu().equals(""))
                && purchase.getStoragedate() == null
                && purchase.getCollectionday() == null
                && (purchase.getRecipients() == null || purchase.getRecipients().equals("")))
        {
            //审批结束，状态是4，预算不足有值，入库日为空，领取日，领取人为空，精算日，精算金额为空
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【您的采购申请需处理】");
            toDoNotice.setInitiator(purchase.getUser_id());
            toDoNotice.setContent("您的采购申请已经通过审批，因预算不足被驳回，如有需要请重新填写发起审批！");
            toDoNotice.setDataid(purchase.getPurchase_id());
            toDoNotice.setUrl("/PFANS3005FormView");
            toDoNotice.setWorkflowurl("/PFANS3005View");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(purchase.getUser_id());
            toDoNoticeService.save(toDoNotice);
            purchase.setStatus("3");
            purchase.preUpdate(tokenModel);
            purchaseMapper.updateByPrimaryKey(purchase);
        }
        if(purchase.getStatus().equals("4")
                && (purchase.getYusuanbuzu() ==null || purchase.getYusuanbuzu().equals(""))
                && purchase.getStoragedate() != null
                && purchase.getCollectionday() == null
                && (purchase.getRecipients() == null || purchase.getRecipients().equals("")))
        {
            //审批结束，状态是4，预算不足为空，入库日有值，领取日，领取人为空，精算日，精算金额为空
            purchase.preUpdate(tokenModel);
            purchaseMapper.updateByPrimaryKey(purchase);
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【您的采购申请需处理】");
            toDoNotice.setInitiator(purchase.getUser_id());
            toDoNotice.setContent("您的采购申请已经通过审批，采购担当已经完成入库，请到采购担当处领取！");
            toDoNotice.setDataid(purchase.getPurchase_id());
            toDoNotice.setUrl("/PFANS3005FormView");
            toDoNotice.setWorkflowurl("/PFANS3005View");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(purchase.getUser_id());
            toDoNoticeService.save(toDoNotice);
        }
        if(purchase.getStatus().equals("4")
                && (purchase.getYusuanbuzu() == null || purchase.getYusuanbuzu().equals(""))
                && purchase.getStoragedate() != null
                && purchase.getCollectionday() != null
                && (purchase.getRecipients() != null && !purchase.getRecipients().equals("")))
        {
            //审批结束，状态是4，预算不足为空，入库日有值，领取有值，领取人有值，精算日，精算金额为空
            purchase.preUpdate(tokenModel);
            purchaseMapper.updateByPrimaryKey(purchase);
        }
        //精算完成，发送待办给IT/财务，去维护管理番号。
        if(purchase.getStatus().equals("4") && purchase.getFixedassetsno()!=null && !purchase.getFixedassetsno().equals(""))
        {
            //审批结束，精算完成，资产管理番号更新
            purchase.preUpdate(tokenModel);
            purchaseMapper.updateByPrimaryKey(purchase);
        }
    }

    @Override
    public void insert(Purchase purchase, TokenModel tokenModel) throws Exception {

        //add-ws-根据当前年月日从001开始增加采购编号
        List<Purchase> purchaselist = purchaseMapper.selectAll();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String year = sf1.format(date);
        int number = 0;
        String Numbers = "";
        String no = "";
        if(purchaselist.size()>0){
            for(Purchase purcha :purchaselist){
                if(purcha.getPurnumbers()!="" && purcha.getPurnumbers()!=null){
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(purcha.getPurnumbers(), 2,10));
                    if(Integer.valueOf(year).equals(Integer.valueOf(checknumber))){
                        number = number+1;
                    }
                }

            }
            if(number<=8){
                no="00"+(number + 1);
            }else{
                no="0"+(number + 1);
            }
        }else{
            no = "001";
        }
        Numbers = "CG"+year+ no;
        //add-ws-根据当前年月日从001开始增加采购编号
        purchase.preInsert(tokenModel);
        purchase.setPurnumbers(Numbers);
        purchase.setPurchase_id(UUID.randomUUID().toString()) ;
        purchaseMapper.insert(purchase);
    }

    //采购业务数据流程查看详情
    @Override
    public Map<String, String> getworkfolwPurchaseData(Purchase purchase) throws Exception
    {
        Map<String, String> getpurchaseMap = new HashMap<String, String>();
        //采购决裁
        List<Purchase> purchaseList = new ArrayList<Purchase>();
        purchaseList = purchaseMapper.select(purchase);
        if(purchaseList.size()>0)
        {
            getpurchaseMap.put("purchase",purchaseList.get(0).getPurchase_id() +","+purchaseList.get(0).getStatus());
            //合同决裁
            List<Contractnumbercount> ccList = new ArrayList<Contractnumbercount>();
            ccList =  contractapplicationMapper.purchaseExistCheck(purchaseList.get(0).getPurnumbers());
            if(ccList.size()>0)
            {
                Award con = new Award();
                con.setContractnumber(ccList.get(0).getContractnumber());
                List<Award> aw = AwardMapper.select(con);
                if(aw.size()>0)
                {
                    getpurchaseMap.put("award",aw.get(0).getAward_id() +","+ aw.get(0).getStatus());
                    getpurchaseMap.put("seal",aw.get(0).getSealid() +","+ aw.get(0).getSealstatus());
                }
            }
            //暂借款申请
            if(purchaseList.get(0).getLoanapplication_id()!=null && !purchaseList.get(0).getLoanapplication_id().equals(""))
            {
                LoanApplication loan = new LoanApplication();
                loan = loanApplicationMapper.selectByPrimaryKey(purchaseList.get(0).getLoanapplication_id());
                if(loan!=null)
                {
                    getpurchaseMap.put("loanApplication",loan.getLoanapplication_id() +","+ loan.getStatus());
                }
            }
            //精算
            if(purchaseList.get(0).getPublicexpense_id()!=null && !purchaseList.get(0).getPublicexpense_id().equals(""))
            {
                PublicExpense pub = new PublicExpense();
                pub = publicExpenseMapper.selectByPrimaryKey(purchaseList.get(0).getPublicexpense_id());
                if(pub!=null)
                {
                    getpurchaseMap.put("publicExpense",pub.getPublicexpenseid() +","+ pub.getStatus());
                }
            }
        }
        return getpurchaseMap;
    }
}
