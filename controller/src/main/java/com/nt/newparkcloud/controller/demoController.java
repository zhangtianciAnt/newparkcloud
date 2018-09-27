package com.nt.newparkcloud.controller;


import com.nt.newparkcloud.dao.dao_demo.Org;
import com.nt.newparkcloud.dao.dao_demo.OrgTree;
import com.nt.newparkcloud.dao.dao_demo.UserAccount;
import com.nt.newparkcloud.services_demo.UserService;
import com.nt.newparkcloud.utils.ApiResult;
import com.nt.newparkcloud.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class demoController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save", method = {RequestMethod.GET})
    public ApiResult save(Org org) throws Exception {
        userService.save(org);
        return ApiResult.success();
    }

    @RequestMapping(value = "/savetree", method = {RequestMethod.POST})
    public ApiResult saveTree(@RequestBody OrgTree orgTree) throws Exception {
        userService.save(orgTree);
        return ApiResult.success();
    }

    @RequestMapping(value = "/saveuser", method = {RequestMethod.POST})
    public ApiResult saveUser(@RequestBody UserAccount userAccount) throws Exception {
        userService.save(userAccount);
        return ApiResult.success();
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get() throws Exception {
//        User user = new User();
//        user.setUserName("小明");
        System.out.println(MessageUtil.getMessage("1"));
        return ApiResult.success();
    }
}
