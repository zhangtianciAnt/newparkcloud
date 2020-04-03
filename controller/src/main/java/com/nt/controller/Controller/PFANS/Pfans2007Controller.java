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
    public ApiResult inserttodo(@RequestBody ToDoNotice toDoNotice, HttpServletRequest request) throws Exception{
        if (toDoNotice == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        toDoNotice.preInsert(tokenModel);
        toDoNotice.setOwner(tokenModel.getUserId());
        toDoNoticeService.save(toDoNotice);

        bonussendService.updateSend(toDoNotice.getDataid());

        return ApiResult.success();
//        // 创建代办
//        ToDoNotice toDoNotice = new ToDoNotice();
//        List<String> params = new ArrayList<String>();
//        params.add(workflowname);
//        toDoNotice.setTitle("您的奖金已发送");
//        toDoNotice.setInitiator(tokenModel.getUserId());
//        toDoNotice.setContent(item.getNodename());
//        toDoNotice.setDataid(dataId);
//        toDoNotice.setUrl(url);
//        toDoNotice.setWorkflowurl(workFlowurl);
//        toDoNotice.preInsert(tokenModel);
//        toDoNotice.setOwner(user);
//        toDoNoticeService.save(toDoNotice);

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
