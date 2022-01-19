package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TotalCostVo;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Comproject;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS3000.PurchaseService;
import com.nt.service_pfans.PFANS3000.mapper.PurchaseMapper;
import com.nt.service_pfans.PFANS5000.mapper.ComProjectMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PublicExpenseServiceImpl implements PublicExpenseService {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private PurchaseApplyMapper purchaseapplyMapper;
    @Autowired
    private CommunicationMapper communicationMapper;
    @Autowired
    private JudgementMapper judgementMapper;

    @Autowired
    private PublicExpenseMapper publicExpenseMapper;

    @Autowired
    private TodoNoticeMapper todoNoticeMapper;

    @Autowired
    private InvoiceMapper invoicemapper;

    @Autowired
    private PublicExpenseService publicExpenseService;

    @Autowired
    private TrafficDetailsMapper trafficDetailsMapper;

    @Autowired
    private PurchaseDetailsMapper purchaseDetailsMapper;

    @Autowired
    private OtherDetailsMapper otherDetailsMapper;

    @Autowired
    private TotalCostMapper totalCostMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    private ComProjectMapper comprojectMapper;

    @Autowired
    private BusinessplanService businessplanService;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private JudgementdetailMapper judgementdetailMapper;
    @Autowired
    private ContractnumbercountMapper contractnumbercountMapper;
    @Autowired
    private AwardDetailMapper awardDetailMapper;

    //add-ws-7/9-禅道任务248
    @Override
    public Map<String, Object> exportjs(String publicexpenseid, HttpServletRequest request) throws Exception {
        PublicExpenseVo publicexpensevo = publicExpenseService.selectById(publicexpenseid);
        Map<String, Object> resultMap = new HashMap<>();
        List<PurchaseDetails> purlist = publicexpensevo.getPurchasedetails();
        List<TrafficDetails> tralist = publicexpensevo.getTrafficdetails();
        List<OtherDetails> otherlist = publicexpensevo.getOtherdetails();
        List<Object> needMergeList = new ArrayList<>();
        if (purlist.size() > 0 || tralist.size() > 0
                || otherlist.size() > 0) {
            needMergeList.addAll(purlist);
            needMergeList.addAll(tralist);
            needMergeList.addAll(otherlist);
        }
        Map<String, Object> oldmergeResult = null;
        oldmergeResult = oldmergeDetailList(needMergeList);
        List<TrafficDetails> traffic = (List<TrafficDetails>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
        List<PurchaseDetails> pudetails = (List<PurchaseDetails>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
        List<OtherDetails> other = (List<OtherDetails>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
        for (Object o : oldmergeResult.values()) {
            String accountcode = getProperty(o, "accountcode");
            String budgetcoding = getProperty(o, "budgetcoding");
            String plsummary = getProperty(o, "plsummary");
            String currency = getProperty(o, "currency");
            String subjectnumber = getProperty(o, "subjectnumber");
            // 精度损失修改 ztc 1125 fr
            String rmb = getProperty(o, "rmb");
            String foreigncurrency = getProperty(o, "foreigncurrency");
            // 精度损失修改 ztc 1125 to
            DecimalFormat df = new DecimalFormat("#0.00");
            int scale = 2;//设置位数
            int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
//            解决PDF打印报错BUG ztc fr
            BigDecimal bd = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(rmb) ? "0" : rmb);
            bd = bd.setScale(scale, roundingMode);
            BigDecimal bd1 = new BigDecimal(com.mysql.jdbc.StringUtils.isNullOrEmpty(foreigncurrency) ? "0" : foreigncurrency);
//            解决PDF打印报错BUG ztc to
            bd1 = bd1.setScale(scale, roundingMode);
            //resign  upd scc   2021/7/23  公共费用精算书在打印时采购费明细中交通费不显示 from
            if (purlist.size() == 0  && (accountcode.equals("PJ119004") || accountcode.equals("PJ132004"))) {
                //end resign  upd scc  2021/7/23 公共费用精算书在打印时采购费明细中交通费不显示  编号 to
                TrafficDetails trafficdetails = new TrafficDetails();
                resultMap.put("交通费", traffic);
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(currency)) {
                        trafficdetails.setCurrency(iteA.getValue1());
                    }
                }
                ;
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(budgetcoding)) {
                        trafficdetails.setBudgetcoding(ite.getValue2() + "_" + ite.getValue3());
                    }
                }
                if (accountcode.length() > 5) {
                    String traAccountcode = accountcode.substring(0, 5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(traAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(accountcode)) {
                            trafficdetails.setAccountcode(iteA.getValue1());
                        }
                    }
                }
                trafficdetails.setSubjectnumber(subjectnumber);
                trafficdetails.setForeigncurrency(String.valueOf(bd1));
                trafficdetails.setRmb(String.valueOf(bd));
                traffic.add(trafficdetails);
                //resign  upd scc   2021/7/23  公共费用精算书在打印时采购费明细中交通费不显示 from
            } else if (plsummary.equals("PJ111010") || plsummary.equals("PJ111008")) {
                //end resign  upd scc  2021/7/23 公共费用精算书在打印时采购费明细中交通费不显示  编号 to
                PurchaseDetails purchasedetails = new PurchaseDetails();
                resultMap.put("采购费", pudetails);
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(currency)) {
                        purchasedetails.setCurrency(iteA.getValue1());
                    }
                }
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(budgetcoding)) {
                        purchasedetails.setBudgetcoding(ite.getValue2() + "_" + ite.getValue3());
                    }
                }
                if (accountcode.length() > 5) {
                    String traAccountcode = accountcode.substring(0, 5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(traAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(accountcode)) {
                            purchasedetails.setAccountcode(iteA.getValue1());
                        }
                    }
                }
                purchasedetails.setSubjectnumber(subjectnumber);
                purchasedetails.setRmb(String.valueOf(bd));
                purchasedetails.setForeigncurrency(String.valueOf(bd1));
                pudetails.add(purchasedetails);
            } else {
                OtherDetails otherdetails = new OtherDetails();
                resultMap.put("其他费用", other);
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(currency)) {
                        otherdetails.setCurrency(iteA.getValue1());
                    }
                }
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(budgetcoding)) {
                        otherdetails.setBudgetcoding(ite.getValue2() + "_" + ite.getValue3());
                    }
                }
                if (accountcode.length() > 5) {
                    String traAccountcode = accountcode.substring(0, 5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(traAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(accountcode)) {
                            otherdetails.setAccountcode(iteA.getValue1());
                        }
                    }
                }
                otherdetails.setSubjectnumber(subjectnumber);
                otherdetails.setForeigncurrency(String.valueOf(bd1));
                otherdetails.setRmb(String.valueOf(bd));
                other.add(otherdetails);
            }
        }
        return resultMap;

    }

    private Map<String, Object> oldmergeDetailList(List<Object> detailList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Float> specialMap = new HashMap<>();
        String inputType = getInputType(detailList.get(0));
        for (Object detail : detailList) {
            String isRmb = getProperty(detail, "rmb");
            // 发票No
            String keyNo = getProperty(detail, FIELD_INVOICENUMBER);
            String budgetcoding = getProperty(detail, "budgetcoding");
            String subjectnumber = getProperty(detail, "subjectnumber");
            String mergeKey = "";
            mergeKey = budgetcoding + " ... " + subjectnumber;
            // 行合并
            float money = getPropertyFloat(detail, "rmb");
            float moneysum = getPropertyFloat(detail, "foreigncurrency");
            Object mergeObject = resultMap.get(mergeKey);
            if (mergeObject != null) {
                // 发现可以合并数据
                float newMoney = getPropertyFloat(mergeObject, "rmb") + money;
                float newMoneysum = getPropertyFloat(mergeObject, "foreigncurrency") + moneysum;
                setProperty(mergeObject, "rmb", newMoney + "");
                setProperty(mergeObject, "foreigncurrency", newMoneysum + "");
            } else {
                resultMap.put(mergeKey, detail);
            }
        }
        return resultMap;
    }

    //add-ws-7/9-禅道任务248
    //列表查询
    @Override
    public List<PublicExpense> get(PublicExpense publicExpense) throws Exception {
        //region add_qhr_20210810 添加项目名称
        List<PublicExpense> publicExpenseList = publicExpenseMapper.select(publicExpense);
        List<PublicExpense> publicList = new ArrayList<>();
        for (PublicExpense expense : publicExpenseList) {
            if (expense.getProject_id().equals("PP024001")) {
                expense.setProjectname("共通项目（会议研修等）");
            }
            CompanyProjects companyProject = companyProjectsMapper.selectByPrimaryKey(expense.getProject_id());
            Comproject comproject = comprojectMapper.selectByPrimaryKey(expense.getProject_id());
            if (companyProject != null) {
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(companyProject.getProject_name())) {
                    expense.setProjectname(companyProject.getProject_name());
                }
            } else if (comproject != null) {
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(comproject.getProject_name())) {
                    expense.setProjectname(comproject.getProject_name());
                }
            }
            publicList.add(expense);
        }
        return publicList;
        //endregion  add_qhr_20210810 添加项目名称
    }

    @Override
    public List<PublicExpense> getpublicelist(String publicexpenseid) throws Exception {
        return publicExpenseMapper.getpublicelist(publicexpenseid);
    }

    @Override
    public List<TotalCost> gettotalcost(TotalCostVo totalcostvo) throws Exception {
        List<TotalCost> listvo = new ArrayList<TotalCost>();
        TotalCost totalcost = new TotalCost();
        List<TotalCost> totalcostlist = totalcostvo.getTotalcost();
        for (TotalCost totalList : totalcostlist) {
            totalcost.setPublicexpenseid(totalList.getPublicexpenseid());
            List<TotalCost> listVo = totalCostMapper.select(totalcost);
            listVo = listVo.stream().sorted(Comparator.comparing(TotalCost::getNumber)).collect(Collectors.toList());
            listvo.addAll(0, listVo);
        }
        return listvo;
    }

    public Float aFloat(Float a) {
        return a > 0 ? a : -a;
    }

    //新建
    @Override
    public void insert(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception {
        //发票编号
        String invoiceNo = "";
        Calendar cal = Calendar.getInstance();
        String year = new SimpleDateFormat("yy", Locale.CHINESE).format(Calendar.getInstance().getTime());
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String no = "";
        //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-添加判定条件-start
        if (publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate())) != null && (publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002002"))&&(publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "GL")) != null) {
            //        add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-添加判定条件-end
            int count = publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "GL");
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-代码回退，添加对应判定-start
            if(count <99){
                no = String.format("%2d", count + 1).replace(" ", "0");
            }
            else{
                no = String.format("%3d", count + 1).replace(" ", "0");
            }
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-代码回退，添加对应判定-end
            String month1 = String.format("%2d", month).replace(" ", "0");
            String day1 = String.format("%2d", day).replace(" ", "0");
            invoiceNo = "DL4GL" + year + month1 + day1 + no;
        }
        //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-添加判定条件-start
        else if (publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate())) != null && (publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002001"))&&(publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "AP")) != null) {
        //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-添加判定条件-end
            int count = publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "AP");
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-代码回退，添加对应判定-start
            if(count <99){
                no = String.format("%2d", count + 1).replace(" ", "0");
            }
            else{
                no = String.format("%3d", count + 1).replace(" ", "0");
            }
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-代码回退，添加对应判定-end
            String month1 = String.format("%2d", month).replace(" ", "0");
            String day1 = String.format("%2d", day).replace(" ", "0");
            invoiceNo = "DL4AP" + year + month1 + day1 + no;
        }
        //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-添加判定条件-start
        else if (publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate())) != null && (publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002003"))&&(publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "AR")) != null) {
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-添加判定条件-end
           int count = publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "AR");
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-代码回退，添加对应判定-start
            if(count <99){
                no = String.format("%2d", count + 1).replace(" ", "0");
            }
            else{
                no = String.format("%3d", count + 1).replace(" ", "0");
            }
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-代码回退，添加对应判定-end
            String month1 = String.format("%2d", month).replace(" ", "0");
            String day1 = String.format("%2d", day).replace(" ", "0");
            invoiceNo = "DL4AR" + year + month1 + day1 + no;
        } else {
            no = "01";
            String month1 = String.format("%2d", month).replace(" ", "0");
            String day1 = String.format("%2d", day).replace(" ", "0");
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-机制修改，现在为利用SQL截取号码的后两位-start
            if(publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002001")){
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-机制修改，现在为利用SQL截取号码的后两位-end
                invoiceNo = "DL4AP" + year + month1 + day1 + no;
            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-机制修改，现在为利用SQL截取号码的后两位-start
            }
            if(publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002002")){
               invoiceNo = "DL4GL" + year + month1 + day1 + no;
            }
            if(publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002003")){
               invoiceNo = "DL4AR" + year + month1 + day1 + no;
            }

            //            add-lyt-21/4/6-PSDCD_PFANS_20210318_BUG_035-机制修改，现在为利用SQL截取号码的后两位-end
        }

