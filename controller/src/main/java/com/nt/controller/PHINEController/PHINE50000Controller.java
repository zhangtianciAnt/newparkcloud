package com.nt.controller.PHINEController;

import com.nt.dao_PHINE.Cabinetinfo;
import com.nt.dao_PHINE.Machineroominfo;
import com.nt.service_PHINE.CabinetinfoService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.MachineroominfoService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.PHINEController
 * @ClassName: PHINE50000Controller
 * @Description: PHINE设备管理接口类
 * @Author: SKAIXX
 * @CreateDate: 2020/2/1
 * @Version: 1.0
 */
@RestController
@RequestMapping("/PHINE50000")
public class PHINE50000Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DeviceinfoService deviceinfoService;

    @Autowired
    private MachineroominfoService machineroominfoService;

    @Autowired
    private CabinetinfoService cabinetinfoService;

    /**
     * @return List<DeviceListVo>设备信息列表
     * @Method getDeviceInfoList
     * @Author SKAIXX
     * @Description 设备一览画面获取设备列表
     * @Date 2020/2/1 23:46
     * @Param none
     **/
    @RequestMapping(value = "/getDeviceInfoList",method={RequestMethod.GET})
    public ApiResult getDeviceInfoList(HttpServletRequest request) throws Exception {
        return ApiResult.success(deviceinfoService.getDeviceInfoList());
    }

    /**
     * @return
     * @Method saveMachineRoomInfo
     * @Author SKAIXX
     * @Description 创建机房信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/saveMachineRoomInfo",method={RequestMethod.POST})
    public ApiResult saveMachineRoomInfo(HttpServletRequest request, @RequestBody Machineroominfo machineroominfo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return machineroominfoService.saveMachineRoomInfo(tokenModel, machineroominfo);
    }

    /**
     * @return
     * @Method updateMachineRoomInfo
     * @Author SKAIXX
     * @Description 更新机房信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/updateMachineRoomInfo",method={RequestMethod.POST})
    public ApiResult updateMachineRoomInfo(HttpServletRequest request, @RequestBody Machineroominfo machineroominfo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return machineroominfoService.updateMachineRoomInfo(tokenModel, machineroominfo);
    }

    /**
     * @return
     * @Method deleteMachineRoomInfo
     * @Author SKAIXX
     * @Description 删除机房信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/deleteMachineRoomInfo",method={RequestMethod.POST})
    public ApiResult deleteMachineRoomInfo(HttpServletRequest request, @RequestBody Machineroominfo machineroominfo) throws Exception {
        return machineroominfoService.deleteMachineRoomInfo(machineroominfo);
    }
}
