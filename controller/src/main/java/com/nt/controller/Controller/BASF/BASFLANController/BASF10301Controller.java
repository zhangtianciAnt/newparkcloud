package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Config.BASF.MultiThreadScheduleTask;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.dao_BASF.Environment;
import com.nt.dao_BASF.Pimspoint;
import com.nt.dao_BASF.VO.EpChartVo;
import com.nt.service_BASF.DeviceInformationServices;
import com.nt.service_BASF.EnvironmentServices;
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
 * @ClassName: BASF10301Controller
 * @Author: WXL
 * @Description: BASF环保工艺数据监控管理模块Controller
 * @Date: 2019/11/14 16：30
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10301")
public class BASF10301Controller {

    @Autowired
    private EnvironmentServices environmentServices;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DeviceInformationServices deviceInformationServices;

    /**
     * @param request
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取监控数据列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/14 16：35
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(environmentServices.list());
    }

    /**
     * @param pimspointid
     * @param request
     * @Method selectById
     * @Author WXL
     * @Version 1.0
     * @Description 获取数据监控详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:35
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String pimspointid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(pimspointid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(environmentServices.one(pimspointid));
    }

    /**
     * @param pimspoint
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新接警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:38
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Pimspoint pimspoint, HttpServletRequest request) throws Exception {
        if (pimspoint == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        environmentServices.update(pimspoint, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getEpChartVo", method = {RequestMethod.GET})
    public ApiResult getEpChartVo(HttpServletRequest request) throws Exception {
        List<EpChartVo> epChartVoList = deviceInformationServices.getEpChartVo();
        MultiThreadScheduleTask.webSocketVo.setEpChartVoList(epChartVoList);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success(epChartVoList);
    }
}
