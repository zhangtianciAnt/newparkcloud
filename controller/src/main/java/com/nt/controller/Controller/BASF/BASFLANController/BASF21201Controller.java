package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.QuestionManage;
import com.nt.service_BASF.QuestionManageServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21201Controller
 * @Author: DTSX
 * @Description: BASF21201Controller
 * @Date: 2019/11/20 18:56
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21201")
public class BASF21201Controller {

    @Autowired
    private QuestionManageServices questionManageServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取题库列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 18:58
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(questionManageServices.list());
    }

    /**
     * @param questionManage
     * @param request
     * @Method create
     * @Author Wxz
     * @Version 1.0
     * @Description 创建试题
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 9：20
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody QuestionManage questionManage, HttpServletRequest request) throws Exception {
        if (questionManage == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(questionManageServices.insert(questionManage, tokenModel));
    }

    @RequestMapping(value = "/createList", method = {RequestMethod.POST})
    public ApiResult createList(@RequestBody List<QuestionManage> questionManageList, HttpServletRequest request) throws Exception {
        if (questionManageList == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        questionManageServices.insertList(questionManageList, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param questionManage
     * @param request
     * @Method delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除试题
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 9:40
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody QuestionManage questionManage, HttpServletRequest request) throws Exception {
        if (questionManage == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        questionManage.setStatus(AuthConstants.DEL_FLAG_DELETE);
        questionManageServices.delete(questionManage);
        return ApiResult.success();
    }

    /**
     * @param questionid
     * @param request
     * @Method selectById
     * @Author Wxz
     * @Version 1.0
     * @Description 获取试题详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 9:46
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String questionid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(questionid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(questionManageServices.one(questionid));
    }

    /**
     * @param questionManage
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新试题详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/21 9：50
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody QuestionManage questionManage, HttpServletRequest request) throws Exception {
        if (questionManage == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        questionManageServices.update(questionManage, tokenModel);
        return ApiResult.success();
    }

    /**
     * @ProjectName: BASF应急平台
     * @Package: com.nt.controller.Controller.BASF.BASFLANController
     * @ClassName: BASF21201
     * @Author: WXZ
     * @Description: EXECL导入
     * @Date: 2019/12/9 11:34
     * @Version: 1.0
     */
    @RequestMapping(value = "/importUser", method = {RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(questionManageServices.eximport(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
