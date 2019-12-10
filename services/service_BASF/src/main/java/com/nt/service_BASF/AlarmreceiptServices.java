package com.nt.service_BASF;

import com.nt.dao_BASF.Alarmreceipt;
import com.nt.dao_BASF.VO.AlarmreceiptVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;


public interface AlarmreceiptServices {
    //
    //获取所有报警单信息
    List<AlarmreceiptVo> getList(String alarmreceipttype) throws Exception;

    //创建报警单信息
    void insert(Alarmreceipt alarmreceipt, TokenModel tokenModel) throws Exception;

    //更新报警单信息
    void update(Alarmreceipt alarmreceipt, TokenModel tokenModel) throws Exception;

    //获取报警单信息
    AlarmreceiptVo select(String alarmreceiptid) throws Exception;
}
