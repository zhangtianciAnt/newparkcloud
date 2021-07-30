package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.*;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class GivingServiceImpl implements GivingService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private GivingMapper givingMapper;

    @Autowired
    private CasgiftApplyMapper casgiftApplyMapper;

    @Autowired
    private BaseMapper baseMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private RetireMapper retireMapper;

    @Autowired
    private WagesMapper wagesMapper;

    @Autowired
    private DutyfreeMapper dutyfreeMapper;

    @Autowired
    private ComprehensiveMapper comprehensiveMapper;

    @Autowired
    private ContrastMapper contrastMapper;

    @Autowired
    private AbNormalMapper abNormalMapper;

    @Autowired
    private CasgiftApplyMapper casgiftapplyMapper;

    @Autowired
    private OtherTwoMapper othertwoMapper;

    @Autowired
    private LackattendanceMapper lackattendanceMapper;

    @Autowired
    private ResidualMapper residualMapper;

    @Autowired
    private OtherTwo2Mapper othertwo2Mapper;

    @Autowired
    private OtherOneMapper otherOneMapper;

    @Autowired
    private OtherFiveMapper otherfiveMapper;

    @Autowired
    private OtherFourMapper otherfourMapper;

    @Autowired
    private AppreciationMapper appreciationMapper;

    @Autowired
    private AccumulatedTaxMapper accumulatedTaxMapper;

    @Autowired
    private DisciplinaryMapper disciplinaryMapper;

    @Autowired
    private AdditionalMapper additionalMapper;

    @Autowired
    private InductionMapper inductionMapper;

    @Autowired
    private WorkingDayMapper workingDayMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;
    @Autowired
    private AnnualLeaveService annualLeaveService;

    private static List<CustomerInfo> customerInfos;
    private static List<CustomerInfo> customerinfoAll;
    private static List<Wages> lastwages;
    private static List<Dictionary> dictionaryAll;

    private static final SimpleDateFormat sdfYM = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat sdfYMD1 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfUTC = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
    // 日期基数
    private static final double dateBase = 21.75;
    private static final String strDate = "T16:00:00.000Z";

    @PostConstruct
    public void init() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_MONTH, 1);
        Query query = new Query();
        Criteria criteria = Criteria.where("status").is("0").and("userinfo.type").is("0").orOperator(Criteria.where("userinfo.resignation_date")
                .gte(sf.format(now.getTime())), Criteria.where("userinfo.resignation_date").is(null), Criteria.where("userinfo.resignation_date").is(""));
        query.addCriteria(criteria);
        customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        customerinfoAll = mongoTemplate.findAll(CustomerInfo.class);
    }

    @Override
    public void initial() throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_MONTH, 1);
        Query query = new Query();
        Criteria criteria = Criteria.where("status").is("0").and("userinfo.type").is("0").orOperator(Criteria.where("userinfo.resignation_date")
                .gte(sf.format(now.getTime())), Criteria.where("userinfo.resignation_date").is(null), Criteria.where("userinfo.resignation_date").is(""));
        query.addCriteria(criteria);
        customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        customerinfoAll = mongoTemplate.findAll(CustomerInfo.class);
        //获取上月工资
        SimpleDateFormat sfYM = new SimpleDateFormat("yyyy-MM");
        Calendar lastMonthDate = Calendar.getInstance();
        lastMonthDate.add(Calendar.MONTH, -1);
        lastwages = wagesMapper.lastWages(sfYM.format(lastMonthDate.getTime()),"");
        //获取所有有字典
        dictionaryAll = dictionaryMapper.selectAll();
    }

    /**
     * 生成基数表
     * FJL
     */
    @Override
    public GivingVo givinglist(String giving_id) throws Exception {
        GivingVo givingVo = new GivingVo();
        Giving giving = new Giving();
        giving.setGiving_id(giving_id);
        List<Giving> givinglist = givingMapper.select(giving);
        String strMonths = "";
        if(givinglist.size() > 0){
            strMonths = givinglist.get(0).getMonths();
        }
        givingVo.setGiving(giving);

        // region 专项控除 By SKAIXX
        List<Disciplinary> disciplinary = disciplinaryMapper.getdisciplinary(giving_id,strMonths);
        disciplinary = disciplinary.stream().filter(coi -> (coi.getGiving_id().contains(giving_id))).collect(Collectors.toList());
        givingVo.setDisciplinaryVo(disciplinary);
        // endregion

        OtherOne otherOne = new OtherOne();
        otherOne.setGiving_id(giving_id);
        List<OtherOne> otherOnelist = otherOneMapper.select(otherOne);
        otherOnelist = otherOnelist.stream().sorted(Comparator.comparing(OtherOne::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherOne(otherOnelist);

        OtherTwo othertwo = new OtherTwo();
        othertwo.setGiving_id(giving_id);
        List<OtherTwo> othertwolist = othertwoMapper.select(othertwo);
        othertwolist = othertwolist.stream().sorted(Comparator.comparing(OtherTwo::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherTwo(othertwolist);

        OtherFive otherfive = new OtherFive();
        otherfive.setGiving_id(giving_id);
        List<OtherFive> otherfivelist = otherfiveMapper.select(otherfive);
        otherfivelist = otherfivelist.stream().sorted(Comparator.comparing(OtherFive::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherFive(otherfivelist);

        // region 附加控除 By SKAIXX
        Additional additional = new Additional();
        additional.setGiving_id(giving_id);
        List<Additional> additionallist = additionalMapper.select(additional);
        givingVo.setAddiTional(additionallist);
        // endregion

        // region 欠勤 By SKAIXX
        Lackattendance lackattendance = new Lackattendance();
        lackattendance.setGiving_id(giving_id);
        List<Lackattendance> lackattendancellist = lackattendanceMapper.select(lackattendance).stream().sorted(Comparator.comparing(Lackattendance::getRowindex)).collect(Collectors.toList());
        givingVo.setLackattendance(lackattendancellist);
        // endregion

        // region 残业 By SKAIXX
        Residual residua = new Residual();
        residua.setGiving_id(giving_id);
        List<Residual> residualllist = residualMapper.select(residua).stream().sorted(Comparator.comparing(Residual::getRowindex)).collect(Collectors.toList());
        givingVo.setResidual(residualllist);
        // endregion

        OtherFour otherfour = new OtherFour();
        otherfour.setGiving_id(giving_id);
        List<OtherFour> otherfourlist = otherfourMapper.select(otherfour);
        otherfourlist = otherfourlist.stream().sorted(Comparator.comparing(OtherFour::getRowindex)).collect(Collectors.toList());
        givingVo.setOtherFour(otherfourlist);

        Base base = new Base();
        base.setGiving_id(giving_id);
        List<Base> baselist = baseMapper.select(base);
        baselist = baselist.stream().sorted(Comparator.comparing(Base::getRowindex)).collect(Collectors.toList());
        givingVo.setBase(baselist);

        // region 月度赏与 By SKAIXX
        Appreciation appreciation = new Appreciation();
        appreciation.setGiving_id(giving_id);
        List<Appreciation> appreciationlist = appreciationMapper.select(appreciation);
        appreciationlist = appreciationlist.stream().sorted(Comparator.comparing(Appreciation::getRowindex)).collect(Collectors.toList());
        // 月度赏与计算
        appreciationlist = appreciationCalc(baselist, appreciationlist);
        givingVo.setAppreciation(appreciationlist);
        // endregion

        //个人对比
        Contrast contrast = new Contrast();
        contrast.setGiving_id(giving_id);
        List<Contrast> contrastList = contrastMapper.select(contrast);
        contrastList = contrastList.stream().sorted(Comparator.comparing(Contrast::getRowindex)).collect(Collectors.toList());
        givingVo.setContrast(contrastList);

        // region 累计税金 By SKAIXX
        List<AccumulatedTaxVo> accumulatedTaxVolist = accumulatedTaxMapper.getaccumulatedTax(giving_id,strMonths);
        accumulatedTaxVolist = accumulatedTaxVolist.stream().filter(coi -> (coi.getGiving_id().contains(giving_id))).collect(Collectors.toList());
        givingVo.setAccumulatedTaxVo(accumulatedTaxVolist);
        // endregion

        // region 免税 By SKAIXX
        List<DutyfreeVo> dutyfreeVolist = dutyfreeMapper.getdutyfree(giving_id,strMonths);
        dutyfreeVolist = dutyfreeVolist.stream().filter(coi -> (coi.getGiving_id().contains(giving_id))).collect(Collectors.toList());
        givingVo.setDutyfreeVo(dutyfreeVolist);
        // endregion

        // region 综合收入 By SKAIXX
        List<ComprehensiveVo> comprehensiveVolist = comprehensiveMapper.getcomprehensive(giving_id,strMonths);
        comprehensiveVolist = comprehensiveVolist.stream().filter(coi -> (coi.getGiving_id().contains(giving_id))).collect(Collectors.toList());
        givingVo.setComprehensiveVo(comprehensiveVolist);
        // endregion

        // 2020/03/11 add by myt start
        // 查询入职信息表
        Induction induction = new Induction();
        induction.setGiving_id(giving_id);
        List<Induction> inductionList = inductionMapper.select(induction);
        inductionList = inductionList.stream().sorted(Comparator.comparing(Induction::getRowindex)).collect(Collectors.toList());
        givingVo.setEntryVo(inductionList);
//        Calendar nowDate = Calendar.getInstance();
//        Calendar lastDate = Calendar.getInstance();
//        lastDate.add(Calendar.MONTH, -1);
//        // 设置上个月的年份和月份
//        givingVo.setYearOfLastMonth(String.valueOf(lastDate.get(Calendar.YEAR)));
//        givingVo.setMonthOfLastMonth(String.valueOf(lastDate.get(Calendar.MONTH) + 1));
//        // 设置当月的年份和月份
//        givingVo.setYearOfThisMonth(String.valueOf(nowDate.get(Calendar.YEAR)));
//        givingVo.setMonthOfThisMonth(String.valueOf(nowDate.get(Calendar.MONTH) + 1));

        //region add_qhr_20210702 修改入职tab显示月份
        // 设置上个月的年份和月份
        givingVo.setMonthOfLastMonth(givinglist.get(0).getMonths().substring(4, 6));
        if (givingVo.getMonthOfLastMonth().equals("01")) {
            givingVo.setMonthOfLastMonth("12");
            givingVo.setYearOfLastMonth(String.valueOf(Integer.valueOf(givinglist.get(0).getMonths().substring(0, 4)) - 1));
        } else {
            givingVo.setMonthOfLastMonth(String.valueOf(Integer.valueOf(givinglist.get(0).getMonths().substring(4, 6)) - 1));
            givingVo.setYearOfLastMonth(givinglist.get(0).getMonths().substring(0, 4));
        }
        // 设置当月的年份和月份
        givingVo.setYearOfThisMonth(givinglist.get(0).getMonths().substring(0, 4));
        givingVo.setMonthOfThisMonth(String.valueOf(Integer.valueOf(givinglist.get(0).getMonths().substring(4, 6))));
        //endregion add_qhr_20210702 修改入职tab显示月份

        // 查询离职信息表
        Retire retire = new Retire();
        retire.setGiving_id(giving_id);
        List<Retire> retireList = retireMapper.select(retire);
        retireList = retireList.stream().sorted(Comparator.comparing(Retire::getRowindex)).collect(Collectors.toList());
        givingVo.setRetireVo(retireList);
        // 2020/03/11 add by myt end

        // zqu start 先判断wages表里有没有当月工资数据，有用表里的，没有用sql生成
        Wages wages = new Wages();
        wages.setGiving_id(giving_id);
        wages.setActual("0");
        System.out.println("工资开始查询");
        long startTime =  System.currentTimeMillis();
        List<Wages> wagesList = wagesMapper.select(wages);
        if (wagesList.size() > 0) {
            givingVo.setWagesList(wagesList.stream().sorted(Comparator.comparing(Wages::getUser_id)).collect(Collectors.toList()));
        } else {
            givingVo.setWagesList(wagesMapper.getWagesByGivingId(giving_id,""));
            //更新个人对比
            if(contrastList.size() > 0){
                for (Contrast con : contrastList) {
                    if(givingVo.getWagesList().size() > 0){
                        List<Wages> waList = givingVo.getWagesList().stream().filter(coi -> (coi.getUser_id().contains(con.getUser_id()))).collect(Collectors.toList());
                        if(waList.size() > 0){
                            //当月实发工资
                            con.setThismonth(waList.get(0).getRealwages());
                            if(con.getThismonth() != null && con.getLastmonth() != null){
                                double Differenc = Double.parseDouble(con.getThismonth()) - Double.parseDouble(con.getLastmonth());
                                //差额
                                con.setDifference(new BigDecimal(Differenc).setScale(2, RoundingMode.HALF_UP).toPlainString());
                            }
                            contrastMapper.updateByPrimaryKeySelective(con);
                        }
                    }
                }
                givingVo.setContrast(contrastList);
            }
        }
        System.out.println("工资查询结束");
        long endTime =  System.currentTimeMillis();
        long usedTime = (endTime-startTime)/1000;
        System.out.println("用时：" + usedTime + "秒");
        return givingVo;
        // zqu end
    }

    @Override
    public void insertOtherOne(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("其他1");
        /*获取 customerInfos-lxx*/
        //initial();
        /*获取 customerInfos-lxx*/
        List<OtherOne> otherOnes = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.00");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long endMillisecond = format.parse("2012-08-31").getTime();
        long otherOneTime = format.parse("2012-03-31").getTime();
        AbNormal abNormal = new AbNormal();
        abNormal.setStatus("4");
        //upd_fjl_0910  更改取值 start
//        List<AbNormal> abNormalinfo = abNormalMapper.selectAbNormal(format.format(new Date()));
        List<AbNormal> abNormalinfo = abNormalMapper.selectAbNormalGiving(format.format(new Date()));
        //upd_fjl_0910  更改取值 end
//        AttendanceSetting attendanceSetting = attendanceSettingMapper.selectOne(new AttendanceSetting());
        /*根据字典获取 納付率.全社社会保険基数（元） -lxx*/
//        Dictionary dictionary = new Dictionary();
//        dictionary.setPcode("PR043");
//        List<Dictionary> dictionarylist = dictionaryMapper.select(dictionary);
//        BigDecimal R2019 = new BigDecimal(0);
//        BigDecimal R2018 = new BigDecimal(0);
//        for (Dictionary diction : dictionarylist) {
//            if (diction.getCode().equals("PR043005")) {
//                 R2019 = new BigDecimal(diction.getValue2());
//            } else if (diction.getCode().equals("PR043006")) {
//                 R2018 = new BigDecimal(diction.getValue2());
//            }
//        }
        /*根据字典获取納付率.全社社会保険基数（元） -lxx*/
        /*base 数据检索 -lxx*/
//        Base baseQuery = new Base();
//        baseQuery.setGiving_id(givingid);
//        List<Base> baseList = baseMapper.select(baseQuery);
        /*base 数据检索 -lxx*/
        // 前月
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String preYear = String.valueOf(cal.get(Calendar.YEAR));
        String preMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");
        if (abNormalinfo.size() > 0) {
            int rowindex = 0;
            int rowindexMan = 0;
            for (AbNormal abNor : abNormalinfo) {
                if (abNor.getErrortype().equals("PR013012") || abNor.getErrortype().equals("PR013013")) {
                    boolean bool = true;
                    Attendance attendance = new Attendance();
                    attendance.setUser_id(abNor.getUser_id());
                    attendance.setYears(preYear);
                    attendance.setMonths(preMonth);
                    List<Attendance> attendanceList = attendanceMapper.select(attendance);
                    String StrNursingleave = BigDecimal.valueOf(attendanceList.stream()
                            .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getNursingleave()))).sum() / 8).setScale(0, RoundingMode.HALF_UP).toPlainString();
                    if(StrNursingleave.equals("0")){
                        continue;
                    }
                    OtherOne otherOne = new OtherOne();
                    String beginTime = "";
                    String otherOneid = UUID.randomUUID().toString();
                    if (tokenModel != null) {
                        otherOne.preInsert(tokenModel);
                    } else {
                        otherOne.preInsert();
                    }
                    otherOne.setOtherone_id(otherOneid);
                    otherOne.setGiving_id(givingid);
                    otherOne.setUser_id(abNor.getUser_id());

                    List<CustomerInfo> cust = customerinfoAll.stream().filter(customerInfo -> customerInfo.getUserid().equals(abNor.getUser_id())).collect(Collectors.toList());

                    if (cust.size() > 0) {
                        otherOne.setDepartment_id(cust.get(0).getUserinfo().getCenterid()); //部门
                        otherOne.setSex(cust.get(0).getUserinfo().getSex()); //性别
                        otherOne.setWorkdate(formatStringDateadd(cust.get(0).getUserinfo().getEnterday())); //入职日
                        beginTime = cust.get(0).getUserinfo().getEnterday();
                    }
                    if (abNor.getErrortype().equals("PR013012")) {
                        rowindex = rowindex + 1;
                        otherOne.setRowindex(rowindex);
                        otherOne.setReststart(abNor.getOccurrencedate());
                        otherOne.setRestend(abNor.getFinisheddate());
                        if(StrNursingleave.equals(abNor.getWorktime())){
                            otherOne.setAttendance("0");
                        }
                        else{
                            Double days = Double.parseDouble(abNor.getRelengthtime()) - Double.parseDouble(StrNursingleave);
                            otherOne.setAttendance(String.valueOf(days.intValue()));
                        }
                        otherOne.setTenantid(abNor.getTenantid());//0无；1当月是产休开始日；2当月是产休结束日
                        /*其他1 -lxx*/
                        //IF 入社日<=2012/3/31
//                        otherOne.setOther1("0");
//                        if(cust.size() > 0){
//                            if(format.parse(cust.get(0).getUserinfo().getEnterday()).getTime() <= otherOneTime){
//                                for (Base baseinfo : baseList) {
//                                    if(baseinfo.getUser_id().equals(abNor.getUser_id())){
//                                        BigDecimal b2019 = (new BigDecimal(baseinfo.getMedical()).subtract(R2019)).multiply(new BigDecimal(0.15/21.75*(21.75 - days))).setScale(2, RoundingMode.HALF_UP);
//                                        BigDecimal b2018 = (new BigDecimal(baseinfo.getMedical()).subtract(R2018)).multiply(new BigDecimal(0.15/21.75*(21.75 - days))).setScale(2, RoundingMode.HALF_UP);
//                                        otherOne.setOther1(b2018.toPlainString() + "," + b2019.toPlainString());
//                                        break;
//                                    }
//                                }
//                            }
//                        }
                        /*其他1 -lxx*/
                        otherOne.setBasedata("2");
                        otherOne.setType("1");
                    } else if (abNor.getErrortype().equals("PR013013")) {
                        rowindexMan = rowindexMan + 1;
                        otherOne.setRowindex(rowindexMan);
                        otherOne.setStartdate(abNor.getOccurrencedate());
                        otherOne.setEnddate(abNor.getFinisheddate());
                        otherOne.setVacation(StrNursingleave);
                        long beginMillisecond = notNull(beginTime).equals("0") ? (long) 0 : format.parse(beginTime.replace("/", "-")).getTime();
                        if (beginMillisecond >= endMillisecond) {
                            otherOne.setHandsupport(Integer.valueOf(StrNursingleave).toString());
                        } else {
                            otherOne.setHandsupport("0");
                        }
                        otherOne.setHandsupport(StrNursingleave);
                        otherOne.setType("2");
                    }
                    otherOne.setStatus(AuthConstants.DEL_FLAG_NORMAL);
                    otherOne.setCreateon(new Date());
                    otherOnes.add(otherOne);
                }

            }
        }
        if (otherOnes.size() > 0) {
            otherOneMapper.insertOtherOne(otherOnes);
        }
    }

    @Override
    public void insertBase(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("基数");
        /*获取 customerInfos-lxx*/
        initial();
        /*获取 customerInfos-lxx*/
        List<Base> bases = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        // 前月
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String preYear = String.valueOf(cal.get(Calendar.YEAR));
        String preMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");
        String strTemp = preYear + preMonth;

        Dictionary dictionary = new Dictionary();
        dictionary.setPcode("PR042");
        List<Dictionary> dictionarylist = dictionaryMapper.select(dictionary);
        /*获取基数 type-lxx*/
        List<Base> baseType = baseMapper.selectBaseType(givingid);
        /*获取基数 type-lxx*/
        String R9 = null;
        String R8 = null;
        String R7 = null;
        for (Dictionary diction : dictionarylist) {
            if (diction.getCode().equals("PR042006")) {
                R9 = diction.getValue2();
            } else if (diction.getCode().equals("PR042007")) {
                R8 = diction.getValue2();
            } else if (diction.getCode().equals("PR042008")) {
                R7 = diction.getValue2();
            }
        }

        /*RN基本工资 -lxx*/
        Dictionary dictionaryForRn = new Dictionary();
        dictionaryForRn.setPcode("PR021");
        List<Dictionary> dictionarylistForRn = dictionaryMapper.select(dictionaryForRn);
        /*RN基本工资 -lxx*/
        if (customerInfos.size() > 0) {
            int rowindex = 0;
            for (CustomerInfo customer : customerInfos) {
                Base base = new Base();
                rowindex = rowindex + 1;
                String baseid = UUID.randomUUID().toString();
                if (tokenModel != null) {
                    base.preInsert(tokenModel);
                } else {
                    base.preInsert();
                }
                base.setMonths(strTemp);
                base.setBase_id(baseid);
                base.setGiving_id(givingid);
                base.setUser_id(customer.getUserid());  //名字
                base.setDepartment_id(customer.getUserinfo().getCenterid());
                base.setJobnumber(customer.getUserinfo().getJobnumber());  //工号
                base.setRn(customer.getUserinfo().getRank());  //RN
                base.setSex(customer.getUserinfo().getSex());  //性别
                /*设置type lxx */
                for (Base basetype : baseType) {
                    if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(basetype.getUser_id())) {
                        if (basetype.getUser_id().equals(customer.getUserid())) {
                            base.setType(basetype.getType());
                            break;
                        }
                    }
                }
                /*设置type lxx */
                if (customer.getUserinfo().getChildren() != null && customer.getUserinfo().getChildren().equals("1")) {
                    base.setOnlychild("1");  //独生子女
                } else {
                    base.setOnlychild("2");  //独生子女
                }
                //根据等级查询奖金记上月数
                List<Dictionary> DicRank = dictionaryAll.stream().filter(item -> (item.getCode().equals(customer.getUserinfo().getRank()))).collect(Collectors.toList());
                if(DicRank.size() > 0){
                    //奨金計上
                    base.setBonus(DicRank.get(0).getValue5());
                }
                else{
                    //奨金計上
                    base.setBonus("1");
                }
                //1999年前社会人
                if (customer.getUserinfo().getWorkday() != null && customer.getUserinfo().getWorkday().length() > 0) {
                    String strWorkday = customer.getUserinfo().getWorkday().substring(0, 4);
                    if (Integer.parseInt(strWorkday) < 1999) {
                        base.setSociology("1");
                    } else {
                        base.setSociology("2");
                    }
                }
                base.setRegistered("2");
                //大連戸籍
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customer.getUserinfo().getDlnation())) {
                    if(customer.getUserinfo().getDlnation().equals("1")){
                        base.setRegistered("1");
                    }
                }
                //新人区分(税金用)*(是否有工作经验):有
                base.setTaxes("0");//
                String Enterday = formatStringDateadd(customer.getUserinfo().getEnterday());
                Enterday = Enterday.substring(0,4);
                //是否有工作经验(无)  /  入社年月日为当年
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customer.getUserinfo().getExperience())) {
                    if(customer.getUserinfo().getExperience().equals("1") && DateUtil.format(new Date(), "yyyy").equals(Enterday)){//当年入职并且无经验的人
                        base.setTaxes("1");//无经验
                    }
                }
                /*基本工资 -> 月工资  月工资拆分为 基本工资  职责工资 -lxx*/
                //上月工资
                base.setLastmonthbasic(getSalaryBasicAndDuty(customer, 0).get("thisMonthBasic"));
                base.setLastmonthduty(getSalaryBasicAndDuty(customer, 0).get("thisMonthDuty"));
                //本月工资
                base.setThismonthbasic(getSalaryBasicAndDuty(customer, 1).get("thisMonthBasic"));
                base.setThismonthduty(getSalaryBasicAndDuty(customer, 1).get("thisMonthDuty"));
                //N月前基数-N根据字典获取 PR061001
//                Dictionary dictionaryPr = new Dictionary();
//                dictionaryPr.setCode("PR061001");
//                Dictionary dicResult = dictionaryMapper.selectByPrimaryKey(dictionaryPr);
                Dictionary dicResult = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR061001"))).collect(Collectors.toList()).get(0);

                int tmabasic = Integer.parseInt(dicResult.getValue1());
                base.setTmabasic(getSalaryBasicAndDuty(customer, -tmabasic).get("thisMonth"));
                /*基本工资 -> 月工资  月工资拆分为 基本工资  职责工资 -lxx*/
                base.setLastmonth(getSalary(customer, 0)); //上月工资
                base.setThismonth(getSalary(customer, 1)); //本月工资
                //base.setPension(notNull(customer.getUserinfo().getOldageinsurance())); //養老・失業・工傷基数
                base.setYanglaojs(notNull(customer.getUserinfo().getYanglaoinsurance())); //養老基数
                base.setShiyejs(notNull(customer.getUserinfo().getShiyeinsurance())); //失業基数
                base.setGongshangjs(notNull(customer.getUserinfo().getGongshanginsurance())); //工傷基数
                //base.setMedical(notNull(customer.getUserinfo().getYiliaoinsurance())); //医療・生育基数
                base.setYiliaojs(notNull(customer.getUserinfo().getYiliaoinsurance())); //医療基数
                base.setShengyujs(notNull(customer.getUserinfo().getShengyuinsurance())); //生育基数
                base.setAccumulation(notNull(customer.getUserinfo().getHouseinsurance()));  //公积金基数
                //采暖费
                if (customer.getUserinfo().getRank() != null && customer.getUserinfo().getRank().length() > 0) {
                    if (!"その他".equals(customer.getUserinfo().getRank())) {
                        String strRank = customer.getUserinfo().getRank().substring(2);
                        int rank = Integer.parseInt(strRank);
                        /*rank修改 lxx */
                        if (rank >= 21009) {
                            base.setHeating(R9);
                        } else if (rank <= 21008 && rank >= 21006) {
                            base.setHeating(R8);
                        } else if (rank <= 21005) {
                            base.setHeating(R7);
                        }
                        /*rank修改 lxx */

                        /*RN基本工资 -lxx*/
                        for (Dictionary diction : dictionarylistForRn) {
                            if (diction.getCode().equals(customer.getUserinfo().getRank())) {
//                            if (4 <= (cal.get(Calendar.MONTH) + 1) && (cal.get(Calendar.MONTH) + 1) <= 6) {
//                                base.setRnbasesalary(diction.getValue4());
//                            } else {
//                                base.setRnbasesalary(diction.getValue5());
//                            }
                                base.setRnbasesalary(diction.getValue2());
                            }
                        }
                        /*RN基本工资 -lxx*/
                    }
                }

                //入社日
                if (customer.getUserinfo().getEnterday().indexOf("Z") > 0) {
                    base.setWorkdate(customer.getUserinfo().getEnterday().replace("-", "/").substring(0, 10));
                }
                else{
                    base.setWorkdate(formatStringDateadd(customer.getUserinfo().getEnterday()));
                }
                base.setRowindex(rowindex);
                /*group id -lxx*/
                base.setGroupid(customer.getUserinfo().getGroupid());
                /*group id -lxx*/
                /*试用期截止日 -lxx*/
                //add-ws-9/15-禅道任务525
                if(customer.getUserinfo().getEnddate()!="" && customer.getUserinfo().getEnddate()!=null){
                    if (customer.getUserinfo().getEnddate().indexOf("Z")  != -1) {
                        String enddate = customer.getUserinfo().getEnddate().substring(0, 10).replace("-", "/");
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(sf.parse(enddate));//设置起时间
                        cal1.add(Calendar.DATE, +1);
                        customer.getUserinfo().setEnddate(sf.format(cal1.getTime()));
                    }
                }
                //add-ws-9/15-禅道任务525
                base.setEnddate(customer.getUserinfo().getEnddate());
                /*试用期截止日 -lxx*/
                /*试用正式天数计算 -lxx*/
                Map<String, String> map = suitAndDaysCalc(customer.getUserinfo());
                base.setLastmonthdays(map.get("lastMonthDays"));
                base.setLastmonthsuitdays(map.get("lastMonthSuitDays"));
                base.setThismonthdays(map.get("thisMonthDays"));
                base.setThismonthsuitdays(map.get("thisMonthSuitDays"));
                /*试用正式天数计算 -lxx*/
                bases.add(base);
            }
        }
        if (bases.size() > 0) {
            baseMapper.insertBase(bases);
        }
    }

    /**
     * @return
     * @Method suitAndDaysCalc
     * @Author LXX
     * @Description 试用正式天数计算【入职计算和基数表用】
     * @Date 2020/3/18 9:45
     * @Param userinfo
     **/
    private Map<String, String> suitAndDaysCalc(CustomerInfo.UserInfo userinfo) throws Exception {
        Integer thisMonthDays = 0;
        Integer thisMonthSuitDays = 0;
        Integer lastMonthDays = 0;
        Integer lastMonthSuitDays = 0;

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        //当月日期1号
        Calendar calNowOne = Calendar.getInstance();
        //region  qhr_20210528 无作用代码注释
        //calNowOne.add(Calendar.MONTH, 0);
        //endregion qhr_20210528 无作用代码注释
        calNowOne.set(Calendar.DAY_OF_MONTH, 1);
        //当月日期月末
        Calendar calNowLast = Calendar.getInstance();
        calNowLast.set(Calendar.DAY_OF_MONTH, calNowLast.getActualMaximum(Calendar.DAY_OF_MONTH));
        //上月日期月末
        Calendar calLast = Calendar.getInstance();
        calLast.add(Calendar.MONTH, -1);
        calLast.set(Calendar.DAY_OF_MONTH, calLast.getActualMaximum(Calendar.DAY_OF_MONTH));
        //上月日期1号
        Calendar calLastOne = Calendar.getInstance();
        calLastOne.add(Calendar.MONTH, -1);
        calLastOne.set(Calendar.DAY_OF_MONTH, 1);
        //入职日
        Calendar calEnterDay = Calendar.getInstance();
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(userinfo.getEnterday())){
            if (userinfo.getEnterday().indexOf("Z") < 0) {
                userinfo.setEnterday(formatStringDate(userinfo.getEnterday()));
            }
            calEnterDay.setTime(sf.parse(userinfo.getEnterday().replace("Z", " UTC")));
        }
        //退职日
        Calendar calResignationDate = Calendar.getInstance();
        if (!StringUtils.isEmpty(userinfo.getResignation_date())) {
            if (userinfo.getResignation_date().indexOf("Z") < 0) {
                userinfo.setResignation_date(formatStringDate(userinfo.getResignation_date().substring(0,10)));
            }
            calResignationDate.setTime(sf.parse(userinfo.getResignation_date().replace("Z", " UTC")));
        } else {
            calResignationDate.setTime(calNowLast.getTime());
        }

        //试用期截止日是空值
        if (StringUtils.isEmpty(userinfo.getEnddate())) {
            //入职日是当月
            if (calNowOne.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calNowOne.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                //上月试用天数
                lastMonthSuitDays = 0;
                //上月正式天数
                lastMonthDays = 0;
                //本月试用天数
                //本月末日/退职日期 取小值
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calEnterDay.getTime(), temp);
                //本月正式天数
                thisMonthDays = 0;
            }
            //入职日是上月
            else if (calLast.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calLast.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                //上月试用天数
                lastMonthSuitDays = getTrialWorkDaysExceptWeekend(calEnterDay.getTime(), calLast.getTime());
                //上月正式天数
                lastMonthDays = 0;
                //本月试用天数
                //本月末日/退职日期 取小值
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calNowOne.getTime(), temp);
                //本月正式天数
                thisMonthDays = 0;
            }
            //其他
            else {
                //上月试用天数
                lastMonthSuitDays = getTrialWorkDaysExceptWeekend(calLastOne.getTime(), calLast.getTime());
                //上月正式天数
                lastMonthDays = 0;
                //本月试用天数
                //本月末日/退职日期 取小值
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calNowOne.getTime(), temp);
                //本月正式天数
                thisMonthDays = 0;
            }
        }
        //试用期截止日不是空值
        else {
            //试用最后日
            Calendar calSuitDate = Calendar.getInstance();
            if (userinfo.getEnddate().indexOf("Z") < 0) {
                userinfo.setEnddate(formatStringDate(userinfo.getEnddate()));
            }
            calSuitDate.setTime(sf.parse(userinfo.getEnddate().replace("Z", " UTC")));
            //calSuitDate.add(Calendar.DATE, -1);
            //试用截止日
            Calendar calOfficialDate = Calendar.getInstance();
            if (userinfo.getEnddate().indexOf("Z") < 0) {
                userinfo.setEnddate(formatStringDate(userinfo.getEnddate()));
            }
            calOfficialDate.setTime(sf.parse(userinfo.getEnddate().replace("Z", " UTC")));
            //试用截止日大于本月末日
            if (calOfficialDate.getTime().getTime() > calNowLast.getTime().getTime()) {
                //入职日是当月
                if (calNowOne.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calNowOne.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                    //上月试用天数
                    lastMonthSuitDays = 0;
                    //上月正式天数
                    lastMonthDays = 0;
                    //本月试用天数
                    //本月末日/退职日期 取小值
                    Date temp = calResignationDate.getTime();
                    if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                        temp = calNowLast.getTime();
                    }
                    thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calEnterDay.getTime(), temp);
                    //本月正式天数
                    thisMonthDays = 0;
                }
                //入职日是上月
                else if (calLast.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calLast.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                    //上月试用天数
                    lastMonthSuitDays = getTrialWorkDaysExceptWeekend(calEnterDay.getTime(), calLast.getTime());
                    //上月正式天数
                    lastMonthDays = 0;
                    //本月试用天数
                    //本月末日/退职日期 取小值
                    Date temp = calResignationDate.getTime();
                    if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                        temp = calNowLast.getTime();
                    }
                    thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calNowOne.getTime(), temp);
                    //本月正式天数
                    thisMonthDays = 0;
                }
                //其他
                else {
                    //上月试用天数
                    lastMonthSuitDays = getTrialWorkDaysExceptWeekend(calLastOne.getTime(), calLast.getTime());
                    //上月正式天数
                    lastMonthDays = 0;
                    //本月试用天数
                    //本月末日/退职日期 取小值
                    Date temp = calResignationDate.getTime();
                    if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                        temp = calNowLast.getTime();
                    }
                    thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calNowOne.getTime(), temp);
                    //本月正式天数
                    thisMonthDays = 0;
                }
            }
            //试用截止日小于上月月初日
            else if (calOfficialDate.getTime().getTime() < calLastOne.getTime().getTime()) {
                //上月试用天数
                lastMonthSuitDays = 0;
                //上月正式天数
                lastMonthDays = getTrialWorkDaysExceptWeekend(calLastOne.getTime(), calLast.getTime());
                //本月试用天数
                thisMonthSuitDays = 0;
                //本月正式天数
                //本月末日/退职日期 取小值
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthDays = getTrialWorkDaysExceptWeekend(calNowOne.getTime(), temp);
            }
            //试用截止日是上月
            else if (calLast.get(Calendar.YEAR) == calOfficialDate.get(Calendar.YEAR) && calLast.get(Calendar.MONTH) == calOfficialDate.get(Calendar.MONTH)) {
                //上月试用天数
                //上月1日/入职日期 取大值
                Date tempStart = calLastOne.getTime();
                if (calEnterDay.getTime().getTime() > calLastOne.getTime().getTime()) {
                    tempStart = calEnterDay.getTime();
                }
                lastMonthSuitDays = getTrialWorkDaysExceptWeekend(tempStart, calSuitDate.getTime());
                //上月正式天数
                lastMonthDays = getTrialWorkDaysExceptWeekend(calOfficialDate.getTime(), calLast.getTime());
                //本月试用天数
                thisMonthSuitDays = 0;
                //本月正式天数
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthDays = getTrialWorkDaysExceptWeekend(calNowOne.getTime(), temp);
            }
            //试用截止日是本月
            else if (calNowLast.get(Calendar.YEAR) == calOfficialDate.get(Calendar.YEAR) && calNowLast.get(Calendar.MONTH) == calOfficialDate.get(Calendar.MONTH)) {
                //入职日是当月
                if (calNowOne.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calNowOne.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                    //上月试用天数
                    lastMonthSuitDays = 0;
                    //本月试用天数
                    thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calEnterDay.getTime(), calSuitDate.getTime());
                }
                //入职不是当月
                else {
                    //上月试用天数
                    //上月1日/入职日期 取大值
                    Date tempStart = calLastOne.getTime();
                    if (calEnterDay.getTime().getTime() > calLastOne.getTime().getTime()) {
                        tempStart = calEnterDay.getTime();
                    }
                    //                    add 试用员工当月转正的当月试用天数 fr
                    //calSuitDate.add(Calendar.DATE, -1);//转正日当天不算使用
                    lastMonthSuitDays = getTrialWorkDaysExceptWeekend(tempStart, calLast.getTime());
                    thisMonthSuitDays = getTrialWorkDaysExceptWeekend(calNowOne.getTime(), calSuitDate.getTime());
                }

                //上月正式天数
                lastMonthDays = 0;
                //本月正式天数
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthDays = getTrialWorkDaysExceptWeekend(calOfficialDate.getTime(), temp);
            }
        }

        Map<String, String> map = new HashMap<String, String>();
        //map.put("thisMonthDays", thisMonthDays > 21.75 ? "21.75" : thisMonthDays.toString());
        map.put("thisMonthDays", calculateAttendanceDays(Double.valueOf(thisMonthDays)) > 21.75 ? "21.75" : String.valueOf(calculateAttendanceDays(Double.valueOf(thisMonthDays))));
        //map.put("thisMonthSuitDays", thisMonthSuitDays > 21.75 ? "21.75" : thisMonthSuitDays.toString());
        map.put("thisMonthSuitDays", calculateAttendanceDays(Double.valueOf(thisMonthSuitDays)) > 21.75 ? "21.75" : String.valueOf(calculateAttendanceDays(Double.valueOf(thisMonthSuitDays))));
        //map.put("lastMonthDays", lastMonthDays > 21.75 ? "21.75" : lastMonthDays.toString());
        map.put("lastMonthDays", calculateAttendanceDays(Double.valueOf(lastMonthDays)) > 21.75 ? "21.75" : String.valueOf(calculateAttendanceDays(Double.valueOf(lastMonthDays))));
        //map.put("lastMonthSuitDays", lastMonthSuitDays > 21.75 ? "21.75" : lastMonthSuitDays.toString());
        map.put("lastMonthSuitDays", calculateAttendanceDays(Double.valueOf(lastMonthSuitDays)) > 21.75 ? "21.75" : String.valueOf(calculateAttendanceDays(Double.valueOf(lastMonthSuitDays))));
        return map;
    }

    @Override
    public void insertOtherTwo(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("其他2");
        OtherTwo othertwo = new OtherTwo();
        CasgiftApply casgiftapply = new CasgiftApply();
        casgiftapply.setPayment("0");
        //add gbb 0727 关联祝礼金 start
        casgiftapply.setReleasedate(DateUtil.format(new Date(),"yyyy-MM"));
        //add gbb 0727 关联祝礼金 end
        casgiftapply.setStatus("4");
        List<CasgiftApply> casgiftapplylist = casgiftapplyMapper.select(casgiftapply);
        othertwo.setType("0");
        othertwoMapper.delete(othertwo);
        int rowundex = 0;
        for (CasgiftApply casgift : casgiftapplylist) {
            rowundex = rowundex + 1;
            String othertwoid = UUID.randomUUID().toString();
            if (tokenModel != null) {
                othertwo.preInsert(tokenModel);
            } else {
                othertwo.preInsert();
            }
            othertwo.setOthertwo_id(othertwoid);
            othertwo.setGiving_id(givingid);
            othertwo.setUser_id(casgift.getUser_id());
            List<CustomerInfo> customerinfo = customerinfoAll.stream().filter(item -> (item.getUserid().equals(casgift.getUser_id()))).collect(Collectors.toList());
            if(customerinfo.size() > 0){
                othertwo.setJobnumber(customerinfo.get(0).getUserinfo().getJobnumber());
            }
            othertwo.setType("0");
            othertwo.setRowindex(rowundex);
            othertwo.setRootknot(casgift.getTwoclass());
            othertwo.setMoneys(casgift.getAmoutmoney());
            othertwoMapper.insertSelective(othertwo);
        }
//        List<OtherTwo2> otherTwo2List = givingMapper.selectOthertwo(givingid);
//        if (otherTwo2List.size() > 0) {
//            for (OtherTwo2 otherTwo2 : otherTwo2List) {
//                if (tokenModel != null) {
//                    otherTwo2.preInsert(tokenModel);
//                } else {
//                    otherTwo2.preInsert();
//                }
//                otherTwo2.setUser_id(otherTwo2.getUser_id());
//                otherTwo2.setMoneys(otherTwo2.getMoneys());
//                otherTwo2.setOthertwo2_id(UUID.randomUUID().toString());
//                othertwo2Mapper.insert(otherTwo2);
//            }
//        }
    }

    /**
     * 生成基数表
     * FJL
     */
    @Override
    public void insertContrast(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("个人对比");
        Contrast contrast = new Contrast();
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baselist = baseMapper.select(base);
        if (baselist != null) {
            int rowindex = 0;
            for (Base base1 : baselist) {
                rowindex = rowindex + 1;
                String consrastid = UUID.randomUUID().toString();
                if (tokenModel != null) {
                    contrast.preInsert(tokenModel);
                } else {
                    contrast.preInsert();
                }
                contrast.setGiving_id(base1.getGiving_id());
                contrast.setContrast_id(consrastid);
                contrast.setUser_id(base1.getUser_id());
                contrast.setOwner(base1.getUser_id());
                contrast.setRowindex(rowindex);
                contrast.setDepartment_id(base1.getDepartment_id());
                //上月【当月实发工资】
                List<Wages> Wages = lastwages.stream().filter(coi -> (coi.getUser_id().contains(base1.getUser_id()))).collect(Collectors.toList());
                if(Wages.size() > 0){
                    contrast.setLastmonth(Wages.get(0).getRealwages());
                }
                contrastMapper.insertSelective(contrast);
            }
        }
    }

    @Override
    public void insert(String generation, TokenModel tokenModel) throws Exception {
        System.out.println("生成当月工资开始");
        long startTime =  System.currentTimeMillis();
        // 生成工资单的时候重新调用init方法获取最新的人员信息 By Skaixx
        initial();
        String givingid = UUID.randomUUID().toString();
        // 时间格式
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
        String strTemp = sf1.format(new Date());
        // 查询当前月份的giving
        Giving giving = new Giving();
        giving.setMonths(strTemp);
        giving.setActual("0");
        List<Giving> givinglist = givingMapper.select(giving);
        // 如果存在当月数据
        if (givinglist.size() != 0) {
            // 删除当前月份的基数表数据
            Base base = new Base();
            base.setGiving_id(givinglist.get(0).getGiving_id());
            baseMapper.delete(base);
            // 删除当前月份的契约表数据 ？？ 契约表干什么的
            Contrast contrast = new Contrast();
            contrast.setGiving_id(givinglist.get(0).getGiving_id());
            contrastMapper.delete(contrast);
            // 删除当前月份的其他1表数据
            OtherOne otherOne = new OtherOne();
            otherOne.setGiving_id(givinglist.get(0).getGiving_id());
            otherOneMapper.delete(otherOne);
            // 2020/03/14 add by myt start
            // 删除当前月份的入职表数据
            Induction induction = new Induction();
            induction.setGiving_id(givinglist.get(0).getGiving_id());
            inductionMapper.delete(induction);
            // 删除当前月份的退职表数据
            Retire retire = new Retire();
            retire.setGiving_id(givinglist.get(0).getGiving_id());
            retireMapper.delete(retire);
            // 2020/03/14 add by myt end
            // region 重新生成Giving时，删除旧数据 By Skaixx
            // dutyfree
            Dutyfree dutyfree = new Dutyfree();
            dutyfree.setGiving_id(givinglist.get(0).getGiving_id());
            dutyfreeMapper.delete(dutyfree);
            // disciplinary
            Disciplinary disciplinary = new Disciplinary();
            disciplinary.setGiving_id(givinglist.get(0).getGiving_id());
            disciplinaryMapper.delete(disciplinary);
            // accumulatedtax
            Accumulatedtax accumulatedtax = new Accumulatedtax();
            accumulatedtax.setGiving_id(givinglist.get(0).getGiving_id());
            accumulatedTaxMapper.delete(accumulatedtax);
            //comprehensive
            Comprehensive comprehensive = new Comprehensive();
            comprehensive.setGivingId(givinglist.get(0).getGiving_id());
            comprehensiveMapper.delete(comprehensive);
            // endregion
            //zqu start 删除wages当月数据
            Wages del_wage = new Wages();
            del_wage.setGiving_id(givinglist.get(0).getGiving_id());
            wagesMapper.delete(del_wage);
            //zqu end
        }
        // 删除当前月giving表数据
        giving = new Giving();
        giving.setMonths(strTemp);
        givingMapper.delete(giving);
        // 创建giving表数据
        giving = new Giving();
        if (tokenModel != null) {
            giving.preInsert(tokenModel);
        } else {
            giving.preInsert();
        }
        giving.setGiving_id(givingid);
        giving.setGeneration(generation);
        giving.setGenerationdate(new Date());
        giving.setMonths(sf1.format(new Date()));
        giving.setActual("0");
        givingMapper.insert(giving);
        /*
            插入其他相关表数据
         */
        // 2020/03/11 add by myt start
        //入职
        insertInduction(givingid, tokenModel);//0.7
        //退职
        insertRetire(givingid, tokenModel);//0.3
        // 2020/03/14 add by myt end
        //基数
        insertBase(givingid, tokenModel);//0.6
        //個人対比
        insertContrast(givingid, tokenModel);//4
        //其他2
        insertOtherTwo(givingid, tokenModel);//0.1
        //其他1
        insertOtherOne(givingid, tokenModel);
        //欠勤
        insertLackattendance(givingid, tokenModel);
        //加班
        insertResidual(givingid, tokenModel);
        long endTime =  System.currentTimeMillis();
        long usedTime = (endTime-startTime)/1000;
        System.out.println("生成当月工资结束");
        System.out.println("用时：" + usedTime + "秒");
    }


    protected void autoCreateGiving() throws Exception {
        insert("1", null);
    }

    @Override
    public List<Giving> getDataList(Giving giving) throws Exception {
        List<Giving> GivingListnew = new ArrayList<>();
        List<Giving> GivingList = givingMapper.select(giving);
        for(Giving gi : GivingList){
            if(com.mysql.jdbc.StringUtils.isNullOrEmpty(gi.getUser_id())){
                GivingListnew.add(gi);
            }
        }
        return GivingListnew;
    }

    @Override
    public void save(GivingVo givingvo, TokenModel tokenModel) throws Exception {
        switch (givingvo.getStrFlg()) {
            case "16":
                List<Contrast> contrastlist = givingvo.getContrast();
                if (contrastlist != null) {
                    for (Contrast contrast : contrastlist) {
                        contrast.preUpdate(tokenModel);
                        contrastMapper.updateByPrimaryKeySelective(contrast);
                    }
                }
                break;
            case "2":
                List<OtherOne> otheronelist = givingvo.getOtherOne();
                if (otheronelist != null) {
                    for (OtherOne otherOne : otheronelist) {
                        otherOne.preUpdate(tokenModel);
                        otherOneMapper.updateByPrimaryKeySelective(otherOne);
                    }
                }
                break;
            case "3":
                List<OtherTwo> otherTwolist = givingvo.getOtherTwo();
                if (otherTwolist != null) {
                    for (OtherTwo othertwo : otherTwolist) {
                        othertwo.preUpdate(tokenModel);
                        othertwoMapper.updateByPrimaryKeySelective(othertwo);
                        List<OtherTwo2> otherTwo2List = givingMapper.selectOthertwo(othertwo.getGiving_id());
//                        if (otherTwo2List.size() > 0) {
//                            for (OtherTwo2 otherTwo2 : otherTwo2List) {
//                                otherTwo2.preInsert(tokenModel);
//                                otherTwo2.setUser_id(otherTwo2.getUser_id());
//                                otherTwo2.setMoneys(otherTwo2.getMoneys());
//                                otherTwo2.setOthertwo2_id(UUID.randomUUID().toString());
//                                othertwo2Mapper.insert(otherTwo2);
//                            }
//                        }
                    }
                }
                break;
            case "8":   // 欠勤
                List<Lackattendance> lackattendanceList = givingvo.getLackattendance();
                lackattendanceList.forEach(item -> {
                    if (item.isDirty()) {
                        item.preUpdate(tokenModel);
                        lackattendanceMapper.updateByPrimaryKeySelective(item);
                    }
                });
                break;
            case "9":   // 残业
                List<Residual> residualList = givingvo.getResidual();
                residualList.forEach(item -> {
                    if (item.isDirty()) {
                        item.preUpdate(tokenModel);
                        residualMapper.updateByPrimaryKeySelective(item);
                    }
                });
                break;
            case "6":   // 入职
                List<Induction> inductionList = givingvo.getEntryVo();
                inductionList.forEach(item -> {
                    item.preUpdate(tokenModel);
                    inductionMapper.updateByPrimaryKeySelective(item);
                });
                break;
            case "7":   // 退职
                List<Retire> retireList = givingvo.getRetireVo();
                retireList.forEach(item -> {
                    item.preUpdate(tokenModel);
                    retireMapper.updateByPrimaryKeySelective(item);
                });
                break;
        }
    }

    public String getMouth(String mouth) {
        String _mouth = mouth.substring(5, 7);
        return _mouth;
    }

    //从1-day
    public int getWorkDays(int theYear, int theMonth, int theDay) {
        int workDays = 0;
        String holi = workingDayMapper.getHoliday(theYear, theMonth, theDay);
        Calendar cal = Calendar.getInstance();
        cal.set(theYear, theMonth - 1, 1);
        int days = theDay == 0 ? cal.getActualMaximum(Calendar.DAY_OF_MONTH) : theDay;
        for (int i = 0; i < days; i++) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                workDays++;
            }
            cal.add(Calendar.DATE, 1);
        }
        return (workDays - Integer.parseInt(holi));
    }

    //day-月末
    public int getIndutionDays(int theYear, int theMonth, int theDay) {
        int workDays = 0;
        int holi = theDay == 1 ? 0 : Integer.parseInt(workingDayMapper.getHoliday(theYear, theMonth, theDay - 1));
        Calendar cal = Calendar.getInstance();
        cal.set(theYear, theMonth - 1, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                workDays++;
            }
            cal.add(Calendar.DATE, 1);
        }
        return (workDays - holi);
    }

    public Map<String, Integer> getYMD(String date) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("year", Integer.parseInt(date.substring(0, 4)));
        map.put("mouth", Integer.parseInt(date.substring(5, 7)));
        if (date.length() > 10) {
            map.put("day", Integer.parseInt(date.substring(8, 10)));
        } else {
            map.put("day", Integer.parseInt(date.substring(8)));
        }
        return map;
    }

    public String getSalary(CustomerInfo customerInfo, int addMouth) throws ParseException {
        //转正日
        if (!StringUtils.isEmpty(customerInfo.getUserinfo().getEnddate())) {
            System.out.println(customerInfo.getUserinfo().getCustomername());
            if (customerInfo.getUserinfo().getEnddate().indexOf("Z")  != -1) {
                String enddate = customerInfo.getUserinfo().getEnddate().substring(0, 10).replace("-", "/");
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(sdfYMD.parse(enddate));//设置起时间
                cal1.add(Calendar.DATE, +1);
                customerInfo.getUserinfo().setEnddate(sdfYMD.format(cal1.getTime()));
            }
        }
        int intNewDate = Integer.parseInt(sdfYM.format(new Date()));
        int intEnddate = Integer.parseInt(customerInfo.getUserinfo().getEnddate().replace("/","").replace("-","").substring(0,6));
        String thisMouth = "0";
        //本月工资
        if(addMouth == 1){
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
                    thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()) + Double.parseDouble(customerInfo.getUserinfo().getDuty()));
                }
                else{
                    thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()));
                }
                //转正日大于当月的情况（试用期）工资为90%
                if(intEnddate > intNewDate){
                    thisMouth  = new BigDecimal(Double.parseDouble(ifNull(thisMouth)) * 0.9d).setScale(2, RoundingMode.HALF_UP).toPlainString();
                }
            }
        }
        else{ //上月基本工資
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserid())) {
                //上月发工资的人
                List<Wages> Wages = lastwages.stream().filter(coi -> (coi.getUser_id().contains(customerInfo.getUserid()))).collect(Collectors.toList());
                if(Wages.size() > 0){
                    //本月基本工资 + 本月职责工资
                    thisMouth = String.valueOf(Double.parseDouble(Wages.get(0).getBasethismonthbasic()) + Double.parseDouble(Wages.get(0).getThismonthduty()));
                }
                else{
                    //上月发工资之后入职的实习工资
                    if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
                        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
                            thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()) + Double.parseDouble(customerInfo.getUserinfo().getDuty()));
                        }
                        else{
                            thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()));
                        }
                        //转正日大于当月的情况（试用期）工资为90%
                        if(intEnddate > intNewDate){
                            thisMouth  = new BigDecimal(Double.parseDouble(ifNull(thisMouth)) * 0.9d).setScale(2, RoundingMode.HALF_UP).toPlainString();
                        }
                    }
                }
            }
        }
        // UPD_GBB_2020/6/9 end
        return thisMouth;
    }

    public Map<String, String> getSalaryBasicAndDuty(CustomerInfo customerInfo, int addMouth) throws ParseException {
        //转正日
        if (StringUtils.isEmpty(customerInfo.getUserinfo().getEnddate())) {
            if (customerInfo.getUserinfo().getEnddate().indexOf("Z")  != -1) {
                String enddate = customerInfo.getUserinfo().getEnddate().substring(0, 10).replace("-", "/");
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(sdfYMD.parse(enddate));//设置起时间
                cal1.add(Calendar.DATE, +1);
                customerInfo.getUserinfo().setEnddate(sdfYMD.format(cal1.getTime()));
            }
        }
        int intNewDate = Integer.parseInt(sdfYM.format(new Date()));
        int intEnddate = Integer.parseInt(customerInfo.getUserinfo().getEnddate().replace("/","").replace("-","").substring(0,6));
        // UPD_GBB_2020/05/20 ALL
        String thisMonth = "0";
        String thisMonthBasic = "0";
        String thisMonthDuty = "0";
        //本月工资
        if(addMouth == 1){
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
                    thisMonth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()) + Double.parseDouble(customerInfo.getUserinfo().getDuty()));
                    thisMonthBasic = customerInfo.getUserinfo().getBasic();
                    thisMonthDuty = customerInfo.getUserinfo().getDuty();
                }
                else{
                    thisMonth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()));
                }
                //转正日大于当月的情况（试用期）工资为90%
                if(intEnddate > intNewDate){
                    thisMonth  = new BigDecimal(Double.parseDouble(ifNull(thisMonth)) * 0.9d).setScale(2, RoundingMode.HALF_UP).toPlainString();
                    //本月基本工资=本月基本工资*0.9 - 本月职责工资 * 0.1
                    thisMonthBasic = new BigDecimal(Double.parseDouble(ifNull(thisMonthBasic)) * 0.9d - Double.parseDouble(ifNull(thisMonthDuty)) * 0.1d).setScale(2, RoundingMode.HALF_UP).toPlainString();
                }
            }
        }
        else{ //上月基本工資
            List<Wages> Wages = lastwages.stream().filter(coi -> (coi.getUser_id().contains(customerInfo.getUserid()))).collect(Collectors.toList());
            if(Wages.size() > 0){
                //本月基本工资（上月）
                thisMonth = Wages.get(0).getBasethismonthbasic();
                //本月基本工资（上月）
                thisMonthBasic = Wages.get(0).getBasethismonthbasic();
                //本月职责工资（上月）
                thisMonthDuty = Wages.get(0).getThismonthduty();
            }
            else{
                //上月发工资之后入职的实习工资
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
                    if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
                        thisMonth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()) + Double.parseDouble(customerInfo.getUserinfo().getDuty()));
                        thisMonthBasic = customerInfo.getUserinfo().getBasic();
                        thisMonthDuty = customerInfo.getUserinfo().getDuty();
                    }
                    else{
                        thisMonth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()));
                    }
                    //转正日大于当月的情况（试用期）工资为90%
                    if(intEnddate > intNewDate){
                        thisMonth  = new BigDecimal(Double.parseDouble(ifNull(thisMonth)) * 0.9d).setScale(2, RoundingMode.HALF_UP).toPlainString();
                        //本月基本工资=本月基本工资*0.9 - 本月职责工资 * 0.1
                        thisMonthBasic = new BigDecimal(Double.parseDouble(ifNull(thisMonthBasic)) * 0.9d - Double.parseDouble(ifNull(thisMonthDuty)) * 0.1d).setScale(2, RoundingMode.HALF_UP).toPlainString();
                    }
                }
            }
        }
        // UPD_GBB_2020/6/9 end

        Map<String, String> map = new HashMap<String, String>();
        map.put("thisMonth", thisMonth);
        map.put("thisMonthBasic", thisMonthBasic);
        map.put("thisMonthDuty", thisMonthDuty);
        return map;
    }

    public String notNull(String data) {
        if (data == null || data.equals("")) {
            return "0";
        }
        return data;
    }

    public Map<String, Integer> getYMD(Date date) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        Calendar calder = Calendar.getInstance();
        calder.setTime(date);
        int year = calder.get(Calendar.YEAR);
        int month = calder.get(Calendar.MONTH);
        int day = calder.get(Calendar.DAY_OF_MONTH);
        int yearMonth = year * 100 + month;
        int lastDay = calder.getActualMaximum(Calendar.DAY_OF_MONTH);
        map.put("day", day);
        map.put("year", year);
        map.put("month", month);
        map.put("yearMonth", yearMonth);
        map.put("lastDay", lastDay);
        return map;
    }

    //根据产修算出当月产假日数
    public double getWorkDays(AbNormal abNormal, AttendanceSetting attendanceSetting) {
//        double lengthTime = Double.valueOf(abNormal.getLengthtime());
//        DecimalFormat df = new DecimalFormat("#.00");
//        Map<String, Integer> start= getYMD(abNormal.getOccurrence_date());
//        Map<String, Integer> end= getYMD(abNormal.getFinished_date());
//        Map<String, Integer> now = getYMD(new Date());
//        if(start.get("yearMonth") == now.get("yearMonth")){
//            if(end.get("yearMonth") == now.get("yearMonth")){
//             return Double.valueOf(lengthTime/8) ;
//            }else if(end.get("yearMonth") > now.get("yearMonth")){
//                if(now.get("lastDay") == now.get("day")){
//                  //  attendanceSetting
//                 //   return  workDays -
//                }
//                return  0;
//            }
//        }else if(start.get("yearMonth") < now.get("yearMonth")){
//            if(end.get("yearMonth") == now.get("yearMonth")){
//               // return df.format(Double.valueOf(workDays) - lengthTime/8);
//            }else if(end.get("yearMonth") > now.get("yearMonth")){
//                return 0;
//            }
//        }
        return 0;
    }

    /**
     * @return
     * @Method appreciationCalc
     * @Author SKAIXX
     * @Description 月度赏与计算
     * @Date 2020/3/16 14:28
     * @Param [appreciationlist]
     **/
    private List<Appreciation> appreciationCalc(List<Base> baseList, List<Appreciation> appreciationlist) {

        for (Appreciation appreciation : appreciationlist) {
            // 获取给与标准
            double rnbasesalary = 0;
            baseList  = baseList.stream().filter(item -> item.getUser_id().equals(appreciation.getUser_id())).collect(Collectors.toList());
            if(baseList.size() > 0){
                rnbasesalary = Double.parseDouble(baseList.get(0).getRnbasesalary());
            }
            if (com.mysql.jdbc.StringUtils.isNullOrEmpty(appreciation.getAmount())) {
                if(!appreciation.getCommentary().equals("")){
                    // 获取评价奖金百分比
                    Dictionary commentaryDic = new Dictionary();
                    commentaryDic.setValue1(appreciation.getCommentary());
                    commentaryDic.setPcode("PR056");
                    commentaryDic = dictionaryMapper.select(commentaryDic).get(0);
                    if(commentaryDic != null){
                        commentaryDic.setValue2(commentaryDic.getValue2().replace("%", ""));
                        // 奖金计算
                        // 月赏与
                        double monthAppreciation = rnbasesalary * 1.8d / 12d;
                        appreciation.setAmount(BigDecimal.valueOf(monthAppreciation
                                * Double.parseDouble(commentaryDic.getValue2()) / 100).setScale(-1, RoundingMode.HALF_UP).toPlainString());
                    }
                }
            }
        }
        return appreciationlist;
    }

    /**
     * @return com.nt.dao_Org.Dictionary
     * @Method getTaxRate
     * @Author SKAIXX
     * @Description 通过年度应纳税总额求出税率及速算扣除额
     * @Date 2020/3/12 13:47
     * @Param [salary, dictionaryList]
     **/
    private Dictionary getTaxRate(double salary, List<Dictionary> dictionaryList) {
        double currentVal;
        double nextVal;
        for (int i = 0; i < dictionaryList.size(); i++) {
            currentVal = Double.parseDouble(dictionaryList.get(i).getValue1());
            if (i != dictionaryList.size() - 1) {
                nextVal = Double.parseDouble(dictionaryList.get(i + 1).getValue1());
            } else {
                return dictionaryList.get(i);
            }
            if ((currentVal < salary || salary == 0d) && nextVal >= salary) {
                return dictionaryList.get(i);
            }
        }
        return null;
    }

    /**
     * @return void
     * @Method insertResidual
     * @Author SKAIXX
     * @Description 残业计算
     * @Date 2020/3/13 15:51
     * @Param [givingid, tokenModel]
     **/
    @Override
    public void insertResidual(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("加班");
        // region 获取当月与前月
        Calendar cal = Calendar.getInstance();

        // 当月
        String currentYear = String.valueOf(cal.get(Calendar.YEAR));
        String currentMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");

        // 前月
        cal.add(Calendar.MONTH, -1);
        String preYear = String.valueOf(cal.get(Calendar.YEAR));
        String preMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");
        // endregion

        // 获取基数表数据
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baseList = baseMapper.select(base);

        // 代休截止间隔修改为从字典中获取
//        Dictionary replaceDic = new Dictionary();
//        replaceDic.setCode("PR061001");    // 代休间隔
//        replaceDic = dictionaryMapper.select(replaceDic).get(0);
        Dictionary replaceDic = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR061001"))).collect(Collectors.toList()).get(0);
        int rep = Integer.parseInt(replaceDic.getValue1());

        // 以基数表数据为单位循环插入残业数据
        baseList.forEach(item -> {
            Residual residual = new Residual();
            // 名字
            residual.setUser_id(item.getUser_id());
            // Rn
            residual.setRn(item.getRn());
            residual.setGiving_id(givingid);
            residual.setResidual_id(UUID.randomUUID().toString());
            residual.setRowindex(item.getRowindex());

            // 本月是否已退职(员工ID如果在退职表里存在，则视为已退职)
            Retire retire = new Retire();
            retire.setUser_id(item.getUser_id());
            boolean isResign = retireMapper.select(retire).size() > 0;

            double totalh = 0d; // 合计加班小时

            // 获取前月勤怠表数据
            Attendance attendance = new Attendance();
            attendance.setUser_id(item.getUser_id());
            attendance.setYears(preYear);
            attendance.setMonths(preMonth);
            List<Attendance> attendanceList = attendanceMapper.select(attendance);
            // region 前月残业数据
            // 平日加班（150%）
            residual.setLastweekdays(BigDecimal.valueOf(attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getOrdinaryindustry()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalh += Double.parseDouble(residual.getLastweekdays());

            // 休日加班（200%）
            residual.setLastrestDay(BigDecimal.valueOf(attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getWeekendindustry()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalh += Double.parseDouble(residual.getLastrestDay());

            // 法定加班（300%）
            residual.setLastlegal(BigDecimal.valueOf(attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getStatutoryresidue()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalh += Double.parseDouble(residual.getLastlegal());

            // 代休
            // 从代休视图获取该员工所有代休
            List<restViewVo> restViewVoList = annualLeaveMapper.getrest(item.getUser_id());
            // 获取3个月之前的代休
            cal.add(Calendar.MONTH, -2 + rep * -1);
            String restYear = String.valueOf(cal.get(Calendar.YEAR));
            String restMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");

            if (restViewVoList.size() > 0) {
                residual.setLastreplace(String.valueOf(restViewVoList.stream().
                        filter(subItem -> subItem.getApplicationdate()
                                .equals(restYear + restMonth))
                        .mapToDouble(tmp -> Double.parseDouble(ifNull(tmp.getRestdays()))).sum() * 8d));
                totalh += Double.parseDouble(residual.getLastreplace());
            } else {
                residual.setLastreplace("0");
            }

            // 合计(H)
            residual.setLasttotalh(BigDecimal.valueOf(totalh).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 合计(元)
            residual.setLasttotaly(overtimeCalc(item, residual, "pre"));
            // endregion

            // 获取本月勤怠表数据
            attendance = new Attendance();
            attendance.setUser_id(item.getUser_id());
            attendance.setYears(currentYear);
            attendance.setMonths(currentMonth);
            attendanceList = attendanceMapper.select(attendance);
            totalh = 0d;

            // region 本月残业数据
            // 平日加班（150%）
            residual.setThisweekdays(BigDecimal.valueOf(attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getOrdinaryindustry()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalh += Double.parseDouble(residual.getThisweekdays());

            // 休日加班（200%）
            residual.setThisrestDay(BigDecimal.valueOf(attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getWeekendindustry()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalh += Double.parseDouble(residual.getThisrestDay());

            // 法定加班（300%）
            residual.setThislegal(BigDecimal.valueOf(attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getStatutoryresidue()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());
            totalh += Double.parseDouble(residual.getThislegal());

            // 本月代休(3か月前)
            cal.add(Calendar.MONTH, 1);
            String currentRestYear = String.valueOf(cal.get(Calendar.YEAR));
            String currentRestMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");

            if (restViewVoList.size() > 0) {
                residual.setThisreplace(String.valueOf(restViewVoList.stream().
                        filter(subItem -> subItem.getApplicationdate()
                                .equals(currentRestYear + currentRestMonth))
                        .mapToDouble(tmp -> Double.parseDouble(ifNull(tmp.getRestdays()))).sum() * 8d));
                totalh += Double.parseDouble(residual.getThisreplace());
            } else {
                residual.setThisreplace("0");
            }

            // 本月代休（3か月以内）
            int index = 0;
            List<String> conditionList = new ArrayList<>();
            while (index <= rep) {
                cal.add(Calendar.MONTH, 1);
                conditionList.add(cal.get(Calendar.YEAR) + String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0"));
                index++;
            }

            if (restViewVoList.size() > 0) {
                residual.setThisreplace3(String.valueOf(restViewVoList.stream().
                        filter(subItem -> conditionList.contains(subItem.getApplicationdate()))
                        .mapToDouble(tmp -> Double.parseDouble(ifNull(tmp.getRestdays()))).sum() * 8d));
                totalh += Double.parseDouble(residual.getThisreplace3());
            } else {
                residual.setThisreplace3("0");
            }

            // 合计(H)
            residual.setThistotalh(BigDecimal.valueOf(totalh).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 合计(元)
            residual.setThistotaly(overtimeCalc(item, residual, "current"));
            // endregion

            // 备考
            residual.setRemarks(isResign ? "退职" : "-");

            // 加班补助
            residual.setSubsidy(BigDecimal.valueOf(Double.parseDouble(residual.getLasttotaly()) + Double.parseDouble(residual.getThistotaly())).setScale(2, RoundingMode.HALF_UP).toPlainString());
            if (tokenModel != null) {
                residual.preInsert(tokenModel);
            } else {
                residual.preInsert();
            }
            residualMapper.insert(residual);
        });
    }

    /**
     * @return
     * @Method ifNull
     * @Author SKAIXX
     * @Description 数值Null替换
     * @Date 2020/3/14 14:15
     * @Param [val]
     **/
    private String ifNull(String val) {
        if (!StringUtils.isEmpty(val)) {
            return val;
        }
        return "0";
    }

    /**
     * @return
     * @Method overtimeCalc
     * @Author SKAIXX
     * @Description 加班费计算
     * @Date 2020/3/16 10:50
     * @Param [base, residual, mode]
     **/
    private String overtimeCalc(Base base, Residual residual, String mode) {

        double total = 0d;  // 总加班费

        // 判断员工当月级别是否为R8及以上
        boolean isOverR8 = false;
        //update gbb 20210318 从2020年4月开始取消R8以及以上人员的加班费用限制 start
//        String rn = com.nt.utils.StringUtils.isEmpty(base.getRn()) || "その他".equals(base.getRn()) ? "PR021001" : base.getRn();
//        if (Integer.parseInt(rn.substring(rn.length() - 2)) > 5) {
//            isOverR8 = true;
//        }
        //update gbb 20210318 从2020年4月开始取消R8以及以上人员的加班费用限制 end

        if ("pre".equals(mode)) {   // 前月加班费计算
            // 3个月前小时工资 = 月工资÷21.75天÷8小时
            double salaryPerHourTma = Double.parseDouble(base.getTmabasic()) / 21.75d / 8d;
            // 2020/06/05 UPDATE by myt start //GBB
            // 前月小时工资
            //double salaryPerHour = Double.parseDouble(base.getThismonth()) / 21.75d / 8d;
            //上月基本工资
            double salaryPerHour = Double.parseDouble(base.getLastmonthbasic()) / 21.75d / 8d;
            // 2020/06/05 UPDATE by myt END //GBB
            // 平日加班费 150%
            total += isOverR8 ? 0d : Double.parseDouble(ifNull(residual.getLastweekdays())) * salaryPerHour * 1.5d;
            // 休日加班费 200%
            total += isOverR8 ? 0d : Double.parseDouble(ifNull(residual.getLastrestDay())) * salaryPerHour * 2.0d;
            // 法定加班费 300%
            total += Double.parseDouble(ifNull(residual.getLastlegal())) * salaryPerHour * 3.0d;
            // 代休加班费 200%
            total += isOverR8 ? 0d : Double.parseDouble(ifNull(residual.getLastreplace())) * 8d * salaryPerHourTma * 2.0d;
        } else {    // 当月加班费计算
            // 小时工资 = 月工资÷21.75天÷8小时
            double salaryPerHour = Double.parseDouble(base.getThismonth()) / 21.75d / 8d;
            // 前月小时工资
            double lastSalaryPerHour = Double.parseDouble(base.getLastmonth()) / 21.75d / 8d;
            // 平日加班费 150%
            total += isOverR8 ? 0d : Double.parseDouble(ifNull(residual.getThisweekdays())) * salaryPerHour * 1.5d;
            // 休日加班费 200%
            total += isOverR8 ? 0d : Double.parseDouble(ifNull(residual.getThisrestDay())) * salaryPerHour * 2.0d;
            // 法定加班费 300%
            total += Double.parseDouble(ifNull(residual.getThislegal())) * salaryPerHour * 3.0d;
            // 代休加班费 200% (本月代休加班费 = (3个月前代休+3个月之内的代休) * 前月小时工资 * 200%)
            total += isOverR8 ? 0d : (Double.parseDouble(ifNull(residual.getThisreplace3())) + Double.parseDouble(ifNull(residual.getThisreplace()))) * 8d * lastSalaryPerHour * 2.0d;
        }
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * @return com.nt.dao_Pfans.PFANS2000.Residual
     * @Method thisMonthOvertimeChange
     * @Author SKAIXX
     * @Description 本月加班数据变更时，重新计算加班费合计
     * @Date 2020/3/18 19:02
     * @Param [base, residual]
     **/
    @Override
    public Residual thisMonthOvertimeChange(GivingVo givingVo) {

        Residual residual = givingVo.getResidual().get(0);
        String userId = residual.getUser_id();
        Base base = givingVo.getBase().stream().filter(item -> item.getUser_id().equals(userId)).collect(Collectors.toList()).get(0);
        double total = 0d;  // 总加班费

        // 判断员工当月级别是否为R8及以上
        boolean isOverR8 = false;
        String rn = StringUtils.isEmpty(base.getRn()) ? "PR021001" : base.getRn();
        if (Integer.parseInt(rn.substring(rn.length() - 2)) > 5) {
            isOverR8 = true;
        }

        // 小时工资 = 月工资÷21.75天÷8小时
        double salaryPerHour = Double.parseDouble(base.getThismonth()) / 21.75d / 8d;
        // 前月小时工资
        double lastSalaryPerHour = Double.parseDouble(base.getLastmonth()) / 21.75d / 8d;
        // 平日加班费 150%
        total += isOverR8 ? 0d : Double.parseDouble(ifNull(residual.getThisweekdays())) * salaryPerHour * 1.5d;
        // 休日加班费 200%
        total += isOverR8 ? 0d : Double.parseDouble(ifNull(residual.getThisrestDay())) * salaryPerHour * 2.0d;
        // 法定加班费 300%
        total += Double.parseDouble(ifNull(residual.getThislegal())) * salaryPerHour * 3.0d;
        // 代休加班费 200% (本月代休加班费 = (3个月前代休+3个月之内的代休) * 前月小时工资 * 200%)
        total += isOverR8 ? 0d : (Double.parseDouble(ifNull(residual.getThisreplace3())) + Double.parseDouble(ifNull(residual.getThisreplace()))) * 8d * lastSalaryPerHour * 2.0d;

        residual.setThistotaly(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toPlainString());
        residual.setSubsidy(new BigDecimal(residual.getThistotaly()).add(new BigDecimal(residual.getLasttotaly())).setScale(2, RoundingMode.HALF_UP).toPlainString());
        return residual;
    }

    /**
     * @return com.nt.dao_Pfans.PFANS2000.Lackattendance
     * @Method thisMonthLacktimeChange
     * @Author SKAIXX
     * @Description 本月欠勤数据变更时，重新计算欠勤费合计
     * @Date 2020/3/19 10:04
     * @Param [givingVo, tokenModel]
     **/
    @Override
    public Lackattendance thisMonthLacktimeChange(GivingVo givingVo) {
        double total = 0d;  // 总欠勤费

        Lackattendance lackattendance = givingVo.getLackattendance().get(0);
        Base base = givingVo.getBase().stream().filter(item -> item.getUser_id().equals(lackattendance.getUser_id())).collect(Collectors.toList()).get(0);

        // 当月小时工资 = 月工资÷21.75天÷8小时
        double currentSalaryPerHour = Double.parseDouble(base.getThismonth()) / 21.75d / 8d;

        // 获取短病欠扣除比例
//        Dictionary shortDictionary = new Dictionary();
//        shortDictionary.setCode("PR049005");    // 短病欠扣除比例
//        shortDictionary = dictionaryMapper.select(shortDictionary).get(0);
        Dictionary shortDictionary = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR049005"))).collect(Collectors.toList()).get(0);

        // 获取长病欠
//        Dictionary longDictionary = new Dictionary();
//        longDictionary.setCode("PR047001");     // 大連社会最低賃金
//        longDictionary = dictionaryMapper.select(longDictionary).get(0);
        Dictionary longDictionary = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR047001"))).collect(Collectors.toList()).get(0);
        // 长病欠小时工资
        double longSalary = Double.parseDouble(longDictionary.getValue2()) / 21.75d / 8d;

        // 当月欠勤费用
        // 欠勤费用-正式
        total += Double.parseDouble(ifNull(lackattendance.getThisdiligenceformal())) * currentSalaryPerHour;
        // 欠勤费用-试用
        total += Double.parseDouble(ifNull(lackattendance.getThisdiligencetry())) * currentSalaryPerHour * 0.9d;

        // 短病欠-正式
        total += Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencyformal())) * currentSalaryPerHour
                * Double.parseDouble(shortDictionary.getValue2());
        // 短病欠-试用
        total += Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencytry())) * currentSalaryPerHour * 0.9d
                * Double.parseDouble(shortDictionary.getValue2());

        // 长病欠-正式
        total += Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencyformal())) * longSalary;
        // 长病欠-试用
        total += Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencytry())) * longSalary * 0.9d;
        lackattendance.setThistotal(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toPlainString());
        lackattendance.setGive(new BigDecimal(lackattendance.getThistotal()).add(new BigDecimal(lackattendance.getLasttotal())).setScale(2, RoundingMode.HALF_UP).toPlainString());
        return lackattendance;
    }

    /**
     * @return
     * @Method insertLackattendance
     * @Author SKAIXX
     * @Description 欠勤数据插入
     * @Date 2020/3/16 8:52
     * @Param [givingid, tokenModel]
     **/
    @Override
    public void insertLackattendance(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("欠勤");
        // region 获取基数表数据
        Base base = new Base();
        base.setGiving_id(givingid);
        List<Base> baseList = baseMapper.select(base);
        // endregion

        // region 获取当月与前月
        Calendar cal = Calendar.getInstance();

        // 当月
        String currentYear = String.valueOf(cal.get(Calendar.YEAR));
        String currentMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");

        // 前月
        cal.add(Calendar.MONTH, -1);
        String preYear = String.valueOf(cal.get(Calendar.YEAR));
        String preMonth = String.format("%2d", cal.get(Calendar.MONTH) + 1).replace(" ", "0");
        // endregion

        // 以基数表数据为单位循环插入欠勤数据
        baseList.forEach(item -> {

            // 本月是否已退职(员工ID如果在退职表里存在，则视为已退职)
            Retire retire = new Retire();
            retire.setUser_id(item.getUser_id());
            boolean isResign = retireMapper.select(retire).size() > 0;

            Lackattendance lackattendance = new Lackattendance();

            lackattendance.setLackattendance_id(UUID.randomUUID().toString());
            lackattendance.setGiving_id(givingid);
            lackattendance.setUser_id(item.getUser_id());
            lackattendance.setRowindex(item.getRowindex());

            // 取得前月欠勤数据
            Attendance attendance = new Attendance();
            attendance.setUser_id(item.getUser_id());
            attendance.setYears(preYear);
            attendance.setMonths(preMonth);
            List<Attendance> attendanceList = attendanceMapper.select(attendance);

            // 欠勤-试用(试用无故旷工 + 试用事假)
            double Lastdiligencetry  =  attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTabsenteeism()))).sum()
                    + attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTcompassionateleave()))).sum();
            lackattendance.setLastdiligencetry(BigDecimal.valueOf(Lastdiligencetry).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 短病欠-试用
            lackattendance.setLastshortdeficiencytry(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTshortsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 长病欠-试用
            lackattendance.setLastchronicdeficiencytry(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTlongsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 欠勤-正式(无故旷工 + 正式事假)
            double Lastdiligenceformal = attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getAbsenteeism()))).sum()
                    + attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getCompassionateleave()))).sum();
            lackattendance.setLastdiligenceformal(BigDecimal.valueOf(Lastdiligenceformal).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 短病欠-正式
            lackattendance.setLastshortdeficiencyformal(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getShortsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 长病欠-正式
            lackattendance.setLastchronicdeficiencyformal(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getLongsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 欠勤
            lackattendance.setLastdiligence(lackSumCalc(lackattendance.getLastdiligencetry(), lackattendance.getLastdiligenceformal()));

            // 短病欠
            lackattendance.setLastshortdeficiency(lackSumCalc(lackattendance.getLastshortdeficiencyformal(), lackattendance.getLastshortdeficiencytry()));

            // 长病欠
            lackattendance.setLastchronicdeficiency(lackSumCalc(lackattendance.getLastchronicdeficiencytry(), lackattendance.getLastchronicdeficiencyformal()));

            // 前月合计
            lackattendance.setLasttotal(lackAttendanceCalc(item, lackattendance, "pre"));

            // 获取本月考勤数据
            attendance = new Attendance();
            attendance.setUser_id(item.getUser_id());
            attendance.setYears(currentYear);
            attendance.setMonths(currentMonth);
            attendanceList = attendanceMapper.select(attendance);

            // 欠勤-试用(试用无故旷工 + 试用事假)
            double Thisdiligencetry  =  attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTabsenteeism()))).sum()
                    + attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTcompassionateleave()))).sum();
            lackattendance.setThisdiligencetry(BigDecimal.valueOf(Thisdiligencetry).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 短病欠-试用
            lackattendance.setThisshortdeficiencytry(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTshortsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 长病欠-试用
            lackattendance.setThischronicdeficiencytry(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getTlongsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 欠勤-正式(无故旷工 + 正式事假)
            double Thisdiligenceformal = attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getAbsenteeism()))).sum()
                    + attendanceList.stream().mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getCompassionateleave()))).sum();
            lackattendance.setThisdiligenceformal(BigDecimal.valueOf(Thisdiligenceformal).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 短病欠-正式
            lackattendance.setThisshortdeficiencyformal(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getShortsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 长病欠-正式
            lackattendance.setThischronicdeficiencyformal(BigDecimal.valueOf(attendanceList.stream()
                    .mapToDouble(subItem -> Double.parseDouble(ifNull(subItem.getLongsickleave()))).sum()).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // 欠勤
            lackattendance.setThisdiligence(lackSumCalc(lackattendance.getThisdiligencetry(), lackattendance.getThisdiligenceformal()));

            // 短病欠
            lackattendance.setThisshortdeficiency(lackSumCalc(lackattendance.getThisshortdeficiencytry(), lackattendance.getThisshortdeficiencyformal()));

            // 长病欠
            lackattendance.setThischronicdeficiency(lackSumCalc(lackattendance.getThischronicdeficiencytry(), lackattendance.getThischronicdeficiencyformal()));

            // 本月合计
            lackattendance.setThistotal(lackAttendanceCalc(item, lackattendance, "current"));

            // 备考
            lackattendance.setRemarks(isResign ? "退职" : "-");

            // 控除给料

            lackattendance.setGive(BigDecimal.valueOf(Double.parseDouble(lackattendance.getLasttotal())
                    + Double.parseDouble(lackattendance.getThistotal())).setScale(2, RoundingMode.HALF_UP).toPlainString());

            // give不为0.00的时候数据加负号
//            if (!"0.00".equals(lackattendance.getGive())) {
//                lackattendance.setGive("-" + lackattendance.getGive());
//            }

            if (tokenModel != null) {
                lackattendance.preInsert(tokenModel);
            } else {
                lackattendance.preInsert();
            }
            lackattendanceMapper.insert(lackattendance);
        });
    }

    /**
     * @return java.lang.String
     * @Method lackSumCalc
     * @Author SKAIXX
     * @Description 欠勤试用与正式合计计算
     * @Date 2020/3/18 16:03
     * @Param [val1, val2]
     **/
    private String lackSumCalc(String val1, String val2) {
        return BigDecimal.valueOf(Double.parseDouble(ifNull(val1)) + Double.parseDouble(ifNull(val2))).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * @return java.lang.String
     * @Method lackAttendanceCalc
     * @Author SKAIXX
     * @Description 合计欠勤计算
     * @Date 2020/3/16 9:45
     * @Param [base, lackattendance]
     **/
    private String lackAttendanceCalc(Base base, Lackattendance lackattendance, String mode) {
        double total = 0d;  // 总欠勤费
        // 前月正式小时工资 = 月工资÷21.75天÷8小时
        double preSalaryPerHour = Double.parseDouble(base.getLastmonth()) / 21.75d / 8d;

        // 当月小时工资 = 月工资÷21.75天÷8小时
        double currentSalaryPerHour = Double.parseDouble(base.getThismonth()) / 21.75d / 8d;

        // 获取短病欠扣除比例
//        Dictionary shortDictionary = new Dictionary();
//        shortDictionary.setCode("PR049005");    // 短病欠扣除比例
//        shortDictionary = dictionaryMapper.select(shortDictionary).get(0);
        Dictionary shortDictionary = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR049005"))).collect(Collectors.toList()).get(0);

        // 获取长病欠
//        Dictionary longDictionary = new Dictionary();
//        longDictionary.setCode("PR047001");     // 大連社会最低賃金
//        longDictionary = dictionaryMapper.select(longDictionary).get(0);
        Dictionary longDictionary = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR047001"))).collect(Collectors.toList()).get(0);
        // 长病欠小时工资
        double longSalary = Double.parseDouble(longDictionary.getValue2()) / 21.75d / 8d;

        // UPD_GBB_2020/06/11 update
        if ("pre".equals(mode)) {   // 前月欠勤费用
            // 欠勤费用-正式
            double Lastdiligenceformal = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getLastdiligenceformal())) * preSalaryPerHour).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 欠勤费用-试用
            double Lastdiligencetry = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getLastdiligencetry())) * preSalaryPerHour).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 短病欠-正式
            double Lastshortdeficiencyformal = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getLastshortdeficiencyformal())) * preSalaryPerHour
                    * Double.parseDouble(shortDictionary.getValue2())).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 短病欠-试用
            double Lastshortdeficiencytry = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getLastshortdeficiencytry())) * preSalaryPerHour
                    * Double.parseDouble(shortDictionary.getValue2())).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 长病欠-正式(本月工资- 上月工资/21.75/8*长病假 + 1810/21.75/8*长病假 )
            //total += Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencyformal())) * longSalary;
            double Lastchronicdeficiencyformal1 = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencyformal())) * preSalaryPerHour).setScale(2, RoundingMode.HALF_UP).doubleValue();
            double Lastchronicdeficiencyformal = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencyformal())) * longSalary).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 长病欠-试用
            double Lastchronicdeficiencytry = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencytry())) * longSalary).setScale(2, RoundingMode.HALF_UP).doubleValue();

            //费用-正式
            double totala = - Lastdiligenceformal - Lastshortdeficiencyformal - Lastchronicdeficiencyformal1 + Lastchronicdeficiencyformal;
            //费用-试用
            double totalb = - Lastdiligencetry - Lastshortdeficiencytry - Lastchronicdeficiencytry;
            total = totala + totalb;
        } else {    // 当月欠勤费用
            // 欠勤费用-正式
            double Thisdiligenceformal = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getThisdiligenceformal())) * currentSalaryPerHour).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 欠勤费用-试用
            double Thisdiligencetry = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getThisdiligencetry())) * currentSalaryPerHour).setScale(2, RoundingMode.HALF_UP).doubleValue();

            // 短病欠-正式
            double Thisshortdeficiencyformal = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencyformal())) * currentSalaryPerHour
                    * Double.parseDouble(shortDictionary.getValue2())).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 短病欠-试用
            double Thisshortdeficiencytry = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencytry())) * currentSalaryPerHour
                    * Double.parseDouble(shortDictionary.getValue2())).setScale(2, RoundingMode.HALF_UP).doubleValue();

            // 长病欠-正式
            double Thischronicdeficiencyformal = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencyformal())) * longSalary).setScale(2, RoundingMode.HALF_UP).doubleValue();
            // 长病欠-试用
            double Thischronicdeficiencytry = new BigDecimal(Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencytry())) * longSalary).setScale(2, RoundingMode.HALF_UP).doubleValue();
            //费用-正式
            double totala = - Thisdiligenceformal - Thisshortdeficiencyformal - Thisshortdeficiencytry;
            //费用-试用
            double totalb = - Thisdiligencetry - Thischronicdeficiencyformal - Thischronicdeficiencytry;
            total = totala + totalb;
        }
        // UPD_GBB_2020/06/11 update
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    //试用天数计算
    private int getTrialWorkDaysExceptWeekend(Date start, Date end) throws Exception{
        if(sdfYMD.format(start).equals(sdfYMD.format(end))){
            return 1;
        }
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        //只取年月日比较
        start = ymd.parse(ymd.format(start));
        end = ymd.parse(ymd.format(end));

        int workDays = 0;
        //获取周末带薪假日
        int intDays = 0;
        //振替休日
        int workingDayscount = 0;
        //每月正常工作日
        if (end.getTime() > start.getTime()) {
            Calendar calStar = Calendar.getInstance();
            calStar.setTime(start);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            while (calStar.compareTo(calEnd) <= 0) {
                if (calStar.get(Calendar.DAY_OF_WEEK) != 7 && calStar.get(Calendar.DAY_OF_WEEK) != 1) {
                    workDays++;
                }
                calStar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        //工作日表
        List<WorkingDay> workingDaysList = workingDayMapper.getWorkingday(ymd.format(start),ymd.format(end));
        for (int i = 0; i < workingDaysList.size(); i++) {
            Date bdate = workingDaysList.get(i).getWorkingdate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            //获取带薪假日（打定节假日+振替出勤日+会社特别休日）周末带薪日
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                //法定日（周末）+振替出勤日
                if(workingDaysList.get(i).getType().equals("1") || workingDaysList.get(i).getType().equals("4")){
                    intDays = intDays + 1;
                }
            }
            //振替休日
            if(workingDaysList.get(i).getType().equals("3")){
                workingDayscount = workingDayscount + 1;
            }
        }
        //正常工作日 - 振替休日 + 法定日（周末）+振替出勤日
        return workDays - workingDayscount + intDays;
    }

    //获取工作日-lxx
    private int getWorkDaysExceptWeekend(Date start, Date end) {
        int workDays = 0;
        if (end.getTime() > start.getTime()) {
            Calendar calStar = Calendar.getInstance();
            calStar.setTime(start);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            while (calStar.compareTo(calEnd) <= 0) {
                if (calStar.get(Calendar.DAY_OF_WEEK) != 7 && calStar.get(Calendar.DAY_OF_WEEK) != 1) {
                    workDays++;
                }
                calStar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        //工作日表
        List<WorkingDay> workingDaysList = workingDayMapper.getWorkingday(ymd.format(start),ymd.format(end));
        int workingDayscount = workingDaysList.size();
        for (int i = 0; i < workingDaysList.size(); i++) {
            Date bdate = workingDaysList.get(i).getWorkingdate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                workingDayscount = workingDayscount - 1;
            }
        }
        //振替出勤日
        List<WorkingDay> workingDaysList1 = workingDaysList.stream().filter(p->(p.getType().equals("4"))).collect(Collectors.toList());

        return workDays - workingDayscount + workingDaysList1.size();
    }

    //获取退职出勤日-gbb
    private int getWorkDaysRetire(CustomerInfo.UserInfo userinfo) throws Exception{
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        //当月日期1号
        Calendar calNowOne = Calendar.getInstance();
        calNowOne.add(Calendar.MONTH, 0);
        calNowOne.set(Calendar.DAY_OF_MONTH, 1);
        //当月日期月末
        Calendar calNowLast = Calendar.getInstance();
        calNowLast.set(Calendar.DAY_OF_MONTH, calNowLast.getActualMaximum(Calendar.DAY_OF_MONTH));
        //退职日
        Calendar calResignationDate = Calendar.getInstance();
        if (!StringUtils.isEmpty(userinfo.getResignation_date())) {
            if (userinfo.getResignation_date().indexOf("Z") < 0) {
                userinfo.setResignation_date(formatStringDate(userinfo.getResignation_date().substring(0,10)));
            }
            calResignationDate.setTime(sf.parse(userinfo.getResignation_date().replace("Z", " UTC")));
        } else {
            calResignationDate.setTime(calNowLast.getTime());
        }

        Date temp = calResignationDate.getTime();
        if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
            temp = calNowLast.getTime();
        }
        Date start = ymd.parse(ymd.format(calNowOne.getTime()));
        Date end = ymd.parse(ymd.format(temp));
        //每月正常工作日
        int workDays = 0;
        //获取周末带薪假日
        int intDays = 0;
        //振替休日
        int workingDayscount = 0;
        //每月正常工作日
        if (end.getTime() > start.getTime()) {
            Calendar calStar = Calendar.getInstance();
            calStar.setTime(start);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            while (calStar.compareTo(calEnd) <= 0) {
                if (calStar.get(Calendar.DAY_OF_WEEK) != 7 && calStar.get(Calendar.DAY_OF_WEEK) != 1) {
                    workDays++;
                }
                calStar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        //工作日表
        List<WorkingDay> workingDaysList = workingDayMapper.getWorkingday(ymd.format(start),ymd.format(end));
        for (int i = 0; i < workingDaysList.size(); i++) {
            Date bdate = workingDaysList.get(i).getWorkingdate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            //获取带薪假日（打定节假日+振替出勤日+会社特别休日）周末带薪日
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                //法定日（周末）+振替出勤日
                if(workingDaysList.get(i).getType().equals("1") || workingDaysList.get(i).getType().equals("4")){
                    intDays = intDays + 1;
                }
            }
            //振替休日
            if(workingDaysList.get(i).getType().equals("3")){
                workingDayscount = workingDayscount + 1;
            }
        }
        //正常工作日 - 振替休日 + 法定日（周末）+振替出勤日
        return workDays - workingDayscount + intDays;
    }

    // region 入职和离职 BY Cash
    // 入职表插入数据
    public void insertInduction(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("入职");
        int rowundex = 1;
        List<Induction> inductionList = getInduction(givingid);
        if (inductionList.size() > 0) {
            for (Induction induction : inductionList) {
                induction.setInduction_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    induction.preInsert(tokenModel);
                } else {
                    induction.preInsert();
                }
                induction.setRowindex(rowundex);
                inductionMapper.insert(induction);
                rowundex++;
            }
        }
    }

    // 退职表插入数据
    public void insertRetire(String givingid, TokenModel tokenModel) throws Exception {
        System.out.println("退职");
        initial();
        int rowundex = 1;
        List<Retire> retireList = getRetire(givingid);
        if (retireList.size() > 0) {
            for (Retire retire : retireList) {
                retire.setRetire_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    retire.preInsert(tokenModel);
                } else {
                    retire.preInsert();
                }
                retire.setRowindex(rowundex);
                retireMapper.insert(retire);
                rowundex++;
            }
        }
    }

    // 入职
    public List<Induction> getInduction(String givingId) throws Exception {
        /*获取 customerInfos-lxx*/
        //initial();
        /*获取 customerInfos-lxx*/
        List<Induction> inductions = new ArrayList<>();
        // 今月日期
        Calendar thisMonthDate = Calendar.getInstance();
        // 上月日期
        Calendar lastMonthDate = Calendar.getInstance();
        lastMonthDate.add(Calendar.MONTH, -1);
        SimpleDateFormat sfUTC = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        SimpleDateFormat sfChina = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sfYM = new SimpleDateFormat("yyyy-MM");
        int lastDay = thisMonthDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        long mouthStart = sfChina.parse((thisMonthDate.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(thisMonthDate.getTime())) + "-01")).getTime();
        long mouthEnd = sfChina.parse((thisMonthDate.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(thisMonthDate.getTime())) + "-" + lastDay)).getTime();
        //add_fjl_0923  获取上月月初 start
        long lastmouthStart = sfChina.parse((lastMonthDate.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(lastMonthDate.getTime())) + "-01")).getTime();
        //add_fjl_0923  获取上月月初 end
        // 保留小数点后两位四舍五入
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        // 正社员工開始日
        //String staffStartDate = "";  add 20201010
        // 納付率表抽取相关数据
        // 一括补助_试用
        double trialSubsidy = 0d;
        // 一括补助_正式
        double officialSubsidy = 0d;
        List<Dictionary> subsidyList1 = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR042009"))).collect(Collectors.toList());
        List<Dictionary> subsidyList2 = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR042010"))).collect(Collectors.toList());
        if(subsidyList1.size() > 0){
            trialSubsidy = Double.parseDouble(subsidyList1.get(0).getValue2());
        }
        if(subsidyList2.size() > 0){
            officialSubsidy = Double.parseDouble(subsidyList2.get(0).getValue2());
        }
        // 试用期工资扣除比例
        double wageDeductionProportion = 0d;
        List<Dictionary> proportionList1 = dictionaryAll.stream().filter(item -> (item.getCode().equals("PR049010"))).collect(Collectors.toList());
        if(proportionList1.size() > 0){
            wageDeductionProportion = Double.parseDouble(proportionList1.get(0).getValue2());
        }
        // 抽取上月发工资的人员
        List<String> userids = wagesMapper.lastMonthWage(sfYM.format(lastMonthDate.getTime()),"");
        if (customerInfos.size() > 0) {
            for (CustomerInfo customerInfo : customerInfos) {
                // 退职日非空的情况（该员工的給料和补助在退职处理中计算）
                if (StringUtils.isNotEmpty(customerInfo.getUserinfo().getResignation_date())) {
                    continue;
                }
                // 试用期截止日不为空的情况
                // 转正日小于当月的除外
                Date endDate = new Date();
                //region  add_qhr_20210528  更改转正日计算形式
                Calendar rightNow = Calendar.getInstance();
                if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getEnddate())){
//                    if (customerInfo.getUserinfo().getEnddate().indexOf("Z") < 0) {
//                        customerInfo.getUserinfo().setEnddate(formatStringDate(customerInfo.getUserinfo().getEnddate()));
//                    }
//                    endDate = sfUTC.parse(customerInfo.getUserinfo().getEnddate().replace("Z", " UTC"));
                    String endddate = customerInfo.getUserinfo().getEnddate().substring(0, 10);
                    rightNow.setTime(Convert.toDate(endddate));
                    if (customerInfo.getUserinfo().getEnddate().length() >= 24) {
                        rightNow.add(Calendar.DAY_OF_YEAR, 2);
                    } else {
                        rightNow.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    endddate = sfChina.format(rightNow.getTime());
                    endDate = sfChina.parse(endddate);
                    //endregion  add_qhr_20210528  更改转正日计算形式
                    if (endDate.getTime() < mouthStart) {
                        continue;
                    }
                }
                // 正社员工開始日
                String staffStartDate = "";
                Induction induction = new Induction();
                induction.setGiving_id(givingId);
                // 上月工资结算时点过后入职没发工资的人（包含上月入职和本月入职的员工）
                if (!userids.contains(customerInfo.getUserid()) && userids.size() > 0) {
                    //add_fjl_0923  添加[入社年月日]是在上月与本月的条件 start
                    if (StringUtils.isNotEmpty(customerInfo.getUserinfo().getEnterday())) {
                        //入社年月日
                        if (customerInfo.getUserinfo().getEnterday().indexOf("Z") < 0) {
                            customerInfo.getUserinfo().setEnterday(formatStringDate(customerInfo.getUserinfo().getEnterday()));
                        }
                        Date enterDay = sfUTC.parse(customerInfo.getUserinfo().getEnterday().replace("Z", " UTC"));
                        //上月入职和本月入职的员工
                        if (enterDay.getTime() >= lastmouthStart && enterDay.getTime() <= mouthEnd) {
                            //add_fjl_0923  添加[入社年月日]是在上月与本月的条件 end
                            if (StringUtils.isNotEmpty(customerInfo.getUserinfo().getEnddate())) {
                                // 本月转正
                                if (endDate.getTime() >= mouthStart && endDate.getTime() <= mouthEnd) {
                                    // 正社员工開始日
                                    staffStartDate = endDate.toString();
                                    induction.setStartdate(endDate);
                                }
                            }
                            //上月入职
                            if (enterDay.getTime() < mouthStart) {//上月未发工资的人
                                // 计算給料和补助
                                calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df,0);
                            }
                            else{//当月入职
                                // 计算給料和补助
                                calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df,1);
                            }
                            inductions.add(induction);
                        }
                        //add_fjl_0923  添加[入社年月日]是在上月与本月的条件 start
                    }
                    //add_fjl_0923  添加[入社年月日]是在上月与本月的条件 end
                } else {
                    // 上月开了工资的员工
                    // 试用期截止日为空的情况
                    if (StringUtils.isEmpty(customerInfo.getUserinfo().getEnddate())) {
                        // 本月未转正
                        // 计算給料和补助
                        calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df,1);
                        inductions.add(induction);
                    } else {
                        // 本月转正
                        if (endDate.getTime() <= mouthEnd) {// 本月转正
                            // 正社员工開始日
                            staffStartDate = endDate.toString();
                            induction.setStartdate(endDate);
                            // 计算給料和补助
                            calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df,1);
                            inductions.add(induction);
                        } else {//本月之后转正
                            // 计算給料和补助
                            calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df,1);
                            inductions.add(induction);
                        }
                    }
                }
            }
        }
        return inductions;
    }

    // 计算給料和补助--只用于入职
    private void calculateSalaryAndSubsidy(Induction induction, CustomerInfo customerInfo, String staffStartDate, double trialSubsidy, double officialSubsidy,
                                           double wageDeductionProportion, SimpleDateFormat sf, DecimalFormat df,int intflg) throws Exception {
        // 用户ID
        induction.setUser_id(customerInfo.getUserid());
        // 工号
        induction.setJobnumber(customerInfo.getUserinfo().getJobnumber());

        Calendar calEnterday = Calendar.getInstance();
        Calendar calEnddate = Calendar.getInstance();
        String strEnterday = customerInfo.getUserinfo().getEnterday();

        //本月最后一天
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        String lastday = format.format(cale.getTime());

        // 入社日 客户导入日期是yy-mm-dd hh:mm:ss
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getEnterday())){
            if (customerInfo.getUserinfo().getEnterday().indexOf("Z") < 0) {
                strEnterday = formatStringDate(customerInfo.getUserinfo().getEnterday().substring(0,10));
                customerInfo.getUserinfo().setEnterday(formatStringDate(customerInfo.getUserinfo().getEnterday()));
            }
            induction.setWorddate(sf.parse(customerInfo.getUserinfo().getEnterday().replace("Z", " UTC")));
            calEnterday.setTime(sf.parse(customerInfo.getUserinfo().getEnterday().replace("Z", " UTC")));
        }
        //试用期截止日
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getEnddate())){
            if (customerInfo.getUserinfo().getEnddate().indexOf("Z") < 0) {
                customerInfo.getUserinfo().setEnddate(formatStringDate(customerInfo.getUserinfo().getEnddate()));
            }
            calEnddate.setTime(sf.parse(customerInfo.getUserinfo().getEnddate().replace("Z", " UTC")));
        }
        // 本月基本工资
        String thisMonthSalary = getSalary(customerInfo, 1);
        induction.setThismonth(thisMonthSalary);
        // 上月基本工资
        String lastMonthSalary = getSalary(customerInfo, 0);
        induction.setLastmonth(lastMonthSalary);
        // 计算出勤日数
        Map<String, String> daysList = suitAndDaysCalc(customerInfo.getUserinfo());
        // 本月正式工作日数
        double thisMonthDays = Double.parseDouble(daysList.get("thisMonthDays"));
        // 本月试用工作日数
        double thisMonthSuitDays = Double.parseDouble(daysList.get("thisMonthSuitDays"));
        // 上月正式工作日数
        double lastMonthDays = Double.parseDouble(daysList.get("lastMonthDays"));
        // 上月试用工作日数
        double lastMonthSuitDays = Double.parseDouble(daysList.get("lastMonthSuitDays"));
        // 上月出勤日数
        double lastAttendanceDays = lastMonthDays + lastMonthSuitDays;
        if(intflg == 0){
            induction.setAttendance(String.valueOf(lastAttendanceDays));
        }
        // 今月試用社員出勤日数
        thisMonthSuitDays = calculateAttendanceDays(thisMonthSuitDays);
        induction.setTrial(String.valueOf(thisMonthSuitDays));
        // 給料
        if (StringUtils.isNotEmpty(staffStartDate)) {//当月转正
            //没有试用期的情况
            if (calEnterday.get(Calendar.YEAR) == calEnddate.get(Calendar.YEAR)
                    && calEnterday.get(Calendar.MONTH) == calEnddate.get(Calendar.MONTH)
                    && calEnterday.get(Calendar.DATE) == calEnddate.get(Calendar.DATE) + 1) {
                induction.setGive(df.format((Double.parseDouble(thisMonthSalary) / dateBase * thisMonthDays)));
            }
            else{
                if(intflg == 1){
                    //试用期截止日为当月最后一天的话也算是当月试用，当月未转正
                    if((DateUtil.format(new Date(), "yyyyMM").equals(DateUtil.format(induction.getStartdate(), "yyyyMM"))
                            && DateUtil.format(induction.getWorddate(), "dd").equals(lastday))){
                        // 今月試用社員出勤日数
                        thisMonthSuitDays = 0d;
                        induction.setTrial("");
                        induction.setGive(df.format(Double.parseDouble(thisMonthSalary)));
                    }
                    else{
                        induction.setGive(df.format(Double.parseDouble(thisMonthSalary) - (Double.parseDouble(thisMonthSalary) / dateBase * thisMonthSuitDays * wageDeductionProportion)));
                    }
                }
                else{//上月入职未发工资当月转正
                    induction.setGive(df.format(Double.parseDouble(lastMonthSalary) / dateBase * lastAttendanceDays + Double.parseDouble(thisMonthSalary)));
                }
            }
        } else {//本月之后转正
            strEnterday  =formatStringDates(strEnterday);
            strEnterday = strEnterday.replace("/","").replace("-","").substring(0,6);
            if(!DateUtil.format(new Date(), "yyyyMM").equals(strEnterday)){//非当月入社的人员
                // 今月試用社員出勤日数
                thisMonthSuitDays = 0d;
                induction.setTrial("");
            }
            if(intflg == 0){//上月未发工资的人
                //上月工资 / 21.75 * 上月出勤天数 + 本月工资
                induction.setGive(df.format(Double.parseDouble(lastMonthSalary) / dateBase * lastAttendanceDays + Double.parseDouble(thisMonthSalary)));
            }
            else{
                if (thisMonthSuitDays > 0) {//当月入职的人
                    induction.setGive(df.format(Double.parseDouble(thisMonthSalary) / dateBase * thisMonthSuitDays));
                } else {//当月之前入职的
                    induction.setGive(thisMonthSalary);
                }
            }
        }
        // 一括补助
        //本月转正判断
        if(induction.getStartdate() != null){//当月转正
            //没有试用期的情况
            if (calEnterday.get(Calendar.YEAR) == calEnddate.get(Calendar.YEAR)
                    && calEnterday.get(Calendar.MONTH) == calEnddate.get(Calendar.MONTH)
                    && calEnterday.get(Calendar.DATE) == calEnddate.get(Calendar.DATE) + 1) {//无试用期的情况
                induction.setLunch(df.format(officialSubsidy / 21.75 * thisMonthDays));
            }
            else if (DateUtil.format(new Date(), "yyyy").equals(String.valueOf(calEnddate.get(Calendar.YEAR)))
                && DateUtil.format(new Date(), "M").equals(String.valueOf(calEnddate.get(Calendar.MONTH) + 1))
                && lastday.equals(String.valueOf(calEnddate.get(Calendar.DATE)))) {//试用期500
                    //calEnddate.get(Calendar.MONTH) 取出的月份月份少一个月
                    induction.setLunch(df.format(trialSubsidy));
            }
            else{
                //ROUND(1000-500/21.75*I3,2)//本月转正有试用期的
                induction.setLunch(df.format(officialSubsidy - trialSubsidy / 21.75 * thisMonthSuitDays));
            }

        }
        else{//当月未转正的人员
            if(induction.getTrial().equals("")){//本月之前开始试用并且本月未转正的（今月试用出勤天数为0）
                if(intflg == 0){//上月发工资之后入职
                    //500 + 500 / 21.75 * 先月出勤日数
                    induction.setLunch(df.format(trialSubsidy + trialSubsidy / dateBase * lastAttendanceDays));
                }
                else{//本月之前之前开始试用
                    //上月试用工作日数 / 21.75 * 500 + 上月正式工作日数 / 21.75 * 1000 + 今月試用社員出勤日数 / 21.75 * 500 + 本月正式工作日数 / 21.75 * 1000
                    induction.setLunch(df.format(trialSubsidy));
                }
            }
            else{//本月入职
                //500 / 21.75 * 今月試用社員出勤日数
                induction.setLunch(df.format(trialSubsidy / dateBase * thisMonthSuitDays));
                //上月试用工作日数 / 21.75 * 500 + 上月正式工作日数 / 21.75 * 1000 + 今月試用社員出勤日数 / 21.75 * 500 + 本月正式工作日数 / 21.75 * 1000
//                    induction.setLunch(df.format(lastMonthSuitDays / dateBase * trialSubsidy + lastMonthDays / dateBase * officialSubsidy +
//                            thisMonthSuitDays / dateBase * trialSubsidy + thisMonthDays / dateBase * officialSubsidy));
            }
        }
    }

    //退职
    public List<Retire> getRetire(String givingId) throws Exception {
        List<Retire> retires = new ArrayList<>();
        Query query = new Query();
        // 国际标准时间格式化
        SimpleDateFormat sfUTC = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        // 东八区（中国）时间格式化
        SimpleDateFormat sfChina = new SimpleDateFormat("yyyy-MM-dd");
        // 保留小数点后两位四舍五入
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        // 上个月
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);
        // 上个月最后一日
        int lastMonthLastDay = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 本月
        Calendar thisMonth = Calendar.getInstance();
        // 本月最后一日
        int thisMonthLastDay = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 納付率表抽取相关数据
        Dictionary dictionary = new Dictionary();
        dictionary.setPcode("PR042");
        List<Dictionary> subsidyList = dictionaryMapper.select(dictionary);
        // 一括补助_试用
        double trialSubsidy = 0d;
        // 一括补助_正式
        double officialSubsidy = 0d;
        for (Dictionary diction : subsidyList) {
            if (diction.getCode().equals("PR042009")) {
                trialSubsidy = Double.parseDouble(diction.getValue2());
            } else if (diction.getCode().equals("PR042010")) {
                officialSubsidy = Double.parseDouble(diction.getValue2());
            }
        }
        // 查询退职人员信息（本月退职人员为对象）
        Criteria criteria = Criteria.where("userinfo.resignation_date")
                .gte(lastMonth.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(lastMonth.getTime())) + "-" + lastMonthLastDay)
                .lte(thisMonth.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(thisMonth.getTime())) + "-" + thisMonthLastDay)
                .and("status").is("0");
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        if (customerInfos.size() > 0) {
            for (CustomerInfo customerInfo : customerInfos) {

                //转正日
                if (StringUtils.isEmpty(customerInfo.getUserinfo().getEnddate())) {
                    if (customerInfo.getUserinfo().getEnddate().indexOf("Z")  != -1) {
                        String enddate = customerInfo.getUserinfo().getEnddate().substring(0, 10).replace("-", "/");
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(sdfYMD.parse(enddate));//设置起时间
                        cal1.add(Calendar.DATE, +1);
                        customerInfo.getUserinfo().setEnddate(sdfYMD.format(cal1.getTime()));
                    }
                }
                int intNewDate = Integer.parseInt(sdfYM.format(new Date()));
                int intEnddate = Integer.parseInt(customerInfo.getUserinfo().getEnddate().replace("/","").replace("-","").substring(0,6));
                Retire retire = new Retire();
                retire.setGiving_id(givingId);
                // 用户名字
                retire.setUser_id(customerInfo.getUserid());
                // 工号
                retire.setJobnumber(customerInfo.getUserinfo().getJobnumber());
                // 当月基本工资
                String thisMonthSalary = getSalary(customerInfo, 1);
                double attendanceDays = calculateAttendanceDays(getWorkDaysRetire(customerInfo.getUserinfo()));
                // 本月出勤日数
                retire.setAttendance(String.valueOf(attendanceDays));
                // 給料
                retire.setGive(df.format(Double.parseDouble(thisMonthSalary) / dateBase * attendanceDays));
                // 一括补助
                if(intEnddate > intNewDate){//试用期
                    retire.setLunch(df.format(trialSubsidy / dateBase * attendanceDays));
                }
                else{//正式
                    retire.setLunch(df.format(officialSubsidy / dateBase * attendanceDays));
                }
                //insert gbb NT_PFANS_20210222_BUG_024 退职人员结算年休  start
                SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
                if (customerInfo.getUserinfo().getResignation_date().indexOf(strDate) != -1) {
                    Date temp = st.parse(customerInfo.getUserinfo().getResignation_date());
                    Calendar cld = Calendar.getInstance();
                    cld.setTime(temp);
                    cld.add(Calendar.DATE, 1);
                    temp = cld.getTime();
                    //获得下一天日期字符串
                    retire.setRetiredate(sdfYMD1.parse(sdfYMD1.format(temp)));
                }
                else{
                    retire.setRetiredate(st.parse(customerInfo.getUserinfo().getResignation_date()));
                }
                Calendar calendar = Calendar.getInstance();
                int year = 0;
                int month = calendar.get(Calendar.MONTH)+1;
                if(month >= 1 && month <= 3) {
                    year = calendar.get(Calendar.YEAR) - 1;
                }else {
                    year = calendar.get(Calendar.YEAR);
                }
                //4月份计算工资此处无需计算，工资详情中的最终工资会集中体现
                if(month != 4){
                    String remaning = "0";
                    //离职剩余年休天数
                    remaning = annualLeaveService.remainingAnnual(customerInfo.getUserid(),String.valueOf(year));
//                    //离职总剩余年修
//                    BigDecimal quitCount = new BigDecimal(remaning);
//                    //上年度剩余年修
//                    List<AnnualLeave> userAn = thisyearsList.stream().filter(item -> (item.getUser_id().equals(customerInfo.getUserid()))).collect(Collectors.toList());
//                    if(userAn.size() > 0){
//                        quitCount = new BigDecimal(remaning).add(userAn.get(0).getRemaining_annual_leave_lastyear());
//                    }
                    //离职年修金额
                    BigDecimal bigquitCount = new BigDecimal(Double.parseDouble(thisMonthSalary) / dateBase * 2 * Double.parseDouble(remaning)).setScale(2, RoundingMode.HALF_UP);
                    //离职其他金额
                    BigDecimal bigGive = new BigDecimal(retire.getGive());
                    //离职总金额
                    double strannualleavegive = bigquitCount.add(bigGive).doubleValue();
                    ///剩余年休
                    retire.setAnnualleave(bigquitCount.toString());
                    //年休结算
                    retire.setAnnualleavegive(bigquitCount.toString());
                    //给料
                    retire.setGive(df.format(strannualleavegive));
                }
                //region add gbb 20210408 退职人员剩余年休查询 end
                //insert gbb NT_PFANS_20210222_BUG_024 退职人员结算年休  end
                retires.add(retire);
            }
        }
        return retires;
    }

    // 计算出勤天数
    private Double calculateAttendanceDays(double attendanceDays) throws Exception{
        double rtnAttendanceDays;
        Calendar calAttendanceStart = Calendar.getInstance();
        calAttendanceStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calAttendanceEnd = Calendar.getInstance();
        calAttendanceEnd.set(Calendar.DAY_OF_MONTH, calAttendanceEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        // 本月应当的全勤日数
        int allAttendanceDay = getTrialWorkDaysExceptWeekend(calAttendanceStart.getTime(), calAttendanceEnd.getTime());
        // 本月出勤日数与工作日基数（21.75）比较
        int compareResult = Double.compare(attendanceDays, dateBase);
        // 本月出勤日数大于等于工作日基数
        if (compareResult >= 0) {
            // 本月出勤日数设置为工作日基数
            rtnAttendanceDays = dateBase;
        } else {
            // 本月出勤日数等于本月应当的全勤日数
            if (Double.compare(attendanceDays, Double.parseDouble(String.valueOf(allAttendanceDay))) == 0) {
                // 本月出勤日数设置为工作日基数
                rtnAttendanceDays = dateBase;
            } else {
                // 本月出勤日数设置为实际工作日数
                rtnAttendanceDays = attendanceDays;
            }
        }
        return rtnAttendanceDays;
    }
    // endregion 入职和离职 BY Cash

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    private String formatStringDate(String date) {
        date = date.replace("/", "-");
        date = date.concat("T00:00:00.000Z");
        return date;
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    private String formatStringDateadd(String date) {
        if (date.indexOf("Z") > 0) {
            date = date.replace("-", "/").substring(0, 10);
            Calendar rightNow = Calendar.getInstance();
            SimpleDateFormat ymd = new SimpleDateFormat("yyyy/MM/dd");
            rightNow.setTime(Convert.toDate(date));
            rightNow.add(Calendar.DAY_OF_YEAR, 1);
            date = ymd.format(rightNow.getTime());
        }
        return date;
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    private String formatStringDates(String date) {
        if (date.indexOf("Z") > 0) {
            date = date.replace("-", "/").substring(0, 10);
            Calendar rightNow = Calendar.getInstance();
            SimpleDateFormat ymd = new SimpleDateFormat("yyyy/MM/dd");
            rightNow.setTime(Convert.toDate(date));
            date = ymd.format(rightNow.getTime());
        }
        return date;
    }

    /**
     * 修改发放状态 gbb 0722 add
     *
     * @param givingid
     * @return
     */
    @Override
    public void updatestate(String givingid, String generationdate,TokenModel tokenModel) throws Exception {
        //工资表
        wagesMapper.updateWages(givingid,tokenModel.getUserId());

        //給与計算表
        Giving giving = new Giving();
        giving.setGiving_id(givingid);
        giving.setGrantstatus("1");
        giving.preUpdate(tokenModel);
        givingMapper.updateByPrimaryKeySelective(giving);

        //祝礼金申请
        casgiftApplyMapper.updpayment(generationdate.substring(0,7),tokenModel.getUserId());
        // add gbb 20210416 4月份工资发放之后清空上一年度剩余年休 start
        if(generationdate.substring(5,7).equals("04")){
            annualLeaveMapper.updateremaining_annual_leave_lastyear(generationdate.substring(0,4));
        }
        // add gbb 20210416 4月份工资发放之后清空上一年度剩余年休 end
    }

    /**
     * insert gbb 0808 生成工资是check考勤（1：上月未审批通过的；2：当月离职人员为审批通过的）
     *
     * @param tokenModel
     * @return
     */
    @Override
    public Integer attendancecheck(TokenModel tokenModel) throws Exception {
        Query query = new Query();
        List<String> userIdList = new ArrayList<String>();
        // 东八区（中国）时间格式化
        SimpleDateFormat sfChina = new SimpleDateFormat("yyyy-MM-dd");
        // 上个月
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);
        // 上个月最后一日
        int lastMonthLastDay = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 本月
        Calendar thisMonth = Calendar.getInstance();
        // 本月最后一日
        int thisMonthLastDay = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 查询退职人员信息（本月退职人员为对象）
        Criteria criteria = Criteria.where("userinfo.resignation_date")
                .gte(thisMonth.get(Calendar.YEAR)  + "-" + getMouth(sfChina.format(lastMonth.getTime())) + "-" + '1')
                .lte(thisMonth.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(lastMonth.getTime())) + "-" + thisMonthLastDay)
                .and("status").is("0");
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        if (customerInfos.size() > 0) {
            for (CustomerInfo customerInfo : customerInfos) {
                userIdList.add(customerInfo.getUserid());
            }
        }
        //add gbb 0805 生成工资时check考试数据(上月所有员工)
        int RetireCount = givingMapper.getAttendanceRetireCount(userIdList);
        //add gbb 0805 生成工资时check考试数据(上月所有员工)
        int AttendanceStatus = givingMapper.getAttendanceStatus();
        return RetireCount + AttendanceStatus;
    }
}
