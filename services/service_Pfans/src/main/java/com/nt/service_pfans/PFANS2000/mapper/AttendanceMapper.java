package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceReport;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;


public interface AttendanceMapper extends MyMapper<Attendance> {

    List<Attendance> getAttendance(Attendance attendance);

    List<Attendance> selectAttendance(AbNormal abNormal);

    Double selectAttenSumSick(Attendance attendance);

    void updStatus(@Param("user_id") String user_id, @Param("years") String years, @Param("months") String months);

    //承认当前数据
    void updStatusre(@Param("user_id") String user_id, @Param("ids") List<Attendance> ids);

    void updStatus1(@Param("user_id") String user_id, @Param("years") String years, @Param("months") String months);

    //ccm 20200713 离职考勤对比  查询 更新 插入
    List<Attendance> selectResignationAll(@Param("user_id") String user_id, @Param("years") String years, @Param("months") String months);

    List<Attendance> selectResignation(@Param("user_id") String user_id, @Param("years") String years, @Param("months") String months);
    //ccm 20200713 离职考勤对比  查询 更新 插入

    //add-ws-1/5-根据当前月份和当前月的上个月获取数据
    @Select("select * from attendance where (years=#{strYear} or years=#{accYear}) and (MONTHS=#{strTemp} or MONTHS=#{accDate}) and user_id=#{userid}")
    List<Attendance> selectDataList( @Param("strTemp") String strTemp, @Param("strYear") String strYear, @Param("accDate") String accDate, @Param("accYear") String accYear,@Param("userid") String userid);
    //add-ws-1/5-根据当前月份和当前月的上个月获取数据

    //add   被承认过的日期不可申请考勤异常  from
    @Select("select * from attendance where user_id = #{userid} and dates between  #{occurrence_date} and  #{finish_date}")
    List<Attendance> selTime(@Param("userid") String userid, @Param("occurrence_date") Date occurrence_date, @Param("finish_date") Date finish_date);

    @Select("select * from attendance where user_id = #{userid} and dates between  #{re_occurrence_date} and  #{re_finish_date}")
    List<Attendance> twoTime(@Param("userid") String userid, @Param("re_occurrence_date") Date re_occurrence_date, @Param("re_finish_date") Date re_finish_date);
    //add   被承认过的日期不可申请考勤异常  to

    //考勤导出 1125 ztc fr
    List<AttendanceReport> getAttInfo(@Param("year")String year, @Param("month")String month);
    //考勤导出 1125 ztc to
}
