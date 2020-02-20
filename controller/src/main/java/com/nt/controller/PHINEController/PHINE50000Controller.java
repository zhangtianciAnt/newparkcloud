package com.nt.controller.PHINEController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_PHINE.Cabinetinfo;
import com.nt.dao_PHINE.Machineroominfo;
import com.nt.dao_PHINE.Vo.DeviceinfoVo;
import com.nt.service_PHINE.CabinetinfoService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.MachineroominfoService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
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
     * @Method getDeviceInfoList
     * @Author SKAIXX
     * @Description 获取设备详情
     * @Date 2020/2/1 23:46
     * @Param none
     **/
    @RequestMapping(value = "/getDeviceInfo",method={RequestMethod.GET})
    public ApiResult getDeviceInfo(HttpServletRequest request, String id) throws Exception {
        if (StrUtil.isEmpty(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(deviceinfoService.getDeviceInfo(id));
    }

    /**
     * @return
     * @Method saveDeviceInfo
     * @Author SKAIXX
     * @Description 创建设备信息
     * @Date 2020/2/7 16:10
     * @Param
     **/
    @RequestMapping(value = "/saveDeviceInfo",method={RequestMethod.POST})
    public ApiResult saveDeviceInfo(HttpServletRequest request, @RequestBody DeviceinfoVo deviceinfoVo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return deviceinfoService.saveDeviceInfo(tokenModel, deviceinfoVo);
    }

    /**
     * @return
     * @Method updateDeviceInfo
     * @Author SKAIXX
     * @Description 更新设备信息
     * @Date 2020/2/7 16:10
     * @Param
     **/
    @RequestMapping(value = "/updateDeviceInfo",method={RequestMethod.POST})
    public ApiResult updateDeviceInfo(HttpServletRequest request, @RequestBody DeviceinfoVo deviceinfoVo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return deviceinfoService.updateDeviceInfo(tokenModel, deviceinfoVo);
    }

    /**
     * @return
     * @Method deleteDeviceInfo
     * @Author SKAIXX
     * @Description 删除设备信息
     * @Date 2020/2/7 16:10
     * @Param
     **/
    @RequestMapping(value = "/deleteDeviceInfo",method={RequestMethod.GET})
    public ApiResult deleteDeviceInfo(HttpServletRequest request, String id) throws Exception {
        return deviceinfoService.deleteDeviceInfo(id);
    }

    // region 机房相关Api
    /**
     * @return
     * @Method getMachineRoomInfo
     * @Author SKAIXX
     * @Description 获取机房信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/getMachineRoomInfo",method={RequestMethod.POST})
    public ApiResult getMachineRoomInfo(HttpServletRequest request, @RequestBody Machineroominfo machineroominfo) throws Exception {
        return ApiResult.success(machineroominfoService.getMachineRoomInfo(machineroominfo));
    }

    /**
     * @return
     * @Method getCabinetInfo
     * @Author MYT
     * @Description 获取机柜信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/getCabinetInfo",method={RequestMethod.POST})
    public ApiResult getCabinetInfo(HttpServletRequest request, @RequestBody Cabinetinfo cabinetinfo) throws Exception {
        return ApiResult.success(cabinetinfoService.getCabinetInfo(cabinetinfo));
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

    /**
     * @return
     * @Method getMachineroominfoList
     * @Author SKAIXX
     * @Description 获取机房列表
     * @Date 2020/2/6 10:28
     * @Param
     **/
    @RequestMapping(value = "/getMachineroominfoList",method={RequestMethod.GET})
    public ApiResult getMachineroominfoList(HttpServletRequest request) throws Exception {
        return ApiResult.success(machineroominfoService.getMachineroominfoList());
    }
    // endregion

    // region 机柜相关Api
    /**
     * @return
     * @Method saveCabinetInfo
     * @Author SKAIXX
     * @Description 创建机柜信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/saveCabinetInfo",method={RequestMethod.POST})
    public ApiResult saveCabinetInfo(HttpServletRequest request, @RequestBody Cabinetinfo cabinetinfo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return cabinetinfoService.saveCabinetInfo(tokenModel, cabinetinfo);
    }

    /**
     * @return
     * @Method updateCabinetInfo
     * @Author SKAIXX
     * @Description 更新机柜信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/updateCabinetInfo",method={RequestMethod.POST})
    public ApiResult updateCabinetInfo(HttpServletRequest request, @RequestBody Cabinetinfo cabinetinfo) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return cabinetinfoService.updateCabinetInfo(tokenModel, cabinetinfo);
    }

    /**
     * @return
     * @Method deleteCabinetInfo
     * @Author SKAIXX
     * @Description 删除机柜信息
     * @Date 2020/2/5 22:28
     * @Param
     **/
    @RequestMapping(value = "/deleteCabinetInfo",method={RequestMethod.POST})
    public ApiResult deleteCabinetInfo(HttpServletRequest request, @RequestBody Cabinetinfo cabinetinfo) throws Exception {
        return cabinetinfoService.deleteCabinetInfo(cabinetinfo);
    }

    /**
     * @return
     * @Method getCabinetInfoList
     * @Author SKAIXX
     * @Description 获取指定机房中的机柜列表
     * @Date 2020/2/6 10:28
     * @Param
     **/
    @RequestMapping(value = "/getCabinetInfoList",method={RequestMethod.GET})
    public ApiResult getCabinetInfoList(HttpServletRequest request, String machineroomid) throws Exception {
        return ApiResult.success(cabinetinfoService.getCabinetinfoListByMachineroomid(machineroomid));
    }
    // endregion
}
