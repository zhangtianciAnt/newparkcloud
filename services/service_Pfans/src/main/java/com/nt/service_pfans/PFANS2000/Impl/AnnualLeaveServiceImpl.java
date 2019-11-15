package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.service_pfans.PFANS2000.mapper.AnnualLeaveMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import cn.hutool.core.date.DateUtil;
import java.util.UUID;

@Service
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

    //新建
    @Override
    public void insert(TokenModel tokenModel) throws Exception {
        List<CustomerInfo> customerinfo = mongoTemplate.findAll(CustomerInfo.class);
        if (customerinfo != null) {
            for (CustomerInfo customer : customerinfo) {

                AnnualLeave annualLeave = new AnnualLeave();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.YEAR, -1);
                annualLeave.setUser_id(customer.getUserid());
                annualLeave.setYears(DateUtil.format(calendar.getTime(),"YYYY"));
                List<AnnualLeave> annualLeavelist = annualLeaveMapper.select(annualLeave);
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
                annualLeave.setPaid_leave_lastyear(paid_leave_lastyear);
                annualLeave.setDeduct_paid_leave_lastyear(deduct_paid_leave_lastyear);
                annualLeave.setRemaining_paid_leave_lastyear(remaining_paid_leave_lastyear);
                //本年度
                annualLeave.setAnnual_leave_thisyear(annual_leave_thisyear);
                annualLeave.setDeduct_annual_leave_thisyear(deduct_annual_leave_thisyear);
                annualLeave.setRemaining_annual_leave_thisyear(remaining_annual_leave_thisyear);
                annualLeave.setPaid_leave_thisyear(paid_leave_thisyear);
                annualLeave.setDeduct_paid_leave_thisyear(deduct_paid_leave_thisyear);
                annualLeave.setRemaining_paid_leave_thisyear(remaining_paid_leave_thisyear);

                annualLeaveMapper.insertSelective(annualLeave);
            }
        }
    }
}
