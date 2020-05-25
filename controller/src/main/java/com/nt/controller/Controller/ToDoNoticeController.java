package com.nt.controller.Controller;


import com.nt.dao_Org.ToDoNotice;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ToDoNotice")
public class ToDoNoticeController {

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        ToDoNotice todonotice = new ToDoNotice();
        todonotice.setOwner(tokenModel.getUserId());
        return ApiResult.success(toDoNoticeService.list(todonotice));
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.GET})
    public ApiResult getList(String status, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        List<ToDoNotice> rst = toDoNoticeService.getDataList(status);
        if (rst != null) {
            rst = rst.stream().filter(item -> (item.getOwner().equals(tokenModel.getUserId()))).collect(Collectors.toList());
        }
        return ApiResult.success(rst);
    }

    /**
     * @方法名：getmessage
     * @描述：获取消息列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[ request]
     * @返回值：toDoNotice
     */
    @RequestMapping(value = "/getmessage", method = {RequestMethod.POST})
    public ApiResult get(HttpServletRequest request) throws Exception {
        ToDoNotice message = new ToDoNotice();
        TokenModel tokenModel = tokenService.getToken(request);
        message.setTenantid(tokenModel.getTenantId());
        message.setOwners(tokenModel.getOwnerList());
        message.setIds(tokenModel.getIdList());
        message.setOwner(tokenModel.getUserId());
        return ApiResult.success(toDoNoticeService.get(message));
    }

    /**
     * @方法名：updatenoticesstatus
     * @描述：更新已阅
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/updatenoticesstatus", method = {RequestMethod.POST})
    public ApiResult updatenoticesstatus(@RequestBody List<ToDoNotice> toDoNoticelist, HttpServletRequest request) throws Exception {
        if (toDoNoticelist == null || StringUtils.isEmpty(toDoNoticelist)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        for (ToDoNotice toDoNotice : toDoNoticelist) {
            toDoNotice.preUpdate(tokenModel);
            toDoNoticeService.updateNoticesStatus(toDoNotice);
        }
        return ApiResult.success();
    }

    //    ADD_FJL_05/25  -- 删除驳回之后无用代办
    @RequestMapping(value = "/delToDoNotice", method = {RequestMethod.GET})
    public ApiResult delToDoNotice(String todonoticeid, HttpServletRequest request) throws Exception {
        if (todonoticeid == null || StringUtils.isEmpty(todonoticeid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        toDoNoticeService.delToDoNotice(todonoticeid);
        return ApiResult.success();
    }
    //    ADD_FJL_05/25  -- 删除驳回之后无用代办
}
