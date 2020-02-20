package com.nt.controller.PHINEController;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Vo.FilemarkVo;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.dao_PHINE.Vo.ReadWriteTestVo;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.FilemarkService;
import com.nt.service_PHINE.OperationrecordService;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private DeviceinfoService deviceinfoService;

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
        operationrecordService.addOperationrecord(tokenModel,operation);
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
    public ApiResult saveFileMark(HttpServletRequest request, @RequestBody FilemarkVo filemarkVo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return filemarkService.saveFileMarkInfo(tokenModel, filemarkVo);
    }

    /**
     * @Method getProjectinfoById
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

    /**
     * @Method getCommunicationDeviceInfo
     * @Author MYT
     * @Description 获取通信操作设备信息
     * @Date 2020/2/3 16:56
     * @Param projectId 项目ID
     **/
    @RequestMapping(value = "/getCommunicationDeviceInfo", method = {RequestMethod.GET})
    public ApiResult getCommunicationDeviceInfo(HttpServletRequest request, @RequestParam String projectId) throws Exception {
        if (StringUtils.isEmpty(projectId)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(deviceinfoService.getCommunicationDeviceInfo(tokenModel, projectId));
    }

    /**
     * @Method createConnection
     * @Author MYT
     * @Description 设备创建连接
     * @Date 2020/2/3 16:56
     * @Param deviceId 设备ID
     **/
    @RequestMapping(value = "/createConnection", method = {RequestMethod.PUT})
    public ApiResult createConnection(HttpServletRequest request, @RequestParam String deviceId) throws Exception {
        if (StringUtils.isEmpty(deviceId)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return deviceinfoService.createConnection(tokenModel, deviceId);
    }

    /**
     * @Method closeConnection
     * @Author MYT
     * @Description 设备关闭连接
     * @Date 2020/2/3 16:56
     * @Param deviceId 设备ID
     **/
    @RequestMapping(value = "/closeConnection", method = {RequestMethod.PUT})
    public ApiResult closeConnection(HttpServletRequest request, @RequestParam String deviceId) throws Exception {
        if (StringUtils.isEmpty(deviceId)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return deviceinfoService.closeConnection(tokenModel, deviceId);
    }

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
        return deviceinfoService.logicFileLoad(tokenModel, fileinfoList);
    }

    /**
     * @return
     * @Method readWriteTest
     * @Author SKAIXX
     * @Description 设备读写测试
     * @Date 2020/2/20 14:04
     * @Param
     **/
    @RequestMapping(value = "/readWriteTest", method = {RequestMethod.GET})
    public ApiResult readWriteTest(HttpServletRequest request, @RequestParam String projectid) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        List<ReadWriteTestVo> readWriteTestVoList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        for (int i = 0; i < 1; i++) {
            ReadWriteTestVo readWriteTestVo = new ReadWriteTestVo();
            readWriteTestVo.setDeviceid("device_1");
            readWriteTestVo.setResult(df.format(new Date()) + "-FPGA" + (i+1) + "读取成功，写入失败！");
            readWriteTestVoList.add(readWriteTestVo);
        }
        for (int i = 0; i < 1; i++) {
            ReadWriteTestVo readWriteTestVo = new ReadWriteTestVo();
            readWriteTestVo.setDeviceid("device_2");
            readWriteTestVo.setResult(df.format(new Date()) + "-FPGA" + (i+1) + "读取失败，写入成功！");
            readWriteTestVoList.add(readWriteTestVo);
        }
        return ApiResult.success(readWriteTestVoList);
    }

}
