package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.service_BASF.DutyServices;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10206Controller
 * @Author: 王哲
 * @Description: 值班信息控制器
 * @Date: 2019/12/25 11:05
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10206")
public class BASF10206Controller {

    @Autowired
    private DutyServices dutyServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF10206
     * @Author: 王哲
     * @Description: EXECL导入
     * @Date: 2019/12/25
     * @Version: 1.0
     */
    @RequestMapping(value = "/importexcel", method = {RequestMethod.POST})
    public ApiResult importexcel(HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(dutyServices.importexcel(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF10206
     * @Author: 王哲
     * @Description: EXECL导入
     * @Date: 2019/12/25
     * @Version: 1.0
     */
    @RequestMapping(value = "/selectDayDuty", method = {RequestMethod.POST})
    public ApiResult selectDayDuty(HttpServletRequest request) throws Exception {
        //返回当天值班信息
        return ApiResult.success(dutyServices.selectDayDuty());
    }
}
