package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TotalCostVo;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS3000.mapper.PurchaseMapper;
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

import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private PublicExpenseMapper publicExpenseMapper;

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
            float rmb = getPropertyFloat(o, "rmb");
            float foreigncurrency = getPropertyFloat(o, "foreigncurrency");
            DecimalFormat df = new DecimalFormat("#0.00");
            int scale = 2;//设置位数
            int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
            BigDecimal bd = new BigDecimal(rmb);
            bd = bd.setScale(scale, roundingMode);
            BigDecimal bd1 = new BigDecimal(foreigncurrency);
            bd1 = bd1.setScale(scale, roundingMode);
            if (accountcode.equals("PJ119004") || accountcode.equals("PJ132004")) {
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
            } else if (plsummary.equals("PJ111010")) {
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
        return publicExpenseMapper.select(publicExpense);
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
        if (publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate())) != null) {
            int count = publicExpenseMapper.getInvoiceNo(sdf.format(publicExpenseVo.getPublicexpense().getReimbursementdate()));
            no = String.format("%2d", count + 1).replace(" ", "0");
        } else {
            no = "01";
        }

        String month1 = String.format("%2d", month).replace(" ", "0");
        String day1 = String.format("%2d", day).replace(" ", "0");
        invoiceNo = "DL4AP" + year + month1 + day1 + no;

        String publicexpenseid = UUID.randomUUID().toString();
        PublicExpense publicExpense = new PublicExpense();
        BeanUtils.copyProperties(publicExpenseVo.getPublicexpense(), publicExpense);
        publicExpense.setInvoiceno(invoiceNo);
        publicExpense.preInsert(tokenModel);
        publicExpense.setPublicexpenseid(publicexpenseid);
        publicExpenseMapper.insertSelective(publicExpense);

        //add ccm 0727
        if(publicExpense.getJudgement_name().substring(0,2).equals("CG"))
        {
            String []pur = publicExpense.getJudgement_name().split(",");
            for(String p:pur)
            {
                Purchase purchase = new Purchase();
                purchase.setPurnumbers(p);
                List<Purchase> purchaseList = purchaseMapper.select(purchase);
                if(purchaseList.size()>0)
                {
                    purchaseList.get(0).setPublicexpense_id(publicExpense.getPublicexpenseid());
                    purchaseList.get(0).setInvoiceno(publicExpense.getInvoiceno());
                    purchaseList.get(0).preUpdate(tokenModel);
                    purchaseMapper.updateByPrimaryKey(purchaseList.get(0));
                }
                if(publicExpense.getStatus().equals("4"))
                {
                    ToDoNotice toDoNotice = new ToDoNotice();
                    toDoNotice.setTitle("【有采购申请需您维护资产信息】");
                    toDoNotice.setInitiator(purchaseList.get(0).getUser_id());
                    toDoNotice.setContent("有一个采购申请已经精算完成，请维护资产相关信息！");
                    toDoNotice.setDataid(purchaseList.get(0).getPurchase_id());
                    toDoNotice.setUrl("/PFANS3005FormView");
                    toDoNotice.setWorkflowurl("/PFANS3005View");
                    toDoNotice.preInsert(tokenModel);
                    //财务担当
                    List<MembersVo> rolelist = roleService.getMembers("5e78645a8f4316308435113c");
                    if(rolelist.size()>0)
                    {
                        toDoNotice.setOwner(rolelist.get(0).getUserid());
                    }
                    toDoNoticeService.save(toDoNotice);

                    //IT
                    List<MembersVo> rolelist1 = roleService.getMembers("5e78630d8f43163084351137");
                    if(rolelist1.size()>0)
                    {
                        toDoNotice.setOwner(rolelist1.get(0).getUserid());
                    }
                    toDoNoticeService.save(toDoNotice);
                }

            }
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
        for (int i = 0; i < 26; i++) {
            List<com.nt.dao_Org.Dictionary> dictionaryListAccount = dictionaryService.getForSelect("PJ" + (112 + i));
            for (Dictionary d : dictionaryListAccount) {
                accountCodeMap.put(d.getCode(), d.getValue1());
            }
        }

        Map<String, Object> mergeResult = null;
        Map<String, Float> specialMap = new HashMap<>();
        for (Invoice invoice : invoicelist) {
            if (SPECIAL_KEY.equals(invoice.getInvoicetype()) && (!"0".equals(invoice.getInvoiceamount()))) {
                // 专票，获取税率
                float rate = getFloatValue(taxRateMap.getOrDefault(invoice.getTaxrate(), ""));
                if (publicExpenseVo.getPublicexpense().getType() == "PJ001002") {
                    if (rate <= 0) {
                        throw new LogicalException("专票税率不能为0");
                    }
                }
                specialMap.put(invoice.getInvoicenumber(), rate);
            }
            // 合计发票金额（含税）
//            specialMap.put(TOTAL_TAX,
//                    specialMap.getOrDefault(TOTAL_TAX, 0f) + getFloatValue(invoice.getInvoiceamount()));
        }
        // 总金额改为人民币支出
        specialMap.put(TOTAL_TAX, Float.parseFloat(publicExpenseVo.getPublicexpense().getRmbexpenditure()) + Float.parseFloat(publicExpenseVo.getPublicexpense().getForeigncurrency()));
        if (specialMap.getOrDefault(TOTAL_TAX, 0f) <= 0) {
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
                    insertInfo.setRemark(userName + accountCodeMap.getOrDefault(insertInfo.getRemark(), ""));
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
    private Map<String, Object> mergeDetailList(List<Object> detailList, final Map<String, Float> specialMap) throws LogicalException {
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
                float rate = specialMap.get(keyNo);
                TotalCost taxCost = new TotalCost();

                // 税拔
                String lineCost = FNUM.format(money / (1 + rate));
                // 税金
                String lineRate = FNUM.format(gettaxes);
                String lineRateNo = FNUM.format(money / (1 + rate) * rate);
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
                    float diff = Float.parseFloat(lineCost) + Float.parseFloat(lineRateNo) - money;
                    if (diff != 0) {
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
        if (totalTax != specialMap.get(TOTAL_TAX)) {
            throw new LogicalException("发票合计金额与明细不匹配。");
        }
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
        PublicExpense publicExpense = new PublicExpense();
        BeanUtils.copyProperties(publicExpenseVo.getPublicexpense(), publicExpense);
        //add-ws-7/20-禅道任务342
        String status = publicExpense.getStatus();
        String judgement = publicExpense.getJudgement();
        //add-ws-7/20-禅道任务342
        publicExpense.preUpdate(tokenModel);
        publicExpenseMapper.updateByPrimaryKey(publicExpense);
        //add-ws-7/20-禅道任务342
        String[] ts = judgement.split(",");
        if (ts.length > 0) {
            for (int i = 0; i < ts.length; i++) {
                Award award = new Award();
                award.setAward_id(ts[i]);
                Award awa = awardMapper.selectByPrimaryKey(award);
                if (awa != null) {
                    awa.setStatuspublic(status);
                    awardMapper.updateByPrimaryKey(awa);
                    if (status.equals("4")) {
                        List<MembersVo> rolelist = roleService.getMembers("5e78633d8f43163084351138");
                        if (rolelist.size() > 0) {
                            ToDoNotice toDoNotice3 = new ToDoNotice();
                            toDoNotice3.setTitle("【" + awa.getContractnumber() + "】发起得精算申请已成功");
                            toDoNotice3.setInitiator(awa.getUser_id());
                            toDoNotice3.setContent("流程结束。可进行线下支付");
                            toDoNotice3.setDataid(awa.getContractnumber());
                            toDoNotice3.setUrl("/PFANS1025FormView");
                            toDoNotice3.setWorkflowurl("/PFANS1025FormView");
                            toDoNotice3.preInsert(tokenModel);
                            toDoNotice3.setOwner(rolelist.get(0).getUserid());
                            toDoNoticeService.save(toDoNotice3);
                        }
                    }
                }
            }
        }
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

}
