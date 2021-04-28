package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.nt.utils.MongoObject.CustmizeQuery;

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

    @Autowired
    private OrgTreeService orgTreeService;

    //系统定时任务每月1号自动保存单价
    @Scheduled(cron = "1 18 13 23 3 ?")
    public void savePersonalCost() throws Exception {
        LocalDate nowDate = LocalDate.now();
        String onYearStr = String.valueOf(nowDate.getYear());
        PersonalCostYears personalCostYears = new PersonalCostYears();
        TokenModel tokenModel = new TokenModel();
        personalCostYears.preInsert(tokenModel);
        String personalCostYerasid = UUID.randomUUID().toString();
        personalCostYears.setYears(onYearStr);
        personalCostYears.setYearsantid(personalCostYerasid);
        personalCostYearsMapper.insert(personalCostYears);
        //取所有人员
        Query query = new Query();
        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));

        //配付与否（根据部门设定）
        //配付List
        List<String> allotmentList = new ArrayList<>();
        OrgTree orgs = orgTreeService.get(new OrgTree());
        //ADMN
        String admn = "";
        for (OrgTree org : orgs.getOrgs()) {
            if (org.getRedirict().equals("0")) {
                allotmentList.add(org.get_id());
            }
            if (org.getCompanyen().equals("ADMN")) {
                admn = org.get_id();
            }

        }

        //Rank各种标准
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        //工会比重
        List<Dictionary> dictionaryUnion = dictionaryService.getForSelect("PR070");
        BigDecimal unionAnt = new BigDecimal(dictionaryUnion.get(0).getValue1());
        //加班时给
        List<Dictionary> dictionaryOver = dictionaryService.getForSelect("PR071");
        BigDecimal mouthworkAnt = new BigDecimal(dictionaryOver.get(0).getValue1());
        BigDecimal dayworkAnt = new BigDecimal(dictionaryOver.get(0).getValue2());
        BigDecimal basicAnt = new BigDecimal(dictionaryOver.get(0).getValue3());

        //社保企业4
        List<Dictionary> dictionarySbqy = dictionaryService.getForSelect("PR067");
        //养老-会社负担 下限 上限
        BigDecimal oldYlAnt = new BigDecimal(dictionarySbqy.get(0).getValue2());
        BigDecimal oldYlLower = new BigDecimal(dictionarySbqy.get(0).getValue3());
        BigDecimal oldYlUpper = new BigDecimal(dictionarySbqy.get(0).getValue4());
        //失业-会社负担
        BigDecimal lossSyAnt = new BigDecimal(dictionarySbqy.get(1).getValue2());
        BigDecimal lossSyLower = new BigDecimal(dictionarySbqy.get(1).getValue3());
        BigDecimal lossSyUpper = new BigDecimal(dictionarySbqy.get(1).getValue4());
        //工伤-会社负担
        BigDecimal gSAnt = new BigDecimal(dictionarySbqy.get(2).getValue2());
        BigDecimal gSLower = new BigDecimal(dictionarySbqy.get(2).getValue3());
        BigDecimal gSUpper = new BigDecimal(dictionarySbqy.get(2).getValue4());
        //生育-会社负担
        BigDecimal sYAnt = new BigDecimal(dictionarySbqy.get(3).getValue2());
        BigDecimal sYLower = new BigDecimal(dictionarySbqy.get(3).getValue3());
        BigDecimal sYUpper = new BigDecimal(dictionarySbqy.get(3).getValue4());
        //医疗-会社负担
        BigDecimal yLAnt = new BigDecimal(dictionarySbqy.get(4).getValue2());
        BigDecimal yLLower = new BigDecimal(dictionarySbqy.get(4).getValue3());
        BigDecimal yLUpper = new BigDecimal(dictionarySbqy.get(4).getValue4());
        //大病险-会社负担
        String dbXAnt = dictionarySbqy.get(5).getValue2();
        //公积金-会社负担
        BigDecimal gJjAnt = new BigDecimal(dictionarySbqy.get(7).getValue2());
        BigDecimal gJjLower = new BigDecimal(dictionarySbqy.get(7).getValue3());
        BigDecimal gJjUpper = new BigDecimal(dictionarySbqy.get(7).getValue4());

        //基本给
        Map<String, String> basicallMap = new HashMap<>();
        //职责给
        Map<String, String> responsibilityMap = new HashMap<>();
        //一括补贴
        Map<String, String> allowanceantMap = new HashMap<>();
        //取暖
        Map<String, String> qnbtMap = new HashMap<>();
        //拓展项1
        Map<String, String> expandOneMap = new HashMap<>();
        //拓展项2
        Map<String, String> expandTwoMap = new HashMap<>();
        //加班小时数
        Map<String, String> overtimehourMap = new HashMap<>();

        //独生子女费
        List<Dictionary> dictionaryOnlyChild = dictionaryService.getForSelect("PR072");
        String onlyChild = dictionaryOnlyChild.get(0).getValue1();
        //月度奖金月数
        Map<String, String> monthlyBonusMonthsMap = new HashMap<>();
        //年度奖金月数
        Map<String, String> annualBonusMonthsMap = new HashMap<>();
        BigDecimal twelveAnt = new BigDecimal("12");
        for (Dictionary dic : dictionaryRank) {
            //基本给2
            basicallMap.put(dic.getCode(), dic.getValue2());
            //职责给3
            responsibilityMap.put(dic.getCode(), dic.getValue3());
            //奖金计上（月度）4
            monthlyBonusMonthsMap.put(dic.getCode(), dic.getValue4());
            //奖金计上（年度）5
            annualBonusMonthsMap.put(dic.getCode(), dic.getValue5());
            //一括补贴6
            allowanceantMap.put(dic.getCode(), dic.getValue6());
            //取暖补贴7
            qnbtMap.put(dic.getCode(), dic.getValue7());
            //拓展项1 8
            expandOneMap.put(dic.getCode(), dic.getValue8());
            //拓展项2 9
            expandTwoMap.put(dic.getCode(), dic.getValue9());
            //加班小时数 10
            overtimehourMap.put(dic.getCode(), dic.getValue10());
        }

        Map<String, String> rankMap = new HashMap<>();
        for (Iterator<CustomerInfo> CustomerInfoListAnt = customerInfos.iterator(); CustomerInfoListAnt.hasNext(); ) {
            int flag = 0;
            CustomerInfo custInfoAnt = CustomerInfoListAnt.next();
            if (custInfoAnt.getUserinfo().getEnterday().indexOf(onYearStr) != -1) {
                flag++;
            }
            //清除去年离职 张建波 番正聪志
            if (StringUtils.isNullOrEmpty(custInfoAnt.getUserinfo().getResignation_date())
                    && !custInfoAnt.getUserid().equals("5e78b2574e3b194874181099")
                    && !custInfoAnt.getUserid().equals("5e78fefff1560b363cdd6db7")) {
                PersonalCost personalCost = new PersonalCost();
                personalCost.setYearsantid(personalCostYerasid);
                personalCost.preInsert(tokenModel);
                personalCost.setPersonalcostid(UUID.randomUUID().toString());
                personalCost.setCenterid(custInfoAnt.getUserinfo().getCenterid());
                if (StringUtils.isNullOrEmpty(custInfoAnt.getUserinfo().getGroupid())) {
                    personalCost.setGroupid(admn);
                } else {
                    personalCost.setGroupid(custInfoAnt.getUserinfo().getGroupid());
                }
                personalCost.setUserid(custInfoAnt.getUserid());
                personalCost.setUsername(custInfoAnt.getUserinfo().getCustomername());
                personalCost.setDepartshort(custInfoAnt.getUserinfo().getBudgetunit());
                //配付
                if (allotmentList.contains(custInfoAnt.getUserinfo().getGroupid())) {
                    personalCost.setAllotment("PR068002");
                } else {
                    personalCost.setAllotment("PR068001");
                }
                personalCost.setNewpersonaldate("-");
                personalCost.setExrank(custInfoAnt.getUserinfo().getRank());
                personalCost.setChangerank("PR069001");
                personalCost.setBasicallyant(basicallMap.get(custInfoAnt.getUserinfo().getRank()));
                personalCost.setResponsibilityant(responsibilityMap.get(custInfoAnt.getUserinfo().getRank()));
                BigDecimal basicallyAntal = new BigDecimal(personalCost.getBasicallyant());
                BigDecimal responsibilityAntal = new BigDecimal(personalCost.getResponsibilityant());
                String monthlysalaryle = (basicallyAntal.add(responsibilityAntal)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                //月度工资
                personalCost.setMonthlysalary(monthlysalaryle);
                personalCost.setAllowanceant(allowanceantMap.get(custInfoAnt.getUserinfo().getRank()));
                personalCost.setOtherantone(expandOneMap.get(custInfoAnt.getUserinfo().getRank()));
                personalCost.setOtheranttwo(expandTwoMap.get(custInfoAnt.getUserinfo().getRank()));
                personalCost.setOnlychild(onlyChild);
                System.out.println(custInfoAnt.getUserinfo().getCustomername());
                System.out.println(custInfoAnt.getUserinfo().getDlnation());
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(custInfoAnt.getUserinfo().getDlnation())) {
                    personalCost.setQnbt(custInfoAnt.getUserinfo().getDlnation().equals("0") ? "0" : qnbtMap.get(custInfoAnt.getUserinfo().getRank()));
                } else {
                    personalCost.setQnbt("0");
                }
                BigDecimal allowanceAntal = new BigDecimal(personalCost.getAllowanceant());
                BigDecimal qnbtAntal = new BigDecimal(personalCost.getQnbt());
                BigDecimal otherantOneAntal = new BigDecimal(personalCost.getOtherantone());
                BigDecimal otherantTwoAntal = new BigDecimal(personalCost.getOtheranttwo());
                BigDecimal onlyChildAntal = new BigDecimal(personalCost.getOnlychild());
                String totalsubsidiesle = ((((allowanceAntal.add(qnbtAntal)).add(otherantOneAntal).add(otherantTwoAntal)).add(onlyChildAntal))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                //补贴总计
                personalCost.setTotalsubsidies(totalsubsidiesle);
                personalCost.setMonthlybonusmonths(monthlyBonusMonthsMap.get(custInfoAnt.getUserinfo().getRank()));
                BigDecimal mbmal = new BigDecimal(personalCost.getMonthlybonusmonths());
                String monthlybonusle = ((mbmal.multiply(basicallyAntal)).divide(twelveAnt, 2, BigDecimal.ROUND_HALF_UP)).toString();
                //月度奖金
                personalCost.setMonthlybonus(monthlybonusle);
                personalCost.setAnnualbonusmonths(allowanceantMap.get(custInfoAnt.getUserinfo().getRank()));
                BigDecimal abmal = new BigDecimal(personalCost.getAnnualbonusmonths());
                String annualbonusle = ((abmal.multiply(basicallyAntal)).divide(twelveAnt, 2, BigDecimal.ROUND_HALF_UP)).toString();
                //年度奖金
                personalCost.setAnnualbonus(annualbonusle);
                //工资总额
                BigDecimal mal = new BigDecimal(personalCost.getMonthlysalary());
                BigDecimal tal = new BigDecimal(personalCost.getTotalsubsidies());
                BigDecimal mgal = new BigDecimal(personalCost.getMonthlybonus());
                BigDecimal ygal = new BigDecimal(personalCost.getAnnualbonus());
                String twle = (((mal.add(tal)).add(mgal)).add(ygal)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setTotalwages(twle);
                BigDecimal tlwal = new BigDecimal(personalCost.getTotalwages());
                //工会经费
                BigDecimal total = new BigDecimal(personalCost.getTotalwages());
                String tradfel = ((total.subtract(qnbtAntal)).subtract(onlyChildAntal)).multiply(unionAnt).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setTradeunionfunds(tradfel);
                BigDecimal tradfal = new BigDecimal(personalCost.getTradeunionfunds());
                //加班费时给
                String otp = ((basicallyAntal.divide(mouthworkAnt, 2, BigDecimal.ROUND_HALF_UP)).divide(dayworkAnt, 2, BigDecimal.ROUND_HALF_UP)).multiply(basicAnt).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setOvertimepay(otp);
                //加班小时数
                personalCost.setOvertimehour(overtimehourMap.get(custInfoAnt.getUserinfo().getRank()));
                //是否大连户籍 1-是 0否
                personalCost.setIndalian(custInfoAnt.getUserinfo().getDlnation());
                //养老保险基4
                personalCost.setOldylbxjaj(custInfoAnt.getUserinfo().getYanglaoinsurance());
                BigDecimal oldylal = new BigDecimal(personalCost.getOldylbxjaj());
                BigDecimal oldSum = oldylal.multiply(oldYlAnt);
                //失业保险基4
                personalCost.setLossybxjaj(custInfoAnt.getUserinfo().getShiyeinsurance());
                BigDecimal losssyal = new BigDecimal(personalCost.getLossybxjaj());
                BigDecimal losssySum = losssyal.multiply(lossSyAnt);
                //工伤保险基4
                personalCost.setGsbxjaj(custInfoAnt.getUserinfo().getGongshanginsurance());
                BigDecimal gsal = new BigDecimal(personalCost.getGsbxjaj());
                BigDecimal gsSum = gsal.multiply(gSAnt);
                //生育保险基4
                personalCost.setSybxjaj(custInfoAnt.getUserinfo().getShengyuinsurance());
                BigDecimal syal = new BigDecimal(personalCost.getSybxjaj());
                BigDecimal sySum = syal.multiply(sYAnt);
                //医疗保险基4
                personalCost.setYlbxjaj(custInfoAnt.getUserinfo().getYanglaoinsurance());
                BigDecimal ylal = new BigDecimal(personalCost.getYlbxjaj());
                BigDecimal ylSum = ylal.multiply(yLAnt);
                //公积金基数4
                personalCost.setGjjjsaj(custInfoAnt.getUserinfo().getGongshanginsurance());
                BigDecimal gjjal = new BigDecimal(personalCost.getGjjjsaj());
                //社保企业4
                String sbqyle = oldSum.add(losssySum).add(gsSum).add(sySum).add(ylSum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setSbgsaj(sbqyle);
                BigDecimal sbqial = new BigDecimal(personalCost.getSbgsaj());
                //大病险
                personalCost.setDbxaj(dbXAnt);
                BigDecimal dbxal = new BigDecimal(personalCost.getDbxaj());
                //社保公司4=社保企业+取暖补贴+大病险
                String sbgsle = sbqial.add(qnbtAntal).add(dbxal).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setSbgsaj(sbgsle);
                BigDecimal sbgsal = new BigDecimal(personalCost.getSbgsaj());
                //公积金公司负担4
                String gjjgsfdle = gjjal.multiply(gJjAnt).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setGjjgsfdaj(gjjgsfdle);
                BigDecimal gjjgsfdal = new BigDecimal(personalCost.getGjjgsfdaj());
                //4月-6月 工资总额+社保公司负担总计+公积金公司负担总计+工会经费。
                String aptojule = tlwal.add(sbgsal).add(gjjgsfdal).add(tradfal).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setAptoju(aptojule);

                //7月-3月
                //计算基数
                //工资总额 alltotalwagesSum
                List<String> comtotalwagesList = wagesMapper.getComtotalwages(custInfoAnt.getUserid(), onYearStr);
                BigDecimal alltotalwagesSum = new BigDecimal("0.00");
                for (String com : comtotalwagesList) {
                    BigDecimal comal = new BigDecimal(com);
                    alltotalwagesSum = alltotalwagesSum.add(comal);
                }
                //判断是否本年入职
                String Monthout = "0";
                if (flag != 0) {
                    String enteryday = custInfoAnt.getUserinfo().getEnterday().replaceAll("/", "-").substring(0, 10);
                    String mounthStr = enteryday.substring(5, 7);
                    String workMount = String.valueOf(12 - Integer.valueOf(mounthStr));
                    BigDecimal workMountB = new BigDecimal(workMount);
                    LocalDate workDay = LocalDate.parse(enteryday);
                    int lenghtDay = workDay.lengthOfMonth();
                    BigDecimal lenghtDayB = new BigDecimal(String.valueOf(lenghtDay));
                    String joinDay = enteryday.substring(7);
                    //入职月工作多少天
                    String workDayOnWork = String.valueOf(lenghtDay - Integer.valueOf(joinDay) + 1);
                    BigDecimal workDayOnWorkB = new BigDecimal(workDayOnWork);
                    //出勤系数
                    BigDecimal workDayResult = (workDayOnWorkB.divide(lenghtDayB, 2, BigDecimal.ROUND_HALF_UP)).add(workMountB);
                    Monthout = workDayResult.toString();
                } else {
                    Monthout = "12";
                }
                BigDecimal lastMonthWage = new BigDecimal("0");
                if (comtotalwagesList.size() != 0) {
                    lastMonthWage = new BigDecimal(comtotalwagesList.get(comtotalwagesList.size() - 1));
                }
                alltotalwagesSum = alltotalwagesSum.add(lastMonthWage);
                //小计3= 采暖费+独生子女费
                List<String> totalThreeList = wagesMapper.getTotal(custInfoAnt.getUserid(), onYearStr);
                BigDecimal totalThreeSum = new BigDecimal("0.00");
                for (String tothree : totalThreeList) {
                    BigDecimal comal = new BigDecimal(tothree);
                    totalThreeSum = totalThreeSum.add(comal);
                }
                BigDecimal lastMonthTotal = new BigDecimal("0");
                if (totalThreeList.size() != 0) {
                    lastMonthTotal = new BigDecimal(totalThreeList.get(totalThreeList.size() - 1));
                }
                totalThreeSum = totalThreeSum.add(lastMonthTotal);
                //本年总计--计算7到3月基数
                BigDecimal allMount = new BigDecimal(Monthout);
                BigDecimal totalSum = (alltotalwagesSum.subtract(totalThreeSum)).divide(allMount, 2, BigDecimal.ROUND_HALF_UP);
                //小于下限取下限 养老
                if (totalSum.compareTo(oldYlLower) < 1) {
                    personalCost.setOldylbxjjm(oldYlLower.toString());
                    //大于上限取上限 养老
                } else if (totalSum.compareTo(oldYlUpper) > -1) {
                    personalCost.setOldylbxjjm(oldYlUpper.toString());
                } else {
                    personalCost.setOldylbxjjm(totalSum.toString());
                }
                //小于下限取下限 失业
                if (totalSum.compareTo(lossSyLower) < 1) {
                    personalCost.setLossybxjjm(lossSyLower.toString());
                    //大于上限取上限 失业
                } else if (totalSum.compareTo(lossSyUpper) > -1) {
                    personalCost.setLossybxjjm(lossSyUpper.toString());
                } else {
                    personalCost.setLossybxjjm(totalSum.toString());
                }
                //小于下限取下限 工伤
                if (totalSum.compareTo(gSLower) < 1) {
                    personalCost.setGsbxjjm(gSLower.toString());
                    //大于上限取上限 工伤
                } else if (totalSum.compareTo(gSUpper) > -1) {
                    personalCost.setGsbxjjm(gSUpper.toString());
                } else {
                    personalCost.setGsbxjjm(totalSum.toString());
                }
                //小于下限取下限 生育
                if (totalSum.compareTo(sYLower) < 1) {
                    personalCost.setSybxjjm(sYLower.toString());
                    //大于上限取上限 生育
                } else if (totalSum.compareTo(sYUpper) > -1) {
                    personalCost.setSybxjjm(sYUpper.toString());
                } else {
                    personalCost.setSybxjjm(totalSum.toString());
                }
                //小于下限取下限 医疗
                if (totalSum.compareTo(yLLower) < 1) {
                    personalCost.setYlbxjjm(yLLower.toString());
                    //大于上限取上限 医疗
                } else if (totalSum.compareTo(yLUpper) > -1) {
                    personalCost.setYlbxjjm(yLUpper.toString());
                } else {
                    personalCost.setYlbxjjm(totalSum.toString());
                }
                //小于下限取下限 公积金
                if (totalSum.compareTo(gJjLower) < 1) {
                    personalCost.setGjjjsjm(gJjLower.toString());
                    //大于上限取上限 公积金
                } else if (totalSum.compareTo(gJjUpper) > -1) {
                    personalCost.setGjjjsjm(gJjUpper.toString());
                } else {
                    personalCost.setGjjjsjm(totalSum.toString());
                }
                //社保企业7
                //养老保险基数
                BigDecimal oldylal7 = new BigDecimal(personalCost.getOldylbxjjm());
                BigDecimal oldSum7 = oldylal7.multiply(oldYlAnt);
                //失业保险基数
                BigDecimal losssyal7 = new BigDecimal(personalCost.getLossybxjjm());
                BigDecimal losssySum7 = losssyal7.multiply(lossSyAnt);
                //工伤保险基数
                BigDecimal gsal7 = new BigDecimal(personalCost.getGsbxjjm());
                BigDecimal gsSum7 = gsal7.multiply(gSAnt);
                //生育保险基数
                BigDecimal syal7 = new BigDecimal(personalCost.getSybxjjm());
                BigDecimal sySum7 = syal7.multiply(sYAnt);
                //医疗保险基数
                BigDecimal ylal7 = new BigDecimal(personalCost.getYlbxjaj());
                BigDecimal ylSum7 = ylal7.multiply(yLAnt);
                String sbqyle7 = oldSum7.add(losssySum7).add(gsSum7).add(sySum7).add(ylSum7).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setSbqyjm(sbqyle7);
                BigDecimal sbqyal7 = new BigDecimal(personalCost.getSbqyjm());
                //公积金基数
                BigDecimal gjjjsal7 = new BigDecimal(personalCost.getGjjjsjm());
                //大病险
                personalCost.setDbxjm(dbXAnt);
                BigDecimal dbxal7 = new BigDecimal(personalCost.getDbxjm());
                //社保公司7=社保企业+取暖补贴+大病险
                String sbgsle7 = sbqyal7.add(qnbtAntal).add(dbxal7).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setSbgsjm(sbgsle7);
                BigDecimal sbgsal7 = new BigDecimal(personalCost.getSbgsjm());
                //公积金公司负担7
                String gjjgsfdle7 = gjjjsal7.multiply(gJjAnt).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setGjjgsfdjm(gjjgsfdle7);
                BigDecimal gjjgsfdal7 = new BigDecimal(personalCost.getGjjgsfdjm());
                //7月-3月 工资总额+社保公司负担总计+公积金公司负担总计+工会经费。
                String aptojule7 = tlwal.add(sbgsal7).add(gjjgsfdal7).add(tradfal).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                personalCost.setJutoma(aptojule7);
                personalCostMapper.insert(personalCost);
            }
        }
    }

    @Override
    public List<String> getGroupId(String yearsantid) throws Exception {
        List<String> groupIdList = new ArrayList<>();
        groupIdList = personalCostMapper.getGroupId(yearsantid);
        return groupIdList;
    }

    @Override
    public List<Dictionary> getChangeRanks() throws Exception {
        List<Dictionary> dictionaryRankList = dictionaryService.getForSelect("PR021");
        return dictionaryRankList;
    }

    @Override
    public List<PersonalCost> getPersonalCost(String groupid, String yearsantid) throws Exception {
        List<PersonalCost> personalCostList = new ArrayList<>();
        if (groupid != "全部") {
            personalCostList = personalCostMapper.selectPersonalCost(groupid, yearsantid);
        } else {
            personalCostList = personalCostMapper.selectAll();
        }
        return personalCostList;
    }


    private OrgTree getCurrentOrg(OrgTree org,String orgId) throws Exception {
        if (org.get_id().equals(orgId)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    OrgTree or = getCurrentOrg(item,orgId);
                    if (or.get_id().equals(orgId)) {
                        return or;
                    }
                }
            }

        }
        return org;
    }

    @Override
    public List<PersonalCostBmSum> gettableBm(String yearsantid) throws Exception {
        List<PersonalCostBmSum> pcbslist = new ArrayList<>();
        List<String> groupList = personalCostMapper.getGroupId(yearsantid);
        OrgTree org = new OrgTree();
        org.setStatus("0");
        Query query = CustmizeQuery(org);
        org = mongoTemplate.findOne(query, OrgTree.class);
        List<String> decomposeGruop = new ArrayList<>();
        List<String> removeGroup = new ArrayList<>();
        for(String cerid : groupList){
            OrgTree orgTreeProcess = new OrgTree();
            orgTreeProcess = getCurrentOrg(org,cerid);
            if(orgTreeProcess.getEncoding().isBlank() && orgTreeProcess.getOrgs().size() > 0){
                removeGroup.add(orgTreeProcess.get_id());
                for(OrgTree otre : orgTreeProcess.getOrgs()){
                    decomposeGruop.add(otre.get_id());
                }
            }
        }
        for(String remGp : removeGroup){
            groupList.remove(remGp);
        }
        for (String gr : groupList) {
            PersonalCostBmSum personalCostBmSum = personalCostMapper.getPersonalCostBmSum(gr, yearsantid);
            if(personalCostBmSum.getDepartshortBmSum() == null){
                continue;
            }
            personalCostBmSum.setExrankBmSum("合计");
            pcbslist.add(personalCostBmSum);
            List<String> groupinRanks = personalCostMapper.getGroupinRanks(gr, yearsantid);
            for (String rank : groupinRanks) {
                pcbslist.add(personalCostMapper.getPersonalCostSum(gr, rank, yearsantid));
            }
        }
        for (String decGp : decomposeGruop) {
            PersonalCostBmSum personalCostBmSum = personalCostMapper.getPersonalCostBmSumGs(decGp, yearsantid);
            if(personalCostBmSum.getDepartshortBmSum() == null ){
                continue;
            }
            personalCostBmSum.setExrankBmSum("合计");
            pcbslist.add(personalCostBmSum);
            List<String> groupinRanks = personalCostMapper.getGroupinRanksGp(decGp, yearsantid);
            for (String rank : groupinRanks) {
                pcbslist.add(personalCostMapper.getPersonalCostSumGp(decGp, rank, yearsantid));
            }
        }
        return pcbslist;
    }

    @Override
    public List<PersonalCostGsSum> gettableGs(String yearsantid) throws Exception {
        List<PersonalCostGsSum> personalCostGsSumList = new ArrayList<>();
        PersonalCostGsSum personalCostGsSum = personalCostMapper.getPersonalCostGsSum(yearsantid);
        personalCostGsSum.setExrankGsSum("合计");
        personalCostGsSumList.add(personalCostGsSum);
        List<String> cominRanks = personalCostMapper.getCominRanks(yearsantid);

        for (String rank : cominRanks) {
            personalCostGsSumList.add(personalCostMapper.getPersonalCostGSum(rank, yearsantid));
        }
        return personalCostGsSumList;
    }

    @Override
    public List<PersonalCostRb> gettableRb(String yearsantid) throws Exception {
        List<PersonalCostRb> personalCostList = personalCostMapper.getPersonalCostRb(yearsantid);
        return personalCostList;
    }

















    @Override
    public List<PersonalCostYears> getPerCostYarList(PersonalCostYears personalCostYears) {
        return personalCostYearsMapper.select(personalCostYears);
    }

    @Override
    public PersonalCost insertPenalcost(String year, TokenModel tokenModel) throws Exception {
//        year = "2020";
//        //Rank给予标准
//        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
//        Map<String, String> rankMap = new HashMap<>();
//        //工资涨幅百分比
//        Map<String, String> ranktageMap = new HashMap<>();
//        for (Dictionary dic : dictionaryRank) {
//            rankMap.put(dic.getCode(), dic.getValue2());
//            ranktageMap.put(dic.getCode(), dic.getValue4());
//        }
//        Query query = new Query();
//        String workday = null;
//        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
//        List<CustomerInfo> customerInfos = new ArrayList<CustomerInfo>();
//        customerInfos.addAll(mongoTemplate.find(query, CustomerInfo.class));
//        //年度
//        PersonalCostYears personalCostYears = new PersonalCostYears();
//        personalCostYears.preInsert(tokenModel);
//        String personalCostYerasid = UUID.randomUUID().toString();
//        //暂时处理
//        personalCostYears.setYears(year);
//        personalCostYears.setYearsantid(personalCostYerasid);
//        personalCostYearsMapper.insert(personalCostYears);
//        //取去年【产休人员】名单
//        List<String> birthuseridList = personalCostMapper.selectBirthUserid("2020");
//        int flag = 0;
//        for (Iterator<CustomerInfo> custList = customerInfos.iterator(); custList.hasNext(); ) {
//            CustomerInfo customerInfoAnt = custList.next();
//            //清除去年离职
//            if (StringUtils.isNullOrEmpty(customerInfoAnt.getUserinfo().getResignation_date())
//                    && !customerInfoAnt.getUserid().equals("5e78b2574e3b194874181099")
//                    && !customerInfoAnt.getUserid().equals("5e78fefff1560b363cdd6db7")) {
//                for (String birthuserid : birthuseridList) {
//                    if (birthuserid.equals(customerInfoAnt.getUserid()))
//                    {
//                        flag ++;
//                    }
//                }
//                    if (flag == 0) //排除产休
//                    {
//                        PersonalCost personalCost = new PersonalCost();
//                        //token
//                        personalCost.preInsert(tokenModel);
//                        personalCost.setPersonalcostid(UUID.randomUUID().toString());
//                        //年度
//                        personalCost.setYearsantid(personalCostYerasid);
//                        personalCost.setCenterid(customerInfoAnt.getUserinfo().getCenterid());
//                        personalCost.setGroupid(customerInfoAnt.getUserinfo().getGroupid());
//                        //userid
//                        personalCost.setUserid(customerInfoAnt.getUserid());
//                        //7~3月人件费 1月1日自动生成
//                        Double lastAnt = 0.00;
//                        if (StrUtil.isNotBlank(customerInfoAnt.getUserinfo().getBasic()) && StrUtil.isNotBlank(customerInfoAnt.getUserinfo().getDuty())) {
//                            BigDecimal Basic = BigDecimal.valueOf(Double.valueOf(customerInfoAnt.getUserinfo().getBasic()));
//                            BigDecimal duty = BigDecimal.valueOf(Double.valueOf(customerInfoAnt.getUserinfo().getDuty()));
//                            lastAnt = Basic.add(duty).doubleValue();
//                        }
//                        BigDecimal jutomacost = new BigDecimal(lastAnt).setScale(2, ROUND_HALF_UP);
//                        //入职日
//                        if (StrUtil.isNotBlank(customerInfoAnt.getUserinfo().getWorkday())) {
//                            workday = customerInfoAnt.getUserinfo().getWorkday().substring(0, 10);
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTime(Convert.toDate(workday));
//                            if (customerInfoAnt.getUserinfo().getWorkday().length() >= 24) {
//                                cal.add(Calendar.DAY_OF_YEAR, 1);
//                            }
//                            workday = s.format(cal.getTime());
//                        }
//                        personalCost.setWorkday(workday);
//                        personalCost.setJutomacost(jutomacost.toString());
//                        //rank
//                        personalCost.setPrank(customerInfoAnt.getUserinfo().getRank());
//                        //计算基数
//                        List<String> comtotalwagesList = wagesMapper.getComtotalwages(customerInfoAnt.getUserid(), "2020");
//                        String comtotalwagesSum = "";
//                        BigDecimal comtotalwages = new BigDecimal(0.0000);
//                        for (String com : comtotalwagesList) {
//                            if (StrUtil.isNotBlank(com)) {
//                                BigDecimal comAnt = BigDecimal.valueOf(Double.valueOf(com));
//                                comtotalwages = comtotalwages.add(comAnt);
//                            }
//                        }
//                        //计算小计
//                        List<String> totalList = wagesMapper.getTotal(customerInfoAnt.getUserid(), "2020");
//                        String totalsum = "";
//                        BigDecimal total = new BigDecimal(0.0000);
//                        for (String tota : totalList) {
//                            if (StrUtil.isNotBlank(tota)) {
//                                BigDecimal comAnt = BigDecimal.valueOf(Double.valueOf(tota));
//                                total = total.add(comAnt);
//                            }
//                        }
//                        LocalDate today = LocalDate.now();
//                        LocalDate yesteryear = today.plusYears(-1);
//                        //去年
//                        String lastYear = String.valueOf(yesteryear.getYear());
//                        List<Dictionary> dictionaryL = dictionaryService.getForSelect("PR067");
//                        //新年度自动跑
//                        if (!workday.substring(0, 4).equals(lastYear)) //一整年
//                        {
//                            BigDecimal allYear = new BigDecimal("12");
//                            BigDecimal ctAnt = comtotalwages.subtract(total);
//                            Bonussend bonussend = new Bonussend();
//                            bonussend.setUser_id(customerInfoAnt.getUserid());
//                            List<Bonussend> bonussendList =  bonussendMapper.select(bonussend);
//                            String bonussendMoney = "";
//                            if(bonussendList.size() != 0){
//                                bonussendMoney  = bonussendList.get(0).getTotalbonus1();
//                                BigDecimal bonMoney = new BigDecimal(bonussendMoney);
//                            }
//                            BigDecimal btAnt = ctAnt.add(ctAnt);
//                            Double baseresultAnt = btAnt.divide(allYear, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                            //养老
//                            if (Double.valueOf(dictionaryL.get(0).getValue2()) > baseresultAnt)//比最低金额小
//                            {
//                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue2());
//                            } else if (Double.valueOf(dictionaryL.get(0).getValue3()) < baseresultAnt)//比最高金额大
//                            {
//                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue3());
//                            } else {
//                                personalCost.setYangbaseresult(baseresultAnt.toString());
//                            }
//                            //医疗
//                            if (Double.valueOf(dictionaryL.get(1).getValue2()) > baseresultAnt)//比最低金额小
//                            {
//                                personalCost.setYibaseresult(dictionaryL.get(1).getValue2());
//                            } else if (Double.valueOf(dictionaryL.get(1).getValue3()) < baseresultAnt)//比最高金额大
//                            {
//                                personalCost.setYibaseresult(dictionaryL.get(1).getValue3());
//                            } else {
//                                personalCost.setYibaseresult(baseresultAnt.toString());
//                            }
//                            //公积金
//                            if (Double.valueOf(dictionaryL.get(2).getValue2()) > baseresultAnt)//比最低金额小
//                            {
//                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue2());
//                                ;
//                            } else if (Double.valueOf(dictionaryL.get(2).getValue3()) < baseresultAnt)//比最高金额大
//                            {
//                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue3());
//                            } else {
//                                personalCost.setZhubaseresult(baseresultAnt.toString());
//                            }
//
//                        } else {
//                            String mounthStr = workday.substring(5, 7);
//                            //整月 workMount
//                            String workMount = String.valueOf(Integer.valueOf("12") - Integer.valueOf(mounthStr));
//                            BigDecimal workMountB = new BigDecimal(workMount);
//
//                            LocalDate workDay = LocalDate.parse(workday);
//                            int lenghtDay = workDay.getDayOfMonth();
//                            BigDecimal lenghtDayB = new BigDecimal(String.valueOf(lenghtDay));
//                            String joinDay = workday.substring(7);
//                            //入职月工作多少天
//                            String workDayOnWork = String.valueOf(lenghtDay - Integer.valueOf(joinDay) + 1);
//                            BigDecimal workDayOnWorkB = new BigDecimal(workDayOnWork);
//                            //出勤系数
//                            BigDecimal workDayResult = (workDayOnWorkB.divide(lenghtDayB, 2, BigDecimal.ROUND_HALF_UP)).add(workMountB);
//                            Double baseresultAnt = ((comtotalwages.subtract(total)).divide(workDayResult, 2, BigDecimal.ROUND_HALF_UP)).doubleValue();
//                            //养老
//                            if (Double.valueOf(dictionaryL.get(0).getValue2()) > baseresultAnt)//比最低金额小
//                            {
//                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue2());
//                            } else if (Double.valueOf(dictionaryL.get(0).getValue3()) < baseresultAnt)//比最高金额大
//                            {
//                                personalCost.setYangbaseresult(dictionaryL.get(0).getValue3());
//                            } else {
//                                personalCost.setYangbaseresult(baseresultAnt.toString());
//                            }
//                            //医疗
//                            if (Double.valueOf(dictionaryL.get(1).getValue2()) > baseresultAnt)//比最低金额小
//                            {
//                                personalCost.setYibaseresult(dictionaryL.get(1).getValue2());
//                            } else if (Double.valueOf(dictionaryL.get(1).getValue3()) < baseresultAnt)//比最高金额大
//                            {
//                                personalCost.setYibaseresult(dictionaryL.get(1).getValue3());
//                            } else {
//                                personalCost.setYibaseresult(baseresultAnt.toString());
//                            }
//                            //公积金
//                            if (Double.valueOf(dictionaryL.get(2).getValue2()) > baseresultAnt)//比最低金额小
//                            {
//                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue2());
//                                ;
//                            } else if (Double.valueOf(dictionaryL.get(2).getValue3()) < baseresultAnt)//比最高金额大
//                            {
//                                personalCost.setZhubaseresult(dictionaryL.get(2).getValue3());
//                            } else {
//                                personalCost.setZhubaseresult(baseresultAnt.toString());
//                            }
//                        }
//
//
//                        //给予标准
//                        String monthCost = rankMap.get(customerInfoAnt.getUserinfo().getRank());
//                        BigDecimal monthCostBal = new BigDecimal(monthCost);
//                        String tageAnt = ranktageMap.get(customerInfoAnt.getUserinfo().getRank());
//                        tageAnt = tageAnt.substring(0,tageAnt.length() - 1);
//                        BigDecimal tageAntBal = new BigDecimal(tageAnt);
//                        BigDecimal bdal = new BigDecimal("100");
//                        Double newWorkCost = ((monthCostBal.multiply(tageAntBal)).divide(bdal,2, BigDecimal.ROUND_HALF_UP).add(monthCostBal)).doubleValue();
//                        personalCost.setAprilcosty(newWorkCost.toString());
//                        personalCost.setMarchcosty(newWorkCost.toString());
//                        personalCost.setJunecosty(newWorkCost.toString());
//                        personalCost.setJulycosty(newWorkCost.toString());
//                        personalCost.setAugustcosty(newWorkCost.toString());
//                        personalCost.setSeptembercosty(newWorkCost.toString());
//                        personalCost.setOctobercosty(newWorkCost.toString());
//                        personalCost.setNovembercosty(newWorkCost.toString());
//                        personalCost.setDecembercosty(newWorkCost.toString());
//                        personalCost.setJanuarycosty(newWorkCost.toString());
//                        personalCost.setJanuarycosty(newWorkCost.toString());
//                        personalCost.setFebruarycosty(newWorkCost.toString());
//                        personalCost.setMarchcosty(newWorkCost.toString());
//                        personalCostMapper.insert(personalCost);
//                    }
//                    else{ //产休人员
//                        flag = 0;
//                    }
//                }
//            custList.remove();
//            }
        return null;
    }


