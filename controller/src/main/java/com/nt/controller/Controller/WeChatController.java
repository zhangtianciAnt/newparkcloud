package com.nt.controller.Controller;
import com.nt.service_Org.UserService;
import com.nt.utils.ApiResult;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.WxUserApi;
import com.nt.utils.dao.WeixinOauth2Token;
import com.nt.utils.dao.WxEnterpriseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.controller.Controller
 * @ClassName: UserController
 * @Description: 微信用户相关操作Controller
 * @Author: ZHANGYING
 * @CreateDate: 2018/12/14
 * @UpdateUser: ZHANGYING
 * @UpdateDate: 2018/12/14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@RestController
@RequestMapping("/weChat")
public class WeChatController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/getUser", method={RequestMethod.GET})
    public ApiResult getWeChatUser(String code) throws Exception{

        try {
            String corpid = "wxa7fe36d9731ebd03";
            String appSecret = "46420ae2ea2590bcf0004fd4d2e332b5";

            WeixinOauth2Token weixinOauth = WxUserApi.getWeChatOauth2Token(corpid, appSecret, code);
            if (weixinOauth != null && !StringUtils.isEmpty(weixinOauth.getOpenid())) {
                String openid = weixinOauth.getOpenid();
                return ApiResult.success(userService.wxLogin(openid));
            } else {
                return ApiResult.fail("获取微信openid失败");
            }
        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value="/getWeChatUserInfo", method={RequestMethod.GET})
    public ApiResult getWeChatUserInfo(String code) throws Exception{

        try {
            String corpid = "wxa7fe36d9731ebd03";
            String appSecret = "46420ae2ea2590bcf0004fd4d2e332b5";

            WeixinOauth2Token weixinOauth = WxUserApi.getWeChatOauth2Token(corpid, appSecret, code);
            if (weixinOauth != null && !StringUtils.isEmpty(weixinOauth.getOpenid())) {
                String openid = weixinOauth.getOpenid();
                return ApiResult.success(userService.getWxById(openid));
            } else {
                return ApiResult.fail("获取微信openid失败");
            }
        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
    }

}
