package com.nt.controller.Controller;
import com.nt.dao_Org.Vo.UserAccountVo;
import com.nt.service_Org.UserAccountVoService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/PersonalCenter")
public class UserAccountVoController {
    @Autowired
    private UserAccountVoService userAccountVoService;
    @Autowired
    private TokenService tokenService;
    /**
     * @方法名：changePassword
     * @描述：修改密码的判断
     * @创建日期：2018/11/05
     * @作者：WANGSHUAI
     * @参数：[userAccountVo]
     * @返回值：com.nt.utils.ApiResult
     */
    //修改密码的判断
    @RequestMapping(value = "/changePassword",method={RequestMethod.POST})
    public ApiResult changePassword(@RequestBody UserAccountVo userAccountVo, HttpServletRequest request) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            String userId = tokenModel.getUserId();
            userAccountVo.setUserid(userId);
            userAccountVoService.confirmPassword(userAccountVo);
            return ApiResult.success();
        } catch (LogicalException ex){
            return ApiResult.fail(ex.getMessage());
        }
    }
}



