package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Course;
import com.nt.service_BASF.CourseServices;
import com.nt.utils.*;
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
 * @ClassName: BASF21201Controller
 * @Author: WXL
 * @Description: BASF培训课程管理模块Controller
 * @Date: 2019/11/20 15：39
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21202")
public class BASF21202Controller {

    @Autowired
    private CourseServices courseServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取培训课程列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16：24
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(courseServices.list());
    }

    /**
     * @param course
     * @param request
     * @Method create
     * @Author Wxz
     * @Version 1.0
     * @Description 创建培训课程
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16:24
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Course course, HttpServletRequest request) throws Exception {
        if (course == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        courseServices.insert(course, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param course
     * @param request
     * @Method delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除课程
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16：25
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Course course, HttpServletRequest request) throws Exception {
        if (course == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        course.setStatus(AuthConstants.DEL_FLAG_DELETE);
        courseServices.delete(course);
        return ApiResult.success();
    }

    /**
     * @param courseid
     * @param request
     * @Method selectById
     * @Author WXL
     * @Version 1.0
     * @Description 获取课程详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16:26
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String courseid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(courseid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(courseServices.one(courseid));
    }

    /**
     * @param course
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新课程详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16:26
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Course course, HttpServletRequest request) throws Exception {
        if (course == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        courseServices.update(course, tokenModel);
        return ApiResult.success();
    }
}
