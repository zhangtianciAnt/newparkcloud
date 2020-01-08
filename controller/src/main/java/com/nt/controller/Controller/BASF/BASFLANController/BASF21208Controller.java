package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.VO.TrainjoinlistVo;
import com.nt.service_BASF.StartprogramServices;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.service_BASF.mapper.TrainjoinlistMapper;
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

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21208Controller
 * @Author: 王哲
 * @Description: 培训申请
 * @Date: 2020/1/7 10:15
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21208")
public class BASF21208Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StartprogramServices startprogramServices;

    @Autowired
    private TrainjoinlistServices trainjoinlistServices;

    //获取未开班培训列表
    @RequestMapping(value = "/nostart", method = {RequestMethod.POST})
    public ApiResult nostart(HttpServletRequest request) throws Exception {
        return ApiResult.success(startprogramServices.nostart());
    }

    //获取获取未开班参加人员名单
    @RequestMapping(value = "/getjoinlist", method = {RequestMethod.GET})
    public ApiResult getjoinlist(String personnelid, HttpServletRequest request) throws Exception {
        return ApiResult.success(trainjoinlistServices.joinlist(personnelid));
    }

    //添加培训参加人员名单
    @RequestMapping(value = "/addjoinlist", method = {RequestMethod.POST})
    public ApiResult addjoinlist(@RequestBody TrainjoinlistVo trainjoinlistVo, HttpServletRequest request) throws Exception {
        if (trainjoinlistVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        trainjoinlistServices.insert(trainjoinlistVo, tokenModel);
        return ApiResult.success();
    }


}
