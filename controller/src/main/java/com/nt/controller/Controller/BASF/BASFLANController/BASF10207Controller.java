package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.service_BASF.PersonnelPermissionsServices;
import com.nt.service_SQL.SqlViewDepartmentServices;
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
import java.util.ArrayList;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10207Controller
 * @Author:
 * @Description:
 * @Date:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10207")
public class BASF10207Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SqlViewDepartmentServices sqlviewdepartmentservices;

    @Autowired
    private PersonnelPermissionsServices personnelPermissionsServices;

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF10207
     * @Author:
     * @Description: 获取SQL部门列表
     * @Date: 2020/03/31
     * @Version: 1.0
     */
    @RequestMapping(value = "/getsqldepmentlist", method = {RequestMethod.POST})
    public ApiResult getsqldepmentlist(HttpServletRequest request) throws Exception {
        //获取SQL部门列表
        return ApiResult.success(sqlviewdepartmentservices.sqllist());
    }

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF10207
     * @Author:
     * @Description: 保存部门列表
     * @Date: 2020/03/31
     * @Version: 1.0
     */
    @RequestMapping(value = "/savedeplist", method = {RequestMethod.POST})
    public ApiResult savedeplist(@RequestBody ArrayList<PersonnelPermissions>  personnelPermissions, HttpServletRequest request) throws Exception {
        if (personnelPermissions == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        personnelPermissionsServices.insert(personnelPermissions, tokenModel);

        return ApiResult.success();
    }

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF10207
     * @Author:
     * @Description: 获取部门列表
     * @Date: 2020/03/31
     * @Version: 1.0
     */
    @RequestMapping(value = "/getdepmentlist", method = {RequestMethod.POST})
    public ApiResult getdepmentlist(HttpServletRequest request) throws Exception {
        //获取部门列表
        return ApiResult.success(personnelPermissionsServices.list());
    }
}
