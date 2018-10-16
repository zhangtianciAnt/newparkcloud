package com.nt.controller.Controller;



import com.nt.dao_Org.Org;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.UserAccount;
import com.nt.service_Org.UserService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
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
