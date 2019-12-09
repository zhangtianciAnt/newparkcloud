package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.OvertimeService;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceSettingMapper;
import com.nt.service_pfans.PFANS2000.mapper.OvertimeMapper;
import com.nt.service_pfans.PFANS2000.mapper.IrregulartimingMapper;
import com.nt.service_pfans.PFANS2000.mapper.PunchcardRecordMapper;
import com.nt.service_pfans.PFANS2000.mapper.FlexibleWorkMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    private PunchcardRecordMapper punchcardrecordMapper;

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
        //overtimeMapper.updateByPrimaryKey(overtime);
        //if(overtime.getStatus().equals("4") || overtime.getStatus().equals("7")){
        if(overtime.getStatus().equals("4") || overtime.getStatus().equals("7")|| overtime.getStatus().equals("0")){
            String workshift_start = null;
            String workshift_end = null;
            String closingtime_start = null;
            String closingtime_end = null;
            String lunchbreak_start = null;
            String lunchbreak_end = null;
            String reserveoverTime = null;
            String actualoverTime = null;
            String weekdaysovertime = null;

//
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
                    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    //公司考勤设定上班开始时间
                    workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":","");
                    workshift_end = attendancesettinglist.get(0).getWorkshift_end();
                    //公司考勤设定下班开始时间
                    closingtime_start = attendancesettinglist.get(0).getClosingtime_start().replace(":","");
                    closingtime_end = attendancesettinglist.get(0).getClosingtime_end();
                    lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start();
                    lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end();
                    //予定加班時間
                    reserveoverTime = overtime.getReserveovertime();
                    BigDecimal reserveoverTimehour = new BigDecimal(reserveoverTime).multiply(new BigDecimal(60 * 60 * 1000));
                    reserveoverTime = String.valueOf(reserveoverTimehour.intValue());
                    //实际加班時間
                    actualoverTime = overtime.getActualovertime();
                    BigDecimal actualoverTimehour = new BigDecimal(actualoverTime).multiply(new BigDecimal(60 * 60 * 1000));
                    actualoverTime = String.valueOf(actualoverTimehour.intValue());
                    //平时晚加班时间扣除
                    weekdaysovertime = attendancesettinglist.get(0).getWeekdaysovertime();
                    BigDecimal weekdaysovertimehour = new BigDecimal(weekdaysovertime).multiply(new BigDecimal(60 * 60 * 1000));
                    weekdaysovertime = String.valueOf(weekdaysovertimehour.intValue());
                    //弹性工作制
                    String Flexibleworkshift_start = null;
                    FlexibleWork flexibleWork = new FlexibleWork();
                    flexibleWork.setUser_id(overtime.getUserid());
                    flexibleWork.setStatus("4");
                    List<FlexibleWork> flexibleWorklist = flexibleworkMapper.select(flexibleWork);
                    if(flexibleWorklist.size() > 0){
                        Flexibleworkshift_start = flexibleWorklist.get(0).getWorktime().replace(":","");
                        String result = null;
                        if(workshift_start != null){
                            //公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数
                            result = String.valueOf((sdf.parse(Flexibleworkshift_start).getTime() - sdf.parse(workshift_start).getTime())/(1000*60));
                            //弹性工作实际上班时间
                            workshift_start = Flexibleworkshift_start;
                        }
                        if(closingtime_start != null && result != null){
                            //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                            long result1 = sdf.parse(closingtime_start).getTime() + (Long.parseLong(result) * 60000);
                            Date d1 = new Date(result1);
                            //弹性工作实际下班时间
                            closingtime_start = sdf.format(d1).toString();
                        }
                    }
                    String overtimeHours = null;
                    //加班后应该下班的时间
                    String overtime_end = null;
                    if(!actualoverTime.equals("0")){
                        //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                        long result1 = sdf.parse(closingtime_start).getTime() +
                                (Long.parseLong(weekdaysovertime)) + (Long.parseLong(actualoverTime));
                        Date d1 = new Date(result1);
                        overtime_end = sdf.format(d1).toString();
                        //实际加班时间
                        overtimeHours = actualoverTime;
                    }
                    else{
                        //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                        //1分钟=60*1000=60000毫秒
                        long result1 = sdf.parse(closingtime_start).getTime() +
                                (Long.parseLong(weekdaysovertime)) + (Long.parseLong(reserveoverTime));
                        Date d1 = new Date(result1);
                        overtime_end = sdf.format(d1).toString();
                        //予定加班時間
                        overtimeHours = reserveoverTime;
                    }
                    //实际打卡记录
                    PunchcardRecord punchcardrecord = new PunchcardRecord();
                    punchcardrecord.setUser_id(overtime.getUserid());
                    //punchcardrecord.setPunchcardrecord_date(overtime.getReserveovertimedate());
                    List<PunchcardRecord> punchcardRecordlist = punchcardrecordMapper.select(punchcardrecord);
                    if(punchcardRecordlist.size() > 0){
                        String time_start = sdf.format(punchcardRecordlist.get(0).getTime_start());
                        String time_end = sdf.format(punchcardRecordlist.get(0).getTime_end());
                        if(!overtime_end.equals("")){
                            if(Integer.valueOf(overtime_end) <= Integer.valueOf(time_end)){
                                //实际加班时间
                                overtimeHours = overtimeHours;
                            }
                            else{
                                //最后打卡记录的时间减去吃饭时间
                                long result1 = sdf.parse(time_end).getTime() - (Long.parseLong(weekdaysovertime)) - sdf.parse(closingtime_start).getTime();
                                System.out.print(result1);
                                System.out.print(String.valueOf(result1));
                                System.out.print(overtimeHours);
//                                Date d1 = new Date(result1);
//                                time_end = sdf.format(d1).toString();
//                                long result2 = sdf.parse(sdf.format(d1).toString()).getTime() - sdf.parse(closingtime_start).getTime();
                                overtimeHours = String.valueOf(result1);
                                System.out.print(overtimeHours);
                            }
                        }
//                        System.out.print((Double.valueOf(overtimeHours)) / 60 / 60 / 1000);
                        double d = (Double.valueOf(overtimeHours)) / 60 / 60 / 1000;
                        String result = String .format("%.2f");
                        System.out.print(result);
                        if(overtime.getOvertimetype().equals("PR001001")){

                        }
                        else if(overtime.getOvertimetype().equals("PR001002")){

                        }
                        else if(overtime.getOvertimetype().equals("PR001003")){

                        }
                        else if(overtime.getOvertimetype().equals("PR001004")){

                        }
                        else if(overtime.getOvertimetype().equals("PR001005")){

                        }
                        else if(overtime.getOvertimetype().equals("PR001006")){

                        }
                        else if(overtime.getOvertimetype().equals("PR001007")){

                        }
                        else if(overtime.getOvertimetype().equals("PR001008")){

                        }
                    }
                }
            }
        }
    }
}
