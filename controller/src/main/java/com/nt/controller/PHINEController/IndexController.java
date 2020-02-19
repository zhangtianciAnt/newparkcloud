package com.nt.controller.PHINEController;

import com.nt.service_PHINE.IndexService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.PHINEController
 * @ClassName: IndexController
 * @Description: 仪表盘接口类
 * @Author: SKAIXX
 * @CreateDate: 2020/2/19
 * @Version: 1.0
 */
@RestController
@RequestMapping("/Index")
public class IndexController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IndexService indexService;

    /**
     * @return
     * @Method getDashboardInfo
     * @Author SKAIXX
     * @Description 获取仪表盘图表数据
     * @Date 2020/2/19 10:23
     * @Param
     **/
    @RequestMapping(value = "/getDashboardInfo",method={RequestMethod.GET})
    public ApiResult getDashboardInfo(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(indexService.getDashboardInfo(tokenModel));
    }
}
