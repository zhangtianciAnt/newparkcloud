package com.nt.service_AOCHUAN.AOCHUAN6000;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.utils.dao.TokenModel;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AttendancesService {

    List<Attendance> get(Attendance attendance) throws Exception;
//    异常数据
    List<Attendance> getYICHANG(Attendance attendance) throws Exception;

    List<Attendance> getNow(Attendance attendance) throws Exception;

    List<Attendance> getNowMons(String id, String attendancetim) throws Exception;

    List<Attendance> getNowMonYC(String id,String attendancetim) throws Exception;

    List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception ;

    List<Attendance> getByUserId(String userId) throws Exception;




}
