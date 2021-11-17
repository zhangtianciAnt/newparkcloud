package com.nt.service_BASF.Impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.dao_BASF.CarInfoList;
import com.nt.dao_BASF.VO.*;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.service_BASF.VehicleinformationServices;
import com.nt.service_BASF.mapper.VehicleinformationMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: VehicleinformationServicesImpl
 * @Author: Wxz
 * @Description: VehicleinformationServicesImpl
 * @Date: 2019/11/14 13:23
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VehicleinformationServicesImpl implements VehicleinformationServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private VehicleinformationMapper vehicleinformationMapper;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @param
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆信息列表
     * @Return java.util.List<Vehicleinformation>
     * @Date 2019/11/14 13：27
     */
    @Override
    public List<Vehicleinformation> list() throws Exception {
        Vehicleinformation vehicleinformation = new Vehicleinformation();
        vehicleinformation.setStatus("0");
        return vehicleinformationMapper.select(vehicleinformation);
    }

    @Override
    public VehicleinformationGpsArrVo one(String vehicleinformationid) throws Exception {
        Vehicleinformation vehicleinformation = new Vehicleinformation();
        vehicleinformation.setVehicleinformationid(vehicleinformationid);

        List<Vehicleinformation> list = vehicleinformationMapper.select(vehicleinformation);

        VehicleinformationGpsArrVo listarr = new VehicleinformationGpsArrVo();

        String gps = list.get(0).getGps();
        if(StringUtils.isNotEmpty(gps)){
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < gps.split(";").length; i++) {
                ArrayList arrayList1 = new ArrayList();
                for (int j = 0; j < gps.split(";")[i].split(",").length; j++) {
                    BigDecimal a = new BigDecimal(gps.split(";")[i].split(",")[j]);
                    arrayList1.add(Arrays.asList(a));
                }
                arrayList.add(arrayList1);
            }
            listarr.setGpsArr(arrayList);
        }

        String errorGps = list.get(0).getErrorgps();
        if(StringUtils.isNotEmpty(errorGps)){
            ArrayList errorGpsArrayList = new ArrayList();
            for (int i = 0; i < errorGps.split(";").length; i++) {
                ArrayList tempList = new ArrayList();
                for (int j = 0; j < errorGps.split(";")[i].split(",").length; j++) {
                    BigDecimal a = new BigDecimal(errorGps.split(";")[i].split(",")[j]);
                    tempList.add(Arrays.asList(a));
                }
                errorGpsArrayList.add(tempList);
            }
            listarr.setErrorGpsArr(errorGpsArrayList);
        }

        listarr.setVehicleinformationid(list.get(0).getVehicleinformationid());
        listarr.setVehiclenumber(list.get(0).getVehiclenumber());
        listarr.setDriver(list.get(0).getDriver());
        listarr.setTransporter(list.get(0).getTransporter());
        listarr.setIntime(list.get(0).getIntime());
        listarr.setOuttime(list.get(0).getOuttime());
        listarr.setIshazardous(list.get(0).getIshazardous());
        listarr.setGps(list.get(0).getGps());
        listarr.setStatus(list.get(0).getStatus());
        listarr.setCreateby(list.get(0).getCreateby());
        listarr.setCreateon(list.get(0).getCreateon());

        return listarr;
    }

    /**
     * @param
     * @Method
     * @Author
     * @Version 1.0
     * @Description 获取危化品车数量
     * @Return int
     * @Date 2019/11/14 13：27
     */
    @Override
    public List<Vehicleinformation> getcountinformation() throws Exception {
        return vehicleinformationMapper.getcountinformation();
    }

    /**
     * @param
     * @Method getlistinformation
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆信息列表(危化品车辆数用)
     * @Return java.util.List<VehicleinformationVo>
     * @Date 2019/11/14 13：27
     */
    @Override
    public List<VehicleinformationVo> getlistinformation() throws Exception {
        List<Vehicleinformation> getInformatoinlist = vehicleinformationMapper.getlistinformation();
        List<VehicleinformationVo> infoList = new ArrayList<>();
        VehicleinformationVo vehicleinformationVo;
        int data[] = new int[12];
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < getInformatoinlist.size(); i++) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            month = dateFormat.format(getInformatoinlist.get(i).getIntime());
//            month = month.substring(6, 7);
            cal.setTime(getInformatoinlist.get(i).getIntime());
            int month = cal.get(Calendar.MONTH);
            data[month] = data[month] + 1;
        }
        for (int j = 0; j < data.length; j++) {
            int m = 1;
            vehicleinformationVo = new VehicleinformationVo();
            vehicleinformationVo.setCnt(data[j]);
            vehicleinformationVo.setDate((m + j) + "月");
            infoList.add(vehicleinformationVo);
        }

        return infoList;
    }

    /**
     * @param vehicleinformation
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆信息详情
     * @Return void
     * @Date 2019/11/14 13：39
     */
    @Override
    public void update(Vehicleinformation vehicleinformation, TokenModel tokenModel) throws Exception {
        vehicleinformation.preUpdate(tokenModel);
        vehicleinformationMapper.updateByPrimaryKeySelective(vehicleinformation);
    }

    /**
     * @param
     * @Method getInsideList
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取在场车辆信息一览
     * @Return java.util.List<com.nt.dao_BASF.Vehicleinformation>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<InsideVehicleinformationVo> getInsideList() throws Exception {
        return vehicleinformationMapper.getInsideList();
    }

    /**
     * @param
     * @Method getAccessStatistics
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取本月车辆出入统计
     * @Return java.util.List<com.nt.dao_BASF.VO.VehicleAccessStatisticsVo>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<VehicleAccessStatisticsVo> getAccessStatistics() throws Exception {
        return vehicleinformationMapper.getAccessStatistics();
    }

    /**
     * @param
     * @Method getAccessStatistics
     * @Author GJ
     * @Version 1.0
     * @Description 获取本周车辆出入统计
     * @Return java.util.List<com.nt.dao_BASF.VO.VehicleAccessStatisticsVo>
     * @Date 2020/04/14 10:14
     */
    @Override
    public List<VehicleAccessStatisticsVo> getWeekAccessStatistics() throws Exception {
        return vehicleinformationMapper.getWeekAccessStatistics();
    }

    /**
     * @param
     * @Method getDailyVehicleInfo
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取当日入场车辆信息
     * @Return java.util.List<com.nt.dao_BASF.Vehicleinformation>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<Vehicleinformation> getDailyVehicleInfo() throws Exception {
        // 2021.05.18 门检系统升级，数据接口调整 start by nt-ma
        // String urlToken = "http://gatecheck.dowann.cn/api/ws/token?username=bachapi&password=123456";
        // String urlDailyInfo = "http://gatecheck.dowann.cn/api/out/dailyInfo";
        String urlToken = "https://e-gate.api.basf.com/api/ws_auth/login?username=bachapi&password=Aa123456";
        String urlDailyInfo = "https://e-gate.api.basf.com/api/out/dailyInfo";
        // 2021.05.18 门检系统升级，数据接口调整 end by nt-ma
        // 获取token
        ResponseEntity<String> rst = restTemplate.exchange(urlToken, HttpMethod.GET, null, String.class);
        String value = rst.getBody();
        // 2021.05.18 门检系统升级，数据结构变化对应 start by nt-ma
        // JSONObject string_to_json = JSONUtil.parseObj(value);
        JSONObject string_to_json = JSONUtil.parseObj(value).getJSONObject("data");
        // 2021.05.18 门检系统升级，数据结构变化对应 end by nt-ma
        String token = string_to_json.get("access_token").toString();

        // 通过token获取实时车辆信息
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        String json = "{\"json\":\"object\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", token));
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        rst = restTemplate.exchange(urlDailyInfo, HttpMethod.GET, requestEntity, String.class);
        value = rst.getBody();
        string_to_json = JSONUtil.parseObj(value);
        List<VehicleinfoForDowann> vehicleinfoForDowannList = JSONUtil.toList(JSONUtil.parseArray(string_to_json.get("data")), VehicleinfoForDowann.class);
        List<Vehicleinformation> vehicleinformationList = new ArrayList<>();
        vehicleinfoForDowannList.forEach(item -> {
            Vehicleinformation vehicleinformation = new Vehicleinformation();
            vehicleinformation.setDriver(item.getDrivername());
            vehicleinformation.setIntime(item.getEnterDateTime());
            vehicleinformation.setVehiclenumber(item.getTruckNo());
            vehicleinformation.setGoodsname(item.getGoodsName());
            vehicleinformation.setVehicletype(item.getVehicle());
            vehicleinformationList.add(vehicleinformation);
        });
        return vehicleinformationList;
    }

    /**
     * @param
     * @Method getInsideVehicleType
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取在场车辆类别统计
     * @Return java.util.List<com.nt.dao_BASF.VO.InsideVehicleTypeVo>
     * @Date 2019/12/17 11:35
     */
    @Override
    public List<InsideVehicleTypeVo> getInsideVehicleType() throws Exception {
        return vehicleinformationMapper.getInsideVehicleType();
    }

    /**
     * @param
     * @Method getQueryVehiclesRegularlyInfo
     * @Author SKAIXX
     * @Version 1.0
     * @Description 定时查询车辆信息表（出场时间为空的数据）
     * @Return java.util.List<com.nt.dao_BASF.Vehicleinformation>
     * @Date 2020/04/13 14:49
     */
    @Override
    public List<VehicleinformationGpsArrVo> getQueryVehiclesRegularlyInfo() throws Exception {
        List<VehicleinformationGpsArrVo> list = vehicleinformationMapper.getQueryVehiclesRegularlyInfo();
        for (VehicleinformationGpsArrVo vehicleinformationGpsArrVo : list) {
            if (StringUtils.isNotEmpty(vehicleinformationGpsArrVo.getGps())) {
                ArrayList arrayList = new ArrayList();
                String[] spiltTemp = vehicleinformationGpsArrVo.getGps().split(";");
                for (int i = 0; i < spiltTemp.length; i++) {
                    ArrayList arrayList1 = new ArrayList();
                    String[] splitTempB = spiltTemp[i].split(",");
                    for (int j = 0; j < splitTempB.length; j++) {
                        BigDecimal a = new BigDecimal(splitTempB[j]);
                        arrayList1.add(Arrays.asList(a));
                    }
                    arrayList.add(arrayList1);
                }
                vehicleinformationGpsArrVo.setGpsArr(arrayList);
            }
            if (StringUtils.isNotEmpty(vehicleinformationGpsArrVo.getErrorgps())) {
                ArrayList arrayList = new ArrayList();
                String[] spiltTemp = vehicleinformationGpsArrVo.getErrorgps().split(";");
                for (int i = 0; i < spiltTemp.length; i++) {
                    ArrayList arrayList1 = new ArrayList();
                    String[] splitTempB = spiltTemp[i].split(",");
                    for (int j = 0; j < splitTempB.length; j++) {
                        BigDecimal a = new BigDecimal(splitTempB[j]);
                        arrayList1.add(Arrays.asList(a));
                    }
                    arrayList.add(arrayList1);
                }
                vehicleinformationGpsArrVo.setErrorGpsArr(arrayList);
            }
        }
        return list;
    }

    /**
     * @param vehicleinformation
     * @param
     * @Method insert
     * @Author Sun
     * @Version 1.0
     * @Description 创建车辆进出厂信息
     * @Return void
     * @Date 2019/11/4 18:48
     */
    @Override
    public String insert(Vehicleinformation vehicleinformation) throws Exception {
        vehicleinformation.preInsert();
        String vehicleinformationid = UUID.randomUUID().toString();
        vehicleinformation.setVehicleinformationid(vehicleinformationid);
        vehicleinformationMapper.insert(vehicleinformation);
        return vehicleinformationid;
    }

    @Override
    public void checkVehicle(Vehicleinformation vehicleinformation) throws Exception {
        List<Vehicleinformation> vehicleinformationList = vehicleinformationMapper.checkVehicle(vehicleinformation.getVehiclenumber(), vehicleinformation.getMeid());
        if (vehicleinformationList.size() > 0) {
            // 更新出场时间
            for (Vehicleinformation tmp : vehicleinformationList) {
                updateouttimebysamecar(tmp);
            }
        }
    }

    /**
     * @param
     * @param
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆gps信息
     * @Return void
     * @Date 2019/11/14 13：39
     */
    @Override
    public void updategps(String vehicleinformationid, String gps, String speed) throws Exception {
        vehicleinformationMapper.updategps(vehicleinformationid, gps, new Date(), speed);
    }

    /**
     * @param
     * @param
     * @Method updateouttimebysamecar
     * @Author Wxz
     * @Version 1.0
     * @Description 同一个pad绑定多次同一辆车，让前一次车辆出厂
     * @Return void
     * @Date 2019/11/14 13：39
     */
    private void updateouttimebysamecar(Vehicleinformation vehicleinformation) throws Exception {
        vehicleinformation.setModifyon(new Date());
        vehicleinformation.setStatus("1");
        vehicleinformation.setOuttime(new Date());
        vehicleinformationMapper.updateByPrimaryKeySelective(vehicleinformation);
    }

    /**
     * @param
     * @param
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆信息出场时间
     * @Return void
     * @Date 2019/11/14 13：39
     */
    @Override
    public void updateouttime(Vehicleinformation vehicleinformation) throws Exception {
        vehicleinformation.setModifyon(new Date());
        vehicleinformation.setOuttime(new Date());
        vehicleinformationMapper.updateByPrimaryKeySelective(vehicleinformation);
    }

    /**
     * @param carInfoList
     * @Method setCarInfoList
     * @Author myt
     * @Version 1.0
     * @Description 调用道闸接口插入数据
     * @Return void
     * @Date 2021/11/13 13：39
     */
    @Override
    public void setCarInfoList(List<CarInfoList> carInfoList) throws Exception {
        // 获取当前系统时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startdate = sdf.format(new Date(System.currentTimeMillis()));
        Date dt = sdf.parse(startdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(dt);
        // 系统当前时间加1天
        ca.add(Calendar.DATE,1);
        String enddate = sdf.format(ca.getTime());
        // 道闸系统接口地址
        String urlCarInfo = "http://192.168.150.200:9090/api/park/addmonthlycar";
        HttpHeaders httpHeaders = new HttpHeaders();
        // 设置请求类型
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        log.info("道闸接口调用开始！");
        for (CarInfoList item : carInfoList) {
            // 封装参数
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startdate", startdate);//月租车开始日期
            jsonObject.put("enddate", enddate);//月租车结束日期
            jsonObject.put("name", item.getDrivername());//人员姓名
            jsonObject.put("phone", item.getPhone());//手机号
            jsonObject.put("carplate", item.getTruckNo());//车牌号
            jsonObject.put("cardtype", "A");//月租车类型
            jsonObject.put("controllers", "1,2");//开通控制机号权限
            // 封装参数和头信息
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject, httpHeaders);
            ResponseEntity<String> rst = restTemplate.exchange(urlCarInfo, HttpMethod.POST, httpEntity, String.class);
            JSONObject result = JSONUtil.parseObj(rst.getBody());
            // 插入失败数据，记入错误日志
            if (!Boolean.parseBoolean(String.valueOf(result.get("success")))) {
                log.error("【道闸系统数据插入失败】：" + "车牌号：" + item.getTruckNo() + "；人员姓名：" + item.getDrivername()+ "；手机号码：" + item.getPhone());
            } else {
                log.info("【道闸系统数据插入成功】：" + "车牌号：" + item.getTruckNo() + "；人员姓名：" + item.getDrivername()+ "；手机号码：" + item.getPhone());
            }
        }
        log.info("道闸接口调用结束！");
    }
}
