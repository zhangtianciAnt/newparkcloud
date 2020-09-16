package com.nt.service_pfans.PFANS2000.Impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.*;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class WagesServiceImpl implements WagesService {

    @Autowired
    private WagesMapper wagesMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private InductionMapper inductionMapper;
    @Autowired
    private RetireMapper retireMapper;
    @Autowired
    private GivingMapper givingMapper;
    @Autowired
    private BonussendMapper bonussendMapper;
    @Autowired
    private BaseMapper baseMapper;
    @Autowired
    private ContrastMapper contrastMapper;
    @Autowired
    private OtherOneMapper otherOneMapper;
    @Autowired
    private DutyfreeMapper dutyfreeMapper;
    @Autowired
    private DisciplinaryMapper disciplinaryMapper;
    @Autowired
    private AccumulatedTaxMapper accumulatedTaxMapper;
    @Autowired
    private ComprehensiveMapper comprehensiveMapper;
    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

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
    private WorkingDayMapper workingDayMapper;

    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;


    private static List<CustomerInfo> customerInfos;
    private static List<Wages> lastwages;

    private static String userid;

    // 日期基数
    private static final double dateBase = 21.75;

    @Override
    public List<Wages> select(TokenModel tokenModel) {
        Query query = new Query();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.add(Calendar.MONTH, -1);
        now.add(Calendar.MONTH, -2);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR));
        now.set(Calendar.MONTH, now.get(Calendar.MONTH));
        int lastDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        Criteria criteria = Criteria.where("userinfo.resignation_date")
                .gte(now.get(Calendar.YEAR) + "-" + getMonth(sf.format(now.getTime())) + "-" + lastDay)
                .lte(cal.get(Calendar.YEAR) + "-" + getMonth(sf.format(cal.getTime())) + "-01");
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfo = mongoTemplate.find(query, CustomerInfo.class);
        if (customerInfo.size() > 0) {
            List<String> list = new ArrayList<>();
            customerInfo.forEach(customerInfo1 -> list.add(customerInfo1.getUserid()));
            List<Retire> retires = retireMapper.selectRetire(list, last.get(Calendar.YEAR) + "" + getMonth(sf.format(last.getTime())), getMonth(sf.format(last.getTime())) + "");
            if (retires.size() > 0) {
                retires.forEach(retire -> {
                    Optional<CustomerInfo> _customerInfo = customerInfo.stream().filter(a -> (retire.getUser_id()).equals(a.getUserid())).findFirst();
                    if (_customerInfo.isPresent()) {
                        String thisMouth = "0";
                        try {
                            if (_customerInfo.get().getUserinfo().getResignation_date() != null && !_customerInfo.get().getUserinfo().getResignation_date().equals("")) {
                                retire.setRetiredate(sf.parse(_customerInfo.get().getUserinfo().getResignation_date()));
                            }
                            if (_customerInfo.get().getUserinfo().getGridData() != null) {
                                List<CustomerInfo.Personal> personals = _customerInfo.get().getUserinfo().getGridData().stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed())
                                        .collect(Collectors.toList());

                                for (CustomerInfo.Personal personal : personals) {
                                    if (sf.parse(personal.getDate()).getTime() <= sf.parse((last.get(Calendar.YEAR) + "-" + getMonth(sf.format(last.getTime())) + "-01")).getTime()) {
                                        // UPD_GBB_2020/05/20
                                        //thisMouth = personal.getAfter();
                                        thisMouth = personal.getBasic();
                                        // UPD_GBB_2020/05/20
                                        return;
                                    }
                                }
                            }
                            DecimalFormat df = new DecimalFormat("#.00");
                            //IF(今月出勤日数="全月",ROUND(基数.当月基本工资,2),IF(今月出勤日数<>"",ROUND(基数.当月基本工资/21.75*今月出勤日数,2),0))
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMonth(sf.format(last.getTime()))))) {
                                retire.setGive(df.format(Double.valueOf(thisMouth)));
                            } else if (Integer.parseInt(retire.getAttendance()) != 0) {
                                retire.setGive(df.format(Double.valueOf(df.format(Double.valueOf(thisMouth) / 21.75 * Integer.parseInt(retire.getAttendance())))));
                            } else {
                                retire.setGive("0");
                            }
                            //IF(今月出勤日数="全月",纳付率.食堂手当,ROUND(纳付率.食堂手当/21.75*今月出勤日数,2)
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMonth(sf.format(last.getTime()))))) {
                                retire.setLunch("105");
                            } else {
                                retire.setLunch(df.format(105 / 21.75 * Integer.parseInt(retire.getAttendance())));
                            }
                            //IF(今月出勤日数="全月",纳付率.交通手当,ROUND(纳付率.交通手当/21.75*今月出勤日数,2))
                            if (Integer.parseInt(retire.getAttendance()) == getWorkDays(last.get(Calendar.YEAR), Integer.parseInt(getMonth(sf.format(last.getTime()))))) {
                                retire.setLunch("84");
                            } else {
                                retire.setLunch(df.format(84 / 21.75 * Integer.parseInt(retire.getAttendance())));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        return null;
    }

    public String getMonth(String mouth) {
        String _mouth = mouth.substring(5, 7);
        if (_mouth.substring(0, 1) == "0") {
            return _mouth.substring(1);
        }
        return _mouth;
    }

    private static int getWorkDays(int theYear, int theMonth) {
        // 计算指定月有多少工作日
        int workDays = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(theYear, theMonth - 1, 1);// 从每月1号开始
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                workDays++;
            }
            cal.add(Calendar.DATE, 1);
        }
        return workDays;
    }

    @Override
    public List<Wages> wagesList(Wages wages) throws Exception {
        //已经发放的工资
        wages.setGrantstatus("1");
        wages.setActual("0");
        List<Wages> listw = wagesMapper.select(wages);
        for (Wages wages1 : listw) {
            if (wages1.getGiving_id() != null) {
                Giving giving = new Giving();
                giving.setGiving_id(wages1.getGiving_id());
                List<Giving> givingList = givingMapper.select(giving);
                for (Giving giving1 : givingList) {
                    if (wages1.getGiving_id().equals(giving1.getGiving_id())) {
                        wages1.setGiving_id(giving1.getMonths());
                    }
                }

            }
        }
        return listw;
    }

    @Override
    public List<BaseVo> selectBase(String dates) throws Exception {
        return givingMapper.selectBase(dates);
    }

    @Override
    public List<Bonussend> bonusList(Bonussend bonussend) throws Exception {

        return bonussendMapper.select(bonussend);
    }

    @Override
    public List<Wages> getWagesByGivingId(String givingId) throws Exception {
        return wagesMapper.getWagesByGivingId(givingId,"");
    }
    //获取工资公司集计
    @Override
    public WagesVo getWagesdepartment(String dates) throws Exception {
        WagesVo wagesvo = new WagesVo();
        List<Wages> wagesList = wagesMapper.getWagesdepartment(dates);
        //预计
        wagesvo.setWagesListestimate(wagesList.stream().filter(coi -> (coi.getActual().contains("0"))).collect(Collectors.toList()));
        //实际
        wagesvo.setWagesListactual(wagesList.stream().filter(coi -> (coi.getActual().contains("1"))).collect(Collectors.toList()));
        //比较
        wagesvo.setWagesListdiff(wagesList.stream().filter(coi -> (coi.getActual().contains("2"))).collect(Collectors.toList()));
        return wagesvo;
    }

    @Override
    public WagesVo getWagecompany() throws Exception {
        WagesVo wagesvo = new WagesVo();
        List<Wages> wagesList = wagesMapper.getWagecompany();
        //预计
        wagesvo.setWagesListestimate(wagesList.stream().filter(coi -> (coi.getActual().contains("0"))).collect(Collectors.toList()));
        //实际
        wagesvo.setWagesListactual(wagesList.stream().filter(coi -> (coi.getActual().contains("1"))).collect(Collectors.toList()));
        //比较
        wagesvo.setWagesListdiff(wagesList.stream().filter(coi -> (coi.getActual().contains("2"))).collect(Collectors.toList()));
        return wagesvo;
    }

    @Override
    public void insertWages(List<Wages> wages, TokenModel tokenModel) throws Exception {
        String status = wages.get(0).getStatus();
        String actual = wages.get(0).getActual();

//        Query query = CustmizeQuery(new OrgTree());
//        OrgTree orgTree = mongoTemplate.findOne(query, OrgTree.class);
//        List<OrgTree> orgTreeList = new ArrayList<>();
//        List<OrgTree>  orgTrees =  orgTree.getOrgs();
//        for (OrgTree org:
//                orgTrees ) {
//            orgTreeList.addAll(org.getOrgs());
//        }

        Query query = new Query();
        List<CustomerInfo> customerInfoList = mongoTemplate.find(query,CustomerInfo.class);
        if(status.equals("0") || status.equals("2")){
            // 先删除当月数据，再插入
            Wages del_wage = new Wages();
            del_wage.setGiving_id(wages.get(0).getGiving_id());
            wagesMapper.delete(del_wage);
            for (Wages wage : wages) {
                wage.setWages_id(UUID.randomUUID().toString());
                wage.setCreateonym(DateUtil.format(new Date(), "YYYY-MM"));
                wage.setActual(actual);
                if(actual.equals("1")){
                    List<CustomerInfo> customerinfo = customerInfoList.stream().filter(coi -> (coi.getUserid().contains(wage.getUser_id()))).collect(Collectors.toList());
                    if(customerinfo.size() > 0){
                        wage.setDepartment_id(customerinfo.get(0).getUserinfo().getBudgetunit());
                    }
                }
                wage.preInsert(tokenModel);
            }
            wagesMapper.insertListAllCols(wages);
        }
        // add 0723 工资计算添加审批 start
        if(!status.equals("0")){
            Giving giving = new Giving();
            giving.setGiving_id(wages.get(0).getGiving_id());
            giving.setStatus(status);
            if(status.equals("X")){
                giving.setStatus("0");
            }
            giving.preUpdate(tokenModel);
            givingMapper.updateByPrimaryKeySelective(giving);
        }
        // add 0723 工资计算添加审批 start
    }

    //获取离职人员工资
    @Override
    public List<Wages> getWagesByResign(String user_id,TokenModel tokenModel) throws Exception {
        userid = user_id;
        // 生成工资单的时候重新调用init方法获取最新的人员信息 By Skaixx
        init();
        List<Wages> wagesList = new ArrayList<>();
        Wages wages = new Wages();
        wages.setUser_id(user_id);
        wages.setCreateonym(DateUtil.format(new Date(),"yyyy-MM"));
        wages.setActual("0");
        //工资表数据
        List<Wages> wagesListEstimate = wagesMapper.select(wages);
        if (wagesListEstimate.size() > 0) {
            //预计工资
            wagesList.add(wagesListEstimate.get(0));
        } else {
            //预计工资
            wagesList.add(getwages("1",tokenModel));
        }

        //实际工資
        wagesList.add(getwages("2",tokenModel));
        return wagesList;
    }

    @Override
    public Wages getwages(String strFlg, TokenModel tokenModel) throws Exception {
        Wages wages = new Wages();
        //获取上月工资
        lastwages = wagesMapper.lastWages(Integer.parseInt(DateUtil.format(new Date(), "yyyy")), Integer.parseInt(DateUtil.format(new Date(), "M")) - 1,userid);
        // 时间格式
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
        String strTemp = sf1.format(new Date());
        // 查询当前月份的giving
        Giving giving = new Giving();
        giving.setMonths(strTemp);
        giving.setUser_id(userid);
        giving.setActual("0");
        List<Giving> givinglist = givingMapper.select(giving);
        // 如果存在当月数据（删除）
        deletewages(strTemp,givinglist);
        // 创建giving表数据
        String givingid = UUID.randomUUID().toString();
        giving = new Giving();
        if (tokenModel != null) {
            giving.preInsert(tokenModel);
        } else {
            giving.preInsert();
        }
        giving.setGiving_id(givingid);
        giving.setGeneration("0");
        giving.setUser_id(userid);
        giving.setGenerationdate(new Date());
        giving.setMonths(strTemp);
        giving.setActual("0");
        givingMapper.insert(giving);
        /*
            插入其他相关表数据
         */
        // 2020/03/11 add by myt start
        insertInduction(givingid, tokenModel);
        insertRetire(givingid, tokenModel);
        insertBase(givingid, tokenModel);
        insertContrast(givingid, tokenModel);
        insertOtherTwo(givingid, tokenModel);
        insertOtherOne(givingid, tokenModel);
        insertLackattendance(strFlg,givingid, tokenModel);
        insertResidual(strFlg,givingid, tokenModel);

        List<Wages> wagesList = wagesMapper.getWagesByGivingId(givingid,userid);
        if(wagesList.size() > 0){
            wages = wagesList.get(0);
        }
        deletewages(strTemp,givinglist);
        return wages;
    }

    @PostConstruct
    public void init() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_MONTH, 1);
        Query query = new Query();
        query.addCriteria(Criteria.where("userid").is(userid));
        customerInfos = mongoTemplate.find(query, CustomerInfo.class);
    }

    public void deletewages(String strTemp,List<Giving> givinglist) {
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
        Giving giving = new Giving();
        giving.setMonths(strTemp);
        giving.setUser_id(userid);
        givingMapper.delete(giving);
    }

    @Override
    public void insertOtherOne(String givingid, TokenModel tokenModel) throws Exception {
        List<OtherOne> otherOnes = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.00");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long endMillisecond = format.parse("2012-08-31").getTime();
        long otherOneTime = format.parse("2012-03-31").getTime();
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
        if (abNormalinfo.size() > 0) {
            int rowindex = 0;
            int rowindexMan = 0;
            for (AbNormal abNor : abNormalinfo) {
                if (abNor.getErrortype().equals("PR013012") || abNor.getErrortype().equals("PR013013")) {
                    boolean bool = true;

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

                    List<CustomerInfo> cust = customerInfos.stream().filter(customerInfo -> customerInfo.getUserid().equals(abNor.getUser_id())).collect(Collectors.toList());

                    if (cust.size() > 0) {
                        otherOne.setDepartment_id(cust.get(0).getUserinfo().getCenterid()); //部门
                        otherOne.setSex(cust.get(0).getUserinfo().getSex()); //性别
                        otherOne.setWorkdate(cust.get(0).getUserinfo().getEnterday()); //入职日
                        beginTime = cust.get(0).getUserinfo().getEnterday();
                    }
                    if (abNor.getErrortype().equals("PR013012")) {
                        rowindex = rowindex + 1;
                        otherOne.setRowindex(rowindex);
                        otherOne.setReststart(abNor.getOccurrencedate());
                        otherOne.setRestend(abNor.getFinisheddate());
                        Integer days = getDaysforOtherOne(abNor.getOccurrencedate(), abNor.getFinisheddate());
                        otherOne.setAttendance(days.toString());
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
                        Integer days = getDaysforOtherOne(abNor.getOccurrencedate(), abNor.getFinisheddate());
                        otherOne.setVacation(days.toString());
                        double intLengthtime = Double.valueOf(abNor.getLengthtime()) / 8;
                        String strLengthtime = String.valueOf(intLengthtime);
                        long beginMillisecond = notNull(beginTime).equals("0") ? (long) 0 : format.parse(beginTime.replace("/", "-")).getTime();
                        if (beginMillisecond >= endMillisecond) {
                            otherOne.setHandsupport(days.toString());
                        } else {
                            otherOne.setHandsupport("0");
                        }
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
        List<Base> bases = new ArrayList<>();
        Dictionary dictionary = new Dictionary();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
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
                //奨金計上
                base.setBonus(customer.getUserinfo().getDifference());
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
                /*基本工资 -> 月工资  月工资拆分为 基本工资  职责工资 -lxx*/
                //上月工资
                base.setLastmonthbasic(getSalaryBasicAndDuty(customer, 0).get("thisMonthBasic"));
                base.setLastmonthduty(getSalaryBasicAndDuty(customer, 0).get("thisMonthDuty"));
                //本月工资
                base.setThismonthbasic(getSalaryBasicAndDuty(customer, 1).get("thisMonthBasic"));
                base.setThismonthduty(getSalaryBasicAndDuty(customer, 1).get("thisMonthDuty"));
                //N月前基数-N根据字典获取 PR061001
                Dictionary dictionaryPr = new Dictionary();
                dictionaryPr.setCode("PR061001");
                Dictionary dicResult = dictionaryMapper.selectByPrimaryKey(dictionaryPr);
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
                base.setWorkdate(customer.getUserinfo().getEnterday());
                base.setRowindex(rowindex);
                /*group id -lxx*/
                base.setGroupid(customer.getUserinfo().getGroupid());
                /*group id -lxx*/
                /*试用期截止日 -lxx*/
                //add-ws-9/15-禅道任务525
                if (customer.getUserinfo().getEnddate().indexOf("Z")  != -1) {
                    String enddate = customer.getUserinfo().getEnddate().substring(0, 10).replace("-", "/");
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(sf.parse(enddate));//设置起时间
                    cal1.add(Calendar.DATE, +1);
                    customer.getUserinfo().setEnddate(sf.format(cal1.getTime()));
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
     * @Description 试用正式天数计算
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
        calNowOne.add(Calendar.MONTH, 0);
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
                thisMonthSuitDays = getWorkDaysExceptWeekend(calEnterDay.getTime(), temp);
                //本月正式天数
                thisMonthDays = 0;
            }
            //入职日是上月
            else if (calLast.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calLast.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                //上月试用天数
                lastMonthSuitDays = getWorkDaysExceptWeekend(calEnterDay.getTime(), calLast.getTime());
                //上月正式天数
                lastMonthDays = 0;
                //本月试用天数
                //本月末日/退职日期 取小值
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthSuitDays = getWorkDaysExceptWeekend(calNowOne.getTime(), temp);
                //本月正式天数
                thisMonthDays = 0;
            }
            //其他
            else {
                //上月试用天数
                lastMonthSuitDays = getWorkDaysExceptWeekend(calLastOne.getTime(), calLast.getTime());
                //上月正式天数
                lastMonthDays = 0;
                //本月试用天数
                //本月末日/退职日期 取小值
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthSuitDays = getWorkDaysExceptWeekend(calNowOne.getTime(), temp);
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
            calSuitDate.add(Calendar.DATE, -1);
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
                    thisMonthSuitDays = getWorkDaysExceptWeekend(calEnterDay.getTime(), temp);
                    //本月正式天数
                    thisMonthDays = 0;
                }
                //入职日是上月
                else if (calLast.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calLast.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                    //上月试用天数
                    lastMonthSuitDays = getWorkDaysExceptWeekend(calEnterDay.getTime(), calLast.getTime());
                    //上月正式天数
                    lastMonthDays = 0;
                    //本月试用天数
                    //本月末日/退职日期 取小值
                    Date temp = calResignationDate.getTime();
                    if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                        temp = calNowLast.getTime();
                    }
                    thisMonthSuitDays = getWorkDaysExceptWeekend(calNowOne.getTime(), temp);
                    //本月正式天数
                    thisMonthDays = 0;
                }
                //其他
                else {
                    //上月试用天数
                    lastMonthSuitDays = getWorkDaysExceptWeekend(calLastOne.getTime(), calLast.getTime());
                    //上月正式天数
                    lastMonthDays = 0;
                    //本月试用天数
                    //本月末日/退职日期 取小值
                    Date temp = calResignationDate.getTime();
                    if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                        temp = calNowLast.getTime();
                    }
                    thisMonthSuitDays = getWorkDaysExceptWeekend(calNowOne.getTime(), temp);
                    //本月正式天数
                    thisMonthDays = 0;
                }
            }
            //试用截止日小于上月月初日
            else if (calOfficialDate.getTime().getTime() < calLastOne.getTime().getTime()) {
                //上月试用天数
                lastMonthSuitDays = 0;
                //上月正式天数
                lastMonthDays = getWorkDaysExceptWeekend(calLastOne.getTime(), calLast.getTime());
                //本月试用天数
                thisMonthSuitDays = 0;
                //本月正式天数
                //本月末日/退职日期 取小值
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthDays = getWorkDaysExceptWeekend(calNowOne.getTime(), temp);
            }
            //试用截止日是上月
            else if (calLast.get(Calendar.YEAR) == calOfficialDate.get(Calendar.YEAR) && calLast.get(Calendar.MONTH) == calOfficialDate.get(Calendar.MONTH)) {
                //上月试用天数
                //上月1日/入职日期 取大值
                Date tempStart = calLastOne.getTime();
                if (calEnterDay.getTime().getTime() > calLastOne.getTime().getTime()) {
                    tempStart = calEnterDay.getTime();
                }
                lastMonthSuitDays = getWorkDaysExceptWeekend(tempStart, calSuitDate.getTime());
                //上月正式天数
                lastMonthDays = getWorkDaysExceptWeekend(calOfficialDate.getTime(), calLast.getTime());
                //本月试用天数
                thisMonthSuitDays = 0;
                //本月正式天数
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthDays = getWorkDaysExceptWeekend(calNowOne.getTime(), temp);
            }
            //试用截止日是本月
            else if (calNowLast.get(Calendar.YEAR) == calOfficialDate.get(Calendar.YEAR) && calNowLast.get(Calendar.MONTH) == calOfficialDate.get(Calendar.MONTH)) {
                //入职日是当月
                if (calNowOne.get(Calendar.YEAR) == calEnterDay.get(Calendar.YEAR) && calNowOne.get(Calendar.MONTH) == calEnterDay.get(Calendar.MONTH)) {
                    //上月试用天数
                    lastMonthSuitDays = 0;
                    //本月试用天数
                    thisMonthSuitDays = getWorkDaysExceptWeekend(calEnterDay.getTime(), calSuitDate.getTime());
                }
                //入职不是当月
                else {
                    //上月试用天数
                    //上月1日/入职日期 取大值
                    Date tempStart = calLastOne.getTime();
                    if (calEnterDay.getTime().getTime() > calLastOne.getTime().getTime()) {
                        tempStart = calEnterDay.getTime();
                    }
                    lastMonthSuitDays = getWorkDaysExceptWeekend(tempStart, calLast.getTime());
                    //本月试用天数
                    thisMonthSuitDays = getWorkDaysExceptWeekend(calNowOne.getTime(), calSuitDate.getTime());
                }

                //上月正式天数
                lastMonthDays = 0;
                //本月正式天数
                Date temp = calResignationDate.getTime();
                if (calNowLast.getTime().getTime() < calResignationDate.getTime().getTime()) {
                    temp = calNowLast.getTime();
                }
                thisMonthDays = getWorkDaysExceptWeekend(calOfficialDate.getTime(), temp);
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
        OtherTwo othertwo = new OtherTwo();
        CasgiftApply casgiftapply = new CasgiftApply();
        casgiftapply.setPayment("0");
        //add gbb 0727 关联祝礼金 start
        casgiftapply.setReleasedate(DateUtil.format(new Date(),"yyyy-MM"));
        //add gbb 0727 关联祝礼金 end
        casgiftapply.setStatus("4");
        casgiftapply.setUser_id(userid);
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
            Query query = new Query();
            String User_id = casgift.getUser_id();
            query.addCriteria(Criteria.where("userid").is(User_id));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            othertwo.setJobnumber(customerInfo.getUserinfo().getJobnumber());
            othertwo.setType("0");
            othertwo.setRowindex(rowundex);
            othertwo.setRootknot(casgift.getTwoclass());
            othertwo.setMoneys(casgift.getAmoutmoney());
            othertwoMapper.insertSelective(othertwo);
        }
        List<OtherTwo2> otherTwo2List = givingMapper.selectOthertwo(givingid);
        if (otherTwo2List.size() > 0) {
            for (OtherTwo2 otherTwo2 : otherTwo2List) {
                if (tokenModel != null) {
                    otherTwo2.preInsert(tokenModel);
                } else {
                    otherTwo2.preInsert();
                }
                otherTwo2.setUser_id(otherTwo2.getUser_id());
                otherTwo2.setMoneys(otherTwo2.getMoneys());
                otherTwo2.setOthertwo2_id(UUID.randomUUID().toString());
                othertwo2Mapper.insert(otherTwo2);
            }
        }
    }

    /**
     * 生成基数表
     * FJL
     */
    @Override
    public void insertContrast(String givingid, TokenModel tokenModel) throws Exception {
        Contrast contrast = new Contrast();
        Base base = new Base();
        base.setGiving_id(givingid);
        base.setUser_id(userid);
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

                Wages wages = new Wages();
                wages.setUser_id(base1.getUser_id());
                wages.setActual("0");
                List<Wages> wageslist = wagesMapper.select(wages);
                if (wageslist != null) {
                    for (Wages wa : wageslist) {
                        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM");
                        String strTemp = sf1.format(new Date());
                        String strTemp1 = sf1.format(wa.getCreateon());
                        Date delDate = sf1.parse(strTemp);
                        Calendar c = Calendar.getInstance();
                        c.setTime(delDate);
                        c.add(Calendar.MONTH, -1);
                        String year1 = String.valueOf(c.get(Calendar.YEAR));    //获取年
                        String month1 = String.valueOf(c.get(Calendar.MONTH) + 1);
                        String aa = year1 + "-" + month1;
                        if (strTemp.equals(strTemp1)) {
                            contrast.setThismonth(wa.getRealwages());
                        } else if (strTemp1.equals(aa)) {
                            contrast.setLastmonth(wa.getRealwages());
                        }
                    }
                }
                contrastMapper.insertSelective(contrast);
            }
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
        // UPD_GBB_2020/05/20 ALL
        String thisMouth = "0";
        //当月工资
//        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
//            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
//                thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()) + Double.parseDouble(customerInfo.getUserinfo().getDuty()));
//            }
//            else{
//                thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()));
//            }
//        }
        // UPD_GBB_2020/6/9 start
        //本月工资
        if(addMouth == 1){
//            Calendar time = Calendar.getInstance();
//            time.add(Calendar.MONTH, addMouth);
//            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//            if (customerInfo.getUserinfo().getGridData() != null) {
//                List<CustomerInfo.Personal> personals = customerInfo.getUserinfo().getGridData();
//                personals = personals.stream().filter(coi -> (!com.mysql.jdbc.StringUtils.isNullOrEmpty(coi.getDate()))).collect(Collectors.toList());
//                personals = personals.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed())
//                        .collect(Collectors.toList());
//
//                for (CustomerInfo.Personal personal : personals) {
//                    if(!personal.getDate().equals("Invalid date")){
//                        if (sf.parse(personal.getDate()).getTime() < sf.parse((time.get(Calendar.YEAR) + "-" + getMouth(sf.format(time.getTime())) + "-01")).getTime()) {
//                            // UPD_GBB_2020/05/20
//                            //thisMouth = personal.getBasic();
//                            thisMouth = String.valueOf(Double.parseDouble(personal.getBasic()) + Double.parseDouble(personal.getDuty()));
//                            // UPD_GBB_2020/05/20
//                            break;
//                        }
//                    }
//                }
//            }
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
                    thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()) + Double.parseDouble(customerInfo.getUserinfo().getDuty()));
                }
                else{
                    thisMouth = String.valueOf(Double.parseDouble(customerInfo.getUserinfo().getBasic()));
                }
            }
        }
        else{ //上月基本工資
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserid())) {
                List<Wages> Wages = lastwages.stream().filter(coi -> (coi.getUser_id().contains(customerInfo.getUserid()))).collect(Collectors.toList());
                if(Wages.size() > 0){
                    //本月基本工资 + 本月职责工资
                    thisMouth = String.valueOf(Double.parseDouble(Wages.get(0).getBasethismonthbasic()) + Double.parseDouble(Wages.get(0).getThismonthduty()));
                }
            }
        }
        // UPD_GBB_2020/6/9 end
        return thisMouth;
    }

    public Map<String, String> getSalaryBasicAndDuty(CustomerInfo customerInfo, int addMouth) throws ParseException {
        // UPD_GBB_2020/05/20 ALL
        String thisMonth = "0";
        String thisMonthBasic = "0";
        String thisMonthDuty = "0";
        // UPD_GBB_2020/6/9 start
//        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
//            thisMonth = customerInfo.getUserinfo().getBasic();
//            thisMonthBasic = customerInfo.getUserinfo().getBasic();
//        }
//        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
//            thisMonthDuty = customerInfo.getUserinfo().getDuty();
//        }
//        if(addMouth == 0){
//            Calendar time = Calendar.getInstance();
//            time.add(Calendar.MONTH, addMouth);
//            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//            if (customerInfo.getUserinfo().getGridData() != null) {
//                List<CustomerInfo.Personal> personals = customerInfo.getUserinfo().getGridData();
//                personals = personals.stream().filter(coi -> (!com.mysql.jdbc.StringUtils.isNullOrEmpty(coi.getDate()))).collect(Collectors.toList());
//                personals = personals.stream().sorted(Comparator.comparing(CustomerInfo.Personal::getDate).reversed())
//                        .collect(Collectors.toList());
//
//                for (CustomerInfo.Personal personal : personals) {
//                    if(!personal.getDate().equals("Invalid date")){
//                        if (sf.parse(personal.getDate()).getTime() < sf.parse((time.get(Calendar.YEAR) + "-" + getMouth(sf.format(time.getTime())) + "-01")).getTime()) {
//                            // UPD_GBB_2020/05/20
//                            //thisMonth = personal.getAfter();
//                            thisMonth = personal.getBasic();
//                            // UPD_GBB_2020/05/20
//                            thisMonthBasic = personal.getBasic();
//                            thisMonthDuty = personal.getDuty();
//                            break;
//                        }
//                    }
//                }
//            }
//        }
        // UPD_GBB_2020/6/9 start
        //本月工资
        if(addMouth == 1){
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getBasic())) {
                thisMonth = customerInfo.getUserinfo().getBasic();
                thisMonthBasic = customerInfo.getUserinfo().getBasic();
            }
            if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDuty())) {
                thisMonthDuty = customerInfo.getUserinfo().getDuty();
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
     * @return void
     * @Method insertResidual
     * @Author SKAIXX
     * @Description 残业计算
     * @Date 2020/3/13 15:51
     * @Param [givingid, tokenModel]
     **/
    @Override
    public void insertResidual(String strFlg, String givingid, TokenModel tokenModel) throws Exception {
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
        base.setUser_id(userid);
        List<Base> baseList = baseMapper.select(base);

        // 代休截止间隔修改为从字典中获取
        Dictionary replaceDic = new Dictionary();
        replaceDic.setCode("PR061001");    // 代休间隔
        replaceDic = dictionaryMapper.select(replaceDic).get(0);
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
            if(strFlg.equals("1")){//预计
                attendance = new Attendance();
                attendance.setUser_id(item.getUser_id());
                attendance.setYears(currentYear);
                attendance.setMonths(currentMonth);
                attendanceList = attendanceMapper.select(attendance);
            }
            else{
                //实际AttendanceResignation
                attendanceList = attendanceMapper.selectResignation(item.getUser_id(),currentYear,currentMonth);
            }
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
        String rn = StringUtils.isEmpty(base.getRn()) || "その他".equals(base.getRn()) ? "PR021001" : base.getRn();
        if (Integer.parseInt(rn.substring(rn.length() - 2)) > 5) {
            isOverR8 = true;
        }

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
     * @return
     * @Method insertLackattendance
     * @Author SKAIXX
     * @Description 欠勤数据插入
     * @Date 2020/3/16 8:52
     * @Param [givingid, tokenModel]
     **/
    @Override
    public void insertLackattendance(String strFlg,String givingid, TokenModel tokenModel) throws Exception {
        // region 获取基数表数据
        Base base = new Base();
        base.setGiving_id(givingid);
        base.setUser_id(userid);
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
            if(strFlg.equals("1")){//预计
                attendance = new Attendance();
                attendance.setUser_id(item.getUser_id());
                attendance.setYears(currentYear);
                attendance.setMonths(currentMonth);
                attendanceList = attendanceMapper.select(attendance);
            }
            else{
                //实际AttendanceResignation
                attendanceList = attendanceMapper.selectResignation(item.getUser_id(),currentYear,currentMonth);
            }

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
        Dictionary shortDictionary = new Dictionary();
        shortDictionary.setCode("PR049005");    // 短病欠扣除比例
        shortDictionary = dictionaryMapper.select(shortDictionary).get(0);

        // 获取长病欠
        Dictionary longDictionary = new Dictionary();
        longDictionary.setCode("PR047001");     // 大連社会最低賃金
        longDictionary = dictionaryMapper.select(longDictionary).get(0);
        // 长病欠小时工资
        double longSalary = Double.parseDouble(longDictionary.getValue2()) / 21.75d / 8d;

        // UPD_GBB_2020/06/11 update
        if ("pre".equals(mode)) {   // 前月欠勤费用
            // 欠勤费用-正式
            //total += Double.parseDouble(ifNull(lackattendance.getLastdiligenceformal())) * preSalaryPerHour;
            double Lastdiligenceformal = Double.parseDouble(ifNull(lackattendance.getLastdiligenceformal())) * preSalaryPerHour;
            // 欠勤费用-试用
            //total += Double.parseDouble(ifNull(lackattendance.getLastdiligencetry())) * preSalaryPerHour * 0.9d;
            double Lastdiligencetry = Double.parseDouble(ifNull(lackattendance.getLastdiligencetry())) * preSalaryPerHour * 0.9d;
            // 短病欠-正式
//            total += Double.parseDouble(ifNull(lackattendance.getLastshortdeficiencyformal())) * preSalaryPerHour
//                    * Double.parseDouble(shortDictionary.getValue2());
            double Lastshortdeficiencyformal = Double.parseDouble(ifNull(lackattendance.getLastshortdeficiencyformal())) * preSalaryPerHour
                    * Double.parseDouble(shortDictionary.getValue2());
            // 短病欠-试用
//            total += Double.parseDouble(ifNull(lackattendance.getLastshortdeficiencytry())) * preSalaryPerHour * 0.9d
//                    * Double.parseDouble(shortDictionary.getValue2());
            double Lastshortdeficiencytry = Double.parseDouble(ifNull(lackattendance.getLastshortdeficiencytry())) * preSalaryPerHour * 0.9d
                    * Double.parseDouble(shortDictionary.getValue2());

            // 长病欠-正式
            //total += Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencyformal())) * longSalary;
            double Lastchronicdeficiencyformal = Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencyformal())) * longSalary;
            // 长病欠-试用
            //total += Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencytry())) * longSalary * 0.9d;
            double Lastchronicdeficiencytry = Double.parseDouble(ifNull(lackattendance.getLastchronicdeficiencytry())) * longSalary * 0.9d;

            //费用-正式
            double totala = - Lastdiligenceformal - Lastshortdeficiencyformal - Lastchronicdeficiencyformal;
            //费用-试用
            double totalb = - Lastdiligencetry - Lastshortdeficiencytry - Lastchronicdeficiencytry;
            total = totala + totalb;
        } else {    // 当月欠勤费用
            // 欠勤费用-正式
            //total += Double.parseDouble(ifNull(lackattendance.getThisdiligenceformal())) * currentSalaryPerHour;
            double Thisdiligenceformal = Double.parseDouble(ifNull(lackattendance.getThisdiligenceformal())) * currentSalaryPerHour;
            // 欠勤费用-试用
            //total += Double.parseDouble(ifNull(lackattendance.getThisdiligencetry())) * currentSalaryPerHour * 0.9d;
            double Thisdiligencetry = Double.parseDouble(ifNull(lackattendance.getThisdiligencetry())) * currentSalaryPerHour * 0.9d;

            // 短病欠-正式
            total += Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencyformal())) * currentSalaryPerHour
                    * Double.parseDouble(shortDictionary.getValue2());
            double Thisshortdeficiencyformal = Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencyformal())) * currentSalaryPerHour
                    * Double.parseDouble(shortDictionary.getValue2());
            // 短病欠-试用
