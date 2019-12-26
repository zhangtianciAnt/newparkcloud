package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: MapBox_MapLevelController
 * @Author: WXZ
 * @Description: MapBox_MapLevelController
 * @Date: 2019/12/17 10:58
 * @Version: 1.0
 */
@RestController
@RequestMapping("/mapboxmaplevel")
public class MapBox_MapLevelController {

    @Autowired
    private MapBox_MapLevelServices mapBox_mapLevelServices;

    /**
     * @param mapid
     * @param request
     * @Method selectById
     * @Author Wxz
     * @Version 1.0
     * @Description 根据id查询详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/17 11：01
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String mapid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(mapid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(mapBox_mapLevelServices.one(mapid));
    }

    @RequestMapping(value = "/getall", method = {RequestMethod.GET})
    public ApiResult getAll(HttpServletRequest request) throws Exception {
        return ApiResult.success(mapBox_mapLevelServices.getall());
    }
}
