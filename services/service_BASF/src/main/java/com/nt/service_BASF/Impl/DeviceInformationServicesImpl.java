package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Application;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Electronicfencestatus;
import com.nt.dao_BASF.VO.DeviceinformationListVo;
import com.nt.dao_BASF.VO.EpChartVo;
import com.nt.service_BASF.DeviceInformationServices;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.service_BASF.mapper.ApplicationMapper;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_BASF.mapper.ElectronicfencestatusMapper;
import com.nt.service_BASF.mapper.MapBox_MapLevelMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.CowBUtils;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: BASF10105ServicesImpl
 * @Author: SKAIXX
 * @Description: BASF设备管理模块实现类
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceInformationServicesImpl implements DeviceInformationServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private DeviceinformationMapper deviceinformationMapper;

    @Autowired
    private MapBox_MapLevelServices mapBox_mapLevelServices;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CowBUtils cowBUtils;

    @Resource
    private ElectronicfencestatusMapper electronicfencestatusMapper;

    /**
     * @param deviceinformation
     * @Method list
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备列表
     * @Return java.util.List<Deviceinformation>
     * @Date 2019/11/4 16:35
     */
    @Override
    public List<Deviceinformation> list(Deviceinformation deviceinformation) throws Exception {
//        Deviceinformation deviceinformation = new Deviceinformation();
//        deviceinformation = new Deviceinformation();
        List<Deviceinformation> deviceinformationList = deviceinformationMapper.select(deviceinformation);
        //移除虚拟路桩信息
        for (Iterator<Deviceinformation> iterator = deviceinformationList.iterator(); iterator.hasNext(); ) {
            if (iterator.next().getDevicetype().equals("BC004006")) {
                iterator.remove();
            }
        }
        return deviceinformationList;
    }

    /**
     * @param deviceinformation
     * @param tokenModel
     * @Method insert
     * @Author SKAIXX
     * @Version 1.0
     * @Description 创建设备
     * @Return void
     * @Date 2019/11/4 18:48
     */
    @Override
    public void insert(Deviceinformation deviceinformation, TokenModel tokenModel, String type) throws Exception {
        if (type.equals("Barricades")) {
            // 路障
            deviceinformation.preInsert(tokenModel);
            deviceinformation.setStatus("1");
            deviceinformationMapper.insert(deviceinformation);
        } else {
            deviceinformation.preInsert(tokenModel);
            deviceinformation.setDeviceinformationid(UUID.randomUUID().toString());
            deviceinformationMapper.insert(deviceinformation);
            // 如果设备是摄像头 创建h5s信息
            if ("BC004004".equals(deviceinformation.getDevicetype())) {
                String insertResult = insertH5sInfomation(deviceinformation);
                // todo insertResult为空说明插入h5s失败，应该throw异常
                if (StringUtils.isEmpty(insertResult)) {

                }
            }
            // 如果设备是电子围栏
            else if ("BC004005".equals(deviceinformation.getDevicetype())) {
                // 根据摄像头id（subordinatesystem）找到 摄像头设备编号
                Deviceinformation dev = new Deviceinformation();
                dev.setDeviceinformationid(deviceinformation.getSubordinatesystem());
                dev = deviceinformationMapper.selectOne(dev);
                // 插入电子围栏状态表信息
                Electronicfencestatus electronicfencestatus = new Electronicfencestatus();
                electronicfencestatus.setId(UUID.randomUUID().toString());
                electronicfencestatus.setDeviceinformationid(deviceinformation.getDeviceinformationid());
                electronicfencestatus.setCamerano(dev.getDeviceno());
                // 围栏对应的摄像头id
                electronicfencestatus.setCameraid(deviceinformation.getSubordinatesystem());
                // 围栏是否报警 初始化 0
                electronicfencestatus.setWarningstatus(0);
                // 围栏是否屏蔽 初始化 0
                electronicfencestatus.setShieldstatus(0);
                electronicfencestatusMapper.insert(electronicfencestatus);
            }
        }
    }

    /**
     * @param deviceinformation
     * @Method Delete
     * @Author SKAIXX
     * @Version 1.0
     * @Description 删除设备
     * @Return void
     * @Date 2019/11/5 15:31
     */
    @Override
    public void delete(Deviceinformation deviceinformation) throws Exception {
        //逻辑删除（status -> "1"）
        deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
    }

    /**
     * @param deviceid
     * @Method one
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备详情
     * @Return com.nt.dao_BASF.Deviceinformation
     * @Date 2019/11/5 15:53
     */
    @Override
    public Deviceinformation one(String deviceid) throws Exception {
        return deviceinformationMapper.selectByPrimaryKey(deviceid);
    }

    private String insertH5sInfomation(Deviceinformation deviceinformation) {
        // 获取摄像头 设备编号 作为h5s token
        String token = deviceinformation.getDevrow();
        // 获取摄像头ip 作为h5s url 需要encode
        String url = deviceinformation.getIp();
        // 根据设备小类 判断摄像头品牌
        String pp = deviceinformation.getDevicetypesmall();
        // 根据摄像头品牌，设置摄像头用户名密码
        String user = "";
        String password = "";
        switch (pp) {
            case "BC048000":    // 海康
                user = "admin";
                password = "Hx37311378";
                break;
            case "BC048001":    // 安讯士
                user = "root";
                password = "root";
                break;
            case "BC048002":    // 派尔高
                user = "admin";
                password = "admin";
                break;
            case "BC048003":    // 大华
                break;
            case "BC048004":    // 博世
                break;
        }
        return cowBUtils.insertH5sInfomation(token, user, password, url);
    }

    /**
     * @param deviceinformation
     * @param tokenModel
     * @Method update
     * @Author SKAIXX
     * @Version 1.0
     * @Description 更新设备详情
     * @Return void
     * @Date 2019/11/5 16:07
     */
    @Override
    public void update(Deviceinformation deviceinformation, TokenModel tokenModel) throws Exception {
        deviceinformation.preUpdate(tokenModel);
        deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
        if ("BC004004".equals(deviceinformation.getDevicetype())) {
            // 如果是设备是 摄像头 根据设备编号 先删除h5s conf中的信息
            String token = deviceinformation.getDevrow();
            cowBUtils.delH5sInformation(token);
            // 再根据设备编号 创建h5s conf中的信息
            String insertResult = insertH5sInfomation(deviceinformation);
            // insertResult为空说明插入h5s失败，应该throw异常
            if (StringUtils.isEmpty(insertResult)) {

            }
        }
        // 如果设备是电子围栏
        else if ("BC004005".equals(deviceinformation.getDevicetype())) {
            // 根据摄像头id（subordinatesystem）找到 摄像头设备编号
            Deviceinformation dev = new Deviceinformation();
            dev.setDeviceinformationid(deviceinformation.getSubordinatesystem());
            dev = deviceinformationMapper.selectOne(dev);
            // 根据设备主键更新，电子围栏状态表信息
            Electronicfencestatus electronicfencestatus = new Electronicfencestatus();
            electronicfencestatus.setDeviceinformationid(deviceinformation.getDeviceinformationid());
            electronicfencestatus.setCamerano(dev.getDeviceno());
            // 围栏对应的摄像头id
            electronicfencestatus.setCameraid(dev.getDeviceinformationid());
            // 围栏是否报警 初始化 0
            electronicfencestatus.setWarningstatus(0);
            // 围栏是否屏蔽 初始化 0
            electronicfencestatus.setShieldstatus(0);
            electronicfencestatusMapper.updateByPrimaryKeySelective(electronicfencestatus);
        }
    }

    /**
     * @Method deviceList
     * @Author 王哲
     * @Version 1.0
     * @Description 查询设备列表（GIS专用）
     * @Return Deviceinformation
     * @Date 2019/12/12 14:36
     */
    @Override
    public DeviceinformationListVo deviceList(String mapid, String[] devicetype, String[] devicetypesmall, String devicename, Integer pageindex, Integer pagesize) throws Exception {
        if (Arrays.asList(devicetype).contains("BC004006")) {
//            //查询更新路障信息
//            Deviceinformation deviceinformation0 = new Deviceinformation();
//            deviceinformation0.setDevicetype("BC004006");
//            deviceinformation0.setStatus("0");
//            for (Deviceinformation deviceinformation : deviceinformationMapper.select(deviceinformation0)) {
//                deviceinformation.setStatus(AuthConstants.DEL_FLAG_DELETE);
//                deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
//            }
            //获取符合要求的虚拟路障的设备信息表的id
//            List<Application> applicationList = applicationMapper.selectBarricades();
            //更新路障信息
//            for (Application application : applicationList) {
//                Deviceinformation deviceinformation = new Deviceinformation();
//                deviceinformation.setDeviceinformationid(application.getDeviceinformationid());
//                deviceinformation.setStatus("0");
//                deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
//            }
        }
        List<String> mapidList = mapBox_mapLevelServices.getChildrensStr(mapid);
        DeviceinformationListVo lv = new DeviceinformationListVo();
        deviceinformationMapper.selectDeviceList(mapidList, devicetype, devicetypesmall, devicename, pageindex, pagesize);
        lv.setDeviceinformationlist(deviceinformationMapper.selectDeviceList(mapidList, devicetype, devicetypesmall, devicename, pageindex, pagesize));
        lv.setTotal(deviceinformationMapper.selectDeviceListcount(mapidList, devicetype, devicetypesmall, devicename));
        return lv;

    }

    @Override
    public List<EpChartVo> getEpChartVo() throws Exception {
        return deviceinformationMapper.getEpChartVo();
    }
}
