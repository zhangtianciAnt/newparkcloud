package com.nt.controller.Config.BASF;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlViewDepartment;
import com.nt.service_BASF.*;
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

import java.time.LocalDateTime;
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
    private PersonnelPermissionsServices personnelPermissionsServices;

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
                    if("class2".equals(((PersonnelPermissions) personnelPermissionsList.get(i)).getClassname())){
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
     * @Description ERC大屏7个装置内人数统计
     * @Date 2020/03/31 11:21
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectDeviceUsersCount() throws Exception {
        // 7个装置人数统计
        Map<String, Integer> deviceInfoList=new HashMap<>();
        List userCnt = basfUserInfoMapper.selectDeviceUsersCnt();
        List<Integer> deviceUserCnt = new ArrayList<>();
        if (userCnt.size() > 0) {
            for (int i = 0; i < userCnt.size(); i++) {
                deviceInfoList.put(((SqlAPBCardHolder) userCnt.get(i)).getApbid(),((SqlAPBCardHolder) userCnt.get(i)).getCnt());
            }
            if (deviceInfoList.size() > 0) {
                //CCP
                if(deviceInfoList.get("14189") > 0){
                    deviceUserCnt.add(deviceInfoList.get("14189"));
                }else{
                    deviceUserCnt.add(0);
                }
                //PTHF
                if(deviceInfoList.get("14185") > 0){
                    deviceUserCnt.add(deviceInfoList.get("14185"));
                }else{
                    deviceUserCnt.add(0);
                }
                //Basonat
                if(deviceInfoList.get("14187") > 0){
                    deviceUserCnt.add(deviceInfoList.get("14187"));
                }else{
                    deviceUserCnt.add(0);
                }
                //BMW大楼
                if(deviceInfoList.get("14195") > 0){
                    deviceUserCnt.add(deviceInfoList.get("14195"));
                }else{
                    deviceUserCnt.add(0);
                }
                //PA6
                if(deviceInfoList.get("14191") > 0){
                    deviceUserCnt.add(deviceInfoList.get("14191"));
                }else{
                    deviceUserCnt.add(0);
                }
                //树脂
                if(deviceInfoList.get("14193") > 0){
                    deviceUserCnt.add(deviceInfoList.get("14193"));
                }else{
                    deviceUserCnt.add(0);
                }
                //PA6仓库
                if(deviceInfoList.get("14197") > 0){
                    deviceUserCnt.add(deviceInfoList.get("14197"));
                }else{
                    deviceUserCnt.add(0);
                }
            }
        }

        webSocketVo.setDeviceUsersCountList(deviceUserCnt);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return void
     * @Method selectDeviceOutUsersCount
     * @Author GJ
     * @Description ERC大屏7个装置外人数统计
     * @Date 2020/04/01 09:44
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectDeviceOutUsersCount() throws Exception {
        // 7个装置人数统计
        Map<String, Integer> deviceInfoList=new HashMap<>();
        List userCnt = basfUserInfoMapper.selectDeviceOutUsersCnt();
        List<Integer> deviceOutUserCnt = new ArrayList<>();
        if (userCnt.size() > 0) {
            for (int i = 0; i < userCnt.size(); i++) {
                deviceInfoList.put(((SqlAPBCardHolder) userCnt.get(i)).getApbid(),((SqlAPBCardHolder) userCnt.get(i)).getCnt());
            }
            if (deviceInfoList.size() > 0) {
                //CCP
                if(deviceInfoList.get("14190") > 0){
                    deviceOutUserCnt.add(deviceInfoList.get("14190"));
                }else{
                    deviceOutUserCnt.add(0);
                }
                //PTHF
                if(deviceInfoList.get("14186") > 0){
                    deviceOutUserCnt.add(deviceInfoList.get("14186"));
                }else{
                    deviceOutUserCnt.add(0);
                }
                //Basonat
                if(deviceInfoList.get("14188") > 0){
                    deviceOutUserCnt.add(deviceInfoList.get("14188"));
                }else{
                    deviceOutUserCnt.add(0);
                }
                //BMW大楼
                if(deviceInfoList.get("14196") > 0){
                    deviceOutUserCnt.add(deviceInfoList.get("14196"));
                }else{
                    deviceOutUserCnt.add(0);
                }
                //PA6
                if(deviceInfoList.get("14192") > 0){
                    deviceOutUserCnt.add(deviceInfoList.get("14192"));
                }else{
                    deviceOutUserCnt.add(0);
                }
                //树脂
                if(deviceInfoList.get("14194") > 0){
                    deviceOutUserCnt.add(deviceInfoList.get("14194"));
                }else{
                    deviceOutUserCnt.add(0);
                }
                //PA6仓库
                if(deviceInfoList.get("14198") > 0){
                    deviceOutUserCnt.add(deviceInfoList.get("14198"));
                }else{
                    deviceOutUserCnt.add(0);
                }
            }
        }

        webSocketVo.setDeviceOutUsersCountList(deviceOutUserCnt);
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
     * @Description ERC大屏车辆定位模块车辆出入统计
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
    // endregion
}
