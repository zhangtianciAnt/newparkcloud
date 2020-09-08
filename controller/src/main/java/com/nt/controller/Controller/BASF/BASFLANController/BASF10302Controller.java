package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Pimspoint;
import com.nt.dao_BASF.Pimsdata;
import com.nt.service_BASF.PimsdataServices;
import com.nt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10302Controller
 * @Author: myt
 * @Description: BASF环保数据PIMS系统数据Controller
 * @Date: 2020/08/17 16:30
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10302")
public class BASF10302Controller {

    @Autowired
    private PimsdataServices pimsdataServices;


    @RequestMapping(value = "/createPimsPoint", method = {RequestMethod.POST})
    public ApiResult createPimsPoint(@RequestBody Pimspoint pimsPoint) throws Exception {
        pimsdataServices.createPimsPoint(pimsPoint);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getPimsPoint", method = {RequestMethod.GET})
    public ApiResult getPimsPoint() throws Exception {
        return ApiResult.success(pimsdataServices.getPimsPoint());
    }


    /**
     * @param pimsdata
     * @param request
     * @Method create
     * @Author myt
     * @Version 1.0
     * @Description 导入PIMS系统数据
     * @Return com.nt.utils.ApiResult
     * @Date 2020/08/17 16:30
     */
    @RequestMapping(value = "/import", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody List<Pimsdata> pimsdata, HttpServletRequest request) throws Exception {
        if (pimsdata == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        pimsdataServices.insert(pimsdata);
        return ApiResult.success();
    }

    /**
     * @Description:获取大屏环境相关信息
     * @Param: [request]
     * @return: com.nt.utils.ApiResult
     * @Author: Mr.LXX
     * @Date: 2020/8/19
     */
    @RequestMapping(value = "/getAllPimsInfo", method = {RequestMethod.GET})
    public ApiResult getAllPimsInfo(String type, Integer hour, HttpServletRequest request) throws Exception {
        return ApiResult.success(pimsdataServices.getAllPimsInfo(type,hour));
    }
}
