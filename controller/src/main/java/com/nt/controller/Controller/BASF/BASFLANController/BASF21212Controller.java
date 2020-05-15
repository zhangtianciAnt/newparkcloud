package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.EmailConfig;
import com.nt.dao_BASF.SendEmail;
import com.nt.service_BASF.EmailConfigServices;
import com.nt.service_BASF.SendEmailServices;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21212Controller
 * @Author: 齐铎
 * @Description: 即将到期人员Controller
 * @Date: 2020/3/18 17:41
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21212")
public class BASF21212Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TrainjoinlistServices trainjoinlistServices;


    //获取参加过培训的人员信息
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(trainjoinlistServices.overduepersonnellist());
    }


    //即将到期人员导出
    @PostMapping("/exportData")
    public ApiResult exportData(HttpServletRequest request) throws Exception{
        return ApiResult.success(trainjoinlistServices.exportData());
    }
}
