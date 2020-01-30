package com.nt.controller.PHINEController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.PHINEController
 * @ClassName: PHINE10000Controller
 * @Description: PHINE平台项目接口类
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@RestController
@RequestMapping("/auth")
public class PHINE10000Controller {

    @Autowired
    private ProjectinfoService projectinfoService;

    /**
     * @return List<ProjectListVo>平台项目信息列表
     * @Method getProjectInfoList
     * @Author SKAIXX
     * @Description 平台项目画面获取项目列表
     * @Date 2020/1/30 15:46
     * @Param ownerid
     **/
    @RequestMapping(value = "/getProjectInfoList",method={RequestMethod.GET})
    public ApiResult getProjectInfoList(String ownerid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(ownerid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectinfoService.getProjectInfoList(ownerid));
    }
}
