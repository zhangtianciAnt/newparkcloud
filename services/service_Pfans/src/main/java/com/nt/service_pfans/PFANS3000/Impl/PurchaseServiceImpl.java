package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS2000.Staffexitproce;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.ContractapplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PublicExpenseMapper;
import com.nt.service_pfans.PFANS3000.PurchaseService;
import com.nt.service_pfans.PFANS3000.mapper.PurchaseMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private TodoNoticeMapper todoNoticeMapper;

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

    @Autowired
    private BusinessplanService businessplanService;

    @Override
    public List<Purchase> getPurchase(Purchase purchase, TokenModel tokenModel) {
        List<Purchase> pList1 = new ArrayList<Purchase>();
        List<Purchase> pList2 = new ArrayList<Purchase>();
        List<Purchase> pList3 = new ArrayList<Purchase>();
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
            Purchase pur = new Purchase();
            pur.setStatus("4");
            pList2  =  purchaseMapper.select(pur);
            if(pList2.size()>0)
            {
                for(Purchase p:pList2)
                {
                    pList3 = pList1.stream().filter(item -> (item.getPurchase_id().equals(p.getPurchase_id()))).collect(Collectors.toList());
                    if(pList3.size()==0)
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
        Purchase purse = purchaseMapper.selectByPrimaryKey(purchase.getPurchase_id());
        if(!purse.getCareerplan().equals(purchase.getCareerplan())){//新旧事业计划不相同
            if(purse.getCareerplan().equals("1")){//旧内新外 还旧的钱
                businessplanService.cgTpReRulingInfo(purse.getRulingid(), purse.getTotalamount(), tokenModel);
            }else{//旧外新内 扣新的钱
                businessplanService.upRulingInfo(purchase.getRulingid(), purchase.getTotalamount(), tokenModel);
            }
        } else{//新旧事业计划相同 都是外不用考虑
            if(purse.getCareerplan().equals("1")){//新旧都是内
                if(purse.getClassificationtype().equals(purchase.getClassificationtype())){ //同类别
                    BigDecimal diffMoney = new BigDecimal(purchase.getTotalamount()).subtract(new BigDecimal(purse.getTotalamount()));
                    businessplanService.upRulingInfo(purchase.getRulingid(), diffMoney.toString(), tokenModel);
                }else{ //不同类别 还旧扣新
                    businessplanService.cgTpReRulingInfo(purse.getRulingid(), purse.getTotalamount(), tokenModel);
                    businessplanService.upRulingInfo(purchase.getRulingid(), purchase.getTotalamount(), tokenModel);
                }
            }
        }
        if(purchase.getStatus().equals("4")
                && (purchase.getYusuanbuzu() ==null || purchase.getYusuanbuzu().equals(""))
                && purchase.getStoragedate() == null
                && purchase.getCollectionday() == null
                && (purchase.getRecipients() == null || purchase.getRecipients().equals(""))
                && (purchase.getAcceptstatus() == null || purchase.getAcceptstatus().equals(""))
                && (purchase.getEnableduplicateloan().equals("PJ055002"))
        )
        {
            //审批结束的更新，状态是4，预算不足为空，入库日为空，领取日，领取人为空，精算日，精算金额为空
            purchase.preUpdate(tokenModel);
            purchaseMapper.updateByPrimaryKey(purchase);
            List<MembersVo> rolelist = roleService.getMembers("5e7863d88f4316308435113b");
            if(rolelist.size()>0){
                for(int i = 0; i < rolelist.size();i++){
                    //发待办给采购担当
                    ToDoNotice toDoNotice = new ToDoNotice();
                    toDoNotice.setTitle("【您有一个采购需处理】");
                    toDoNotice.setInitiator(purchase.getUser_id());
                    toDoNotice.setContent("有一个采购申请已经通过审批，需要您处理！");
                    toDoNotice.setDataid(purchase.getPurchase_id());
                    toDoNotice.setUrl("/PFANS3005FormView");
                    toDoNotice.setWorkflowurl("/PFANS3005View");
                    toDoNotice.preInsert(tokenModel);
                    toDoNotice.setOwner(rolelist.get(i).getUserid());
                    toDoNoticeService.save(toDoNotice);
                }
            }
        }
        if(purchase.getStatus().equals("4")
                && (purchase.getYusuanbuzu() !=null && !purchase.getYusuanbuzu().equals(""))
                && purchase.getStoragedate() == null
                && purchase.getCollectionday() == null
                && (purchase.getRecipients() == null || purchase.getRecipients().equals(""))
                && (purchase.getAcceptstatus() == null || purchase.getAcceptstatus().equals(""))
        )
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
                && (purchase.getRecipients() == null || purchase.getRecipients().equals(""))
                && (purchase.getAcceptstatus() == null || purchase.getAcceptstatus().equals(""))
        )
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
                && (purchase.getRecipients() != null && !purchase.getRecipients().equals(""))
                && (purchase.getAcceptstatus() == null || purchase.getAcceptstatus().equals(""))
        )
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
            toDoNoticeService.delToDoNotice2(purchase.getPurchase_id(),tokenModel);
        }
        //处理状态
        if(purchase.getStatus().equals("4") && purchase.getAcceptstatus() != null && !purchase.getAcceptstatus().equals("")){
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
        purchase.setPurchase_id(UUID.randomUUID().toString());
        //purchase.setSurloappmoney(purchase.getTotalamount());
        purchaseMapper.insert(purchase);

        //事业计划余额计算
        if(purchase.getCareerplan().equals("1")){
            businessplanService.upRulingInfo(purchase.getRulingid(), purchase.getTotalamount(), tokenModel);
        }
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
            if(getpurchaseMap.containsKey("purchase"))
            {
                String val= getpurchaseMap.get("purchase")+";"+purchaseList.get(0).getPurchase_id() +","+purchaseList.get(0).getStatus();
                getpurchaseMap.put("purchase",val);
            }
            else
            {
                getpurchaseMap.put("purchase",purchaseList.get(0).getPurchase_id() +","+purchaseList.get(0).getStatus());
            }

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
                    //合同
                    if(getpurchaseMap.containsKey("award"))
                    {
                        String val= getpurchaseMap.get("award")+";"+aw.get(0).getAward_id() +","+ aw.get(0).getStatus();
                        getpurchaseMap.put("award",val);
                    }
                    else
                    {
                        getpurchaseMap.put("award",aw.get(0).getAward_id() +","+ aw.get(0).getStatus());
                    }

                    //印章
                    if(getpurchaseMap.containsKey("seal"))
                    {
                        String val= getpurchaseMap.get("seal")+";"+aw.get(0).getSealid() +","+ aw.get(0).getSealstatus();
                        getpurchaseMap.put("seal",val);
                    }
                    else
                    {
                        getpurchaseMap.put("seal",aw.get(0).getSealid() +","+ aw.get(0).getSealstatus());
                    }
                }
            }
            //暂借款申请
            if(purchaseList.get(0).getLoanapplication_id()!=null && !purchaseList.get(0).getLoanapplication_id().equals(""))
            {
                String [] loanid = purchaseList.get(0).getLoanapplication_id().split(",");
                LoanApplication loan = new LoanApplication();
                if(loanid.length>0)
                {
                    for(int i=0;i<loanid.length;i++)
                    {
                        loan = loanApplicationMapper.selectByPrimaryKey(loanid[i]);
                        if(loan!=null)
                        {
                            if(getpurchaseMap.containsKey("loanApplication"))
                            {
                                String val= getpurchaseMap.get("loanApplication")+";"+loan.getLoanapplication_id() +","+ loan.getStatus();
                                getpurchaseMap.put("loanApplication",val);
                            }
                            else
                            {
                                getpurchaseMap.put("loanApplication",loan.getLoanapplication_id() +","+ loan.getStatus());
                            }
                        }
                    }
                }
            }
            //精算
            if(purchaseList.get(0).getPublicexpense_id()!=null && !purchaseList.get(0).getPublicexpense_id().equals(""))
            {
                String [] purid = purchaseList.get(0).getPublicexpense_id().split(",");
                PublicExpense pub = new PublicExpense();
                if(purid.length>0)
                {
                    for(int i=0;i<purid.length;i++)
                    {
                        pub = publicExpenseMapper.selectByPrimaryKey(purid[0]);
                        if(pub!=null)
                        {
                            if(getpurchaseMap.containsKey("publicExpense"))
                            {
                                String val= getpurchaseMap.get("publicExpense")+";"+pub.getPublicexpenseid() +","+ pub.getStatus();
                                getpurchaseMap.put("publicExpense",val);
                            }
                            else
                            {
                                getpurchaseMap.put("publicExpense",pub.getPublicexpenseid() +","+ pub.getStatus());
                            }
                        }
                    }
                }
            }
        }
        return getpurchaseMap;
    }

    @Override
    public void change(Purchase purchase, TokenModel tokenModel) throws Exception {
        Purchase pur = purchaseMapper.selectByPrimaryKey(purchase.getPurchase_id());
        pur.setCenter_id(purchase.getCenter_id());
        pur.setGroup_id(purchase.getGroup_id());
        pur.setTeam_id(purchase.getTeam_id());
        pur.setBudgetnumber(purchase.getBudgetnumber());
        pur.preUpdate(tokenModel);
        purchaseMapper.updateByPrimaryKey(pur);
    }

}
