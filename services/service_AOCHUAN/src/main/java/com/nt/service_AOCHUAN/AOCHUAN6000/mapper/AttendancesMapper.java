package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AttendancesMapper extends MyMapper<Attendance> {

    public List<Attendance> getNowMon(@Param("attendancetim") String attendancetim, @Param("owners")List<String> owners);

    public List<Attendance> getNowMonYC(@Param("Mon") String Mon, @Param("Tus") String Tus, @Param("Wed") String Wed, @Param("Thu") String Thu, @Param("Fri") String Fri, @Param("attendancetim") String attendancetim, @Param("owners")List<String> owners);

    public List<Attendance> getYICHANG(@Param("Mon") String Mon, @Param("Tus") String Tus, @Param("Wed") String Wed, @Param("Thu") String Thu, @Param("Fri") String Fri);

    public List<Attendance> getByUserId(@Param("id") String userId, @Param("nowMons") String nowMons, @Param("lastMons") String lastMons);

    //更新
    public List<Attendance> getStatus(@Param("attendancetim") String AttendAnCetim);

    //    删除
    public List<Attendance> getdel(@Param("id") String Status);
}
