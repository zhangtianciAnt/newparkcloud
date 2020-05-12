package com.nt.controller.Config.BASF;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Firealarm;
import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.dao_BASF.Switchnotifications;
import com.nt.dao_BASF.VO.DeviceAndSqlUserinfoVo;
import com.nt.dao_BASF.VO.DeviceinformationVo;
import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlViewDepartment;
import com.nt.service_BASF.*;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_SQL.SqlUserInfoServices;
import com.nt.service_SQL.sqlMapper.BasfUserInfoMapper;
import com.nt.service_SQL.sqlMapper.SqlUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.util.*;

/**
 * basf多线程定时任务
 */
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class MultiThreadScheduleTask {

    @Autowired
    @SuppressWarnings("all")
    DeviceInformationServices deviceInformationServices;

    @Autowired
    @SuppressWarnings("all")
    BasfUserInfoMapper basfUserInfoMapper;

    @Autowired
    @SuppressWarnings("all")
    VehicleinformationServices vehicleinformationServices;

    @Autowired
    @SuppressWarnings("all")
    FirealarmServices firealarmServices;

    @Autowired
    @SuppressWarnings("all")
    StartprogramServices startprogramServices;

    @Autowired
    @SuppressWarnings("all")
    TrainjoinlistServices trainjoinlistServices;

    //MongoDB
    @Autowired
    @SuppressWarnings("all")
    RiskassessmentServices riskassessmentServices;

    //MySql
    @Autowired
    @SuppressWarnings("all")
    RiskassessmentsServices riskassessmentsServices;

    @Autowired
    EmergencyplanServices emergencyplanServices;

    @Autowired
    private ResponseinformationServices responseinformationServices;

    @Autowired
    private SqlUserInfoServices sqluserinfoservices;

    @Autowired
    private SqlUserInfoMapper sqlUserInfoMapper;

    @Autowired
    @SuppressWarnings("all")
    private HighriskareaServices highriskareaServices;

    @Autowired
    @SuppressWarnings("all")
    private ApplicationServices applicationServices;

    @Autowired
    private PersonnelPermissionsServices personnelPermissionsServices;

    @Autowired
    private SwitchnotificationsServices switchnotificationsServices;

    @Autowired
    private DeviceinformationMapper deviceinformationMapper;

    // websocket消息推送
    private WebSocket ws = new WebSocket();
    // WebSocketVow
    private WebSocketVo webSocketVo = new WebSocketVo();

    /**
     * @return void
     * @Method selectUsersCount
     * @Author GJ
     * @Description ERC大屏在厂人数统计
     * @Date 2020/03/30 18:10
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectUsersCount() throws Exception {
        Map<String,String> departmentInfoList=new HashMap<>();
        List departlist = sqlUserInfoMapper.selectdepartment();
        List apblist = sqlUserInfoMapper.selectapbcardholder();
        List<PersonnelPermissions> personnelPermissionsList = personnelPermissionsServices.list();
        String companystaff="";
        String supplier="";
        String foreignworkers="";

        // 查询员工人数
        int YG = 0;
        // 查询访客人数
        int FK = 0;
        // 查询承包商人数
        int CBS = 0;
        // 在厂总人数
        int ZALL = 0;

        if (apblist.size() > 0) {
            if (departlist.size() > 0) {
                for (int i = 0; i < departlist.size(); i++) {
                    departmentInfoList.put(((SqlViewDepartment) departlist.get(i)).getRecnum(),((SqlViewDepartment) departlist.get(i)).getDepartmentpeid());
                }
            }
            if (personnelPermissionsList.size() > 0) {
                for (int i = 0; i < personnelPermissionsList.size(); i++) {
                    if("class1".equals(((PersonnelPermissions) personnelPermissionsList.get(i)).getClassname())){
                        if("".equals(companystaff)){
                            companystaff=((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }else{
                            companystaff=companystaff+","+((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }
                    }
                    if("class2".equals(((PersonnelPermissions) personnelPermissionsList.get(i)).getClassname())){
                        if("".equals(foreignworkers)){
                            foreignworkers=((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }else{
                            foreignworkers=foreignworkers+","+((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }
                    }
                    if("class3".equals(((PersonnelPermissions) personnelPermissionsList.get(i)).getClassname())){
                        if("".equals(supplier)){
                            supplier=((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }else{
                            supplier=supplier+","+((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }
                    }
                }
            }
            List<String> companystaffList = Arrays.asList(companystaff.split(","));
            List<String> supplierList = Arrays.asList(supplier.split(","));
            List<String> foreignworkersList = Arrays.asList(foreignworkers.split(","));

            for (int i = 0; i < apblist.size(); i++) {
                String departmentID = ((SqlAPBCardHolder) apblist.get(i)).getDepartmentid();
                for (int j = 1; j<10; j++) {
                    if ("-1".equals(departmentInfoList.get(departmentID))) {
                        for(int z = 0; z<companystaffList.size(); z++) {
                            if(companystaffList.get(z).equals(departmentID)){
                                YG += 1;
                            }
                        }
                        for(int z = 0; z<supplierList.size(); z++) {
                            if(supplierList.get(z).equals(departmentID)){
                                CBS += 1;
                            }
                        }
                        for(int z = 0; z<foreignworkersList.size(); z++) {
                            if(foreignworkersList.get(z).equals(departmentID)){
                                FK += 1;
                            }
                        }
                        j=10;
                    }else {
                        departmentID=departmentInfoList.get(departmentID);
                    }
                }
            }
        }

        ZALL = YG + FK + CBS;
        webSocketVo.setUsersCount(YG);
        webSocketVo.setContractorsCount(CBS);
        webSocketVo.setVisitorsCount(FK);
        webSocketVo.setAllUsersCount(ZALL);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return void
     * @Method selectDeviceUsersCount
     * @Author GJ
     * @Description ERC大屏装置人数统计
     * @Date 2020/03/31 11:21
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectDeviceUsersCount() throws Exception {
        // 装置人数统计
        List<SqlAPBCardHolder> userCnt = basfUserInfoMapper.selectDeviceUsersCnt();
        List<SqlAPBCardHolder> userCntLast =new ArrayList<>();
        String lastName="";
        if (userCnt.size() > 0) {
            int j=-1;
            for (int i = 0; i < userCnt.size(); i++) {
                if(userCnt.get(i).getApbname().contains("（内）")){
                    SqlAPBCardHolder tempApb = new SqlAPBCardHolder();
                    if(i != 0){
                        if(1!=userCnt.get(i-1).getApbflg()){
                            userCntLast.get(j).setApbflg(1);
                            userCntLast.get(j).setOutCnt(0);
                        }
                    }

                    String[] strs=userCnt.get(i).getApbname().split("（内）");
                    tempApb.setApbid(userCnt.get(i).getApbid());
                    tempApb.setApbname(strs[0]+"装置");
                    tempApb.setCnt(userCnt.get(i).getCnt());

                    userCnt.get(i).setApbflg(0);
                    userCnt.get(i).setApbname(strs[0]+"装置");
                    userCntLast.add(userCnt.get(i));
                    j=j+1;
                    lastName=userCnt.get(i).getApbname();

                    if(i==userCnt.size()-1){
                        userCntLast.get(j).setOutCnt(0);
                    }
                }

                if(userCnt.get(i).getApbname().contains("（外）")){
                    String[] strs=userCnt.get(i).getApbname().split("（外）");
                    SqlAPBCardHolder tempApb = new SqlAPBCardHolder();
                    if(i==0 || !lastName.equals(strs[0]+"装置")){
                        tempApb.setCnt(0);
                        tempApb.setApbname(strs[0]+"装置");
                        tempApb.setApbid(userCnt.get(i).getApbid());
                        userCntLast.add(tempApb);
                        j=j+1;
                    }
                    userCnt.get(i).setApbflg(1);
                    userCnt.get(i).setApbname(strs[0]+"装置");
                    userCntLast.get(j).setOutCnt(userCnt.get(i).getCnt());
                    lastName=userCnt.get(i).getApbname();
                }
            }
        }

        webSocketVo.setDeviceUsersCountList(userCntLast);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    // region BASF90600 ERC-车辆定位

    /**
     * @return void
     * @Method BASF90600_GetInsideList
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块获取在场车辆信息一览
     * @Date 2019/12/30 15:47
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetInsideList() throws Exception {
        // 获取在场车辆信息一览
        webSocketVo.setInsideVehicleinformationVoList(vehicleinformationServices.getInsideList());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return void
     * @Method BASF90600_GetInsideVehicleType
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块在场车辆类别统计
     * @Date 2019/12/30 16:27
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetInsideVehicleType() throws Exception {
        // 在场车辆类别统计
        webSocketVo.setInsideVehicleTypeVoList(vehicleinformationServices.getInsideVehicleType());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return
     * @Method BASF90600_GetAccessStatistics
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块本月车辆出入统计
     * @Date 2019/12/30 19:09
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetAccessStatistics() throws Exception {
        // 车辆出入统计
        webSocketVo.setVehicleAccessStatisticsVoList(vehicleinformationServices.getAccessStatistics());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return
     * @Method BASF90600_GetWeekAccessStatistics
     * @Author GJ
     * @Description ERC大屏车辆定位模块本周车辆出入统计
     * @Date 2020/04/14 10:19
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetWeekAccessStatistics() throws Exception {
        // 车辆出入统计
        webSocketVo.setVehicleWeekAccessStatisticsVoList(vehicleinformationServices.getWeekAccessStatistics());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return
     * @Method BASF90600_GetDailyVehicleInfo
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块获取当日入场车辆信息
     * @Date 2019/12/31 11:27
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetDailyVehicleInfo() throws Exception {
        // 获取当日入场车辆信息
        webSocketVo.setVehicleinformationList(vehicleinformationServices.getDailyVehicleInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return
     * @Method BASF90600_GetQueryVehiclesRegularlyInfo
     * @Author SKAIXX
     * @Description ERC定时查询车辆信息表（出场时间为空的数据）
     * @Date 2020/04/13 14:56
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetQueryVehiclesRegularlyInfo() throws Exception {
        // 定时查询车辆信息表（出场时间为空的数据）
        webSocketVo.setQueryVehiclesRegularlyInfoList(vehicleinformationServices.getQueryVehiclesRegularlyInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }
    // endregion

    // region BASF90200 ERC-火灾消防

    /**
     * @return 近30日接警数据分析
     * @Method BASF90200_GetFireAlarmStatistics
     * @Author SKAIXX
     * @Description 获取当月接警数据分析
     * @Date 2020/1/7 14:17
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90200_GetFireAlarmStatistics() throws Exception {
        // 获取当月接警数据分析
        webSocketVo.setFireAlarmStatisticsVoList(firealarmServices.getFireAlarmStatistics());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90200_GetFireAlarmList() throws Exception {
        // 获取接警事件记录
        webSocketVo.setFireAlarmList(firealarmServices.getFireAlarm());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetMandatoryInfo() throws Exception {
        // 获取培训教育强制的通过/未通过
        webSocketVo.setPassingRateList(startprogramServices.getMandatoryInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetIsMandatoryInfo() throws Exception {
        // 获取培训教育非强制的通过/未通过
        webSocketVo.setPassingIsRateList(startprogramServices.getIsMandatoryInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF10802_GetEmergencyplanInfo() throws Exception {
        // 应急预案列表
        webSocketVo.setEmergencyplanList(emergencyplanServices.list());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF10803_GetResponseinformationInfo() throws Exception {
        //应急预案工艺处置队列表
        webSocketVo.setResponseinformationList(responseinformationServices.list());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASFSQL60001_GetSelectapbcard() throws Exception {
        //在厂人员列表
        webSocketVo.setSelectapbcardList(sqluserinfoservices.selectapbcard());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASFSQL60001_GetSelectapbid() throws Exception {
        //在厂人员列表
        webSocketVo.setSelectapbcardList(sqluserinfoservices.selectapbcard());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetOverduePersonnelList() throws Exception {
        //获取培训到期人员列表
        webSocketVo.setOverduePersonnelListVoList(trainjoinlistServices.overduepersonnellist());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetFutureProgram() throws Exception {
        //大屏培训信息推送列表
        webSocketVo.setFutureProgramList(startprogramServices.getFutureProgram());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90900_GetRiskassessment() throws Exception {
        //获取风险判研信息(MongoDB)
        webSocketVo.setRiskassessment(riskassessmentServices.getData());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90900_WriteList() throws Exception {
        //获取风险研判信息（MySql）
        webSocketVo.setRiskassessmentsList(riskassessmentsServices.writeList());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }


    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90905_GetSameDayFireAlarm() throws Exception {
        // 获取今日事件列表
        webSocketVo.setSameDayFireAlarm(firealarmServices.getSameDayFireAlarm());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90906_GetWeekFireAlarm() throws Exception {
        // 获取本周事件列表
        webSocketVo.setWeekFireAlarm(firealarmServices.getWeekFireAlarm());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90921_GetHighriskareaList() throws Exception {
        // 获取高风险作业清单
        webSocketVo.setHighriskareaList(highriskareaServices.list());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }


//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void BASF10105_GetDeviceinformationVoList() throws Exception {
//        List<DeviceinformationVo> deviceinformationVos=new ArrayList<>();
//        //获取非误报且未完成的消防报警单
//        Firealarm firealarm=new Firealarm();
//        firealarm.setCompletesta("0");
//        firealarm.setMisinformation("0");
//        List<Firealarm> firealarms=firealarmServices.list(firealarm);
//        //获取消防报警单对应的设备信息
//        for (Firealarm firealarm1 : firealarms) {
//            DeviceinformationVo deviceinformationVo = new DeviceinformationVo();
//            deviceinformationVo.setFirealarmuuid(firealarm1.getFirealarmid());
//            deviceinformationVo.setDeviceinformation(deviceInformationServices.one(firealarm1.getDeviceinformationid()));
//            deviceinformationVos.add(deviceinformationVo);
//        }
//        webSocketVo.setDeviceinformationList(deviceinformationVos);
//        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void BASF10201_GetFirealarmList() throws Exception {
//
//        //获取非误报且未完成的消防报警单
//        Firealarm firealarm = new Firealarm();
//        firealarm.setCompletesta("0");
//        firealarm.setMisinformation("0");
//        List<Firealarm> firealarms = firealarmServices.list(firealarm);
//
//        webSocketVo.setTopfirealarmList(firealarms);
//        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetVehicleinformationVolList() throws Exception {
        //获取车辆列表(危化品车辆数用)
        webSocketVo.setDangerousgoodsList(vehicleinformationServices.getlistinformation());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetVehicleinformationCount() throws Exception {
        //获取车辆列表(危化品车辆数用)
        webSocketVo.setCountdangerousgoods(vehicleinformationServices.getcountinformation());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF10105_GetDeviceAndSqlUserinfoVoList() throws Exception {
        DeviceAndSqlUserinfoVo deviceAndSqlUserinfoVo = new DeviceAndSqlUserinfoVo();
        List<DeviceAndSqlUserinfoVo> deviceAndSqlUserinfoVoList = new ArrayList<>();

        Deviceinformation deviceinformation = new Deviceinformation();
        deviceinformation.setDevicetype("BC004003");
        List<Deviceinformation> deviceinformationList = deviceinformationMapper.select(deviceinformation);
        for (int i = 0; i < deviceinformationList.size(); i++) {

//            for (int j = 0; j < departlist.size(); j++) {
//                if (deviceinformationList.get(i).getDeviceno() == departlist.get(j).getApbid()) {
            List<SqlAPBCardHolder> departlist = sqlUserInfoMapper.selectapbid(deviceinformationList.get(i).getDeviceno());
            if (departlist.size() > 0) {
                deviceAndSqlUserinfoVo.setDeviceinformation(deviceinformationList.get(i));
                deviceAndSqlUserinfoVo.setSqlUserInfoCnt(departlist.size());
                deviceAndSqlUserinfoVoList.add(deviceAndSqlUserinfoVo);
            }
        }
        webSocketVo.setDeviceAndSqlUserinfoVoList(deviceAndSqlUserinfoVoList);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    // endregion

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90620_GetRoadClosed() throws Exception{
        //道路占用/临时封闭区域列表
        webSocketVo.setRoadClosed(applicationServices.roadClosed());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void SENDEMAIL_Listswitch() throws Exception{
        //主备服务通知表
        Switchnotifications switchnotifications = new Switchnotifications();
        webSocketVo.setSwitchList(switchnotificationsServices.list(switchnotifications));
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }
}
