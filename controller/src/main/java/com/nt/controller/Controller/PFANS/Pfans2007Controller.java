package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS2000.BonussendService;
import com.nt.utils.*;
import com.nt.dao_Org.ToDoNotice;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bonussend")
public class Pfans2007Controller {

    @Autowired
    private BonussendService bonussendService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        if ( "0".equals(type) ) {
            templateName = "jiangjinfasong.xlsx";
            fileName = "奖金发送导入模板";
        }
        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody List<Bonussend> bonussend, HttpServletRequest request) throws Exception{
        if (bonussend == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        bonussendService.update(bonussend,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/inserttodo",method = {RequestMethod.POST})
    // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 start
    public ApiResult inserttodo(@RequestBody List<Bonussend> bonussend, HttpServletRequest request) throws Exception{
        if (bonussend == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
//        TokenModel tokenModel = tokenService.getToken(request);
//        toDoNotice.preInsert(tokenModel);
//        toDoNotice.setOwner(tokenModel.getUserId());
//        toDoNoticeService.save(toDoNotice);
//        bonussendService.updateSend(toDoNotice.getDataid());
        TokenModel tokenModel = tokenService.getToken(request);
        bonussendService.updateSend(bonussend,tokenModel);
        // update gbb 20210312 NT_PFANS_20210305_BUG_131 点击送信发送代办 end
        return ApiResult.success();

    }

    @RequestMapping(value = "/importUser",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(bonussendService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/List", method = {RequestMethod.POST})
    public ApiResult List(HttpServletRequest request,@RequestBody Bonussend bonussend ) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        bonussend.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(bonussendService.List(bonussend,tokenModel));
    }

}
