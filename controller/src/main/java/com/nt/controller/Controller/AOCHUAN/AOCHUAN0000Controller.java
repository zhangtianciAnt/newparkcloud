package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN0000.WorkPlan;
import com.nt.service_AOCHUAN.AOCHUAN0000.WorkPlanService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/index")
public class AOCHUAN0000Controller {

    @Autowired
    private WorkPlanService workPlanService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getWorkPlanList",method={RequestMethod.POST})
    public ApiResult getWorkPlanList(HttpServletRequest request) throws Exception {
        return ApiResult.success(workPlanService.getWorkPlanList());
    }

    /**
     * 新建
     * @param workPlan
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody WorkPlan workPlan, HttpServletRequest request) throws Exception {
        if(workPlan == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        workPlan.preInsert(tokenService.getToken(request));

        if(!workPlanService.insert(workPlan)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success();
    }

    /**
     * 更新
     * @param workPlan
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody WorkPlan workPlan, HttpServletRequest request) throws Exception {
        if(workPlan == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        workPlan.preUpdate(tokenService.getToken(request));

        if(!workPlanService.update(workPlan)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success();
    }

    /**
     * 删除
     * @param workPlan
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/del",method={RequestMethod.POST})
    public ApiResult del(@RequestBody WorkPlan workPlan, HttpServletRequest request) throws Exception {
        if(workPlan == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        workPlan.preUpdate(tokenService.getToken(request));

        if(!workPlanService.del(workPlan)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success();
    }
}
