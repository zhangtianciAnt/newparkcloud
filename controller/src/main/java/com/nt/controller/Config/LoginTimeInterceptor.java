package com.nt.controller.Config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nt.service_Auth.AuthService;
import com.nt.utils.ApiCode;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LoginTimeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthService authService;

    //在控制器执行前调用
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(AuthConstants.AUTH_TOKEN);
        String url = request.getHeader(AuthConstants.CURRENTURL);
        try {
            if (StrUtil.isNotBlank(token)) {
                // 验证token
                if (!tokenService.validToken(request)) {
                    // 验证token失败，则返回直接返回用户未登录错误
                    errorResponse(response, ApiResult.fail(ApiCode.USER_NOT_LOGIN));
                    return false;

                }
            } else {
                // 验证token失败，则返回直接返回用户未登录错误
                errorResponse(response, ApiResult.fail(ApiCode.USER_NOT_LOGIN));
                return false;
            }

        } catch (Exception e) {
            errorResponse(response, ApiResult.fail(ApiCode.USER_NOT_LOGIN));
            return false;
        }

        TokenModel tokenModel = tokenService.getToken(request);
        //获取ownerlist
        if (!StrUtil.isEmpty(url)) {
            List<String> ownerList = getOwnerList(url, tokenModel);
            tokenModel.setOwnerList(ownerList);
        }
        tokenService.setToken(tokenModel);
        return true;
    }

    /**
     * 错误输出
     *
     * @param response
     * @param apiResult
     */
    private void errorResponse(HttpServletResponse response, ApiResult apiResult) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(JSONUtil.parse(apiResult).toString());
        out.flush();
        out.close();
    }

    /**
     * @方法名：getOwnerList
     * @描述：获取ownerlist
     * @创建日期：2018/12/12
     * @作者：WENCHAO
     * @参数：[url, userid]
     * @返回值：java.util.List<java.lang.String>
     */
    private List<String> getOwnerList(String url, TokenModel tokenModel) throws Exception {
        List<String> ownerList = new ArrayList<String>();
        ownerList = authService.getOwnerList(url, tokenModel.getUserId());
        return ownerList;
    }

}
