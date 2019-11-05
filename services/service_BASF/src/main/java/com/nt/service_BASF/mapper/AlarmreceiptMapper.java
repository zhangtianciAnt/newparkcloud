package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Alarmreceipt;
import com.nt.dao_BASF.VO.AlarmreceiptVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlarmreceiptMapper extends MyMapper<Alarmreceipt> {

    List<AlarmreceiptVo> selectAlarmreceiptVoList();

    AlarmreceiptVo selectAlarmreceiptVo(@Param("alarmreceiptid") String alarmreceiptid);
}