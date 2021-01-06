package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Chathistory;
import com.nt.dao_BASF.Webchatuserinfo;
import com.nt.service_BASF.WebchatuserinfoServices;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF21212Controller
 * @Author: 齐铎
 * @Description: 即将到期人员Controller
 * @Date: 2020/3/18 17:41
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF30000")
public class BASF30000Controller {

    @Autowired
    private WebchatuserinfoServices webchatuserinfoServices;

    // 聊天室用户认证
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody Webchatuserinfo webchatuserinfo, HttpServletRequest request) throws Exception {
        if (webchatuserinfo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return webchatuserinfoServices.checkPwd(webchatuserinfo);
    }

    // 保存聊天记录
    @RequestMapping(value = "/saveChatHistory", method = {RequestMethod.POST})
    public ApiResult saveChatHistory(@RequestBody Chathistory chathistory, HttpServletRequest request) throws Exception {
        if (chathistory == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return webchatuserinfoServices.saveChatHistory(chathistory);
    }

    // 获取聊天记录
    @RequestMapping(value = "/getChatHistory", method = {RequestMethod.GET})
    public ApiResult getChatHistory(@RequestParam String from, @RequestParam String to, HttpServletRequest request) throws Exception {
        return webchatuserinfoServices.getChatHistory(from, to);
    }

    // 聊天室用户修改密码
//    @PostMapping("/exportData")
//    public ApiResult exportData(HttpServletRequest request) throws Exception{
//        return ApiResult.success(trainjoinlistServices.exportData());
//    }
}
