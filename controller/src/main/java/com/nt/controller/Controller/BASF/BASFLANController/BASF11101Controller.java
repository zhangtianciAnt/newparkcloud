package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.service_BASF.RiskassessmentServices;
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

    @Autowired
    private TokenService tokenService;

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF11101
     * @Author: 王哲
     * @Description: EXECL导入
     * @Date: 2020/02/04
     * @Version: 1.0
     */
    @RequestMapping(value = "/importexcel", method = {RequestMethod.POST})
    public ApiResult importexcel(HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(riskassessmentServices.importexcel(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }


}
