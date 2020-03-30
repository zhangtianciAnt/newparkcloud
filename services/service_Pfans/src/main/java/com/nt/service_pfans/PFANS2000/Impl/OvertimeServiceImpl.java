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

            Overtime over = new Overtime();
            over.setOvertimeid(overtime.getOvertimeid());
            List<Overtime> overTimelist = overtimeMapper.select(over);
            if(!(overTimelist.get(0).getStatus().equals(overtime.getStatus())))
            {
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
                        workshift_end = attendancesettinglist.get(0).getWorkshift_end().replace(":","");
                        //公司考勤设定下班开始时间
                        closingtime_start = attendancesettinglist.get(0).getClosingtime_start().replace(":","");
                        closingtime_end = attendancesettinglist.get(0).getClosingtime_end().replace(":","");
                        lunchbreak_start = attendancesettinglist.get(0).getLunchbreak_start().replace(":","");
                        lunchbreak_end = attendancesettinglist.get(0).getLunchbreak_end().replace(":","");
                        //加班计算单位
                        strovertime =  attendancesettinglist.get(0).getOvertime();
                        //予定加班時間
                        reserveoverTime = overtime.getReserveovertime();
                        //实际加班時間
                        actualoverTime = overtime.getActualovertime();

                        String overtimeHours = null;
                        //考勤管理
                        Attendance attendance = new Attendance();
                        attendance.setUser_id(overtime.getUserid());
                        attendance.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
                        attendance.setDates(overtime.getReserveovertimedate());
                        List<Attendance> attendancelist = attendanceMapper.select(attendance);
                        DecimalFormat  df = new DecimalFormat("######0.00");
                        if(attendancelist.size() > 0){
                            for (Attendance attend : attendancelist) {
                                //实际打卡记录
                                PunchcardRecord punchcardrecord = new PunchcardRecord();
                                punchcardrecord.setUser_id(overtime.getUserid());
                                punchcardrecord.setPunchcardrecord_date(overtime.getReserveovertimedate());
                                List<PunchcardRecord> punchcardRecordlist = punchcardrecordMapper.select(punchcardrecord);
                                if(punchcardRecordlist.size() > 0){
                                    String time_start = sdf.format(punchcardRecordlist.get(0).getTime_start());
                                    String time_end = sdf.format(punchcardRecordlist.get(0).getTime_end());

                                    if(sdf.parse(time_end).getTime() < sdf.parse(closingtime_end).getTime())
                                    {
                                        //18点之前下班
                                        //实际加班时间
                                        overtimeHours = "0";
                                    }
                                    else
                                    {
                                        //18点之后下班
                                        long result1 = sdf.parse(time_end).getTime() - sdf.parse(closingtime_end).getTime();
                                        //实际加班时间
                                        overtimeHours = String.valueOf(Double.valueOf(String.valueOf(result1))/ 60 / 60 / 1000);
                                    }

                                    if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES))
                                    {
                                        //状态是4 ，取预定
                                        if(Double.valueOf(overtimeHours) > Double.valueOf(reserveoverTime))
                                        {
                                            overtimeHours = String.valueOf(Double.valueOf(reserveoverTime));
                                        }
                                        else
                                        {
                                            overtimeHours = String.valueOf(Double.valueOf(overtimeHours));
                                        }
                                    }
                                    else
                                    {
                                        //状态是7 ，取实际
                                        if(Double.valueOf(overtimeHours) > Double.valueOf(actualoverTime))
                                        {
                                            overtimeHours = String.valueOf(Double.valueOf(actualoverTime));
                                        }
                                        else
                                        {
                                            overtimeHours = String.valueOf(Double.valueOf(overtimeHours));
                                        }
                                    }
                                    if(overtime.getOvertimetype().equals("PR001007") || overtime.getOvertimetype().equals("PR001008") || overtime.getOvertimetype().equals("PR001005"))
                                    {
                                        //五四青年节  妇女节   会社特别休日加班
                                        overtimeHours = workhours(time_start,time_end,lunchbreak_start,lunchbreak_end,attend);
                                        overtimeHours = df.format(Math.floor(Double.valueOf(overtimeHours) / (Double.valueOf(strovertime)))*(Double.valueOf(strovertime)));
                                        if(overtime.getOvertimetype().equals("PR001005")){
                                            if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES))
                                            {
                                                if(Double.valueOf(overtimeHours)>= Double.valueOf(reserveoverTime))
                                                {
                                                    overtimeHours = reserveoverTime;
                                                }
                                                else
                                                {
                                                    overtimeHours ="0";
                                                }
                                            }
                                            else
                                            {
                                                if(Double.valueOf(overtimeHours) >= Double.valueOf(actualoverTime))
                                                {
                                                    overtimeHours = actualoverTime;
                                                }
                                                else
                                                {
                                                    overtimeHours ="0";
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES))
                                            {
                                                if(Double.valueOf(overtimeHours) - 4 >= Double.valueOf(reserveoverTime))
                                                {
                                                    overtimeHours = reserveoverTime;
                                                }
                                                else
                                                {
                                                    overtimeHours ="0";
                                                }
                                            }
                                            else
                                            {
                                                if(Double.valueOf(overtimeHours) - 4 >= Double.valueOf(actualoverTime))
                                                {
                                                    overtimeHours = actualoverTime;
                                                }
                                                else
                                                {
                                                    overtimeHours ="0";
                                                }
                                            }
                                        }
                                    }
                                    else if(overtime.getOvertimetype().equals("PR001002") || overtime.getOvertimetype().equals("PR001003") || overtime.getOvertimetype().equals("PR001004"))
                                    {
                                        //周末加班/法定日加班/一齐年休日加班
                                        overtimeHours = timeLength(time_start,time_end,lunchbreak_start,lunchbreak_end);
                                        if(overtime.getOvertimetype().equals("PR001002"))
                                        {
                                            if(Double.valueOf(overtimeHours) >= 8)
                                            {
                                                overtimeHours = String.valueOf(Double.valueOf(overtimeHours) - 8);
                                            }
                                        }
                                        if(overtime.getStatus().equals("7"))
                                        {
                                            if(Double.valueOf(overtimeHours) > Double.valueOf(actualoverTime))
                                            {
                                                overtimeHours = actualoverTime;
                                            }
                                        }
                                        else
                                        {
                                            if(Double.valueOf(overtimeHours) > Double.valueOf(reserveoverTime))
                                            {
                                                overtimeHours = reserveoverTime;
                                            }
                                        }
                                    }
                                    if(overtime.getOvertimetype().equals("PR001001")){//平日加班
                                        if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES))
                                        {
                                            if(attend.getOrdinaryindustry() != null && !attend.getOrdinaryindustry().isEmpty()){
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getOrdinaryindustry())));
                                            }
                                            else{
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                        }
                                        else
                                        {
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        }
                                        //加班时间大于等于15分
                                        if (Double.valueOf(overtimeHours) % (Double.valueOf(strovertime))==0) {
                                            attend.setOrdinaryindustry(overtimeHours);
                                        }
                                        else
                                        {
                                            attend.setOrdinaryindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / (Double.valueOf(strovertime)))*(Double.valueOf(strovertime))));
                                        }
                                    }
                                    else if(overtime.getOvertimetype().equals("PR001002")){//周末加班
                                        if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES))
                                        {
                                            if(attend.getWeekendindustry() != null && !attend.getWeekendindustry().isEmpty()){
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getWeekendindustry())));
                                            }
                                            else{
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                        }
                                        else
                                        {
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        }

                                        //加班时间大于等于15分
                                        if (Double.valueOf(overtimeHours) % (Double.valueOf(strovertime))==0) {
                                            attend.setWeekendindustry(overtimeHours);
                                        }
                                        else
                                        {
                                            attend.setWeekendindustry(df.format(Math.floor(Double.valueOf(overtimeHours) / (Double.valueOf(strovertime)))*(Double.valueOf(strovertime))));
                                        }
                                    }
                                    else if(overtime.getOvertimetype().equals("PR001003")){//法定日加班
                                        if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES))
                                        {
                                            if(attend.getStatutoryresidue() != null && !attend.getStatutoryresidue().isEmpty()){
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getStatutoryresidue())));
                                            }
                                            else{
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                        }
                                        else
                                        {
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        }

                                        //加班时间大于等于15分
                                        if (Double.valueOf(overtimeHours) % (Double.valueOf(strovertime))==0) {
                                            attend.setStatutoryresidue(overtimeHours);
                                        }
                                        else
                                        {
                                            attend.setStatutoryresidue(df.format(Math.floor(Double.valueOf(overtimeHours) / (Double.valueOf(strovertime)))*(Double.valueOf(strovertime))));
                                        }
                                    }
                                    else if(overtime.getOvertimetype().equals("PR001004")){//一齐年休日加班
                                        if(overtime.getStatus().equals(AuthConstants.APPROVED_FLAG_YES))
                                        {
                                            if(attend.getAnnualrestday() != null && !attend.getAnnualrestday().isEmpty()){
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours) + Double.valueOf(attend.getAnnualrestday())));
                                            }
                                            else{
                                                overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                            }
                                        }
                                        else
                                        {
                                            overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        }

                                        //加班时间大于等于15分
                                        if (Double.valueOf(overtimeHours) % (Double.valueOf(strovertime))==0) {
                                            attend.setAnnualrestday(overtimeHours);
                                        }
                                        else
                                        {
                                            attend.setAnnualrestday(df.format(Math.floor(Double.valueOf(overtimeHours) / (Double.valueOf(strovertime)))*(Double.valueOf(strovertime))));
                                        }
                                    }
                                    else if(overtime.getOvertimetype().equals("PR001005")){//会社特别休日加班
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        attend.setSpecialday(overtimeHours);
                                    }
                                    else if(overtime.getOvertimetype().equals("PR001007")){//五四青年节
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        attend.setYouthday(overtimeHours);

                                    }
                                    else if(overtime.getOvertimetype().equals("PR001008")){//妇女节
                                        overtimeHours = String.valueOf(df.format(Double.valueOf(overtimeHours)));
                                        attend.setWomensday(overtimeHours);

                                    }
                                    attend.preUpdate(tokenModel);
                                    attendanceMapper.updateByPrimaryKey(attend);
                                }
                            }
                        }
                    }
                }
            }
        }

        overtime.preUpdate(tokenModel);
        overtimeMapper.updateByPrimaryKey(overtime);
    }

    public String workhours(String time_start,String time_end,String lunchbreak_start, String lunchbreak_end,Attendance ad) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            String shijiworkHours ="0";
            if(sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime())
            {
                long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
            }
            else if(sdf.parse(time_start).getTime() <= sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() < sdf.parse(lunchbreak_end).getTime())
            {
                long result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
            }
            else if(sdf.parse(time_start).getTime() > sdf.parse(lunchbreak_start).getTime() && sdf.parse(time_end).getTime() >= sdf.parse(lunchbreak_end).getTime())
            {
                long result1 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                shijiworkHours = String.valueOf((Double.valueOf(String.valueOf(result1)) / 60 / 60 / 1000)-Double.valueOf(ad.getAbsenteeism()));
            }
            else
            {
                //打卡开始-结束时间
                long result1 = sdf.parse(time_end).getTime() - sdf.parse(time_start).getTime();
                //午休时间
                long result2 = sdf.parse(lunchbreak_end).getTime() - sdf.parse(lunchbreak_start).getTime();
                //打卡记录加班时间
                Double result3 = Double.valueOf(String.valueOf(result1 - result2)) / 60 / 60 / 1000;
                shijiworkHours = String.valueOf(result3 - Double.valueOf(ad.getAbsenteeism()));
                if(Double.valueOf(shijiworkHours) >= 8)
                {
                    result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                    result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                    if(result1 >=4 && result2>=4)
                    {
                        shijiworkHours = shijiworkHours;
                    }
                    else
                    {
                        result3 = Double.valueOf(String.valueOf(result1 > result2 ? result1 : result2)) / 60 / 60 / 1000;
                        shijiworkHours = String.valueOf(result3);
                    }
                }
                else
                {
                    //上午上班时间
                    result1 = sdf.parse(lunchbreak_start).getTime() - sdf.parse(time_start).getTime();
                    //下午上班时间
                    result2 = sdf.parse(time_end).getTime() - sdf.parse(lunchbreak_end).getTime();
                    result3 = Double.valueOf(String.valueOf(result1 > result2 ? result1 : result2)) / 60 / 60 / 1000;
                    shijiworkHours = String.valueOf(result3);
                }
            }
        return shijiworkHours;
    }

    //时间段判断
    public String timeLength(String time_start,String time_end,String lunchbreak_start, String lunchbreak_end) throws Exception {
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


    //代休添加
    /*public void insertReplacerest(Overtime overtime, TokenModel tokenModel) throws Exception {
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
    }*/
}
