package com.nt.controller.Config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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

public class LoginTimeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenService tokenService;


    //在控制器执行前调用
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(AuthConstants.AUTH_TOKEN);
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

}
