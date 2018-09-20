package com.nt.newparkcloud.controller;


import com.nt.newparkcloud.dao.dao_demo.User;
import com.nt.newparkcloud.services_demo.UserService;
import com.nt.newparkcloud.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/demo")
public class demoController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save", method = {RequestMethod.GET})
    public ApiResult save() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUserName("小明");
        user.setPassWord("fffooo123");
        user.setUppassWord("112332132");
        userService.save(user);
        return ApiResult.success();
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get() throws Exception {
        User user = new User();
        user.setUserName("小明");
        return ApiResult.success(userService.get(user));
    }
}
