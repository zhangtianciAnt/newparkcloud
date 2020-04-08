package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.nt.dao_Org.CustomerInfo;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS1000.Outsidedetail;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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
    private TokenService tokenService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<AnnualLeave> getDataList(TokenModel tokenModel) {
        return annualLeaveMapper.getDataList(tokenModel.getOwnerList());
    }

    //系统服务--事业年度开始获取年休
    //@Scheduled(cron="10 * * * * ?")测试用
    @Scheduled(cron="0 0 0 1 4 *")//正式时间每年4月1日零时执行
    public void insert() throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {
                //if(customer.getUserid().equals("5e78b2264e3b194874180f37")){
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
        //仕事开始年月日
        String workdaystartCal = customer.getUserinfo().getWorkday();

        if (StringUtil.isNotEmpty(workdaystartCal)) {
            int year = getYears(workdaystartCal);
            //本年度法定年休（期初）
            if(year >= 1 && year < 10){
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
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar_a = Calendar.getInstance();
        calendar_a.setTime(new Date());
        calendar_a.add(Calendar.DAY_OF_YEAR,-1);
        DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
        DateUtil.format(calendar.getTime(),"yyyy-MM-dd");


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
        insertattendance(-1);
    }
    //系统服务--取打卡记录
    @Override
    public void insertattendance(int diffday) throws Exception {
//        try {
            TokenModel tokenModel = new TokenModel();
            List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
            //测试接口 GBB add
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, diffday);
            String thisDate = DateUtil.format(cal.getTime(),"yyyy-MM-dd");
            //String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
            //删除昨天的临时数据
            punchcardrecorddetailmapper.deletetepun(thisDate);
            //删除昨天的临时数据
            punchcardrecorddetailmapper.deletetepundet(thisDate);
            //正式
            String doorIDList = "34,16,17";//34:自动门；16：1F子母门-左；17：1F子母门-右；
            String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
            //請求接口
            ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
            Object obj = JSON.toJSON(getresult.getData());
            JSONArray jsonArray = JSONArray.parseArray(obj.toString());
            //打卡时间
            String recordTime = "";
            //员工编号
            String jobnumber = "";
            String jobnumberOld = "";
            //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
            String eventNoOld = "";
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
                    //判断是否短时间同一人多次打卡
                    if(eventNo.equals(eventNoOld) && jobnumber.equals(jobnumberOld)){
                        continue;
                    }
                    eventNoOld = eventNo;
                    jobnumberOld = jobnumber;
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
                    closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");
                    //午休时间开始
                    lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                    //午休时间结束
                    lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
                }
                PunchcardRecordDetail pd = new PunchcardRecordDetail();
                //所有记录时间升序
                Collections.sort(punDetaillist, new Comparator<PunchcardRecordDetail>() {
                    @Override
                    public int compare(PunchcardRecordDetail o1, PunchcardRecordDetail o2) {
//                        try {
                            Date dt1 = o1.getPunchcardrecord_date();
                            Date dt2 = o2.getPunchcardrecord_date();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return 0;
                    }
                });
                //卡号去重得到打卡总人数
                List<PunchcardRecordDetail> punDetaillistCount = new ArrayList<PunchcardRecordDetail>();
                punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));

                String books[] = new String[punDetaillistCount.size() + 1];
                int x = 0;
                for(PunchcardRecordDetail count : punDetaillistCount){
                    x = x + 1;
                    //欠勤时间 全天
                    Double minute = 0D;
                    //上午
                    Double minuteam = 0D;
                    //个人所有进门记录
                    List<PunchcardRecordDetail> punDetaillistevent1 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("1") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                    //第一条进门记录
                    Date Time_start = new Date();
                    if(punDetaillistevent1.size() > 0){
                        Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                    }
                    //个人所有出门记录
                    List<PunchcardRecordDetail> punDetaillistevent2 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("2") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                    //最后一条出门记录
                    Date Time_end = new Date();
                    if(punDetaillistevent2.size() > 0){
                        Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                    }
                    //从第一次出门开始计算
                    for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                        if(i < punDetaillistevent1.size() - 1){

                            //个人出门时间
                            long startl = sdhm.parse(sdhm.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                            //个人出门之后再次进门时间
                            long endl = sdhm.parse(sdhm.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();

//                            if(startl < sdhm.parse(workshift_start).getTime() || startl > sdhm.parse(closingtime_end).getTime()){
                            if(startl < sdhm.parse(workshift_start).getTime() || startl > DateUtil.offset(Time_start, DateField.HOUR_OF_DAY, 9).getTime()){

                                continue;
                            }
                            //去除午餐时间的情况1
                            if((startl < sdhm.parse(lunchbreak_start).getTime() && endl < sdhm.parse(lunchbreak_start).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                BigDecimal abnormal = new BigDecimal(minutes);
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            else if((startl > sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                BigDecimal abnormal = new BigDecimal(minutes);
                                minute = minute + minutes;
                            }
                            else if(startl < sdhm.parse(lunchbreak_start).getTime() && endl > sdhm.parse(lunchbreak_start).getTime()){
                                //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况2
                                //午餐前出门时间
                                long from = startl;
                                //午餐开始时间
                                long to = sdhm.parse(lunchbreak_start).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //超过15分钟翻倍记录（向上取整）
                                BigDecimal abnormal = new BigDecimal(minutes);
                                //累计欠勤时间
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                                //午餐结束之后进门的情况3
                                if(endl > sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));

                                    //超过15分钟翻倍记录（向上取整）
                                    BigDecimal abnormalb = new BigDecimal(minutesi);
                                    //累计欠勤时间
                                    minute = minute + minutesi;
                                }
                            }
                            else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl < sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()){
                                //午餐期间出门并且午餐结束之后进门4
                                //午餐结束时间
                                long from = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门时间
                                long to = endl;
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));

                                //超过15分钟翻倍记录（向上取整）
                                BigDecimal abnormal = new BigDecimal(minutes);
                                //累计欠勤时间
                                minute = minute + minutes;
                            }
                        }
                    }
                    //添加打卡记录start
                    //打卡记录
                    PunchcardRecord punchcardrecord = new PunchcardRecord();
                    //获取人员信息
                    Query query = new Query();
                    //欠勤基本单位
                    //BigDecimal standard =new BigDecimal(15);
