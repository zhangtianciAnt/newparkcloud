package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.AttendanceSetting;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AttendanceSettingService {

    void insert(AttendanceSetting attendanceSetting, TokenModel tokenModel) throws Exception;

    List<AttendanceSetting> list(AttendanceSetting attendanceSetting) throws Exception;

    void update(AttendanceSetting attendanceSetting, TokenModel tokenModel) throws Exception;

    AttendanceSetting One(String attendancesetting_id) throws Exception;
}
