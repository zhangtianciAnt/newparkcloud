package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Mapbox;
import com.nt.service_BASF.MapboxServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF90600Controller
 * @Author: SKIAXX
 * @Description: ERC大屏车辆定位模块接口
 * @Date: 2019/12/4 13:40
 * @Version: 1.0
 */
@RestController
@RequestMapping("/Mapbox")
public class MapboxController {
    @Autowired
    private MapboxServices mapboxServices;
    private TokenService tokenService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //获取信息
/*    @RequestMapping(value = "/MapLeveTree", method = {RequestMethod.GET})
    public ApiResult MapLeveTree(String id, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(mapboxServices.get(id));
    }*/
/*    @RequestMapping(value = "/MapLeveTree", method = {RequestMethod.GET})
    public ApiResult MapLeveTree(HttpServletRequest request) throws Exception {
        return ApiResult.success(mapboxServices.get());
    }*/

    @RequestMapping(value = "/MapLeveTree", method = {RequestMethod.POST})
    public ApiResult MapLeveTree(HttpServletRequest request) throws Exception {
        Mapbox mapbox = new Mapbox();
        return ApiResult.success(mapboxServices.get(mapbox));
    }
}
