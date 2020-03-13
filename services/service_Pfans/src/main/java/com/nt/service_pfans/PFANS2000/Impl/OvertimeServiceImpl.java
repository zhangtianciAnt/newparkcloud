package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.OvertimeService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private ReplacerestMapper replacerestmapper;
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
        SimpleDateFormat sdfxx = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date dateStart = overtime.getReserveovertimedate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateStart);
        cal.add(Calendar.HOUR_OF_DAY, -8);
        dateStart = cal.getTime();
        overtime.setReserveovertimedate(dateStart);
        if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES) || overtime.getStatus().equals("7")){

            //添加代休记录
            insertReplacerest(overtime,tokenModel);
            //上班时间开始
            String workshift_start = null;
            //上班时间结束
            String workshift_end = null;
            //下班时间开始
            String closingtime_start = null;
            //下班时间结束
            String closingtime_end = null;
            //午休时间开始
            String lunchbreak_start = null;
            //午休时间结束
            String lunchbreak_end = null;

            //上班时间开始
            String reserveoverTime = null;
            //实际加班時間
            String actualoverTime = null;
            //平日晚加班时间扣除
            String weekdaysovertime = null;
            //加班基本计算单位
            String strovertime = null;

            //不定时考勤人员(非不定时考勤人员才计算)
            Irregulartiming irregulartiming = new Irregulartiming();
            irregulartiming.setUser_id(overtime.getUserid());
            irregulartiming.setStatus(AuthConstants.APPROVED_FLAG_YES);
            List<Irregulartiming> irregulartiminglist = irregulartimingMapper.select(irregulartiming);
            if(irregulartiminglist.size() == 0){
                //考勤设定
                AttendanceSetting attendancesetting = new AttendanceSetting();
                List<AttendanceSetting> attendancesettinglist = attendanceSettingMapper.select(attendancesetting);
                if(attendancesettinglist.size() > 0){
                    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
                    //公司考勤设定上班开始时间
                    workshift_start = attendancesettinglist.get(0).getWorkshift_start().replace(":","");
                    workshift_end = attendancesettinglist.get(0).getWorkshift_end();
                    //公司考勤设定下班开始时间
                    closingtime_start = attendancesettinglist.get(0).getClosingtime_start().replace(":","");
                    closingtime_end = attendancesettinglist.get(0).getClosingtime_end();
                    lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":","");;
                    lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":","");;

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
                    strovertime = attendancesettinglist.get(0).getOvertime();
                    BigDecimal strovertimehour = new BigDecimal(strovertime).multiply(new BigDecimal(60 * 60 * 1000));
                    strovertime = String.valueOf(strovertimehour.intValue());
                    //弹性工作制
                    String Flexibleworkshift_start = null;
                    FlexibleWork flexibleWork = new FlexibleWork();
                    flexibleWork.setUser_id(overtime.getUserid());
                    flexibleWork.setStatus(AuthConstants.APPROVED_FLAG_YES);
                    List<FlexibleWork> flexibleWorklist = flexibleworkMapper.select(flexibleWork);
                    if(flexibleWorklist.size() > 0){
                        String strImplement_date = flexibleWorklist.get(0).getImplement_date().substring(0,10);
                        String strReserveovertimedate = sf1ymd.format(overtime.getReserveovertimedate());
                        int res=strReserveovertimedate.compareTo(strImplement_date);
                        if(res>0){
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
                        overtimeHours = String.valueOf(Double.valueOf(actualoverTime) / 60 / 60 / 1000);
                    }
                    else{
                        //根据公司考勤设定上班开始时间与该员工弹性工作上班的时间的差的分钟数计算该员工实际的下班时间
                        //1分钟=60*1000=60000毫秒
                        long result1 = sdf.parse(closingtime_start).getTime() +
                                (Long.parseLong(weekdaysovertime)) + (Long.parseLong(reserveoverTime));
                        Date d1 = new Date(result1);
                        overtime_end = sdf.format(d1).toString();
                        //予定加班時間
                        overtimeHours = String.valueOf(Double.valueOf(reserveoverTime) / 60 / 60 / 1000);
                    }
                    //实际打卡记录
                    PunchcardRecord punchcardrecord = new PunchcardRecord();
                    punchcardrecord.setUser_id(overtime.getUserid());
                    punchcardrecord.setPunchcardrecord_date(overtime.getReserveovertimedate());
                    List<PunchcardRecord> punchcardRecordlist = punchcardrecordMapper.select(punchcardrecord);
                    if(punchcardRecordlist.size() > 0){
                        String time_start = sdf.format(punchcardRecordlist.get(0).getTime_start());
                        String time_end = sdf.format(punchcardRecordlist.get(0).getTime_end());
                        String worktime = punchcardRecordlist.get(0).getWorktime();

                        //周末加班/法定日加班
                        if(overtime.getOvertimetype().equals("PR001002") || overtime.getOvertimetype().equals("PR001003")){
                            //打卡开始-结束时间
                            long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                            //午休时间
                            long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
                            //打卡结束时间-公司考勤下班时间（判断周末晚餐时间）
                            //long result3 = sdf.parse(time_end).getTime() - sdf.parse(closingtime_start).getTime();
                            Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
                            if(Double.valueOf(overtimeHours) < Double.valueOf(result3)){
                                BigDecimal dovertimeHours = new BigDecimal(overtimeHours).multiply(new BigDecimal(60 * 60 * 1000));
                                long result5 = sdf.parse(workshift_start).getTime() + result2 + (Long.parseLong(String.valueOf(dovertimeHours.intValue())));
                                Date d1 = new Date(result1);
                                //加班后应该下班的时间
                                overtime_end = sdf.format(d1).toString();

                            }
                            else{
                                //实际打卡记录的时间设定为加班时间。
                                overtimeHours = String.valueOf(result3);
                            }
                        }
                        else{
                            if(!overtime_end.equals("")){
                                if(Integer.valueOf(overtime_end) > Integer.valueOf(time_end)){

                                    //最后打卡记录的时间减去吃饭时间
                                    long result1 = sdf.parse(time_end).getTime() - (Long.parseLong(weekdaysovertime)) - sdf.parse(closingtime_start).getTime();
                                    overtimeHours = String.valueOf(Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000);
                                }
                            }
                        }
                        //考勤管理
                        Attendance attendance = new Attendance();
                        attendance.setUser_id(overtime.getUserid());
                        attendance.setDates(overtime.getReserveovertimedate());
                        List<Attendance> attendancelist = attendanceMapper.select(attendance);
                        DecimalFormat  df = new DecimalFormat("######0.00");
                        if(attendancelist.size() > 0){
                            for (Attendance attend : attendancelist) {
                                if(overtime.getOvertimetype().equals("PR001001")){//平日加班
                                    if(attend.getOrdinaryindustry() != null && !attend.getOrdinaryindustry().isEmpty()){
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getOrdinaryindustry())));
                                    }
                                    else{
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                    }
                                    attend.setOrdinaryindustry(overtimeHours);

                                }
                                else if(overtime.getOvertimetype().equals("PR001002")){//周末加班
                                    if(attend.getWeekendindustry() != null && !attend.getWeekendindustry().isEmpty()){
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getWeekendindustry())));
                                    }
                                    else{
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                    }
                                    attend.setWeekendindustry(overtimeHours);

                                    //进代休表
                                }
                                else if(overtime.getOvertimetype().equals("PR001003")){//法定日加班
                                    if(attend.getStatutoryresidue() != null && !attend.getStatutoryresidue().isEmpty()){
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getStatutoryresidue())));
                                    }
                                    else{
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                    }
                                    attend.setStatutoryresidue(overtimeHours);

                                }
                                else if(overtime.getOvertimetype().equals("PR001004")){//一齐年休日加班
                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                    attend.setAnnualrestday(overtimeHours);

                                }
                                else if(overtime.getOvertimetype().equals("PR001005")){//会社特别休日加班
                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                    attend.setSpecialday(overtimeHours);
                                }
                                else if(overtime.getOvertimetype().equals("PR001007")){//五四青年节
                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                    attend.setYouthday(overtimeHours);
                                    //进代休表
                                }
                                else if(overtime.getOvertimetype().equals("PR001008")){//妇女节
                                    overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                    attend.setWomensday(overtimeHours);
                                    //进代休表
                                }
                                attend.preUpdate(tokenModel);
                                attendanceMapper.updateByPrimaryKey(attend);
                            }
                        }
                        else{
                            attendance.setAttendanceid(UUID.randomUUID().toString());
                            attendance.setYears(DateUtil.format(overtime.getReserveovertimedate(),"YYYY").toString());
                            attendance.setMonths(DateUtil.format(overtime.getReserveovertimedate(),"MM").toString());
                            attendance.setUser_id(overtime.getUserid());
                            attendance.setDates(overtime.getReserveovertimedate());
                            attendance.setNormal("8");
                            attendance.setActual(worktime);
                            attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                            attendance.preInsert(tokenModel);
                            if(overtime.getOvertimetype().equals("PR001001")){//平日加班
                                attendance.setOrdinaryindustry(overtimeHours);

                            }
                            else if(overtime.getOvertimetype().equals("PR001002")){//周末加班
                                attendance.setWeekendindustry(overtimeHours);

                                //进代休表
                            }
                            else if(overtime.getOvertimetype().equals("PR001003")){//法定日加班
                                attendance.setStatutoryresidue(overtimeHours);

                            }
                            else if(overtime.getOvertimetype().equals("PR001004")){//一齐年休日加班
                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                attendance.setAnnualrestday(overtimeHours);

                            }
                            else if(overtime.getOvertimetype().equals("PR001005")){//会社特别休日加班
                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                attendance.setSpecialday(overtimeHours);
                            }
                            else if(overtime.getOvertimetype().equals("PR001007")){//五四青年节
                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                attendance.setYouthday(overtimeHours);
                                //进代休表
                            }
                            else if(overtime.getOvertimetype().equals("PR001008")){//妇女节
                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                attendance.setWomensday(overtimeHours);
                                //进代休表
                            }
                            attendance.setOwner(overtime.getUserid());
                            attendanceMapper.insert(attendance);
                        }
                        //承认
                        overtime.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_YES);
                    }
                }
            }
        }
        overtime.preUpdate(tokenModel);
        overtimeMapper.updateByPrimaryKey(overtime);
    }
    //代休添加
    public void insertReplacerest(Overtime overtime, TokenModel tokenModel) throws Exception {
        //加班满8小时的情况
        if(Double.valueOf(overtime.getReserveovertime()) >= 8){
            //代休类型
            String strtype = null;
            //周末加班
            if(overtime.getOvertimetype().equals("PR001002")){
                strtype = "1";
            }
            //会社特别休日加班
            if(overtime.getOvertimetype().equals("PR001005")){
                strtype = "2";
            }
            Replacerest replacerest = new Replacerest();
            replacerest.setUser_id(overtime.getUserid());
            replacerest.setCenter_id(overtime.getCenterid());
            replacerest.setGroup_id(overtime.getGroupid());
            replacerest.setTeam_id(overtime.getTeamid());
            replacerest.setApplication_date(DateUtil.format(new Date(),"YYYY/MM/dd"));
            replacerest.setType(strtype);
            replacerest.setDuration("8");
            replacerest.setRecognitionstate("0");
            replacerest.preInsert(tokenModel);
            replacerest.setReplacerest_id(UUID.randomUUID().toString());
            replacerestmapper.insert(replacerest);
            //一齐年休日加班
            if(overtime.getOvertimetype().equals("PR001004")){

            }
        }
    }
}
