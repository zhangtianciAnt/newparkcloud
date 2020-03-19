package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Vo.LunarAllVo;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.service_pfans.PFANS2000.ExaminationobjectService;
import com.nt.service_pfans.PFANS2000.LunarbonusService;
import com.nt.service_pfans.PFANS2000.LunardetailService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/Getlunarbonus")
public class Pfans2027Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LunarbonusService lunarbonusService;

    @Autowired
    private LunardetailService lunardetailService;

    @Autowired
    private ExaminationobjectService examinationobjectService;

    @RequestMapping(value = "/getList", method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        wagesService.select(tokenModel);
        return ApiResult.success(lunarbonusService.getList());
    }
//考课对象List
    @RequestMapping(value = "/getExaminationobject", method = {RequestMethod.GET})
    public ApiResult getExaminationobject(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        wagesService.select(tokenModel);
        return ApiResult.success(examinationobjectService.getList());
    }

    //新建
    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody LunardetailVo lunardetailVo, HttpServletRequest request) throws Exception {
        if (lunardetailVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        lunarbonusService.insert(lunardetailVo,tokenModel);
        return ApiResult.success();
    }

    //编辑
    @RequestMapping(value = "/getLunardetail", method = {RequestMethod.POST})
    public ApiResult selectById(@RequestBody LunardetailVo lunardetailVo, HttpServletRequest request) throws Exception {
        if(lunardetailVo==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
//        Lunarbonus lunarbonus = new Lunarbonus();
//        lunarbonus.setLunarbonus_id(lunardetailVo.getLunarbonus().getLunarbonus_id());
//        lunardetailVo.setLunarbonus(lunarbonus);

        return ApiResult.success(lunardetailService.getLunardetail(lunardetailVo));
    }

    //获取详情状态
    @RequestMapping(value = "/getStatus", method = {RequestMethod.GET})
    public ApiResult selStatus(String ids, HttpServletRequest request) throws Exception {
        if(ids==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }

        return ApiResult.success(lunardetailService.getExam(ids));
    }


    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody LunarAllVo lunarAllVo, HttpServletRequest request) throws Exception{
        if (lunarAllVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        lunardetailService.update(lunarAllVo,tokenModel);
        return ApiResult.success();
    }

    //获取详情下拉列表初始
    @RequestMapping(value = "/getOne", method = {RequestMethod.GET})
    public ApiResult getOne(String lunarbonus_id, HttpServletRequest request) throws Exception {
        if(lunarbonus_id==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(lunarbonusService.getOne(lunarbonus_id));
    }



}
