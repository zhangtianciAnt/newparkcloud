package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Startprogram;
import com.nt.service_BASF.StartprogramServices;
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

    //获取未开班培训列表
    @RequestMapping(value = "/nostart", method = {RequestMethod.POST})
    public ApiResult nostart(HttpServletRequest request) throws Exception {
        return ApiResult.success(startprogramServices.nostart());
    }

    //添加培训参加名单
    @RequestMapping(value = "/addjoinlist", method = {RequestMethod.POST})
    public ApiResult addjoinlist(@RequestBody String personnelid, HttpServletRequest request) throws Exception {

        return ApiResult.success();
    }

}