//                    if (minute.remainder(standard) != BigDecimal.ZERO)
//                    {
//                        double minutes= minute.doubleValue();
//                        double standards = standard.doubleValue();
//                        minute = BigDecimal.valueOf(Math.floor(Double.valueOf(minutes)/Double.valueOf(standards))*Double.valueOf(standards) +Double.valueOf(standards));
//
//                    }
                    double minutess= minute.doubleValue();
                    minute = NumberUtil.round(minutess/60,2).doubleValue();

//                    if (minuteam.remainder(standard) != BigDecimal.ZERO)
//                    {
//                        double minutes= minuteam.doubleValue();
//                        double standards = standard.doubleValue();
//                        minuteam = BigDecimal.valueOf(Math.floor(Double.valueOf(minutes)/Double.valueOf(standards))*Double.valueOf(standards) +Double.valueOf(standards));
//
//                    }
                    double minutesss= minuteam.doubleValue();
                    minuteam = NumberUtil.round(minutesss/60,2).doubleValue();

                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(count.getJobnumber()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
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
                        punchcardrecord.setTime_start(Time_start);
                        punchcardrecord.setTime_end(Time_end);
                        punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                        punchcardrecord.preInsert(tokenModel);
                        punchcardrecordMapper.insert(punchcardrecord);

                        //创建考勤数据
                        Attendance attendance = new Attendance();
                        attendance.setUser_id(customerInfo.getUserid());
                        attendance.setDates(sfymd.parse(recordTime));

                        attendance.setNormal("8");
                        // 设置统计外出的时间
                        attendance.setAbsenteeism(minute.toString());
                        attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                        attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                        attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());

                        attendance.setYears(DateUtil.format(sfymd.parse(recordTime),"YYYY").toString());
                        attendance.setMonths(DateUtil.format(sfymd.parse(recordTime),"MM").toString());
                        attendance.setAttendanceid(UUID.randomUUID().toString());
                        attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                        attendance.preInsert(tokenModel);
                        attendance.setTenantid(Time_start.toString());
                        attendanceMapper.insert(attendance);
                        books[x] = customerInfo.getUserid();
                    }
                    //添加打卡记录end
                }
                Query query_userid = new Query();
                query_userid.addCriteria(Criteria.where("userid").nin(books));
                List<CustomerInfo> customerInfoList = mongoTemplate.find(query_userid, CustomerInfo.class);
                for (CustomerInfo customerInfo : customerInfoList)
                {
                    //插入没有打卡记录的员工的考勤
                    Attendance attendance = new Attendance();
                    attendance.setAbsenteeism("8");
                    attendance.setNormal("0");
                    attendance.setAttendanceid(UUID.randomUUID().toString());
                    attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                    attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                    attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                    attendance.setUser_id(customerInfo.getUserid());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_YEAR, diffday);
                    attendance.setDates(calendar.getTime());
                    attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                    attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                    attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                    tokenModel.setUserId(attendance.getUser_id());
                    tokenModel.setExpireDate(new Date());
                    attendance.preInsert(tokenModel);
                    attendanceMapper.insert(attendance);
                }
                //处理异常和加班数据
                punchcardRecordService.methodAttendance_b(tokenModel,customerInfoList,diffday);
            }
