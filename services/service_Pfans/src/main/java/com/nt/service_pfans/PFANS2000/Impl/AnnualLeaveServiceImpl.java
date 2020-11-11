package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.restViewVo;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import com.nt.utils.ApiResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;
import com.nt.utils.services.TokenService;
import com.nt.service_pfans.PFANS2000.WagesService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@Component
@Configuration
@EnableScheduling
@CommonsLog
@Transactional(rollbackFor=Exception.class)
public class AnnualLeaveServiceImpl implements AnnualLeaveService {
    @Autowired
    private ReplacerestMapper replacerestMapper;

    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WorkingDayMapper workingDayMapper;

    @Autowired
    private AbNormalMapper abNormalMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PunchcardRecordDetailMapper punchcardrecorddetailmapper;

    @Autowired
    private PunchcardRecordDetailbpMapper punchcardrecorddetailbpmapper;

    @Autowired
    private PunchcardRecordMapper punchcardrecordMapper;
    @Autowired
    private PunchcardRecordbpMapper punchcardrecordbpMapper;
    @Autowired
    private AttendanceSettingMapper attendanceSettingMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private AttendancebpMapper attendancebpMapper;
    @Autowired
    private PunchcardRecordService punchcardRecordService;
    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;
    @Autowired
    private GivingMapper givingMapper;
    @Autowired
    private InductionMapper inductionMapper;
    @Autowired
    private RetireMapper retireMapper;
    @Autowired
    private BaseMapper baseMapper;
    @Autowired
    private OtherTwoMapper othertwoMapper;
    @Autowired
    private OtherOneMapper otheroneMapper;
    @Autowired
    private LackattendanceMapper lackattendanceMapper;
    @Autowired
    private ResidualMapper residualMapper;
    @Autowired
    private OtherFourMapper otherfourMapper;
    @Autowired
    private OtherFiveMapper otherfiveMapper;
    @Autowired
    private AppreciationMapper appreciationMapper;
    @Autowired
    private AdditionalMapper additionalMapper;
    @Autowired
    private WagesMapper wagesMapper;
    @Autowired
    private WagesService wagesservice;
    @Autowired
    private DictionaryMapper dictionaryMapper;


    @Autowired
    private TokenService tokenService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<AnnualLeave> getDataList(TokenModel tokenModel) throws Exception {
        //ccm 202000702 from
        Calendar calendar = Calendar.getInstance();
        //当前年度
        int year = 0;
        int month = calendar.get(Calendar.MONTH);
        if(month >= 1 && month <= 3) {
            year = calendar.get(Calendar.YEAR) - 1;
        }else {
            year = calendar.get(Calendar.YEAR);
        }
        List<AnnualLeave> tempannualLeaveList = new ArrayList<AnnualLeave>();
        List<AnnualLeave> annualLeaveList = annualLeaveMapper.getDataList(tokenModel.getOwnerList());
        for(AnnualLeave annualLeave:annualLeaveList)
        {
            List<CustomerInfo> customerinfo = mongoTemplate.find(new Query(Criteria.where("userid").is(annualLeave.getUser_id())), CustomerInfo.class);
            if(customerinfo.size()>0)
            {
                //生成年休时，去除已离职人员
                if(customerinfo.get(0).getUserinfo() .getResignation_date() != null && !customerinfo.get(0).getUserinfo() .getResignation_date().isEmpty())
                {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    String resignationdate = customerinfo.get(0).getUserinfo().getResignation_date().substring(0, 10);
                    Calendar rightNow = Calendar.getInstance();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    if(customerinfo.get(0).getUserinfo().getResignation_date().length() >= 24)
                    {
                        rightNow.setTime(Convert.toDate(resignationdate));
                        rightNow.add(Calendar.DAY_OF_YEAR, 1);
                        resignationdate = s.format(rightNow.getTime());
                    }
                    //离职日期 < 当前日期
                    if(s.parse(resignationdate).getTime() < s.parse(s.format(cal.getTime())).getTime())
                    {
                        continue;
                    }
                    else
                    {
                        BigDecimal b = new BigDecimal(remainingAnnual(annualLeave.getUser_id(),String.valueOf(year)));
                        annualLeave.setAnnual_avg_remaining(b.toString());
                        tempannualLeaveList.add(annualLeave);
                    }
                }
                else
                {
                    annualLeave.setAnnual_avg_remaining("-");
                    tempannualLeaveList.add(annualLeave);
                }
            }
        }
        //ccm 202000702 to
        return tempannualLeaveList;
    }
    @Scheduled(cron="0 0 0 1 4 *")//正式时间每年4月1日零时执行
    public void creatAnnualLeaveAn() throws Exception {
        insert();
    }
    //系统服务--事业年度开始获取年休
    @Override
    public void insert() throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {
                //if(customer.getUserid().equals("5e78b2264e3b194874180f37")){
                //ccm 202000702 from
                //生成年休时，去除已离职人员
                if(customer.getUserinfo() .getResignation_date() != null && !customer.getUserinfo() .getResignation_date().isEmpty())
                {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    String resignationdate = customer.getUserinfo().getResignation_date().substring(0, 10);
                    Calendar rightNow = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    if(customer.getUserinfo().getResignation_date().length() >= 24)
                    {
                        rightNow.setTime(Convert.toDate(resignationdate));
                        rightNow.add(Calendar.DAY_OF_YEAR, 1);
                        resignationdate = s.format(rightNow.getTime());
                    }
                    //离职日期 < 当前日期
                    if(s.parse(resignationdate).getTime() < s.parse(s.format(calendar.getTime())).getTime())
                    {
                        continue;
                    }
                }
                //ccm 202000702 to
                insertannualLeave(customer);
//                //ccm 临时测试接口
//                SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
//                String enterdate = customer.getUserinfo().getEnterday().substring(0, 10);
//                Calendar rightNow1 = Calendar.getInstance();
//                rightNow1.setTime(Convert.toDate(enterdate));
//                Calendar calendar1 = Calendar.getInstance();
//                calendar1.setTime(Convert.toDate("2020-04-04"));
//                if(customer.getUserinfo().getEnterday().length() >= 24)
//                {
//                    rightNow1.setTime(Convert.toDate(enterdate));
//                    rightNow1.add(Calendar.DAY_OF_YEAR, 1);
//                    enterdate = s1.format(rightNow1.getTime());
//                }
//                if(s1.parse(s1.format(rightNow1.getTime())).getTime() > s1.parse(s1.format(calendar1.getTime())).getTime())
//                {
//                    AnnualLeave a = new AnnualLeave();
//                    a.setYears("2020");
//                    a.setUser_id(customer.getUserid());
//                    List<AnnualLeave> annualLeaveList = annualLeaveMapper.select(a);
//                    if(annualLeaveList.size()>0)
//                    {
//                        annualLeaveMapper.deleteByPrimaryKey(annualLeaveList.get(0).getAnnualleave_id());
//                        insertannualLeave(customer);
//                    }
//                }
//
//
//                //ccm 临时测试接口
                //}
            }
        }
        //会社特别休日加班
