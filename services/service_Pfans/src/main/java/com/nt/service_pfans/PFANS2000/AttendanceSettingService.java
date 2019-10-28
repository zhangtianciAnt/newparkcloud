package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.AttendanceSetting;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AttendanceSettingService {

    public void insert(AttendanceSetting attendanceSetting, TokenModel tokenModel)throws Exception;

    public List<AttendanceSetting> list(AttendanceSetting attendanceSetting) throws Exception;

    public void update(AttendanceSetting attendanceSetting, TokenModel tokenModel)throws Exception;

    public  AttendanceSetting One(String attendancesetting_id)throws Exception;
}
