package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.PersonalCost;
import com.nt.dao_Pfans.PFANS2000.PersonalCostYears;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.service_pfans.PFANS2000.mapper.BonussendMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostYearsMapper;
import com.nt.service_pfans.PFANS2000.mapper.WagesMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
@Transactional(rollbackFor = Exception.class)
public class PersonalCostServiceImpl implements PersonalCostService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PersonalCostYearsMapper personalCostYearsMapper;

    @Autowired
    private PersonalCostMapper personalCostMapper;

    @Autowired
    private WagesMapper wagesMapper;

    @Autowired
    private BonussendMapper bonussendMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public List<PersonalCostYears> getPerCostYarList(PersonalCostYears personalCostYears) {
        return personalCostYearsMapper.select(personalCostYears);
    }

    @Override
    public PersonalCost insertPenalcost(String year, TokenModel tokenModel) throws Exception {
        year = "2020";
        Query query = new Query();
        String workday = null;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
        //年度
        PersonalCostYears personalCostYears = new PersonalCostYears();
        personalCostYears.preInsert(tokenModel);
        String personalCostYerasid = UUID.randomUUID().toString();
        //暂时处理
        personalCostYears.setYears(year);
        personalCostYears.setYearsantid(personalCostYerasid);
        personalCostYearsMapper.insert(personalCostYears);
        //取去年【产休人员】名单
        List<String> birthuseridList = personalCostMapper.selectBirthUserid("2020");
        int flag = 0;
        for (Iterator<CustomerInfo> custList = customerInfos.iterator(); custList.hasNext(); ) {
            CustomerInfo customerInfoAnt = custList.next();
            //清除去年离职
            if (StringUtils.isNullOrEmpty(customerInfoAnt.getUserinfo().getResignation_date())) {
                for (String birthuserid : birthuseridList) {
                    if (birthuserid.equals(customerInfoAnt.getUserid()))
                    {
                        flag ++;
                    }
                }
                    if (flag == 0) //排除产休
                    {
                        PersonalCost personalCost = new PersonalCost();
                        //token
                        personalCost.preInsert(tokenModel);
                        personalCost.setPersonalcostid(UUID.randomUUID().toString());
                        //年度
                        personalCost.setYearsantid(personalCostYerasid);
                        //userid
                        personalCost.setUserid(customerInfoAnt.getUserid());
                        //7~3月人件费 1月1日自动生成
                        Double lastAnt = 0.00;
                        if (StrUtil.isNotBlank(customerInfoAnt.getUserinfo().getBasic()) && StrUtil.isNotBlank(customerInfoAnt.getUserinfo().getDuty())) {
                            BigDecimal Basic = BigDecimal.valueOf(Double.valueOf(customerInfoAnt.getUserinfo().getBasic()));
                            BigDecimal duty = BigDecimal.valueOf(Double.valueOf(customerInfoAnt.getUserinfo().getDuty()));
                            lastAnt = Basic.add(duty).doubleValue();
                        }
                        BigDecimal jutomacost = new BigDecimal(lastAnt).setScale(2, ROUND_HALF_UP);
                        //入职日
                        if (StrUtil.isNotBlank(customerInfoAnt.getUserinfo().getWorkday())) {
                            workday = customerInfoAnt.getUserinfo().getWorkday().substring(0, 10);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(Convert.toDate(workday));
                            if (customerInfoAnt.getUserinfo().getWorkday().length() >= 24) {
                                cal.add(Calendar.DAY_OF_YEAR, 1);
                            }
                            workday = s.format(cal.getTime());
                        }
                        personalCost.setWorkday(workday);
                        personalCost.setJutomacost(jutomacost.toString());
                        //rank
                        personalCost.setPrank(customerInfoAnt.getUserinfo().getRank());
                        //计算基数
                        List<String> comtotalwagesList = wagesMapper.getComtotalwages(customerInfoAnt.getUserid(), "2020");
                        String comtotalwagesSum = "";
                        BigDecimal comtotalwages = new BigDecimal(0.0000);
                        for (String com : comtotalwagesList) {
                            if (StrUtil.isNotBlank(com)) {
                                BigDecimal comAnt = BigDecimal.valueOf(Double.valueOf(com));
                                comtotalwages = comtotalwages.add(comAnt);
                            }
                        }
                        //计算小计
                        List<String> totalList = wagesMapper.getTotal(customerInfoAnt.getUserid(), "2020");
                        String totalsum = "";
                        BigDecimal total = new BigDecimal(0.0000);
                        for (String tota : totalList) {
                            if (StrUtil.isNotBlank(tota)) {
                                BigDecimal comAnt = BigDecimal.valueOf(Double.valueOf(tota));
                                total = total.add(comAnt);
                            }
                        }
                        LocalDate today = LocalDate.now();
                        LocalDate yesteryear = today.plusYears(-1);
                        //去年
                        String lastYear = String.valueOf(yesteryear.getYear());
                        List<Dictionary> dictionaryL = dictionaryService.getForSelect("PR067");
                        //新年度自动跑
                        if (!workday.substring(0, 4).equals(lastYear)) //一整年
                        {
                            BigDecimal allYear = new BigDecimal("12");
                            BigDecimal ctAnt = comtotalwages.subtract(total);
                            Bonussend bonussend = new Bonussend();
                            bonussend.setUser_id(customerInfoAnt.getUserid());
                            List<Bonussend> bonussendList =  bonussendMapper.select(bonussend);
                            String bonussendMoney = "";
                            if(bonussendList.size() != 0){
                                bonussendMoney  = bonussendList.get(0).getTotalbonus1();
                                BigDecimal bonMoney = new BigDecimal(bonussendMoney);
                            }
                            BigDecimal btAnt = ctAnt.add(ctAnt);
                            Double baseresultAnt = btAnt.divide(allYear, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            //养老
                            if (Double.valueOf(dictionaryL.get(0).getValue2()) > baseresultAnt)//比最低金额小
                            {
                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue2());
                            } else if (Double.valueOf(dictionaryL.get(0).getValue3()) < baseresultAnt)//比最高金额大
                            {
                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue3());
                            } else {
                                personalCost.setYangbaseresult(baseresultAnt.toString());
                            }
                            //医疗
                            if (Double.valueOf(dictionaryL.get(1).getValue2()) > baseresultAnt)//比最低金额小
                            {
                                personalCost.setYibaseresult(dictionaryL.get(1).getValue2());
                            } else if (Double.valueOf(dictionaryL.get(1).getValue3()) < baseresultAnt)//比最高金额大
                            {
                                personalCost.setYibaseresult(dictionaryL.get(1).getValue3());
                            } else {
                                personalCost.setYibaseresult(baseresultAnt.toString());
                            }
                            //公积金
                            if (Double.valueOf(dictionaryL.get(2).getValue2()) > baseresultAnt)//比最低金额小
                            {
                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue2());
                                ;
                            } else if (Double.valueOf(dictionaryL.get(2).getValue3()) < baseresultAnt)//比最高金额大
                            {
                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue3());
                            } else {
                                personalCost.setZhubaseresult(baseresultAnt.toString());
                            }

                        } else {
                            String mounthStr = workday.substring(5, 7);
                            //整月 workMount
                            String workMount = String.valueOf(Integer.valueOf("12") - Integer.valueOf(mounthStr));
                            BigDecimal workMountB = new BigDecimal(workMount);

                            LocalDate workDay = LocalDate.parse(workday);
                            int lenghtDay = workDay.getDayOfMonth();
                            BigDecimal lenghtDayB = new BigDecimal(String.valueOf(lenghtDay));
                            String joinDay = workday.substring(7);
                            //入职月工作多少天
                            String workDayOnWork = String.valueOf(lenghtDay - Integer.valueOf(joinDay) + 1);
                            BigDecimal workDayOnWorkB = new BigDecimal(workDayOnWork);
                            //出勤系数
                            BigDecimal workDayResult = (workDayOnWorkB.divide(lenghtDayB, 2, BigDecimal.ROUND_HALF_UP)).add(workMountB);
                            Double baseresultAnt = ((comtotalwages.subtract(total)).divide(workDayResult, 2, BigDecimal.ROUND_HALF_UP)).doubleValue();
                            //养老
                            if (Double.valueOf(dictionaryL.get(0).getValue2()) > baseresultAnt)//比最低金额小
                            {
                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue2());
                            } else if (Double.valueOf(dictionaryL.get(0).getValue3()) < baseresultAnt)//比最高金额大
                            {
                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue3());
                            } else {
                                personalCost.setYangbaseresult(baseresultAnt.toString());
                            }
                            //医疗
                            if (Double.valueOf(dictionaryL.get(1).getValue2()) > baseresultAnt)//比最低金额小
                            {
                                personalCost.setYibaseresult(dictionaryL.get(1).getValue2());
                            } else if (Double.valueOf(dictionaryL.get(1).getValue3()) < baseresultAnt)//比最高金额大
                            {
                                personalCost.setYibaseresult(dictionaryL.get(1).getValue3());
                            } else {
                                personalCost.setYibaseresult(baseresultAnt.toString());
                            }
                            //公积金
                            if (Double.valueOf(dictionaryL.get(2).getValue2()) > baseresultAnt)//比最低金额小
                            {
                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue2());
                                ;
                            } else if (Double.valueOf(dictionaryL.get(2).getValue3()) < baseresultAnt)//比最高金额大
                            {
                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue3());
                            } else {
                                personalCost.setZhubaseresult(baseresultAnt.toString());
                            }
                        }

                        //Rank给予标准
                        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
                        Map<String, String> rankMap = new HashMap<>();
                        for (Dictionary dic : dictionaryRank) {
                            rankMap.put(dic.getCode(), dic.getValue2());
                        }
                        String monthCost = rankMap.get(customerInfoAnt.getUserinfo().getRank());
                        personalCost.setAprilcosty(monthCost);
                        personalCost.setMarchcosty(monthCost);
                        personalCost.setJunecosty(monthCost);
                        personalCost.setJulycosty(monthCost);
                        personalCost.setAugustcosty(monthCost);
                        personalCost.setSeptembercosty(monthCost);
                        personalCost.setOctobercosty(monthCost);
                        personalCost.setNovembercosty(monthCost);
                        personalCost.setDecembercosty(monthCost);
                        personalCost.setJanuarycosty(monthCost);
                        personalCost.setJanuarycosty(monthCost);
                        personalCost.setFebruarycosty(monthCost);
                        personalCost.setMarchcosty(monthCost);
                        personalCostMapper.insert(personalCost);
                    }
                    else{ //产休人员
                        flag = 0;
                    }
                }
            custList.remove();
            }
        return null;
    }


    @Override
    public List<PersonalCost> getPersonalCost(String groupid, String yearsantid) throws Exception {
        List<CustomerInfo> customerInfoList = mongoTemplate.find(new Query(Criteria.where("userinfo.groupid").is(groupid)), CustomerInfo.class);
        List<PersonalCost> personalCostList = personalCostMapper.selectPersonalCostResult(customerInfoList, yearsantid);

        return personalCostList;
    }

    @Override
    public void upPersonalCost(List<PersonalCost> personalCostList, TokenModel tokenModel) throws Exception {
        personalCostMapper.updatePersonalCost(personalCostList, tokenModel);
    }
