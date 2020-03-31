package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Highriskarea;
import com.nt.dao_BASF.Riskassessments;
import com.nt.service_BASF.RiskassessmentServices;
import com.nt.service_BASF.RiskassessmentsServices;
import com.nt.service_BASF.mapper.RiskassessmentsMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    //获取风险研判数据
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(riskassessmentServices.getData());
    }

    //风险研判承诺公告更新
    @RequestMapping(value = "/noticeUpdata", method = {RequestMethod.GET})
    public ApiResult noticeUpdata(String notice, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        riskassessmentServices.noticeUpdata(notice, tokenModel);
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
        return ApiResult.success();
    }

    //增加风险研判数据
    @PostMapping("/insertRiskassessments")
    public ApiResult insertRiskassessments(@RequestBody Riskassessments riskassessments, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
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
        return ApiResult.fail(riskassessmentsServices.writeList());
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
        return ApiResult.success();
    }

    //高风险作业更新
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Highriskarea highriskarea, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        riskassessmentservices.update(tokenModel, highriskarea);
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
