package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.AttendanceSetting;
import com.nt.service_pfans.PFANS2000.AttendanceSettingService;
import com.nt.service_pfans.PFANS2000.mapper.AttendanceSettingMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceSettingServiceImpl implements AttendanceSettingService {

    @Autowired
    private AttendanceSettingMapper attendanceSettingMapper;

    @Override
    public List<AttendanceSetting> list(AttendanceSetting attendanceSetting) throws Exception {
        return attendanceSettingMapper.select(attendanceSetting);
    }

    @Override
    public void insert(AttendanceSetting attendanceSetting, TokenModel tokenModel) throws Exception {
        attendanceSetting.preInsert(tokenModel);
        attendanceSetting.setAttendancesetting_id(UUID.randomUUID().toString());
        attendanceSettingMapper.insert(attendanceSetting);
    }

    @Override
    public void update(AttendanceSetting attendanceSetting, TokenModel tokenModel) throws Exception {
        attendanceSetting.preUpdate(tokenModel);
        attendanceSettingMapper.updateByPrimaryKeySelective(attendanceSetting);
    }

    @Override
    public AttendanceSetting One(String attendancesetting_id) throws Exception {
        return attendanceSettingMapper.selectByPrimaryKey(attendancesetting_id);
    }
}
