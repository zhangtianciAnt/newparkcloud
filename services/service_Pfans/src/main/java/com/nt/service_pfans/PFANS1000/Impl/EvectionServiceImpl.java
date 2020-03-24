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

            Currencyexchange currencyexchange = new Currencyexchange();
            currencyexchange.setEvectionid(travelList.getEvectionid());
            List<Currencyexchange> lscer = currencyexchangeMapper.select(currencyexchange);
            for (Currencyexchange item : lscer) {
                Double ct = 0D;
                Double diff = Convert.toDouble(item.getCurrencyexchangerate()) - Convert.toDouble(item.getExchangerate());
                if(diff == 0){
                    continue;
                }
                TravelCost tc = new TravelCost();
                BeanUtil.copyProperties(ListVo.get(0), tc);
                tc.setNumber(ListVo.size() + 1);
                tc.setBudgetcoding("000000");
                if (diff < 0) {
                    tc.setSubjectnumber("8611-00-3061");
                }else{
                    tc.setSubjectnumber("8511-00-3061");
                }

                List<Double> costs = trafficdetailsMapper.getCount(travelList.getEvectionid(),item.getCurrency());
                for(Double cost:costs){
                    ct += cost;
                }
                tc.setLineamount(Convert.toStr(ct * diff));
                Listvo.add(tc);
            }
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
        //通过字典查取汇率缩写
        List<com.nt.dao_Org.Dictionary> dicExchangeRateList = dictionaryService.getForSelect("PG019");
        Map<String, String> exchangeRateMap = new HashMap<>();
        for ( Dictionary d : dicExchangeRateList ) {
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
                if (rate <= 0) {
                    throw new Exception("专票税率不能为0");
                }
                specialMap.put(invoice.getInvoicenumber(), rate);
            }
        }
        // 总金额改为人民币支出
        specialMap.put(TOTAL_TAX, Float.parseFloat(evectionVo.getEvection().getTotalpay()));
        if (specialMap.getOrDefault(TOTAL_TAX, 0f) <= 0) {
            throw new Exception("发票合计金额不能为0");
        }

        List<Object> needMergeList = new ArrayList<>();
        if (trafficDetailslist.size() > 0 || accommodationdetailslist.size() > 0
                || otherDetailslist.size() > 0) {
            needMergeList.addAll(trafficDetailslist);
            needMergeList.addAll(accommodationdetailslist);
            needMergeList.addAll(otherDetailslist);
        }
        mergeResult = mergeDetailList(needMergeList, specialMap, currencyexchangeList);


        List<TravelCost> csvList = new ArrayList<>();
        List<TravelCost> taxList = (List<TravelCost>) mergeResult.getOrDefault(TAX_KEY, new ArrayList<>());
        List<TravelCost> paddingList = (List<TravelCost>) mergeResult.getOrDefault(PADDING_KEY, new ArrayList<>());