//        int count = replacerestMapper.updateDateList();
    }

    //add 导入人员计算年休 20201030
    @Override
    public void insertAnnualImport() throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {
                //if(customer.getUserid().equals("5e78b2264e3b194874180f37")){
                //ccm 202000702 from
                //生成年休时，去除已离职人员
                if(customer.getUserinfo() .getResignation_date() != null && !customer.getUserinfo() .getResignation_date().isEmpty())
                {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    String resignationdate = customer.getUserinfo().getResignation_date().substring(0, 10);
                    Calendar rightNow = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    if(customer.getUserinfo().getResignation_date().length() >= 24)
                    {
                        rightNow.setTime(Convert.toDate(resignationdate));
                        rightNow.add(Calendar.DAY_OF_YEAR, 1);
                        resignationdate = s.format(rightNow.getTime());
                    }
                    //离职日期 < 当前日期
                    if(s.parse(resignationdate).getTime() < s.parse(s.format(calendar.getTime())).getTime())
                    {
                        continue;
                    }
                }
                //ccm 临时测试接口
                SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
                AnnualLeave a = new AnnualLeave();
                String ymd = s1.format(new Date());
                String years = ymd.substring(0,4);
                String mm = ymd.substring(5,7);
                if(Double.valueOf(mm)<4)
                {
                    years = String.valueOf(Integer.valueOf(years) + 1);
                }
                a.setYears(years);
                a.setUser_id(customer.getUserid());
                List<AnnualLeave> annualLeaveList = annualLeaveMapper.select(a);
                if(annualLeaveList.size()==0)
                {
                    insertannualLeave(customer);
                }
                //ccm 临时测试接口
                //}
            }
        }
    }
    //add 导入人员计算年休 20201030
    //新建
    @Override
    public void insertannualLeave(CustomerInfo customer) throws Exception {
        AnnualLeave annualLeave = new AnnualLeave();
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        annualLeave.setUser_id(customer.getUserid());
        annualLeave.setYears(DateUtil.format(calendar.getTime(),"YYYY"));
        List<AnnualLeave> annualLeavelist = annualLeaveMapper.select(annualLeave);
        BigDecimal zero = BigDecimal.ZERO;
        //上年度
        BigDecimal annual_leave_lastyear = BigDecimal.ZERO;
        BigDecimal deduct_annual_leave_lastyear = BigDecimal.ZERO;
        BigDecimal remaining_annual_leave_lastyear = BigDecimal.ZERO;

        BigDecimal remaining_paid_leave_lastyear = BigDecimal.ZERO;

        //本年度
        BigDecimal annual_leave_thisyear = BigDecimal.ZERO;
        BigDecimal deduct_annual_leave_thisyear = BigDecimal.ZERO;
        BigDecimal remaining_annual_leave_thisyear = BigDecimal.ZERO;
        BigDecimal remaining_paid_leave_thisyear = BigDecimal.ZERO;

        if(annualLeavelist.size() > 0){
            annual_leave_lastyear = annualLeavelist.get(0).getAnnual_leave_thisyear();
            deduct_annual_leave_lastyear = annualLeavelist.get(0).getDeduct_annual_leave_thisyear();
            remaining_annual_leave_lastyear = annualLeavelist.get(0).getRemaining_annual_leave_thisyear();
        }
        annualLeave = new AnnualLeave();
        String annualleaveid = UUID.randomUUID().toString();
        TokenModel tokenModel = new TokenModel();
        tokenModel.setUserId(customer.getUserid());
        annualLeave.preInsert(tokenModel);
        annualLeave.setAnnualleave_id(annualleaveid);
        annualLeave.setUser_id(customer.getUserid());
        annualLeave.setOwner(customer.getUserid());
        annualLeave.setYears(DateUtil.format(new Date(),"YYYY"));
        annualLeave.setCenter_id(customer.getUserinfo().getCenterid());
        annualLeave.setGroup_id(customer.getUserinfo().getGroupid());
        annualLeave.setTeam_id(customer.getUserinfo().getTeamid());
        //上年度
        annualLeave.setAnnual_leave_lastyear(annual_leave_lastyear);
        annualLeave.setDeduct_annual_leave_lastyear(deduct_annual_leave_lastyear);
        annualLeave.setRemaining_annual_leave_lastyear(remaining_annual_leave_lastyear);

        //////////////////////本年度//////////////////////
//        //入社年月日
//        String enterdaystartCal = customer.getUserinfo().getEnterday();
//        if (StringUtil.isEmpty(enterdaystartCal)) {
//            enterdaystartCal = "1980-02-29";
//        }
//        enterdaystartCal = enterdaystartCal.substring(0,10);
//        if(customer.getUserinfo().getEnterday().length()>=24)
//        {
//            Calendar calendar_ent = Calendar.getInstance();
//            calendar_ent.setTime(Convert.toDate(enterdaystartCal));
//            calendar_ent.add(Calendar.DAY_OF_YEAR, 1);
//            enterdaystartCal = sfymd.format(calendar_ent.getTime());
//        }
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        //仕事开始年月日
        String workdaystartCal = customer.getUserinfo().getWorkday();

        if (StringUtil.isNotEmpty(workdaystartCal)) {
            int year = getYears(workdaystartCal);
            //本年度法定年休（期初）
            if(year < 10){
                //年休计算不考虑试用期，未满10年均有5天年休
                //del CCM 20200411--from
//                if(customer.getUserinfo().getEnddate() == null || customer.getUserinfo().getEnddate().isEmpty())
//                {
//                    annual_leave_thisyear = annual_leave_thisyear.add(new BigDecimal("0"));
//                }
//                else
//                {
//                    String enddate = customer.getUserinfo().getEnddate().substring(0,10);
//                    if (sf1.parse(Convert.toStr(sf1.format(Convert.toDate(enddate)))).compareTo(calendar.getTime()) < 0)
//                    {
//                        annual_leave_thisyear = annual_leave_thisyear.add(new BigDecimal("5"));
//                    }
//                    else
//                    {
//                        annual_leave_thisyear = annual_leave_thisyear.add(new BigDecimal("0"));
//                    }
//                }
                //del CCM 20200411--to
                annual_leave_thisyear = annual_leave_thisyear.add(new BigDecimal("5"));
            }
            if(year >= 10 && year < 20){
                annual_leave_thisyear = annual_leave_thisyear.add(new BigDecimal("10"));
            }
            if(year >= 20){
                annual_leave_thisyear = annual_leave_thisyear.add(new BigDecimal("15"));
            }
        }

        //员工入职/离职时，年休计算
        //入社年月日
        String enterdaystartCal = customer.getUserinfo().getEnterday();
        if (StringUtil.isEmpty(enterdaystartCal)) {
            enterdaystartCal = "1980-02-29";
        }
        enterdaystartCal = enterdaystartCal.substring(0,10);
        Calendar calendar_start = Calendar.getInstance();
        calendar_start.setTime(Convert.toDate(enterdaystartCal));
        if(customer.getUserinfo().getEnterday().length()>=24)
        {
            calendar_start.setTime(Convert.toDate(enterdaystartCal));
            calendar_start.add(Calendar.DAY_OF_YEAR, 1);
            enterdaystartCal = sfymd.format(calendar_start.getTime());
        }
        //离社年月日
        String resignationDateendCal = "";
        if (!StringUtil.isEmpty(customer.getUserinfo().getResignation_date()))
        {
             resignationDateendCal = customer.getUserinfo().getResignation_date().substring(0, 10);
            if(customer.getUserinfo().getResignation_date().length()>=24)
            {
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(Convert.toDate(resignationDateendCal));
                rightNow.add(Calendar.DAY_OF_YEAR, 1);
                resignationDateendCal = sfymd.format(rightNow.getTime());
            }
        }
        //事业年度开始（4月1日）

        Calendar start = Calendar.getInstance();
        start.setTime(Convert.toDate(enterdaystartCal));
        String shi_start = "";
        shi_start = sfymd.format(start.getTime());
        if(Integer.valueOf(shi_start.substring(5,7)) < 4)
        {
            shi_start = String.valueOf(Integer.valueOf(shi_start.substring(0,4))-1) + "-04-01";
        }
        else
        {
            shi_start = shi_start.substring(0,4) + "-04-01";
        }
        DateUtil.format(start.getTime(),"yyyy-MM-dd");
        start.setTime(Convert.toDate(shi_start));
        Calendar end = Calendar.getInstance();
        end.setTime(Convert.toDate(shi_start));
        end.add(Calendar.YEAR,1);
        end.add(Calendar.DAY_OF_YEAR,-1);
        DateUtil.format(end.getTime(),"yyyy-MM-dd");
//        Calendar calendar_a = Calendar.getInstance();
//        calendar_a.setTime(new Date());
//        calendar_a.add(Calendar.DAY_OF_YEAR,-1);
//        DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
//        DateUtil.format(calendar.getTime(),"yyyy-MM-dd");


        //Ⅰ.途中入职：本事业年度在职期间/365天*当年度法定年休天数
        if(StringUtil.isEmpty(resignationDateendCal))
        {
            //入职日期>=事业年度开始日期 && 入职日期<=事业年度结束日期
            //途中入职
            if(calendar_start.getTime().compareTo(start.getTime())>=0 && calendar_start.getTime().compareTo(end.getTime())<=0)
            {
                int year = getYears(workdaystartCal);
                //有工作经验者
                if(year>=1)
                {
                    //入职日 > 事业年度开始日
                    if(calendar_start.getTime().compareTo(start.getTime())>0)
                    {
                        annual_leave_thisyear=dateLeave(calendar_start,end,annual_leave_thisyear);
                        annual_leave_thisyear =(new BigDecimal(annual_leave_thisyear.intValue())).setScale(2);
                    }
                }
                else
                {
                    annual_leave_thisyear = new BigDecimal(0);
                }
            }
        }
/*
        //Ⅱ.途中离职：本事业年度在职期间/12个月*当年年休天数
        if(StringUtil.isNotEmpty(resignationDateendCal))
        {
            String resignationDateendCal1 = resignationDateendCal.substring(0,10);
            if(sf1.parse(Convert.toStr(sf1.format(Convert.toDate(resignationDateendCal1)))).compareTo(calendar.getTime())>=0 && sf1.parse(Convert.toStr(sf1.format(Convert.toDate(resignationDateendCal1)))).compareTo(calendar_a.getTime())<=0)
            {
                //离职日
                calendar_a.setTime(sf1.parse(Convert.toStr(sf1.format(Convert.toDate(resignationDateendCal1)))));
                annual_leave_thisyear = dateLeave(calendar,calendar_a,annual_leave_thisyear);
                annual_leave_thisyear =(new BigDecimal(annual_leave_thisyear.intValue())).setScale(2);
                if(sf1.parse(Convert.toStr(sf1.format(Convert.toDate(enterdaystartCal)))).compareTo(calendar.getTime())>=0 && sf1.parse(Convert.toStr(sf1.format(Convert.toDate(enterdaystartCal)))).compareTo(calendar_a.getTime())<=0)
                {
                    //入职日
                    calendar.setTime(sf1.parse(Convert.toStr(sf1.format(Convert.toDate(enterdaystartCal)))));
                    //离职日
                    calendar_a.setTime(sf1.parse(Convert.toStr(sf1.format(Convert.toDate(resignationDateendCal1)))));
                    annual_leave_thisyear = dateLeave(calendar,calendar_a,annual_leave_thisyear);
                    annual_leave_thisyear =(new BigDecimal(annual_leave_thisyear.intValue())).setScale(2);
                }
            }
        }
        */

        //法定年假(本年度)
        annualLeave.setAnnual_leave_thisyear(annual_leave_thisyear);
        //法定年假扣除(本年度)
        annualLeave.setDeduct_annual_leave_thisyear(deduct_annual_leave_thisyear);
        //法定年假剩余(本年度)
        annualLeave.setRemaining_annual_leave_thisyear(annual_leave_thisyear);
        annualLeaveMapper.insertSelective(annualLeave);

        //更新人员信息
        //今年年休数
        customer.getUserinfo().setAnnualyear(String.valueOf(annual_leave_thisyear));
        //去年年休数(残)
        customer.getUserinfo().setAnnuallastyear(String.valueOf(remaining_annual_leave_lastyear));

        //今年福利年休数
        customer.getUserinfo().setWelfareyear(String.valueOf(remaining_paid_leave_thisyear));
        //去年福利年休数(残)
        customer.getUserinfo().setWelfarelastyear(String.valueOf(remaining_paid_leave_lastyear));

        //今年法定年休数
        customer.getUserinfo().setRestyear(String.valueOf(annual_leave_thisyear));
        //去年法定年休数(残)
        customer.getUserinfo().setRestlastyear(String.valueOf(remaining_annual_leave_lastyear));
        mongoTemplate.save(customer);
    }

    public BigDecimal dateLeave(Calendar startCal,Calendar endCal,BigDecimal annual_leave_thisyear) throws Exception {
        BigDecimal day_Annual_Leave = BigDecimal.ZERO;
        long time_a = startCal.getTimeInMillis();
        //事业年度終了日
        long time_b = endCal.getTimeInMillis();

        day_Annual_Leave = (new BigDecimal(((double) (time_b-time_a)/(1000*3600*24))/365)).multiply(new BigDecimal(String.valueOf(annual_leave_thisyear))).setScale(3,RoundingMode.HALF_UP);
        return day_Annual_Leave;
    }

    public int getYears(String startCal) throws Exception {
        int year = 0;
        startCal = startCal.substring(0,10);
        Calendar cal = Calendar.getInstance();
        String this_year = String.valueOf(cal.get(cal.YEAR));
        String endCal = this_year + "-04-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(Convert.toStr(sdf.format(Convert.toDate(startCal))));
        Date endDate = sdf.parse(endCal);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);
        if (endCalendar.compareTo(startCalendar) >= 0) {
            int day = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);
            int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            if (day < 0) {
                month --;
            }
            if (month < 0) {
                month += 12;
                year --;
            }
        }
        return year;
    }

    public void insertNewAnnualRest(UserVo userVo,String id) throws Exception{
        List<CustomerInfo> customerInfos =  mongoTemplate.find(new Query(Criteria.where("status").is("0")), CustomerInfo.class);
        List<CustomerInfo> _customerInfos =  customerInfos.stream().filter( customerInfo -> customerInfo.getUserid().equals(userVo.getCustomerInfo().getUserid())).collect(Collectors.toList());
        String enterDay = _customerInfos.size() > 0 ? _customerInfos.get(0).getUserinfo().getEnterday() : "";
        String userEnterDay = userVo.getCustomerInfo().getUserinfo().getEnterday();
        Calendar now = Calendar.getInstance();
        Date _now = sdf.parse(sdf.format(new Date()));
        now.setTime(_now);
        int year = now.get(Calendar.MONTH) <3 ? now.get(Calendar.YEAR) - 1 : now.get(Calendar.YEAR);
        if(((_customerInfos.size() == 0 || enterDay == "") && userEnterDay!="")){
            Iterator<Map.Entry<Double,Integer>> entry = getAunnalDays(userVo).entrySet().iterator();
            while (entry.hasNext()){
                AnnualLeave annualLeave = new AnnualLeave();
                Map.Entry<Double,Integer> _entry = entry.next();
                double deduct_paid_leave_thisyear = Double.valueOf(_entry.getValue());
                annualLeave.setAnnual_leave_thisyear(BigDecimal.valueOf(0));
                annualLeave.setAnnual_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setDeduct_annual_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setRemaining_annual_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setDeduct_annual_leave_thisyear(BigDecimal.valueOf(0));
                annualLeave.setRemaining_annual_leave_thisyear(BigDecimal.valueOf(0));
                annualLeave.setUser_id(id);
                annualLeave.setStatus("0");
                annualLeave.setCreateon(new Date());
                annualLeave.setCreateby(userVo.getCustomerInfo().getCreateby());
                annualLeave.setOwner(id);
                annualLeave.setGroup_id(userVo.getCustomerInfo().getUserinfo().getGroupid());
                annualLeave.setCenter_id(userVo.getCustomerInfo().getUserinfo().getCenterid());
                annualLeave.setTeam_id(userVo.getCustomerInfo().getUserinfo().getTeamid());
                annualLeave.setYears(year + "");
                annualLeave.setAnnualleave_id(UUID.randomUUID().toString());
                annualLeaveMapper.insert(annualLeave);
            }
        }else if((userEnterDay!="" && !sdf.parse(userEnterDay).equals(sdf.parse(enterDay)))){
            AnnualLeave annualLeave = new AnnualLeave();
            annualLeave.setYears(year + "");
            annualLeave.setUser_id(userVo.getCustomerInfo().getUserid());
            AnnualLeave _annualLeave = annualLeaveMapper.selectOne(annualLeave);
            if(_annualLeave!=null){
                Iterator<Map.Entry<Double,Integer>> entry = getAunnalDays(userVo).entrySet().iterator();
                while (entry.hasNext()){
                    Map.Entry<Double,Integer> _entry = entry.next();
                    double deduct_paid_leave_thisyear = Double.valueOf(_entry.getValue());
                    _annualLeave.setModifyon(new Date());
                    _annualLeave.setModifyby(userVo.getCustomerInfo().getModifyby());
                    annualLeaveMapper.updateByPrimaryKey(_annualLeave);
                }
            }
        }
    }

    private Map<Double,Integer> getAunnalDays(UserVo userVo) throws Exception{
        int result = 0;
        Map<Double,Integer> map = new HashMap<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        WorkingDay workingDay = new WorkingDay();
        Date _userEntryDay = sdf.parse(userVo.getCustomerInfo().getUserinfo().getEnterday());
        min.setTime(sdf.parse(userVo.getCustomerInfo().getUserinfo().getEnterday()));
        min.add(Calendar.DATE,1);
        int year = min.get(Calendar.MONTH) <3 ? min.get(Calendar.YEAR) : min.get(Calendar.YEAR) + 1;
        workingDay.setType("6");
        workingDay.setYears((year-1) + "");
        List<WorkingDay> workingDays = workingDayMapper.select(workingDay);
        List<WorkingDay> annualDays =  workingDays.stream().filter(workingday -> workingday.getWorkingdate().getTime() >= _userEntryDay.getTime()).collect(Collectors.toList());;
        Date date = sdf.parse(year + "-" + "04" +"-"+ "01");
        max.setTime(date);
        while (min.before(max)) {
            result++;
            min.add(Calendar.MONTH, 1);
        }
        BigDecimal annualRest =  new BigDecimal((float)result / 12 * 15 );
        Double _annualRest = Math.floor(annualRest.doubleValue());
        map.put(_annualRest,annualDays.size());
        return map;
    }

    @Scheduled(cron="0 30 0 * * ?")//正式时间每天半夜12点半  GBB add
    public void insertattendanceTask()throws Exception {
        insertattendance(-1,"","");
    }

    @Scheduled(cron="0 35 0 * * ?")//正式时间每天半夜12点半  GBB add
    public void insertattendancebpTask()throws Exception {
        insertattendancebp(-1,"","");
    }

    @Scheduled(cron="0 45 0 * * ?")//正式时间每天半夜12点半  GBB add
    public void insertpunchcardTask()throws Exception {
        //处理异常和加班数据
        //上月1号
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(new Date());
        calStart.add(Calendar.MONTH, -1);
        calStart.set(Calendar.DAY_OF_MONTH, 1);

        //本月末日
        Calendar calend = Calendar.getInstance();
        calend.setTime(new Date());
        int maxCurrentMonthDay=calend.getActualMaximum(Calendar.DAY_OF_MONTH);
        calend  .set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);

        for(Calendar item = calStart;item.compareTo(calend) <= 0;item.add(Calendar.DAY_OF_MONTH,1)){
            //add ccm 20200708 异常实时反应
            Query query_userid = new Query();
            List<CustomerInfo> customerInfoList = mongoTemplate.findAll(CustomerInfo.class);
            for (CustomerInfo customerInfo : customerInfoList)
            {
                punchcardRecordService.methodAttendance_b(item,customerInfo.getUserid());
            }
            //add ccm 20200708 异常实时反应
        }
    }

    //系统服务--取当天打卡记录//正式时间每天下午4点45分执行  GBB add
    @Scheduled(cron="0 45 16 * * ?")
    public void selectattendanceTask()throws Exception {
        selectattendance();
    }

    //系统服务--取当天打卡记录BP//正式时间每天下午4点50分执行  GBB add
    @Scheduled(cron="0 50 16 * * ?")
    public void selectattendancebpTask()throws Exception {
        selectattendancebp();
    }

    //系统服务--取当天打卡记录BP//正式时间每天下午4点50分执行  GBB add
    @Scheduled(cron="0 0 23 * * ?")
    public void selectrealwagesTask()throws Exception {
        getrealwages();
    }

    //系统服务--取打卡记录
    @Override
    public void insertattendance(int diffday,String staffId,String staffNo) throws Exception {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, diffday);
            String thisDate = DateUtil.format(cal.getTime(),"yyyy-MM-dd");
            //String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
            //删除昨天的临时数据
            punchcardrecorddetailmapper.deletetepun(thisDate,thisDate,staffNo);
            //删除昨天的临时数据
            punchcardrecorddetailmapper.deletetepundet(thisDate,thisDate,staffNo);
            //正式
            String doorIDList = "34,16,17,80,81,83,84";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
            String url = "";
            if(staffId == null){
                url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
            }
            else{
                url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList + "&staffIDList=" + staffId;
            }
            //請求接口
            ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
            Object obj = JSON.toJSON(getresult.getData());
            JSONArray jsonArray = JSONArray.parseArray(obj.toString());

            insertattendanceMethod(jsonArray);
    }

    //社员打卡记录方法
    public void insertattendanceMethod(JSONArray jsonArray) throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
        //测试接口 GBB add
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        DecimalFormat df = new DecimalFormat("######0.00");
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";

        if(jsonArray.size() > 0){
            for(Object ob : jsonArray){
                //打卡时间
                recordTime = getProperty(ob, "recordTime");
                //员工编号
                jobnumber = getProperty(ob, "staffNo");
                //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
                String eventNo = getProperty(ob, "eventNo");
                //无效-反潜回
                if(eventNo.equals("30")){
                    continue;
                }
                //PSCDC(本社人员)
                String departmentName_P = getProperty(ob, "departmentName");
                if(!departmentName_P.equals("PSDCD")){
                    continue;
                }
                //员工姓名
                String staffName = getProperty(ob, "staffName");
                //员工部门
                String departmentName = getProperty(ob, "departmentName");
                //门号
                String doorID = getProperty(ob, "doorID");
                //添加打卡详细
                PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
                //卡号
                punchcardrecorddetail.setJobnumber(jobnumber);
                punchcardrecorddetail.setDates(sfymd.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setUser_id(staffName);
                //进出状态
                punchcardrecorddetail.setEventno(eventNo);
                punchcardrecorddetail.preInsert(tokenModel);
                punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
                punchcardrecorddetailmapper.insert(punchcardrecorddetail);
                punDetaillist.add(punchcardrecorddetail);
            }
        }
        if(punDetaillist.size() > 0){
            //考勤设定
            AttendanceSetting attendancesetting = new AttendanceSetting();
            //上班开始时间
            String workshift_start = "";
            //午下班结束时间
            String closingtime_end = "";
            //午休时间开始
            String lunchbreak_start = "";
            //午休时间结束
            String lunchbreak_end = "";
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0) {
                //上班开始时间
                workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                //下班结束时间
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }

            //卡号去重得到打卡总人数
            List<PunchcardRecordDetail> punDetaillistCount = new ArrayList<PunchcardRecordDetail>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
            //String books[] = new String[punDetaillistCount.size() + 1];
            int x = 0;
            for(PunchcardRecordDetail count : punDetaillistCount){
                try{
                    x = x + 1;
                    //欠勤时间 全天
                    Double minutelogs = 0D;
                    //欠勤时间 8点到18点
                    Double minute = 0D;
                    //上午
                    Double minuteam = 0D;
                    List<PunchcardRecordDetail> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                    //所有记录时间升序
                    Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetail>() {
                        @Override
                        public int compare(PunchcardRecordDetail o1, PunchcardRecordDetail o2) {
                            Date dt1 = o1.getPunchcardrecord_date();
                            Date dt2 = o2.getPunchcardrecord_date();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    String Eventno = "";
                    //去除重复
                    for (int i = 0;i < punDetaillistx.size();i++){
                        if(i < punDetaillistx.size() ){
                            if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                                if(punDetaillistx.get(i).getEventno().equals("1")){
                                    //进进选后
                                    punDetaillistx.remove(i - 1);
                                }
                                else{
                                    //出出选前
                                    punDetaillistx.remove(i);
                                }
                            }
                        }
                        if(i < punDetaillistx.size() ){
                            Eventno = punDetaillistx.get(i).getEventno();
                        }
                    }
                    //个人所有进门记录
                    List<PunchcardRecordDetail> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                    //第一条进门记录
                    Date Time_start = null;
                    //第一条进门时间
                    long startlfirst = 0L;
                    //第一条进门时间
                    long startlfirsts = 0L;
                    if(punDetaillistevent1.size() > 0){
                        Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                        //第一条进门时间
                        startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                        startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                    }
                    //个人所有出门记录
                    List<PunchcardRecordDetail> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                    //最后一条出门记录
                    Date Time_end = null;
                    //第一条出门时间
                    long endlfirst = 0L;
                    if(punDetaillistevent2.size() > 0){
                        //最后一条出门记录
                        Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                        //第一条出门时间
                        endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                        //个人第一条考勤时出门记录的情况
                        if(endlfirst < startlfirsts){
                            punDetaillistevent2.remove(0);
                        }
                    }
                    //从第一次出门开始计算
                    for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                        if(i < punDetaillistevent1.size() - 1){
                            Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                            Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                            //个人出门时间
                            long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                            //region 日志用外出时间合计
                            //12点前出门：1、12点后进门；2、13点后进门
                            if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){

                                //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况
                                //午餐前出门时间
                                long from = startl;
                                //午餐开始时间
                                long to = sdhm.parse(lunchbreak_start).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutes;
                                //午餐结束之后进门的情况
                                if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间
                                    minutelogs = minutelogs + minutesi;
                                }
                            }

                            else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐时间出门并且午餐结束之后进门的情况
                                if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间
                                    minutelogs = minutelogs + minutesi;
                                }
                            }
                            else{
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutes;
                            }
                            //endregion

                            //region 考勤用外出时间合计
                            //个人出门时间小于8点
                            if(startl < sdhm.parse(workshift_start).getTime()){
                                //进门时间跨18点的情况
                                if(endl >= sdhm.parse(closingtime_end).getTime()){
                                    //全天欠勤
                                    minute = minute + 480D;
                                }
                                //进门时间跨13点的情况
                                else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                    minute = minute + 240D + minutesi;
                                }
                                //进门时间跨12点的情况
                                else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                    //上午欠勤
                                    minute = minute + 240D;
                                }
                                //进门时间跨8点的情况
                                else if(endl > sdhm.parse(workshift_start).getTime()){
                                    //时间出门到进门的相差分钟数
                                    Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                    minute = minute + minutes;
                                    minuteam = minuteam + minutes;
                                }
                                continue;
                            }
                            //个人出门时间晚于18点的数据排除
                            if(startl >= sdhm.parse(closingtime_end).getTime()){
                                continue;
                            }

                            //进出时间都在8点到12点之间
                            if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            //进出时间都在13点之后
                            else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //出门时间再18点之后
                                if(sdhm.parse(closingtime_end).getTime() <= endl){
                                    //18点减18之前的最后一次出门时间
                                    Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                                else{
                                    //时间出门到进门的相差分钟数
                                    Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                            }
                            //进出门跨12点的情况
                            else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                                //午餐前出门时间
                                long from = startl;
                                //午餐开始时间
                                long to = sdhm.parse(lunchbreak_start).getTime();
                                //12点减12点之前最后一次出门时间
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                                if(sdhm.parse(closingtime_end).getTime() <= endl){
                                    //18点减18之前的最后一次出门时间
                                    Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                    minute = minute + minutesi;
                                }
                                //午餐结束之后进门的情况3
                                else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间
                                    minute = minute + minutesi;
                                }
                            }
                            //跨进门13点的情况
                            else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐期间出门并且午餐结束之后进门4
                                //午餐结束时间
                                long from = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门时间
                                long to = endl;
                                if(sdhm.parse(closingtime_end).getTime() <= to){
                                    //18点减18之前的最后一次出门时间
                                    Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                                else{
                                    //时间出门到进门的相差分钟数
                                    Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                    //累计欠勤时间
                                    minute = minute + minutes;
                                }
                            }
                            //endregion
                        }
                    }
                    //添加打卡记录start
                    double minutess= minute.doubleValue();
                    minute = NumberUtil.round(minutess/60,2).doubleValue();
                    double minutesss= minuteam.doubleValue();
                    minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                    //日志用外出时长
                    double minutelogss= minutelogs.doubleValue();
                    minutelogs = NumberUtil.round(minutelogss/60,2).doubleValue();
                    //获取人员信息
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(count.getJobnumber().trim()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        if(Time_start == null){
                            Time_start = Time_end;
                        }
                        if(Time_end == null){
                            Time_end = Time_start;
                        }
                        String overtimeHours = "";
                        overtimeHours = timeLength(sdhm.format(Time_start), sdhm.format(Time_end), lunchbreak_start, lunchbreak_end);
                        if (Double.valueOf(overtimeHours) > Double.valueOf(minutelogs.toString())) {
                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) - Double.valueOf(minutelogs.toString())));
                        }
                        //打卡记录
                        PunchcardRecord punchcardrecord = new PunchcardRecord();
                        tokenModel.setUserId(customerInfo.getUserid());
                        tokenModel.setExpireDate(new Date());
                        punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                        punchcardrecord.setUser_id(customerInfo.getUserid());
                        punchcardrecord.setJobnumber(count.getJobnumber());
                        punchcardrecord.setCenter_id(customerInfo.getUserinfo().getCentername());
                        punchcardrecord.setGroup_id(customerInfo.getUserinfo().getGroupname());
                        punchcardrecord.setTeam_id(customerInfo.getUserinfo().getTeamname());
                        //外出超过15分钟的欠勤时间
                        punchcardrecord.setWorktime(minute.toString());
                        punchcardrecord.setAbsenteeismam(minuteam.toString());
                        // 日志用外出时长
                        punchcardrecord.setOutgoinghours(minutelogs.toString());
                        punchcardrecord.setTime_start(Time_start);
                        punchcardrecord.setTime_end(Time_end);
                        punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                        punchcardrecord.preInsert(tokenModel);
                        punchcardrecordMapper.insert(punchcardrecord);
                    }
                    //添加打卡记录end
                }
                catch (Exception e) {
                    System.out.println("卡号:" + count.getJobnumber() + "生成数据异常");
                    continue;
                }
            }
        }
    }
    //系统服务--取打卡记录BP
    @Override
    public void insertattendancebp(int diffday,String staffId,String staffNo) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, diffday);
        String thisDate = DateUtil.format(cal.getTime(),"yyyy-MM-dd");
        //删除昨天的临时数据
        punchcardrecorddetailbpmapper.deletetepunbp(thisDate,thisDate,staffNo);
        //删除昨天的临时数据
        punchcardrecorddetailbpmapper.deletetepundetbp(thisDate,thisDate,staffNo);

        //正式
        String doorIDList = "34,16,17,80,81,83,84";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
        String url = "";
        if(staffId == null){
            url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
        }
        else{
            url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList+ "&staffIDList=" + staffId;
        }
        //請求接口
        ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
        Object obj = JSON.toJSON(getresult.getData());
        JSONArray jsonArray = JSONArray.parseArray(obj.toString());
        insertattendancebpMethod(jsonArray,staffId,diffday);
    }

    //外协打卡记录方法
    public void insertattendancebpMethod(JSONArray jsonArray,String staffId,int diffday) throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<PunchcardRecordDetailbp> punDetaillist = new ArrayList<PunchcardRecordDetailbp>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        DecimalFormat df = new DecimalFormat("######0.00");
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";
        //外驻人员信息
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
        expatriatesinforList = expatriatesinforList.stream().filter(coi ->(!StringUtils.isNullOrEmpty(coi.getNumber()))).collect(Collectors.toList());
        if(jsonArray.size() > 0){
            for(Object ob : jsonArray){
                //打卡时间
                recordTime = getProperty(ob, "recordTime");
                //员工编号
                jobnumber = getProperty(ob, "staffNo");
                //进出状态(1，正常进入；2，正常外出;30:无效-反潜回;5/6:搬家)
                String eventNo = getProperty(ob, "eventNo");
                //无效-反潜回
                if(eventNo.equals("30") || eventNo.equals("5") || eventNo.equals("6")){
                    continue;
                }
                //外协人员
                String departmentName_P = getProperty(ob, "departmentName");
                if(departmentName_P.equals("PSDCD") || departmentName_P.equals("保洁")
                        || departmentName_P.equals("搬家") || departmentName_P.equals("访客")
                        || departmentName_P.equals("闲置") || departmentName_P.equals("300张临时卡")
                        || departmentName_P.equals("测试")){
                    continue;
                }
                //员工姓名
                String staffName = getProperty(ob, "staffName");
                //员工部门
                String departmentName = getProperty(ob, "departmentName");
                //门号
                String doorID = getProperty(ob, "doorID");
                //添加打卡详细
                PunchcardRecordDetailbp punchcardrecorddetail = new PunchcardRecordDetailbp();
                //卡号
                punchcardrecorddetail.setJobnumber(jobnumber);
                punchcardrecorddetail.setDates(sfymd.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setUser_id(staffName);
                //进出状态
                punchcardrecorddetail.setEventno(eventNo);
                punchcardrecorddetail.preInsert(tokenModel);
                punchcardrecorddetail.setPunchcardrecorddetailbp_id(UUID.randomUUID().toString());
                punchcardrecorddetailbpmapper.insert(punchcardrecorddetail);
                punDetaillist.add(punchcardrecorddetail);
            }
        }
        if(punDetaillist.size() > 0){
            //考勤设定
            AttendanceSetting attendancesetting = new AttendanceSetting();
            //上班开始时间
            String workshift_start = "";
            //午下班结束时间
            String closingtime_end = "";
            //午休时间开始
            String lunchbreak_start = "";
            //午休时间结束
            String lunchbreak_end = "";
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0) {
                //上班开始时间
                workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                //下班结束时间
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }

            //卡号去重得到打卡总人数
            List<PunchcardRecordDetailbp> punDetaillistCount = new ArrayList<PunchcardRecordDetailbp>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
            List<String> ids= new ArrayList<String>();
            int x = 0;
            for(PunchcardRecordDetailbp count : punDetaillistCount){
                try{
                    x = x + 1;
                    //欠勤时间 全天
                    Double minutelogs = 0D;
                    //欠勤时间 8点到18点
                    Double minute = 0D;
                    //上午
                    Double minuteam = 0D;
                    List<PunchcardRecordDetailbp> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                    //所有记录时间升序
                    Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetailbp>() {
                        @Override
                        public int compare(PunchcardRecordDetailbp o1, PunchcardRecordDetailbp o2) {
                            Date dt1 = o1.getPunchcardrecord_date();
                            Date dt2 = o2.getPunchcardrecord_date();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    String Eventno = "";
                    //去除重复
                    for (int i = 0;i < punDetaillistx.size();i++){
                        if(i < punDetaillistx.size() ){
                            if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                                if(punDetaillistx.get(i).getEventno().equals("1")){
                                    //进进选后
                                    punDetaillistx.remove(i - 1);
                                }
                                else{
                                    //出出选前
                                    punDetaillistx.remove(i);
                                }
                            }
                        }
                        if(i < punDetaillistx.size() ){
                            Eventno = punDetaillistx.get(i).getEventno();
                        }
                    }
                    //个人所有进门记录
                    List<PunchcardRecordDetailbp> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                    //第一条进门记录
                    Date Time_start = null;
                    //第一条进门时间
                    long startlfirst = 0L;
                    //第一条进门时间
                    long startlfirsts = 0L;
                    if(punDetaillistevent1.size() > 0){
                        Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                        //第一条进门时间
                        startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                        startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                    }
                    //个人所有出门记录
                    List<PunchcardRecordDetailbp> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                    //最后一条出门记录
                    Date Time_end = null;
                    //第一条出门时间
                    long endlfirst = 0L;
                    if(punDetaillistevent2.size() > 0){
                        //最后一条出门记录
                        Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                        //第一条出门时间
                        endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                        //个人第一条考勤时出门记录的情况
                        if(endlfirst < startlfirsts){
                            punDetaillistevent2.remove(0);
                        }
                    }
                    //从第一次出门开始计算
                    for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                        if(i < punDetaillistevent1.size() - 1){
                            Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                            Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                            //个人出门时间
                            long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                            //region 日志用外出时间合计
                            //12点前出门：1、12点后进门；2、13点后进门
                            if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){

                                //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况
                                //午餐前出门时间
                                long from = startl;
                                //午餐开始时间
                                long to = sdhm.parse(lunchbreak_start).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutes;
                                //午餐结束之后进门的情况
                                if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间
                                    minutelogs = minutelogs + minutesi;
                                }
                            }

                            else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐时间出门并且午餐结束之后进门的情况
                                if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间
                                    minutelogs = minutelogs + minutesi;
                                }
                            }
                            else{
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutes;
                            }
                            //endregion

                            //region 考勤用外出时间合计
                            //个人出门时间小于8点
                            if(startl < sdhm.parse(workshift_start).getTime()){
                                //进门时间跨18点的情况
                                if(endl >= sdhm.parse(closingtime_end).getTime()){
                                    //全天欠勤
                                    minute = minute + 480D;
                                }
                                //进门时间跨13点的情况
                                else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                    minute = minute + 240D + minutesi;
                                }
                                //进门时间跨12点的情况
                                else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                    //上午欠勤
                                    minute = minute + 240D;
                                }
                                //进门时间跨8点的情况
                                else if(endl > sdhm.parse(workshift_start).getTime()){
                                    //时间出门到进门的相差分钟数
                                    Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                    minute = minute + minutes;
                                    minuteam = minuteam + minutes;
                                }
                                continue;
                            }
                            //个人出门时间晚于18点的数据排除
                            if(startl >= sdhm.parse(closingtime_end).getTime()){
                                continue;
                            }

                            //进出时间都在8点到12点之间
                            if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            //进出时间都在13点之后
                            else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //出门时间再18点之后
                                if(sdhm.parse(closingtime_end).getTime() <= endl){
                                    //18点减18之前的最后一次出门时间
                                    Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                                else{
                                    //时间出门到进门的相差分钟数
                                    Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                            }
                            //进出门跨12点的情况
                            else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                                //午餐前出门时间
                                long from = startl;
                                //午餐开始时间
                                long to = sdhm.parse(lunchbreak_start).getTime();
                                //12点减12点之前最后一次出门时间
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                                if(sdhm.parse(closingtime_end).getTime() <= endl){
                                    //18点减18之前的最后一次出门时间
                                    Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                    minute = minute + minutesi;
                                }
                                //午餐结束之后进门的情况3
                                else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间
                                    minute = minute + minutesi;
                                }
                            }
                            //跨进门13点的情况
                            else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐期间出门并且午餐结束之后进门4
                                //午餐结束时间
                                long from = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门时间
                                long to = endl;
                                if(sdhm.parse(closingtime_end).getTime() <= to){
                                    //18点减18之前的最后一次出门时间
                                    Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                                else{
                                    //时间出门到进门的相差分钟数
                                    Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                    //累计欠勤时间
                                    minute = minute + minutes;
                                }
                            }
                            //endregion
                        }
                    }
                    //添加打卡记录start
                    double minutess= minute.doubleValue();
                    minute = NumberUtil.round(minutess/60,2).doubleValue();
                    double minutesss= minuteam.doubleValue();
                    minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                    //日志用外出时长
                    double minutelogss= minutelogs.doubleValue();
                    minutelogs = NumberUtil.round(minutelogss/60,2).doubleValue();
                    List<Expatriatesinfor> exList = expatriatesinforList.stream().filter(coi ->(coi.getNumber().contains(count.getJobnumber().trim()))).collect(Collectors.toList());
                    if (exList.size() > 0) {
                        if(Time_start == null){
                            Time_start = Time_end;
                        }
                        if(Time_end == null){
                            Time_end = Time_start;
                        }
                        String overtimeHours = "";
                        overtimeHours = timeLength(sdhm.format(Time_start), sdhm.format(Time_end), lunchbreak_start, lunchbreak_end);
                        if (Double.valueOf(overtimeHours) > Double.valueOf(minutelogs.toString())) {
                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) - Double.valueOf(minutelogs.toString())));
                        }
                        //打卡记录
                        PunchcardRecordbp punchcardrecord = new PunchcardRecordbp();
                        tokenModel.setUserId(exList.get(0).getAccount());
                        tokenModel.setExpireDate(new Date());
                        punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                        punchcardrecord.setUser_id(exList.get(0).getAccount());
                        punchcardrecord.setJobnumber(count.getJobnumber());
                        punchcardrecord.setGroup_id(exList.get(0).getGroup_id());
                        //外出超过15分钟的欠勤时间
                        punchcardrecord.setWorktime(minute.toString());
                        punchcardrecord.setAbsenteeismam(minuteam.toString());
                        // 日志用外出时长
                        punchcardrecord.setOutgoinghours(overtimeHours);
                        punchcardrecord.setTime_start(Time_start);
                        punchcardrecord.setTime_end(Time_end);
                        punchcardrecord.setPunchcardrecordbp_id(UUID.randomUUID().toString());
                        punchcardrecord.preInsert(tokenModel);
                        punchcardrecordbpMapper.insert(punchcardrecord);

                        //创建考勤数据
                        Attendancebp attendance = new Attendancebp();
                        attendance.setUser_id(exList.get(0).getAccount());
                        attendance.setDates(sfymd.parse(recordTime));

                        attendance.setNormal("8");
                        // 设置统计外出的时间
                        attendance.setAbsenteeism(minute.toString());
                        // 日志用外出时长
                        attendance.setOutgoinghours(overtimeHours);
                        attendance.setGroup_id(exList.get(0).getGroup_id());

                        attendance.setYears(DateUtil.format(sfymd.parse(recordTime),"YYYY").toString());
                        attendance.setMonths(DateUtil.format(sfymd.parse(recordTime),"MM").toString());
                        attendance.setAttendancebpid(UUID.randomUUID().toString());
                        attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                        attendance.preInsert(tokenModel);
                        attendancebpMapper.insert(attendance);
                        ids.add(exList.get(0).getAccount());
                    }
                    //添加打卡记录end
                }
                catch (Exception e) {
                    System.out.println("卡号:" + count.getJobnumber() + "生成数据异常");
                    continue;
                }

            }
            if(staffId == null){
                List<Expatriatesinfor> inforlist = punchcardrecorddetailbpmapper.getexpatriatesinforbp(ids);
                for (Expatriatesinfor Expatriatesinfor : inforlist){
                    try{
                        tokenModel.setUserId(Expatriatesinfor.getAccount());
                        tokenModel.setExpireDate(new Date());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_YEAR, diffday);
                        //插入没有打卡记录的员工的考勤
                        Attendancebp attendance = new Attendancebp();
                        attendance.setAbsenteeism("8");
                        attendance.setNormal("0");
                        // 日志用外出时长
                        attendance.setOutgoinghours("0");
                        attendance.setAttendancebpid(UUID.randomUUID().toString());
                        attendance.setGroup_id(Expatriatesinfor.getGroup_id());
                        attendance.setUser_id(Expatriatesinfor.getAccount());
                        attendance.setDates(calendar.getTime());
                        attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                        attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                        attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                        attendance.preInsert(tokenModel);
                        attendancebpMapper.insert(attendance);
                    }
                    catch (Exception e) {
                        System.out.println("卡号:" + Expatriatesinfor.getNumber() + "生成数据异常");
                        continue;
                    }
                }
            }
        }
    }
    @Override
    public void insertpunchcard(int diffday) throws Exception {
        //处理异常和加班数据
        //上月1号
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(new Date());
        calStart.add(Calendar.MONTH, -1);
        calStart.set(Calendar.DAY_OF_MONTH, 1);

        //本月末日
        Calendar calend = Calendar.getInstance();
        calend.setTime(new Date());
        int maxCurrentMonthDay=calend.getActualMaximum(Calendar.DAY_OF_MONTH);
        calend.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);

        for(Calendar item = calStart;item.compareTo(calend) <= 0;item.add(Calendar.DAY_OF_MONTH,1)){
            //add ccm 20200708 异常实时反应
            Query query_userid = new Query();
            List<CustomerInfo> customerInfoList = mongoTemplate.findAll(CustomerInfo.class);
            for (CustomerInfo customerInfo : customerInfoList)
            {
                punchcardRecordService.methodAttendance_b(item,customerInfo.getUserid());
            }
            //add ccm 20200708 异常实时反应
        }

    }

    //系统服务--获取指定人和日期的打卡记录
    @Override
    public void insertHistoricalCard(String strStartDate,String strendDate,String strFlg,String staffNo) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String [] jobnumber = staffNo.split(",");
        for(int i=0;i<jobnumber.length;i++){

            //社内人员//
            if(strFlg.equals("1")){
                //删除昨天的临时数据
                punchcardrecorddetailmapper.deletetepun(strStartDate,strendDate,jobnumber[i]);
                //删除昨天的临时数据
                punchcardrecorddetailmapper.deletetepundet(strStartDate,strendDate,jobnumber[i]);
            }
            else{
                //外驻人员
                //删除昨天的临时数据
                punchcardrecorddetailbpmapper.deletetepunbp(strStartDate,strendDate,jobnumber[i]);
                //删除昨天的临时数据
                punchcardrecorddetailbpmapper.deletetepundetbp(strStartDate,strendDate,jobnumber[i]);
            }
            Calendar cl1 = Calendar.getInstance();
            Calendar cl2 = Calendar.getInstance();
            cl1.setTime(df.parse(strStartDate));
            cl2.setTime(df.parse(strendDate));
            while (cl1.compareTo(cl2) <= 0) {

                //打卡时间
                String StartDate = df.format(cl1.getTime());
                //正式
                String doorIDList = "34,16,17,80,81,83,84";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
                String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByStaffNoList?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + StartDate + "&endDate=" + StartDate + "&doorIDList=" + doorIDList + "&StaffNoList=" + jobnumber[i];

                //請求接口
                ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
                Object obj = JSON.toJSON(getresult.getData());
                JSONArray jsonArray = JSONArray.parseArray(obj.toString());
                if(strFlg.equals("1")){
                    insertattendanceMethod(jsonArray);
                }
                else{
                    insertattendancebpMethod(jsonArray,"",0);
                }
                cl1.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    }

    @Override
    public void getattendanceByuser(String userid) throws Exception {
        //处理异常和加班数据
        //上月1号
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(new Date());
        calStart.add(Calendar.MONTH, -1);
        calStart.set(Calendar.DAY_OF_MONTH, 1);

        //本月末日
        Calendar calend = Calendar.getInstance();
        calend.setTime(new Date());
        int maxCurrentMonthDay=calend.getActualMaximum(Calendar.DAY_OF_MONTH);
        calend.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);

        for(Calendar item = calStart;item.compareTo(calend) <= 0;item.add(Calendar.DAY_OF_MONTH,1)){
            //add ccm 20200708 异常实时反应
//            Query query_userid = new Query();
//            List<CustomerInfo> customerInfoList = mongoTemplate.findAll(CustomerInfo.class);
//            for (CustomerInfo customerInfo : customerInfoList)
//            {
                punchcardRecordService.methodAttendance_b(item,userid);
//            }
            //add ccm 20200708 异常实时反应
        }

    }

    //取object的值
    private String getProperty(Object o, String key) throws Exception{
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(o, key);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    //系统服务--取当天打卡记录//正式时间每天下午4点45分执行  GBB add
    public void selectattendance() throws Exception {
            TokenModel tokenModel = new TokenModel();
            List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
            //测试接口 GBB add
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
            SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
            DecimalFormat df = new DecimalFormat("######0.00");
            String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
            //正式
            String doorIDList = "34,16,17,80,81,83,84";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
            String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
            //請求接口
            ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
            Object obj = JSON.toJSON(getresult.getData());
            JSONArray jsonArray = JSONArray.parseArray(obj.toString());
            //打卡时间
            String recordTime = "";
            //员工编号
            String jobnumber = "";
            if(jsonArray.size() > 0){
                for(Object ob : jsonArray){
                    //打卡时间
                    recordTime = getProperty(ob, "recordTime");
                    //员工编号
                    jobnumber = getProperty(ob, "staffNo");
                    //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
                    String eventNo = getProperty(ob, "eventNo");
                    //无效-反潜回
                    if(eventNo.equals("30")){
                        continue;
                    }
                    //PSCDC(本社人员)
                    String departmentName_P = getProperty(ob, "departmentName");
                    if(!departmentName_P.equals("PSDCD")){
                        continue;
                    }
                    //员工姓名
                    String staffName = getProperty(ob, "staffName");
                    //员工部门
                    String departmentName = getProperty(ob, "departmentName");
                    //门号
                    String doorID = getProperty(ob, "doorID");
                    //添加打卡详细
                    PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
                    //卡号
                    punchcardrecorddetail.setJobnumber(jobnumber);
                    punchcardrecorddetail.setDates(sfymd.parse(recordTime));
                    //打卡时间
                    punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
                    //打卡时间
                    punchcardrecorddetail.setUser_id(staffName);
                    //进出状态
                    punchcardrecorddetail.setEventno(eventNo);
                    punchcardrecorddetail.preInsert(tokenModel);
                    punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
                    punchcardrecorddetailmapper.insert(punchcardrecorddetail);
                    punDetaillist.add(punchcardrecorddetail);
                }
            }
            if(punDetaillist.size() > 0){
                //考勤设定
                AttendanceSetting attendancesetting = new AttendanceSetting();
                //上班开始时间
                String workshift_start = "";
                //午下班结束时间
                String closingtime_end = "";
                //午休时间开始
                String lunchbreak_start = "";
                //午休时间结束
                String lunchbreak_end = "";
                List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
                if(attendancesettinglist.size() > 0) {
                    //上班开始时间
                    workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                    //下班结束时间
                    closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                    //午休时间开始
                    lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                    //午休时间结束
                    lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
                }

                //卡号去重得到打卡总人数
                List<PunchcardRecordDetail> punDetaillistCount = new ArrayList<PunchcardRecordDetail>();
                punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
                int x = 0;
                for(PunchcardRecordDetail count : punDetaillistCount){
                    x = x + 1;
                    //欠勤时间 8点到18点
                    Double minute = 0D;
                    //上午
                    Double minuteam = 0D;
                    List<PunchcardRecordDetail> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                    //所有记录时间升序
                    Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetail>() {
                        @Override
                        public int compare(PunchcardRecordDetail o1, PunchcardRecordDetail o2) {
                            Date dt1 = o1.getPunchcardrecord_date();
                            Date dt2 = o2.getPunchcardrecord_date();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    String Eventno = "";
                    //去除重复
                    for (int i = 0;i < punDetaillistx.size();i++){
                        if(i < punDetaillistx.size() ){
                            if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                                if(punDetaillistx.get(i).getEventno().equals("1")){
                                    //进进选后
                                    punDetaillistx.remove(i - 1);
                                }
                                else{
                                    //出出选前
                                    punDetaillistx.remove(i);
                                }
                            }
                        }
                        if(i < punDetaillistx.size() ){
                            Eventno = punDetaillistx.get(i).getEventno();
                        }
                    }
                    //个人所有进门记录
                    List<PunchcardRecordDetail> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                    //第一条进门记录
                    Date Time_start = null;
                    //第一条进门时间
                    long startlfirst = 0L;
                    //第一条进门时间
                    long startlfirsts = 0L;
                    if(punDetaillistevent1.size() > 0){
                        Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                        //第一条进门时间
                        startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                        startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                    }
                    //个人所有出门记录
                    List<PunchcardRecordDetail> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                    //最后一条出门记录
                    Date Time_end = null;
                    //第一条出门时间
                    long endlfirst = 0L;
                    if(punDetaillistevent2.size() > 0){
                        //最后一条出门记录
                        Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                        //第一条出门时间
                        endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                        //个人第一条考勤时出门记录的情况
                        if(endlfirst < startlfirsts){
                            punDetaillistevent2.remove(0);
                        }
                    }
                    //从第一次出门开始计算
                    for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                        if(i < punDetaillistevent1.size() - 1){
                            Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                            Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                            //个人出门时间
                            long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                            //region 考勤用外出时间合计
                            //个人出门时间小于8点
                            if(startl < sdhm.parse(workshift_start).getTime()){
                                //进门时间跨18点的情况
                                if(endl >= sdhm.parse(closingtime_end).getTime()){
                                    //全天欠勤
                                    minute = minute + 480D;
                                }
                                //进门时间跨13点的情况
                                else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                    minute = minute + 240D + minutesi;
                                }
                                //进门时间跨12点的情况
                                else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                    //上午欠勤
                                    minute = minute + 240D;
                                }
                                //进门时间跨8点的情况
                                else if(endl > sdhm.parse(workshift_start).getTime()){
                                    //时间出门到进门的相差分钟数
                                    Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                    minute = minute + minutes;
                                    minuteam = minuteam + minutes;
                                }
                                continue;
                            }
                            //个人出门时间晚于18点的数据排除
                            if(startl >= sdhm.parse(closingtime_end).getTime()){
                                continue;
                            }

                            //进出时间都在8点到12点之间
                            if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            //进出时间都在13点之后
                            else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(DateStart)).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(DateEnd)).getTime();
                                //出门时间再18点之后
                                if(sdhm.parse(closingtime_end).getTime() <= endl){
                                    //18点减18之前的最后一次出门时间
                                    Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                                else{
                                    //时间出门到进门的相差分钟数
                                    Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                            }
                            //进出门跨12点的情况
                            else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                                //午餐前出门时间
                                long from = startl;
                                //午餐开始时间
                                long to = sdhm.parse(lunchbreak_start).getTime();
                                //12点减12点之前最后一次出门时间
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                                if(sdhm.parse(closingtime_end).getTime() <= endl){
                                    //18点减18之前的最后一次出门时间
                                    Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                    minute = minute + minutesi;
                                }
                                //午餐结束之后进门的情况3
                                else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                    //累计欠勤时间
                                    minute = minute + minutesi;
                                }
                            }
                            //跨进门13点的情况
                            else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐期间出门并且午餐结束之后进门4
                                //午餐结束时间
                                long from = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门时间
                                long to = endl;
                                if(sdhm.parse(closingtime_end).getTime() <= to){
                                    //18点减18之前的最后一次出门时间
                                    Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                    minute = minute + minutes;
                                }
                                else{
                                    //时间出门到进门的相差分钟数
                                    Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                    //累计欠勤时间
                                    minute = minute + minutes;
                                }
                            }
                            //endregion
                        }
                    }
                    //添加打卡记录start
                    double minutess= minute.doubleValue();
                    minute = NumberUtil.round(minutess/60,2).doubleValue();
                    double minutesss= minuteam.doubleValue();
                    minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                    //获取人员信息
                    Query query = new Query();
                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(count.getJobnumber().trim()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
                        //打卡记录
                        PunchcardRecord punchcardrecord = new PunchcardRecord();
                        tokenModel.setUserId(customerInfo.getUserid());
                        tokenModel.setExpireDate(new Date());
                        punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                        punchcardrecord.setUser_id(customerInfo.getUserid());
                        punchcardrecord.setJobnumber(count.getJobnumber());
                        punchcardrecord.setCenter_id(customerInfo.getUserinfo().getCentername());
                        punchcardrecord.setGroup_id(customerInfo.getUserinfo().getGroupname());
                        punchcardrecord.setTeam_id(customerInfo.getUserinfo().getTeamname());
                        //外出超过15分钟的欠勤时间
                        punchcardrecord.setWorktime(minute.toString());
                        punchcardrecord.setAbsenteeismam(minuteam.toString());
                        // 日志用外出时长
                        punchcardrecord.setOutgoinghours("0");
                        punchcardrecord.setTime_start(Time_start);
                        punchcardrecord.setTime_end(Time_end);
                        punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                        punchcardrecord.preInsert(tokenModel);
                        punchcardrecordMapper.insert(punchcardrecord);
                    }
                    //添加打卡记录end
                }
            }
    }

    //系统服务--取当天打卡记录BP//正式时间每天下午4点50分执行  GBB add
    public void selectattendancebp() throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<PunchcardRecordDetailbp> punDetaillist = new ArrayList<PunchcardRecordDetailbp>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        DecimalFormat df = new DecimalFormat("######0.00");
        String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
        expatriatesinforList = expatriatesinforList.stream().filter(coi ->(!StringUtils.isNullOrEmpty(coi.getNumber()))).collect(Collectors.toList());
        //正式
        String doorIDList = "34,16,17,80,81,83,84";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
        String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
        //請求接口
        ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
        Object obj = JSON.toJSON(getresult.getData());
        JSONArray jsonArray = JSONArray.parseArray(obj.toString());
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";
        if(jsonArray.size() > 0){
            for(Object ob : jsonArray){
                //打卡时间
                recordTime = getProperty(ob, "recordTime");
                //员工编号
                jobnumber = getProperty(ob, "staffNo");
                //进出状态(1，正常进入；2，正常外出;30:无效-反潜回;5/6:搬家)
                String eventNo = getProperty(ob, "eventNo");
                //无效-反潜回
                if(eventNo.equals("30") || eventNo.equals("5") || eventNo.equals("6")){
                    continue;
                }
                //外协人员
                String departmentName_P = getProperty(ob, "departmentName");
                if(departmentName_P.equals("PSDCD") || departmentName_P.equals("保洁")
                        || departmentName_P.equals("搬家") || departmentName_P.equals("访客")
                        || departmentName_P.equals("闲置") || departmentName_P.equals("300张临时卡")
                        || departmentName_P.equals("测试")){
                    continue;
                }
                //员工姓名
                String staffName = getProperty(ob, "staffName");
                //员工部门
                String departmentName = getProperty(ob, "departmentName");
                //门号
                String doorID = getProperty(ob, "doorID");
                //添加打卡详细
                PunchcardRecordDetailbp punchcardrecorddetail = new PunchcardRecordDetailbp();
                //卡号
                punchcardrecorddetail.setJobnumber(jobnumber);
                punchcardrecorddetail.setDates(sfymd.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
                //打卡时间
                punchcardrecorddetail.setUser_id(staffName);
                //进出状态
                punchcardrecorddetail.setEventno(eventNo);
                punchcardrecorddetail.preInsert(tokenModel);
                punchcardrecorddetail.setPunchcardrecorddetailbp_id(UUID.randomUUID().toString());
                punchcardrecorddetailbpmapper.insert(punchcardrecorddetail);
                punDetaillist.add(punchcardrecorddetail);
            }
        }
        if(punDetaillist.size() > 0){
            //考勤设定
            AttendanceSetting attendancesetting = new AttendanceSetting();
            //上班开始时间
            String workshift_start = "";
            //午下班结束时间
            String closingtime_end = "";
            //午休时间开始
            String lunchbreak_start = "";
            //午休时间结束
            String lunchbreak_end = "";
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0) {
                //上班开始时间
                workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                //下班结束时间
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }

            //卡号去重得到打卡总人数
            List<PunchcardRecordDetailbp> punDetaillistCount = new ArrayList<PunchcardRecordDetailbp>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
            int x = 0;
            for(PunchcardRecordDetailbp count : punDetaillistCount){
                x = x + 1;
                //欠勤时间 8点到18点
                Double minute = 0D;
                //上午
                Double minuteam = 0D;
                List<PunchcardRecordDetailbp> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                //所有记录时间升序
                Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetailbp>() {
                    @Override
                    public int compare(PunchcardRecordDetailbp o1, PunchcardRecordDetailbp o2) {
                        Date dt1 = o1.getPunchcardrecord_date();
                        Date dt2 = o2.getPunchcardrecord_date();
                        if (dt1.getTime() > dt2.getTime()) {
                            return 1;
                        } else if (dt1.getTime() < dt2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

                String Eventno = "";
                //去除重复
                for (int i = 0;i < punDetaillistx.size();i++){
                    if(i < punDetaillistx.size() ){
                        if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                            if(punDetaillistx.get(i).getEventno().equals("1")){
                                //进进选后
                                punDetaillistx.remove(i - 1);
                            }
                            else{
                                //出出选前
                                punDetaillistx.remove(i);
                            }
                        }
                    }
                    if(i < punDetaillistx.size() ){
                        Eventno = punDetaillistx.get(i).getEventno();
                    }
                }
                //个人所有进门记录
                List<PunchcardRecordDetailbp> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                //第一条进门记录
                Date Time_start = null;
                //第一条进门时间
                long startlfirst = 0L;
                //第一条进门时间
                long startlfirsts = 0L;
                if(punDetaillistevent1.size() > 0){
                    Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                    //第一条进门时间
                    startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                    startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                }
                //个人所有出门记录
                List<PunchcardRecordDetailbp> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                //最后一条出门记录
                Date Time_end = null;
                //第一条出门时间
                long endlfirst = 0L;
                if(punDetaillistevent2.size() > 0){
                    //最后一条出门记录
                    Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                    //第一条出门时间
                    endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                    //个人第一条考勤时出门记录的情况
                    if(endlfirst < startlfirsts){
                        punDetaillistevent2.remove(0);
                    }
                }
                //从第一次出门开始计算
                for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                    if(i < punDetaillistevent1.size() - 1){
                        Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                        Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                        //个人出门时间
                        long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                        //个人出门之后再次进门时间
                        long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                        //region 考勤用外出时间合计
                        //个人出门时间小于8点
                        if(startl < sdhm.parse(workshift_start).getTime()){
                            //进门时间跨18点的情况
                            if(endl >= sdhm.parse(closingtime_end).getTime()){
                                //全天欠勤
                                minute = minute + 480D;
                            }
                            //进门时间跨13点的情况
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                minute = minute + 240D + minutesi;
                            }
                            //进门时间跨12点的情况
                            else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                //上午欠勤
                                minute = minute + 240D;
                            }
                            //进门时间跨8点的情况
                            else if(endl > sdhm.parse(workshift_start).getTime()){
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            continue;
                        }
                        //个人出门时间晚于18点的数据排除
                        if(startl >= sdhm.parse(closingtime_end).getTime()){
                            continue;
                        }

                        //进出时间都在8点到12点之间
                        if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                        }
                        //进出时间都在13点之后
                        else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //出门时间再18点之后
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                        }
                        //进出门跨12点的情况
                        else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //12点减12点之前最后一次出门时间
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                minute = minute + minutesi;
                            }
                            //午餐结束之后进门的情况3
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutesi;
                            }
                        }
                        //跨进门13点的情况
                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                            //午餐期间出门并且午餐结束之后进门4
                            //午餐结束时间
                            long from = sdhm.parse(lunchbreak_end).getTime();
                            //午餐结束之后进门时间
                            long to = endl;
                            if(sdhm.parse(closingtime_end).getTime() <= to){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutes;
                            }
                        }
                        //endregion
                    }
                }
                //添加打卡记录start
                double minutess= minute.doubleValue();
                minute = NumberUtil.round(minutess/60,2).doubleValue();
                double minutesss= minuteam.doubleValue();
                minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                List<Expatriatesinfor> exList = expatriatesinforList.stream().filter(coi ->(coi.getNumber().contains(count.getJobnumber().trim()))).collect(Collectors.toList());
                if (exList.size() > 0) {
                    if(Time_start == null){
                        Time_start = Time_end;
                    }
                    if(Time_end == null){
                        Time_end = Time_start;
                    }
                    //打卡记录
                    PunchcardRecordbp punchcardrecord = new PunchcardRecordbp();
                    tokenModel.setUserId(exList.get(0).getAccount());
                    tokenModel.setExpireDate(new Date());
                    punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                    punchcardrecord.setUser_id(exList.get(0).getAccount());
                    punchcardrecord.setJobnumber(count.getJobnumber());
                    punchcardrecord.setGroup_id(exList.get(0).getGroup_id());
                    //外出超过15分钟的欠勤时间
                    punchcardrecord.setWorktime(minute.toString());
                    punchcardrecord.setAbsenteeismam(minuteam.toString());
                    punchcardrecord.setTime_start(Time_start);
                    punchcardrecord.setTime_end(Time_end);
                    punchcardrecord.setPunchcardrecordbp_id(UUID.randomUUID().toString());
                    punchcardrecord.preInsert(tokenModel);
                    punchcardrecordbpMapper.insert(punchcardrecord);
                }
                //添加打卡记录end
            }
        }
    }

    //获取打卡记录（参数）
    @Override
    public void getPunchcard(List<Punchcard> Punchcardlist) throws Exception {
        TokenModel tokenModel = new TokenModel();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        DecimalFormat df = new DecimalFormat("######0.00");
        List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";
        for(Punchcard punchcard : Punchcardlist){
            //打卡时间
            recordTime = punchcard.getRecordTime();
            //员工编号
            jobnumber = punchcard.getStaffNo();
            //进出状态(1，正常进入；2，正常外出;30:无效-反潜回;5/6:搬家)
            String eventNo = punchcard.getEventNo();
            //无效-反潜回
            if(eventNo.equals("30") || eventNo.equals("5") || eventNo.equals("6")){
                continue;
            }
            //外协人员
            String departmentName_P = punchcard.getDepartmentName();
            if(!departmentName_P.equals("PSDCD")){
                continue;
            }
            //员工姓名
            String staffName = punchcard.getStaffName();
            //员工部门
            String departmentName = punchcard.getDepartmentName();
            //门号
            String doorID = punchcard.getDoorID();
            //添加打卡详细
            PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
            //卡号
            punchcardrecorddetail.setJobnumber(jobnumber);
            punchcardrecorddetail.setDates(sfymd.parse(recordTime));
            //打卡时间
            punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
            //打卡时间
            punchcardrecorddetail.setUser_id(staffName);
            //部门
            punchcardrecorddetail.setCenter_id(departmentName_P);
            //进出状态
            punchcardrecorddetail.setEventno(eventNo);
            punchcardrecorddetail.preInsert(tokenModel);
            punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
            punchcardrecorddetailmapper.insert(punchcardrecorddetail);
            punDetaillist.add(punchcardrecorddetail);
        }
        if(punDetaillist.size() > 0){
            //考勤设定
            AttendanceSetting attendancesetting = new AttendanceSetting();
            //上班开始时间
            String workshift_start = "";
            //午下班结束时间
            String closingtime_end = "";
            //午休时间开始
            String lunchbreak_start = "";
            //午休时间结束
            String lunchbreak_end = "";
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0) {
                //上班开始时间
                workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                //下班结束时间
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }

            //卡号去重得到打卡总人数
            List<PunchcardRecordDetail> punDetaillistCount = new ArrayList<PunchcardRecordDetail>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
            //String books[] = new String[punDetaillistCount.size() + 1];
            int x = 0;
            for(PunchcardRecordDetail count : punDetaillistCount){
                x = x + 1;
                //欠勤时间 全天
                Double minutelogs = 0D;
                //欠勤时间 8点到18点
                Double minute = 0D;
                //上午
                Double minuteam = 0D;
                List<PunchcardRecordDetail> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                //所有记录时间升序
                Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetail>() {
                    @Override
                    public int compare(PunchcardRecordDetail o1, PunchcardRecordDetail o2) {
                        Date dt1 = o1.getPunchcardrecord_date();
                        Date dt2 = o2.getPunchcardrecord_date();
                        if (dt1.getTime() > dt2.getTime()) {
                            return 1;
                        } else if (dt1.getTime() < dt2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

                String Eventno = "";
                //去除重复
                for (int i = 0;i < punDetaillistx.size();i++){
                    if(i < punDetaillistx.size() ){
                        if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                            if(punDetaillistx.get(i).getEventno().equals("1")){
                                //进进选后
                                punDetaillistx.remove(i - 1);
                            }
                            else{
                                //出出选前
                                punDetaillistx.remove(i);
                            }
                        }
                    }
                    if(i < punDetaillistx.size() ){
                        Eventno = punDetaillistx.get(i).getEventno();
                    }
                }
                //个人所有进门记录
                List<PunchcardRecordDetail> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                //第一条进门记录
                Date Time_start = null;
                //第一条进门时间
                long startlfirst = 0L;
                //第一条进门时间
                long startlfirsts = 0L;
                if(punDetaillistevent1.size() > 0){
                    Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                    //第一条进门时间
                    startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                    startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                }
                //个人所有出门记录
                List<PunchcardRecordDetail> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                //最后一条出门记录
                Date Time_end = null;
                //第一条出门时间
                long endlfirst = 0L;
                if(punDetaillistevent2.size() > 0){
                    //最后一条出门记录
                    Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                    //第一条出门时间
                    endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                    //个人第一条考勤时出门记录的情况
                    if(endlfirst < startlfirsts){
                        punDetaillistevent2.remove(0);
                    }
                }
                //从第一次出门开始计算
                for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                    if(i < punDetaillistevent1.size() - 1){
                        Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                        Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                        //个人出门时间
                        long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                        //个人出门之后再次进门时间
                        long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                        //region 日志用外出时间合计
                        //12点前出门：1、12点后进门；2、13点后进门
                        if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){

                            //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minutelogs = minutelogs + minutes;
                            //午餐结束之后进门的情况
                            if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutesi;
                            }
                        }

                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime()){
                            //午餐时间出门并且午餐结束之后进门的情况
                            if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutesi;
                            }
                        }
                        else{
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minutelogs = minutelogs + minutes;
                        }
                        //endregion

                        //region 考勤用外出时间合计
                        //个人出门时间小于8点
                        if(startl < sdhm.parse(workshift_start).getTime()){
                            //进门时间跨18点的情况
                            if(endl >= sdhm.parse(closingtime_end).getTime()){
                                //全天欠勤
                                minute = minute + 480D;
                            }
                            //进门时间跨13点的情况
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                minute = minute + 240D + minutesi;
                            }
                            //进门时间跨12点的情况
                            else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                //上午欠勤
                                minute = minute + 240D;
                            }
                            //进门时间跨8点的情况
                            else if(endl > sdhm.parse(workshift_start).getTime()){
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            continue;
                        }
                        //个人出门时间晚于18点的数据排除
                        if(startl >= sdhm.parse(closingtime_end).getTime()){
                            continue;
                        }

                        //进出时间都在8点到12点之间
                        if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                        }
                        //进出时间都在13点之后
                        else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //出门时间再18点之后
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                        }
                        //进出门跨12点的情况
                        else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //12点减12点之前最后一次出门时间
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                minute = minute + minutesi;
                            }
                            //午餐结束之后进门的情况3
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutesi;
                            }
                        }
                        //跨进门13点的情况
                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                            //午餐期间出门并且午餐结束之后进门4
                            //午餐结束时间
                            long from = sdhm.parse(lunchbreak_end).getTime();
                            //午餐结束之后进门时间
                            long to = endl;
                            if(sdhm.parse(closingtime_end).getTime() <= to){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutes;
                            }
                        }
                        //endregion
                    }
                }
                //添加打卡记录start
                double minutess= minute.doubleValue();
                minute = NumberUtil.round(minutess/60,2).doubleValue();
                double minutesss= minuteam.doubleValue();
                minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                //日志用外出时长
                double minutelogss= minutelogs.doubleValue();
                minutelogs = NumberUtil.round(minutelogss/60,2).doubleValue();
                //获取人员信息
                Query query = new Query();
                query.addCriteria(Criteria.where("userinfo.jobnumber").is(count.getJobnumber().trim()));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
                    if(Time_start == null){
                        Time_start = Time_end;
                    }
                    if(Time_end == null){
                        Time_end = Time_start;
                    }
                    String overtimeHours = "";
                    overtimeHours = timeLength(sdhm.format(Time_start), sdhm.format(Time_end), lunchbreak_start, lunchbreak_end);
                    if (Double.valueOf(overtimeHours) > Double.valueOf(minutelogs.toString())) {
                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) - Double.valueOf(minutelogs.toString())));
                    }
                    //打卡记录
                    PunchcardRecord punchcardrecord = new PunchcardRecord();
                    tokenModel.setUserId(customerInfo.getUserid());
                    tokenModel.setExpireDate(new Date());
                    punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                    punchcardrecord.setUser_id(customerInfo.getUserid());
                    punchcardrecord.setJobnumber(count.getJobnumber());
                    punchcardrecord.setCenter_id(customerInfo.getUserinfo().getCentername());
                    punchcardrecord.setGroup_id(customerInfo.getUserinfo().getGroupname());
                    punchcardrecord.setTeam_id(customerInfo.getUserinfo().getTeamname());
                    //外出超过15分钟的欠勤时间
                    punchcardrecord.setWorktime(minute.toString());
                    punchcardrecord.setAbsenteeismam(minuteam.toString());
                    // 日志用外出时长
                    punchcardrecord.setOutgoinghours(minutelogs.toString());
                    punchcardrecord.setTime_start(Time_start);
                    punchcardrecord.setTime_end(Time_end);
                    punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                    punchcardrecord.preInsert(tokenModel);
                    punchcardrecordMapper.insert(punchcardrecord);

