package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
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
//    @Scheduled(cron="0 0 0 1 4 *")//正式时间每年4月1日零时执行
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
                //}
            }
        }
        //会社特别休日加班
        int count = replacerestMapper.updateDateList();
    }
    //新建
    @Override
    public void insertannualLeave(CustomerInfo customer) throws Exception {
        AnnualLeave annualLeave = new AnnualLeave();
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
        //入社年月日
        String enterdaystartCal = customer.getUserinfo().getEnterday();
        if (StringUtil.isEmpty(enterdaystartCal)) {
            enterdaystartCal = "1980-02-29";
        }
        enterdaystartCal = enterdaystartCal.substring(0,10);
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
        //离社年月日
        String resignationDateendCal = customer.getUserinfo().getResignation_date();
        //事业年度开始（4月1日）
        Calendar calendar_a = Calendar.getInstance();
        calendar_a.setTime(new Date());
        calendar_a.add(Calendar.DAY_OF_YEAR,-1);
        DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
        DateUtil.format(calendar.getTime(),"yyyy-MM-dd");

        /*
        //Ⅰ.途中入职：本事业年度在职期间/12个月*15天
        if(StringUtil.isEmpty(resignationDateendCal))
        {
            if(sf1.parse(Convert.toStr(sf1.format(Convert.toDate(enterdaystartCal)))).compareTo(calendar.getTime())>=0 && sf1.parse(Convert.toStr(sf1.format(Convert.toDate(enterdaystartCal)))).compareTo(calendar_a.getTime())<=0)
            {
                //有工作经验者
                if(!(customer.getUserinfo().getEnddate() == null || customer.getUserinfo().getEnddate().isEmpty()))
                {
                    //入职日
                    calendar.setTime(sf1.parse(Convert.toStr(sf1.format(Convert.toDate(enterdaystartCal))).toString().replace("Z"," UTC")));
                    annual_leave_thisyear=dateLeave(calendar,calendar_a,annual_leave_thisyear);
                    annual_leave_thisyear =(new BigDecimal(annual_leave_thisyear.intValue())).setScale(2);
                }
            }
        }

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

        //有以下情形的，不能享受该事业年度的年休：
        //1年以上10年未满  因病休假2个月以上的
        if (StringUtil.isNotEmpty(workdaystartCal)) {
            int year = getYears(workdaystartCal);
            BigDecimal hours = BigDecimal.ZERO;
            AbNormal abNormal = new AbNormal();
            abNormal.setUser_id(customer.getUserid());
            abNormal.setStatus("4");

            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR,-1);
            calendar_a.setTime(new Date());
            calendar_a.add(Calendar.DAY_OF_YEAR,-1);

            DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
            DateUtil.format(calendar.getTime(),"yyyy-MM-dd");
            abNormal.setOccurrencedate(calendar.getTime());
            abNormal.setFinisheddate(calendar_a.getTime());
            List<AbNormal> abNormalList = abNormalMapper.selectAbNormalThisYear(abNormal);
            if(abNormalList!=null)
            {
                for(AbNormal abNormalinfo : abNormalList)
                {
                    calendar.setTime(new Date());
                    calendar.add(Calendar.YEAR,-1);
                    calendar_a.setTime(new Date());
                    calendar_a.add(Calendar.DAY_OF_YEAR,-1);

                    DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
                    DateUtil.format(calendar.getTime(),"yyyy-MM-dd");
                    if(abNormalinfo.getErrortype().equals("PR013010") || abNormalinfo.getErrortype().equals("PR013009"))//病休
                    {
                        if(abNormalinfo.getOccurrencedate().compareTo(calendar.getTime())>=0 && abNormalinfo.getOccurrencedate().compareTo(calendar_a.getTime())<=0
                                && abNormalinfo.getFinisheddate().compareTo(calendar.getTime())>=0 && abNormalinfo.getFinisheddate().compareTo(calendar_a.getTime())<=0)
                        {
                            hours = hours.add(BigDecimal.valueOf(Double.valueOf(abNormalinfo.getLengthtime())));
                        }
                        else if(abNormalinfo.getOccurrencedate().compareTo(calendar.getTime())>=0 && abNormalinfo.getOccurrencedate().compareTo(calendar_a.getTime())<=0)
                        {
                            //开始日
                            calendar.setTime(abNormalinfo.getOccurrencedate());
                            DateUtil.format(calendar.getTime(),"yyyy-MM-dd");
                            DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
                            long days= (calendar_a.getTimeInMillis()-calendar.getTimeInMillis())/(1000*3600*24);
                            hours = hours.add(BigDecimal.valueOf(days*8));

                        }
                        else
                        {
                            //终了日
                            calendar_a.setTime(abNormalinfo.getFinisheddate());
                            DateUtil.format(calendar.getTime(),"yyyy-MM-dd");
                            DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
                            long days= (calendar_a.getTimeInMillis()-calendar.getTimeInMillis())/(1000*3600*24);
                            hours = hours.add(BigDecimal.valueOf(days*8));
                        }
                    }
                }
                BigDecimal eight =new  BigDecimal(0);
                hours.divide(BigDecimal.valueOf(8));
                if(year >= 1 && year < 10){
                    if( hours.divide(BigDecimal.valueOf(8)).compareTo( BigDecimal.valueOf(60))>-1)
                    {
                        annual_leave_thisyear = BigDecimal.ZERO;
                    }
                }
                if(year >= 10 && year < 20){
                    if( hours.divide(BigDecimal.valueOf(8)).compareTo( BigDecimal.valueOf(90))>-1)
                    {
                        annual_leave_thisyear = BigDecimal.ZERO;
                    }
                }
                if(year >= 20){
                    if( hours.divide(BigDecimal.valueOf(8)).compareTo( BigDecimal.valueOf(120))>-1)
                    {
                        annual_leave_thisyear = BigDecimal.ZERO;
                    }
                }
            }
        }

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
        if (endCalendar.compareTo(startCalendar) > 0) {
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
            TokenModel tokenModel = new TokenModel();
            List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
            //测试接口 GBB add
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
            SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
            DecimalFormat df = new DecimalFormat("######0.00");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, diffday);
            String thisDate = DateUtil.format(cal.getTime(),"yyyy-MM-dd");
            //String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
            //删除昨天的临时数据
            punchcardrecorddetailmapper.deletetepun(thisDate,staffNo);
            //删除昨天的临时数据
            punchcardrecorddetailmapper.deletetepundet(thisDate,staffNo);
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
        TokenModel tokenModel = new TokenModel();
        List<PunchcardRecordDetailbp> punDetaillist = new ArrayList<PunchcardRecordDetailbp>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        SimpleDateFormat sdhms = new SimpleDateFormat("HHmmss");
        DecimalFormat df = new DecimalFormat("######0.00");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, diffday);
        String thisDate = DateUtil.format(cal.getTime(),"yyyy-MM-dd");
        //删除昨天的临时数据
        punchcardrecorddetailbpmapper.deletetepunbp(thisDate,staffNo);
        //删除昨天的临时数据
        punchcardrecorddetailbpmapper.deletetepundetbp(thisDate,staffNo);
        //外驻人员信息
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
        expatriatesinforList = expatriatesinforList.stream().filter(coi ->(!StringUtils.isNullOrEmpty(coi.getNumber()))).collect(Collectors.toList());
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
    //@Scheduled(cron="0 45 16 * * ?")
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
    //@Scheduled(cron="0 50 16 * * ?")
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
        if (customerInfoList.size() > 0) {
            for(CustomerInfo info :customerInfoList){
                StaffNoList = info.getUserinfo().getJobnumber();

                //删除昨天的临时数据
                punchcardrecorddetailmapper.deletetepun(thisDate,StaffNoList);
                //删除昨天的临时数据
                punchcardrecorddetailmapper.deletetepundet(thisDate,StaffNoList);

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

        //region 获取本月预计工资
        String strTemp = sfym.format(new Date());
        String oldgivingid = "";
        // 查询当前月份的giving
        Giving giving = new Giving();
        giving.setMonths(strTemp);
        giving.setStatus("4");
        giving.setActual("0");
        List<Giving> givinglist = givingMapper.select(giving);

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
        indu.setGive(oldgivingid);
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
        ret.setGive(oldgivingid);
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

        //region 総合収入 comprehensiveview
        //endregion 総合収入 comprehensiveview

        //region 専項控除 disciplinaryview
        //endregion 専項控除 disciplinaryview

        //region 免税 dutyfreeview
        //endregion 免税 dutyfreeview

        //region 累计税金 accumulatedtaxview
        //endregion 累计税金 accumulatedtaxview

        //endregion 本月实际工资

        //region 实际工资
        List<Wages> wagesList = wagesMapper.getWagesByGivingId(givingid,"");
        if(wagesList.size() > 0){
            wagesList.get(0).setStatus(AuthConstants.DEL_FLAG_NORMAL);//实际工资
            wagesList.get(0).setActual("1");//实际工资
        }
        wagesservice.insertWages(wagesList,tokenModel);
        //endregion 实际工资

    }

    public String getMonth(String mouth) {
        String _mouth = mouth.substring(5, 7);
        if (_mouth.substring(0, 1) == "0") {
            return _mouth.substring(1);
        }
        return _mouth;
    }
}
