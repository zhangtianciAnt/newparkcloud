package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Log;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.LogService;
import com.nt.service_Org.UserService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.Controller
 * @ClassName: RoleController
 * @Description: 用户相关操作Controller
 * @Author: WenChao
 * @CreateDate: 2018/12/06
 * @UpdateUser: WenChao
 * @UpdateDate: 2018/12/06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenService tokenService;

    /**
     * @方法名：getRoleList
     * @描述：获取角色列表
     * @创建日期：2018/12/6
     * @作者：WENCHAO
     * @参数：[role, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getRoleList",method={RequestMethod.POST})
    public ApiResult getRoleList(@RequestBody Role role, HttpServletRequest request) throws Exception {
        if (role == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        role.setOwners(tokenModel.getOwnerList());
        role.setTenantid(tokenModel.getTenantId());
        role.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        return ApiResult.success(roleService.getRoleList(role));
    }

    @RequestMapping(value = "/getRoleInfo",method={RequestMethod.GET})
    public ApiResult getRoleInfo(String roleid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(roleid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        return ApiResult.success(roleService.getRoleInfo(roleid));
    }

    /**
     * @方法名：saveRole
     * @描述：创建/更新角色信息
     * @创建日期：2018/12/6
     * @作者：WENCHAO
     * @参数：[role, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveRole",method={RequestMethod.POST})
    public ApiResult saveRole(@RequestBody Role role, HttpServletRequest request) throws Exception {
        if (role == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        if(role.getCreateby() == null || role.getCreateon() == null){
            role.preInsert(tokenModel);
        }
        role.preUpdate(tokenModel);
        roleService.saveRole(role);
        return ApiResult.success();
    }

    /**
     * @方法名：saveMenus
     * @描述：创建/更新应用和菜单信息
     * @创建日期：2018/12/6
     * @作者：WENCHAO
     * @参数：[appPermission, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveMenus",method={RequestMethod.POST})
    public ApiResult saveMenus(@RequestBody AppPermission appPermission, HttpServletRequest request) throws Exception {
        if (appPermission == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        if(appPermission.getCreateby() == null || appPermission.getCreateon() == null){
            appPermission.preInsert(tokenModel);
        }
        appPermission.preUpdate(tokenModel);
        roleService.saveMenus(appPermission);
        return ApiResult.success();
    }

    /**
     * @方法名：delRoleInfo
     * @描述：删除角色
     * @创建日期：2018/12/6
     * @作者：WENCHAO
     * @参数：[role, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/delRoleInfo",method={RequestMethod.POST})
    public ApiResult delRoleInfo(@RequestBody Role role, HttpServletRequest request) throws Exception {
        if (role == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        //获取最新角色信息
        Role temp = roleService.getRoleInfo(role.get_id());
        if (temp != null) {
            //更新人和更新时间
            temp.preUpdate(tokenModel);
            //逻辑删除
            temp.setStatus(AuthConstants.DEL_FLAG_DELETE);
            //保存
            roleService.saveRole(temp);
        }
        return ApiResult.success();
    }

    /**
     * @方法名：selectAllApplications
     * @描述：获取所有应用和菜单
     * @创建日期：2018/12/6
     * @作者：WENCHAO
     * @参数：[]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/selectAllApplications",method={RequestMethod.GET})
    public ApiResult selectAllApplications() throws Exception {
        return ApiResult.success(roleService.selectAllApplications());
    }
}
