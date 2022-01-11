package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.dao_Pfans.PFANS1000.Vo.RevenueForecastVo;
import com.nt.service_pfans.PFANS1000.RevenueForecastService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TableDataInfo;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Theme别收入见通(RevenueForecast)表控制层
 *
 * @author makejava
 * @since 2021-11-18 14:58:25
 */
@RestController
@RequestMapping("revenueForecast")
public class Pfans1053Controller {

    @Autowired
    private RevenueForecastService revenueForecastService;

    @Autowired
    private TokenService tokenService;


    /**
     * 保存信息
     *
     * @param revenueForecastVo
     * @param request           请求
     * @return {@link ApiResult}
     * @throws Exception 异常
     */
    @RequestMapping(value = "/saveInfo",method={RequestMethod.POST})
    public ApiResult saveInfo(@RequestBody RevenueForecastVo revenueForecastVo, HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);
        revenueForecastService.saveInfo(revenueForecastVo,tokenModel);
        return ApiResult.success();
    }


    /**
     * 得到信息
     *
     * @param revenueForecast 收入预测
     * @param request         请求
     * @return {@link ApiResult}
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getInfo",method={RequestMethod.POST})
    public ApiResult getInfo(@RequestBody RevenueForecast revenueForecast, HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);
        List<RevenueForecast> revenueForecastList =  revenueForecastService.selectInfo(revenueForecast);
        return ApiResult.success(revenueForecastList);
    }


    /**
     * 获取剩余theme（部门条件筛选以外的theme）
     *
     * @param request 请求
     * @return {@link ApiResult}
     * @throws Exception 异常
     */
    @RequestMapping(value = "/getThemeOutDepth",method={RequestMethod.POST})
    public ApiResult getThemeOutDepth(@RequestBody RevenueForecastVo revenueForecastVo, HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);

        TableDataInfo themeOutDepth = revenueForecastService.getThemeOutDepth(revenueForecastVo.getRevenueForecast(), revenueForecastVo.getCurrentPage(), revenueForecastVo.getPageSize());
        return ApiResult.success(themeOutDepth);
    }

}

