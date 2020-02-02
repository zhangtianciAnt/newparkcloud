package com.nt.controller.PHINEController;

import com.nt.service_PHINE.DeviceinfoService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
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
    private DeviceinfoService deviceinfoService;

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
}
