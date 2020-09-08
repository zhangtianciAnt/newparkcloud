package com.nt.controller.Config.BASF;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.annotations.JsonAdapter;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketDeviceinfoVo;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.dao_BASF.*;
import com.nt.dao_BASF.VO.DeviceAndSqlUserinfoVo;
import com.nt.dao_SQL.APBCardHolderVo;
import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlViewDepartment;
import com.nt.service_BASF.*;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_BASF.mapper.PimsPointMapper;
import com.nt.service_BASF.mapper.PimsdataMapper;
import com.nt.service_BASF.mapper.VehicleManagementMapper;
import com.nt.service_SQL.sqlMapper.BasfUserInfoMapper;
import com.nt.utils.CowBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    ChemicalsdsServices chemicalsdsServices;

    @Autowired
    private ResponseinformationServices responseinformationServices;

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

    @Resource
    private DeviceinformationMapper deviceinformationMapper;

    @Resource
    private PimsdataMapper pimsdataMapper;

    @Resource
    private PimsPointMapper pimsPointMapper;

    @Autowired
    private RestTemplate restTemplate;

    public static WebSocketVo webSocketVo = new WebSocketVo();

//    public static WebSocketDeviceinfoVo webSocketDeviceinfoVo = new WebSocketDeviceinfoVo();

    private static String Url;

    @Value("${sqlSerer.ip}")
    public void setHost(String Url) {
        this.Url = Url;
    }

    public static JSONArray returnpostlist(String urlstr) {
        String url = Url + urlstr;
        HashMap<String, Object> paramMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.post(url, paramMap));
        return (JSONArray) jsonObject.get("data");
    }

    public JSONArray returngetlist(String urlstr, String id) throws Exception {
//        String url = Url + urlstr;
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("selectapbid", id);
//        JSONObject jsonObject =JSONObject.parseObject(HttpUtil.get(url, paramMap));
//        return (JSONArray) jsonObject.get("data");

        String url = Url + urlstr;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("selectapbid", id);
        JSONObject jsonObject = new JSONObject();
        Switchnotifications switchnotifications = new Switchnotifications();
        try {
            jsonObject = JSONObject.parseObject(HttpUtil.get(url, paramMap));
            if (jsonObject.get("code").equals(-1)) {
                switchnotifications.setContent("门禁系统网络异常！");
                List<Switchnotifications> switchnotificationsList = switchnotificationsServices.list(switchnotifications);
                if (switchnotificationsList.size() > 0) {

                } else {
                    switchnotifications.setContent("门禁系统网络异常！");
                    switchnotificationsServices.create(switchnotifications);
                }
            }
        } catch (Exception ex) {
            switchnotifications.setContent("门禁系统连接被拒绝！");
//            List<Switchnotifications> switchnotificationsList = switchnotificationsServices.list(switchnotifications);
//            if (switchnotificationsList.size() > 0) {
//
//            } else {
//                switchnotifications.setContent("门禁系统连接被拒绝！");
//                switchnotificationsServices.create(switchnotifications);
//            }
        }

        return (JSONArray) jsonObject.get("data");
    }

    //    /**
//     * @return
//     * @Method BASF90600_GetQueryVehiclesRegularlyInfo
//     * @Author SKAIXX
//     * @Description ERC定时查询车辆信息表（出场时间为空的数据）
//     * @Date 2020/04/13 14:56
//     * @Param
//     **/
//    @Async
//    @Scheduled(fixedDelay = 10000)
//    public void BASF90600_GetQueryVehiclesRegularlyInfo() throws Exception {
//        // 定时查询车辆信息表（出场时间为空的数据）
//        webSocketVo.setQueryVehiclesRegularlyInfoList(vehicleinformationServices.getQueryVehiclesRegularlyInfo());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
    @Resource
    private VehicleManagementMapper vehicleManagementMapper;
    @Autowired
    private CowBUtils cowBUtils;

    @Async
    @Scheduled(fixedDelay = 20000)
    public void getGps() throws Exception {
        //获取所有GPS盒子imei和key（key是鉴权码），循环调用获取GPS，都调用结束，推送前台
        List<VehicleManagement> vehicleManagements = vehicleManagementMapper.select(new VehicleManagement());
        List<MhInfo> mhInfos = new ArrayList<>();
        for (VehicleManagement v : vehicleManagements) {
            String obj = cowBUtils.getMhLastinfo(v.getIkey(), v.getImei());
            MhInfo mhInfo = JSONObject.parseObject(obj, MhInfo.class);
            mhInfo.setVehicleManagement(v);
            mhInfos.add(mhInfo);
        }
        webSocketVo.setCarSet(mhInfos);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    private HttpEntity<String> getResponse(Map<String, Object> requestMap) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSONString(requestMap), headers);
        return formEntity;
    }

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
        // 获取personnelpermissions下人员类别分组
        List<PersonnelPermissions> personnelPermissions = personnelPermissionsServices.selectByClass();
        // 获取门禁数据库今天所有用户信息
        List<APBCardHolderVo> apbCardHolderVos = JSONObject.parseArray(
                JSONObject.parseObject(HttpUtil.post(Url + "userInfo/selectapbcardholder", new HashMap<>())).get("data").toString(), APBCardHolderVo.class
        );
        // TODO: 2020/8/5 因为json循环引用，导致前端接收的json是$ref,需要研究下怎么改