//            total += Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencytry())) * currentSalaryPerHour * 0.9d
//                    * Double.parseDouble(shortDictionary.getValue2());
            double Thisshortdeficiencytry = Double.parseDouble(ifNull(lackattendance.getThisshortdeficiencytry())) * currentSalaryPerHour * 0.9d
                    * Double.parseDouble(shortDictionary.getValue2());

            // 长病欠-正式
            //total += Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencyformal())) * longSalary;
            double Thischronicdeficiencyformal = Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencyformal())) * longSalary;
            // 长病欠-试用
            //total += Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencytry())) * longSalary * 0.9d;
            double Thischronicdeficiencytry = Double.parseDouble(ifNull(lackattendance.getThischronicdeficiencytry())) * longSalary * 0.9d;
            //费用-正式
            double totala = - Thisdiligenceformal - Thisshortdeficiencyformal - Thisshortdeficiencytry;
            //费用-试用
            double totalb = - Thisdiligencetry - Thischronicdeficiencyformal - Thischronicdeficiencytry;
            total = totala + totalb;
        }
        // UPD_GBB_2020/06/11 update
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    //计算 其他1 当月应出勤天数-lxx
    private int getDaysforOtherOne(Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        int monthDays = getWorkDaysExceptWeekend(cal.getTime(), calEnd.getTime());
        Date statrd = new Date();
        Date endd = new Date();

        if (start.getTime() < cal.getTime().getTime()) {
            statrd = cal.getTime();
        } else {
            statrd = start;
        }
        if (end.getTime() > calEnd.getTime().getTime()) {
            endd = calEnd.getTime();
        } else {
            endd = end;
        }
        int restDays = getWorkDaysExceptWeekend(statrd, endd);

        return (monthDays - restDays);
    }

    //获取工作日-lxx
    private int getWorkDaysExceptWeekend(Date start, Date end) {
        int workDays = 0;
        if (end.getTime() > start.getTime()) {
            //        Integer holi = workingDayMapper.getHolidayExceptWeekend(start, end);
            Calendar calStar = Calendar.getInstance();
            calStar.setTime(start);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            for (int i = calStar.get(Calendar.DATE); i <= calEnd.get(Calendar.DATE); i++) {
                Calendar cal = Calendar.getInstance();
                cal.set(calStar.get(Calendar.YEAR), calStar.get(Calendar.MONTH), i);
                int day = cal.get(Calendar.DAY_OF_WEEK);
                if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
                    workDays++;
                }
            }
        }
        return workDays;
    }

    // region 入职和离职 BY Cash
    // 入职表插入数据
    public void insertInduction(String givingid, TokenModel tokenModel) throws Exception {
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
        List<Induction> inductions = new ArrayList<>();
        // 今月日期
        Calendar thisMonthDate = Calendar.getInstance();
        // 上月日期
        Calendar lastMonthDate = Calendar.getInstance();
        lastMonthDate.add(Calendar.MONTH, -1);
        SimpleDateFormat sfUTC = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        SimpleDateFormat sfChina = new SimpleDateFormat("yyyy-MM-dd");
        int lastDay = thisMonthDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        long mouthStart = sfChina.parse((thisMonthDate.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(thisMonthDate.getTime())) + "-01")).getTime();
        long mouthEnd = sfChina.parse((thisMonthDate.get(Calendar.YEAR) + "-" + getMouth(sfChina.format(thisMonthDate.getTime())) + "-" + lastDay)).getTime();
        // 保留小数点后两位四舍五入
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        // 正社员工開始日
        String staffStartDate = "";
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
        dictionary.setPcode("PR049");
        List<Dictionary> proportionList = dictionaryMapper.select(dictionary);
        // 试用期工资扣除比例
        double wageDeductionProportion = 0d;
        for (Dictionary diction : proportionList) {
            if (diction.getCode().equals("PR049010")) {
                wageDeductionProportion = Double.parseDouble(diction.getValue2());
            }
        }
        // 抽取上月发工资的人员
        List<String> userids = wagesMapper.lastMonthWage(thisMonthDate.get(Calendar.YEAR), Integer.parseInt(getMouth(sfChina.format(lastMonthDate.getTime()))),userid);
        if (customerInfos.size() > 0) {
            for (CustomerInfo customerInfo : customerInfos) {
                // 退职日非空的情况（该员工的給料和补助在退职处理中计算）
                if (StringUtils.isNotEmpty(customerInfo.getUserinfo().getResignation_date())) {
                    continue;
                }
                Induction induction = new Induction();
                induction.setGiving_id(givingId);
                // 上月工资结算时点过后入职没发工资的人（包含上月入职和本月入职的员工）
                if (!userids.contains(customerInfo.getUserid()) && userids.size() > 0) {
                    if (StringUtils.isNotEmpty(customerInfo.getUserinfo().getEnddate())) {
                        // 转正日期
                        if (customerInfo.getUserinfo().getEnddate().indexOf("Z") < 0) {
                            customerInfo.getUserinfo().setEnddate(formatStringDate(customerInfo.getUserinfo().getEnddate()));
                        }
                        Date endDate = sfUTC.parse(customerInfo.getUserinfo().getEnddate().replace("Z", " UTC"));
                        // 本月转正
                        if (endDate.getTime() >= mouthStart && endDate.getTime() <= mouthEnd) {
                            // 正社员工開始日
                            staffStartDate = endDate.toString();
                            induction.setStartdate(endDate);
                        }
                    }
                    // 计算給料和补助
                    calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df);
                    inductions.add(induction);
                } else {
                    // 上月开了工资的员工
                    // 试用期截止日为空的情况
                    if (StringUtils.isEmpty(customerInfo.getUserinfo().getEnddate())) {
                        // 本月未转正
                        // 计算給料和补助
                        calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df);
                        inductions.add(induction);
                    } else {
                        // 试用期截止日不为空的情况
                        // 本月转正或未转正的人
                        if (customerInfo.getUserinfo().getEnddate().indexOf("Z") < 0) {
                            customerInfo.getUserinfo().setEnddate(formatStringDate(customerInfo.getUserinfo().getEnddate()));
                        }
                        Date endDate = sfUTC.parse(customerInfo.getUserinfo().getEnddate().replace("Z", " UTC"));
                        if (endDate.getTime() >= mouthStart) {
                            // 本月转正
                            if (endDate.getTime() <= mouthEnd) {
                                // 正社员工開始日
                                staffStartDate = endDate.toString();
                                induction.setStartdate(endDate);
                                // 计算給料和补助
                                calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df);
                                inductions.add(induction);
                            } else {
                                // 本月未转正
                                // 计算給料和补助
                                calculateSalaryAndSubsidy(induction, customerInfo, staffStartDate, trialSubsidy, officialSubsidy, wageDeductionProportion, sfUTC, df);
                                inductions.add(induction);
                            }
                        }
                    }
                }
            }
        }
        return inductions;
    }

    // 计算給料和补助
    private void calculateSalaryAndSubsidy(Induction induction, CustomerInfo customerInfo, String staffStartDate, double trialSubsidy, double officialSubsidy,
                                           double wageDeductionProportion, SimpleDateFormat sf, DecimalFormat df) throws Exception {
        // 用户ID
        induction.setUser_id(customerInfo.getUserid());
        // 工号
        induction.setJobnumber(customerInfo.getUserinfo().getJobnumber());
        // 入社日 客户导入日期是yy-mm-dd hh:mm:ss
        if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getEnterday())){
            if (customerInfo.getUserinfo().getEnterday().indexOf("Z") < 0) {
                customerInfo.getUserinfo().setEnterday(formatStringDate(customerInfo.getUserinfo().getEnterday()));
            }
            induction.setWorddate(sf.parse(customerInfo.getUserinfo().getEnterday().replace("Z", " UTC")));
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
        induction.setAttendance(String.valueOf(lastAttendanceDays));
        // 今月試用社員出勤日数
        thisMonthSuitDays = calculateAttendanceDays(thisMonthSuitDays);
        induction.setTrial(String.valueOf(thisMonthSuitDays));
        // 給料
        if (StringUtils.isNotEmpty(staffStartDate)) {
            induction.setGive(df.format(Double.parseDouble(thisMonthSalary) - (Double.parseDouble(thisMonthSalary) / dateBase * thisMonthSuitDays * wageDeductionProportion)));
        } else {
            //本月之前的试用人员当月无转正的情况:【先月出勤日数】/【今月試用社員出勤日数】设空值（kang）
            if(!DateUtil.format(new Date(), "yyyyMM").equals(DateUtil.format(induction.getWorddate(), "yyyyMM"))){
                // 今月試用社員出勤日数
                lastAttendanceDays = 0d;
                induction.setAttendance(String.valueOf(""));
                // 今月試用社員出勤日数
                thisMonthSuitDays = 0d;
                induction.setTrial(String.valueOf(""));
            }
            if (lastAttendanceDays > 0) {
                induction.setGive(df.format(Double.parseDouble(lastMonthSalary) / dateBase * lastAttendanceDays + Double.parseDouble(thisMonthSalary)));
            } else {
                if (thisMonthSuitDays > 0) {
                    induction.setGive(df.format(Double.parseDouble(thisMonthSalary) / dateBase * thisMonthSuitDays));
                } else {
                    induction.setGive(thisMonthSalary);
                }
            }
        }
        // 一括补助
        //上月试用工作日数 / 21.75 * 500 + 上月正式工作日数 / 21.75 * 1000 + 今月試用社員出勤日数 / 21.75 * 500 + 本月正式工作日数 / 21.75 * 1000
        induction.setLunch(df.format(lastMonthSuitDays / dateBase * trialSubsidy + lastMonthDays / dateBase * officialSubsidy +
                thisMonthSuitDays / dateBase * trialSubsidy + thisMonthDays / dateBase * officialSubsidy));
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
        query.addCriteria(Criteria.where("userid").is(userid));
        List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
        if (customerInfos.size() > 0) {
            for (CustomerInfo customerInfo : customerInfos) {
                Retire retire = new Retire();
                retire.setGiving_id(givingId);
                // 用户名字
                retire.setUser_id(customerInfo.getUserid());
                // 工号
                retire.setJobnumber(customerInfo.getUserinfo().getJobnumber());
                // 退职日
                String resignationDate = customerInfo.getUserinfo().getResignation_date();
                if (resignationDate.indexOf("Z") < 0) {
                    resignationDate = formatStringDate(resignationDate.substring(0,10));
                }
                retire.setRetiredate(sfUTC.parse(resignationDate.replace("Z", " UTC")));
                // 当月基本工资
                String thisMonthSalary = getSalary(customerInfo, 1);
                // 计算出勤日数
                Map<String, String> daysList = suitAndDaysCalc(customerInfo.getUserinfo());
                // 本月正式工作日数
                double thisMonthDays = Double.parseDouble(daysList.get("thisMonthDays"));
                // 本月试用工作日数
                double thisMonthSuitDays = Double.parseDouble(daysList.get("thisMonthSuitDays"));
                // 本月出勤日数（本月试用工作日数 + 本月正式工作日数）
                double attendanceDays = thisMonthSuitDays + thisMonthDays;
                // 本月出勤日数
                retire.setAttendance(String.valueOf(calculateAttendanceDays(attendanceDays)));
                // 給料
                retire.setGive(df.format(Double.parseDouble(thisMonthSalary) / dateBase * attendanceDays));
                // 一括补助
                retire.setLunch(df.format(thisMonthSuitDays / dateBase * trialSubsidy + thisMonthDays / dateBase * officialSubsidy));
                retires.add(retire);
            }
        }
        return retires;
    }

    // 计算出勤天数
    private Double calculateAttendanceDays(double attendanceDays) {
        double rtnAttendanceDays;
        Calendar calAttendanceStart = Calendar.getInstance();
        calAttendanceStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calAttendanceEnd = Calendar.getInstance();
        calAttendanceEnd.set(Calendar.DAY_OF_MONTH, calAttendanceEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        // 本月应当的全勤日数
        int allAttendanceDay = getWorkDaysExceptWeekend(calAttendanceStart.getTime(), calAttendanceEnd.getTime());
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importWages(HttpServletRequest request,TokenModel tokenModel) throws Exception {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdym = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdymd = new SimpleDateFormat("yyyy-MM-dd");
        String strTemp = sf1.format(new Date());
        String givingid = UUID.randomUUID().toString();
        List<Wages> listVo = new ArrayList<Wages>();
        List<String> Result = new ArrayList<String>();
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        File f = null;
        f = File.createTempFile("tmp", null);
        file.transferTo(f);
        ExcelReader reader = ExcelUtil.getReader(f);
        List<List<Object>> list = reader.read();
        List<Object> model = new ArrayList<Object>();
        model.add("发放时间");
        model.add("工号");
        model.add("部门");
        model.add("名字");
        model.add("養老基数");
        model.add("失業基数");
        model.add("工傷基数");
        model.add("医療基数");
        model.add("生育基数");
        model.add("住房公积金基数");
        model.add("上月职责工资");
        model.add("上个月基本工资");
        model.add("本月基本工资");
        model.add("本月职责工资");
        model.add("rank标准工资");
        model.add("産休出勤日数(看護休暇日数)");
        model.add("本月入/退職/産休基本");
        model.add("本月入/退職/産休基本給");
        model.add("短病欠時数");
        model.add("长病欠時数");
        model.add("欠勤時数");
        model.add("病欠/欠勤控除");
        model.add("本月基本给");
        model.add("本月职责给");
        model.add("一括补助基本给");
        model.add("一括补助");
        model.add("加班补助");
        model.add("小计1(基本給+补助)");
        model.add("交通补助");
        model.add("女性洗理费");
        model.add("其他2");
        model.add("月度賞与");
        model.add("其他3");
        model.add("小计2");
        model.add("纳税工资总额(小计1+2)");
        model.add("采暖费");
        model.add("独生子女费");
        model.add("小计3");
        model.add("工资总额(纳税+免税)");
        model.add("养老保险");
        model.add("医疗保险");
        model.add("失业保险");
        model.add("个人社会保险(専項控除)");
        model.add("个人住房公积金(専項控除)");
        model.add("个人社会保险费+公积金(専項控除)合计");
        model.add("専項控除累計（当月まで）");
        model.add("附加控除累計（当月まで）");
        model.add("免税分累計（当月まで）");
        model.add("年間累計税金（先月まで）");
        model.add("住房公积金应纳税金额");
        model.add("其他4公司负担社保（24元）");
        model.add("其他5（保険：福祉）");
        model.add("当月応発工資（工资总额(纳税+免税)+只納税）");
        model.add("累計応発工資（当月含）");
        model.add("累計应纳税所得额");
        model.add("本月应扣缴所得税");
        model.add("当月实发工资");
        model.add("养老保险");
        model.add("医疗保险");
        model.add("失业保险");
        model.add("工伤保险");
        model.add("生育保险");
        model.add("暖房费");
        model.add("住房公积金");
        model.add("总计");
        model.add("工会经费基数");
        model.add("工会经费");
        model.add("工资总额(纳税+免税)+福祉+公司負担+工会経費总计");
        model.add("年休结余");
        model.add("計上奨金");
        model.add("总计+計上奨金");
        List<Object> key = list.get(0);
        for (int i = 0; i < key.size()-1; i++) {
            if (!key.get(i).toString().trim().replace("\n","").equals(model.get(i))) {
                throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
            }
        }
        //导入区分
        String importflg = "";
        if(list.size() > 1){
            Giving giv = new Giving();
            giv.setGeneration("2");//导入
            giv.setMonths(list.get(1).get(0).toString().replace("-","").substring(0,6));
            List<Giving> givlist = givingMapper.select(giv);
            if(givlist.size() > 0){
                importflg = "1";
                givingid = givlist.get(0).getGiving_id();
            }
        }
        int k = 1;
        int accesscount = 0;
        int error = 0;
        String Createonym = "";
        for (int i = 1; i < list.size(); i++) {
            Wages wages = new Wages();
            String WagesFlg = "";//判断该月是否已发放
            List<Object> value = list.get(k);
            k++;
            if (value != null && !value.isEmpty()) {
                Createonym = value.get(0).toString().substring(0,10);
                //卡号
                if (value.get(1).toString().equals("")) {
                    continue;
                }
                String click = "^(-?[0-9][0-9]*)+(.[0-9]{1,2})?$";

                //region 查询是否已经存在
                Wages wa = new Wages();
                wa.setCreateonym(Createonym.substring(0,7));
                wa.setGiving_id(givingid);
                wa.setJobnumber(value.get(1).toString());
                List<Wages> wageslist = wagesMapper.select(wa);
                if(wageslist.size() > 0){
                    WagesFlg = "1";
                    wages.setWages_id(wageslist.get(0).getWages_id());
                }
                //endregion 查询是否已经存在

                //region 人员基础信息查询
                Query query = new Query();
                String jobnumber = value.get(1).toString();
                query.addCriteria(Criteria.where("userinfo.jobnumber").is(jobnumber));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    wages.setJobnumber(jobnumber);  //工号
                    wages.setUser_id(customerInfo.getUserid());
                    //upd_fjl_0910
//                    wages.setWorkdate(customerInfo.getUserinfo().getEnterday());//入社时间
                    String resignationDate = customerInfo.getUserinfo().getEnterday();
                    if (customerInfo.getUserinfo().getEnterday().indexOf("Z") > 0) {
                        resignationDate = resignationDate.replace("-", "/").substring(0, 10);
                    }
                    //upd_fjl_0910
                    wages.setWorkdate(resignationDate);//入社时间
                    wages.setSex(customerInfo.getUserinfo().getSex());  //性别

                    wages.setBonus(customerInfo.getUserinfo().getDifference());//奨金計上

                    //独生子女
                    if (customerInfo.getUserinfo().getChildren() != null && customerInfo.getUserinfo().getChildren().equals("1")) {
                        wages.setOnlychild("1");
                    } else {
                        wages.setOnlychild("2");
                    }
                    //入/退職/産休
                    //wages.setType(value.get(1).toString());
                    //奨金計上
                    wages.setBonus(customerInfo.getUserinfo().getDifference());
                    //1999年前社会人
                    if (customerInfo.getUserinfo().getWorkday() != null && customerInfo.getUserinfo().getWorkday().length() > 0) {
                        String strWorkday = customerInfo.getUserinfo().getWorkday().substring(0, 4);
                        if (Integer.parseInt(strWorkday) < 1999) {
                            wages.setSociology("1");
                        } else {
                            wages.setSociology("2");
                        }
                    }
                    //大連戸籍
                    wages.setRegistered("2");
                    if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(customerInfo.getUserinfo().getDlnation())) {
                        if(customerInfo.getUserinfo().getDlnation().equals("1")){
                            wages.setRegistered("1");
                        }
                    }
                }
                else{
                    error = error + 1;
                    Result.add("模板第" + (k - 1) + "行的工号字段没有找到，请输入正确的工号，导入失败");
                    continue;
                }
                //endregion 人员基础信息查询

                wages.setYanglaojs(value.get(4).toString());
                wages.setShiyejs(value.get(5).toString());
                wages.setGongshangjs(value.get(6).toString());
                wages.setYiliaojs(value.get(7).toString());
                wages.setShengyujs(value.get(8).toString());
                wages.setAccumulation(value.get(9).toString());
                wages.setLastmonthduty(value.get(10).toString());
                wages.setLastmonthbasic(value.get(11).toString());
                //上月职责工资
                BigDecimal Lastmonthduty = BigDecimal.valueOf(Double.valueOf(value.get(10).toString()));
                //上个月基本工资
                BigDecimal Lastmonthbasic = BigDecimal.valueOf(Double.valueOf(value.get(11).toString()));
                //上月基本給
                wages.setLastmonth(String.valueOf(Lastmonthduty.add(Lastmonthbasic)));
                wages.setBasethismonthbasic(value.get(12).toString());
                wages.setThismonthduty(value.get(13).toString());
                //本月职责工资
                BigDecimal Basethismonthbasic = BigDecimal.valueOf(Double.valueOf(value.get(12).toString()));
                //本个月基本工资
                BigDecimal Thismonthduty = BigDecimal.valueOf(Double.valueOf(value.get(13).toString()));
                //本月基本給
                wages.setThismonth(String.valueOf(Basethismonthbasic.add(Thismonthduty)));
                wages.setRnbasesalary(value.get(14).toString());
                wages.setBirthrest(value.get(15).toString());
                wages.setThismonthbasicbasic(value.get(16).toString());
                wages.setThismonthbasic(value.get(17).toString());
                wages.setShortillness(value.get(18).toString());
                wages.setLongillness(value.get(19).toString());
                wages.setOwediligence(value.get(20).toString());
                wages.setOwingcontrol(value.get(21).toString());
                wages.setThismonthbasicgei(value.get(22).toString());
                wages.setThismonthdutygei(value.get(23).toString());
                //本月基本给
                BigDecimal Thismonthbasicgei = BigDecimal.valueOf(Double.valueOf(value.get(22).toString()));
                //本月职责给
                BigDecimal Thismonthdutygei = BigDecimal.valueOf(Double.valueOf(value.get(23).toString()));
                //基本給实际金额
                wages.setActualamount(String.valueOf(Thismonthbasicgei.add(Thismonthdutygei)));
                wages.setYkbzjs(value.get(24).toString());
                wages.setYkbz(value.get(25).toString());
                wages.setOvertimesubsidy(value.get(26).toString());
                wages.setTotal1(value.get(27).toString());
                wages.setTraffic(value.get(28).toString());
                wages.setWashingtheory(value.get(29).toString());
                wages.setOther2(value.get(30).toString());
                wages.setAppreciation(value.get(31).toString());
                wages.setOther3(value.get(32).toString());
                wages.setTotal2(value.get(33).toString());
                wages.setTaxestotal(value.get(34).toString());
                wages.setHeating(value.get(35).toString());
                wages.setOnlychildmoney(value.get(36).toString());
                wages.setTotal3(value.get(37).toString());
                wages.setTotalwages(value.get(38).toString());
                wages.setEndowmentinsurance(value.get(39).toString());
                wages.setMedicalinsurance(value.get(40).toString());
                wages.setUnemploymentinsurance(value.get(41).toString());
                wages.setSocialinsurance(value.get(42).toString());
                wages.setAccumulationfund(value.get(43).toString());
                wages.setDisciplinarycontrol(value.get(44).toString());
                wages.setThismonthterm(value.get(45).toString());
                wages.setThismonthadditional(value.get(46).toString());
                wages.setThismonthdutyfree(value.get(47).toString());
                wages.setLastdutyfree(value.get(48).toString());
                wages.setHousingmoneys(value.get(49).toString());
                wages.setOther4(value.get(50).toString());
                wages.setOther5(value.get(51).toString());
                wages.setShouldwages(value.get(52).toString());
                wages.setShouldcumulative(value.get(53).toString());
                wages.setShouldpaytaxes(value.get(54).toString());
                wages.setThismonthadjustment(value.get(55).toString());
                wages.setRealwages(value.get(56).toString());
                wages.setComendowmentinsurance(value.get(57).toString());
                wages.setCommedicalinsurance(value.get(58).toString());
                wages.setComunemploymentinsurance(value.get(59).toString());
                wages.setCominjuryinsurance(value.get(60).toString());
                wages.setCombirthinsurance(value.get(61).toString());
                wages.setComheating(value.get(62).toString());
                wages.setComaccumulationfund(value.get(63).toString());
                wages.setTotal(value.get(64).toString());
                wages.setLabourunionbase(value.get(65).toString());
                wages.setLabourunionfunds(value.get(66).toString());
                wages.setComtotalwages(value.get(67).toString());
                wages.setNjjy(value.get(68).toString());
                wages.setBonusmoney(value.get(69).toString());
                wages.setTotalbonus(value.get(70).toString());
            }
            wages.setCreateonym(Createonym.substring(0,7));
            wages.setGrantstatus("1");
            wages.setActual("0");
            wages.setGiving_id(givingid);
            int rowundex = accesscount+ 1;
            wages.setRowindex(rowundex);
            if(WagesFlg.equals("")){
                wages.preInsert(tokenModel);
                wages.setWages_id(UUID.randomUUID().toString());
                wagesMapper.insert(wages);
            }
            else{
                wages.preUpdate(tokenModel);
                wagesMapper.updateByPrimaryKey(wages);
            }
            listVo.add(wages);
            accesscount = accesscount + 1;
        }
        if(importflg.equals("")){
            // region  給与計算预计工资
            if(error == 0 && accesscount > 0){
                // region  給与計算预计工资
                Giving giving = new Giving();
                giving.preInsert(tokenModel);
                giving.setStatus("4");
                giving.setGiving_id(givingid);
                giving.setGeneration("2");//导入
                giving.setGrantstatus("1");//已发放
                giving.setGenerationdate(sdymd.parse(Createonym));
                giving.setMonths(Createonym.replace("-","").substring(0,6));
                giving.setActual("0");//预计
                givingMapper.insert(giving);
                //endregion 給与計算预计工资
            }
        }
        Result.add("失败数：" + error);
        Result.add("成功数：" + accesscount);
        return Result;
    }
}
