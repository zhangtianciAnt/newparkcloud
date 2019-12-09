package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AttendanceService {

    void insert(Attendance attendance, TokenModel tokenModel) throws Exception;

    List<Attendance> list(Attendance attendance) throws Exception;

    void upd(Attendance attendance, TokenModel tokenModel) throws Exception;
}