//        } catch (Exception e) {
//            throw new LogicalException("获取打卡记录数据异常，请通知管理员");
//        }
    }

    //系统服务--取打卡记录BP
    @Override
    public void insertattendancebp(int diffday) throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<PunchcardRecordDetailbp> punDetaillist = new ArrayList<PunchcardRecordDetailbp>();
        //测试接口 GBB add
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, diffday);
        String thisDate = DateUtil.format(cal.getTime(),"yyyy-MM-dd");
        //String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        //删除昨天的临时数据
        punchcardrecorddetailbpmapper.deletetepunbp(thisDate);
        //删除昨天的临时数据
        punchcardrecorddetailbpmapper.deletetepundetbp(thisDate);
        //正式
        String doorIDList = "34,16,17";//34:自动门；16：1F子母门-左；17：1F子母门-右；
        String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
        //請求接口
        ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
        Object obj = JSON.toJSON(getresult.getData());
        JSONArray jsonArray = JSONArray.parseArray(obj.toString());
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";
        String jobnumberOld = "";
        //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
        String eventNoOld = "";
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
                if(departmentName_P.equals("PSDCD")){
                    continue;
                }
                //判断是否短时间同一人多次打卡
                if(eventNo.equals(eventNoOld) && jobnumber.equals(jobnumberOld)){
                    continue;
                }
                eventNoOld = eventNo;
                jobnumberOld = jobnumber;
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
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }
            PunchcardRecordDetailbp pd = new PunchcardRecordDetailbp();
            //所有记录时间升序
            Collections.sort(punDetaillist, new Comparator<PunchcardRecordDetailbp>() {
                @Override
                public int compare(PunchcardRecordDetailbp o1, PunchcardRecordDetailbp o2) {
//                        try {
                    Date dt1 = o1.getPunchcardrecord_date();
                    Date dt2 = o2.getPunchcardrecord_date();
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return 0;
                }
            });
            //卡号去重得到打卡总人数
            List<PunchcardRecordDetailbp> punDetaillistCount = new ArrayList<PunchcardRecordDetailbp>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));

            String books[] = new String[punDetaillistCount.size() + 1];
            int x = 0;
            for(PunchcardRecordDetailbp count : punDetaillistCount){
                x = x + 1;
                //欠勤时间 全天
                Double minute = 0D;
                //上午
                Double minuteam = 0D;
                //个人所有进门记录
                List<PunchcardRecordDetailbp> punDetaillistevent1 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("1") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                //第一条进门记录
                Date Time_start = new Date();
                if(punDetaillistevent1.size() > 0){
                    Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                }
                //个人所有出门记录
                List<PunchcardRecordDetailbp> punDetaillistevent2 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("2") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                //最后一条出门记录
                Date Time_end = new Date();
                if(punDetaillistevent2.size() > 0){
                    Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                }
                //从第一次出门开始计算
                for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                    if(i < punDetaillistevent1.size() - 1){

                        //个人出门时间
                        long startl = sdhm.parse(sdhm.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                        //个人出门之后再次进门时间
                        long endl = sdhm.parse(sdhm.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();

//                            if(startl < sdhm.parse(workshift_start).getTime() || startl > sdhm.parse(closingtime_end).getTime()){
                        if(startl < sdhm.parse(workshift_start).getTime() || startl > DateUtil.offset(Time_start, DateField.HOUR_OF_DAY, 9).getTime()){

                            continue;
                        }
                        //去除午餐时间的情况1
                        if((startl < sdhm.parse(lunchbreak_start).getTime() && endl < sdhm.parse(lunchbreak_start).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            BigDecimal abnormal = new BigDecimal(minutes);
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                        }
                        else if((startl > sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            BigDecimal abnormal = new BigDecimal(minutes);
                            minute = minute + minutes;
                        }
                        else if(startl < sdhm.parse(lunchbreak_start).getTime() && endl > sdhm.parse(lunchbreak_start).getTime()){
                            //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况2
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //超过15分钟翻倍记录（向上取整）
                            BigDecimal abnormal = new BigDecimal(minutes);
                            //累计欠勤时间
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                            //午餐结束之后进门的情况3
                            if(endl > sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));

                                //超过15分钟翻倍记录（向上取整）
                                BigDecimal abnormalb = new BigDecimal(minutesi);
                                //累计欠勤时间
                                minute = minute + minutesi;
                            }
                        }
                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl < sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()){
                            //午餐期间出门并且午餐结束之后进门4
                            //午餐结束时间
                            long from = sdhm.parse(lunchbreak_end).getTime();
                            //午餐结束之后进门时间
                            long to = endl;
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));

                            //超过15分钟翻倍记录（向上取整）
                            BigDecimal abnormal = new BigDecimal(minutes);
                            //累计欠勤时间
                            minute = minute + minutes;
                        }
                    }
                }
                //添加打卡记录start
                //打卡记录
                PunchcardRecordbp punchcardrecord = new PunchcardRecordbp();
                //获取人员信息
                Query query = new Query();
                double minutess= minute.doubleValue();
                minute = NumberUtil.round(minutess/60,2).doubleValue();
                double minutesss= minuteam.doubleValue();
                minuteam = NumberUtil.round(minutesss/60,2).doubleValue();

                query.addCriteria(Criteria.where("userinfo.jobnumber").is(count.getJobnumber()));
                CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                if (customerInfo != null) {
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
                    punchcardrecord.setTime_start(Time_start);
                    punchcardrecord.setTime_end(Time_end);
                    punchcardrecord.setPunchcardrecordbp_id(UUID.randomUUID().toString());
                    punchcardrecord.preInsert(tokenModel);
                    punchcardrecordbpMapper.insert(punchcardrecord);

                    //创建考勤数据
                    Attendancebp attendance = new Attendancebp();
                    attendance.setUser_id(customerInfo.getUserid());
                    attendance.setDates(sfymd.parse(recordTime));

                    attendance.setNormal("8");
                    // 设置统计外出的时间
                    attendance.setAbsenteeism(minute.toString());
                    attendance.setCenter_id(customerInfo.getUserinfo().getCentername());

                    attendance.setYears(DateUtil.format(sfymd.parse(recordTime),"YYYY").toString());
                    attendance.setMonths(DateUtil.format(sfymd.parse(recordTime),"MM").toString());
                    attendance.setAttendancebpid(UUID.randomUUID().toString());
                    attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                    attendance.preInsert(tokenModel);
                    attendance.setTenantid(Time_start.toString());
                    attendancebpMapper.insert(attendance);
                    books[x] = customerInfo.getUserid();
                }
                //添加打卡记录end
            }
            Query query_userid = new Query();
            query_userid.addCriteria(Criteria.where("userid").nin(books));
            List<CustomerInfo> customerInfoList = mongoTemplate.find(query_userid, CustomerInfo.class);
            for (CustomerInfo customerInfo : customerInfoList)
            {
                //插入没有打卡记录的员工的考勤
                Attendance attendance = new Attendance();
                attendance.setAbsenteeism("8");
                attendance.setNormal("0");
                attendance.setAttendanceid(UUID.randomUUID().toString());
                attendance.setCenter_id(customerInfo.getUserinfo().getCentername());
                attendance.setGroup_id(customerInfo.getUserinfo().getGroupname());
                attendance.setTeam_id(customerInfo.getUserinfo().getTeamname());
                attendance.setUser_id(customerInfo.getUserid());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, diffday);
                attendance.setDates(calendar.getTime());
                attendance.setYears(DateUtil.format(attendance.getDates(), "YYYY").toString());
                attendance.setMonths(DateUtil.format(attendance.getDates(), "MM").toString());
                attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                tokenModel.setUserId(attendance.getUser_id());
                tokenModel.setExpireDate(new Date());
                attendance.preInsert(tokenModel);
                attendanceMapper.insert(attendance);
            }
            //处理异常和加班数据
            punchcardRecordService.methodAttendance_b(tokenModel,customerInfoList,diffday);
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
    //系统服务--取当天打卡记录
    @Scheduled(cron="0 45 16 * * ?")//正式时间每天下午4点45分执行  GBB add
    public void selectattendance() throws Exception {
//        try {
            TokenModel tokenModel = new TokenModel();
            List<PunchcardRecordDetail> punDetaillist = new ArrayList<PunchcardRecordDetail>();
            //测试接口 GBB add
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
            String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
            //正式
            String doorIDList = "34,16,17";//34:自动门；16：1F子母门-左；17：1F子母门-右；
            String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
            //請求接口
            ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
            Object obj = JSON.toJSON(getresult.getData());
            JSONArray jsonArray = JSONArray.parseArray(obj.toString());
            //打卡时间
            String recordTime = "";
            //员工编号
            String jobnumber = "";
            String jobnumberOld = "";
            //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
            String eventNoOld = "";
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
                    //判断是否短时间同一人多次打卡
                    if(eventNo.equals(eventNoOld) && jobnumber.equals(jobnumberOld)){
                        continue;
                    }
                    eventNoOld = eventNo;
                    jobnumberOld = jobnumber;
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
                    closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");
                    //午休时间开始
                    lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                    //午休时间结束
                    lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
                }
                PunchcardRecordDetail pd = new PunchcardRecordDetail();
                //所有记录时间升序
                Collections.sort(punDetaillist, new Comparator<PunchcardRecordDetail>() {
                    @Override
                    public int compare(PunchcardRecordDetail o1, PunchcardRecordDetail o2) {
//                        try {
                            Date dt1 = o1.getPunchcardrecord_date();
                            Date dt2 = o2.getPunchcardrecord_date();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return 0;
                    }
                });
                //卡号去重得到打卡总人数
                List<PunchcardRecordDetail> punDetaillistCount = new ArrayList<PunchcardRecordDetail>();
                punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));

                String books[] = new String[punDetaillistCount.size() + 1];
                int x = 0;
                for(PunchcardRecordDetail count : punDetaillistCount){
                    x = x + 1;
                    //欠勤时间 全天
                    Double minute = 0D;
                    //上午
                    Double minuteam = 0D;
                    //个人所有进门记录
                    List<PunchcardRecordDetail> punDetaillistevent1 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("1") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                    //第一条进门记录
                    Date Time_start = new Date();
                    if(punDetaillistevent1.size() > 0){
                        Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                    }
                    //个人所有出门记录
                    List<PunchcardRecordDetail> punDetaillistevent2 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("2") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                    //最后一条出门记录
                    Date Time_end = new Date();
                    if(punDetaillistevent2.size() > 0){
                        Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                    }
                    //从第一次出门开始计算
                    for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                        if(i < punDetaillistevent1.size() - 1){

                            //个人出门时间
                            long startl = sdhm.parse(sdhm.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                            //个人出门之后再次进门时间
                            long endl = sdhm.parse(sdhm.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();

//                            if(startl < sdhm.parse(workshift_start).getTime() || startl > sdhm.parse(closingtime_end).getTime()){
                            if(startl < sdhm.parse(workshift_start).getTime() || startl > DateUtil.offset(Time_start, DateField.HOUR_OF_DAY, 9).getTime()){

                                continue;
                            }
                            //去除午餐时间的情况1
                            if((startl < sdhm.parse(lunchbreak_start).getTime() && endl < sdhm.parse(lunchbreak_start).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                                BigDecimal abnormal = new BigDecimal(minutes);
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                            }
                            else if((startl > sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()))
                            {
                                //个人出门时间
                                long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                                //个人出门之后再次进门时间
                                long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                BigDecimal abnormal = new BigDecimal(minutes);
                                minute = minute + minutes;
                            }
                            else if(startl < sdhm.parse(lunchbreak_start).getTime() && endl > sdhm.parse(lunchbreak_start).getTime()){
                                //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况2
                                //午餐前出门时间
                                long from = startl;
                                //午餐开始时间
                                long to = sdhm.parse(lunchbreak_start).getTime();
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                                //超过15分钟翻倍记录（向上取整）
                                BigDecimal abnormal = new BigDecimal(minutes);
                                //累计欠勤时间
                                minute = minute + minutes;
                                minuteam = minuteam + minutes;
                                //午餐结束之后进门的情况3
                                if(endl > sdhm.parse(lunchbreak_end).getTime()){
                                    //午餐结束时间
                                    long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                    //午餐结束之后进门
                                    long toendl = endl;
                                    //时间出门到进门的相差分钟数
                                    Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));

                                    //超过15分钟翻倍记录（向上取整）
                                    BigDecimal abnormalb = new BigDecimal(minutesi);
                                    //累计欠勤时间
                                    minute = minute + minutesi;
                                }
                            }
                            else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl < sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()){
                                //午餐期间出门并且午餐结束之后进门4
                                //午餐结束时间
                                long from = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门时间
                                long to = endl;
                                //时间出门到进门的相差分钟数
                                Double minutes = Convert.toDouble((to - from)/(1000 * 60));

                                //超过15分钟翻倍记录（向上取整）
                                BigDecimal abnormal = new BigDecimal(minutes);
                                //累计欠勤时间
                                minute = minute + minutes;
                            }
                        }
                    }
                    //添加打卡记录start
                    //打卡记录
                    PunchcardRecord punchcardrecord = new PunchcardRecord();
                    //获取人员信息
                    Query query = new Query();
                    //欠勤基本单位
                    //BigDecimal standard =new BigDecimal(15);
