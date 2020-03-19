package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21210Controller
 * @Author: 王哲
 * @Description: 培训档案Controller
 * @Date: 2020/3/18 17:41
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21210")
public class BASF21210Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TrainjoinlistServices trainjoinlistServices;

    //获取参加过培训的人员信息
    @PostMapping("/list")
    public ApiResult upTrain(HttpServletRequest request) throws Exception {
        return ApiResult.success(trainjoinlistServices.joinPersonnel(trainjoinlistServices.joinPersonnelid()));
    }


}
