package com.nt.controller.PHINEController;

import com.nt.service_PHINE.OperationrecordService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/PHINE20000")
public class PHINE20000Controller {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private OperationrecordService operationrecordService;

    @RequestMapping(value = "/getFileByVersion", method = {RequestMethod.GET})
    public ApiResult getFileByVersion(HttpServletRequest request, @RequestParam String version) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return null;
    }

    /**
     * @return
     * @Method getOperationrecordList
     * @Author SKAIXX
     * @Description 项目管理画面获取操作记录列表
     * @Date 2020/2/3 16:56
     * @Param
     **/
    @RequestMapping(value = "/getOperationrecordList", method = {RequestMethod.GET})
    public ApiResult getOperationrecordList(HttpServletRequest request, @RequestParam String projectId) throws Exception {
        if (StringUtils.isEmpty(projectId)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(operationrecordService.getOperationrecordList(projectId));
    }
}
