package com.nt.controller.Controller;



import com.nt.dao_Org.Org;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.User;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Org.UserService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
        user.setPassWord("fffooo121113");
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

    @RequestMapping(value = "/up", method = {RequestMethod.GET})
    public ApiResult up() throws Exception {
        User user = new User();
        user.setUserName("小明up222222");
        userService.up(user);
        return ApiResult.success();
    }
}
