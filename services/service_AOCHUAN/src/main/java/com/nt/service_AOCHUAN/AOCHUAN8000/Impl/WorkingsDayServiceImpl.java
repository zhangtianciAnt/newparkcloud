package com.nt.service_AOCHUAN.AOCHUAN8000.Impl;


import com.nt.dao_AOCHUAN.AOCHUAN8000.WorkingDay;
import com.nt.service_AOCHUAN.AOCHUAN8000.WorkingsDayService;
import com.nt.service_AOCHUAN.AOCHUAN8000.mapper.WorkingsDayMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkingsDayServiceImpl implements WorkingsDayService {

    @Autowired
    private WorkingsDayMapper workingdayMapper;
//    @Autowired
//    private AnnualLeaveMapper annualLeaveMapper;


    @Override
    public void insert(WorkingDay workingday, TokenModel tokenModel) throws Exception {
        boolean bool = true;
        if(workingday.getType().equals("6")){
//            bool = updateAnnualYear(workingday,1);
        }
          if(bool){
              workingday.preInsert(tokenModel);
              workingday.setWorkingday_id(UUID.randomUUID().toString());
              workingdayMapper.insert(workingday);
          }
    }
    @Override
    public List<WorkingDay> getDataList() throws Exception {
        Calendar cal = Calendar.getInstance();
        String this_year = String.valueOf(cal.get(cal.YEAR)+2);
        String last_year = String.valueOf(cal.get(cal.YEAR) - 3);
        List<WorkingDay> workingday = workingdayMapper.getDataList(this_year, last_year);
        return workingday;
    }

    @Override
    public void deletete(WorkingDay workingday, TokenModel tokenModel) throws Exception{
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        String strTemp = sf1.format(workingday.getWorkingdate());
        Date delDate = sf1.parse(strTemp);
        workingday.setWorkingdate(delDate);
        List <WorkingDay> workingDays = workingdayMapper.select(workingday);
        workingDays  = workingDays.stream().filter( workingDay -> workingDay.getType().equals("6")).collect(Collectors.toList());
        if(workingDays.size()>0){
//            updateAnnualYear(workingday,-1);
        }
        workingdayMapper.deletete(workingday.getWorkingdate());
    }
    
//    public boolean updateAnnualYear(WorkingDay workingday,int day){
//            AnnualLeave _annualLeave = new AnnualLeave();
//            _annualLeave.setYears(workingday.getYears());
//            List<AnnualLeave> annualLeaves = annualLeaveMapper.select(_annualLeave);
//            for (AnnualLeave annualLeave:
//                    annualLeaves) {
//                if (annualLeave.getRemaining_annual_leave_lastyear().intValue() > 0) {
//                    annualLeave.setRemaining_annual_leave_lastyear(new BigDecimal(annualLeave.getRemaining_annual_leave_lastyear().intValue() - day));
//                    annualLeave.setDeduct_annual_leave_lastyear(new BigDecimal(annualLeave.getDeduct_annual_leave_lastyear().intValue() + day));
//                }else if(annualLeave.getRemaining_annual_leave_lastyear().intValue() == 0 && annualLeave.getAnnual_leave_thisyear().intValue() == annualLeave.getRemaining_annual_leave_thisyear().intValue() &&
//                !(annualLeave.getAnnual_leave_thisyear().intValue() == 0 && annualLeave.getRemaining_annual_leave_thisyear().intValue() == 0)&& day<0){
//                    annualLeave.setRemaining_annual_leave_lastyear(new BigDecimal(annualLeave.getRemaining_annual_leave_lastyear().intValue() - day));
//                     annualLeave.setDeduct_annual_leave_lastyear(new BigDecimal(annualLeave.getDeduct_annual_leave_lastyear().intValue() + day));
//                }
//                 else if (annualLeave.getRemaining_annual_leave_thisyear().intValue() > 0) {
//                    annualLeave.setRemaining_annual_leave_thisyear(new BigDecimal(annualLeave.getRemaining_annual_leave_thisyear().intValue() - day));
//                    annualLeave.setDeduct_annual_leave_thisyear(new BigDecimal(annualLeave.getDeduct_annual_leave_thisyear().intValue() + day));
//                 }
//            }
//            if(annualLeaves.size()>0){
//                annualLeaveMapper.updateAnnualYear(annualLeaves);
//            }
//        return true;
//    }


}
