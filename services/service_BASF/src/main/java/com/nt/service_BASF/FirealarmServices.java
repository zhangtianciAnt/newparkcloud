package com.nt.service_BASF;

import com.nt.dao_BASF.Firealarm;
import com.nt.dao_BASF.VO.FireAlarmStatisticsVo;
import com.nt.dao_BASF.VO.FireAlarmVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF10201Services
 * @Author: Wxz
 * @Description:报警单管理接口
 * @Date: 2019/11/12 10:36
 * @Version: 1.0
 */
public interface FirealarmServices {

    //获取报警单列表
    List<Firealarm> list() throws Exception;

    //获取报警单列表
    List<Firealarm> list(Firealarm firealarm) throws Exception;

    //创建报警单
    String insert(Firealarm firealarm, TokenModel tokenModel) throws Exception;

    //删除报警单
    void delete(Firealarm firealarm)throws Exception;

    //获取报警单详情
    Firealarm one(String firealarmid) throws Exception;

    //更新报警单
    void update(Firealarm firealarm, TokenModel tokenModel)throws Exception;


    //获取当月接警数据
    List<FireAlarmStatisticsVo> getFireAlarmStatistics() throws Exception;

    //获取接警事件记录
    List<FireAlarmVo> getFireAlarm() throws Exception;

    //获取今日事件列表
    List<FireAlarmVo> getSameDayFireAlarm() throws Exception;

    //获取本周事件列表
    List<FireAlarmVo> getWeekFireAlarm() throws Exception;
}
