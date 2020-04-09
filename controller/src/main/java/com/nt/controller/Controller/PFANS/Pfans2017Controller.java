package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.PunchcardRecord;
import com.nt.dao_Pfans.PFANS2000.PunchcardRecordDetail;
import com.nt.service_pfans.PFANS2000.PunchcardRecordService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/punchcardrecord")
public class Pfans2017Controller {

    @Autowired
    private PunchcardRecordService punchcardrecordService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getDataList", method = {RequestMethod.POST})
    public ApiResult getDataList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PunchcardRecord punchcardrecord = new PunchcardRecord();
        return ApiResult.success(punchcardrecordService.getDataList(punchcardrecord,tokenModel));
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PunchcardRecord punchcardrecord = new PunchcardRecord();
        punchcardrecord.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(punchcardrecordService.list(punchcardrecord,tokenModel));
    }

    @RequestMapping(value = "/list1", method = {RequestMethod.POST})
    public ApiResult list1(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PunchcardRecord punchcardrecord = new PunchcardRecord();
        punchcardrecord.setOwner(tokenModel.getUserId());
        return ApiResult.success(punchcardrecordService.list(punchcardrecord,tokenModel));
    }
    @RequestMapping(value = "/importUser",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(punchcardrecordService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/getPunDetail", method = {RequestMethod.POST})
    public ApiResult getPunDetail(@RequestBody PunchcardRecordDetail detail, HttpServletRequest request) throws Exception {
        return ApiResult.success(punchcardrecordService.getPunDetail(detail));
    }
}
