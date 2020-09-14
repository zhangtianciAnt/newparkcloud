package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Pimsalarm;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PimsAlarmMapper extends MyMapper<Pimsalarm> {
    List<Pimsalarm> getAllPimsAlarm(@Param("type") String type);
}