//    @Override
//    public List<PersonalCost> getPersonalCost(String groupid, String yearsantid) throws Exception {
//        List<CustomerInfo> customerInfoList = mongoTemplate.find(new Query(Criteria.where("userinfo.groupid").is(groupid)), CustomerInfo.class);
//        List<PersonalCost> personalCostList = personalCostMapper.selectPersonalCostResult(customerInfoList, yearsantid);
//        return personalCostList;
//    }

    @Override
    public void upPersonalCost(List<PersonalCost> personalCostList, TokenModel tokenModel) throws Exception {
        List<PersonalCost> changePctList = new ArrayList<>();
        for(PersonalCost pct : personalCostList){
            PersonalCost pctFind = new PersonalCost();
            List<PersonalCost> pctFindList = new ArrayList<>();
            pctFind.setPersonalcostid(pct.getPersonalcostid());
            pctFindList = personalCostMapper.select(pctFind);
            if(pctFindList.get(0).getLtrank() != pct.getLtrank()){
                changePctList.add(pct);
            }
        }
        changePctWork(changePctList);
        personalCostMapper.updatePersonalCost(personalCostList, tokenModel);
    }

    private void changePctWork(List<PersonalCost> changePctListAnt) throws Exception{
        /*
         * 基本给\职责给\月工资\一括补贴\取暖补贴\扩展项补贴(午餐)
         * 扩展项补贴(交通)\补贴总计\月度奖金月数\月度奖金\年度奖金月数\年度奖金
         * 工资总额\工会经费\加班费时给\社保公司负担总计\4月-6月人件费\7月-3月人件费\加班小时数
         * */
        BigDecimal twelveAnt = new BigDecimal("12");
        //Rank各种标准
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        //基本给
        Map<String, String> basicallMap = new HashMap<>();
        //职责给
        Map<String, String> responsibilityMap = new HashMap<>();
        //一括补贴
        Map<String, String> allowanceantMap = new HashMap<>();
        //取暖补贴
        Map<String, String> qnbtMap = new HashMap<>();
        //扩展项补贴(午餐)
        Map<String, String> expandOneMap = new HashMap<>();
        //扩展项补贴(交通)
        Map<String, String> expandTwoMap = new HashMap<>();
        //月度奖金月数
        Map<String, String> monthlyBonusMonthsMap = new HashMap<>();
        //年度奖金月数
        Map<String, String> annualBonusMonthsMap = new HashMap<>();
        //加班小时数
        Map<String, String> overtimehourMap = new HashMap<>();
        //工会比重
        List<Dictionary> dictionaryUnion = dictionaryService.getForSelect("PR070");
        BigDecimal unionAnt = new BigDecimal(dictionaryUnion.get(0).getValue1());
        //加班时给
        List<Dictionary> dictionaryOver = dictionaryService.getForSelect("PR071");
        BigDecimal muwokBig = new BigDecimal(dictionaryOver.get(0).getValue1());
        BigDecimal daowkBig = new BigDecimal(dictionaryOver.get(0).getValue2());
        BigDecimal basesBig = new BigDecimal(dictionaryOver.get(0).getValue3());
        for (Dictionary dic : dictionaryRank) {
            //基本给2
            basicallMap.put(dic.getCode(), dic.getValue2());
            //职责给3
            responsibilityMap.put(dic.getCode(), dic.getValue3());
            //一括补贴6
            allowanceantMap.put(dic.getCode(), dic.getValue6());
            //取暖补贴7
            qnbtMap.put(dic.getCode(), dic.getValue7());
            //拓展项1 8
            expandOneMap.put(dic.getCode(), dic.getValue8());
            //拓展项2 9
            expandTwoMap.put(dic.getCode(), dic.getValue9());
            //奖金计上（月度）4
            monthlyBonusMonthsMap.put(dic.getCode(), dic.getValue4());
            //奖金计上（年度）5
            annualBonusMonthsMap.put(dic.getCode(), dic.getValue5());
            //加班小时数 10
            overtimehourMap.put(dic.getCode(), dic.getValue10());
        }

        for(PersonalCost pcst : changePctListAnt){
            String perranks = pcst.getLtrank();
            //基本给
            String basic = basicallMap.get(perranks);
            pcst.setBasicallyant(basic);
            //职责给
            String respo = responsibilityMap.get(perranks);
            pcst.setResponsibilityant(respo);
            //月工资
            BigDecimal basicBig = new BigDecimal(basic);
            BigDecimal respoBig = new BigDecimal(respo);
            String monthlysalaryle = (basicBig.add(respoBig)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            pcst.setMonthlysalary(monthlysalaryle);
            BigDecimal monthlyBig = new BigDecimal(monthlysalaryle);
            //一括补贴
            String allce = allowanceantMap.get(perranks);
            pcst.setAllowanceant(allce);
            //取暖补贴-旧
            String qnuanOld = pcst.getQnbt();
            BigDecimal qnOldBig = new BigDecimal(qnuanOld);
            //取暖补贴-新
            String qnuan = qnbtMap.get(perranks);
            pcst.setQnbt(qnuan);
            //扩展项补贴(午餐)
            String otone = expandOneMap.get(perranks);
            pcst.setOtherantone(otone);
            //扩展项补贴(交通)
            String ottwo = expandTwoMap.get(perranks);
            pcst.setOtheranttwo(ottwo);
            //补贴总计
            BigDecimal allceBig = new BigDecimal(allce);
            BigDecimal qnuanBig = new BigDecimal(qnuan);
            BigDecimal otoneBig = new BigDecimal(otone);
            BigDecimal ottwoBig = new BigDecimal(ottwo);
            BigDecimal oncldBig = new BigDecimal(pcst.getOnlychild());
            String tosub = ((((allceBig.add(qnuanBig)).add(otoneBig).add(ottwoBig)).add(oncldBig))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            pcst.setTotalsubsidies(tosub);
            BigDecimal tosubBig = new BigDecimal(tosub);
            //月度奖金月数
            String monbom = monthlyBonusMonthsMap.get(perranks);
            pcst.setMonthlybonusmonths(monbom);
            //月度奖金
            BigDecimal monbomBig = new BigDecimal(monbom);
            String monbos = (monbomBig.multiply(basicBig)).divide(twelveAnt,2, RoundingMode.HALF_UP).toString();
            pcst.setMonthlybonus(monbos);
            BigDecimal monbosBig = new BigDecimal(monbos);
            //年度奖金月数
            String yearbom = annualBonusMonthsMap.get(perranks);
            pcst.setAnnualbonusmonths(yearbom);
            //年度奖金
            BigDecimal yearbomBig = new BigDecimal(yearbom);
            String yearbos = (yearbomBig.multiply(basicBig)).divide(twelveAnt,2, RoundingMode.HALF_UP).toString();
            pcst.setAnnualbonus(yearbos);
            BigDecimal yearbosBig = new BigDecimal(yearbos);
            //工资总额 = 月度工资+补贴总计+月度奖金+年度奖金
            BigDecimal totmonOldBig = new BigDecimal(pcst.getTotalwages());
            String totmon =  monthlyBig.add(tosubBig).add(monbosBig).add(yearbosBig).toString();
            pcst.setTotalwages(totmon);
            BigDecimal totmonBig = new BigDecimal(totmon);
            //独生子女费
            BigDecimal olycidBig = new BigDecimal(pcst.getOnlychild());
            //工会经费 = (工资总额-取暖补贴-独生子女费)*0.02
            BigDecimal unfdsOldBig = new BigDecimal(pcst.getTradeunionfunds());
            String unfds = ((totmonBig.subtract(qnuanBig)).subtract(olycidBig)).divide(unionAnt,2,RoundingMode.HALF_UP).toString();
            pcst.setTradeunionfunds(unfds);
            BigDecimal unfdsNewBig = new BigDecimal(unfds);
            //加班费时给
            String ovtme = ((basicBig.divide(muwokBig, 2, BigDecimal.ROUND_HALF_UP)).divide(daowkBig, 2, BigDecimal.ROUND_HALF_UP)).multiply(basesBig).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            pcst.setOvertimepay(ovtme);
            //加班小时数
            String ovhor = overtimehourMap.get(perranks);
            pcst.setOvertimehour(ovhor);
            //社保公司负担总计4=社保企业+取暖补贴+大病险
            String sbgsFOld = pcst.getSbgsaj();
            BigDecimal sbgsFOldBig = new BigDecimal(sbgsFOld);
            String sbgsFNew = sbgsFOldBig.subtract(qnOldBig).add(qnuanBig).toString();
            pcst.setSbgsaj(sbgsFNew);
            BigDecimal sbgsFNewBig = new BigDecimal(sbgsFNew);
            //4月-6月 工资总额+社保公司负担总计+公积金公司负担总计+工会经费。
            BigDecimal aptojuBig = new BigDecimal(pcst.getAptoju());
            String aptijuNew = aptojuBig.subtract(totmonOldBig).subtract(unfdsOldBig).subtract(sbgsFOldBig).add(totmonBig).add(unfdsNewBig).add(sbgsFNewBig).toString();
            pcst.setAptoju(aptijuNew);
            //社保公司负担总计7=社保企业+取暖补贴+大病险
            String sbgsSOld = pcst.getSbgsjm();
            BigDecimal sbgsSOldBig = new BigDecimal(sbgsSOld);
            String sbgsSNew = sbgsSOldBig.subtract(qnOldBig).add(qnuanBig).toString();
            pcst.setSbgsaj(sbgsSNew);
            BigDecimal sbgsSNewBig = new BigDecimal(sbgsSNew);
            //7月-3月 工资总额+社保公司负担总计+公积金公司负担总计+工会经费。
            BigDecimal jutomaBig = new BigDecimal(pcst.getJutoma());
            String jutomaNew = jutomaBig.subtract(totmonOldBig).subtract(unfdsOldBig).subtract(sbgsSOldBig).add(totmonBig).add(unfdsNewBig).add(sbgsSNewBig).toString();
            pcst.setJutoma(jutomaNew);
            personalCostMapper.updateByPrimaryKey(pcst);
        }
    }

    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-start
    @Override
    public  List<PersonalCost> getFuzzyQuery(String yearsantid,String username,String allotmentAnt,String group_id,String rnAnt) throws Exception {
        List<PersonalCost> personalCost = new ArrayList<>();
        if (group_id.equals("全部")) {
            group_id = "";
        }
        personalCost = personalCostMapper.getFuzzyQuery(yearsantid,username,allotmentAnt,group_id,rnAnt);
        return personalCost;
    }
    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-end
}
