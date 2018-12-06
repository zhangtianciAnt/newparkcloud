package com.nt.service_Auth.Impl;

import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.service_Auth.RoleService;
import com.nt.utils.AuthConstants;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TokenService tokenService;

    //获取角色列表
    @Override
    public List<Role> getRoleList(Role role) throws Exception {
        Query query = CustmizeQuery(role);
        return mongoTemplate.find(query, Role.class);
    }

    //获取角色详细信息
    @Override
    public Role getRoleInfo(String roleid) throws Exception {
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(roleid));
        return mongoTemplate.findOne(query,Role.class);
    }

    //获取所有应用和菜单信息
    @Override
    public List<AppPermission.menu> selectAllApplications() throws Exception {
        List<AppPermission.menu> result = new ArrayList<AppPermission.menu>();
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
        List<AppPermission> appList = mongoTemplate.find(query,AppPermission.class);
        for (int i = 0; i < appList.size(); i++) {
            List<AppPermission.menu> menuList = appList.get(i).getMenus();
            if (menuList != null && menuList.size() > 0){
                result.addAll(menuList);
            }
        }
        return result;
    }

    //获取角色和菜单关系信息
    @Override
    public void getMenuAuthToRole() throws Exception {

    }

    //获取角色和按钮关系信息
    @Override
    public void getActionAuthToRole() throws Exception {

    }

    //创建/更新角色信息
    @Override
    public void saveRole(Role role) throws Exception {
        mongoTemplate.save(role);
    }

    //创建/更新应用和菜单信息
    public void saveMenus(AppPermission appPermission) throws Exception {
        mongoTemplate.save(appPermission);
    }
}
