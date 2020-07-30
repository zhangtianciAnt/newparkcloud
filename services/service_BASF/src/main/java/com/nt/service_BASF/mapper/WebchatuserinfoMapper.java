package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Alarmreceipt;
import com.nt.dao_BASF.VO.AlarmreceiptVo;
import com.nt.dao_BASF.Webchatuserinfo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebchatuserinfoMapper extends MyMapper<Alarmreceipt> {
    // 取得登录用户密码
    Webchatuserinfo selectPwd(@Param("loginid") String loginid) throws Exception;

    AlarmreceiptVo selectAlarmreceiptVo(@Param("alarmreceiptid") String alarmreceiptid) throws Exception;
}
