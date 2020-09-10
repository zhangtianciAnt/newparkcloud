package com.nt.controller.Controller.BASF.BASFLANController;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Config.BASF.MultiThreadScheduleTask;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketDeviceinfoVo;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.dao_BASF.Highriskarea;
import com.nt.dao_BASF.Riskassessment;
import com.nt.dao_BASF.Riskassessments;
import com.nt.service_BASF.HighriskareaServices;
import com.nt.service_BASF.RiskassessmentServices;
import com.nt.service_BASF.RiskassessmentsServices;
import com.nt.service_BASF.mapper.RiskassessmentsMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF11101Controller
 * @Author: 王哲
 * @Description:风险判研
 * @Date: 2020/02/03 16:06
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF11101")
public class BASF11101Controller {

    @Autowired
    private RiskassessmentServices riskassessmentServices;

    //风险研判（MySql表）
    @Autowired
    private RiskassessmentsServices riskassessmentsServices;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RiskassessmentServices riskassessmentservices;

    @Autowired
    private HighriskareaServices highriskareaServices;

    //获取风险研判数据
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        Riskassessment riskassessment = riskassessmentServices.getData();
        MultiThreadScheduleTask.webSocketVo.setRiskassessment(riskassessment);
        return ApiResult.success(riskassessment);
    }

    //获取高风险作业
    @RequestMapping(value = "/getHighRisk", method = {RequestMethod.POST})
    public ApiResult getHighRisk(HttpServletRequest request) throws Exception {
        List<Highriskarea> highriskareas = highriskareaServices.list();
        MultiThreadScheduleTask.webSocketVo.setHighriskareaList(highriskareas);
        return ApiResult.success(highriskareaServices.list());
    }

    //风险研判承诺公告更新
    @RequestMapping(value = "/noticeUpdata", method = {RequestMethod.GET})
    public ApiResult noticeUpdata(String notice, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        riskassessmentServices.noticeUpdata(notice, tokenModel);

        //获取风险判研信息(MongoDB)
        MultiThreadScheduleTask.webSocketVo.setRiskassessment(riskassessmentServices.getData());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }

    //region 风险研判（MySql表）

    //获取风险研判数据
    @RequestMapping(value = "/getAll", method = {RequestMethod.POST})
    public ApiResult getAll(@RequestBody Riskassessments riskassessments, HttpServletRequest request) throws Exception {
        return ApiResult.success(riskassessmentsServices.getAll(riskassessments));
    }

    //更新风险研判数据
    @PostMapping("/updataRiskassessments")
    public ApiResult updataRiskassessments(@RequestBody Riskassessments riskassessments, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        riskassessmentsServices.updataRiskassessments(riskassessments, tokenModel);

        //获取风险研判信息（MySql）
        MultiThreadScheduleTask.webSocketVo.setRiskassessmentsList(riskassessmentsServices.writeList());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));

        return ApiResult.success();
    }

    //增加风险研判数据
    @PostMapping("/insertRiskassessments")
    public ApiResult insertRiskassessments(@RequestBody Riskassessments riskassessments, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        //获取风险研判信息（MySql）
        MultiThreadScheduleTask.webSocketVo.setRiskassessmentsList(riskassessmentsServices.writeList());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success(riskassessmentsServices.insertRiskassessments(riskassessments, tokenModel));
    }

    //根据id查找风险研判数据
    @GetMapping("/getDataById")
    public ApiResult getDataById(String id, HttpServletRequest request) throws Exception {
        if (StringUtils.isNotEmpty(id)) {
            return ApiResult.success(riskassessmentsServices.getDataById(id));
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

    }

    //根据装置code查找今日有无填写信息
    @GetMapping("checkExist")
    public ApiResult checkExist(String devicecode, HttpServletRequest request) throws Exception {
        if (StringUtils.isNotEmpty(devicecode)) {
            return ApiResult.success(riskassessmentsServices.checkExist(devicecode));
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
    }

    //查询装置今日已填写的风险研判信息
    @PostMapping("/writeList")
    public ApiResult writeList(HttpServletRequest request) throws Exception {
        List<Riskassessments> riskassessments = riskassessmentsServices.writeList();
        MultiThreadScheduleTask.webSocketVo.setRiskassessmentsList(riskassessments);
        return ApiResult.success(riskassessments);
    }
    //endregion

    //region 高风险作业的相关方法
    //高风险作业查找
    @RequestMapping(value = "/selecthig", method = {RequestMethod.POST})
    public ApiResult selecthig(@RequestBody Highriskarea highriskarea, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(riskassessmentservices.selecthig(tokenModel, highriskarea));
    }

    //高风险作业添加
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody Highriskarea highriskarea, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        riskassessmentservices.insert(tokenModel, highriskarea);

        // 获取高风险作业清单
        MultiThreadScheduleTask.webSocketVo.setHighriskareaList(highriskareaServices.list());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }

    //高风险作业更新
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Highriskarea highriskarea, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        riskassessmentservices.update(tokenModel, highriskarea);

        // 获取高风险作业清单
        MultiThreadScheduleTask.webSocketVo.setHighriskareaList(highriskareaServices.list());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }

    //高风险作业删除
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Highriskarea highriskarea, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        highriskarea.setStatus(AuthConstants.DEL_FLAG_DELETE);
        riskassessmentservices.delete(tokenModel, highriskarea);
        return ApiResult.success();
    }
    //endregion
}
