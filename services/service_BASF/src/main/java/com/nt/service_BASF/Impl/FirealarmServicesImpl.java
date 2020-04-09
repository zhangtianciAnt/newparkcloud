package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Firealarm;
import com.nt.dao_BASF.VO.FireAlarmStatisticsVo;
import com.nt.dao_BASF.VO.FireAlarmVo;
import com.nt.service_BASF.FirealarmServices;
import com.nt.service_BASF.mapper.FirealarmMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF10201ServicesImpl
 * @Author: Wxz
 * @Description: BASF报警单模块实现类
 * @Date: 2019/11/12 10:50
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FirealarmServicesImpl implements FirealarmServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private FirealarmMapper firealarmMapper;

    /**
     * @param firealarm
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取报警单列表
     * @Return java.util.List<Firealarm>
     * @Date 2019/11/12 10:54
     */
    @Override
    public List<Firealarm> list() throws Exception {
        Firealarm firealarm = new Firealarm();
        return firealarmMapper.select(firealarm);
    }

    /**
     * @param firealarm
     * @param tokenModel
     * @Method insert
     * @Author Wxz
     * @Version 1.0
     * @Description 创建报警单
     * @Return void
     * @Date 2019/11/12 10:55
     */
    @Override
    public String insert(Firealarm firealarm, TokenModel tokenModel) throws Exception {

        Firealarm firealarm1 =new Firealarm();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date()).toString();
        int a = firealarmMapper.selectCount(firealarm1);
        String countno =new DecimalFormat("00").format(a+1);
        String typec = null;
//        switch (firealarm.getTypacc()){
//            case "BC013001": typec = "FAS";//火灾事故
//                break;
//            case "BC013002": typec = "PDA";//生产事故
//                break;
//            case "BC013003": typec = "EPA";//爆炸事故
//                break;
//            case "BC013004": typec = "OTA";//其他事故
//                break;
//        }

            typec = "FAS";

        firealarm.setFirealarmno(yyMMdd+typec+countno);

        firealarm.preInsert(tokenModel);
        String ccid = UUID.randomUUID().toString();
        firealarm.setFirealarmid(ccid);
        firealarm.setCompletesta("0");
        firealarm.setMisinformation("0");
        firealarmMapper.insert(firealarm);
        return ccid;
    }

    /**
     * @param firealarm
     * @Method Delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除报警单
     * @Return void
     * @Date 2019/11/12 11：06
     */
    @Override
    public void delete(Firealarm firealarm) throws Exception {
        //逻辑删除（status -> "1"）
        firealarmMapper.updateByPrimaryKeySelective(firealarm);
    }

    /**
     * @param firealarmid
     * @Method one
     * @Author Wxz
     * @Version 1.0
     * @Description 获取报警单详情
     * @Return com.nt.dao_BASF.Firealarm
     * @Date 2019/11/12 11：07
     */
    @Override
    public Firealarm one(String firealarmid) throws Exception {
        return firealarmMapper.selectByPrimaryKey(firealarmid);
    }

    /**
     * @param firealarm
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新报警单详情
     * @Return void
     * @Date 2019/11/12 11：07
     */
    @Override
    public void update(Firealarm firealarm, TokenModel tokenModel) throws Exception {
        firealarm.preUpdate(tokenModel);
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
//        Date date = new Date(System.currentTimeMillis());
//        firealarm.setPolicetim(formatter.format(date));
        firealarmMapper.updateByPrimaryKey(firealarm);
    }

    /**
     * @param firealarm
     * @Method upcompletesta
     * @Author 王哲
     * @Version 1.0
     * @Description 更新报警单状态
     * @Return void
     * @Date 2019/11/22 16：45
     */
    @Override
    public void upcompletesta(Firealarm firealarm, TokenModel tokenModel) throws Exception {
        //状态更新（completesta -> "1"）
        firealarmMapper.updateByPrimaryKeySelective(firealarm);
    }

    /**
     * @return 最近30日接警数据统计
     * @Method getFireAlarmStatistics
     * @Author SKAIXX
     * @Description 获取最近30日平台接警数据统计结果
     * @Date 2020/1/7 14:04
     * @Param
     **/
    @Override
    public List<FireAlarmStatisticsVo> getFireAlarmStatistics() throws Exception {
        return firealarmMapper.getFireAlarmStatistics();
    }

    /**
     * @return 接警事件记录
     * @Method getFireAlarm
     * @Author SKAIXX
     * @Description 获取接警事件记录
     * @Date 2020/1/7 16:08
     * @Param
     **/
    @Override
    public List<FireAlarmVo> getFireAlarm() throws Exception {
        return firealarmMapper.getFireAlarm();
    }

    /**
     * @return 今日事件列表
     * @Method getSameDayFireAlarm
     * @Author GJ
     * @Description 获取今日事件列表
     * @Date 2020/3/26 15:43
     * @Param
     **/
    @Override
    public List<FireAlarmVo> getSameDayFireAlarm() throws Exception {
        return firealarmMapper.getSameDayFireAlarm();
    }

    /**
     * @return 本周事件列表
     * @Method getWeekFireAlarm
     * @Author GJ
     * @Description 获取本周事件列表
     * @Date 2020/3/26 15:43
     * @Param
     **/
    @Override
    public List<FireAlarmVo> getWeekFireAlarm() throws Exception {
        return firealarmMapper.getWeekFireAlarm();
    }
}