//                    if (minute.remainder(standard) != BigDecimal.ZERO)
//                    {
//                        double minutes= minute.doubleValue();
//                        double standards = standard.doubleValue();
//                        minute = BigDecimal.valueOf(Math.floor(Double.valueOf(minutes)/Double.valueOf(standards))*Double.valueOf(standards) +Double.valueOf(standards));
//
//                    }
                    double minutess= minute.doubleValue();
                    minute = NumberUtil.round(minutess/60,2).doubleValue();

//                    if (minuteam.remainder(standard) != BigDecimal.ZERO)
//                    {
//                        double minutes= minuteam.doubleValue();
//                        double standards = standard.doubleValue();
//                        minuteam = BigDecimal.valueOf(Math.floor(Double.valueOf(minutes)/Double.valueOf(standards))*Double.valueOf(standards) +Double.valueOf(standards));
//
//                    }
                    double minutesss= minuteam.doubleValue();
                    minuteam = NumberUtil.round(minutesss/60,2).doubleValue();

                    query.addCriteria(Criteria.where("userinfo.jobnumber").is(count.getJobnumber()));
                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
                    if (customerInfo != null) {
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
                        punchcardrecord.setTime_start(Time_start);
                        punchcardrecord.setTime_end(Time_end);
                        punchcardrecord.setPunchcardrecord_id(UUID.randomUUID().toString());
                        punchcardrecord.preInsert(tokenModel);
                        punchcardrecordMapper.insert(punchcardrecord);
                    }
                    //添加打卡记录end
                }
            }