//                    //创建考勤数据
//                    Attendance attendance = new Attendance();
//                    attendance.setUser_id(customerInfo.getUserid());
//                    attendance.setDates(sfymd.parse(recordTime));
//
//                    attendance.setNormal("8");
//                    // 设置统计外出的时间
//                    attendance.setAbsenteeism(minute.toString());
//                    attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
//                    attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
//                    attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
//
//                    attendance.setYears(DateUtil.format(sfymd.parse(recordTime),"YYYY").toString());
//                    attendance.setMonths(DateUtil.format(sfymd.parse(recordTime),"MM").toString());
//                    attendance.setAttendanceid(UUID.randomUUID().toString());
//                    attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
//                    attendance.preInsert(tokenModel);
//                    attendanceMapper.insert(attendance);
                    //books[x] = customerInfo.getUserid();
                }
                //添加打卡记录end
            }
        }
    }

    //获取打卡记录bp（参数）
    @Override
    public void getPunchcardbp(List<Punchcard> Punchcardlist) throws Exception {
        TokenModel tokenModel = new TokenModel();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        DecimalFormat df = new DecimalFormat("######0.00");
        List<PunchcardRecordDetailbp> punDetaillist = new ArrayList<PunchcardRecordDetailbp>();
        //外驻人员信息
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
        expatriatesinforList = expatriatesinforList.stream().filter(coi ->(!StringUtils.isNullOrEmpty(coi.getNumber()))).collect(Collectors.toList());
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";
        for(Punchcard punchcard : Punchcardlist){
            //打卡时间
            recordTime = punchcard.getRecordTime();
            //员工编号
            jobnumber = punchcard.getStaffNo();
            //进出状态(1，正常进入；2，正常外出;30:无效-反潜回;5/6:搬家)
            String eventNo = punchcard.getEventNo();
            //无效-反潜回
            if(eventNo.equals("30") || eventNo.equals("5") || eventNo.equals("6")){
                continue;
            }
            //外协人员
            String departmentName_P = punchcard.getDepartmentName();
            if(departmentName_P.equals("PSDCD") || departmentName_P.equals("保洁")
                    || departmentName_P.equals("搬家") || departmentName_P.equals("访客")
                    || departmentName_P.equals("闲置") || departmentName_P.equals("300张临时卡")
                    || departmentName_P.equals("测试")){
                continue;
            }
            //员工姓名
            String staffName = punchcard.getStaffName();
            //员工部门
            String departmentName = punchcard.getDepartmentName();
            //门号
            String doorID = punchcard.getDoorID();
            //添加打卡详细
            PunchcardRecordDetailbp punchcardrecorddetail = new PunchcardRecordDetailbp();
            //卡号
            punchcardrecorddetail.setJobnumber(jobnumber);
            punchcardrecorddetail.setDates(sfymd.parse(recordTime));
            //打卡时间
            punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
            //打卡时间
            punchcardrecorddetail.setUser_id(staffName);
            //部门
            punchcardrecorddetail.setCenter_id(departmentName_P);
            //进出状态
            punchcardrecorddetail.setEventno(eventNo);
            punchcardrecorddetail.preInsert(tokenModel);
            punchcardrecorddetail.setPunchcardrecorddetailbp_id(UUID.randomUUID().toString());
            punchcardrecorddetailbpmapper.insert(punchcardrecorddetail);
            punDetaillist.add(punchcardrecorddetail);
        }
        if(punDetaillist.size() > 0){
            //考勤设定
            AttendanceSetting attendancesetting = new AttendanceSetting();
            //上班开始时间
            String workshift_start = "";
            //午下班结束时间
            String closingtime_end = "";
            //午休时间开始
            String lunchbreak_start = "";
            //午休时间结束
            String lunchbreak_end = "";
            List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
            if(attendancesettinglist.size() > 0) {
                //上班开始时间
                workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                //下班结束时间
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }

            //卡号去重得到打卡总人数
            List<PunchcardRecordDetailbp> punDetaillistCount = new ArrayList<PunchcardRecordDetailbp>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
            List<String> ids= new ArrayList<String>();
            int x = 0;
            for(PunchcardRecordDetailbp count : punDetaillistCount){
                x = x + 1;
                //欠勤时间 全天
                Double minutelogs = 0D;
                //欠勤时间 8点到18点
                Double minute = 0D;
                //上午
                Double minuteam = 0D;
                List<PunchcardRecordDetailbp> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                //所有记录时间升序
                Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetailbp>() {
                    @Override
                    public int compare(PunchcardRecordDetailbp o1, PunchcardRecordDetailbp o2) {
                        Date dt1 = o1.getPunchcardrecord_date();
                        Date dt2 = o2.getPunchcardrecord_date();
                        if (dt1.getTime() > dt2.getTime()) {
                            return 1;
                        } else if (dt1.getTime() < dt2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });

                String Eventno = "";
                //去除重复
                for (int i = 0;i < punDetaillistx.size();i++){
                    if(i < punDetaillistx.size() ){
                        if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                            if(punDetaillistx.get(i).getEventno().equals("1")){
                                //进进选后
                                punDetaillistx.remove(i - 1);
                            }
                            else{
                                //出出选前
                                punDetaillistx.remove(i);
                            }
                        }
                    }
                    if(i < punDetaillistx.size() ){
                        Eventno = punDetaillistx.get(i).getEventno();
                    }
                }
                //个人所有进门记录
                List<PunchcardRecordDetailbp> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                //第一条进门记录
                Date Time_start = null;
                //第一条进门时间
                long startlfirst = 0L;
                //第一条进门时间
                long startlfirsts = 0L;
                if(punDetaillistevent1.size() > 0){
                    Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                    //第一条进门时间
                    startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                    startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                }
                //个人所有出门记录
                List<PunchcardRecordDetailbp> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                //最后一条出门记录
                Date Time_end = null;
                //第一条出门时间
                long endlfirst = 0L;
                if(punDetaillistevent2.size() > 0){
                    //最后一条出门记录
                    Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                    //第一条出门时间
                    endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                    //个人第一条考勤时出门记录的情况
                    if(endlfirst < startlfirsts){
                        punDetaillistevent2.remove(0);
                    }
                }
                //从第一次出门开始计算
                for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                    if(i < punDetaillistevent1.size() - 1){
                        Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                        Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                        //个人出门时间
                        long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                        //个人出门之后再次进门时间
                        long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                        //region 日志用外出时间合计
                        //12点前出门：1、12点后进门；2、13点后进门
                        if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){

                            //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minutelogs = minutelogs + minutes;
                            //午餐结束之后进门的情况
                            if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutesi;
                            }
                        }

                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime()){
                            //午餐时间出门并且午餐结束之后进门的情况
                            if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minutelogs = minutelogs + minutesi;
                            }
                        }
                        else{
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minutelogs = minutelogs + minutes;
                        }
                        //endregion

                        //region 考勤用外出时间合计
                        //个人出门时间小于8点
                        if(startl < sdhm.parse(workshift_start).getTime()){
                            //进门时间跨18点的情况
                            if(endl >= sdhm.parse(closingtime_end).getTime()){
                                //全天欠勤
                                minute = minute + 480D;
                            }
                            //进门时间跨13点的情况
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                minute = minute + 240D + minutesi;
                            }
                            //进门时间跨12点的情况
                            else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                //上午欠勤
                                minute = minute + 240D;
                            }
                            //进门时间跨8点的情况
                            else if(endl > sdhm.parse(workshift_start).getTime()){
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            continue;
                        }
                        //个人出门时间晚于18点的数据排除
                        if(startl >= sdhm.parse(closingtime_end).getTime()){
                            continue;
                        }

                        //进出时间都在8点到12点之间
                        if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                        }
                        //进出时间都在13点之后
                        else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(DateStart)).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(DateEnd)).getTime();
                            //出门时间再18点之后
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                        }
                        //进出门跨12点的情况
                        else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //12点减12点之前最后一次出门时间
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //累计欠勤时间
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                            if(sdhm.parse(closingtime_end).getTime() <= endl){
                                //18点减18之前的最后一次出门时间
                                Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                minute = minute + minutesi;
                            }
                            //午餐结束之后进门的情况3
                            else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutesi;
                            }
                        }
                        //跨进门13点的情况
                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                            //午餐期间出门并且午餐结束之后进门4
                            //午餐结束时间
                            long from = sdhm.parse(lunchbreak_end).getTime();
                            //午餐结束之后进门时间
                            long to = endl;
                            if(sdhm.parse(closingtime_end).getTime() <= to){
                                //18点减18之前的最后一次出门时间
                                Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                minute = minute + minutes;
                            }
                            else{
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //累计欠勤时间
                                minute = minute + minutes;
                            }
                        }
                        //endregion
                    }
                }
                //添加打卡记录start
                double minutess= minute.doubleValue();
                minute = NumberUtil.round(minutess/60,2).doubleValue();
                double minutesss= minuteam.doubleValue();
                minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                //日志用外出时长
                double minutelogss= minutelogs.doubleValue();
                minutelogs = NumberUtil.round(minutelogss/60,2).doubleValue();
                List<Expatriatesinfor> exList = expatriatesinforList.stream().filter(coi ->(coi.getNumber().contains(count.getJobnumber().trim()))).collect(Collectors.toList());
                if (exList.size() > 0) {
                    if(Time_start == null){
                        Time_start = Time_end;
                    }
                    if(Time_end == null){
                        Time_end = Time_start;
                    }
                    String overtimeHours = "";
                    overtimeHours = timeLength(sdhm.format(Time_start), sdhm.format(Time_end), lunchbreak_start, lunchbreak_end);
                    if (Double.valueOf(overtimeHours) > Double.valueOf(minutelogs.toString())) {
                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) - Double.valueOf(minutelogs.toString())));

                    }
                    //打卡记录
                    PunchcardRecordbp punchcardrecord = new PunchcardRecordbp();
                    tokenModel.setUserId(exList.get(0).getAccount());
                    tokenModel.setExpireDate(new Date());
                    punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                    punchcardrecord.setUser_id(exList.get(0).getAccount());
                    punchcardrecord.setJobnumber(count.getJobnumber());
                    punchcardrecord.setGroup_id(exList.get(0).getGroup_id());
                    //外出超过15分钟的欠勤时间
                    punchcardrecord.setWorktime(minute.toString());
                    punchcardrecord.setAbsenteeismam(minuteam.toString());
                    // 日志用外出时长
                    punchcardrecord.setOutgoinghours(minutelogs.toString());
                    punchcardrecord.setTime_start(Time_start);
                    punchcardrecord.setTime_end(Time_end);
                    punchcardrecord.setPunchcardrecordbp_id(UUID.randomUUID().toString());
                    punchcardrecord.preInsert(tokenModel);
                    punchcardrecordbpMapper.insert(punchcardrecord);

                    //创建考勤数据
                    Attendancebp attendance = new Attendancebp();
                    attendance.setUser_id(exList.get(0).getAccount());
                    attendance.setDates(sfymd.parse(recordTime));

                    attendance.setNormal("8");
                    // 设置统计外出的时间
                    attendance.setAbsenteeism(minute.toString());
                    // 日志用外出时长
                    attendance.setOutgoinghours(overtimeHours);
                    attendance.setGroup_id(exList.get(0).getGroup_id());

                    attendance.setYears(DateUtil.format(sfymd.parse(recordTime),"YYYY").toString());
                    attendance.setMonths(DateUtil.format(sfymd.parse(recordTime),"MM").toString());
                    attendance.setAttendancebpid(UUID.randomUUID().toString());
                    attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                    attendance.preInsert(tokenModel);
                    attendancebpMapper.insert(attendance);
                    ids.add(exList.get(0).getAccount());

                }
                //添加打卡记录end
            }
