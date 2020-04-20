package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.service_AOCHUAN.AOCHUAN6000.AttendancesService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.AttendancesMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class AttendancesServiceImpl implements AttendancesService {

    @Autowired
    private AttendancesMapper attendanceMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;


    @Override
    public List<Attendance> get(Attendance attendance) throws Exception {
        return attendanceMapper.select(attendance);
    }

    @Override
    public List<Attendance> getNow(Attendance attendance) throws Exception {
        return attendanceMapper.select(attendance);
    }

    @Override
    public List<Attendance> getNowMons(String attendancetim) throws Exception {
        return attendanceMapper.getNowMon(attendancetim);
    }
}
