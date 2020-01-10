package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.MapBox_MapLevel;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Autowired
    private TokenService tokenService;
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

    @GetMapping("/list")
    public ApiResult list(){
        return  ApiResult.success(mapBox_mapLevelServices.list());
    }
    @PostMapping("/add")
    public ApiResult  add(@RequestBody MapBox_MapLevel info,HttpServletRequest request) throws Exception{
        if(info==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        mapBox_mapLevelServices.add(info,tokenModel);
        return ApiResult.success();
    }
    @PostMapping("/edit")
    public ApiResult edit(@RequestBody MapBox_MapLevel info, HttpServletRequest request) throws Exception{
        if(info==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        mapBox_mapLevelServices.edit(info,tokenModel);
        return ApiResult.success();
}
    @PostMapping("/del")
    public ApiResult delete(@RequestBody MapBox_MapLevel mapBox_mapLevel, HttpServletRequest request) throws Exception{
        if(mapBox_mapLevel == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        mapBox_mapLevelServices.delete(mapBox_mapLevel,tokenModel);
        return ApiResult.success();
    }


}
