package com.nt.controller.PHINEController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_PHINE.Project2deviceExtend;
import com.nt.dao_PHINE.Project2userExtend;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.service_PHINE.ChipinfoService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.ProjectinfoService;
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
 * @ProjectName: PHINE管理平台
 * @Package: com.nt.controller.PHINEController.PHINE40000Controller
 * @ClassName: PHINE40000Controller
 * @Author: MYT
 * @Description: PHINE项目状态Controller
 * @Date: 2020/1/30 14.01
 * @Version: 1.0
 */
@RestController
@RequestMapping("/PHINE40000")
public class PHINE40000Controller {

    @Autowired
    private DeviceinfoService deviceinfoService;

    /**
     * @方法名：getAllDeviceStatus
     * @描述：获取全部设备状态信息
     * @创建日期：2020/2/6
     * @作者：MYT
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getAllDeviceStatus", method = {RequestMethod.GET})
    public ApiResult getAllDeviceStatus(String companyid) throws Exception {
        return ApiResult.success(deviceinfoService.getAllDeviceStatus(companyid));
    }
}