//            List<Expatriatesinfor> inforlist = punchcardrecorddetailbpmapper.getexpatriatesinforbp(ids);
//            for (Expatriatesinfor Expatriatesinfor : inforlist){
//                tokenModel.setUserId(inforlist.get(0).getAccount());
//                tokenModel.setExpireDate(new Date());
//                //插入没有打卡记录的员工的考勤
//                Attendancebp attendance = new Attendancebp();
//                attendance.setAbsenteeism("8");
//                attendance.setNormal("0");
//                attendance.setAttendancebpid(UUID.randomUUID().toString());
//                attendance.setGroup_id(inforlist.get(0).getGroup_id());
//                attendance.setUser_id(inforlist.get(0).getAccount());
//                attendance.setDates(new Date());
//                attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
//                attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
//                attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
//                attendance.preInsert(tokenModel);
//                attendancebpMapper.insert(attendance);
//            }
        }
    }

    //实际工作时间
    public String timeLength(String time_start,String time_end,String lunchbreak_start, String lunchbreak_end) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String overtimeHours =null;
        if(sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime())
        {
            //午休都包括在内
            //打卡开始-结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            //午休时间
            long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
            //打卡记录加班时间
            Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        }
        else if(sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime() || sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime())
        {
            //午休不包括在内
            //打卡开始-结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
            //打卡记录加班时间
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);

        }
        else if(sdf.parse(time_start).getTime() < sdf.parse(lunchbreak_start).getTime() && (sdf.parse(lunchbreak_start).getTime() < sdf.parse(time_end).getTime() ||sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime()))
        {
            //午休包括在内一部分（打卡结束时间在午休内）
            //打卡开始-午休开始时间
            long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        }
        else
        {
            //午休包括在内一部分（打卡开始时间在午休内）
            //午休结束-打开结束时间
            long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
            Double result3 = Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000;
            overtimeHours = String.valueOf(result3);
        }
        return overtimeHours;
    }

    //离职剩余年休
    @Override
    public String remainingAnnual(String userid,String year) throws Exception {
        //离职剩余年休计算公式
        // 当前年度总年休数 / 当前年度全部工作天数 * 当前年度截止到当前日期的工作天数 - 当前年度已经审批通过的年休
        String ra = "0";
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        //当前年度全部工作天数
        String workDayYear ="0";
        String startDate = year + "-04-01";
        String endDate = String.valueOf(Integer.valueOf(year)+1) + "-04-01";
        workDayYear = workDayYears(startDate,endDate,year);

        //当前年度截止到当前日期已经工作天数
        String workDayYearT ="0";
        startDate = String.valueOf(year) + "-04-01";

        Calendar end = Calendar.getInstance();
        List<CustomerInfo> customerinfo = mongoTemplate.find(new Query(Criteria.where("userid").is(userid)), CustomerInfo.class);
        if(customerinfo.size()>0) {
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            if(customerinfo.get(0).getUserinfo() .getResignation_date() != null && !customerinfo.get(0).getUserinfo() .getResignation_date().isEmpty())
            {
                String resignationdate = customerinfo.get(0).getUserinfo().getResignation_date().substring(0, 10);
                Calendar rightNow = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                if(customerinfo.get(0).getUserinfo().getResignation_date().length() >= 24)
                {
                    rightNow.setTime(Convert.toDate(resignationdate));
                    rightNow.add(Calendar.DAY_OF_YEAR, 1);
                    resignationdate = s.format(rightNow.getTime());
                }
                //离职日期 >= 年度截止日
                if(s.parse(resignationdate).getTime() >= s.parse(endDate).getTime())
                {
                    end.setTime(s.parse(endDate));
                }
                else
                {
                    end.setTime(s.parse(resignationdate));
                }
            }
            else
            {
                end.setTime(s.parse(endDate));
            }
        }
        endDate = sfymd.format(end.getTime());
        workDayYearT = workDayYears(startDate,endDate,year);

        //当前年度已经审批通过的年休
        Double finishAnnuel = 0d;
        finishAnnuel = abNormalMapper.selectfinishAnnuel(userid,year);
        if(finishAnnuel==null)
        {
            finishAnnuel = 0d;
        }

        //当前年度的总年休数
        List<AnnualLeave> annualLeaveList = new ArrayList<AnnualLeave>();
        AnnualLeave annualLeave = new AnnualLeave();
        annualLeave.setUser_id(userid);
        annualLeave.setYears(year);
        annualLeave.setStatus("0");
        annualLeaveList = annualLeaveMapper.select(annualLeave);
        if(annualLeaveList.size() > 0)
        {
             // 平均每天的年休数
                Double avgannual = 0d;
                avgannual = annualLeaveList.get(0).getAnnual_leave_thisyear().doubleValue() / Double.valueOf(workDayYear) * Double.valueOf(workDayYearT);
                if(avgannual==null)
                {
                    avgannual = 0d;
                }
                ra = String.valueOf(avgannual - finishAnnuel);
                //DecimalFormat df = new DecimalFormat("######0.0");
                //ra = df.format(df.parse(ra));
                ra = ra.substring(0,ra.indexOf("."));
        }
        if(Double.valueOf(ra) < 0)
        {
            ra = "0";
        }
        return ra;
    }
    //当前年度工作天数
    public String workDayYears(String startDate,String endDate,String year) throws Exception {
        String workDayYear = "0";
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        //工作日表
        WorkingDay workDay = new WorkingDay();
        workDay.setYears(year);
        workDay.setStatus("0");
        List<WorkingDay> workingDaysList = workingDayMapper.select(workDay);

        List<WorkingDay> workingDaysListqu = new ArrayList<WorkingDay>();
        workingDaysListqu = workingDaysList.stream().filter(p->(!p.getType().equals("4"))).collect(Collectors.toList());

        // 全年度的所有日期集合
        List<String> days = new ArrayList<String>();
        List<String> tempdays = new ArrayList<String>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(sfymd.parse(startDate));
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(sfymd.parse(endDate));

        while (tempStart.before(tempEnd)) {
            days.add(sfymd.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }

        for(WorkingDay workingDay : workingDaysListqu)
        {
            //从全年的日期中删除假期
            days.remove(sfymd.format(workingDay.getWorkingdate()));
        }

        //删除周六周日
        List<WorkingDay> workingDaysListbao = new ArrayList<WorkingDay>();
        workingDaysListbao = workingDaysList.stream().filter(p->(p.getType().equals("4"))).collect(Collectors.toList());

        for(String d : days)
        {
            Calendar saturorsunday = Calendar.getInstance();
            saturorsunday.setTime(sfymd.parse(d));
            List<WorkingDay> wList = new ArrayList<WorkingDay>();
            wList = workingDaysListbao.stream().filter(p->(sfymd.format(p.getWorkingdate()).equals(d))).collect(Collectors.toList());

            if (wList.size() == 0 && (saturorsunday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || saturorsunday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
            {
                tempdays.add(d);
            }
        }

        days.removeAll(tempdays);

        //3.8   5.4  的判断
        boolean w = false;
        boolean y = false;
        String womenday = String.valueOf(Integer.valueOf(year)+1) + "-03-08";
        //当前剩余日期中包含3月8日，出勤振替日中不包含3月8日
        if(days.contains(womenday) && !workingDaysListbao.contains(womenday))
        {
            w = true;
        }
        String youngday = year + "-05-04";
        //当前剩余日期中包含5月4日，出勤振替日中不包含5月4日
        if(days.contains(youngday) && !workingDaysListbao.contains(youngday))
        {
            y = true;
        }

        //最终返回天数
        if(w && y)
        {
            workDayYear = String.valueOf(days.size() - 1);
        }
        else if(w || y)
        {
            workDayYear = String.valueOf(days.size() - 0.5);
        }
        else
        {
            workDayYear = String.valueOf(days.size());
        }
        return workDayYear;
    }

    //ccm 1019 计算一个月出勤多少小时
    public String workDayBymonth(String startDate,String endDate,String year) throws Exception
    {
        String workDaymonth = "0";
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        //工作日表
        List<WorkingDay> workingDaysList = workingDayMapper.getBymonth(startDate,endDate,year);

        List<WorkingDay> workingDaysListqu = new ArrayList<WorkingDay>();
        workingDaysListqu = workingDaysList.stream().filter(p->(!p.getType().equals("4"))).collect(Collectors.toList());

        // 当年度当月的所有日期集合
        List<String> days = new ArrayList<String>();
        List<String> tempdays = new ArrayList<String>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(sfymd.parse(startDate));
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(sfymd.parse(endDate));
        tempEnd.add(Calendar.DAY_OF_YEAR,1);

        while (tempStart.before(tempEnd)) {
            days.add(sfymd.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }

        for(WorkingDay workingDay : workingDaysListqu)
        {
            //从当月的日期中删除假期
            days.remove(sfymd.format(workingDay.getWorkingdate()));
        }

        List<WorkingDay> workingDaysListbao = new ArrayList<WorkingDay>();
        workingDaysListbao = workingDaysList.stream().filter(p->(p.getType().equals("4"))).collect(Collectors.toList());

        for(String d : days)
        {
            Calendar saturorsunday = Calendar.getInstance();
            saturorsunday.setTime(sfymd.parse(d));
            List<WorkingDay> wList = new ArrayList<WorkingDay>();
            wList = workingDaysListbao.stream().filter(p->(sfymd.format(p.getWorkingdate()).equals(d))).collect(Collectors.toList());

            if (wList.size() == 0 && (saturorsunday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || saturorsunday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
            {
                tempdays.add(d);
            }
        }
        days.removeAll(tempdays);
        //3.8   5.4  的判断
        boolean w = false;
        boolean y = false;
        String womenday = String.valueOf(Integer.valueOf(year)+1) + "-03-08";
        //当前剩余日期中包含3月8日，出勤振替日中不包含3月8日
        if(days.contains(womenday) && !workingDaysListbao.contains(womenday))
        {
            w = true;
        }
        String youngday = year + "-05-04";
        //当前剩余日期中包含5月4日，出勤振替日中不包含5月4日
        if(days.contains(youngday) && !workingDaysListbao.contains(youngday))
        {
            y = true;
        }

        //最终返回天数
        if(w && y)
        {
            workDaymonth = String.valueOf(days.size() - 1);
        }
        else if(w || y)
        {
            workDaymonth = String.valueOf(days.size() - 0.5);
        }
        else
        {
            workDaymonth = String.valueOf(days.size());
        }
        return workDaymonth;
    }
    //ccm 1019 计算一个月出勤多少小时



    //系统服务-每月最后一天计算实际工资  GBB add
    public void getrealwages()throws Exception {
        TokenModel tokenModel = new TokenModel();
        String StaffNoList = "00000";
        int rowundex = 1;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        SimpleDateFormat sfym = new SimpleDateFormat("yyyyMM");
        DecimalFormat df = new DecimalFormat("######0.00");
        //当天时间
        String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
        Query query = new Query();
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, now.get(Calendar.YEAR));
        now.set(Calendar.MONTH, now.get(Calendar.MONTH));
        int lastDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
        //判断当月最后一天
        if(!thisDate.substring(thisDate.length()-2,thisDate.length()).equals(String.valueOf(lastDay))){
            return;
        }

        //region 获取离职人员最后一天的打卡记录并插入到数据库
        Criteria criteria = Criteria.where("userinfo.resignation_date")
                .gte(now.get(Calendar.YEAR) + "-" + getMonth(sfymd.format(now.getTime())) + "-" + lastDay)
                .lte(now.get(Calendar.YEAR) + "-" + getMonth(sfymd.format(now.getTime())) + "-" + lastDay);
        query.addCriteria(criteria);
        List<CustomerInfo> customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
        //List<CustomerInfo> customerInfoList = new ArrayList<>();
        if (customerInfoList.size() > 0) {
            for(CustomerInfo info :customerInfoList){
                StaffNoList = info.getUserinfo().getJobnumber();

                //删除昨天的临时数据
                punchcardrecorddetailmapper.deletetepun(thisDate,thisDate,StaffNoList);
                //删除昨天的临时数据
                punchcardrecorddetailmapper.deletetepundet(thisDate,thisDate,StaffNoList);

                //正式
                String doorIDList = "34,16,17,80,81,83,84";//34:自动门；16：1F子母门-左；17：1F子母门-右；80：B2南侧；81：B2北侧；83：B1北侧；84：B2南侧；
                String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByStaffNoList?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList + "&StaffNoList=" + StaffNoList;
                //請求接口
                ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
                Object obj = JSON.toJSON(getresult.getData());
                JSONArray jsonArray = JSONArray.parseArray(obj.toString());
                //打卡时间
                String recordTime = "";
                //员工编号
                String jobnumber = "";
                if(jsonArray.size() > 0){
                    for(Object ob : jsonArray){
                        //打卡时间
                        recordTime = getProperty(ob, "recordTime");
                        //员工编号
                        jobnumber = getProperty(ob, "staffNo");
                        //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
                        String eventNo = getProperty(ob, "eventNo");
                        //无效-反潜回
                        if(eventNo.equals("30")){
                            continue;
                        }
                        //PSCDC(本社人员)
                        String departmentName_P = getProperty(ob, "departmentName");
                        if(!departmentName_P.equals("PSDCD")){
                            continue;
                        }
                        //员工姓名
                        String staffName = getProperty(ob, "staffName");
                        //员工部门
                        String departmentName = getProperty(ob, "departmentName");
                        //门号
                        String doorID = getProperty(ob, "doorID");
                        //添加打卡详细
                        PunchcardRecordDetail punchcardrecorddetail = new PunchcardRecordDetail();
                        //卡号
                        punchcardrecorddetail.setJobnumber(jobnumber);
                        punchcardrecorddetail.setDates(sfymd.parse(recordTime));
                        //打卡时间
                        punchcardrecorddetail.setPunchcardrecord_date(sf.parse(recordTime));
                        //打卡时间
                        punchcardrecorddetail.setUser_id(staffName);
                        //进出状态
                        punchcardrecorddetail.setEventno(eventNo);
                        punchcardrecorddetail.preInsert(tokenModel);
                        punchcardrecorddetail.setPunchcardrecorddetail_id(UUID.randomUUID().toString());
                        punchcardrecorddetailmapper.insert(punchcardrecorddetail);
                        punDetaillist.add(punchcardrecorddetail);
                    }
                }
                if(punDetaillist.size() > 0){
                    //考勤设定
                    AttendanceSetting attendancesetting = new AttendanceSetting();
                    //上班开始时间
                    String workshift_start = "";
                    //午下班结束时间
                    String closingtime_end = "";
                    //午休时间开始
                    String lunchbreak_start = "";
                    //午休时间结束
                    String lunchbreak_end = "";
                    List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
                    if(attendancesettinglist.size() > 0) {
                        //上班开始时间
                        workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":", "");
                        //下班结束时间
                        closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");;
                        //午休时间开始
                        lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                        //午休时间结束
                        lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
                    }

                    //卡号去重得到打卡总人数
                    List<PunchcardRecordDetail> punDetaillistCount = new ArrayList<PunchcardRecordDetail>();
                    punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));
                    //String books[] = new String[punDetaillistCount.size() + 1];
                    int x = 0;
                    for(PunchcardRecordDetail count : punDetaillistCount){
                        try{
                            x = x + 1;
                            //欠勤时间 全天
                            Double minutelogs = 0D;
                            //欠勤时间 8点到18点
                            Double minute = 0D;
                            //上午
                            Double minuteam = 0D;
                            List<PunchcardRecordDetail> punDetaillistx = punDetaillist.stream().filter(p->(count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());

                            //所有记录时间升序
                            Collections.sort(punDetaillistx, new Comparator<PunchcardRecordDetail>() {
                                @Override
                                public int compare(PunchcardRecordDetail o1, PunchcardRecordDetail o2) {
                                    Date dt1 = o1.getPunchcardrecord_date();
                                    Date dt2 = o2.getPunchcardrecord_date();
                                    if (dt1.getTime() > dt2.getTime()) {
                                        return 1;
                                    } else if (dt1.getTime() < dt2.getTime()) {
                                        return -1;
                                    } else {
                                        return 0;
                                    }
                                }
                            });

                            String Eventno = "";
                            //去除重复
                            for (int i = 0;i < punDetaillistx.size();i++){
                                if(i < punDetaillistx.size() ){
                                    if(punDetaillistx.get(i).getEventno().equals(Eventno)){
                                        if(punDetaillistx.get(i).getEventno().equals("1")){
                                            //进进选后
                                            punDetaillistx.remove(i - 1);
                                        }
                                        else{
                                            //出出选前
                                            punDetaillistx.remove(i);
                                        }
                                    }
                                }
                                if(i < punDetaillistx.size() ){
                                    Eventno = punDetaillistx.get(i).getEventno();
                                }
                            }
                            //个人所有进门记录
                            List<PunchcardRecordDetail> punDetaillistevent1 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("1"))).collect(Collectors.toList());
                            //第一条进门记录
                            Date Time_start = null;
                            //第一条进门时间
                            long startlfirst = 0L;
                            //第一条进门时间
                            long startlfirsts = 0L;
                            if(punDetaillistevent1.size() > 0){
                                Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                                //第一条进门时间
                                startlfirst = sdhm.parse(sdhm.format(Time_start)).getTime();
                                startlfirsts = sdhms.parse(sdhms.format(Time_start)).getTime();
                            }
                            //个人所有出门记录
                            List<PunchcardRecordDetail> punDetaillistevent2 = punDetaillistx.stream().filter(p->(p.getEventno().equalsIgnoreCase("2"))).collect(Collectors.toList());
                            //最后一条出门记录
                            Date Time_end = null;
                            //第一条出门时间
                            long endlfirst = 0L;
                            if(punDetaillistevent2.size() > 0){
                                //最后一条出门记录
                                Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                                //第一条出门时间
                                endlfirst = sdhms.parse(sdhms.format(punDetaillistevent2.get(0).getPunchcardrecord_date())).getTime();
                                //个人第一条考勤时出门记录的情况
                                if(endlfirst < startlfirsts){
                                    punDetaillistevent2.remove(0);
                                }
                            }
                            //从第一次出门开始计算
                            for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                                if(i < punDetaillistevent1.size() - 1){
                                    Date DateStart = punDetaillistevent2.get(i).getPunchcardrecord_date();
                                    Date DateEnd = punDetaillistevent1.get(i + 1).getPunchcardrecord_date();
                                    //个人出门时间
                                    long startl = sdhm.parse(sdhm.format(DateStart)).getTime();
                                    //个人出门之后再次进门时间
                                    long endl = sdhm.parse(sdhm.format(DateEnd)).getTime();

                                    //region 日志用外出时间合计
                                    //12点前出门：1、12点后进门；2、13点后进门
                                    if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){

                                        //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况
                                        //午餐前出门时间
                                        long from = startl;
                                        //午餐开始时间
                                        long to = sdhm.parse(lunchbreak_start).getTime();
                                        //时间出门到进门的相差分钟数
                                        Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                        //累计欠勤时间
                                        minutelogs = minutelogs + minutes;
                                        //午餐结束之后进门的情况
                                        if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                            //午餐结束时间
                                            long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                            //午餐结束之后进门
                                            long toendl = endl;
                                            //时间出门到进门的相差分钟数
                                            Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                            //累计欠勤时间
                                            minutelogs = minutelogs + minutesi;
                                        }
                                    }

                                    else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime()){
                                        //午餐时间出门并且午餐结束之后进门的情况
                                        if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                            //午餐结束时间
                                            long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                            //午餐结束之后进门
                                            long toendl = endl;
                                            //时间出门到进门的相差分钟数
                                            Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                            //累计欠勤时间
                                            minutelogs = minutelogs + minutesi;
                                        }
                                    }
                                    else{
                                        //个人出门时间
                                        long from = sf.parse(sf.format(DateStart)).getTime();
                                        //个人出门之后再次进门时间
                                        long to = sf.parse(sf.format(DateEnd)).getTime();
                                        //时间出门到进门的相差分钟数
                                        Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                        //累计欠勤时间
                                        minutelogs = minutelogs + minutes;
                                    }
                                    //endregion

                                    //region 考勤用外出时间合计
                                    //个人出门时间小于8点
                                    if(startl < sdhm.parse(workshift_start).getTime()){
                                        //进门时间跨18点的情况
                                        if(endl >= sdhm.parse(closingtime_end).getTime()){
                                            //全天欠勤
                                            minute = minute + 480D;
                                        }
                                        //进门时间跨13点的情况
                                        else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                            //午餐结束时间
                                            long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                            //时间出门到进门的相差分钟数
                                            Double minutesi = Convert.toDouble((endl - fromlunchbreak_end)/(1000 * 60));
                                            //累计欠勤时间(上午欠勤+午餐结束完归时间)
                                            minute = minute + 240D + minutesi;
                                        }
                                        //进门时间跨12点的情况
                                        else if(endl >= sdhm.parse(lunchbreak_start).getTime()){
                                            //上午欠勤
                                            minute = minute + 240D;
                                        }
                                        //进门时间跨8点的情况
                                        else if(endl > sdhm.parse(workshift_start).getTime()){
                                            //时间出门到进门的相差分钟数
                                            Double minutes =Convert.toDouble((endl - sdhm.parse(workshift_start).getTime())/(1000 * 60));
                                            minute = minute + minutes;
                                            minuteam = minuteam + minutes;
                                        }
                                        continue;
                                    }
                                    //个人出门时间晚于18点的数据排除
                                    if(startl >= sdhm.parse(closingtime_end).getTime()){
                                        continue;
                                    }

                                    //进出时间都在8点到12点之间
                                    if((startl <= sdhm.parse(lunchbreak_start).getTime() && endl <= sdhm.parse(lunchbreak_start).getTime()))
                                    {
                                        //个人出门时间
                                        long from = sf.parse(sf.format(DateStart)).getTime();
                                        //个人出门之后再次进门时间
                                        long to = sf.parse(sf.format(DateEnd)).getTime();
                                        //时间出门到进门的相差分钟数
                                        Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                        minute = minute + minutes;
                                        minuteam = minuteam + minutes;
                                    }
                                    //进出时间都在13点之后
                                    else if((startl >= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()))
                                    {
                                        //个人出门时间
                                        long from = sf.parse(sf.format(DateStart)).getTime();
                                        //个人出门之后再次进门时间
                                        long to = sf.parse(sf.format(DateEnd)).getTime();
                                        //出门时间再18点之后
                                        if(sdhm.parse(closingtime_end).getTime() <= endl){
                                            //18点减18之前的最后一次出门时间
                                            Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - startl)/(1000 * 60));
                                            minute = minute + minutes;
                                        }
                                        else{
                                            //时间出门到进门的相差分钟数
                                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                            minute = minute + minutes;
                                        }
                                    }
                                    //进出门跨12点的情况
                                    else if(startl <= sdhm.parse(lunchbreak_start).getTime() && endl >= sdhm.parse(lunchbreak_start).getTime()){
                                        //午餐前出门时间
                                        long from = startl;
                                        //午餐开始时间
                                        long to = sdhm.parse(lunchbreak_start).getTime();
                                        //12点减12点之前最后一次出门时间
                                        Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                        //累计欠勤时间
                                        minute = minute + minutes;
                                        minuteam = minuteam + minutes;
                                        if(sdhm.parse(closingtime_end).getTime() <= endl){
                                            //18点减18之前的最后一次出门时间
                                            Double minutesi = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - sdhm.parse(lunchbreak_end).getTime())/(1000 * 60));
                                            minute = minute + minutesi;
                                        }
                                        //午餐结束之后进门的情况3
                                        else if(endl >= sdhm.parse(lunchbreak_end).getTime()){
                                            //午餐结束时间
                                            long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                            //午餐结束之后进门
                                            long toendl = endl;
                                            //时间出门到进门的相差分钟数
                                            Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));
                                            //累计欠勤时间
                                            minute = minute + minutesi;
                                        }
                                    }
                                    //跨进门13点的情况
                                    else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl <= sdhm.parse(lunchbreak_end).getTime() && endl >= sdhm.parse(lunchbreak_end).getTime()){
                                        //午餐期间出门并且午餐结束之后进门4
                                        //午餐结束时间
                                        long from = sdhm.parse(lunchbreak_end).getTime();
                                        //午餐结束之后进门时间
                                        long to = endl;
                                        if(sdhm.parse(closingtime_end).getTime() <= to){
                                            //18点减18之前的最后一次出门时间
                                            Double minutes = Convert.toDouble((sdhm.parse(closingtime_end).getTime() - from)/(1000 * 60));
                                            minute = minute + minutes;
                                        }
                                        else{
                                            //时间出门到进门的相差分钟数
                                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                            //累计欠勤时间
                                            minute = minute + minutes;
                                        }
                                    }
                                    //endregion
                                }
                            }
                            //添加打卡记录start
                            double minutess= minute.doubleValue();
                            minute = NumberUtil.round(minutess/60,2).doubleValue();
                            double minutesss= minuteam.doubleValue();
                            minuteam = NumberUtil.round(minutesss/60,2).doubleValue();
                            //日志用外出时长
                            double minutelogss= minutelogs.doubleValue();
                            minutelogs = NumberUtil.round(minutelogss/60,2).doubleValue();
                            //获取人员信息
                            CustomerInfo customerInfo = info;
                            if (customerInfo != null) {
                                if(Time_start == null){
                                    Time_start = Time_end;
                                }
                                if(Time_end == null){
                                    Time_end = Time_start;
                                }
                                String overtimeHours = "";
                                overtimeHours = timeLength(sdhm.format(Time_start), sdhm.format(Time_end), lunchbreak_start, lunchbreak_end);
                                if (Double.valueOf(overtimeHours) > Double.valueOf(minutelogs.toString())) {
                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) - Double.valueOf(minutelogs.toString())));
                                }
                                //打卡记录
                                PunchcardRecord punchcardrecord = new PunchcardRecord();
                                tokenModel.setUserId(customerInfo.getUserid());
                                tokenModel.setExpireDate(new Date());
                                punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                                punchcardrecord.setUser_id(customerInfo.getUserid());
                                punchcardrecord.setJobnumber(count.getJobnumber());
                                punchcardrecord.setCenter_id(customerInfo.getUserinfo().getCentername());
                                punchcardrecord.setGroup_id(customerInfo.getUserinfo().getGroupname());
                                punchcardrecord.setTeam_id(customerInfo.getUserinfo().getTeamname());
                                //外出超过15分钟的欠勤时间
                                punchcardrecord.setWorktime(minute.toString());
                                punchcardrecord.setAbsenteeismam(minuteam.toString());
                                // 日志用外出时长
                                punchcardrecord.setOutgoinghours(minutelogs.toString());
                                punchcardrecord.setTime_start(Time_start);
                                punchcardrecord.setTime_end(Time_end);
                                punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                                punchcardrecord.preInsert(tokenModel);
                                punchcardrecordMapper.insert(punchcardrecord);
                            }
                            //添加打卡记录end
                        }
                        catch (Exception e) {
                            System.out.println("卡号:" + count.getJobnumber() + "生成数据异常");
                            continue;
                        }
                    }
                }

                //region 重新计算考勤
                punchcardRecordService.methodAttendance_b(now,info.getUserid());
                //endregion 重新计算考勤
            }
        }
        //endregion 获取离职人员最后一天的打卡记录并插入到数据库

        //region 本月实际工资
        String strTemp = sfym.format(new Date());

        //region 删除实际工资
        // 查询当前月份的giving
        Giving giving = new Giving();
        giving.setMonths(strTemp);
        giving.setActual("1");
        List<Giving> givinglist = givingMapper.select(giving);

        if (givinglist.size() > 0) {
            deletewages(givinglist);
        }
        //endregion 获取本月预计工资

        //region 获取本月预计工资
        String oldgivingid = "";
        // 查询当前月份的giving
        giving = new Giving();
        giving.setMonths(strTemp);
        giving.setStatus("4");
        giving.setActual("0");
        givinglist = givingMapper.select(giving);

        if (givinglist.size() > 0) {
            oldgivingid = givinglist.get(0).getGiving_id();
            tokenModel.setUserId(givinglist.get(0).getCreateby());
        }
        //endregion 获取本月预计工资

        // region  給与計算实际工资
        String givingid = UUID.randomUUID().toString();
        giving = new Giving();
        if (tokenModel != null) {
            giving.preInsert(tokenModel);
        } else {
            giving.preInsert();
        }
        giving.setGiving_id(givingid);
        giving.setGeneration("2");
        giving.setGenerationdate(new Date());
        giving.setMonths(sfym.format(new Date()));
        giving.setActual("1");
        givingMapper.insert(giving);

        //endregion 給与計算实际工资

        //region 入职
        Induction indu = new Induction();
        indu.setGiving_id(oldgivingid);
        List<Induction> inductionList = inductionMapper.select(indu);
        if (inductionList.size() > 0) {
            for (Induction induction : inductionList) {
                induction.setInduction_id(UUID.randomUUID().toString());
                induction.setGiving_id(givingid);
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
        //endregion 入职

        //region 退职
        rowundex = 1;
        Retire ret = new Retire();
        ret.setGiving_id(oldgivingid);
        List<Retire> retireList = retireMapper.select(ret);
        if (retireList.size() > 0) {
            for (Retire retire : retireList) {
                retire.setGiving_id(givingid);
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
        //endregion 退职

        //region 基数
        rowundex = 1;
        Base ba = new Base();
        ba.setGiving_id(oldgivingid);
        List<Base> baseList = baseMapper.select(ba);
        if (baseList.size() > 0) {
            for (Base base : baseList) {
                base.setGiving_id(givingid);
                base.setBase_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    base.preInsert(tokenModel);
                } else {
                    base.preInsert();
                }
                base.setRowindex(rowundex);
                baseMapper.insert(base);
                rowundex++;
            }
        }
        //endregion 基数

        //region 其他2
        rowundex = 1;
        OtherTwo ot = new OtherTwo();
        ot.setGiving_id(oldgivingid);
        List<OtherTwo> othertwoList = othertwoMapper.select(ot);
        if (othertwoList.size() > 0) {
            for (OtherTwo othertwo : othertwoList) {
                othertwo.setGiving_id(givingid);
                othertwo.setOthertwo_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    othertwo.preInsert(tokenModel);
                } else {
                    othertwo.preInsert();
                }
                othertwo.setRowindex(rowundex);
                othertwoMapper.insert(othertwo);
                rowundex++;
            }
        }
        //endregion 其他2

        //region 其他1
        rowundex = 1;
        OtherOne oo = new OtherOne();
        oo.setGiving_id(oldgivingid);
        List<OtherOne> otheroneList = otheroneMapper.select(oo);
        if (otheroneList.size() > 0) {
            for (OtherOne otherone : otheroneList) {
                otherone.setGiving_id(givingid);
                otherone.setOtherone_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    otherone.preInsert(tokenModel);
                } else {
                    otherone.preInsert();
                }
                otherone.setRowindex(rowundex);
                otheroneMapper.insert(otherone);
                rowundex++;
            }
        }
        //endregion 其他1

        //region 其他4
        rowundex = 1;
        OtherFour of = new OtherFour();
        of.setGiving_id(oldgivingid);
        List<OtherFour> otherfourList = otherfourMapper.select(of);
        if (otherfourList.size() > 0) {
            for (OtherFour otherfour : otherfourList) {
                otherfour.setGiving_id(givingid);
                otherfour.setOtherfour_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    otherfour.preInsert(tokenModel);
                } else {
                    otherfour.preInsert();
                }
                otherfour.setRowindex(rowundex);
                otherfourMapper.insert(otherfour);
                rowundex++;
            }
        }
        //endregion 其他4

        //region 其他5
        rowundex = 1;
        OtherFive ofi = new OtherFive();
        ofi.setGiving_id(oldgivingid);
        List<OtherFive> otherfiveList = otherfiveMapper.select(ofi);
        if (otherfiveList.size() > 0) {
            for (OtherFive otherfive : otherfiveList) {
                otherfive.setGiving_id(givingid);
                otherfive.setOtherfive_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    otherfive.preInsert(tokenModel);
                } else {
                    otherfive.preInsert();
                }
                otherfive.setRowindex(rowundex);
                otherfiveMapper.insert(otherfive);
                rowundex++;
            }
        }
        //endregion 其他5

        //region 欠勤
        rowundex = 1;
        Lackattendance la = new Lackattendance();
        la.setGiving_id(oldgivingid);
        List<Lackattendance> lackattendanceList = lackattendanceMapper.select(la);
        if (lackattendanceList.size() > 0) {
            for (Lackattendance lackattendance : lackattendanceList) {
                lackattendance.setGiving_id(givingid);
                lackattendance.setLackattendance_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    lackattendance.preInsert(tokenModel);
                } else {
                    lackattendance.preInsert();
                }
                lackattendance.setRowindex(rowundex);
                lackattendanceMapper.insert(lackattendance);
                rowundex++;
            }
        }
        //离职人员实际欠勤
        List<Lackattendance> lackattendanceListnew = insertLackattendance("2",givingid, tokenModel);
        if(lackattendanceListnew.size() > 0){
            for (Lackattendance lackattendance : lackattendanceListnew) {
                //删除预计离职人员加班
                Lackattendance lackattendanceold = new Lackattendance();
                lackattendanceold.setGiving_id(lackattendance.getGiving_id());
                lackattendanceold.setUser_id(lackattendance.getUser_id());
                lackattendanceMapper.delete(lackattendanceold);
                //添加离职人员实际欠勤
                lackattendance.setRowindex(rowundex);
                lackattendanceMapper.insert(lackattendance);
                rowundex++;
            }
        }
        //endregion 欠勤

        //region 加班
        rowundex = 1;
        Residual re = new Residual();
        re.setGiving_id(oldgivingid);
        List<Residual> residualList = residualMapper.select(re);
        if (residualList.size() > 0) {
            for (Residual residual : residualList) {
                residual.setGiving_id(givingid);
                residual.setResidual_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    residual.preInsert(tokenModel);
                } else {
                    residual.preInsert();
                }
                residual.setRowindex(rowundex);
                residualMapper.insert(residual);
                rowundex++;
            }
        }
        //离职人员实际加班
        List<Residual> residualListnew = insertResidual("2",givingid, tokenModel);
        if(residualListnew.size() > 0){
            for (Residual residual : residualListnew) {
                //删除预计离职人员加班
                Residual residualold = new Residual();
                residualold.setGiving_id(residual.getGiving_id());
                residualold.setUser_id(residual.getUser_id());
                residualMapper.delete(residualold);
                //添加离职人员实际加班
                residual.setRowindex(rowundex);
                residualMapper.insert(residual);
                rowundex++;
            }
        }
        //endregion 加班

        //region 月度赏与
        rowundex = 1;
        Appreciation app = new Appreciation();
        app.setGiving_id(oldgivingid);
        List<Appreciation> appreciationList = appreciationMapper.select(app);
        if (appreciationList.size() > 0) {
            for (Appreciation appreciation : appreciationList) {
                appreciation.setGiving_id(givingid);
                appreciation.setAppreciation_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    appreciation.preInsert(tokenModel);
                } else {
                    appreciation.preInsert();
                }
                appreciation.setRowindex(rowundex);
                appreciationMapper.insert(appreciation);
                rowundex++;
            }
        }
        //endregion 月度赏与

        //region 附加控除
        rowundex = 1;
        Additional ad = new Additional();
        ad.setGiving_id(oldgivingid);
        List<Additional> additionalList = additionalMapper.select(ad);
        if (additionalList.size() > 0) {
            for (Additional additional : additionalList) {
                additional.setGiving_id(givingid);
                additional.setAdditional_id(UUID.randomUUID().toString());
                if (tokenModel != null) {
                    additional.preInsert(tokenModel);
                } else {
                    additional.preInsert();
                }
                additional.setRowindex(rowundex);
                additionalMapper.insert(additional);
                rowundex++;
            }
        }
        //endregion 附加控除

        //region 総合収入 comprehensiveview  月度賞与 + 当月応発工資（工资总额(纳税+免税)+只納税）
        //endregion 総合収入 comprehensiveview

        //region 専項控除 disciplinaryview  専項控除累計（当月まで）
        //endregion 専項控除 disciplinaryview

        //region 免税 dutyfreeview  小计3
        //endregion 免税 dutyfreeview

        //region 累计税金 accumulatedtaxview    当月応発工資（工资总额(纳税+免税)+只納税）
        //endregi2

        //endregion 本月实际工资

        //region 实际工资
        List<Wages> wagesList = wagesMapper.getWagesByGivingId(givingid,"");
        if(wagesList.size() > 0){
            wagesList.get(0).setStatus(AuthConstants.DEL_FLAG_NORMAL);//实际工资
            wagesList.get(0).setActual("1");//实际工资
            wagesservice.insertWages(wagesList,tokenModel);
        }
        //endregion 实际工资

    }

    public void deletewages(List<Giving> givinglist) {
        // 删除当前月份的入职表数据
        Induction induction = new Induction();
        induction.setGiving_id(givinglist.get(0).getGiving_id());
        inductionMapper.delete(induction);
        // 删除当前月份的退职表数据
        Retire retire = new Retire();
        retire.setGiving_id(givinglist.get(0).getGiving_id());
        retireMapper.delete(retire);
        // 删除当前月份的基数表数据
        Base base = new Base();
        base.setGiving_id(givinglist.get(0).getGiving_id());
        baseMapper.delete(base);
        // 删除当前月份的其他2表数据
        OtherTwo otherTwo = new OtherTwo();
        otherTwo.setGiving_id(givinglist.get(0).getGiving_id());
        othertwoMapper.delete(otherTwo);
        // 删除当前月份的其他1表数据
        OtherOne otherOne = new OtherOne();
        otherOne.setGiving_id(givinglist.get(0).getGiving_id());
        otheroneMapper.delete(otherOne);
        // 删除当前月份的其他4表数据
        OtherFour otherfour = new OtherFour();
        otherfour.setGiving_id(givinglist.get(0).getGiving_id());
        otherfourMapper.delete(otherfour);
        // 删除当前月份的其他5表数据
        OtherFive otherfive = new OtherFive();
        otherfive.setGiving_id(givinglist.get(0).getGiving_id());
        otherfiveMapper.delete(otherfive);

        //删除当前月份的欠勤表数据
        Lackattendance lackattendance = new Lackattendance();
        lackattendance.setGiving_id(givinglist.get(0).getGiving_id());
        lackattendanceMapper.delete(lackattendance);

        //删除当前月份的加班数据
        Residual residual = new Residual();
        residual.setGiving_id(givinglist.get(0).getGiving_id());
        residualMapper.delete(residual);

        //删除当前月份的月度赏与数据
        Appreciation appreciation = new Appreciation();
        appreciation.setGiving_id(givinglist.get(0).getGiving_id());
        appreciationMapper.delete(appreciation);

        //删除当前月份的附加控除数据
        Additional additional = new Additional();
        additional.setGiving_id(givinglist.get(0).getGiving_id());
        additionalMapper.delete(additional);

        //删除wages当月数据
        Wages del_wage = new Wages();
        del_wage.setGiving_id(givinglist.get(0).getGiving_id());
        wagesMapper.delete(del_wage);
        //zqu end
        // 删除当前月giving表数据
        Giving giving = new Giving();
        giving.setGiving_id(givinglist.get(0).getGiving_id());
        givingMapper.delete(giving);
    }

    /**
     * @return
     * @Method insertLackattendance
     * @Author SKAIXX
     * @Description 欠勤数据插入
     * @Date 2020/3/16 8:52
     * @Param [givingid, tokenModel]
     **/
    public List<Lackattendance> insertLackattendance(String strFlg,String givingid, TokenModel tokenModel) throws Exception {
        List<Lackattendance> lackattendanceList = new ArrayList<>();
        // region 获取基数表数据
        Base base = new Base();
        base.setGiving_id(givingid);
        base.setType("4");
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
            lackattendanceList.add(lackattendance);
            //lackattendanceMapper.insert(lackattendance);
        });
        return lackattendanceList;
    }

    /**
     * @return void
     * @Method insertResidual
     * @Author SKAIXX
     * @Description 残业计算
     * @Date 2020/3/13 15:51
     * @Param [givingid, tokenModel]
     **/
    public List<Residual> insertResidual(String strFlg, String givingid, TokenModel tokenModel) throws Exception {
        List<Residual> residualList = new ArrayList<>();
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
        base.setType("4");//离职人员
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
            residualList.add(residual);
            //residualMapper.insert(residual);
        });
        return residualList;
    }

    public String getMonth(String mouth) {
        String _mouth = mouth.substring(5, 7);
        if (_mouth.substring(0, 1) == "0") {
            return _mouth.substring(1);
        }
        return _mouth;
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
        if (!com.nt.utils.StringUtils.isEmpty(val)) {
            return val;
        }
        return "0";
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
        com.nt.dao_Org.Dictionary shortDictionary = new com.nt.dao_Org.Dictionary();
        shortDictionary.setCode("PR049005");    // 短病欠扣除比例
        shortDictionary = dictionaryMapper.select(shortDictionary).get(0);

        // 获取长病欠
        com.nt.dao_Org.Dictionary longDictionary = new Dictionary();
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
        String rn = com.nt.utils.StringUtils.isEmpty(base.getRn()) || "その他".equals(base.getRn()) ? "PR021001" : base.getRn();
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
}
