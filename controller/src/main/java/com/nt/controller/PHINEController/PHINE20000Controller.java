package com.nt.controller.PHINEController;

import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/PHINE20000")
public class PHINE20000Controller {
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getFileByVersion", method = {RequestMethod.GET})
    public ApiResult getFileByVersion(HttpServletRequest request, @RequestParam String version) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return null;
    }
}
