package com.nt.controller.Controller;

import com.nt.dao_Org.ToDoNotice;
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

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ApiResult save(@RequestBody ToDoNotice toDoNotice, HttpServletRequest request) throws Exception {
        if (toDoNotice == null || StringUtils.isEmpty(toDoNotice)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
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
}
