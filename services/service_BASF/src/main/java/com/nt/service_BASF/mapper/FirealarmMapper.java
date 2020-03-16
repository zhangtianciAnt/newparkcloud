package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Firealarm;
import com.nt.dao_BASF.VO.FireAlarmStatisticsVo;
import com.nt.dao_BASF.VO.FireAlarmVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface FirealarmMapper extends MyMapper<Firealarm> {


    //获取当月接警数据
    List<FireAlarmStatisticsVo> getFireAlarmStatistics() throws Exception;

    //获取接警事件记录
    List<FireAlarmVo> getFireAlarm() throws Exception;
}