//        List<APBCardHolderVo> apbCardHolderVos1 = JSONObject.parseArray(
//                JSONObject.parseObject(HttpUtil.post(Url + "userInfo/selectapbcardholder", new HashMap<>())).get("data").toString(), APBCardHolderVo.class
//        );
        // 获取内部员工总数
        List<APBCardHolderVo> YGList = apbCardHolderVos.stream().filter(
                s -> CowBUtils.wordsEqualOf(s.getType(),
                        personnelPermissions.stream().filter(p -> p.getClassname().equals("class1")).collect(Collectors.toList()).get(0).getAllname().split(",")))
                .map(s -> {
                    s.setType("员工");
                    return s;
                })
                .collect(Collectors.toList());
        int YG = YGList.size();
        //2. 获取临时访客总数
        List<APBCardHolderVo> FKList = apbCardHolderVos.stream().filter(
                s -> CowBUtils.wordsEqualOf(s.getType(),
                        personnelPermissions.stream().filter(p -> p.getClassname().equals("class2")).collect(Collectors.toList()).get(0).getAllname().split(",")))
                .map(s -> {
                    s.setType("临时访客");
                    return s;
                })
                .collect(Collectors.toList());
        int FK = FKList.size();
        //3. 获取承包商总数
        List<APBCardHolderVo> CBSList = apbCardHolderVos.stream().filter(
                s -> CowBUtils.wordsEqualOf(s.getType(),
                        personnelPermissions.stream().filter(p -> p.getClassname().equals("class3")).collect(Collectors.toList()).get(0).getAllname().split(",")))
                .map(s -> {
                    s.setType("承包商");
                    return s;
                })
                .collect(Collectors.toList());
        int CBS = CBSList.size();
        // 发送websocket信息
        webSocketVo.setUsersCount(YGList);
        webSocketVo.setContractorsCount(CBSList);
        webSocketVo.setVisitorsCount(FKList);
        webSocketVo.setAllUsersCount(YG + FK + CBS);
