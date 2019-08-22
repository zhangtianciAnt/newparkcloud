package com.nt.controller.Controller;


import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.ToDoNotice.Notices;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
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

@RestController
@RequestMapping("/ToDoNotice")
public class ToDoNoticeController {

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private TokenService tokenService;

    /**
     * @方法名：save
     * @描述：保存消息
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ApiResult save(@RequestBody ToDoNotice toDoNotice, HttpServletRequest request) throws Exception {
        if (toDoNotice == null || StringUtils.isEmpty(toDoNotice)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        if(toDoNotice.getType().equals(AuthConstants.TODONOTICE_TYPE_TODO)){
            for(ToDoNotice.ToDoInfos item :toDoNotice.getToDoInfos()){
                item.setInitiator(tokenModel.getUserId());
                item.setLaunchtime(new Date());
            }
        }
        toDoNotice.preInsert(tokenModel);
        toDoNoticeService.save(toDoNotice);
        return ApiResult.success();
    }

    /**
     * @方法名：getmessage
     * @描述：获取消息列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[ request]
     * @返回值：toDoNotice
     */
    @RequestMapping(value = "/getmessage", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        ToDoNotice message = new ToDoNotice();
        TokenModel tokenModel = tokenService.getToken(request);
        message.setTenantid(tokenModel.getTenantId());
        message.setOwners(tokenModel.getOwnerList());
        message.setIds(tokenModel.getIdList());
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
    public ApiResult updatenoticesstatus(@RequestBody ToDoNotice toDoNotice, HttpServletRequest request) throws Exception {
        if (toDoNotice == null || StringUtils.isEmpty(toDoNotice)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03));
        }
//        TokenModel tokenModel = tokenService.getToken(request);
//        toDoNotice.preInsert(tokenModel);
        toDoNoticeService.updateNoticesStatus(toDoNotice);
        return ApiResult.success();
    }
}
