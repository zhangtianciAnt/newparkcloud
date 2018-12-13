package com.nt.service_Auth.Impl;

import cn.hutool.core.util.StrUtil;
import com.mongodb.BasicDBObject;
import com.nt.dao_Auth.AppPermission;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private MongoTemplate mongoTemplate;

    //获取ownerlist
    @Override
    public List<String> getOwnerList(String url, String useraccountid) throws Exception {
        List<String> result = new ArrayList<String>();
        List<String> roleIds = new ArrayList<String>();
        String actionId = "";
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(useraccountid));
        UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
        List<Role> roles = account.getRoles();
        if (roles != null) {
            for (int i = 0; i < roles.size(); i++) {
                roleIds.add(roles.get(i).get_id());
            }
        }
        //根据条件检索数据
        Query newquery = new Query();
        newquery.addCriteria(Criteria.where("_id").in(roleIds));
        newquery.addCriteria(Criteria.where("menus.menuurl").is(url));
        List<Role> list = mongoTemplate.find(newquery, Role.class);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                List<AppPermission.menu> menus = list.get(i).getMenus();
                var menu = menus.stream()
                        .filter(info -> info.getMenuurl().equals(url))
                        .collect(Collectors.toList());
                if (menu != null && menu.size() > 0) {
                    var action = menu.get(0).getActions().stream()
                            .filter(item -> item.getActiontype().equals("0"))
                            .collect(Collectors.toList());
                    if (action != null && action.size() > 0) {
                        actionId = action.get(0).get_id();
                    }
                }
                List<AppPermission.menu.actions> actions = list.get(i).getActions();
                if (!StrUtil.isEmpty(actionId)) {
                    String finalActionId = actionId;
                    var action = actions.stream()
                            .filter(item -> item.get_id().equals(finalActionId))
                            .collect(Collectors.toList());
                    if (action != null && action.size() > 0) {
                        int auth = action.get(0).getAuth();
                        if (auth == -1) {
                            return result;
                        }
                        continue;
                    }
                }
            }
            result.add(useraccountid);
            return result;
        }
        result.add("XXXXX");
        return result;
    }

    //获取ownerlist
//    @Override
//    public List<String> getOwnerList(String url, String useraccountid) throws Exception {
//        List<String> result = new ArrayList<String>();
//        List<String> roleIds = new ArrayList<String>();
//        //根据条件检索数据
//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").is(useraccountid));
//        UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
//        List<Role> roles = account.getRoles();
//        if (roles != null) {
//            for (int i = 0; i < roles.size(); i++) {
//                roleIds.add(roles.get(i).get_id());
//            }
//        }
//        //根据条件检索数据
//        Query newquery = new Query();
//        newquery.addCriteria(Criteria.where("_id").in(roleIds));
//        newquery.addCriteria(Criteria.where("apps.menus.children.menuurl").is(url));
//        List<Role> list = mongoTemplate.find(newquery, Role.class);
//        if (list != null) {
//            Label_out:
//            for (int i = 0; i < list.size(); i++) {
//                List<AppPermission> apps = list.get(i).getApps();
//                if (apps != null) {
//                    for (int j = 0; j < apps.size(); j++) {
//                        var menus = apps.get(j).getMenus();
//                        for (int k = 0; k < menus.size(); k++) {
//                            var children = menus.get(k).getChildren();
//                            var menu = children.stream()
//                                        .filter(info -> info.getMenuurl().equals(url))
//                                        .collect(Collectors.toList());
//                            if (menu != null && menu.size() > 0) {
//                                var action = menu.get(0).getActions().stream()
//                                            .filter(item -> item.getActiontype().equals("0"))
//                                            .collect(Collectors.toList());
//                                if (action != null && action.size() > 0) {
//                                    int auth = action.get(0).getAuth();
//                                    if (auth == -1) {
//                                        return result;
//                                    }
//                                    continue Label_out;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            result.add(useraccountid);
//            return result;
//        }
//        result.add("XXXXX");
//        return result;
//    }

}
