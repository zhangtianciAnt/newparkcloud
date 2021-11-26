package com.nt.service_Org.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.PersonScale;
import com.nt.dao_Org.PersonScaleMee;
import com.nt.dao_Org.Vo.PersonScaleVo;
import com.nt.dao_Org.Vo.ScaleComproject;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.PersonScaleService;
import com.nt.service_Org.mapper.ExpartinforMapper;
import com.nt.service_Org.mapper.PersonScaleMapper;
import com.nt.service_Org.mapper.PersonScaleMeeMapper;
import com.nt.utils.BigDecimalUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PersonScaleServiceImpl implements PersonScaleService {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private PersonScaleMapper personScaleMapper;

    @Autowired
    private PersonScaleMeeMapper personScaleMeeMapper;

    @Autowired
    private ExpartinforMapper expartinforMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 10 1 * * ?")
    public void calMonthScaleInfo() throws Exception {
        List<Dictionary> dictionaryL = dictionaryService.getForSelect("BP027");
        TokenModel tokenModel = new TokenModel();
        SimpleDateFormat f_t = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMM");
        Calendar lastMonthDate = Calendar.getInstance();
        lastMonthDate.add(Calendar.MONTH, -1);
        String now_Date = f_t.format(lastMonthDate.getTime());
        String nowDate = ft.format(lastMonthDate.getTime());
        String now_Day = now_Date.substring(8,10);
        String nowY_Month = now_Date.substring(0,7);
        String nowYMonth = nowDate.substring(0,6);
        //if(Integer.parseInt(nowDay) != Integer.parseInt(dictionaryL.get(0).getValue1()) + 1) return;
        List<PersonScaleMee> getMeeInfoList = personScaleMapper.getMeeInfo(nowY_Month);
        if(getMeeInfoList.size() > 0){
            getMeeInfoList.forEach(mee -> {
                mee.setPersonscalemee_id(UUID.randomUUID().toString());
                mee.setYearmonth(nowY_Month);
                Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(mee.getUser_id()));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    mee.setCenter_id(customerInfo.getUserinfo().getCenterid());
                    mee.setGroup_id(customerInfo.getUserinfo().getGroupid());
                    mee.setRanks(customerInfo.getUserinfo().getRank());
                }
                mee.setMangernumber(personScaleMapper.getMangernum(mee.getUser_id(),nowY_Month));
                mee.setManagesorce(personScaleMapper.getPronum(mee.getUser_id(),nowY_Month));
                mee.setCreateon(new Date());
            });
            personScaleMapper.insetMeeList(getMeeInfoList);
        }

        List<PersonScale> getLogPerInfoList = personScaleMapper.getLogInfo(nowY_Month);
        BigDecimal workingHB = new BigDecimal(personScaleMapper.getWorktimeger(nowYMonth));
        Map<String, List<PersonScale>> groupByPeoples = getLogPerInfoList.stream().collect(Collectors.groupingBy(PersonScale::getReportpeople));
        List<PersonScale> saveResult = new ArrayList<>();
        groupByPeoples.forEach((peoId,groList) ->{
            if(groList.size() > 1){
                BigDecimal sumAge = groList.stream().map(PersonScale::getWorktime).reduce(BigDecimal.ZERO,BigDecimal::add);
                if(sumAge.compareTo(workingHB) == 1){//a大于b
                    setGroList(nowY_Month, sumAge, saveResult, groList, tokenModel);
                }else{
                    setGroList(nowY_Month, workingHB, saveResult, groList, tokenModel);
                }

            }else if(groList.size() == 1){
                if(workingHB.compareTo(groList.get(0).getWorktime()) == 1){//a大于b
                    String prosT = groList.get(0).getWorktime().divide(workingHB,2,BigDecimal.ROUND_HALF_UP).toString();
                    groList.get(0).setPersonscale_id(UUID.randomUUID().toString());
                    groList.get(0).setProportions(prosT);
                    groList.get(0).setYearmonth(nowY_Month);
                    this.setPeoInfo(groList.get(0),tokenModel);
                    saveResult.add(groList.get(0));

                }else{
                    groList.get(0).setPersonscale_id(UUID.randomUUID().toString());
                    groList.get(0).setProportions("1.00");
                    groList.get(0).setYearmonth(nowY_Month);
                    this.setPeoInfo(groList.get(0),tokenModel);
                    saveResult.add(groList.get(0));
                }
            }
        });

        Map<String,BigDecimal> diffMap = new HashMap<>();
        Map<String,List<PersonScale>> checkSummary = saveResult.stream().collect(Collectors.groupingBy(PersonScale :: getReportpeople));
        for(Map.Entry<String, List<PersonScale>> entry : checkSummary.entrySet()){
            String totalQuantity = entry.getValue().stream().map(PersonScale::getProportions).reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum);
            if(new BigDecimal(totalQuantity).compareTo(new BigDecimal("1.00")) == 1){//a大于b
                BigDecimal diff = new BigDecimal(totalQuantity).subtract(new BigDecimal("1.00"));
                diffMap.put(entry.getKey(),diff);
            }
        }
        if(saveResult.size() > 0){
            personScaleMapper.insetList(saveResult);
        }
        if(diffMap.size() > 0){
            diffMap.forEach((userid,dif) ->{
                PersonScale personScale = new PersonScale();
                personScale.setReportpeople(userid);
                personScale.setYearmonth(nowY_Month);
                List<PersonScale> personScaleList = personScaleMapper.select(personScale);
                if(personScaleList.size() > 0){
                    Comparator<PersonScale> comparator = Comparator.comparing(PersonScale::getProportions);
                    PersonScale maxPersonScale = personScaleList.stream().max(comparator).get();
                    String difRes = new BigDecimal(maxPersonScale.getProportions()).subtract(dif).toString();
                    maxPersonScale.setProportions(difRes);
                    personScaleMapper.updateByPrimaryKeySelective(maxPersonScale);
                }
            });
        }
    }

    private void setGroList(String nowY_Month, BigDecimal workingHB, List<PersonScale> saveResult, List<PersonScale> groList, TokenModel tokenModel) {
        groList.forEach(groR ->{
            String prosR = groR.getWorktime().divide(workingHB,2,BigDecimal.ROUND_HALF_UP).toString();
            groR.setPersonscale_id(UUID.randomUUID().toString());
            groR.setProportions(prosR);
            groR.setYearmonth(nowY_Month);
            this.setPeoInfo(groR,tokenModel);
            saveResult.add(groR);
        });
    }

    private void setPeoInfo(PersonScale peoInfo,TokenModel tokenModel){
        if(peoInfo.getType().equals("0")){//社内
            Query query = new Query();
            query.addCriteria(Criteria.where("userid").is(peoInfo.getReportpeople()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                peoInfo.setCenter_id(customerInfo.getUserinfo().getCenterid());
                peoInfo.setGroup_id(customerInfo.getUserinfo().getGroupid());
                peoInfo.setRanks(customerInfo.getUserinfo().getRank());
            }
        }else{
            Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
            expatriatesinfor.setAccount(peoInfo.getReportpeople());
            List<Expatriatesinfor> expatriatesinlist = expartinforMapper.select(expatriatesinfor);
            if(expatriatesinlist.size() > 0){
                peoInfo.setCenter_id(expatriatesinlist.get(0).getOrgInformationcenterid());
                peoInfo.setGroup_id(expatriatesinlist.get(0).getOrgInformationgroupid());
                peoInfo.setRanks(expatriatesinlist.get(0).getRn());
            }
        }

    }

    @Override
    public List<PersonScaleMee> getList(PersonScaleMee personScaleMee) throws Exception {
        List<PersonScaleMee> personScaleMees = personScaleMeeMapper.select(personScaleMee);
        personScaleMees = personScaleMees.stream().sorted(Comparator.comparing(PersonScaleMee::getMangernumber).reversed()).collect(Collectors.toList());
        return personScaleMees;
    }

    @Override
    public PersonScaleVo getPeopleInfo(String personScaleMee_id,String yearMonth) throws Exception {
        PersonScaleVo personScaleVo = new PersonScaleVo();
        PersonScaleMee personScaleMee = personScaleMeeMapper.selectByPrimaryKey(personScaleMee_id);
        personScaleVo.setPersonScaleMee(personScaleMee);
        PersonScale personScale = new PersonScale();
        personScale.setReporters(personScaleMee.getUser_id());
        List<PersonScale> personScaleList = personScaleMapper.select(personScale);
        if(personScaleList.size() > 0){
            personScaleList = this.getRecursion(personScaleList,personScaleList,yearMonth);
            personScaleList = personScaleList.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(
                            o -> o.getReportpeople() + ";" + o.getProject_id()))), ArrayList::new));
        }
        personScaleList = personScaleList.stream().sorted(Comparator.comparing(PersonScale::getType)
                .thenComparing(PersonScale::getReportpeople)
                .thenComparing(PersonScale::getProportions).reversed()).collect(Collectors.toList());
        personScaleVo.setPersonScaleList(personScaleList);
        return personScaleVo;
    }

    public List<PersonScale> getRecursion(List<PersonScale> perScaleS, List<PersonScale> personScaleList, String yearMonth) throws Exception {
        List<ScaleComproject> comprojectList = this.getComprojectList(perScaleS,yearMonth);
        List<PersonScale> selectOthers = new ArrayList<>();
        comprojectList.forEach(comp -> {
            PersonScale personSe = new PersonScale();
            personSe.setProject_id(comp.getProject_id());
            personSe.setReportpeople(comp.getReportpeople());
            List<PersonScale> scaleList = personScaleMapper.select(personSe);
            if(scaleList.size() > 0){
                personScaleList.add(scaleList.get(0));
                selectOthers.add(scaleList.get(0));
            }
        });
        if(selectOthers.size() > 0){
            this.getRecursionOther(selectOthers,personScaleList, yearMonth);
        }
        return personScaleList;
    }

    public List<ScaleComproject> getComprojectList(List<PersonScale> getResults, String yearMonth) throws Exception {
        List<ScaleComproject> comprojectList = new ArrayList<>();
        getResults.forEach(pss ->{
            comprojectList.addAll(personScaleMapper.getComprojects(pss.getReportpeople(), yearMonth));
        });
        return comprojectList;
    }

    public void getRecursionOther(List<PersonScale> selectOthers, List<PersonScale> personScaleList, String yearMonth) throws Exception {
        List<ScaleComproject> selectOtherList = this.getComprojectList(selectOthers, yearMonth);
        List<PersonScale> personScales = new ArrayList<>();
        if(selectOtherList.size() > 0){
            selectOtherList.forEach(sot -> {
                PersonScale pss = new PersonScale();
                pss.setProject_id(sot.getProject_id());
                pss.setReportpeople(sot.getReportpeople());
                personScales.add(pss);
            });
            this.getRecursion(personScales, personScaleList, yearMonth);
        }
    }
}
