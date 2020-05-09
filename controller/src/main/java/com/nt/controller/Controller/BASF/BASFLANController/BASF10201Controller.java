package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Firealarm;
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
import java.util.List;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10201Controller
 * @Author: Wxz
 * @Description: BASF报警单管理模块Controller
 * @Date: 2019/11/12 11：39
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10201")
public class BASF10201Controller {

    @Autowired
    private FirealarmServices firealarmServices;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private DeviceInformationServices deviceinFormationServices;

    @Autowired
    private MapBox_MapLevelServices mapBox_mapLevelServices;

    // websocket消息推送
    private WebSocket ws = new WebSocket();
    private WebSocketVo webSocketVo = new WebSocketVo();

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取报警单列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13：10
     */
    @RequestMapping(value = "/list",method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request)throws Exception{
        return ApiResult.success(firealarmServices.list());
    }

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取报警单列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13：10
     */
    @RequestMapping(value = "/listdialog",method = {RequestMethod.POST})
    public ApiResult listdialog(HttpServletRequest request)throws Exception{
        Firealarm firealarm = new Firealarm();
        firealarm.setCompletesta("0");
        firealarm.setMisinformation("0");
        return ApiResult.success(firealarmServices.list(firealarm));
    }

    /**
     * @param firealarm
     * @param request
     * @Method create
     * @Author Wxz
     * @Version 1.0
     * @Description 创建报警单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:20
     */
    @RequestMapping(value = "/create",method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Firealarm firealarm, HttpServletRequest request)throws Exception{
        if (firealarm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(firealarmServices.insert(firealarm,tokenModel));
    }

    /**
     * @param firealarm
     * @param request
     * @Method delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除报警单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13：31
     */
    @RequestMapping(value = "/delete",method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Firealarm firealarm,HttpServletRequest request)throws Exception{
        if (firealarm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        firealarm.setStatus(AuthConstants.DEL_FLAG_DELETE);
        firealarmServices.delete(firealarm);
        return ApiResult.success();
    }

    /**
     * @param firealarmid
     * @param request
     * @Method selectById
     * @Author Wxz
     * @Version 1.0
     * @Description 获取报警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:35
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String firealarmid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(firealarmid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(firealarmServices.one(firealarmid));
    }

    /**
     * @param firealarm
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新报警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:38
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody  Firealarm firealarm , HttpServletRequest request) throws Exception {
        if (firealarm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        firealarmServices.update(firealarm, tokenModel);

        //获取并立即推送非误报且未完成的消防报警单
        Firealarm firealarmnew = new Firealarm();
        firealarmnew.setCompletesta("0");
        firealarmnew.setMisinformation("0");
        List<Firealarm> firealarms = firealarmServices.list(firealarmnew);
        webSocketVo.setTopfirealarmList(firealarms);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
        for (Firealarm fi : firealarms) {
            if (StringUtils.isNotEmpty(fi.getDeviceinformationid())) {
                Deviceinformation deviceinformation = deviceinFormationServices.one(fi.getDeviceinformationid());
                if (deviceinformation != null && StringUtils.isNotEmpty(deviceinformation.getMapid())) {
                    mapBox_mapLevelServices.remarkSet(deviceinformation.getMapid(), true, tokenModel);
                }
            }
        }
        return ApiResult.success();
    }
}