//        String month1 = String.format("%2d", month).replace(" ", "0");
//        String day1 = String.format("%2d", day).replace(" ", "0");
//        invoiceNo = "DL4AP" + year + month1 + day1 + no;
        //PSDCD_PFANS_20210519_BUG_006 修改供应商编码 供应商地点错误 fr
        //add-ws-9/25-禅道567
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(publicExpenseVo.getPublicexpense().getJudgement())) {
            Award award1 = new Award();
            String[] aa = publicExpenseVo.getPublicexpense().getJudgement().split(",");
            award1.setAward_id(aa[0]);
            List<Award> award = awardMapper.select(award1);
            if (award.size() > 0) {
                String statuspublic3 = "";
                String statuspublic1 = publicExpenseVo.getPublicexpense().getJudgement_name();
                String statuspublic2 = award.get(0).getStatuspublic();
                if (com.mysql.jdbc.StringUtils.isNullOrEmpty(statuspublic2)) {
                    statuspublic3 = statuspublic1;
                } else {
                    statuspublic3 = statuspublic1 + ',' + statuspublic2;
                }
                award.get(0).setStatuspublic(statuspublic3);
                awardMapper.updateByPrimaryKey(award.get(0));
            }
        }
        //add-ws-9/25-禅道567
        String publicexpenseid = UUID.randomUUID().toString();
        PublicExpense publicExpense = new PublicExpense();
        BeanUtils.copyProperties(publicExpenseVo.getPublicexpense(), publicExpense);
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(publicExpenseVo.getPublicexpense().getLoan())){
            List<String> loanTypeList = new ArrayList();
            String pecode = "";
            String[] loaList = publicExpenseVo.getPublicexpense().getLoan().split(",");
            if (loaList.length > 0) {
                for (String lo : loaList) {
                    LoanApplication loan = new LoanApplication();
                    loan.setLoanapplication_id(lo);
                    List<LoanApplication> loanApplicationList = loanApplicationMapper.select(loan);
                    if(loanApplicationList.size() > 0){
                        loanTypeList.add(loanApplicationList.get(0).getPaymentmethod());
                    }
                }
                LoanApplication loanCode = new LoanApplication();
                loanCode.setLoanapplication_id(loaList[0]);
                loanCode = loanApplicationMapper.selectByPrimaryKey(loanCode);
                pecode = !com.mysql.jdbc.StringUtils.isNullOrEmpty(loanCode.getPayeecode()) ? loanCode.getPayeecode() : loanCode.getName();
            }
            if(loanTypeList.size() > 0){
                if(loanTypeList.contains("PJ015002")){
                    publicExpense.setLoantype("0" + "," + pecode);
                }else if(loanTypeList.contains("PJ015001") || loanTypeList.contains("PJ015003")){
                    publicExpense.setLoantype("1" + "," + pecode);
                }else{
                    publicExpense.setLoantype("");
                }
            }
        }
        //PSDCD_PFANS_20210519_BUG_006 修改供应商编码 供应商地点错误 to
        publicExpense.setInvoiceno(invoiceNo);
        publicExpense.preInsert(tokenModel);
        publicExpense.setPublicexpenseid(publicexpenseid);
        publicExpenseMapper.insertSelective(publicExpense);
        //float money = Float.parseFloat(publicExpense.getRmbexpenditure()) + Float.parseFloat(publicExpense.getTormb());

        //add ccm 0727
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(publicExpense.getJudgement_name())) {
            if (publicExpense.getJudgement_name().substring(0, 2).equals("CG")) {//采购
                String[] pur = publicExpense.getJudgement_name().split(",");
                for (String p : pur) {
                    Purchase purchase = new Purchase();
                    purchase.setPurnumbers(p);
                    List<Purchase> purchaseList = purchaseMapper.select(purchase);
                    if (purchaseList.size() > 0) {
//                        float diff = money - Float.parseFloat(purchaseList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        purchaseList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (com.nt.utils.StringUtils.isNotBlank(purchaseList.get(0).getPublicexpense_id())) {
                            purchaseList.get(0).setPublicexpense_id(purchaseList.get(0).getPublicexpense_id() + "," + publicExpense.getPublicexpenseid());
                            purchaseList.get(0).setInvoiceno(purchaseList.get(0).getInvoiceno() + "," + publicExpense.getInvoiceno());
                        } else {
                            purchaseList.get(0).setPublicexpense_id(publicExpense.getPublicexpenseid());
                            purchaseList.get(0).setInvoiceno(publicExpense.getInvoiceno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能

                        purchaseList.get(0).preUpdate(tokenModel);
                        purchaseMapper.updateByPrimaryKey(purchaseList.get(0));
                    }
                }
            }
            //ADD_FJL_0730  start
            else if (publicExpense.getJudgement_name().substring(0, 3).equals("JJF"))//交际费
            {
                String[] pur = publicExpense.getJudgement_name().split(",");
                for (String p : pur) {
                    Communication communication = new Communication();
                    communication.setNumbercation(p);
                    List<Communication> communicationList = communicationMapper.select(communication);
                    if (communicationList.size() > 0) {
//                        float diff = money - Float.parseFloat(communicationList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        communicationList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (com.nt.utils.StringUtils.isNotBlank(communicationList.get(0).getPublicexpense_id())) {
                            communicationList.get(0).setPublicexpense_id(communicationList.get(0).getPublicexpense_id() + "," + publicExpense.getPublicexpenseid());
                            communicationList.get(0).setInvoiceno(communicationList.get(0).getInvoiceno() + "," + publicExpense.getInvoiceno());
                        } else {
                            communicationList.get(0).setPublicexpense_id(publicExpense.getPublicexpenseid());
                            communicationList.get(0).setInvoiceno(publicExpense.getInvoiceno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能

                        communicationList.get(0).preUpdate(tokenModel);
                        communicationMapper.updateByPrimaryKey(communicationList.get(0));
                    }
                }
            } else if (publicExpense.getJudgement_name().substring(0, 2).equals("JC"))//其他业务
            {
                String[] pur = publicExpense.getJudgement_name().split(",");
                for (String p : pur) {
                    Judgement judgement = new Judgement();
                    judgement.setJudgnumbers(p);
                    List<Judgement> judgementList = judgementMapper.select(judgement);
                    if (judgementList.size() > 0) {
//                        float diff = money - Float.parseFloat(judgementList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        judgementList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (com.nt.utils.StringUtils.isNotBlank(judgementList.get(0).getPublicexpense_id())) {
                            judgementList.get(0).setPublicexpense_id(judgementList.get(0).getPublicexpense_id() + "," + publicExpense.getPublicexpenseid());
                            judgementList.get(0).setInvoiceno(judgementList.get(0).getInvoiceno() + "," + publicExpense.getInvoiceno());
                        } else {
                            judgementList.get(0).setPublicexpense_id(publicExpense.getPublicexpenseid());
                            judgementList.get(0).setInvoiceno(publicExpense.getInvoiceno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        judgementList.get(0).preUpdate(tokenModel);
                        judgementMapper.updateByPrimaryKey(judgementList.get(0));
                    }
                }
            } else if (publicExpense.getJudgement_name().substring(0, 2).equals("WC"))//无偿设备
            {
                String[] pur = publicExpense.getJudgement_name().split(",");
                for (String p : pur) {
                    Judgement judgement = new Judgement();
                    judgement.setJudgnumbers(p);
                    List<Judgement> judgementList = judgementMapper.select(judgement);
                    if (judgementList.size() > 0) {
//                        float diff = money - Float.parseFloat(judgementList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        judgementList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (com.nt.utils.StringUtils.isNotBlank(judgementList.get(0).getPublicexpense_id())) {
                            judgementList.get(0).setPublicexpense_id(judgementList.get(0).getPublicexpense_id() + "," + publicExpense.getPublicexpenseid());
                            judgementList.get(0).setInvoiceno(judgementList.get(0).getInvoiceno() + "," + publicExpense.getInvoiceno());
                        } else {
                            judgementList.get(0).setPublicexpense_id(publicExpense.getPublicexpenseid());
                            judgementList.get(0).setInvoiceno(publicExpense.getInvoiceno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        judgementList.get(0).preUpdate(tokenModel);
                        judgementMapper.updateByPrimaryKey(judgementList.get(0));
                    }
                }
            } else if (publicExpense.getJudgement_name().substring(0, 2).equals("QY"))//千元费用
            {
                String[] pur = publicExpense.getJudgement_name().split(",");
                for (String p : pur) {
                    PurchaseApply purchaseapply = new PurchaseApply();
                    purchaseapply.setPurchasenumbers(p);
                    List<PurchaseApply> purchaseapplyList = purchaseapplyMapper.select(purchaseapply);
                    if (purchaseapplyList.size() > 0) {
//                        float diff = money - Float.parseFloat(purchaseapplyList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        purchaseapplyList.get(0).setBalancejude(String.valueOf(diff));
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        if (com.nt.utils.StringUtils.isNotBlank(purchaseapplyList.get(0).getPublicexpense_id())) {
                            purchaseapplyList.get(0).setPublicexpense_id(purchaseapplyList.get(0).getPublicexpense_id() + "," + publicExpense.getPublicexpenseid());
                            purchaseapplyList.get(0).setInvoiceno(purchaseapplyList.get(0).getInvoiceno() + "," + publicExpense.getInvoiceno());
                        } else {
                            purchaseapplyList.get(0).setPublicexpense_id(publicExpense.getPublicexpenseid());
                            purchaseapplyList.get(0).setInvoiceno(publicExpense.getInvoiceno());
                        }
                        //add ccm 0813 决裁到暂借款，精算  check去掉  决裁中的暂借款和精算存在多条的可能
                        purchaseapplyList.get(0).preUpdate(tokenModel);
                        purchaseapplyMapper.updateByPrimaryKey(purchaseapplyList.get(0));
                    }
                }
            } else if (publicExpense.getJudgement_name().substring(0, 1).equals("C"))//境内外出差
            {
                String[] pur = publicExpense.getJudgement_name().split(",");
                for (String p : pur) {
                    Business business = new Business();
                    business.setBusiness_number(p);
                    List<Business> businessList = businessMapper.select(business);
                    if (businessList.size() > 0) {
//                        float diff = money - Float.parseFloat(businessList.get(0).getBalancejude());
//                        if (diff >= 0) {
//                            diff = 0;
//                            money = diff;
//                        } else {
//                            aFloat(diff);
//                        }
//                        businessList.get(0).setBalancejude(String.valueOf(diff));
                        businessList.get(0).setPublicexpense_id(publicExpense.getPublicexpenseid());
                        businessList.get(0).setInvoiceno(publicExpense.getInvoiceno());
                        businessList.get(0).preUpdate(tokenModel);
                        businessMapper.updateByPrimaryKey(businessList.get(0));
                    }
                }
            }
            //ADD_FJL_0730  end
        }
        //add ccm 0727


        List<TrafficDetails> trafficDetailslist = publicExpenseVo.getTrafficdetails();
        List<PurchaseDetails> purchaseDetailslist = publicExpenseVo.getPurchasedetails();
        List<OtherDetails> otherDetailslist = publicExpenseVo.getOtherdetails();
        List<Invoice> invoicelist = publicExpenseVo.getInvoice();

        if (trafficDetailslist != null) {
            int rowundex = 0;
            for (TrafficDetails trafficDetails : trafficDetailslist) {
                rowundex = rowundex + 1;
                trafficDetails.preInsert(tokenModel);
                trafficDetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficDetails.setPublicexpenseid(publicexpenseid);
                trafficDetails.setRowindex(rowundex);
                trafficDetailsMapper.insertSelective(trafficDetails);
            }
        }
        if (purchaseDetailslist != null) {
            int rowundex = 0;
            for (PurchaseDetails purchaseDetails : purchaseDetailslist) {
                rowundex = rowundex + 1;
                purchaseDetails.preInsert(tokenModel);
                purchaseDetails.setPurchasedetails_id(UUID.randomUUID().toString());
                purchaseDetails.setPublicexpenseid(publicexpenseid);
                purchaseDetails.setRowindex(rowundex);
                purchaseDetailsMapper.insertSelective(purchaseDetails);
            }
        }
        if (otherDetailslist != null) {
            int rowundex = 0;
            for (OtherDetails otherDetails : otherDetailslist) {
                rowundex = rowundex + 1;
                otherDetails.preInsert(tokenModel);
                otherDetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherDetails.setPublicexpenseid(publicexpenseid);
                otherDetails.setRowindex(rowundex);
                otherDetailsMapper.insertSelective(otherDetails);
            }
        }
        if (invoicelist != null) {
            int rowundex = 0;
            for (Invoice invoice : invoicelist) {
                rowundex = rowundex + 1;
                invoice.preInsert(tokenModel);
                invoice.setInvoice_id(UUID.randomUUID().toString());
                invoice.setPublicexpenseid(publicexpenseid);
                invoice.setRowindex(rowundex);
                invoicemapper.insertSelective(invoice);
            }
        }

        // 付款方式为网上银行付款，个人账户，转账支票做以下处理
//        if("PJ004001".equals(publicExpense.getPaymentmethod()) || "PJ004002".equals(publicExpense.getPaymentmethod()) || "PJ004003".equals(publicExpense.getPaymentmethod())){
        saveTotalCostList(invoiceNo, invoicelist, trafficDetailslist, purchaseDetailslist, otherDetailslist, publicExpenseVo, tokenModel, publicexpenseid);
//        }
    }

    private void saveTotalCostList(String invoiceNo, List<Invoice> invoicelist, List<TrafficDetails> trafficDetailslist, List<PurchaseDetails> purchaseDetailslist,
                                   List<OtherDetails> otherDetailslist, PublicExpenseVo publicExpenseVo, TokenModel tokenModel, String publicexpenseid) throws Exception {
        // 发票日期，条件日期
        Date date = new Date();
        SimpleDateFormat myFormatter = new SimpleDateFormat("ddMMMyyyy", Locale.ENGLISH);
        date = myFormatter.parse(myFormatter.format(date));
        //通过字典查取税率
        List<com.nt.dao_Org.Dictionary> dictionaryList = dictionaryService.getForSelect("PJ071");
        Map<String, String> taxRateMap = new HashMap<>();
        for (Dictionary d : dictionaryList) {
            taxRateMap.put(d.getCode(), d.getValue1());
        }
        //通过字典查取汇率缩写
        List<com.nt.dao_Org.Dictionary> dicExchangeRateList = dictionaryService.getForSelect("PG019");
        Map<String, String> exchangeRateMap = new HashMap<>();
        for (Dictionary d : dicExchangeRateList) {
            exchangeRateMap.put(d.getCode(), d.getValue3());
        }
        //科目名字典
        Map<String, String> accountCodeMap = new HashMap<>();
        for (int i = 0; i < 28; i++) {
            List<com.nt.dao_Org.Dictionary> dictionaryListAccount = dictionaryService.getForSelect("PJ" + (112 + i));
            for (Dictionary d : dictionaryListAccount) {
                accountCodeMap.put(d.getCode(), d.getValue1());
            }
        }

        Map<String, Object> mergeResult = null;
        Map<String, BigDecimal> specialMap = new HashMap<>();
        for (Invoice invoice : invoicelist) {
            if (SPECIAL_KEY.equals(invoice.getInvoicetype()) && (!"0".equals(invoice.getInvoiceamount()))) {
                // 专票，获取税率
                BigDecimal rate = new BigDecimal(taxRateMap.getOrDefault(invoice.getTaxrate(), "0"));
                if (publicExpenseVo.getPublicexpense().getType() == "PJ001002") {
                    if (rate.compareTo(new BigDecimal(0)) == -1) {
                        throw new LogicalException("专票税率不能为0");
                    }
                }
                specialMap.put(invoice.getInvoicenumber(), rate);
            }
            // 合计发票金额（含税）
//            specialMap.put(TOTAL_TAX,
//                    specialMap.getOrDefault(TOTAL_TAX, 0f) + getFloatValue(invoice.getInvoiceamount()));
        }
        BigDecimal mount = new BigDecimal(publicExpenseVo.getPublicexpense().getRmbexpenditure()).add(new BigDecimal(publicExpenseVo.getPublicexpense().getForeigncurrency()));
        // 总金额改为人民币支出
        specialMap.put(TOTAL_TAX, mount);
        if (mount.compareTo(new BigDecimal(0)) == -1) {
            throw new LogicalException("支出总金额应大于0");
        }

        List<Object> needMergeList = new ArrayList<>();
        if (trafficDetailslist.size() > 0) {
            // 交通费
            needMergeList.addAll(trafficDetailslist);
        } else {
            // 其他
            needMergeList.addAll(purchaseDetailslist);
            needMergeList.addAll(otherDetailslist);
        }
        mergeResult = mergeDetailList(needMergeList, specialMap);


        List<TotalCost> csvList = new ArrayList<>();
        List<TotalCost> taxList = (List<TotalCost>) mergeResult.getOrDefault(TAX_KEY, new ArrayList<>());
        List<TotalCost> paddingList = (List<TotalCost>) mergeResult.getOrDefault(PADDING_KEY, new ArrayList<>());
        String inputType = (String) mergeResult.get(INPUT_TYPE_KEY);
        for (Object o : mergeResult.values()) {
            if (o instanceof TrafficDetails || o instanceof PurchaseDetails || o instanceof OtherDetails) {
                String money = getProperty(o, inputType);
                TotalCost cost = new TotalCost();
//                if ( FIELD_RMB.equals(inputType) ) {
//                    cost.setCurrency("CYN");
//                }
//                else if ( FIELD_FOREIGNCURRENCY.equals(inputType) ) {
//                    cost.setCurrency("FOREIGN");
//                }
                //币种，汇率
                if (!StringUtils.isEmpty(getProperty(o, "currency"))) {
                    cost.setCurrency(exchangeRateMap.getOrDefault(getProperty(o, "currency"), ""));
                    cost.setExchangerate(getProperty(o, "currencyrate"));
                } else {
                    cost.setCurrency("CNY");
                    cost.setExchangerate("");
                }

                cost.setLineamount(money);
                cost.setBudgetcoding(getProperty(o, "budgetcoding"));
                cost.setSubjectnumber(getProperty(o, "subjectnumber"));
                //发票说明取人名+科目名
                cost.setRemark(getProperty(o, "accountcode"));
                csvList.add(cost);
            }
        }
        csvList.addAll(taxList);
        csvList.addAll(paddingList);
        //获取人名
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(publicExpenseVo.getPublicexpense().getUser_id()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        String userName = "";
        if (customerInfo != null) {
            userName = customerInfo.getUserinfo().getCustomername();
        }

        int rowindex = 0;
        for (TotalCost insertInfo : csvList) {
            if (insertInfo.getLineamount().equals("0")) {
                continue;
            } else {
                rowindex = rowindex + 1;
                insertInfo.preInsert(tokenModel);
                insertInfo.setTotalcost_id(UUID.randomUUID().toString());
                insertInfo.setPublicexpenseid(publicexpenseid);
                insertInfo.setNumber(rowindex);
                //日期格式，取当前日期， 输出CSV时需要格式化成28OCT2019
                insertInfo.setInvoicedate(date);
                insertInfo.setConditiondate(date);
                if (publicExpenseVo.getPublicexpense().getPayeecode() != "") {
                    insertInfo.setVendorcode(publicExpenseVo.getPublicexpense().getPayeecode());//供应商编号
                } else {
                    insertInfo.setVendorcode(publicExpenseVo.getPublicexpense().getCode());//个人编号
                }
                insertInfo.setInvoiceamount(specialMap.get(TOTAL_TAX).toString());//总金额
                //发票说明
                if (insertInfo.getRemark() != "" && insertInfo.getRemark() != null) {
                    insertInfo.setRemark(userName + "经办" + accountCodeMap.getOrDefault(insertInfo.getRemark(), ""));
                }

                insertInfo.setInvoicenumber(invoiceNo);

                totalCostMapper.insertSelective(insertInfo);
            }
        }
    }

    /**
     * 专票类型KEY
     */
    private static final String SPECIAL_KEY = "PJ068001";
    private static final String TOTAL_TAX = "__TOTAL_TAX__";
    /**
     * 税-》KEY
     */
    private static final String TAX_KEY = "__TAX_KEY__";
    private static final String PADDING_KEY = "__PADDING_KEY__";
    private static final String INPUT_TYPE_KEY = "__INPUT_TYPE_KEY__";
    private static final DecimalFormat FNUM = new DecimalFormat("##0.00");

    /**
     * 对明细数据分组
     *
     * @param detailList
     * @return resultMap
     */
    private Map<String, Object> mergeDetailList(List<Object> detailList, final Map<String, BigDecimal> specialMap) throws LogicalException {
        Map<String, Object> resultMap = new HashMap<>();
        if (detailList.size() <= 0) {
            throw new LogicalException("明细不能为空");
        }
        String inputType = getInputType(detailList.get(0));
//        Map<String, Object> mergedMap = new HashMap<>();
        for (Object detail : detailList) {
            if (!inputType.equals(getInputType(detail))) {
                throw new LogicalException("一次申请，只能选择一种货币。");
            }
            // 发票No
            String keyNo = getProperty(detail, FIELD_INVOICENUMBER);
            String budgetcoding = getProperty(detail, "budgetcoding");
            String subjectnumber = getProperty(detail, "subjectnumber");
            String isRmb = getProperty(detail, "rmb");
            String mergeKey;
            if (specialMap.containsKey(keyNo) && Float.parseFloat(isRmb) > 0) {
                mergeKey = keyNo + " ... " + budgetcoding + " ... " + subjectnumber;
            } else {
                mergeKey = budgetcoding + " ... " + subjectnumber;
            }
            // 行合并
            float money = getPropertyFloat(detail, inputType);
            float taxes = getPropertyFloat(detail, "taxes");
            Object mergeObject = resultMap.get(mergeKey);
            if (mergeObject != null) {
                // 发现可以合并数据
                float newMoney = getPropertyFloat(mergeObject, inputType) + money;
                float oldMoneysum = getPropertyFloat(mergeObject, "taxes") + taxes;
                setProperty(mergeObject, inputType, newMoney + "");
                setProperty(mergeObject, "taxes", oldMoneysum + "");
            } else {
                resultMap.put(mergeKey, detail);
            }
        }

        float totalTax = 0f;
        List<Object> list = new ArrayList<>(resultMap.values());
        for (Object detail : list) {
            // 发票No
            String keyNo = getProperty(detail, FIELD_INVOICENUMBER);
            float money = getPropertyFloat(detail, "rmb");
            float moneysum = getPropertyFloat(detail, "foreigncurrency");
            float gettaxes = getPropertyFloat(detail, "taxes");
            totalTax = totalTax + money + moneysum;
            String getRmb = getProperty(detail, "rmb");
            // 如果是专票，处理税
            if (specialMap.containsKey(keyNo) && Float.parseFloat(getRmb) > 0) {
                List<TotalCost> taxList = (List<TotalCost>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
                resultMap.put(TAX_KEY, taxList);
                BigDecimal rate = specialMap.get(keyNo);
                TotalCost taxCost = new TotalCost();
                int scale1 = 2;//设置位数
                int roundingMode1 = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
                //PSDCD_PFANS_20210519_BUG_007 前端画面实际会略微调整，后台不需要重新计算税额 fr
                // 税拔
                String lineCost = FNUM.format(new BigDecimal(money).subtract(new BigDecimal(gettaxes)));
                // 税金
                String lineRate = FNUM.format(gettaxes);
                String lineRateNo = FNUM.format(new BigDecimal(gettaxes));
                //PSDCD_PFANS_20210519_BUG_007 前端画面实际会略微调整，后台不需要重新计算税额 to
                if (money > 0) {
                    // 税
                    //add-ws-4/22-税金不为0存2302-00-01A0
                    if (!lineRate.equals("0")) {
                        taxCost.setSubjectnumber("2302-00-01A0");
                    } else {
                        taxCost.setSubjectnumber(getProperty(detail, "subjectnumber"));
                    }
                    //add-ws-4/22-税金不为0存2302-00-01A0
                    taxCost.setLineamount(lineRate);
                    taxCost.setBudgetcoding(getProperty(detail, "budgetcoding"));
                    //发票说明
                    taxCost.setRemark(getProperty(detail, "accountcode"));
                    //币种
                    taxCost.setCurrency("CNY");
                    taxList.add(taxCost);
                    // 税拔
                    setProperty(detail, inputType, lineCost);
//                    float diff = Float.parseFloat(lineCost) + Float.parseFloat(lineRateNo) - money;
                    BigDecimal diff = new BigDecimal(lineCost).add(new BigDecimal(lineRateNo)).subtract(new BigDecimal(money)).setScale(scale1, roundingMode1);
                    if (diff.compareTo(new BigDecimal(0)) == 1) {
                        TotalCost padding = new TotalCost();
                        padding.setLineamount(diff + "");
                        padding.setBudgetcoding(getProperty(detail, "budgetcoding"));
                        padding.setSubjectnumber(getProperty(detail, "subjectnumber"));
                        //发票说明
                        padding.setRemark(getProperty(detail, "accountcode"));
                        //币种
                        padding.setCurrency("CNY");
                        List<TotalCost> paddingList = (List<TotalCost>) resultMap.getOrDefault(PADDING_KEY, new ArrayList<>());
                        paddingList.add(padding);
                        resultMap.put(PADDING_KEY, paddingList);
                    }
                }
            }
        }
//        if (totalTax != specialMap.get(TOTAL_TAX)) {
//            throw new LogicalException("发票合计金额与明细不匹配。");
//        }
        resultMap.put(INPUT_TYPE_KEY, inputType);
        return resultMap;
    }

    private static final String FIELD_RMB = "rmb";
    private static final String FIELD_FOREIGNCURRENCY = "foreigncurrency";
    private static final String FIELD_INVOICENUMBER = "invoicenumber";

    private String getInputType(Object o) throws LogicalException {
        float rmb = getPropertyFloat(o, FIELD_RMB);
        float foreign = getPropertyFloat(o, FIELD_FOREIGNCURRENCY);
        if (rmb > 0 && foreign > 0) {
            throw new LogicalException("人民币和外币不能同时输入。");
        }
        if (rmb < 0 || foreign < 0 || (rmb + foreign) < 0) {
            throw new LogicalException("明细行金额不能为负数。");
        }

        return rmb >= 0 ? FIELD_RMB : FIELD_FOREIGNCURRENCY;
    }

    private String getProperty(Object o, String key) {
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(o, key);
        } catch (Exception e) {
            log.error("error get property for {}, key {}", o, key);
            return "";
        }
    }

    private float getPropertyFloat(Object o, String key) {
        return getFloatValue(getProperty(o, key));
    }

    private void setProperty(Object o, String key, String val) {
        try {
            org.apache.commons.beanutils.BeanUtils.setProperty(o, key, val);
        } catch (Exception e) {
            log.error("error set bean {} property {} value {}", o.getClass(), key, val);
        }
    }

    private float getFloatValue(String o) {
        try {
            return Float.parseFloat(o);
        } catch (Exception e) {
            return 0;
        }
    }

    //编辑
    @Override
    public void update(PublicExpenseVo publicExpenseVo, TokenModel tokenModel) throws Exception {
        //        del-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-start
//        String invoiceNos = "";
//        Calendar cal = Calendar.getInstance();
//        String year = new SimpleDateFormat("yy", Locale.CHINESE).format(Calendar.getInstance().getTime());
//        int month = cal.get(Calendar.MONTH) + 1;
//        int day = cal.get(Calendar.DATE);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String no = "";
//        String id = publicExpenseVo.getPublicexpense().getPublicexpenseid();
//        PublicExpense publics = publicExpenseMapper.selectByPrimaryKey(id);
        //        del-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-end
        if(publicExpenseVo.getPublicexpense().getModuleid().equals("GL")){
            publicExpenseVo.getPublicexpense().setModuleid("PJ002002");
        }else if(publicExpenseVo.getPublicexpense().getModuleid().equals("AP")){
            publicExpenseVo.getPublicexpense().setModuleid("PJ002001");
        }else if(publicExpenseVo.getPublicexpense().getModuleid().equals("AR")){
            publicExpenseVo.getPublicexpense().setModuleid("PJ002003");
        }
//        del-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-start
//        if (!publics.getModuleid().equals(publicExpenseVo.getPublicexpense().getModuleid())) {
//            if (publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate())) != null && (publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002002"))) {
//                int count = publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "GL");
//                no = String.format("%2d", count + 1).replace(" ", "0");
//                String month1 = String.format("%2d", month).replace(" ", "0");
//                String day1 = String.format("%2d", day).replace(" ", "0");
//                invoiceNos = "DL4GL" + year + month1 + day1 + no;
//            } else if (publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate())) != null && (publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002001"))) {
//                int count = publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "AP");
//                no = String.format("%2d", count + 1).replace(" ", "0");
//                String month1 = String.format("%2d", month).replace(" ", "0");
//                String day1 = String.format("%2d", day).replace(" ", "0");
//                invoiceNos = "DL4AP" + year + month1 + day1 + no;
//            } else if (publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate())) != null && (publicExpenseVo.getPublicexpense().getModuleid().equals("PJ002003"))) {
//                int count = publicExpenseMapper.getAporGlNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()), "AR");
//                no = String.format("%2d", count + 1).replace(" ", "0");
//                String month1 = String.format("%2d", month).replace(" ", "0");
//                String day1 = String.format("%2d", day).replace(" ", "0");
//                invoiceNos = "DL4AR" + year + month1 + day1 + no;
//            } else {
//                no = "01";
//                String month1 = String.format("%2d", month).replace(" ", "0");
//                String day1 = String.format("%2d", day).replace(" ", "0");
//                invoiceNos = "DL4AP" + year + month1 + day1 + no;
//            }
//        }
//        del-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-start
//        add-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-start
          String  invoiceNos = publicExpenseVo.getPublicexpense().getInvoiceno();
//        add-lyt-21/3/24-PSDCD_PFANS_20210318_BUG_035-end

        PublicExpense publicExpense = new PublicExpense();
        BeanUtils.copyProperties(publicExpenseVo.getPublicexpense(), publicExpense);
        //add-ws-7/20-禅道任务342
        String status = publicExpense.getStatus();
        String judgement = publicExpense.getJudgement();
        String processingstatus = publicExpense.getProcessingstatus();
        //add-ws-7/20-禅道任务342
        //upd-8/20-ws-禅道468任务
        Date modeon = publicExpense.getModifyon();
        //upd-8/20-ws-禅道468任务
        publicExpense.preUpdate(tokenModel);
        //upd-8/20-ws-禅道468任务
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(publicExpense.getProcessingstatus())) {
            if (status.equals("4") && processingstatus.equals("1")) {
                publicExpense.setModifyon(modeon);
            }
        }
        publicExpense.setInvoiceno(invoiceNos);
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(publicExpenseVo.getPublicexpense().getLoan())){
            List<String> loanTypeList = new ArrayList();
            String pecode = "";
            String[] loaList = publicExpenseVo.getPublicexpense().getLoan().split(",");
            if (loaList.length > 0) {
                for (String lo : loaList) {
                    LoanApplication loan = new LoanApplication();
                    loan.setLoanapplication_id(lo);
                    List<LoanApplication> loanApplicationList = loanApplicationMapper.select(loan);
                    if(loanApplicationList.size() > 0){
                        loanTypeList.add(loanApplicationList.get(0).getPaymentmethod());
                    }
                }
                LoanApplication loanCode = new LoanApplication();
                loanCode.setLoanapplication_id(loaList[0]);
                loanCode = loanApplicationMapper.selectByPrimaryKey(loanCode);
                pecode = !com.mysql.jdbc.StringUtils.isNullOrEmpty(loanCode.getPayeecode()) ? loanCode.getPayeecode() : loanCode.getName();
            }
            if(loanTypeList.size() > 0){
                if(loanTypeList.contains("PJ015002")){
                    publicExpense.setLoantype("0" + "," + pecode);
                }else if(loanTypeList.contains("PJ015001") || loanTypeList.contains("PJ015003")){
                    publicExpense.setLoantype("1" + "," + pecode);
                }else{
                    publicExpense.setLoantype("");
                }
            }
        }
        //解决旧数据审批结束操作异常问题 ztc fr
        PublicExpense publEe = publicExpenseMapper.selectByPrimaryKey(publicExpense.getPublicexpenseid());
        if (publicExpenseVo.getPublicexpense().getStatus().equals("4") && publicExpenseVo.getPublicexpense().getBusiness_type() != null && publicExpenseVo.getPublicexpense().getBusiness_type()){
            //解决旧数据审批结束操作异常问题 ztc to
            if(!publEe.getStatus().equals("4")){
                this.writeOff(publicExpenseVo,tokenModel);
            }
        }
        //upd-8/20-ws-禅道468任务
        publicExpenseMapper.updateByPrimaryKey(publicExpense);

        if (publicExpense.getStatus().equals("4") && !publEe.getStatus().equals("4")) {
            String[] loa = publicExpense.getLoan().split(",");
            if (loa.length > 0) {
                for (int i = 0; i < loa.length; i++) {
                    LoanApplication loanApplication = new LoanApplication();
                    loanApplication.setLoanapplication_id(loa[i]);
                    LoanApplication loantion = loanApplicationMapper.selectByPrimaryKey(loanApplication);
                    if (loantion != null) {
                        loantion.setCanafver("1");
                        if (loantion.getCanafvermoney() != null && loantion.getCanafvermoney() != "") {
                            loantion.setCanafvermoney(BigDecimal.valueOf(Double.parseDouble(loantion.getCanafvermoney()) + Double.parseDouble(publicExpense.getMoneys())).setScale(2, RoundingMode.HALF_UP).toPlainString());
                        } else {
                            loantion.setCanafvermoney(publicExpense.getMoneys());
                        }
                        loanApplicationMapper.updateByPrimaryKey(loantion);
                    }
                }
            }
        }


        //add-ws-7/20-禅道任务342  08/17待办删除
//        String[] ts = judgement.split(",");
//        if (ts.length > 0) {
//            for (int i = 0; i < ts.length; i++) {
//                Award award = new Award();
//                award.setAward_id(ts[i]);
//                Award awa = awardMapper.selectByPrimaryKey(award);
//                if (awa != null) {
//                    awa.setStatuspublic(status);
//                    awardMapper.updateByPrimaryKey(awa);
//                    if (status.equals("4")) {
//                        List<MembersVo> rolelist = roleService.getMembers("5e78633d8f43163084351138");
//                        if (rolelist.size() > 0) {
//                            ToDoNotice toDoNotice3 = new ToDoNotice();
//                            toDoNotice3.setTitle("【" + awa.getContractnumber() + "】发起得精算申请已成功");
//                            toDoNotice3.setInitiator(awa.getUser_id());
//                            toDoNotice3.setContent("流程结束。可进行线下支付");
//                            toDoNotice3.setDataid(awa.getContractnumber());
//                            toDoNotice3.setUrl("/PFANS1025FormView");
//                            toDoNotice3.setWorkflowurl("/PFANS1025FormView");
//                            toDoNotice3.preInsert(tokenModel);
//                            toDoNotice3.setOwner(rolelist.get(0).getUserid());
//                            toDoNoticeService.save(toDoNotice3);
//                        }
//                    }
//                }
//            }
//        }
        //add-ws-7/20-禅道任务342
        String spublicexpenseid = publicExpense.getPublicexpenseid();
        String invoiceNo = publicExpense.getInvoiceno();
        TrafficDetails traffic = new TrafficDetails();
        traffic.setPublicexpenseid(spublicexpenseid);
        trafficDetailsMapper.delete(traffic);
        List<TrafficDetails> trafficlist = publicExpenseVo.getTrafficdetails();

        PurchaseDetails purchase = new PurchaseDetails();
        purchase.setPublicexpenseid(spublicexpenseid);
        purchaseDetailsMapper.delete(purchase);
        List<PurchaseDetails> purchaselist = publicExpenseVo.getPurchasedetails();

        OtherDetails other = new OtherDetails();
        other.setPublicexpenseid(spublicexpenseid);
        otherDetailsMapper.delete(other);
        List<OtherDetails> otherlist = publicExpenseVo.getOtherdetails();

        Invoice invoice = new Invoice();
        invoice.setPublicexpenseid(spublicexpenseid);
        invoicemapper.delete(invoice);
        List<Invoice> invoicelist = publicExpenseVo.getInvoice();

        if (trafficlist != null) {
            int rowundex = 0;
            for (TrafficDetails trafficDetails : trafficlist) {
                rowundex = rowundex + 1;
                trafficDetails.preInsert(tokenModel);
                trafficDetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficDetails.setPublicexpenseid(spublicexpenseid);
                trafficDetails.setRowindex(rowundex);
                trafficDetailsMapper.insertSelective(trafficDetails);
            }
        }
        if (purchaselist != null) {
            int rowundex = 0;
            for (PurchaseDetails purchaseDetails : purchaselist) {
                rowundex = rowundex + 1;
                purchaseDetails.preInsert(tokenModel);
                purchaseDetails.setPurchasedetails_id(UUID.randomUUID().toString());
                purchaseDetails.setPublicexpenseid(spublicexpenseid);
                purchaseDetails.setRowindex(rowundex);
                purchaseDetailsMapper.insertSelective(purchaseDetails);
            }
        }
        if (otherlist != null) {
            int rowundex = 0;
            for (OtherDetails otherDetails : otherlist) {
                rowundex = rowundex + 1;
                otherDetails.preInsert(tokenModel);
                otherDetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherDetails.setPublicexpenseid(spublicexpenseid);
                otherDetails.setRowindex(rowundex);
                otherDetailsMapper.insertSelective(otherDetails);
            }
        }

        if (invoicelist != null) {
            int rowundex = 0;
            for (Invoice invoicel : invoicelist) {
                rowundex = rowundex + 1;
                invoicel.preInsert(tokenModel);
                invoicel.setInvoice_id(UUID.randomUUID().toString());
                invoicel.setPublicexpenseid(spublicexpenseid);
                invoicel.setRowindex(rowundex);
                invoicemapper.insertSelective(invoicel);
            }
        }


        // 付款方式为网上银行付款，个人账户，转账支票做以下处理
//        if ("PJ004001".equals(publicExpense.getPaymentmethod()) || "PJ004002".equals(publicExpense.getPaymentmethod()) || "PJ004003".equals(publicExpense.getPaymentmethod())) {
        TotalCost totalCost = new TotalCost();
        totalCost.setPublicexpenseid(spublicexpenseid);
        totalCostMapper.delete(totalCost);
        saveTotalCostList(invoiceNo, invoicelist, trafficlist, purchaselist, otherlist, publicExpenseVo, tokenModel, spublicexpenseid);
//        }

        //采购申请精算完成之后，发待办
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(publicExpense.getJudgement_name())) {
            if (publicExpense.getJudgement_name().substring(0, 2).equals("CG") && publicExpense.getStatus().equals("4")) {
                String[] pur = publicExpense.getJudgement_name().split(",");
                for (String p : pur) {
                    Purchase pu = new Purchase();
                    pu.setPurnumbers(p);
                    List<Purchase> purchaseList = purchaseMapper.select(pu);
                    if (purchaseList.size() > 0) {
                        purchaseList.get(0).setActuarialdate(new Date());
//                        if (purchaseList.get(0).getActuarialamount() != null && !purchaseList.get(0).getActuarialamount().equals("")) {
//                            purchaseList.get(0).setActuarialamount(String.valueOf(Double.valueOf(purchaseList.get(0).getActuarialamount()) + Double.valueOf(publicExpense.getMoneys())));
//                        } else {
                        purchaseList.get(0).setActuarialamount(publicExpense.getMoneys());
//                        }

                        purchaseList.get(0).preUpdate(tokenModel);
                        purchaseMapper.updateByPrimaryKey(purchaseList.get(0));

                        ToDoNotice toDoNotice = new ToDoNotice();
                        toDoNotice.setTitle("【有采购申请需您维护资产信息】");
                        toDoNotice.setInitiator(purchaseList.get(0).getUser_id());
                        toDoNotice.setContent("有一个采购申请已经精算完成，请维护资产相关信息！");
                        toDoNotice.setUrl("/PFANS3005FormView");
                        toDoNotice.setWorkflowurl("/PFANS3005View");
                        toDoNotice.setDataid(purchaseList.get(0).getPurchase_id());

                        if(purchaseList.get(0).getProcurementproject() != ""){
                            if(purchaseList.get(0).getProcurementproject() == "PJ005005"
                                    || purchaseList.get(0).getProcurementproject() == "PJ005006"
                                    || purchaseList.get(0).getProcurementproject() == "PJ005020"
                                ){
                                //IT
                                List<MembersVo> rolelist1 = roleService.getMembers("5f8820dc82e49438647c60ff");
                                if (rolelist1.size() > 0) {
                                    for (int t = 0; t < rolelist1.size(); t++) {
                                        toDoNotice.setOwner(rolelist1.get(t).getUserid());
                                        List<ToDoNotice> existTnList_It = todoNoticeMapper.select(toDoNotice);
                                        if(existTnList_It.size() == 0){
                                            toDoNotice.preInsert(tokenModel);
                                            toDoNoticeService.save(toDoNotice);
                                        }
                                    }
                                }
                            }else{
                                //财务担当
                                List<MembersVo> rolelist = roleService.getMembers("5f88210582e49438647c6100");
                                if (rolelist.size() > 0) {
                                    for (int i = 0; i < rolelist.size(); i++) {
                                        toDoNotice.setOwner(rolelist.get(i).getUserid());
                                        List<ToDoNotice> existTnList_Fe = todoNoticeMapper.select(toDoNotice);
                                        if(existTnList_Fe.size() == 0){
                                            toDoNotice.preInsert(tokenModel);
                                            toDoNoticeService.save(toDoNotice);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void writeOff(PublicExpenseVo publicExpenseVo,TokenModel tokenModel) throws Exception {
        Calendar cal = Calendar.getInstance();
        String this_year = String.valueOf(cal.get(cal.YEAR));
        String month = String.valueOf(cal.get(cal.MONTH) + 1);
        if(Integer.parseInt(month) < 4)
        {
            this_year = String.valueOf(Integer.parseInt(this_year) - 1);
        }
        OrgTree newOrgInfo = orgTreeService.get(new OrgTree());
        List<TrafficDetails> trafficdetails = publicExpenseVo.getTrafficdetails().stream()
                .filter(traff -> !("").equals(traff.getDepartmentname()) && (!("0").equals(traff.getRmb())
                || !("0").equals(traff.getTormb())) ).collect(Collectors.toList());
        List<PurchaseDetails> purchaseDetails = publicExpenseVo.getPurchasedetails().stream()
                .filter(purs -> !("").equals(purs.getDepartmentname()) && (!("0").equals(purs.getRmb())
                        || !("0").equals(purs.getTormb()))).collect(Collectors.toList());
        List<OtherDetails> otherDetails = publicExpenseVo.getOtherdetails().stream()
                .filter(oter -> !("").equals(oter.getDepartmentname()) && (!("0").equals(oter.getRmb())
                        || !("0").equals(oter.getTormb()))).collect(Collectors.toList());
        if(trafficdetails.size() > 0){
            String finalThis_year2 = this_year;
            trafficdetails.forEach(traf ->{
                //组织变更后需要确认
                OrgTree orginfo = null;
                String depart = "";
                try {
                    orginfo = orgTreeService.getOrgInfo(newOrgInfo, traf.getDepartmentname());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (orginfo.getEffective() != null) {
                    if(orginfo.getEffective()){
                        depart = orginfo.get_id();
                    }else{
                        depart = orginfo.getParent_id();
                    }
                }
                if(new BigDecimal(traf.getRmb()).compareTo(BigDecimal.ZERO) > 1){
                    try {
                        businessplanService.woffRulingInfoAnt(traf.getRmb(), traf.getPlsummary(), finalThis_year2, depart, tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(new BigDecimal(traf.getTormb()).compareTo(BigDecimal.ZERO) > 1){
                    try {
                        businessplanService.woffRulingInfoAnt(traf.getTormb(), traf.getPlsummary(), finalThis_year2, depart, tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if(purchaseDetails.size() > 0){
            String finalThis_year1 = this_year;
            purchaseDetails.forEach(purch ->{
                //组织变更后需要确认
                OrgTree orginfo = null;
                String depart = "";
                try {
                    orginfo = orgTreeService.getOrgInfo(newOrgInfo, purch.getDepartmentname());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (orginfo.getEffective() != null) {
                    if(orginfo.getEffective()){
                        depart = orginfo.get_id();
                    }else{
                        depart = orginfo.getParent_id();
                    }
                }
                if(new BigDecimal(purch.getRmb()).compareTo(BigDecimal.ZERO) > 1){
                    try {
                        businessplanService.woffRulingInfoAnt(purch.getRmb(), purch.getPlsummary(), finalThis_year1, depart, tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(new BigDecimal(purch.getTormb()).compareTo(BigDecimal.ZERO) > 1){
                    try {
                        businessplanService.woffRulingInfoAnt(purch.getTormb(), purch.getPlsummary(), finalThis_year1, depart, tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if(otherDetails.size() > 0){
            String finalThis_year = this_year;
            otherDetails.forEach(oter ->{
                //组织变更后需要确认
                OrgTree orginfo = null;
                String depart = "";
                try {
                    orginfo = orgTreeService.getOrgInfo(newOrgInfo, oter.getDepartmentname());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (orginfo.getEffective() != null) {
                    if(orginfo.getEffective()){
                        depart = orginfo.get_id();
                    }else{
                        depart = orginfo.getParent_id();
                    }
                }
                if(new BigDecimal(oter.getRmb()).compareTo(BigDecimal.ZERO) > 1){
                    try {
                        businessplanService.woffRulingInfoAnt(oter.getRmb(), oter.getPlsummary(), finalThis_year, depart, tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(new BigDecimal(oter.getTormb()).compareTo(BigDecimal.ZERO) > 1){
                    try {
                        businessplanService.woffRulingInfoAnt(oter.getTormb(), oter.getPlsummary(), finalThis_year, depart,tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(publicExpenseVo.getPublicexpense().getJudgement())){
            String judge_id[] = publicExpenseVo.getPublicexpense().getJudgement().split(",");
            PublicExpense publicExpense = new PublicExpense();
            BeanUtils.copyProperties(publicExpenseVo.getPublicexpense(), publicExpense);
            for (String judge : judge_id) {
                if(publicExpense.getJudgement_name().substring(0, 2).equals("CG")){//采购
                    Purchase purchase = purchaseMapper.selectByPrimaryKey(judge);
                    if(purchase != null && !com.mysql.jdbc.StringUtils.isNullOrEmpty(purchase.getRulingid())){
                        businessplanService.cgTpReRulingInfo(purchase.getRulingid(),purchase.getTotalamount(),tokenModel);
                    }
                }
                else if (publicExpense.getJudgement_name().substring(0, 3).equals("JJF")){//交际费
                    Communication communication = communicationMapper.selectByPrimaryKey(judge);
                    if(communication != null && !com.mysql.jdbc.StringUtils.isNullOrEmpty(communication.getRulingid())){
                        businessplanService.cgTpReRulingInfo(communication.getRulingid(),communication.getMoneys(),tokenModel);
                    }
                }
                else if (publicExpense.getJudgement_name().substring(0, 2).equals("JC")){//其他业务决裁
                    Judgement judgement = judgementMapper.selectByPrimaryKey(judge);
                    if(judgement != null && !com.mysql.jdbc.StringUtils.isNullOrEmpty(judgement.getRulingid()) && judgement.getMusectosion().equals("0")){
                        businessplanService.cgTpReRulingInfo(judgement.getRulingid(),judgement.getMoney(),tokenModel);
                    }else if(judgement != null &&  judgement.getMusectosion().equals("1")){
                        Judgementdetail judgementdetail = new Judgementdetail();
                        judgementdetail.setJudgementid(judgement.getJudgementid());
                        List<Judgementdetail> judgementdetails = judgementdetailMapper.select(judgementdetail);
                        if(judgementdetails.size() > 0){
                            for(Judgementdetail jud : judgementdetails){
                                businessplanService.cgTpReRulingInfo(jud.getRulingid(),jud.getAmounttobegivenM(),tokenModel);
                            }
                        }
                    }
                }
                else if (publicExpense.getJudgement_name().substring(0, 2).equals("QY")){//千元以下
                    PurchaseApply purchaseApply = purchaseapplyMapper.selectByPrimaryKey(judge);
                    if(purchaseApply != null && !com.mysql.jdbc.StringUtils.isNullOrEmpty(purchaseApply.getRulingid())){
                        businessplanService.cgTpReRulingInfo(purchaseApply.getRulingid(),purchaseApply.getSummoney(),tokenModel);
                    }
                }
            }
            if(publicExpense.getJudgement_name().substring(0,2).equals("WF") ||
                    publicExpense.getJudgement_name().substring(0,2).equals("FG") ||
                    publicExpense.getJudgement_name().substring(0,2).equals("NF") ||
                    publicExpense.getJudgement_name().substring(0,2).equals("NH")
            ){//委托合同
                String juStr[] = publicExpense.getJudgement().split(",");
                List<String> judList = new ArrayList<String>();
                for (int i = 0; i < juStr.length; i++) {
                    if (!judList.contains(juStr[i])) {
                        judList.add(juStr[i]);
                    }
                }
//                if(judList.size() > 0){
//                    for(String aware : judList){
//                        AwardDetail awardDetail = new AwardDetail();
//                        awardDetail.setAward_id(aware);
//                        List<AwardDetail> awardDetails = awardDetailMapper.select(awardDetail);
//                        if(awardDetails.size() > 0){
//                            for(AwardDetail aDetail : awardDetails){
//                                if(!aDetail.getRulingid().equals("")){
//                                    businessplanService.cgTpReRulingInfo(aDetail.getRulingid(),aDetail.getAwardmoney(),tokenModel);
//                                }
//                            }
//                        }
//                    }
//                }
                if(judList.size() > 0){
                    for(String aware : judList){
                        Award award = new Award();
                        award.setAward_id(aware);
                        List<Award> awardList = awardMapper.select(award);
                        if(awardList.size() > 0){
                            String awrStr[] = awardList.get(0).getStatuspublic().split(",");
                            Contractnumbercount contnub = new Contractnumbercount();
                            contnub.setContractnumber(awardList.get(0).getContractnumber());
                            List<Contractnumbercount> cnbList = contractnumbercountMapper.select(contnub);
                            if(awrStr.length == cnbList.size()){
                                AwardDetail awardDetail = new AwardDetail();
                                awardDetail.setAward_id(aware);
                                List<AwardDetail> awardDetails = awardDetailMapper.select(awardDetail);
                                if(awardDetails.size() > 0){
                                    for(AwardDetail aDetail : awardDetails){
                                        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(aDetail.getRulingid())){
                                            businessplanService.cgTpReRulingInfo(aDetail.getRulingid(),aDetail.getAwardmoney(),tokenModel);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //按id查询

    @Override
    public PublicExpenseVo selectById(String publicexpenseid) throws Exception {
        PublicExpenseVo pubVo = new PublicExpenseVo();
        TrafficDetails trafficDetails = new TrafficDetails();
        PurchaseDetails purchaseDetails = new PurchaseDetails();
        OtherDetails otherDetails = new OtherDetails();
        Invoice invoice = new Invoice();
        trafficDetails.setPublicexpenseid(publicexpenseid);
        purchaseDetails.setPublicexpenseid(publicexpenseid);
        otherDetails.setPublicexpenseid(publicexpenseid);
        invoice.setPublicexpenseid(publicexpenseid);
        List<TrafficDetails> trafficDetailslist = trafficDetailsMapper.select(trafficDetails);
        List<PurchaseDetails> purchaseDetailslist = purchaseDetailsMapper.select(purchaseDetails);
        List<OtherDetails> otherDetailslist = otherDetailsMapper.select(otherDetails);
        List<Invoice> invoicelist = invoicemapper.select(invoice);
        trafficDetailslist = trafficDetailslist.stream().sorted(Comparator.comparing(TrafficDetails::getRowindex)).collect(Collectors.toList());
        purchaseDetailslist = purchaseDetailslist.stream().sorted(Comparator.comparing(PurchaseDetails::getAnnexno)).collect(Collectors.toList());
        otherDetailslist = otherDetailslist.stream().sorted(Comparator.comparing(OtherDetails::getAnnexno)).collect(Collectors.toList());
        invoicelist = invoicelist.stream().sorted(Comparator.comparing(Invoice::getInvoicenumber)).collect(Collectors.toList());
        PublicExpense pub = publicExpenseMapper.selectByPrimaryKey(publicexpenseid);
        pubVo.setPublicexpense(pub);
        pubVo.setTrafficdetails(trafficDetailslist);
        pubVo.setPurchasedetails(purchaseDetailslist);
        pubVo.setOtherdetails(otherDetailslist);
        pubVo.setInvoice(invoicelist);
        return pubVo;
    }

    public Map<String, String> getworkfolwPurchaseData(PublicExpense publicExpense) throws Exception {
        Map<String, String> getpurchaseMap = new HashMap<String, String>();
        Map<String, String> getpurchaseMapsub = new HashMap<String, String>();
        publicExpense = publicExpenseMapper.selectByPrimaryKey(publicExpense.getPublicexpenseid());
        if (publicExpense.getJudgement_name() != null && !publicExpense.getJudgement_name().equals("")) {
            String[] pusname = publicExpense.getJudgement_name().split(",");
            String[] pusid = publicExpense.getJudgement().split(",");
            if (pusname.length > 0) {
                for (int i = 0; i < pusname.length; i++) {
                    if (pusname[i].substring(0, 2).equals("CG")) {
                        Purchase purchase = new Purchase();
                        purchase.setPurnumbers(pusname[i]);
                        List<Purchase> purchaseList = new ArrayList<Purchase>();
                        purchaseList = purchaseMapper.select(purchase);
                        if (purchaseList.size() > 0) {
                            //采购决裁
                            if (getpurchaseMap.containsKey("purchase")) {
                                String val = getpurchaseMap.get("purchase") + ";" + purchaseList.get(0).getPurchase_id() + "," + purchaseList.get(0).getStatus();
                                getpurchaseMap.put("purchase", val);
                            } else {
                                getpurchaseMap.put("purchase", purchaseList.get(0).getPurchase_id() + "," + purchaseList.get(0).getStatus());
                            }
                            //精算
                            if (getpurchaseMap.containsKey("publicExpense")) {
                                String val = getpurchaseMap.get("publicExpense") + ";" + publicExpense.getPublicexpenseid() + "," + publicExpense.getStatus();
                                getpurchaseMap.put("publicExpense", val);
                            } else {
                                getpurchaseMap.put("publicExpense", publicExpense.getPublicexpenseid() + "," + publicExpense.getStatus());
                            }
                        }
                    }
                }
            }
        }
        return getpurchaseMap;
    }

    //region   add  ml  220112  检索  from
    @Override
    public List<PublicExpense> getSearch(PublicExpense publicExpense) throws Exception {
        return publicExpenseMapper.getSearch(publicExpense);
    }
    //endregion   add  ml  220112  检索  to

}
