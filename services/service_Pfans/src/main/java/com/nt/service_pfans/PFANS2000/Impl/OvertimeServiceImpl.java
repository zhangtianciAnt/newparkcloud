package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.AttendanceSetting;
import com.nt.dao_Pfans.PFANS2000.FlexibleWork;
import com.nt.dao_Pfans.PFANS2000.Irregulartiming;
import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.service_pfans.PFANS2000.OvertimeService;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceSettingMapper;
import com.nt.service_pfans.PFANS2000.mapper.OvertimeMapper;
import com.nt.service_pfans.PFANS2000.mapper.IrregulartimingMapper;
import com.nt.service_pfans.PFANS2000.mapper.FlexibleWorkMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class OvertimeServiceImpl implements OvertimeService {

    @Autowired
    private OvertimeMapper overtimeMapper;
    @Autowired
    private IrregulartimingMapper irregulartimingMapper;
    @Autowired
    private FlexibleWorkMapper flexibleworkMapper;
    @Autowired
    private AttendanceSettingMapper attendanceSettingMapper;

    @Override
    public List<Overtime> getOvertime(Overtime overtime) throws Exception {
        return overtimeMapper.select(overtime);
    }

    @Override
    public List<Overtime> getOvertimelist(Overtime overtime) throws Exception {
        return overtimeMapper.select(overtime);
    }
    @Override
    public Overtime One(String overtimeid) throws Exception {
        return overtimeMapper.selectByPrimaryKey(overtimeid);
    }

    @Override
    public void insertOvertime(Overtime overtime, TokenModel tokenModel) throws Exception {
        overtime.preInsert(tokenModel);
        overtime.setOvertimeid(UUID.randomUUID().toString());
        overtimeMapper.insert(overtime);
    }

    @Override
    public void updateOvertime(Overtime overtime, TokenModel tokenModel) throws Exception {
        overtime.preUpdate(tokenModel);
        overtimeMapper.updateByPrimaryKey(overtime);
        //if(overtime.getStatus().equals("4") || overtime.getStatus().equals("7")){
        if(overtime.getStatus().equals("4") || overtime.getStatus().equals("7")|| overtime.getStatus().equals("0")){
            String workshift_start = null;
            String workshift_end = null;
            String closingtime_start = null;
            String closingtime_end = null;
            String lunchbreak_start = null;
            String lunchbreak_end = null;
            //不定时考勤人员(非不定时考勤人员才计算)
            Irregulartiming irregulartiming = new Irregulartiming();
            irregulartiming.setUser_id(overtime.getUserid());
            irregulartiming.setStatus("4");
            List<Irregulartiming> irregulartiminglist = irregulartimingMapper.select(irregulartiming);
            if(irregulartiminglist.size() == 0){
                //考勤设定
                AttendanceSetting attendancesetting = new AttendanceSetting();
                List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
                if(attendancesettinglist.size() > 0){
                    workshift_start = attendancesettinglist.get(0).getWorkshift_start();
                    workshift_end = attendancesettinglist.get(0).getWorkshift_end();
                    closingtime_start = attendancesettinglist.get(0).getClosingtime_start();
                    closingtime_end = attendancesettinglist.get(0).getClosingtime_end();
                    lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start();
                    lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end();
                    //弹性工作制
                    String Flexibleworkshift_start = null;
                    FlexibleWork flexibleWork = new FlexibleWork();
                    flexibleWork.setUser_id(overtime.getUserid());
                    flexibleWork.setStatus("4");
                    List<FlexibleWork> flexibleWorklist = flexibleworkMapper.select(flexibleWork);
                    if(flexibleWorklist.size() > 0){
                        Flexibleworkshift_start = flexibleWorklist.get(0).getWorktime();
                        String result = null;
                        if(workshift_start != null){
                            String start = workshift_start.replace(":","");
                            String end = Flexibleworkshift_start.replace(":","");
                            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                            result = String.valueOf((sdf.parse(end).getTime() - sdf.parse(start).getTime())/(1000*60));
                            //弹性工作实际上班时间
                            workshift_start = Flexibleworkshift_start;
                        }
                        if(closingtime_start != null){
                            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                            long result1 = sdf.parse(closingtime_start.replace(":","")).getTime() + (Long.parseLong(result) * 60000);
                            Date d1 = new Date(result1);
                            //弹性工作实际下班时间
                            closingtime_start = sdf1.format(d1).toString();
                        }
                    }
                }
            }
        }
    }
}