//        webSocketVo.setSelectapbcardList(apbCardHolderVos1);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }


    /**
     * 火灾消防 事故区域人数
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectDeviceUsersCount() throws Exception {
        APBCardHolder apbCardHolder = JSONObject.parseObject(JSONObject.parseObject(HttpUtil.post(Url + "userInfo/selectDeviceUsersCnt", new HashMap<>())).get("data").toString(), APBCardHolder.class);
        webSocketVo.setApbCardHolder(apbCardHolder);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    // region BASF90600 ERC-车辆定位

//    /**
//     * @return void
//     * @Method BASF90600_GetInsideList
//     * @Author SKAIXX
//     * @Description ERC大屏车辆定位模块获取在场车辆信息一览
//     * @Date 2019/12/30 15:47
//     * @Param
//     **/
//    @Async
//    @Scheduled(fixedDelay = 40000)
//    public void BASF90600_GetInsideList() throws Exception {
//        // 获取在场车辆信息一览
//        webSocketVo.setInsideVehicleinformationVoList(vehicleinformationServices.getInsideList());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    /**
//     * @return void
//     * @Method BASF90600_GetInsideVehicleType
//     * @Author SKAIXX
//     * @Description ERC大屏车辆定位模块在场车辆类别统计
//     * @Date 2019/12/30 16:27
//     * @Param
//     **/
//    @Async
//    @Scheduled(fixedDelay = 40000)
//    public void BASF90600_GetInsideVehicleType() throws Exception {
//        // 在场车辆类别统计
//        webSocketVo.setInsideVehicleTypeVoList(vehicleinformationServices.getInsideVehicleType());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    /**
//     * @return
//     * @Method BASF90600_GetAccessStatistics
//     * @Author SKAIXX
//     * @Description ERC大屏车辆定位模块本月车辆出入统计
//     * @Date 2019/12/30 19:09
//     * @Param
//     **/
//    @Async
//    @Scheduled(fixedDelay = 60000)
//    public void BASF90600_GetAccessStatistics() throws Exception {
//        // 车辆出入统计
//        webSocketVo.setVehicleAccessStatisticsVoList(vehicleinformationServices.getAccessStatistics());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    /**
//     * @return
//     * @Method BASF90600_GetWeekAccessStatistics
//     * @Author GJ
//     * @Description ERC大屏车辆定位模块本周车辆出入统计
//     * @Date 2020/04/14 10:19
//     * @Param
//     **/
//    @Async
//    @Scheduled(fixedDelay = 60000)
//    public void BASF90600_GetWeekAccessStatistics() throws Exception {
//        // 车辆出入统计
//        webSocketVo.setVehicleWeekAccessStatisticsVoList(vehicleinformationServices.getWeekAccessStatistics());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    /**
//     * @return
//     * @Method BASF90600_GetDailyVehicleInfo
//     * @Author SKAIXX
//     * @Description ERC大屏车辆定位模块获取当日入场车辆信息
//     * @Date 2019/12/31 11:27
//     * @Param
//     **/
//    @Async
//    @Scheduled(fixedDelay = 50000)
//    public void BASF90600_GetDailyVehicleInfo() throws Exception {
//        // 获取当日入场车辆信息
//        webSocketVo.setVehicleinformationList(vehicleinformationServices.getDailyVehicleInfo());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
    // endregion
//
//    // region BASF90200 ERC-火灾消防
//

    /**
     * @return 近30日接警数据分析
     * @Method BASF90200_GetFireAlarmStatistics
     * @Author SKAIXX
     * @Description 获取当月接警数据分析
     * @Date 2020/1/7 14:17
     * @Param
     **/
//    @Async
//    @Scheduled(fixedDelay = 70000)
//    public void BASF90200_GetFireAlarmStatistics() throws Exception {
//        // 获取当月接警数据分析
//        webSocketVo.setFireAlarmStatisticsVoList(firealarmServices.getFireAlarmStatistics());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    @Async
//    @Scheduled(fixedDelay = 70000)
//    public void BASF90200_GetFireAlarmList() throws Exception {
//        // 获取接警事件记录
//        webSocketVo.setFireAlarmList(firealarmServices.getFireAlarm());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void BASF90800_GetMandatoryInfo() throws Exception {
//        // 获取培训教育强制的通过/未通过
//        webSocketVo.setPassingRateList(startprogramServices.getMandatoryInfo());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void BASF90800_GetIsMandatoryInfo() throws Exception {
//        // 获取培训教育非强制的通过/未通过
//        webSocketVo.setPassingIsRateList(startprogramServices.getIsMandatoryInfo());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
//
//    @Async
//    @Scheduled(fixedDelay = 80000)
//    public void BASF10802_GetEmergencyplanInfo() throws Exception {
//        // 应急预案列表
//        webSocketVo.setEmergencyplanList(emergencyplanServices.list());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
//
//    @Async
//    @Scheduled(fixedDelay = 80000)
//    public void BASF10804_GetChemicalsdsInfo() throws Exception {
//        // 化学品SDS列表
//        webSocketVo.setChemicalsdsList(chemicalsdsServices.list());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    @Async
//    @Scheduled(fixedDelay = 90000)
//    public void BASF10803_GetResponseinformationInfo() throws Exception {
//        //应急预案工艺处置队列表
//        webSocketVo.setResponseinformationList(responseinformationServices.list());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void BASF90800_GetOverduePersonnelList() throws Exception {
//        //获取培训到期人员列表
//        webSocketVo.setOverduePersonnelListVoList(trainjoinlistServices.overduepersonnellist());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
//
//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void BASF90800_GetFutureProgram() throws Exception {
//        //大屏培训信息推送列表
//        webSocketVo.setFutureProgramList(startprogramServices.getFutureProgram());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
//
//    @Async
//    @Scheduled(fixedDelay = 90000)
//    public void BASF90900_GetRiskassessment() throws Exception {
//        //获取风险判研信息(MongoDB)
//        webSocketVo.setRiskassessment(riskassessmentServices.getData());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

