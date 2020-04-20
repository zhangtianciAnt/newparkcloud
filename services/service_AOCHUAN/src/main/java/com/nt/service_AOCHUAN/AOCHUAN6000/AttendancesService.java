package com.nt.service_AOCHUAN.AOCHUAN6000;


import com.nt.dao_AOCHUAN.AOCHUAN3000.Returngoods;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AttendancesService {

    List<Attendance> get(Attendance attendance) throws Exception;

    List<Attendance> getNow(Attendance attendance) throws Exception;

    List<Attendance> getNowMons(String attendancetim) throws Exception;





}
