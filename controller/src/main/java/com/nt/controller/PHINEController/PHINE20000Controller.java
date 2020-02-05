package com.nt.controller.PHINEController;

import com.nt.dao_PHINE.Project2device;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.service_PHINE.OperationrecordService;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/PHINE20000")
public class PHINE20000Controller {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProjectinfoService projectinfoService;

//    @Autowired
//    private OperationrecordService operationrecordService;

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
        return ApiResult.success(null);
//        return ApiResult.success(operationrecordService.getOperationrecordList(projectId));
    }

    /**
     * @return
     * @Method getDeviceIdByProjectId
     * @Author MYT
     * @Description 根据项目ID获取设备列表
     * @Date 2020/2/3 16:56
     * @Param projectId 项目ID
     **/
    @RequestMapping(value = "/getDeviceIdByProjectId", method = {RequestMethod.GET})
    public ApiResult getDeviceIdByProjectId(HttpServletRequest request, @RequestParam String projectId) throws Exception {
        if (StringUtils.isEmpty(projectId)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectinfoService.getDeviceIdByProjectId(projectId));
    }
}
