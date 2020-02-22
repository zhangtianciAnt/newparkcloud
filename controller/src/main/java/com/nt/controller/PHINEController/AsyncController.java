package com.nt.controller.PHINEController;


import com.nt.dao_PHINE.Fileinfo;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/async")
public class AsyncController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DeviceinfoService deviceinfoService;

    /**
     * 直接使用异步线程池
     */
    @Autowired
    private Executor asyncExecutor;

    /**
     * @Method closeConnection
     * @Author MYT
     * @Description 设备关闭连接
     * @Date 2020/2/3 16:56
     * @Param deviceId 设备ID
     **/
    @RequestMapping(value = "/logicFileLoad", method = {RequestMethod.POST})
    public ApiResult logicFileLoad(HttpServletRequest request, @RequestBody List<Fileinfo> fileinfoList) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        asyncExecutor.execute(() -> System.out.println("异步线程池执行...."));
        return deviceinfoService.logicFileLoad(tokenModel, fileinfoList);
    }
}
