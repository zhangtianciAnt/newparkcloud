package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.BASFImage;
import com.nt.service_BASF.BASFImageService;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10102Controller
 * @Author: LXY
 * @Description: 首页Controller
 * @Date: 2019/11/6 22.40
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10901")
public class BASF10901Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BASFImageService imageService;

    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult get(@RequestBody BASFImage image) throws Exception {
        return ApiResult.success(imageService.get(image));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody BASFImage image, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        imageService.insert(tokenModel, image);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody BASFImage image, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        imageService.update(tokenModel, image);
        return ApiResult.success();
    }

    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody BASFImage image, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        image.setStatus(AuthConstants.DEL_FLAG_DELETE);
        imageService.del(tokenModel, image);
        return ApiResult.success();
    }
}
