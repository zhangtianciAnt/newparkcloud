package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.dao_Pfans.PFANS4000.SealDetail;
import com.nt.service_pfans.PFANS4000.SealService;
import com.nt.service_pfans.PFANS4000.mapper.SealDetailMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/seal")
public class Pfans4001Controller {

    @Autowired
    private SealService sealService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SealDetailMapper sealdetailmapper;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);

        Seal seal = new Seal();
        List<SealDetail> sealdetaillist = sealdetailmapper.selectAll();
        if (sealdetaillist.size() > 0) {
            if (!tokenModel.getUserId().equals(sealdetaillist.get(0).getSealdetailname())) {
                seal.setOwners(tokenModel.getOwnerList());
            }
        }else{
            seal.setOwners(tokenModel.getOwnerList());
        }
        return ApiResult.success(sealService.list(seal));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.insert(seal, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/createbook", method = {RequestMethod.POST})
    public ApiResult createbook(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(sealService.createbook(seal, tokenModel));
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.upd(seal, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(sealService.One(seal.getSealid()));
    }
    //add-ws-12/21-印章盖印
    @RequestMapping(value = "/insertrecognition", method = {RequestMethod.POST})
    public ApiResult insertrecognition(@RequestBody Seal seal, HttpServletRequest request) throws Exception {
        if (seal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.insertrecognition(seal.getSealid(),tokenModel);
        return ApiResult.success();
    }
    @GetMapping("/insertnamedialog")
    public ApiResult insertnamedialog(String sealdetailname, String sealdetaildate, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        sealService.insertnamedialog(sealdetailname,sealdetaildate,tokenModel);
        return ApiResult.success();
    }
    //add-ws-12/21-印章盖印
}
