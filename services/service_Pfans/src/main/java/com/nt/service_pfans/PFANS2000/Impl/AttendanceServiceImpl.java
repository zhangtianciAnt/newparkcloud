package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.service_pfans.PFANS2000.AttendanceService;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return attendanceMapper.select(attendance);
    }
}
