package com.nt.service_Auth;

import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Auth
 * @ClassName: RoleService
 * @Description: 角色的相关Services
 * @Author: WenChao
 * @CreateDate: 2018/12/06
 * @UpdateUser: WenChao
 * @UpdateDate: 2018/12/06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface RoleService {

    //获取角色列表
    List<Role> getRoleList(Role role) throws Exception;

    //获取角色详细信息
    Role getRoleInfo(String roleid) throws Exception;

    //获取所有应用和菜单信息
    List<AppPermission> selectAllApplications() throws Exception;

    //创建/更新角色信息
    void saveRole(Role role) throws Exception;

    //创建/更新应用和菜单信息
    void saveMenus(AppPermission appPermission) throws Exception;

    //获取角色成员信息
    List<MembersVo> getMembers(String roleid) throws Exception;

}
