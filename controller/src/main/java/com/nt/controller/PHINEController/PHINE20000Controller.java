package com.nt.controller.PHINEController;

import com.nt.dao_PHINE.Operationdetail;
import com.nt.dao_PHINE.Project2device;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.FilemarkVo;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.service_PHINE.FilemarkService;
import com.nt.service_PHINE.OperationrecordService;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/PHINE20000")
public class PHINE20000Controller {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProjectinfoService projectinfoService;

    @Autowired
    private OperationrecordService operationrecordService;

    @Autowired
    private FilemarkService filemarkService;

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

    /**
     * @return
     * @Method addOperationrecord
     * @Author lxx
     * @Description 创建操作记录列表
     * @Date 2020/2/3 16:56
     * @Param
     **/
    @RequestMapping(value = "/addOperationrecord", method = {RequestMethod.POST})
    public ApiResult addOperationrecord(HttpServletRequest request, @RequestBody OperationRecordVo operation) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        operation.preInsert(tokenModel);
        for(Operationdetail detail : operation.getDetailist()){
            detail.preInsert(tokenModel);
        }
        operationrecordService.addOperationrecord(operation);
        return ApiResult.success();
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

    /**
     * @return
     * @Method saveFileMark
     * @Author MYT
     * @Description 保存文件标记
     * @Date 2020/2/11 16:56
     * @Param 文件标记信息
     **/
    @RequestMapping(value = "/saveFileMark", method = {RequestMethod.POST})
    public ApiResult saveFileMark(HttpServletRequest request, @RequestParam FilemarkVo filemarkVo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return filemarkService.saveFileMarkInfo(tokenModel, filemarkVo);
    }

    /** @Method getProjectinfoById
     * @Author MYT
     * @Description 根据项目ID获取项目信息
     * @Date 2020/2/3 16:56
            * @Param projectId 项目ID
     **/
    @RequestMapping(value = "/getProjectinfoById", method = {RequestMethod.GET})
    public ApiResult getProjectinfoById(HttpServletRequest request, @RequestParam String projectId) throws Exception {
        if (StringUtils.isEmpty(projectId)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectinfoService.getProjectinfoById(projectId));
    }
}
