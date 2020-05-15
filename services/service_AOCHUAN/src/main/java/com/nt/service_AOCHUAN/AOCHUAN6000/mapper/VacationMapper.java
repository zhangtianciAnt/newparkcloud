package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vacation;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.LeaveDaysVo;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.StatisticsVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface VacationMapper extends MyMapper<Vacation> {
    public List<StatisticsVo> getVo();

    public LeaveDaysVo getVacation(@Param("id") String id);

    public List<Attendance> getTim(@Param("id") String id,@Param("Sta") String Sta,@Param("Sun") String Sun);
}
