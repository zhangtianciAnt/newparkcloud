package com.nt.service_Auth.Impl;

import cn.hutool.core.util.StrUtil;
import com.mongodb.BasicDBObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
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
        //登录人所在组织的所有人GBB
        List<String> resultTeam = new ArrayList<String>();
        String flg = "0";
        //根据人员查询所在组织的所有人人
        Query cusquery = new Query();
        cusquery.addCriteria(Criteria.where("userid").is(useraccountid));
        CustomerInfo cus = mongoTemplate.findOne(cusquery, CustomerInfo.class);
        List<CustomerInfo> cuslist = new ArrayList<CustomerInfo>();
        String teamid = cus.getUserinfo().getTeamid();
        String groupid = cus.getUserinfo().getGroupid();
        String centerid = cus.getUserinfo().getCenterid();
        if(!StringUtils.isNullOrEmpty(teamid)){
            Query cusqueryteamid = new Query();
            cusqueryteamid.addCriteria(Criteria.where("userinfo.teamid").is(teamid));
            cuslist = mongoTemplate.find(cusqueryteamid, CustomerInfo.class);
        }
        else if(!StringUtils.isNullOrEmpty(groupid)){
            Query cusquerygroupid = new Query();
            cusquerygroupid.addCriteria(Criteria.where("userinfo.groupid").is(groupid));
            cuslist = mongoTemplate.find(cusquerygroupid, CustomerInfo.class);
        }
        else if(!StringUtils.isNullOrEmpty(centerid)){
            Query cusquerycenterid = new Query();
            cusquerycenterid.addCriteria(Criteria.where("userinfo.centerid").is(centerid));
            cuslist = mongoTemplate.find(cusquerycenterid, CustomerInfo.class);
        }
        if(cuslist.size() > 0){
            for(CustomerInfo cusinfo : cuslist){
                resultTeam.add(cusinfo.getUserid());
            }
        }
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
                        if (auth == 5) {
                            flg = "1";
                            return resultTeam;
                        }
                        continue;
                    }
                }
            }
            if(flg.equals("1")){
                result = resultTeam;
            }
            else{
                result.add(useraccountid);
            }
            return result;
        }
        result.add("XXXXX");
        return result;
    }

    //获取新建按钮权限
    @Override
    public Boolean getNewActionAuth(String url, String useraccountid) throws Exception {
        boolean disabledNew = true;
        List<String> roleIds = new ArrayList<String>();
        String newId = "";
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
                List<AppPermission.menu.actions> actions = list.get(i).getActions();
                var menu = menus.stream()
                        .filter(info -> info.getMenuurl().equals(url))
                        .collect(Collectors.toList());
                if (menu != null && menu.size() > 0) {
                    //新建按钮id
                    var newAction = menu.get(0).getActions().stream()
                            .filter(item -> item.getActiontype().equals("new"))
                            .collect(Collectors.toList());
                    if (newAction != null && newAction.size() > 0) {
                        newId = newAction.get(0).get_id();
                    }
                    //新建按钮权限
                    if (!StrUtil.isEmpty(newId)) {
                        String finalActionId = newId;
                        var action = actions.stream()
                                .filter(item -> item.get_id().equals(finalActionId))
                                .collect(Collectors.toList());
                        if (action != null && action.size() > 0) {
                            disabledNew = false;
                            break;
                        }
                    }
                }
            }
        }
        return disabledNew;
    }


    //获取按钮权限（新建，编辑，删除）
    @Override
    public List<Boolean> getActionsAuth(String url, String useraccountid, String ownerid) throws Exception {
        List<Boolean> result = new ArrayList<Boolean>();
        boolean disabledNew = true;
        boolean disablededit = true;
        boolean disableddel = true;
        List<String> roleIds = new ArrayList<String>();
        String newId = "";
        String editId = "";
        String delId = "";
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
                List<AppPermission.menu.actions> actions = list.get(i).getActions();
                var menu = menus.stream()
                        .filter(info -> info.getMenuurl().equals(url))
                        .collect(Collectors.toList());
                if (menu != null && menu.size() > 0) {
                    if (disabledNew) {
                        //新建按钮id
                        var newAction = menu.get(0).getActions().stream()
                                .filter(item -> item.getActiontype().equals("new"))
                                .collect(Collectors.toList());
                        if (newAction != null && newAction.size() > 0) {
                            newId = newAction.get(0).get_id();
                        }
                        //新建按钮权限
                        if (!StrUtil.isEmpty(newId)) {
                            String finalActionId = newId;
                            var action = actions.stream()
                                    .filter(item -> item.get_id().equals(finalActionId))
                                    .collect(Collectors.toList());
                            if (action != null && action.size() > 0) {
                                disabledNew = false;
//                                int auth = action.get(0).getAuth();
//                                if (auth == 4) {
//                                    if (useraccountid.equals(ownerid)) {
//                                        disabledNew = false;
//                                    }
//                                }else if(auth == -1){
//                                    disabledNew = false;
//                                }
                            }
                        }
                    }
                    if (disablededit) {
                        //编辑按钮id
                        var editAction = menu.get(0).getActions().stream()
                                .filter(item -> item.getActiontype().equals("edit"))
                                .collect(Collectors.toList());
                        if (editAction != null && editAction.size() > 0) {
                            editId = editAction.get(0).get_id();
                        }
                        //编辑按钮权限
                        if (!StrUtil.isEmpty(editId)) {
                            String edtiActionId = editId;
                            var action = actions.stream()
                                    .filter(item -> item.get_id().equals(edtiActionId))
                                    .collect(Collectors.toList());
                            if (action != null && action.size() > 0) {
                                int auth = action.get(0).getAuth();
                                if (auth == 4) {
                                    if (useraccountid.equals(ownerid)) {
                                        disablededit = false;
                                    }
                                }else if(auth == -1){
                                    disablededit = false;
                                }
                            }
                        }
                    }
                    if (disableddel) {
                        //删除按钮id
                        var delAtion = menu.get(0).getActions().stream()
                                .filter(item -> item.getActiontype().equals("del"))
                                .collect(Collectors.toList());
                        if (delAtion != null && delAtion.size() > 0) {
                            delId = delAtion.get(0).get_id();
                        }
                        //删除按钮权限
                        if (!StrUtil.isEmpty(delId)) {
                            String delActionId = delId;
                            var action = actions.stream()
                                    .filter(item -> item.get_id().equals(delActionId))
                                    .collect(Collectors.toList());
                            if (action != null && action.size() > 0) {
                                int auth = action.get(0).getAuth();
                                if (auth == 4) {
                                    if (useraccountid.equals(ownerid)) {
                                        disableddel = false;
                                    }
                                }else if(auth == -1){
                                    disableddel = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        result.add(disabledNew);
        result.add(disablededit);
        result.add(disableddel);
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
