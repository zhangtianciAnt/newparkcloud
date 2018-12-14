package com.nt.service_Auth;

import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.AuthVo;
import com.nt.dao_Auth.Vo.MembersVo;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Auth
 * @ClassName: AuthService
 * @Description: 权限的相关Services
 * @Author: WenChao
 * @CreateDate: 2018/12/12
 * @UpdateUser: WenChao
 * @UpdateDate: 2018/12/12
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface AuthService {

    //获取ownerlist
    List<String> getOwnerList(String url, String useraccountid) throws Exception;

    //获取按钮权限（新建，编辑，删除）
    List<Boolean> getActionsAuth(String url, String useraccountid, String ownerid) throws Exception;

    //获取新建按钮权限
    Boolean getNewActionAuth(String url, String useraccountid) throws Exception;

}