//        } catch (Exception e) {
//            throw new LogicalException("获取当日打卡记录数据异常，请联系管理员");
//        }
    }

    //系统服务--取当天打卡记录BP
    //@Scheduled(cron="0 50 16 * * ?")//正式时间每天下午4点45分执行  GBB add
    public void selectattendancebp() throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<PunchcardRecordDetailbp> punDetaillist = new ArrayList<PunchcardRecordDetailbp>();
        //测试接口 GBB add
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sfymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdhm = new SimpleDateFormat("HHmm");
        String thisDate = DateUtil.format(new Date(),"yyyy-MM-dd");
        //正式
        String doorIDList = "34,16,17";//34:自动门；16：1F子母门-左；17：1F子母门-右；
        String url = "http://192.168.2.202:80/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=" + thisDate + "&endDate=" + thisDate + "&doorIDList=" + doorIDList;
        //請求接口
        ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
        Object obj = JSON.toJSON(getresult.getData());
        JSONArray jsonArray = JSONArray.parseArray(obj.toString());
        //打卡时间
        String recordTime = "";
        //员工编号
        String jobnumber = "";
        String jobnumberOld = "";
        //进出状态(1，正常进入；2，正常外出;30:无效-反潜回)
        String eventNoOld = "";
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
                //判断是否短时间同一人多次打卡
                if(eventNo.equals(eventNoOld) && jobnumber.equals(jobnumberOld)){
                    continue;
                }
                eventNoOld = eventNo;
                jobnumberOld = jobnumber;
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
                closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":", "");
                //午休时间开始
                lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":", "");
                //午休时间结束
                lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":", "");
            }
            PunchcardRecordDetailbp pd = new PunchcardRecordDetailbp();
            //所有记录时间升序
            Collections.sort(punDetaillist, new Comparator<PunchcardRecordDetailbp>() {
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
            //卡号去重得到打卡总人数
            List<PunchcardRecordDetailbp> punDetaillistCount = new ArrayList<PunchcardRecordDetailbp>();
            punDetaillistCount = punDetaillist.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->new TreeSet<>(Comparator.comparing(t -> t.getJobnumber()))),ArrayList::new));

            String books[] = new String[punDetaillistCount.size() + 1];
            int x = 0;
            for(PunchcardRecordDetailbp count : punDetaillistCount){
                x = x + 1;
                //欠勤时间 全天
                Double minute = 0D;
                //上午
                Double minuteam = 0D;
                //个人所有进门记录
                List<PunchcardRecordDetailbp> punDetaillistevent1 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("1") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                //第一条进门记录
                Date Time_start = new Date();
                if(punDetaillistevent1.size() > 0){
                    Time_start = punDetaillistevent1.get(0).getPunchcardrecord_date();
                }
                //个人所有出门记录
                List<PunchcardRecordDetailbp> punDetaillistevent2 = punDetaillist.stream().filter(p->(p.getEventno().equalsIgnoreCase("2") && count.getJobnumber().equalsIgnoreCase(p.getJobnumber()))).collect(Collectors.toList());
                //最后一条出门记录
                Date Time_end = new Date();
                if(punDetaillistevent2.size() > 0){
                    Time_end = punDetaillistevent2.get(punDetaillistevent2.size() - 1).getPunchcardrecord_date();
                }
                //从第一次出门开始计算
                for (int i = 0; i < punDetaillistevent2.size() - 1; i ++){
                    if(i < punDetaillistevent1.size() - 1){

                        //个人出门时间
                        long startl = sdhm.parse(sdhm.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                        //个人出门之后再次进门时间
                        long endl = sdhm.parse(sdhm.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();

                        if(startl < sdhm.parse(workshift_start).getTime() || startl > DateUtil.offset(Time_start, DateField.HOUR_OF_DAY, 9).getTime()){
                            continue;
                        }
                        //去除午餐时间的情况1
                        if((startl < sdhm.parse(lunchbreak_start).getTime() && endl < sdhm.parse(lunchbreak_start).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes =Convert.toDouble((to - from)/(1000 * 60));
                            BigDecimal abnormal = new BigDecimal(minutes);
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                        }
                        else if((startl > sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()))
                        {
                            //个人出门时间
                            long from = sf.parse(sf.format(punDetaillistevent2.get(i).getPunchcardrecord_date())).getTime();
                            //个人出门之后再次进门时间
                            long to = sf.parse(sf.format(punDetaillistevent1.get(i + 1).getPunchcardrecord_date())).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            BigDecimal abnormal = new BigDecimal(minutes);
                            minute = minute + minutes;
                        }
                        else if(startl < sdhm.parse(lunchbreak_start).getTime() && endl > sdhm.parse(lunchbreak_start).getTime()){
                            //午餐开始前最后一次出门时间并且午餐开始前没有进门时间的情况2
                            //午餐前出门时间
                            long from = startl;
                            //午餐开始时间
                            long to = sdhm.parse(lunchbreak_start).getTime();
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));
                            //超过15分钟翻倍记录（向上取整）
                            BigDecimal abnormal = new BigDecimal(minutes);
                            //累计欠勤时间
                            minute = minute + minutes;
                            minuteam = minuteam + minutes;
                            //午餐结束之后进门的情况3
                            if(endl > sdhm.parse(lunchbreak_end).getTime()){
                                //午餐结束时间
                                long fromlunchbreak_end = sdhm.parse(lunchbreak_end).getTime();
                                //午餐结束之后进门
                                long toendl = endl;
                                //时间出门到进门的相差分钟数
                                Double minutesi = Convert.toDouble((toendl - fromlunchbreak_end)/(1000 * 60));

                                //超过15分钟翻倍记录（向上取整）
                                BigDecimal abnormalb = new BigDecimal(minutesi);
                                //累计欠勤时间
                                minute = minute + minutesi;
                            }
                        }
                        else if(startl >= sdhm.parse(lunchbreak_start).getTime() && startl < sdhm.parse(lunchbreak_end).getTime() && endl > sdhm.parse(lunchbreak_end).getTime()){
                            //午餐期间出门并且午餐结束之后进门4
                            //午餐结束时间
                            long from = sdhm.parse(lunchbreak_end).getTime();
                            //午餐结束之后进门时间
                            long to = endl;
                            //时间出门到进门的相差分钟数
                            Double minutes = Convert.toDouble((to - from)/(1000 * 60));

                            //超过15分钟翻倍记录（向上取整）
                            BigDecimal abnormal = new BigDecimal(minutes);
                            //累计欠勤时间
                            minute = minute + minutes;
                        }
                    }
                }
                //添加打卡记录start
                //获取人员信息
                Query query = new Query();
                double minutess= minute.doubleValue();
                minute = NumberUtil.round(minutess/60,2).doubleValue();
                double minutesss= minuteam.doubleValue();
                minuteam = NumberUtil.round(minutesss/60,2).doubleValue();

                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                expatriatesinfor.setNumber(count.getJobnumber());
                List<Expatriatesinfor> exList = expatriatesinforMapper.select(expatriatesinfor);
                if (exList.size() > 0) {
                    //打卡记录
                    PunchcardRecordbp punchcardrecord = new PunchcardRecordbp();
                    tokenModel.setUserId(exList.get(0).getExpatriatesinfor_id());
                    tokenModel.setExpireDate(new Date());
                    punchcardrecord.setPunchcardrecord_date(sfymd.parse(recordTime));
                    punchcardrecord.setUser_id(exList.get(0).getExpatriatesinfor_id());
                    punchcardrecord.setJobnumber(count.getJobnumber());
                    punchcardrecord.setCenter_id(exList.get(0).getGroup_id());
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
}
