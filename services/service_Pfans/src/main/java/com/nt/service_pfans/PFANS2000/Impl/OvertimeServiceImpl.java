package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.OvertimeService;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.service_pfans.PFANS2000.mapper.*;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private PunchcardRecordService punchcardRecordService;


    @Override
    public List<Overtime> getOvertimeDay(Overtime overtime) throws Exception {
        List<Overtime> overtimelist =overtimeMapper.getOvertimeDay(overtime.getOvertimetype(),DateUtil.format(overtime.getReserveovertimedate(),"yyyy-MM-dd"),overtime.getUserid());
        overtimelist = overtimelist.stream().filter(item -> (!item.getStatus().equals("1"))).collect(Collectors.toList());
        return overtimelist;
    }

    @Override
    public List<Overtime> getOvertimeOne(Overtime overtime) throws Exception {
        List<Overtime> overtimelist = overtimeMapper.getOvertimeOne(DateUtil.format(overtime.getReserveovertimedate(),"yyyy-MM-dd"),overtime.getUserid());
        //upd ccm 20210901 加班申请同一天不能重复申请，时间为0不算在内 fr
        //overtimelist = overtimelist.stream().filter(item -> (!item.getStatus().equals("1"))).collect(Collectors.toList());
        //return overtimelist;
        List<Overtime> olist = new ArrayList<>();
        for(Overtime o :overtimelist)
        {
            if(o.getStatus().equals("0") || o.getStatus().equals("2") || o.getStatus().equals("3") || o.getStatus().equals("4") || o.getStatus().equals("6") )
            {
                if(Double.valueOf(o.getReserveovertime()) == 0d)
                {
                    continue;
                }
                else
                {
                    olist.add(o);
                }
            }
            else if(o.getStatus().equals("5") || o.getStatus().equals("7"))
            {
                if(Double.valueOf(o.getActualovertime()) == 0d)
                {
                    continue;
                }
                else
                {
                    olist.add(o);
                }
            }
        }
        return olist;
        //upd ccm 20210901 加班申请同一天不能重复申请，时间为0不算在内 to
    }

    @Override
    public List<Overtime> getOvertime(Overtime overtime) throws Exception {
        //add-ws-9/4-加班申请可删除任务
        List<Overtime> overtimelist = overtimeMapper.select(overtime);
        overtimelist = overtimelist.stream().filter(item -> (!item.getStatus().equals("1"))).collect(Collectors.toList());
        return overtimelist;
        //add-ws-9/4-加班申请可删除任务
    }
    //add-ws-9/4-加班申请可删除任务
    @Override
    public void delete(Overtime overtime) throws Exception {
        Overtime over = new Overtime();
        Overtime overtimes = overtimeMapper.selectByPrimaryKey( overtime.getOvertimeid());
        BeanUtils.copyProperties(overtimes, over);
        over.setStatus(AuthConstants.DEL_FLAG_DELETE);
        overtimeMapper.updateByPrimaryKey(over);
    }
    //add-ws-9/4-加班申请可删除任务
    @Override
    public List<Overtime> getOvertimelist(Overtime overtime) throws Exception {
        List<Overtime> overtimelist = overtimeMapper.select(overtime);
        overtimelist = overtimelist.stream().filter(item -> (!item.getStatus().equals("1"))).collect(Collectors.toList());
        return overtimelist;
    }
    @Override
    public Overtime One(String overtimeid) throws Exception {
        return overtimeMapper.selectByPrimaryKey(overtimeid);
    }

    @Override
    public void insertOvertime(Overtime overtime, TokenModel tokenModel) throws Exception {
        String strtus = overtime.getStatus();

        overtime.preInsert(tokenModel);
        //总经理新建自动通过
//        if(strtus.equals(AuthConstants.APPROVED_FLAG_YES)){
//            overtime.setStatus(AuthConstants.APPROVED_FLAG_YES);
//        }
        overtime.setOvertimeid(UUID.randomUUID().toString());
        overtimeMapper.insert(overtime);
    }

    @Override
    public void updateOvertime(Overtime overtime, TokenModel tokenModel) throws Exception {
        overtime.preUpdate(tokenModel);
        overtimeMapper.updateByPrimaryKey(overtime);
        //add ccm 2020729 加班实时反应
        if(overtime.getStatus().equals("4") || overtime.getStatus().equals("7"))
        {
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(overtime.getReserveovertimedate());
            punchcardRecordService.methodAttendance_b(calStart,overtime.getUserid());
        }
        //add ccm 2020729 加班实时反应
    }

    public String workhours(String time_start,String time_end,String lunchbreak_start, String lunchbreak_end,Attendance ad) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            String shijiworkHours ="0";
            if(sdf.parse(time_end).getTime() <= sdf.parse(lunchbreak_start).getTime() || sdf.parse(time_start).getTime() >= sdf.parse(lunchbreak_end).getTime())
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
            replacerest.setApplication_date(DateUtil.format(new Date(),"yyyy/MM/dd"));
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