//        String inputType = (String) mergeResult.get(INPUT_TYPE_KEY);
        for (Object o : mergeResult.values()) {
            if (o instanceof TrafficDetails || o instanceof AccommodationDetails || o instanceof OtherDetails) {
                Float money = getPropertyFloat(o, "rmb") + getPropertyFloat(o, "foreigncurrency");
                TravelCost cost = new TravelCost();
                //币种，汇率
                if(!StringUtils.isEmpty(getProperty(o, "currency"))){
                    cost.setCurrency(exchangeRateMap.getOrDefault(getProperty(o, "currency"), ""));
                    //匹配汇率
                    if(currencyexchangeList.size() > 0){
                        for(Currencyexchange exchange: currencyexchangeList){
                            if(getProperty(o, "currency").equals(getProperty(exchange, "currency"))){
                                cost.setExchangerate(getProperty(exchange, "currencyexchangerate"));
                            }
                        }
                    }
                }else {
                    cost.setCurrency("CNY");
                    cost.setExchangerate("");
                }
                cost.setLineamount(money.toString());
                cost.setBudgetcoding(getProperty(o, "budgetcoding"));
                cost.setSubjectnumber(getProperty(o, "subjectnumber"));
                //发票说明
                cost.setRemarks(getProperty(o, "accountcode"));
                csvList.add(cost);
            }
        }
        csvList.addAll(taxList);
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

    /**
     * 对明细数据分组
     *
     * @param detailList
     * @return resultMap
     */
    private Map<String, Object> mergeDetailList(List<Object> detailList, final Map<String, Float> specialMap, List<Currencyexchange> currencyexchangeList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (detailList.size() <= 0) {
            throw new Exception("明细不能为空");
        }
        //String inputType = getInputType(detailList.get(0));
        for (Object detail : detailList) {
//            if ( !inputType.equals(getInputType(detail)) ) {
//                throw new Exception("一次申请，只能选择一种货币。");
//            }
            // 发票No
            String keyNo = getProperty(detail, FIELD_INVOICENUMBER);
            String curren = getProperty(detail, "currency");
            String budgetcoding = getProperty(detail, "budgetcoding");
            String subjectnumber = getProperty(detail, "subjectnumber");
            String isRmb = getProperty(detail, "rmb");
            String mergeKey;
            if (specialMap.containsKey(keyNo) && Float.parseFloat(isRmb) > 0) {
                mergeKey = keyNo + " ... " + budgetcoding + " ... " + subjectnumber + " ... " + curren;
            } else {
                mergeKey = budgetcoding + " ... " + subjectnumber+ " ... " + curren;
            }
            // 行合并
            float money = getPropertyFloat(detail, "rmb") + getPropertyFloat(detail, "foreigncurrency");
            Object mergeObject = resultMap.get(mergeKey);
            if (mergeObject != null) {
                // 发现可以合并数据
                float newMoney = getPropertyFloat(mergeObject, "rmb") + getPropertyFloat(mergeObject, "foreigncurrency") + money;
//                setProperty(mergeObject, inputType, newMoney + "");
                if(!"0".equals(getProperty(detail, "rmb"))){
                    setProperty(mergeObject, "rmb", newMoney + "");
                }else {
                    setProperty(mergeObject, "foreigncurrency", newMoney + "");
                }
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
            totalTax = totalTax + money;
            String getRmb = getProperty(detail, "rmb");
            // 如果是专票，处理税
            if (specialMap.containsKey(keyNo) && Float.parseFloat(getRmb) > 0) {
                List<TravelCost> taxList = (List<TravelCost>) resultMap.getOrDefault(TAX_KEY, new ArrayList<>());
                resultMap.put(TAX_KEY, taxList);
                float rate = specialMap.get(keyNo);
                TravelCost taxCost = new TravelCost();

                // 税拔
                String lineCost = FNUM.format(money / (1 + rate));
                // 税金
                String lineRate = FNUM.format((money / (1 + rate)) * rate);
                if (money > 0) {
                    // 税
                    taxCost.setLineamount(lineRate);
                    taxCost.setBudgetcoding(getProperty(detail, "budgetcoding"));
                    taxCost.setSubjectnumber(getProperty(detail, "subjectnumber"));
                    //发票说明
                    taxCost.setRemarks(getProperty(detail, "accountcode"));
                    //币种
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
                        //币种
                        padding.setCurrency("CNY");
                        List<TravelCost> paddingList = (List<TravelCost>) resultMap.getOrDefault(PADDING_KEY, new ArrayList<>());
                        paddingList.add(padding);
                        resultMap.put(PADDING_KEY, paddingList);
                    }
                }
            }
            //计算汇兑损失
//            if(currencyexchangeList != null){
//                for(Currencyexchange curren : currencyexchangeList){
//                    String curRear = curren.getExchangerate();         //兑换汇率
////                    String curBefore = detail.getcurrency();
//                    Dictionary dictionary = new Dictionary();
//                    String accflg = ((AccommodationDetails) detail).getcurrency();
//                    dictionary.setCode(accflg);
////                    dictionary.setCode("PJ003002");
//                    List<Dictionary>  aa = dictionaryService.getDictionaryList(dictionary);
//                    float curBefore = 0;
//                    if(aa.size() > 0){
//                        curBefore = Float.valueOf(aa.get(0).getValue2());
//                    }
////                    float curBefore = getPropertyFloat(detail, CURRENCY_KEY);
//                    float diff = curBefore - getFloatValue(curRear);
//                    if ( diff != 0 ) {
//                        TravelCost cur = new TravelCost();
//                        float isRmb = getPropertyFloat(detail, "rmb");
//                        cur.setLineamount(String.valueOf(diff * isRmb));
//                        cur.setBudgetcoding(getProperty(detail, "budgetcoding"));
//                        cur.setSubjectnumber(getProperty(detail, "subjectnumber"));
//                        //发票说明
//                        cur.setRemarks(getProperty(detail, "accountcode"));
//                        List<TravelCost> paddingList = (List<TravelCost>) resultMap.getOrDefault(CURRENCY_KEY, new ArrayList<>());
//                        paddingList.add(cur);
//                        resultMap.put(CURRENCY_KEY, inputType);
//                    }
//                }
//            }
        }
        if (totalTax != specialMap.get(TOTAL_TAX)) {
            throw new Exception("发票合计金额与明细不匹配。");
        }
        resultMap.put(INPUT_TYPE_KEY, "rmb");
        return resultMap;
    }

    private String getInputType(Object o) throws Exception {
        float rmb = getPropertyFloat(o, FIELD_RMB);
        float foreign = getPropertyFloat(o, FIELD_FOREIGNCURRENCY);
//        if (rmb > 0 && foreign > 0) {
//            throw new Exception("人民币和外币不能同时输入。");
//        }
        if (rmb < 0 || foreign < 0 || (rmb + foreign) < 0) {
            throw new Exception("明细行金额不能为负数。");
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
        String year = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String no = "";
        if(evectionMapper.getInvoiceNo(sdf.format(evectionVo.getEvection().getReimbursementdate())) != null){
            int count = evectionMapper.getInvoiceNo(sdf.format(evectionVo.getEvection().getReimbursementdate()));
            no=String.format("%2d", count + 1).replace(" ", "0");
        }else {
            no = "01";
        }

        String month1 = String.format("%2d", month).replace(" ", "0");
        invoiceNo = "WY" + year + month1 + day + no;

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
