package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.PersonalCostExpVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.service_pfans.PFANS2000.mapper.BonussendMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostMapper;
import com.nt.service_pfans.PFANS2000.mapper.PersonalCostYearsMapper;
import com.nt.service_pfans.PFANS2000.mapper.WagesMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private RoleService roleService;

    //系统定时任务每月1号自动保存单价
    @Scheduled(cron = "38 25 16 8 9 ?")
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
        List<MembersVo> rolelist = roleService.getMembers("5e785fd38f4316308435112d");
        String user_id = "";
        if (rolelist.size() > 0) {
            user_id = rolelist.get(0).getUserid();
        }

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
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(custInfoAnt.getUserinfo().getEnterday())) {
                if (custInfoAnt.getUserinfo().getEnterday().indexOf(onYearStr) != -1) {
                    flag++;
                }
            }
            //清除去年离职
            if (StringUtils.isNullOrEmpty(custInfoAnt.getUserinfo().getResignation_date())
                    && !custInfoAnt.getUserid().equals(user_id)//去除总经理
                    && !custInfoAnt.getUserid().equals("60c81a94093af30fcce86cd1")//去除出向者
            ) {
                System.out.println(custInfoAnt.getUserinfo().getCustomername());
                PersonalCost personalCost = new PersonalCost();
                personalCost.setYearsantid(personalCostYerasid);
                personalCost.preInsert(tokenModel);
                personalCost.setPersonalcostid(UUID.randomUUID().toString());
                personalCost.setCenterid(custInfoAnt.getUserinfo().getCenterid());
                personalCost.setJobnumber(custInfoAnt.getUserinfo().getJobnumber());
                if (StringUtils.isNullOrEmpty(custInfoAnt.getUserinfo().getGroupid())) {
                    personalCost.setGroupid(admn);
                } else {
                    personalCost.setGroupid(custInfoAnt.getUserinfo().getGroupid());
                }
                personalCost.setUserid(custInfoAnt.getUserid());
                personalCost.setUsername(custInfoAnt.getUserinfo().getCustomername());
                personalCost.setJobnumber(custInfoAnt.getUserinfo().getJobnumber());
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
                personalCost.setAnnualbonusmonths(annualBonusMonthsMap.get(custInfoAnt.getUserinfo().getRank()));
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
                personalCost.setIndalian(!com.mysql.jdbc.StringUtils.isNullOrEmpty(custInfoAnt.getUserinfo().getDlnation()) ? custInfoAnt.getUserinfo().getDlnation() : "0");
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
                personalCost.setSbqyaj(sbqyle);
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


    private OrgTree getCurrentOrg(OrgTree org, String orgId) throws Exception {
        if (org.get_id().equals(orgId)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    OrgTree or = getCurrentOrg(item, orgId);
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
        List<Dictionary> dicList = dictionaryService.getForSelect("PR021");
        List<String> rankList = dicList.stream().map(Dictionary::getCode).collect(Collectors.toList());
        List<DepartmentVo> departmentVoList = new ArrayList<>();
        departmentVoList = orgTreeService.getAllDepartment();
        PersonalCost personalCost = new PersonalCost();
        personalCost.setYearsantid(yearsantid);
        List<PersonalCost> personalCostListList = personalCostMapper.select(personalCost);
        Map<String, Map<String, List<PersonalCost>>> perGroupMap = personalCostListList.stream()
                .filter(ids -> !StringUtils.isNullOrEmpty(ids.getDepartment()) && !StringUtils.isNullOrEmpty(ids.getExrank()))
                .collect(Collectors.groupingBy(PersonalCost::getDepartment,
                        Collectors.groupingBy(PersonalCost::getExrank)));
        for (DepartmentVo dep : departmentVoList) {

        }

        List<PersonalCostBmSum> pcbslist = new ArrayList<>();
        List<String> groupList = personalCostMapper.getGroupId(yearsantid);
        OrgTree org = new OrgTree();
        org.setStatus("0");
        Query query = CustmizeQuery(org);
        org = mongoTemplate.findOne(query, OrgTree.class);
        List<String> decomposeGruop = new ArrayList<>();
        List<String> removeGroup = new ArrayList<>();
        for (String cerid : groupList) {
            OrgTree orgTreeProcess = new OrgTree();
            orgTreeProcess = getCurrentOrg(org, cerid);
            if (orgTreeProcess.getEncoding().isBlank() && orgTreeProcess.getOrgs().size() > 0) {
                removeGroup.add(orgTreeProcess.get_id());
                for (OrgTree otre : orgTreeProcess.getOrgs()) {
                    decomposeGruop.add(otre.get_id());
                }
            }
        }
        for (String remGp : removeGroup) {
            groupList.remove(remGp);
        }
        for (String gr : groupList) {
            PersonalCostBmSum personalCostBmSum = personalCostMapper.getPersonalCostBmSum(gr, yearsantid);
            if (personalCostBmSum.getDepartshortBmSum() == null) {
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
            if (personalCostBmSum.getDepartshortBmSum() == null) {
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
    public List<PersonalCostExpVo> exportinfo(String yearsantid) throws Exception {
        List<PersonalCostExpVo> personalCostExpVoList = personalCostMapper.percostVo(yearsantid);

        Map<String, String> alldicMap = new HashMap<>();
        List<Dictionary> alldicList = dictionaryService.getForSelect("PR068");
        for (Dictionary all : alldicList) {
            alldicMap.put(all.getCode(), all.getValue1());
        }

        Map<String, String> rankdicMap = new HashMap<>();
        List<Dictionary> rankdicList = dictionaryService.getForSelect("PR021");
        for (Dictionary rank : rankdicList) {
            rankdicMap.put(rank.getCode(), rank.getValue1());
        }

        Map<String, String> chankdicMap = new HashMap<>();
        List<Dictionary> chankdicList = dictionaryService.getForSelect("PR069");
        for (Dictionary chank : chankdicList) {
            chankdicMap.put(chank.getCode(), chank.getValue1());
        }

        Map<String, String> inDlMap = new HashMap<>();
        inDlMap.put("1", "是");
        inDlMap.put("0", "否");

//        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (PersonalCostExpVo pctev : personalCostExpVoList) {
//            OrgTree centerName = orgTreeService.getCurrentOrg(orgs,pct.getCenterid());
//            OrgTree groupName = orgTreeService.getCurrentOrg(orgs,pct.getGroupid());
//            pct.setCenterid(centerName.getTitle());
//            pct.setGroupid(groupName.getTitle());
//            List<Dictionary> curListA = dictionaryService.getForSelectCode(pct.getAllotment());
            pctev.setAllotment(alldicMap.get(pctev.getAllotment()));
            pctev.setExrank(rankdicMap.get(pctev.getExrank()));
            pctev.setLtrank(rankdicMap.get(pctev.getLtrank()));
            pctev.setChangerank(chankdicMap.get(pctev.getChangerank()));
            pctev.setIndalian(inDlMap.get(pctev.getChangerank()));
        }
        return personalCostExpVoList;
    }


//    public Map<String,String> getOrgTreeInfo() throws Exception{
//        OrgTree orgs = orgTreeService.get(new OrgTree());
//        Map<String,String> orgsInfoMap = new HashMap<>();
//        if(orgs != null){
//            orgsInfoMap.put(orgs.get_id(),orgs.getTitle());
//            if(orgs.getOrgs() != null){
//                for(OrgTree ot : orgs.getOrgs()){
//
//                }
//            }
//
//        }
//        return orgsInfoMap;
//    }
//
//    public OrgTree getOrgs()


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importPersInfo(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        //配付
        Map<String, String> alldicMap = new HashMap<>();
        List<Dictionary> alldicList = dictionaryService.getForSelect("PR068");
        for (Dictionary all : alldicList) {
            alldicMap.put(all.getValue1(), all.getCode());
        }
        //rank
        Map<String, String> rankdicMap = new HashMap<>();
        List<Dictionary> rankdicList = dictionaryService.getForSelect("PR021");
        for (Dictionary rank : rankdicList) {
            rankdicMap.put(rank.getValue1(), rank.getCode());
        }
        //是否升格升号
        Map<String, String> chankdicMap = new HashMap<>();
        List<Dictionary> chankdicList = dictionaryService.getForSelect("PR069");
        for (Dictionary chank : chankdicList) {
            chankdicMap.put(chank.getValue1(), chank.getCode());
        }
        List<String> Result = new ArrayList<String>();
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        File f = null;
        f = File.createTempFile("tmp", null);
        file.transferTo(f);
        ExcelReader reader = ExcelUtil.getReader(f);
        List<Map<String, Object>> readAll = reader.readAll();
        boolean resultInsUpd = true;
        Map<String, Object> key = readAll.get(0);
        if (key.keySet().toString().trim().substring(0, 55).contains("●")) {
            resultInsUpd = false;
        }
        int k = 1;
        int accesscount = 0;
        int error = 0;
        //新人
        int newAnt = 0;
        List<String> personInfoadd = new ArrayList<String>();
        Map<String, Object> readOne = readAll.get(0);
        String yearAnt = readOne.get("年度").toString();
        PersonalCostYears personalCostYears = new PersonalCostYears();
        personalCostYears.setYears(yearAnt);
        List<PersonalCostYears> pyList = personalCostYearsMapper.select(personalCostYears);
        String yearid = "";
        if (pyList != null) {
            yearid = pyList.get(0).getYearsantid();
        }
        if (resultInsUpd) { //新建
            List<PersonalCost> peralIrtList = new ArrayList<>();
            for (Map<String, Object> item : readAll) {
                PersonalCost personalCost = new PersonalCost();
                k++;
                //姓名
                if (item.get("姓名") != null && item.get("姓名").toString().contains("新人")) {
                    Integer newPerNumIer = personalCostMapper.seleNew();
                    int newPerNum = newPerNumIer.intValue() + newAnt;
                    newAnt++;
                    String perNma = "新人" + newPerNum;
                    item.replace("姓名", perNma);
                    personalCost.setUsername(perNma);
                } else {
                    throw new LogicalException("第" + k + "行 姓名 应为【新人】并且不能为空，请确认。");
                }

                //部门简称
                if (item.get("部门简称") != null) {
                    personalCost.setDepartshort(item.get("部门简称").toString());
                    OrgTree newOrgInfo = orgTreeService.get(new OrgTree());
                    OrgTree orgTree = getOrgInfo(newOrgInfo, item.get("部门简称").toString());
                    if (orgTree.getType().equals("1")) {
                        personalCost.setCenterid(orgTree.get_id());
                    } else if (orgTree.getType().equals("2")) {
                        personalCost.setGroupid(orgTree.get_id());
                    }
                } else {
                    throw new LogicalException("第" + k + "行 部门简称 不能为空，请确认。");
                }

                //配付与否
                if (item.get("配付与否") != null) {
                    String allAnt = alldicMap.get(item.get("配付与否").toString());
                    personalCost.setAllotment(allAnt);
                } else {
                    throw new LogicalException("第" + k + "行 配付与否 不能为空，请确认。");
                }

                //新人入社预定月
                if (item.get("新人入社预定月") != null && item.get("新人入社预定月") != "") {
                    personalCost.setNewpersonaldate(item.get("新人入社预定月").toString().substring(0, 10));
                } else {
                    throw new LogicalException("第" + k + "行 新人入社预定月 不能为空，请确认。");
                }

                //升格前Rn
                if (item.get("升格前Rn") != null) {
                    String rankExAnt = rankdicMap.get(item.get("升格前Rn").toString());
                    personalCost.setExrank(rankExAnt);
                } else {
                    throw new LogicalException("第" + k + "行 升格前Rn 不能为空，请确认。");
                }

                //是否升格升号
                if (item.get("是否升格升号") != null) {
                    String chanAnt = chankdicMap.get(item.get("是否升格升号").toString());
                    personalCost.setChangerank(chanAnt);
                } else {
                    throw new LogicalException("第" + k + "行 是否升格升号 不能为空，请确认。");
                }

                //升格后Rn
                if (item.get("升格后Rn") != null) {
                    String rankLtAnt = rankdicMap.get(item.get("升格后Rn").toString());
                    personalCost.setLtrank(rankLtAnt);
                } else {
                    throw new LogicalException("第" + k + "行 升格后Rn 不能为空，请确认。");
                }
                personalCost.setYearsantid(yearid);
                personalCost.preInsert(tokenModel);
                personalCost.setPersonalcostid(UUID.randomUUID().toString());
                peralIrtList.add(personalCost);
            }
            insertPctWork(peralIrtList);
        } else {
            List<PersonalCost> peralUptList = new ArrayList<>();
            for (Map<String, Object> item : readAll) {
                PersonalCost personalCost = new PersonalCost();
                k++;
                //部门简称
                if (item.get("部门简称") != null) {
                    personalCost.setDepartshort(item.get("部门简称").toString());
                    OrgTree newOrgInfo = orgTreeService.get(new OrgTree());
                    OrgTree orgTree = getOrgInfo(newOrgInfo, item.get("部门简称").toString());
                    if (orgTree.getType().equals("1")) {
                        personalCost.setCenterid(orgTree.get_id());
                    } else if (orgTree.getType().equals("2")) {
                        personalCost.setGroupid(orgTree.get_id());
                    }
                } else {
                    throw new LogicalException("第" + k + "行 部门简称 不能为空，请确认。");
                }

                //配付与否
                if (item.get("配付与否") != null) {
                    String allAnt = alldicMap.get(item.get("配付与否").toString());
                    personalCost.setAllotment(allAnt);
                } else {
                    throw new LogicalException("第" + k + "行 配付与否 不能为空，请确认。");
                }

                //升格前Rn
                if (item.get("升格前Rn") != null) {
                    String rankExAnt = rankdicMap.get(item.get("升格前Rn").toString());
                    personalCost.setExrank(rankExAnt);
                } else {
                    throw new LogicalException("第" + k + "行 升格前Rn 不能为空，请确认。");
                }

                //是否升格升号
                if (item.get("是否升格升号") != null) {
                    String chanAnt = chankdicMap.get(item.get("是否升格升号").toString());
                    personalCost.setChangerank(chanAnt);
                } else {
                    throw new LogicalException("第" + k + "行 是否升格升号 不能为空，请确认。");
                }

                //升格后Rn
                if (item.get("升格后Rn") != null) {
                    String rankLtAnt = rankdicMap.get(item.get("升格后Rn").toString());
                    personalCost.setLtrank(rankLtAnt);
                } else {
                    throw new LogicalException("第" + k + "行 升格后Rn 不能为空，请确认。");
                }
                peralUptList.add(personalCost);
            }
        }
//        Result.add("失败数：" + error);
//        Result.add("成功数：" + accesscount);
//        Result.add("成功数人数id" + useradd);
        return Result;
    }


    public OrgTree getOrgInfo(OrgTree org, String compn) throws Exception {
        OrgTree returnorg = new OrgTree();
        if (org.getOrgs() != null && org.getCompanyen().equals(compn)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    returnorg = getOrgInfo(item, compn);
                    if (returnorg.getCompanyen() != null) {
                        if (returnorg.getCompanyen().equals(compn)) {
                            return returnorg;
                        }
                    }
                }
            }

        }
        return new OrgTree();
    }


    private void insertPctWork(List<PersonalCost> changePctListAnt) throws Exception {
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
        for (PersonalCost pct : changePctListAnt) {
            String preRank = "";
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(pct.getLtrank())) {
                preRank = pct.getLtrank();
            } else {
                preRank = pct.getExrank();
            }
            pct.setBasicallyant(basicallMap.get(preRank));
            pct.setResponsibilityant(responsibilityMap.get(preRank));
            BigDecimal basicallyAntal = new BigDecimal(pct.getBasicallyant());
            BigDecimal responsibilityAntal = new BigDecimal(pct.getResponsibilityant());
            String monthlysalaryle = (basicallyAntal.add(responsibilityAntal)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            //月度工资
            pct.setMonthlysalary(monthlysalaryle);
            pct.setAllowanceant(allowanceantMap.get(preRank));
            pct.setOtherantone(expandOneMap.get(preRank));
            pct.setOtheranttwo(expandTwoMap.get(preRank));
            pct.setQnbt("0");
            pct.setOnlychild(onlyChild);
            BigDecimal allowanceAntal = new BigDecimal(pct.getAllowanceant());
            BigDecimal qnbtAntal = new BigDecimal(pct.getQnbt());
            BigDecimal otherantOneAntal = new BigDecimal(pct.getOtherantone());
            BigDecimal otherantTwoAntal = new BigDecimal(pct.getOtheranttwo());
            BigDecimal onlyChildAntal = new BigDecimal(pct.getOnlychild());
            String totalsubsidiesle = ((((allowanceAntal.add(qnbtAntal)).add(otherantOneAntal).add(otherantTwoAntal)).add(onlyChildAntal))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            //补贴总计
            pct.setTotalsubsidies(totalsubsidiesle);
            pct.setMonthlybonusmonths(monthlyBonusMonthsMap.get(preRank));
            BigDecimal mbmal = new BigDecimal(pct.getMonthlybonusmonths());
            String monthlybonusle = ((mbmal.multiply(basicallyAntal)).divide(twelveAnt, 2, BigDecimal.ROUND_HALF_UP)).toString();
            //月度奖金
            pct.setMonthlybonus(monthlybonusle);
            pct.setAnnualbonusmonths(annualBonusMonthsMap.get(preRank));
            BigDecimal abmal = new BigDecimal(pct.getAnnualbonusmonths());
            String annualbonusle = ((abmal.multiply(basicallyAntal)).divide(twelveAnt, 2, BigDecimal.ROUND_HALF_UP)).toString();
            //年度奖金
            pct.setAnnualbonus(annualbonusle);
            //工资总额
            BigDecimal mal = new BigDecimal(pct.getMonthlysalary());
            BigDecimal tal = new BigDecimal(pct.getTotalsubsidies());
            BigDecimal mgal = new BigDecimal(pct.getMonthlybonus());
            BigDecimal ygal = new BigDecimal(pct.getAnnualbonus());
            String twle = (((mal.add(tal)).add(mgal)).add(ygal)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            pct.setTotalwages(twle);
            BigDecimal tlwal = new BigDecimal(pct.getTotalwages());
            //工会经费
            BigDecimal total = new BigDecimal(pct.getTotalwages());
            String tradfel = ((total.subtract(qnbtAntal)).subtract(onlyChildAntal)).multiply(unionAnt).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            pct.setTradeunionfunds(tradfel);
            BigDecimal tradfal = new BigDecimal(pct.getTradeunionfunds());
            //加班费时给
            String otp = ((basicallyAntal.divide(mouthworkAnt, 2, BigDecimal.ROUND_HALF_UP)).divide(dayworkAnt, 2, BigDecimal.ROUND_HALF_UP)).multiply(basicAnt).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            pct.setOvertimepay(otp);
            //加班小时数
            pct.setOvertimehour(overtimehourMap.get(preRank));
            //是否大连户籍 1-是 0否
            pct.setIndalian("0");
            personalCostMapper.insert(pct);
        }
    }


    @Override
    public List<PersonalCostYears> getPerCostYarList(PersonalCostYears personalCostYears) {
        return personalCostYearsMapper.select(personalCostYears);
    }

    @Override
    public void upPersonalCost(List<PersonalCost> personalCostList, TokenModel tokenModel) throws Exception {
        List<PersonalCost> changePctList = new ArrayList<>();
        for (PersonalCost pct : personalCostList) {
            PersonalCost pctFind = new PersonalCost();
            List<PersonalCost> pctFindList = new ArrayList<>();
            pctFind.setPersonalcostid(pct.getPersonalcostid());
            pctFindList = personalCostMapper.select(pctFind);
            if (pctFindList.get(0).getLtrank() != pct.getLtrank()) {
                changePctList.add(pct);
            }
        }
        changePctWork(changePctList);
        personalCostMapper.updatePersonalCost(personalCostList, tokenModel);
    }

    private void changePctWork(List<PersonalCost> changePctListAnt) throws Exception {
        /*
         * 0为修改，1为新增
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

        for (PersonalCost pcst : changePctListAnt) {
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
            String monbos = (monbomBig.multiply(basicBig)).divide(twelveAnt, 2, RoundingMode.HALF_UP).toString();
            pcst.setMonthlybonus(monbos);
            BigDecimal monbosBig = new BigDecimal(monbos);
            //年度奖金月数
            String yearbom = annualBonusMonthsMap.get(perranks);
            pcst.setAnnualbonusmonths(yearbom);
            //年度奖金
            BigDecimal yearbomBig = new BigDecimal(yearbom);
            String yearbos = (yearbomBig.multiply(basicBig)).divide(twelveAnt, 2, RoundingMode.HALF_UP).toString();
            pcst.setAnnualbonus(yearbos);
            BigDecimal yearbosBig = new BigDecimal(yearbos);
            //工资总额 = 月度工资+补贴总计+月度奖金+年度奖金
            BigDecimal totmonOldBig = new BigDecimal(pcst.getTotalwages());
            String totmon = monthlyBig.add(tosubBig).add(monbosBig).add(yearbosBig).toString();
            pcst.setTotalwages(totmon);
            BigDecimal totmonBig = new BigDecimal(totmon);
            //独生子女费
            BigDecimal olycidBig = new BigDecimal(pcst.getOnlychild());
            //工会经费 = (工资总额-取暖补贴-独生子女费)*0.02
            BigDecimal unfdsOldBig = new BigDecimal(pcst.getTradeunionfunds());
            String unfds = ((totmonBig.subtract(qnuanBig)).subtract(olycidBig)).divide(unionAnt, 2, RoundingMode.HALF_UP).toString();
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
    public List<PersonalCost> getFuzzyQuery(String yearsantid, String username, String allotmentAnt, String group_id, String rnAnt) throws Exception {
        List<PersonalCost> personalCost = new ArrayList<>();
        if (group_id.equals("全部")) {
            group_id = "";
        }
        personalCost = personalCostMapper.getFuzzyQuery(yearsantid, username, allotmentAnt, group_id, rnAnt);
        return personalCost;
    }
    //add-lyt-21/2/19-PSDCD_PFANS_20201123_XQ_017-end

    @Override
    public Map<String,PeoplewareFee> getBmRanksInfo(String years, String department) throws Exception {
        Map<String,PeoplewareFee> getRankReault = new HashMap<>();
        List<Dictionary> dicList = dictionaryService.getForSelect("PR021");
        List<String> rankList = dicList.stream().map(Dictionary::getCode).collect(Collectors.toList());
        PersonalCostYears perCoYears = new PersonalCostYears();
        perCoYears.setYears(years);
        List<PersonalCostYears> perYeList = personalCostYearsMapper.select(perCoYears);
        if(perYeList.size() > 0){
            List<DepartmentVo> departmentVoList = new ArrayList<>();
            departmentVoList = orgTreeService.getAllDepartment();
            PersonalCost personalCost = new PersonalCost();
            personalCost.setYearsantid(perYeList.get(0).getYearsantid());
            personalCost.setDepartment(department);
            List<PersonalCost> personalCostListList = personalCostMapper.select(personalCost);
            Map<String,List<PersonalCost>> perGroupMap = personalCostListList.stream()
                    .filter(ids -> !StringUtils.isNullOrEmpty(ids.getExrank()))
                    .collect(Collectors.groupingBy(PersonalCost::getExrank));
            for (String rks : rankList) {
                PeoplewareFee peopleFee = new PeoplewareFee();
                if(perGroupMap.get(rks) != null){
                    List<PersonalCost> personCostList = new ArrayList<>();
                    personCostList = perGroupMap.get(rks);
                    String MonthResult = "";
                    BigDecimal rankSum = BigDecimal.ZERO;
                    rankSum = personCostList.stream().map(i -> new BigDecimal(i.getAptoju())).reduce(BigDecimal.ZERO, BigDecimal::add);
                    MonthResult = rankSum.divide(new BigDecimal(personCostList.size()), 2, RoundingMode.HALF_UP).toString();
                    peopleFee.setMonth4(MonthResult);
                    rankSum = personCostList.stream().map(i -> new BigDecimal(i.getJutoma())).reduce(BigDecimal.ZERO, BigDecimal::add);
                    MonthResult = rankSum.divide(new BigDecimal(personCostList.size()), 2, RoundingMode.HALF_UP).toString();
                    peopleFee.setMonth7(MonthResult);
                }else{
                    peopleFee.setMonth4("0.0");
                    peopleFee.setMonth7("0.0");
                }
                getRankReault.put(rks,peopleFee);
            }
        }
        return getRankReault;
    }
}