//    @Async
//    @Scheduled(fixedDelay = 100000)
//    public void BASF90900_WriteList() throws Exception {
//        //获取风险研判信息（MySql）
//        webSocketVo.setRiskassessmentsList(riskassessmentsServices.writeList());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
    @Async
    @Scheduled(fixedDelay = 50000)
    public void BASF90905_GetSameDayFireAlarm() throws Exception {
        // 获取今日事件列表
        webSocketVo.setSameDayFireAlarm(firealarmServices.getSameDayFireAlarm());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 50000)
    public void BASF90906_GetWeekFireAlarm() throws Exception {
        // 获取本周事件列表
        webSocketVo.setWeekFireAlarm(firealarmServices.getWeekFireAlarm());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

//    @Async
//    @Scheduled(fixedDelay = 110000)
//    public void BASF90921_GetHighriskareaList() throws Exception {
//        // 获取高风险作业清单
//        webSocketVo.setHighriskareaList(highriskareaServices.list());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

    @Async
    @Scheduled(fixedDelay = 40000)
    public void BASF90800_GetVehicleinformationVolList() throws Exception {
        //获取车辆列表(危化品车辆数用)
        webSocketVo.setDangerousgoodsList(vehicleinformationServices.getlistinformation());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 40000)
    public void BASF90800_GetVehicleinformationCount() throws Exception {
        //获取车辆列表(危化品车辆数用)
        webSocketVo.setCountdangerousgoods(vehicleinformationServices.getcountinformation());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

//    @Async
//    @Scheduled(fixedDelay = 130000)
//    public void BASF10105_GetDeviceAndSqlUserinfoVoList() throws Exception {
//        DeviceAndSqlUserinfoVo deviceAndSqlUserinfoVo = new DeviceAndSqlUserinfoVo();
//        List<DeviceAndSqlUserinfoVo> deviceAndSqlUserinfoVoList = new ArrayList<>();
//
//        Deviceinformation deviceinformation = new Deviceinformation();
//        deviceinformation.setDevicetype("BC004003");
//        List<Deviceinformation> deviceinformationList = deviceinformationMapper.select(deviceinformation);
//        for (int i = 0; i < deviceinformationList.size(); i++) {
//
////            for (int j = 0; j < departlist.size(); j++) {
////                if (deviceinformationList.get(i).getDeviceno() == departlist.get(j).getApbid()) {
////            List<SqlAPBCardHolder> departlist = sqlUserInfoMapper.selectapbid(deviceinformationList.get(i).getDeviceno());
//
//            JSONArray departarray = returngetlist("userInfo/selectapbid", deviceinformationList.get(i).getDeviceno());
//            List<SqlViewDepartment> departlist = JSONObject.parseArray(departarray.toJSONString(), SqlViewDepartment.class);
//
//            if (departlist.size() > 0) {
//                deviceAndSqlUserinfoVo.setDeviceinformation(deviceinformationList.get(i));
//                deviceAndSqlUserinfoVo.setSqlUserInfoCnt(departlist.size());
//                deviceAndSqlUserinfoVoList.add(deviceAndSqlUserinfoVo);
//            }
//        }
//        webSocketVo.setDeviceAndSqlUserinfoVoList(deviceAndSqlUserinfoVoList);
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }

    // endregion
//
//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void BASF90620_GetRoadClosed() throws Exception {
//        //道路占用/临时封闭区域列表
//        webSocketVo.setRoadClosed(applicationServices.roadClosed());
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
//
//    @Async
//    @Scheduled(fixedDelay = 30000)
//    public void SENDEMAIL_Listswitch() throws Exception {
//        //主备服务通知表
//        Switchnotifications switchnotifications = new Switchnotifications();
//        webSocketVo.setSwitchList(switchnotificationsServices.list(switchnotifications));
//        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
//    }
//
    @Async
    @Scheduled(fixedDelay = 100000)
    public void updatePims() throws Exception {
        System.out.println("updatePims start");
        //获取最新PimsData
        List<Pimsdata> pimsdataList = pimsdataMapper.getLastestPims();
        pimsdataList.forEach(item -> {
            item.setPimsid(UUID.randomUUID().toString());
            item.preInsert();
        });
        //插入最新数据到PimsData
        if (pimsdataList.size() > 0) {
            pimsdataMapper.insertPimsDataList(pimsdataList);
        }
        System.out.println("updatePims end");
    }
}
