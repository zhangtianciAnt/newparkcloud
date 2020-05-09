package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Firealarm;
import com.nt.dao_BASF.ServerInfo;
import com.nt.dao_BASF.VO.DeviceinformationVo;
import com.nt.service_BASF.DeviceInformationServices;
import com.nt.service_BASF.FirealarmServices;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10105Controller
 * @Author: SKAIXX
 * @Description: BASF设备管理模块Controller
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10105")
public class BASF10105Controller {

    @Autowired
    private DeviceInformationServices deviceinFormationServices;

    @Autowired
    private MapBox_MapLevelServices mapBox_mapLevelServices;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FirealarmServices firealarmServices;

    // websocket消息推送
    private WebSocket ws = new WebSocket();
    private WebSocketVo webSocketVo = new WebSocketVo();

    /**
     * @param request
     * @Method list
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:38
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        return ApiResult.success(deviceinFormationServices.list(deviceinformation));
    }


    @RequestMapping(value = "/linkagelist", method = {RequestMethod.POST})
    public ApiResult linkagelist(@RequestBody List<ServerInfo> serverinfolist, HttpServletRequest request) throws Exception {
        //serverinfolist是机柜传来的报警信息列表
        if (serverinfolist.size() > 0) {
            TokenModel tokenModel = tokenService.getToken(request);
            //存储需要更改remark的报警设备的Mapid
            List<String> alarmMapidList = new ArrayList<>();
            //存储发送到websocket中的信息
            List<DeviceinformationVo> list = new ArrayList<>();

            for (int i = 0; i < serverinfolist.size(); i++) {

                //根据设备总厂/分厂名称、回路号/寄存器地址、设备地址/寄存器位查询唯一对应设备
                ServerInfo serverinfo = serverinfolist.get(i);
                Deviceinformation deviceinformation = new Deviceinformation();
                deviceinformation.setFactoryname(serverinfo.getFactoryname());
                deviceinformation.setDevline(serverinfo.getDevline());
                deviceinformation.setDevrow(serverinfo.getDevrow());
                List<Deviceinformation> linkagelist = deviceinFormationServices.list(deviceinformation);

                //添加需要更改remark的报警设备的Mapid
                if (linkagelist.size() == 1) {
                    alarmMapidList.add(linkagelist.get(0).getMapid());
                }

                //创建消防报警单
                if (linkagelist.size() == 1) {
                    Firealarm firealarm = new Firealarm();
                    String yyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString();
                    firealarm.setDeviceinformationid(linkagelist.get(0).getDeviceinformationid());
                    firealarm.setAlarmpeo("系统管理员");
                    firealarm.setAlarmtimes(yyMMddHHmmss);
                    firealarm.setIndevice(linkagelist.get(0).getDevice());
                    firealarm.setSpeloc(linkagelist.get(0).getDetailedlocation());
                    firealarm.setCompletesta("0");
                    firealarm.setMisinformation("0");
                    String firealarmuuid = firealarmServices.insert(firealarm, tokenModel);
                }

            }

            //获取并立即推送非误报且未完成的消防报警单
            Firealarm firealarm = new Firealarm();
            firealarm.setCompletesta("0");
            firealarm.setMisinformation("0");
            List<Firealarm> firealarms = firealarmServices.list(firealarm);
            for (Firealarm f : firealarms) {
                if (StringUtils.isNotEmpty(f.getDeviceinformationid()) && StringUtils.isNotEmpty(f.getFirealarmid())) {
                    DeviceinformationVo linkagelistVo = new DeviceinformationVo();
                    linkagelistVo.setFirealarmuuid(f.getFirealarmid());
                    if (deviceinFormationServices.one(f.getDeviceinformationid()) != null) {
                        linkagelistVo.setDeviceinformation(deviceinFormationServices.one(f.getDeviceinformationid()));
                    } else
                        continue;
                    list.add(linkagelistVo);
                }
            }
            webSocketVo.setTopfirealarmList(firealarms);
            ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));

            //更新mapbox_maplevel中的remark为1，并一直追设到对应的level2
            mapBox_mapLevelServices.remarkSet(alarmMapidList, true, tokenModel);

            // 推送报警设备信息
            webSocketVo.setDeviceinformationList(list);
            ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));


        }
        return ApiResult.success();
    }

    /**
     * @param deviceinformation
     * @param request
     * @Method create
     * @Author SKAIXX
     * @Version 1.0
     * @Description 创建设备
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:39
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        if (deviceinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deviceinFormationServices.insert(deviceinformation, tokenModel, "");
        return ApiResult.success();
    }

    /**
     * @param deviceinformation
     * @param request
     * @Method delete
     * @Author SKAIXX
     * @Version 1.0
     * @Description 删除设备
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/5 15:37
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        if (deviceinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        deviceinformation.setStatus(AuthConstants.DEL_FLAG_DELETE);
        deviceinFormationServices.delete(deviceinformation);
        return ApiResult.success();
    }

    /**
     * @param deviceid
     * @param request
     * @Method selectById
     * @Author SKAIXX
     * @Version 1.0
     * @Description 获取设备详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/5 15:58
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String deviceid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(deviceid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(deviceinFormationServices.one(deviceid));
    }

    /**
     * @param deviceinformation
     * @param request
     * @Method update
     * @Author SKAIXX
     * @Version 1.0
     * @Description 更新设备详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/5 16:08
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Deviceinformation deviceinformation, HttpServletRequest request) throws Exception {
        if (deviceinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        deviceinFormationServices.update(deviceinformation, tokenModel);
        return ApiResult.success();
    }

    /**
     * @Method update
     * @Author 王哲
     * @Version 1.0
     * @Description 查询设备列表（GIS专用）
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/12 14:47
     */
    @RequestMapping(value = "/deviceList", method = {RequestMethod.GET})
    public ApiResult deviceList(String mapid, String devicetype, String devicetypesmall, String devicename, Integer pageindex, Integer pagesize, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(mapid) || StrUtil.isEmpty(devicetype) || pageindex == null || pagesize == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        String[] devicetypeList = devicetype.split("-");
        String[] devicetypesmallList = null;
        if (devicetypesmall != null) {
            devicetypesmallList = devicetypesmall.split("-");
        }
        if (!StrUtil.isEmpty(devicename)) {
            devicename = "%" + devicename + "%";
        }
        pageindex = pagesize * (pageindex - 1);
        return ApiResult.success(
                deviceinFormationServices.deviceList(mapid, devicetypeList, devicetypesmallList, devicename, pageindex, pagesize)
        );
    }
}
