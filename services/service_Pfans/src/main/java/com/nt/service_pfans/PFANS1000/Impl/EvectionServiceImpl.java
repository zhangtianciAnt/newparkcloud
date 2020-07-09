package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.EvectionVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TravelCostVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.EvectionService;
import com.nt.service_pfans.PFANS1000.mapper.*;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EvectionServiceImpl implements EvectionService {

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
    private static final String CURRENCY_KEY = "__CURRENCY_KEY__";      //外币
    private static final String INPUT_TYPE_KEY = "__INPUT_TYPE_KEY__";
    private static final DecimalFormat FNUM = new DecimalFormat("##0.00");
    private static final String FIELD_RMB = "rmb";
    private static final String FIELD_FOREIGNCURRENCY = "foreigncurrency";
    private static final String FIELD_INVOICENUMBER = "invoicenumber";
    @Autowired
    private EvectionService evectionService;
    @Autowired
    private EvectionMapper evectionMapper;
    @Autowired
    private TrafficDetailsMapper trafficdetailsMapper;
    @Autowired
    private AccommodationDetailsMapper accommodationdetailsMapper;
    @Autowired
    private OtherDetailsMapper otherdetailsMapper;
    @Autowired
    private InvoiceMapper invoicemapper;
    @Autowired
    private CurrencyexchangeMapper currencyexchangeMapper;
    @Autowired
    private TravelCostMapper travelcostmapper;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Map<String, Object> exportjs(String evectionid, HttpServletRequest request) throws Exception {
        EvectionVo evevo = evectionService.selectById(evectionid);
        Map<String, Object> resultMap = new HashMap<>();
        List<AccommodationDetails> acclist = evevo.getAccommodationdetails();
        List<TrafficDetails> tralist = evevo.getTrafficdetails();
        List<OtherDetails> otherlist = evevo.getOtherdetails();
        List<Object> needMergeList = new ArrayList<>();
        if (acclist.size() > 0 || tralist.size() > 0
                || otherlist.size() > 0) {
            needMergeList.addAll(acclist);
            needMergeList.addAll(tralist);
            needMergeList.addAll(otherlist);
        }
//        List<TrafficDetails> traffic = new ArrayList<>();
//        List<AccommodationDetails> accommodation = new ArrayList<>();
//        List<OtherDetails> other = new ArrayList<>();
        Map<String, Object> oldmergeResult = null;
        oldmergeResult = oldmergeDetailList(needMergeList);
        List<TrafficDetails> traffic = (List<TrafficDetails>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
        List<AccommodationDetails> accommodation = (List<AccommodationDetails>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
        List<OtherDetails> other = (List<OtherDetails>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
        for (Object o : oldmergeResult.values()) {
            String accountcode = getProperty(o, "accountcode");
            String budgetcoding = getProperty(o, "budgetcoding");
            String currency = getProperty(o, "currency");
            String subjectnumber = getProperty(o, "subjectnumber");
            String redirict = getProperty(o, "redirict");
            float rmb = getPropertyFloat(o, "rmb");
            float subsidies = getPropertyFloat(o, "subsidies");
            float foreigncurrency = getPropertyFloat(o, "foreigncurrency");
            float travel = getPropertyFloat(o, "travel");
            DecimalFormat df = new DecimalFormat("#0.00");
            int scale = 2;//设置位数
            int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
            BigDecimal bd = new BigDecimal(rmb);
            bd = bd.setScale(scale, roundingMode);
            BigDecimal bd1 = new BigDecimal(foreigncurrency);
            bd1 = bd1.setScale(scale, roundingMode);
            BigDecimal bd2 = new BigDecimal(travel);
            bd2 = bd2.setScale(scale, roundingMode);
            BigDecimal bd3 = new BigDecimal(subsidies);
            bd3 = bd3.setScale(scale, roundingMode);
            if (accountcode.equals("PJ132001") || accountcode.equals("PJ119001")) {
                AccommodationDetails accommodationdetails = new AccommodationDetails();
                resultMap.put("住宿费", accommodation);
                List<Dictionary> dictionaryL = dictionaryService.getForSelect("PJ119");
                String value1 = dictionaryL.get(4).getValue2();
                List<Dictionary> dictionary = dictionaryService.getForSelect("PJ132");
                String value2 = dictionary.get(4).getValue2();
                List<Dictionary> dictionaryList = dictionaryService.getForSelect("PJ119");
                String value3 = dictionary.get(4).getValue1();
                accommodationdetails.setAnnexno(value3);
                if (redirict.equals("0")) {
                    accommodationdetails.setRedirict(value1);
                } else if (redirict.equals("1")) {
                    accommodationdetails.setRedirict(value2);
                }
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(currency)) {
                        accommodationdetails.setCurrency(iteA.getValue1());
                    }
                }
                List<Dictionary> curListT = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : curListT) {
                    if (ite.getCode().equals(budgetcoding)) {
                        accommodationdetails.setBudgetcoding(ite.getValue2() + "_" + ite.getValue3());
                    }
                }
                accommodationdetails.setSubjectnumber(subjectnumber);
                if (accountcode.length() > 5) {
                    String traAccountcode = accountcode.substring(0, 5);
                    List<Dictionary> curListA = dictionaryService.getForSelect(traAccountcode);
                    for (Dictionary iteA : curListA) {
                        if (iteA.getCode().equals(accountcode)) {
                            accommodationdetails.setAccountcode(iteA.getValue1());
                        }
                    }
                }
                accommodationdetails.setTravel(String.valueOf(bd2));
                accommodationdetails.setRmb(String.valueOf(bd));
                accommodationdetails.setSubsidies(String.valueOf(bd3));
                accommodation.add(accommodationdetails);
            } else if (accountcode.equals("PJ132002") || accountcode.equals("PJ119002")) {
                TrafficDetails trafficdetails = new TrafficDetails();
                resultMap.put("交通费", traffic);
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(currency)) {
                        trafficdetails.setCurrency(iteA.getValue1());
                    }
                }
                trafficdetails.setSubjectnumber(subjectnumber);
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
                trafficdetails.setForeigncurrency(String.valueOf(bd1));
                trafficdetails.setRmb(String.valueOf(bd));
                traffic.add(trafficdetails);
            } else if (accountcode.equals("PJ132007") || accountcode.equals("PJ119007")) {
                OtherDetails otherdetails = new OtherDetails();
                resultMap.put("其他费用", other);
                List<Dictionary> curListAc = dictionaryService.getForSelect("PG019");
                for (Dictionary iteA : curListAc) {
                    if (iteA.getCode().equals(currency)) {
                        otherdetails.setCurrency(iteA.getValue1());
                    }
                }
                otherdetails.setSubjectnumber(subjectnumber);
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
            String currency = getProperty(detail, "currency");
            String accountcode = getProperty(detail, "accountcode");
            String mergeKey = "";
            mergeKey = budgetcoding + " ... " + subjectnumber + " ... " + currency;
            // 行合并
            float money = getPropertyFloat(detail, "rmb");
            float moneysum = getPropertyFloat(detail, "foreigncurrency");
            float taxes = getPropertyFloat(detail, "travel");
            float subsidies = getPropertyFloat(detail, "subsidies");
            Object mergeObject = resultMap.get(mergeKey);
            if (mergeObject != null) {
                // 发现可以合并数据
                float newMoney = getPropertyFloat(mergeObject, "rmb") + money;
                float newMoneysum = getPropertyFloat(mergeObject, "foreigncurrency") + moneysum;
                float oldMoneysum = getPropertyFloat(mergeObject, "travel") + taxes;
                float oldsubsidies = getPropertyFloat(mergeObject, "subsidies") + subsidies;
                setProperty(mergeObject, "rmb", newMoney + "");
                setProperty(mergeObject, "foreigncurrency", newMoneysum + "");
                setProperty(mergeObject, "travel", oldMoneysum + "");
                setProperty(mergeObject, "subsidies", oldsubsidies + "");
            } else {
                resultMap.put(mergeKey, detail);
            }
        }
        return resultMap;
    }

    @Override
    public List<Evection> get(Evection evection) throws Exception {
        return evectionMapper.select(evection);
    }

    @Override
    public List<TravelCost> gettravelcost(TravelCostVo travelcostvo) throws Exception {
        List<TravelCost> Listvo = new ArrayList<TravelCost>();
        TravelCost travelcost = new TravelCost();
        List<TravelCost> travelcostlist = travelcostvo.getTravelcost();
        for (TravelCost travelList : travelcostlist) {
            travelcost.setEvectionid(travelList.getEvectionid());
            List<TravelCost> ListVo = travelcostmapper.select(travelcost);
            ListVo = ListVo.stream().sorted(Comparator.comparing(TravelCost::getNumber)).collect(Collectors.toList());
            Listvo.addAll(0, ListVo);

//            Currencyexchange currencyexchange = new Currencyexchange();
//            currencyexchange.setEvectionid(travelList.getEvectionid());
//            List<Currencyexchange> lscer = currencyexchangeMapper.select(currencyexchange);
//            if (lscer != null && lscer.size() > 0) {
//                TravelCost tc = new TravelCost();
//                String st = null;
//                Double ctsum = 0D;
//                for (Currencyexchange item : lscer) {
//                    Double ct = 0D;
//                    Double diff = Convert.toDouble(item.getCurrencyexchangerate()) - Convert.toDouble(item.getExchangerate());
//                    if (diff == 0) {
//                        continue;
//                    }
//                    BeanUtil.copyProperties(ListVo.get(0), tc);
//                    tc.setNumber(ListVo.size() + 1);
//                    tc.setBudgetcoding("000000");
//                    List<Dictionary> dictionaryList = dictionaryService.getForSelect("PG024");
//                    String value1 = dictionaryList.get(0).getValue2();
//                    String value2 = dictionaryList.get(1).getValue2();
//                    if (diff < 0) {
//                        tc.setSubjectnumber(value2);
//                    } else {
//                        tc.setSubjectnumber(value1);
//                    }
//
//                    List<Double> costs = trafficdetailsMapper.getCount(travelList.getEvectionid(), item.getCurrency());
//                    for (Double cost : costs) {
//                        ct += cost;
//                    }
//                    ctsum += ct * diff;
//                }
//                DecimalFormat df = new DecimalFormat(".##");
//                st = df.format(ctsum);
//                tc.setLineamount(Convert.toStr(st));
//                Listvo.add(tc);
//            }
        }
        return Listvo;
    }

    @Override
    public EvectionVo selectById(String evectionid) throws Exception {
        EvectionVo eveVo = new EvectionVo();
        TrafficDetails trafficdetails = new TrafficDetails();
        AccommodationDetails accommodationdetails = new AccommodationDetails();
        OtherDetails otherdetails = new OtherDetails();
        Invoice invoice = new Invoice();
        Currencyexchange currencyexchange = new Currencyexchange();
        trafficdetails.setEvectionid(evectionid);
        accommodationdetails.setEvectionid(evectionid);
        otherdetails.setEvectionid(evectionid);
        invoice.setEvectionid(evectionid);
        currencyexchange.setEvectionid(evectionid);
        List<TrafficDetails> trafficdetailslist = trafficdetailsMapper.select(trafficdetails);
        List<AccommodationDetails> accommodationdetailslist = accommodationdetailsMapper.select(accommodationdetails);
        List<OtherDetails> otherdetailslist = otherdetailsMapper.select(otherdetails);
        List<Invoice> invoicelist = invoicemapper.select(invoice);
        List<Currencyexchange> currencyexchangeList = currencyexchangeMapper.select(currencyexchange);
        trafficdetailslist = trafficdetailslist.stream().sorted(Comparator.comparing(TrafficDetails::getRowindex)).collect(Collectors.toList());
        accommodationdetailslist = accommodationdetailslist.stream().sorted(Comparator.comparing(AccommodationDetails::getRowindex)).collect(Collectors.toList());
        otherdetailslist = otherdetailslist.stream().sorted(Comparator.comparing(OtherDetails::getRowindex)).collect(Collectors.toList());
        invoicelist = invoicelist.stream().sorted(Comparator.comparing(Invoice::getInvoicenumber)).collect(Collectors.toList());
        currencyexchangeList = currencyexchangeList.stream().sorted(Comparator.comparing(Currencyexchange::getRowindex)).collect(Collectors.toList());
        Evection Eve = evectionMapper.selectByPrimaryKey(evectionid);
        eveVo.setEvection(Eve);
        eveVo.setTrafficdetails(trafficdetailslist);
        eveVo.setAccommodationdetails(accommodationdetailslist);
        eveVo.setOtherdetails(otherdetailslist);
        eveVo.setInvoice(invoicelist);
        eveVo.setCurrencyexchanges(currencyexchangeList);
        return eveVo;
    }

    //    saveTotalCostList(trafficdetailslist, accommodationdetailslist, otherdetailslist, invoicelist, currencyexchangeList, evectionVo, tokenModel, evectionid);
    private void saveTravelCostList(String invoiceNo, List<TrafficDetails> trafficDetailslist, List<AccommodationDetails> accommodationdetailslist, List<OtherDetails> otherDetailslist, List<Invoice> invoicelist,
                                    List<Currencyexchange> currencyexchangeList, EvectionVo evectionVo, TokenModel tokenModel, String evectionid) throws Exception {
        // 发票日期，条件日期
        Date date = new Date();
        SimpleDateFormat myFormatter = new SimpleDateFormat("ddMMMyyyy", Locale.ENGLISH);
        date = myFormatter.parse(myFormatter.format(date));
        //通过字典查取税率
        List<com.nt.dao_Org.Dictionary> dictionaryList = dictionaryService.getForSelect("PJ071");
        Map<String, String> taxRateMap = new HashMap<>();
        for (com.nt.dao_Org.Dictionary d : dictionaryList) {
            taxRateMap.put(d.getCode(), d.getValue1());
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
        //add-ws-5/12-汇税收益与汇税损失问题对应
        Map<String, Object> newmergeResult = null;
        //add-ws-5/12-汇税收益与汇税损失问题对应
        Map<String, Float> specialMap = new HashMap<>();
        for (Invoice invoice : invoicelist) {
            if (Double.valueOf(invoice.getInvoiceamount()) > 0.0) {
                // 专票，获取税率
                float rate = getFloatValue(taxRateMap.getOrDefault(invoice.getTaxrate(), ""));
                if (rate <= 0) {
                    throw new LogicalException("专票税率不能为0");
                }
                specialMap.put(invoice.getInvoicenumber(), rate);
            }
        }
        // 总金额改为人民币支出
        specialMap.put(TOTAL_TAX, Float.parseFloat(evectionVo.getEvection().getTotalpay()));
        if (specialMap.getOrDefault(TOTAL_TAX, 0f) <= 0) {
            throw new LogicalException("发票合计金额不能为0");
        }

        List<Object> needMergeList = new ArrayList<>();
        if (trafficDetailslist.size() > 0 || accommodationdetailslist.size() > 0
                || otherDetailslist.size() > 0) {
            needMergeList.addAll(trafficDetailslist);
            needMergeList.addAll(accommodationdetailslist);
            needMergeList.addAll(otherDetailslist);
        }
        mergeResult = mergeDetailList(needMergeList, specialMap, currencyexchangeList);
        //add-ws-5/12-汇税收益与汇税损失问题对应
        newmergeResult = newmergeDetailList(needMergeList, specialMap, currencyexchangeList);
        //add-ws-5/12-汇税收益与汇税损失问题对应
        List<TravelCost> csvList = new ArrayList<>();
        List<TravelCost> taxList = (List<TravelCost>) mergeResult.getOrDefault(TAX_KEY, new ArrayList<>());
        List<TravelCost> newtaxList = (List<TravelCost>) newmergeResult.getOrDefault(TAX_KEY, new ArrayList<>());
        List<TravelCost> paddingList = (List<TravelCost>) mergeResult.getOrDefault(PADDING_KEY, new ArrayList<>());
        for (Object o : mergeResult.values()) {
            if (o instanceof TrafficDetails || o instanceof AccommodationDetails || o instanceof OtherDetails) {
                String money = getProperty(o, "rmb");
                TravelCost cost = new TravelCost();
                //csv只出力RMB
                cost.setCurrency("CNY");
                cost.setLineamount(money);
                cost.setBudgetcoding(getProperty(o, "budgetcoding"));
                cost.setSubjectnumber(getProperty(o, "subjectnumber"));
                //发票说明
                cost.setRemarks(getProperty(o, "accountcode"));
                csvList.add(cost);
            }
        }
        csvList.addAll(taxList);
        csvList.addAll(newtaxList);
        csvList.addAll(paddingList);
        //获取人名
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(evectionVo.getEvection().getUserid()));
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        String userName = "";
        if (customerInfo != null) {
            userName = customerInfo.getUserinfo().getCustomername();
        }

        int rowindex = 0;
        for (TravelCost insertInfo : csvList) {
            if (insertInfo.getLineamount().equals("0")) {
                continue;
            } else {
                rowindex = rowindex + 1;
                insertInfo.preInsert(tokenModel);
                insertInfo.setTravelcost_id(UUID.randomUUID().toString());
                insertInfo.setEvectionid(evectionid);
                insertInfo.setNumber(rowindex);
                //日期格式，取当前日期， 输出CSV时需要格式化成28OCT2019
                insertInfo.setInvoicedate(date);
                insertInfo.setConditiondate(date);
                insertInfo.setVendorcode(evectionVo.getEvection().getPersonalcode());//个人编号
                insertInfo.setInvoiceamount(specialMap.get(TOTAL_TAX).toString());//总金额
                //发票说明
                if (insertInfo.getRemarks() != "" && insertInfo.getRemarks() != null) {
                    insertInfo.setRemarks(userName + accountCodeMap.getOrDefault(insertInfo.getRemarks(), ""));
                }

                insertInfo.setInvoicenumber(invoiceNo);
                travelcostmapper.insertSelective(insertInfo);
            }

        }
    }

    //add-ws-5/12-汇税收益与汇税损失问题对应
    private Map<String, Object> newmergeDetailList(List<Object> detailList, final Map<String, Float> specialMap, List<Currencyexchange> currencyexchangeList) throws Exception {
        Map<String, Object> newresultMap = new HashMap<>();
        if (detailList.size() <= 0) {
            throw new LogicalException("明细不能为空");
        }
        String inputType = getInputType(detailList.get(0));
        for (Object detail : detailList) {
            String isRmb = getProperty(detail, "rmb");
            String currency = getProperty(detail, "currency");

            String keyNo = getProperty(detail, FIELD_INVOICENUMBER);
            String budgetcoding = getProperty(detail, "budgetcoding");
            String subjectnumber = getProperty(detail, "subjectnumber");
            String mergeKey;

            mergeKey = budgetcoding + " ... " + subjectnumber + " ... " + currency;

            // 行合并
            float money = getPropertyFloat(detail, "foreigncurrency");
            float moneysum = getPropertyFloat(detail, "travel");
            Object mergeObject = newresultMap.get(mergeKey);
            if (mergeObject != null) {
                // 发现可以合并数据
                float newMoney = getPropertyFloat(mergeObject, "foreigncurrency") + money;
                float newMoneysum = getPropertyFloat(mergeObject, "travel") + moneysum;
                setProperty(mergeObject, "foreigncurrency", newMoney + "");
                setProperty(mergeObject, "travel", newMoneysum + "");
            } else {
                newresultMap.put(mergeKey, detail);
            }
        }
        float totalTax = 0f;
        List<Object> list = new ArrayList<>(newresultMap.values());
        List<TravelCost> newtaxList = (List<TravelCost>) newresultMap.getOrDefault(TAX_KEY, new ArrayList<>());
        newresultMap.put(TAX_KEY, newtaxList);
        TravelCost newtaxCost = new TravelCost();
        List<Dictionary> dictionaryL = dictionaryService.getForSelect("PG024");
        String value1 = dictionaryL.get(0).getValue2();
        String value2 = dictionaryL.get(1).getValue2();
        float sum = 0f;
        float sum1 = 0f;
        float sum2 = 0f;
        if (currencyexchangeList.size() > 0) {
            for (Currencyexchange listchange : currencyexchangeList) {
                for (Object detail : list) {
                    String currency = getProperty(detail, "currency");
                    if (listchange.getCurrency().equals(currency)) {
                        float exchangerate = Float.valueOf(listchange.getExchangerate());
                        float currencyexchangerate = Float.valueOf(listchange.getCurrencyexchangerate());
                        float foreigncurrency = getPropertyFloat(detail, "foreigncurrency");
                        float travel = getPropertyFloat(detail, "travel");
                        float checkforeigncurrency = 0f;
                        float checktravel = 0f;
                        if (foreigncurrency != 0.0) {
                            checkforeigncurrency = foreigncurrency * exchangerate - foreigncurrency * currencyexchangerate;
//                        if (checkforeigncurrency > 0.0) {
//                            newtaxCost.setSubjectnumber(value2);
//                        } else {
//                            newtaxCost.setSubjectnumber(value1);
//                        }
                        }
                        if (travel != 0.0) {
                            checktravel = travel * exchangerate - travel * currencyexchangerate;
//                        if (checktravel > 0.0) {
//                            newtaxCost.setSubjectnumber(value2);
//                        } else {
//                            newtaxCost.setSubjectnumber(value1);
//                        }
                        }

                        DecimalFormat df = new DecimalFormat("#0.00");
                        int scale = 2;//设置位数
                        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
                        BigDecimal bd = new BigDecimal(checktravel);
                        BigDecimal bd1 = new BigDecimal(checkforeigncurrency);
                        bd = bd.setScale(scale, roundingMode);
                        bd1 = bd1.setScale(scale, roundingMode);
                        sum += bd.floatValue();
                        sum1 += bd1.floatValue();
                        sum2 = sum + sum1;
                        newtaxCost.setBudgetcoding(getProperty(detail, "budgetcoding"));
                        //发票说明
                        newtaxCost.setRemarks(getProperty(detail, "accountcode"));
                        newtaxCost.setCurrency("CNY");
                    }
                }
            }
            if (sum2 > 0.0) {
                newtaxCost.setLineamount(String.valueOf(sum2));
                newtaxCost.setSubjectnumber(value2);
            } else {
                newtaxCost.setLineamount(String.valueOf(sum2));
                newtaxCost.setSubjectnumber(value1);
            }
            newtaxList.add(newtaxCost);
        }
        return newresultMap;
    }
    //add-ws-5/12-汇税收益与汇税损失问题对应


    /**
     * 对明细数据分组
     *
     * @param detailList
     * @return resultMap
     */
    private Map<String, Object> mergeDetailList(List<Object> detailList, final Map<String, Float> specialMap, List<Currencyexchange> currencyexchangeList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (detailList.size() <= 0) {
            throw new LogicalException("明细不能为空");
        }
        String inputType = getInputType(detailList.get(0));
        for (Object detail : detailList) {
            String isRmb = getProperty(detail, "rmb");
            // 发票No
            String keyNo = getProperty(detail, FIELD_INVOICENUMBER);
            String budgetcoding = getProperty(detail, "budgetcoding");
            String subjectnumber = getProperty(detail, "subjectnumber");
            String mergeKey;
            mergeKey = budgetcoding + " ... " + subjectnumber;
            // 行合并
            float money = getPropertyFloat(detail, "rmb");
            float moneysum = getPropertyFloat(detail, "subsidies");
            float taxes = getPropertyFloat(detail, "taxes");
            Object mergeObject = resultMap.get(mergeKey);
            if (mergeObject != null) {
                // 发现可以合并数据
                float newMoney = getPropertyFloat(mergeObject, "rmb") + money;
                float newMoneysum = getPropertyFloat(mergeObject, "subsidies") + moneysum;
                float oldMoneysum = getPropertyFloat(mergeObject, "taxes") + taxes;
                setProperty(mergeObject, "rmb", newMoney + "");
                setProperty(mergeObject, "subsidies", newMoneysum + "");
                setProperty(mergeObject, "taxes", oldMoneysum + "");
            } else {
                resultMap.put(mergeKey, detail);
            }

        }

        float totalTax = 0f;
        List<Object> list = new ArrayList<>(resultMap.values());
        DecimalFormat df1 = new DecimalFormat("######0.00");
        for (Object detail : list) {
            // 发票No
            String keyNo = getProperty(detail, FIELD_INVOICENUMBER);
            float money = getPropertyFloat(detail, "rmb");
            float taxes = getPropertyFloat(detail, "taxes");
            float moneysum = getPropertyFloat(detail, "subsidies");
            totalTax = totalTax + money + moneysum;
            String getRmb = getProperty(detail, "rmb");
            String gettaxes = getProperty(detail, "taxes");
            String redirictchheck = getProperty(detail, "redirict");
            // 如果是专票，处理税
            if (specialMap.containsKey(keyNo) && Float.parseFloat(gettaxes) > 0) {
                List<TravelCost> taxList = (List<TravelCost>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
                resultMap.put(TAX_KEY, taxList);
                float rate = specialMap.get(keyNo);
                TravelCost taxCost = new TravelCost();
                // 税拔
                String lineCost = FNUM.format(money - taxes);
                // 税金
                String lineRate = FNUM.format(taxes);
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
                    taxCost.setRemarks(getProperty(detail, "accountcode"));
                    taxCost.setCurrency("CNY");
                    taxList.add(taxCost);
                    // 税拔
                    setProperty(detail, "rmb", lineCost);
                    float diff = getFloatValue(lineRate) + getFloatValue(lineCost) - money;
                    if (diff != 0) {
                        TravelCost padding = new TravelCost();
                        padding.setLineamount(diff + "");
                        padding.setBudgetcoding(getProperty(detail, "budgetcoding"));
                        padding.setSubjectnumber(getProperty(detail, "subjectnumber"));
                        //发票说明
                        padding.setRemarks(getProperty(detail, "accountcode"));
                        padding.setCurrency("CNY");
                        List<TravelCost> paddingList = (List<TravelCost>) resultMap.getOrDefault(PADDING_KEY, new ArrayList<>());
                        paddingList.add(padding);
                        resultMap.put(PADDING_KEY, paddingList);
                    }
                }
            }

            //add-ws-5/13-获取当前人是否直属部门后台导出csv使用
            List<Dictionary> dictionaryL = dictionaryService.getForSelect("PJ119");
            String value1 = dictionaryL.get(4).getValue2();
            List<Dictionary> dictionary = dictionaryService.getForSelect("PJ132");
            String value2 = dictionary.get(4).getValue2();
            if (redirictchheck.equals("0")) {
                List<TravelCost> oldtaxList = (List<TravelCost>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
                resultMap.put(TAX_KEY, oldtaxList);
                TravelCost newtaxCost = new TravelCost();
                newtaxCost.setSubjectnumber(value1);
                newtaxCost.setLineamount(df1.format(moneysum));
                newtaxCost.setBudgetcoding(getProperty(detail, "budgetcoding"));
                //发票说明
                newtaxCost.setRemarks(getProperty(detail, "accountcode"));
                newtaxCost.setCurrency("CNY");
                oldtaxList.add(newtaxCost);
            } else if (redirictchheck.equals("1")) {
                List<TravelCost> oldtaxList = (List<TravelCost>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
                resultMap.put(TAX_KEY, oldtaxList);
                TravelCost newtaxCost = new TravelCost();
                newtaxCost.setSubjectnumber(value2);
                newtaxCost.setLineamount(df1.format(moneysum));
                newtaxCost.setBudgetcoding(getProperty(detail, "budgetcoding"));
                //发票说明
                newtaxCost.setRemarks(getProperty(detail, "accountcode"));
                newtaxCost.setCurrency("CNY");
                oldtaxList.add(newtaxCost);
            }
            //add-ws-5/13-获取当前人是否直属部门后台导出csv使用
        }
//        if (Float.valueOf(df1.format(totalTax)) != specialMap.get(TOTAL_TAX)) {
//            throw new LogicalException("发票合计金额与明细不匹配。");
//        }
        resultMap.put(INPUT_TYPE_KEY, "rmb");
        return resultMap;
    }

    private String getInputType(Object o) throws LogicalException {
        float rmb = getPropertyFloat(o, FIELD_RMB);
        float foreign = getPropertyFloat(o, FIELD_FOREIGNCURRENCY);
//        if (rmb > 0 && foreign > 0) {
//            throw new Exception("人民币和外币不能同时输入。");
//        }
        if (rmb < 0 || foreign < 0 || (rmb + foreign) < 0) {
            throw new LogicalException("明细行金额不能为负数。");
        }

        return rmb > 0 ? FIELD_RMB : FIELD_FOREIGNCURRENCY;
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

    @Override
    public void updateEvectionVo(EvectionVo evectionVo, TokenModel tokenModel) throws Exception {
        Evection evection = new Evection();
        BeanUtils.copyProperties(evectionVo.getEvection(), evection);
        evection.preUpdate(tokenModel);
        evectionMapper.updateByPrimaryKey(evection);
        String evectionid = evection.getEvectionid();
        String invoiceNo = evection.getInvoiceno();
        TrafficDetails traffic = new TrafficDetails();
        traffic.setEvectionid(evectionid);
        trafficdetailsMapper.delete(traffic);
        List<TrafficDetails> trafficdetailslist = evectionVo.getTrafficdetails();

        AccommodationDetails accommodation = new AccommodationDetails();
        accommodation.setEvectionid(evectionid);
        accommodationdetailsMapper.delete(accommodation);
        List<AccommodationDetails> accommodationdetailslist = evectionVo.getAccommodationdetails();

        OtherDetails other = new OtherDetails();
        other.setEvectionid(evectionid);
        otherdetailsMapper.delete(other);
        List<OtherDetails> otherdetailslist = evectionVo.getOtherdetails();

        Invoice invoice = new Invoice();
        invoice.setEvectionid(evectionid);
        invoicemapper.delete(invoice);
        List<Invoice> invoicelist = evectionVo.getInvoice();

        Currencyexchange currencyexchange = new Currencyexchange();
        currencyexchange.setEvectionid(evectionid);
        currencyexchangeMapper.delete(currencyexchange);
        List<Currencyexchange> currencyexchangeList = evectionVo.getCurrencyexchanges();

        if (trafficdetailslist != null) {
            int rowindex = 0;
            for (TrafficDetails trafficdetails : trafficdetailslist) {
                rowindex = rowindex + 1;
                trafficdetails.preInsert(tokenModel);
                trafficdetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficdetails.setEvectionid(evectionid);
                trafficdetails.setRowindex(rowindex);
                trafficdetailsMapper.insertSelective(trafficdetails);
            }
        }
        if (accommodationdetailslist != null) {
            int rowindex = 0;
            for (AccommodationDetails accommodationdetails : accommodationdetailslist) {
                rowindex = rowindex + 1;
                accommodationdetails.preInsert(tokenModel);
                accommodationdetails.setAccommodationdetails_id(UUID.randomUUID().toString());
                accommodationdetails.setEvectionid(evectionid);
                accommodationdetails.setRowindex(rowindex);
                accommodationdetailsMapper.insertSelective(accommodationdetails);
            }
        }
        if (otherdetailslist != null) {
            int rowindex = 0;
            for (OtherDetails otherdetails : otherdetailslist) {
                rowindex = rowindex + 1;
                otherdetails.preInsert(tokenModel);
                otherdetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherdetails.setEvectionid(evectionid);
                otherdetails.setRowindex(rowindex);
                otherdetailsMapper.insertSelective(otherdetails);

            }
        }
        if (invoicelist != null) {
            int rowundex = 0;
            for (Invoice invoicel : invoicelist) {
                rowundex = rowundex + 1;
                invoicel.preInsert(tokenModel);
                invoicel.setInvoice_id(UUID.randomUUID().toString());
                invoicel.setEvectionid(evectionid);
                invoicel.setRowindex(rowundex);
                invoicemapper.insertSelective(invoicel);
            }
        }
        if (currencyexchangeList != null) {
            int rowundex = 0;
            for (Currencyexchange curr : currencyexchangeList) {
                rowundex = rowundex + 1;
                curr.preInsert(tokenModel);
                curr.setCurrencyexchangeid(UUID.randomUUID().toString());
                curr.setEvectionid(evectionid);
                curr.setRowindex(rowundex);
                currencyexchangeMapper.insertSelective(curr);
            }
        }
        TravelCost travelCost = new TravelCost();
        travelCost.setEvectionid(evectionid);
        travelcostmapper.delete(travelCost);
        saveTravelCostList(invoiceNo, trafficdetailslist, accommodationdetailslist, otherdetailslist, invoicelist, currencyexchangeList, evectionVo, tokenModel, evectionid);

    }

    @Override
    public void insertEvectionVo(EvectionVo evectionVo, TokenModel tokenModel) throws Exception {
        //发票编号
        String invoiceNo = "";
        Calendar cal = Calendar.getInstance();
        String year = new SimpleDateFormat("yy", Locale.CHINESE).format(Calendar.getInstance().getTime());
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String no = "";
        if (evectionMapper.getInvoiceNo(sdf.format(evectionVo.getEvection().getReimbursementdate())) != null) {
            int count = evectionMapper.getInvoiceNo(sdf.format(evectionVo.getEvection().getReimbursementdate()));
            no = String.format("%2d", count + 1).replace(" ", "0");
        } else {
            no = "01";
        }

        String month1 = String.format("%2d", month).replace(" ", "0");
        String day1 = String.format("%2d", day).replace(" ", "0");
        //报销编号变更  add_fjl 禅道任务261
//        invoiceNo = "WY" + year + month1 + day1 + no;
        invoiceNo = "C" + year + month1 + day1 + no;
        //报销编号变更  add_fjl 禅道任务261

        String evectionid = UUID.randomUUID().toString();
        Evection evection = new Evection();
        BeanUtils.copyProperties(evectionVo.getEvection(), evection);
        evection.setInvoiceno(invoiceNo);
        evection.preInsert(tokenModel);
        evection.setEvectionid(evectionid);
        evectionMapper.insertSelective(evection);
        List<TrafficDetails> trafficdetailslist = evectionVo.getTrafficdetails();
        List<AccommodationDetails> accommodationdetailslist = evectionVo.getAccommodationdetails();
        List<OtherDetails> otherdetailslist = evectionVo.getOtherdetails();
        List<Invoice> invoicelist = evectionVo.getInvoice();
        List<Currencyexchange> currencyexchangeList = evectionVo.getCurrencyexchanges();

        if (trafficdetailslist != null) {
            int rowindex = 0;
            for (TrafficDetails trafficdetails : trafficdetailslist) {
                rowindex = rowindex + 1;
                trafficdetails.preInsert(tokenModel);
                trafficdetails.setTrafficdetails_id(UUID.randomUUID().toString());
                trafficdetails.setEvectionid(evectionid);
                trafficdetails.setRowindex(rowindex);
                trafficdetailsMapper.insertSelective(trafficdetails);
            }
        }

        if (accommodationdetailslist != null) {
            int rowindex = 0;
            for (AccommodationDetails accommodationdetails : accommodationdetailslist) {
                rowindex = rowindex + 1;
                accommodationdetails.preInsert(tokenModel);
                accommodationdetails.setAccommodationdetails_id(UUID.randomUUID().toString());
                accommodationdetails.setEvectionid(evectionid);
                accommodationdetails.setRowindex(rowindex);
                accommodationdetailsMapper.insertSelective(accommodationdetails);
            }
        }

        if (otherdetailslist != null) {
            int rowindex = 0;
            for (OtherDetails otherdetails : otherdetailslist) {
                rowindex = rowindex + 1;
                otherdetails.preInsert(tokenModel);
                otherdetails.setOtherdetails_id(UUID.randomUUID().toString());
                otherdetails.setEvectionid(evectionid);
                otherdetails.setRowindex(rowindex);
                otherdetailsMapper.insertSelective(otherdetails);
            }
        }

        if (invoicelist != null) {
            int rowundex = 0;
            for (Invoice invoice : invoicelist) {
                rowundex = rowundex + 1;
                invoice.preInsert(tokenModel);
                invoice.setInvoice_id(UUID.randomUUID().toString());
                invoice.setEvectionid(evectionid);
                invoice.setRowindex(rowundex);
                invoicemapper.insertSelective(invoice);
            }
        }
        if (currencyexchangeList != null) {
            int rowundex = 0;
            for (Currencyexchange curr : currencyexchangeList) {
                rowundex = rowundex + 1;
                curr.preInsert(tokenModel);
                curr.setCurrencyexchangeid(UUID.randomUUID().toString());
                curr.setEvectionid(evectionid);
                curr.setRowindex(rowundex);
                currencyexchangeMapper.insertSelective(curr);
            }
        }
        saveTravelCostList(invoiceNo, trafficdetailslist, accommodationdetailslist, otherdetailslist, invoicelist, currencyexchangeList, evectionVo, tokenModel, evectionid);

    }

}
