package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.mapper.AnnualLeaveMapper;
import com.nt.utils.dao.TokenModel;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Component
@Configuration
@EnableScheduling
@CommonsLog
@Transactional(rollbackFor=Exception.class)
public class AnnualLeaveServiceImpl implements AnnualLeaveService {

    @Autowired
    private AnnualLeaveMapper annualLeaveMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<AnnualLeave> getDataList(AnnualLeave annualLeave) {
        return annualLeaveMapper.select(annualLeave);
    }

    //事业年度开始跑系统服务（4月1日）
   // @Scheduled(cron="59 * * * * ?")
    public void insert() throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {
               if(customer.getUserid().equals("5e16fc07c5e52516b0ab5263")){
                    insertannualLeave(customer);
               }
            }
        }
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
        //CustomerInfo.UserInfo userInfo = new CustomerInfo.UserInfo();
        //今年年休数
        customer.getUserinfo().setAnnualyear(String.valueOf(anniversary));
        //去年年休数(残)
        //userInfo.setAnnuallastyear(String.valueOf(paid_leave_thisyear));
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
       // if((_customerInfos.size() == 0 || _customerInfos.get(0).getUserinfo().getEnterday() == "")&& (userVo.getCustomerInfo().getUserinfo().getEnterday()!="")){
            int result = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(sdf.parse(userVo.getCustomerInfo().getUserinfo().getEnterday()));
            min.add(Calendar.DATE,1);
            int year = min.get(Calendar.MONTH) <3 ? min.get(Calendar.YEAR) : min.get(Calendar.YEAR) + 1;
            Date date = sdf.parse(year + "-" + "03" +"-"+ "31");
            max.setTime(date);
            while (min.before(max)) {
                result++;
                min.add(Calendar.MONTH, 1);
            }
            BigDecimal annualRest =  new BigDecimal((float)result / 12 * 15);
            Double _annualRest =  Math.floor(annualRest.doubleValue());
            AnnualLeave annualLeave = new AnnualLeave();
            annualLeave.setAnnual_leave_thisyear(BigDecimal.valueOf(0));
            annualLeave.setAnnual_leave_lastyear(BigDecimal.valueOf(0));
            annualLeave.setPaid_leave_lastyear(BigDecimal.valueOf(0));
            annualLeave.setDeduct_annual_leave_lastyear(BigDecimal.valueOf(0));
            annualLeave.setDeduct_paid_leave_lastyear(BigDecimal.valueOf(0));
            annualLeave.setRemaining_annual_leave_lastyear(BigDecimal.valueOf(0));
            annualLeave.setRemaining_paid_leave_lastyear(BigDecimal.valueOf(0));
            annualLeave.setPaid_leave_thisyear(BigDecimal.valueOf(_annualRest));
            annualLeave.setDeduct_annual_leave_thisyear(BigDecimal.valueOf(0));
            annualLeave.setDeduct_paid_leave_thisyear(BigDecimal.valueOf(0));
            annualLeave.setRemaining_annual_leave_thisyear(BigDecimal.valueOf(0));
            annualLeave.setRemaining_paid_leave_thisyear(BigDecimal.valueOf(_annualRest));
            annualLeave.setUser_id(id);
            annualLeave.setStatus("0");
            annualLeave.setCreateon(sdf.parse(sdf.format(new Date())));
            annualLeave.setCreateby(userVo.getCustomerInfo().getCreateby());
            annualLeave.setOwner(id);
            annualLeave.setGroup_id(userVo.getCustomerInfo().getUserinfo().getGroupid());
            annualLeave.setCenter_id(userVo.getCustomerInfo().getUserinfo().getCenterid());
            annualLeave.setTeam_id(userVo.getCustomerInfo().getUserinfo().getTeamid());
            annualLeave.setYears(Calendar.getInstance().get(Calendar.YEAR) + "");
            annualLeave.setAnnualleave_id(UUID.randomUUID().toString());
            annualLeaveMapper.insert(annualLeave);
        }
    //}

}
