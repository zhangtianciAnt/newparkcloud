package com.nt.service_BASF.Impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.dao_BASF.VO.*;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.service_BASF.VehicleinformationServices;
import com.nt.service_BASF.mapper.VehicleinformationMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        return vehicleinformationMapper.select(vehicleinformation);
    }

    @Override
    public VehicleinformationGpsArrVo one(String vehicleinformationid) throws Exception {
        Vehicleinformation vehicleinformation = new Vehicleinformation();
        vehicleinformation.setVehicleinformationid(vehicleinformationid);

        List<Vehicleinformation> list = vehicleinformationMapper.select(vehicleinformation);

        VehicleinformationGpsArrVo listarr = new VehicleinformationGpsArrVo();

        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.get(0).getGps().split(";").length; i++) {
            ArrayList arrayList1 = new ArrayList();
            for (int j = 0; j < list.get(0).getGps().split(";")[i].split(",").length; j++) {
                BigDecimal a = new BigDecimal(list.get(0).getGps().split(";")[i].split(",")[j]);
                arrayList1.add(Arrays.asList(a));
            }
            arrayList.add(arrayList1);
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
        listarr.setGpsArr(arrayList);
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
    public int getcountinformation() throws Exception {
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
        String month = "";
        int data[] = new int[12];
        for (int i = 0; i < getInformatoinlist.size(); i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            month = dateFormat.format(getInformatoinlist.get(i).getIntime());
            month = month.substring(6, 7);
            data[Integer.parseInt(month) - 1] = data[Integer.parseInt(month) - 1] + 1;
        }
        for (int j = 0; j < data.length; j++) {
            int m = 1;
            vehicleinformationVo = new VehicleinformationVo();
            vehicleinformationVo.setCnt(data[j]);
            vehicleinformationVo.setDate(String.valueOf(m + j) + "月");
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
        String urlToken = "http://gatecheck.dowann.cn/api/ws/token?username=bachapi&password=123456";
        String urlDailyInfo = "http://gatecheck.dowann.cn/api/out/dailyInfo";
        // 获取token
        ResponseEntity<String> rst = restTemplate.exchange(urlToken, HttpMethod.GET, null, String.class);
        String value = rst.getBody();
        JSONObject string_to_json = JSONUtil.parseObj(value);
        String token = string_to_json.get("access_token").toString();
        System.out.println("token:" + token);

        // 通过token获取实时车辆信息
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.forEach(httpMessageConverter -> {
            if(httpMessageConverter instanceof StringHttpMessageConverter){
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
        vehicleinformation = vehicleinformationMapper.checkVehicle(vehicleinformation.getVehiclenumber(), vehicleinformation.getMeid());
        if (vehicleinformation != null) {
            // 更新出场时间
            updateouttime(vehicleinformation);
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
}
