package com.nt.controller.Controller.BASF.BASFLANController;


import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Commandrecord;
import com.nt.dao_BASF.Fireaccidentrecord;
import com.nt.dao_BASF.Firealarm;
import com.nt.service_BASF.CommandrecordServices;
import com.nt.service_BASF.FireaccidentrecordServices;
import com.nt.service_BASF.FirealarmServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10203Controller
 * @Author: SUN
 * @Description: BASF消防事故记录Controller
 * @Date: 2019/11/11 17:22
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10203")
public class BASF10203Controller {

    @Autowired
    private FireaccidentrecordServices fireaccidentrecordServices;

    @Autowired
    private FirealarmServices firealarmServices;

    @Autowired
    private CommandrecordServices commandrecordService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody Fireaccidentrecord fireaccidentrecord ,HttpServletRequest request) throws Exception {
        return ApiResult.success(fireaccidentrecordServices.list(fireaccidentrecord));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String fireaccidentrecordid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(fireaccidentrecordid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(fireaccidentrecordServices.selectById(fireaccidentrecordid));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody Fireaccidentrecord fireaccidentrecord, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        fireaccidentrecordServices.insert(tokenModel,fireaccidentrecord);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Fireaccidentrecord fireaccidentrecord, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        fireaccidentrecordServices.update(tokenModel,fireaccidentrecord);
        return ApiResult.success();
    }

    @RequestMapping(value = "/excelout", method = {RequestMethod.GET})
    public void excelout(String fireaccidentrecordid, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        判断传来的参数是否存在
        if (StrUtil.isEmpty(fireaccidentrecordid)) {
            //不存在，返回错误提示
            return;
        } else {
            Fireaccidentrecord fireaccidentrecord = new Fireaccidentrecord();
            Firealarm firealarm = new Firealarm();
            Commandrecord commandrecord = new Commandrecord();
            //获取消防事故记录
            fireaccidentrecord = fireaccidentrecordServices.selectById(fireaccidentrecordid);
            //获取消防报警单
            if (fireaccidentrecord.getFirealarmid() != null) {
                String firealarmid = fireaccidentrecord.getFirealarmid();
                firealarm = firealarmServices.one(firealarmid);
            }
            //获取消防接警单
            if (fireaccidentrecord.getCommandrecordid() != null) {
                String commandrecordid = fireaccidentrecord.getCommandrecordid();
                commandrecord = commandrecordService.get(commandrecordid);
            }
            fireaccidentrecordServices.excelexport(fireaccidentrecord, firealarm, commandrecord, response);
        }
        /*ExcelController excelController=new ExcelController();
        excelController.save(response);*/
    }
}
