package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonResponse;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.mapper.AnnualLeaveMapper;
import com.nt.service_pfans.PFANS2000.mapper.AbNormalMapper;
import com.nt.service_pfans.PFANS2000.mapper.ReplacerestMapper;
import com.nt.service_pfans.PFANS8000.mapper.WorkingDayMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.AccessToken;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.nt.utils.MongoObject.CustmizeQuery;

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


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<AnnualLeave> getDataList() {
        return annualLeaveMapper.getDataList("");
    }

    //事业年度开始跑系统服务（4月1日）
    //@Scheduled(cron="10 * * * * ?")
    public void insert() throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {
                if(customer.getUserid().equals("5dad92f0cfe3561ca0486632")){
                    insertannualLeave(customer);
                }
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
        BigDecimal paid_leave_lastyear = BigDecimal.ZERO;
        BigDecimal deduct_paid_leave_lastyear = BigDecimal.ZERO;
        BigDecimal remaining_paid_leave_lastyear = BigDecimal.ZERO;
        //本年度
        BigDecimal annual_leave_thisyear = BigDecimal.ZERO;
        BigDecimal deduct_annual_leave_thisyear = BigDecimal.ZERO;
        BigDecimal remaining_annual_leave_thisyear = BigDecimal.ZERO;
        BigDecimal paid_leave_thisyear = BigDecimal.ZERO;
        BigDecimal deduct_paid_leave_thisyear = BigDecimal.ZERO;
        BigDecimal remaining_paid_leave_thisyear = BigDecimal.ZERO;
        if(annualLeavelist.size() > 0){
            annual_leave_lastyear = annualLeavelist.get(0).getAnnual_leave_thisyear();
            deduct_annual_leave_lastyear = annualLeavelist.get(0).getDeduct_annual_leave_thisyear();
            remaining_annual_leave_lastyear = annualLeavelist.get(0).getRemaining_annual_leave_thisyear();
            paid_leave_lastyear = annualLeavelist.get(0).getPaid_leave_thisyear();
            deduct_paid_leave_lastyear = annualLeavelist.get(0).getDeduct_paid_leave_thisyear();
            remaining_paid_leave_lastyear = annualLeavelist.get(0).getRemaining_paid_leave_thisyear();
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
        if(zero.compareTo(annual_leave_lastyear)==0) {
            annualLeave.setAnnual_leave_lastyear(null);
        }
        if(zero.compareTo(deduct_annual_leave_lastyear)==0) {
            annualLeave.setDeduct_annual_leave_lastyear(null);
        }
        annualLeave.setAnnual_leave_lastyear(annual_leave_lastyear);
        annualLeave.setDeduct_annual_leave_lastyear(deduct_annual_leave_lastyear);
        annualLeave.setRemaining_annual_leave_lastyear(remaining_annual_leave_lastyear);
        annualLeave.setPaid_leave_lastyear(paid_leave_lastyear);
        annualLeave.setDeduct_paid_leave_lastyear(deduct_paid_leave_lastyear);
        annualLeave.setRemaining_paid_leave_lastyear(remaining_paid_leave_lastyear);

        //////////////////////本年度//////////////////////
        //入社年月日
        String enterdaystartCal = customer.getUserinfo().getEnterday();
        int anniversary = 0;
        if (StringUtil.isNotEmpty(enterdaystartCal)) {
            int year = getYears(enterdaystartCal);
            for(int i = 1; i < 11; i++){
                if(year == i){
                    anniversary = 14 + i;
                    break;
                }
            }
        }else {
            enterdaystartCal = "1980-02-29T16:00:00.000Z";
        }
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
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Calendar calendar_a = Calendar.getInstance();
        calendar_a.setTime(new Date());
        calendar_a.add(Calendar.DAY_OF_YEAR,-1);
        DateUtil.format(calendar_a.getTime(),"yyyy-MM-dd");
        DateUtil.format(calendar.getTime(),"yyyy-MM-dd");
        BigDecimal day_Annual_Leave = BigDecimal.ZERO;
        //Ⅰ.途中入职：本事业年度在职期间/12个月*15天
        if(StringUtil.isEmpty(resignationDateendCal))
        {
            if(sf1.parse(enterdaystartCal.replace("Z"," UTC").toString()).compareTo(calendar.getTime())>=0 && sf1.parse(enterdaystartCal.replace("Z"," UTC").toString()).compareTo(calendar_a.getTime())<=0)
            {
                //入职日
                calendar.setTime(sf1.parse(enterdaystartCal.toString().replace("Z"," UTC")));
                day_Annual_Leave = dateLeave(calendar,calendar_a).multiply(new BigDecimal(String.valueOf(15)));
                anniversary = day_Annual_Leave.intValue();
            }
        }

        //Ⅱ.途中离职：本事业年度在职期间/12个月*当年年休天数
        if(StringUtil.isNotEmpty(resignationDateendCal))
        {
            if(sf1.parse(resignationDateendCal.toString().replace("Z"," UTC")).compareTo(calendar.getTime())>=0 && sf1.parse(resignationDateendCal.toString().replace("Z"," UTC")).compareTo(calendar_a.getTime())<=0)
            {
                //离职日
                calendar_a.setTime(sf1.parse(resignationDateendCal.toString().replace("Z"," UTC")));
                day_Annual_Leave = dateLeave(calendar,calendar_a).multiply(new BigDecimal(String.valueOf(anniversary)));
                anniversary = day_Annual_Leave.intValue();
                if(sf1.parse(enterdaystartCal.toString().replace("Z"," UTC")).compareTo(calendar.getTime())>=0 && sf1.parse(enterdaystartCal.toString().replace("Z"," UTC")).compareTo(calendar_a.getTime())<=0)
                {
                    //入职日
                    calendar.setTime(sf1.parse(enterdaystartCal.toString().replace("Z"," UTC")));
                    //离职日
                    calendar_a.setTime(sf1.parse(resignationDateendCal.toString().replace("Z"," UTC")));
                    day_Annual_Leave = dateLeave(calendar,calendar_a).multiply(new BigDecimal(String.valueOf(anniversary)));
                    anniversary = day_Annual_Leave.intValue();
                }
            }
        }

        //有以下情形的，不能享受该事业年度的年休：
        //1年以上10年未满  因病休假2个月以上的
        if (StringUtil.isNotEmpty(workdaystartCal)) {
            int year = getYears(workdaystartCal);
            int hours = 0;
            AbNormal abNormal = new AbNormal();
            abNormal.setUser_id(customer.getUserid());
            abNormal.setStatus("4");
            List<AbNormal> abNormalList = abNormalMapper.select(abNormal);
            if(abNormalList!=null)
            {
                for(AbNormal abNormalinfo : abNormalList)
                {
                    if(abNormalinfo.getErrortype().equals("PR013010") || abNormalinfo.getErrortype().equals("PR013009"))//病休
                    {
                        hours = hours + Double.valueOf(abNormalinfo.getLengthtime()).intValue();
                    }
                }

                if(year >= 1 && year < 10){
                    if( hours/8 >=60)
                    {
                        anniversary=0;
                        annual_leave_thisyear = BigDecimal.ZERO;
                    }
                }
                if(year >= 10 && year < 20){
                    if(hours/8 >=90)
                    {
                        anniversary=0;
                        annual_leave_thisyear = BigDecimal.ZERO;
                    }
                }
                if(year >= 20){
                    if(hours/8 >=120)
                    {
                        anniversary=0;
                        annual_leave_thisyear = BigDecimal.ZERO;
                    }
                }
            }
        }

        //本年度福利年休（期初）
        if(anniversary > 0){
            if (StringUtil.isNotEmpty(customer.getUserinfo().getRestyear())) {
                paid_leave_thisyear = paid_leave_thisyear.add(new BigDecimal(String.valueOf(anniversary))).subtract(annual_leave_thisyear);
            }
        }

        //法定年假(本年度)
        annualLeave.setAnnual_leave_thisyear(annual_leave_thisyear);
        //法定年假扣除(本年度)
        annualLeave.setDeduct_annual_leave_thisyear(deduct_annual_leave_thisyear);
        //法定年假剩余(本年度)
        annualLeave.setRemaining_annual_leave_thisyear(remaining_annual_leave_thisyear);
        annualLeave.setPaid_leave_thisyear(paid_leave_thisyear);
        annualLeave.setDeduct_paid_leave_thisyear(deduct_paid_leave_thisyear);
        annualLeave.setRemaining_paid_leave_thisyear(remaining_paid_leave_thisyear);
        annualLeaveMapper.insertSelective(annualLeave);

        //更新人员信息
        //今年年休数
        customer.getUserinfo().setAnnualyear(String.valueOf(anniversary));
        //去年年休数(残)
        customer.getUserinfo().setAnnuallastyear(String.valueOf(remaining_paid_leave_lastyear.add(remaining_annual_leave_lastyear)));
        //今年福利年休数
        customer.getUserinfo().setWelfareyear(String.valueOf(paid_leave_thisyear));
        //去年福利年休数(残)
        customer.getUserinfo().setWelfarelastyear(String.valueOf(remaining_paid_leave_lastyear));
        //今年法定年休数
        customer.getUserinfo().setRestyear(String.valueOf(annual_leave_thisyear));
        //去年法定年休数(残)
        customer.getUserinfo().setRestlastyear(String.valueOf(remaining_annual_leave_lastyear));
        mongoTemplate.save(customer);
    }

    public BigDecimal dateLeave(Calendar startCal,Calendar endCal) throws Exception {
        int year_leave = endCal.get(Calendar.YEAR)-startCal.get(Calendar.YEAR);
        int month_leave = endCal.get(Calendar.MONTH)-startCal.get(Calendar.MONTH);
        int day_leave = endCal.get(Calendar.DAY_OF_MONTH)-startCal.get(Calendar.DAY_OF_MONTH);
        if(day_leave<0)
        {
            month_leave--;
            day_leave = day_leave+30;
        }
        if(month_leave<0)
        {
            month_leave= month_leave+12;
            year_leave--;
        }
        return (new BigDecimal(String.valueOf(month_leave)).add((new BigDecimal(String.valueOf(day_leave))).divide(new BigDecimal(String.valueOf(30)),3,RoundingMode.HALF_UP))).divide(new BigDecimal(String.valueOf(12)),3,RoundingMode.HALF_UP);
    }

    public int getYears(String startCal) throws Exception {
        int year = 0;
        startCal = startCal.substring(0,10);
        Calendar cal = Calendar.getInstance();
        String this_year = String.valueOf(cal.get(cal.YEAR));
        String endCal = this_year + "-04-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startCal);
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
                annualLeave.setPaid_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setDeduct_annual_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setDeduct_paid_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setRemaining_annual_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setRemaining_paid_leave_lastyear(BigDecimal.valueOf(0));
                annualLeave.setPaid_leave_thisyear(BigDecimal.valueOf(_entry.getKey()));
                annualLeave.setDeduct_annual_leave_thisyear(BigDecimal.valueOf(0));
                annualLeave.setDeduct_paid_leave_thisyear(BigDecimal.valueOf(deduct_paid_leave_thisyear));
                annualLeave.setRemaining_annual_leave_thisyear(BigDecimal.valueOf(0));
                annualLeave.setRemaining_paid_leave_thisyear(BigDecimal.valueOf(_entry.getValue() - deduct_paid_leave_thisyear));
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
                    _annualLeave.setPaid_leave_thisyear(BigDecimal.valueOf(_entry.getKey()));
                    _annualLeave.setDeduct_paid_leave_thisyear(BigDecimal.valueOf(deduct_paid_leave_thisyear));
                    _annualLeave.setRemaining_paid_leave_thisyear(BigDecimal.valueOf(_entry.getKey() - deduct_paid_leave_thisyear));
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

    //事业年度开始跑系统服务（4月1日）
    //@Scheduled(cron="20 * * * * ?")
    public void insertattendance() throws Exception {
//        Query query = new Query();
//        String books[] = new String[2];
//        books[0] = "5e69d4a515d3ce3938f4202d";
//        books[1] = "5e69cfe4fb5a67819039703d";
//        query.addCriteria(Criteria.where("userid").nin(books));
//        List<CustomerInfo> customerInfo = mongoTemplate.find(query, CustomerInfo.class);
        //CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//        if (customerInfo != null) {
//        }
//        if (customerInfo == null) {
//        }


//        String url = "http://192.168.31.165:9950/KernelService/Admin/QueryRecordByDate?userName=admin&password=admin&pageIndex=1&pageSize=999999&startDate=2020-01-01&endDate=2020-04-01";
        // 請求接口
//        ApiResult getresult = this.restTemplate.getForObject(url, ApiResult.class);
        //JSONObject jsonObject = JSON.parseObject(getresult.getData());
//        Object jsonObject = JSON.toJSON(getresult.getData());
//        String a = "1";
        //JSONObject jsonobject =  JSONObject.get(getresult.getData());
//                JSONObject jsonobject = JSONObject.getObj(getresult.getData());
//        basiccompanyinfo = (Basiccompanyinfo) JSONObject.toBean(jsonobject, Basiccompanyinfo.class);
//        basiccompanyinfo.setName(customernamechange.getNewname());

//        if (getresult.getCode() == 0) {
//            //JSONObject jsonObject = JSON.parseObject(getresult.getData(), AccessToken.class);
//            ArrayList folderList = (ArrayList) getresult.getData();
//            for (int i = 0; i < folderList.size(); i++) {
//                String res = folderList.get(i).toString();
//                JSONObject string_to_json = JSONUtil.parseObj(res);
//                String a = "1";
//                String b = "1";
//            }
//        }
    }

}