//
//    @Override
//    public void insert(Recruit recruit, TokenModel tokenModel) throws Exception {
////add-ws-8/4-禅道任务296--
//        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
//        Date date = new Date();
//        List<Recruit> recruitlist = recruitMapper.selectAll();
//        String year = sf1.format(date);
//        int number = 0;
//        String Numbers = "";
//        String no = "";
//        if (recruitlist.size() > 0) {
//            for (Recruit recr : recruitlist) {
//                if (recr.getNumbers() != "" && recr.getNumbers() != null) {
//                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(recr.getNumbers(), 2, 10));
//                    if (Integer.valueOf(year).equals(Integer.valueOf(checknumber))) {
//                        number = number + 1;
//                    }
//                }
//
//            }
//            if (number <= 8) {
//                no = "00" + (number + 1);
//            } else {
//                no = "0" + (number + 1);
//            }
//        } else {
//            no = "001";
//        }
//        Numbers = "ZP" + year + no;
//        //add-ws-8/4-禅道任务296--
//        recruit.preInsert(tokenModel);
//        recruit.setNumbers(Numbers);
//        recruit.setRecruitid(UUID.randomUUID().toString());
//        recruitMapper.insert(recruit);
//    }
//
//    @Override
//    public List<Recruit> getRecruitList(Recruit recruit, HttpServletRequest request) throws Exception {
//
//        return recruitMapper.select(recruit);
//    }
}
