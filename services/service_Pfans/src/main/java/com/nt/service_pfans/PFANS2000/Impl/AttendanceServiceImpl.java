package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.service_pfans.PFANS2000.AttendanceService;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nt.utils.dao.TokenModel;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Override
    public List<Attendance> getlist(Attendance attendance) throws Exception {
        return attendanceMapper.getAttendance(attendance);
    }

    @Override
    public List<Attendance> getAttendancelist(Attendance attendance) throws Exception {
        //add-ws-5/6-根据当前月份和当前月的上个月获取数据
        List<Attendance> attendancelist = attendanceMapper.select(attendance);
        SimpleDateFormat sf1 = new SimpleDateFormat("MM");
        String strTemp = sf1.format(new Date());
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        attendancelist = attendancelist.stream()
                .filter(item -> (item.getMonths().equals(accDate) || item.getMonths().equals(strTemp)))
                .collect(Collectors.toList());
        //add-ws-5/6-根据当前月份和当前月的上个月获取数据
        return attendancelist;
    }

    @Override
    public void update(Attendance attendance, TokenModel tokenModel) throws Exception {
        attendance.preUpdate(tokenModel);
        attendanceMapper.updateByPrimaryKeySelective(attendance);
    }
}
