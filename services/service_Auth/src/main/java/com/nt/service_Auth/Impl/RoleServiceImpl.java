package com.nt.service_Auth.Impl;

import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Auth.RoleService;
import com.nt.utils.AuthConstants;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public List<AppPermission> selectAllApplications() throws Exception {
        //List<AppPermission.menu> result = new ArrayList<AppPermission.menu>();
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
        List<AppPermission> appList = mongoTemplate.find(query,AppPermission.class);
//        for (int i = 0; i < appList.size(); i++) {
//            List<AppPermission.menu> menuList = appList.get(i).getMenus();
//            if (menuList != null && menuList.size() > 0){
//                result.addAll(menuList);
//            }
//        }
        return appList;
    }

    //创建/更新角色信息
    @Override
    public void saveRole(Role role) throws Exception {
        mongoTemplate.save(role);
    }

    //创建/更新应用和菜单信息
    @Override
    public void saveMenus(AppPermission appPermission) throws Exception {
        mongoTemplate.save(appPermission);
    }

    //获取角色成员信息
    public List<MembersVo> getMembers(String roleid) throws Exception {
        List<MembersVo> result = new ArrayList<MembersVo>();
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("roles._id").is(roleid));
        query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
        List<UserAccount> accounts = mongoTemplate.find(query, UserAccount.class);
        if (accounts != null && accounts.size() > 0) {
            for (int i = 0; i < accounts.size(); i++) {
                Query newQuery = new Query();
                newQuery.addCriteria(Criteria.where("userid").is(accounts.get(i).get_id()));
                newQuery.addCriteria(Criteria.where("type").is("1"));
                newQuery.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
                CustomerInfo customerInfo = mongoTemplate.findOne(newQuery, CustomerInfo.class);
                if (customerInfo != null) {
                    CustomerInfo.UserInfo userInfo = customerInfo.getUserinfo();
                    if (userInfo != null) {
                        MembersVo membersVo = new MembersVo();
                        membersVo.set_id(customerInfo.get_id());
                        membersVo.setCustomername(userInfo.getCustomername());
                        membersVo.setMobilenumber(userInfo.getMobilenumber());
                        membersVo.setDepartments(userInfo.getDepartmentid());
                        result.add(membersVo);
                    }
                }
            }
        }
        return result;
    }
}
