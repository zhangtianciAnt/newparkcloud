package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/fincdrl")
public class AOCHUAN5004Controller {

    @Autowired
    private FinCrdlInfoService finCrdlInfoService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取凭证信息
     */
    @RequestMapping(value = "/getCdrlList", method = {RequestMethod.POST})
    public ApiResult getCdrlList(HttpServletRequest request) throws Exception {
        CredentialInformation credentialInformation = new CredentialInformation();
        return ApiResult.success(finCrdlInfoService.getCrdlInfo(credentialInformation));
    }

    /**
     * 获取凭证表
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getForm",method={RequestMethod.GET})
    public ApiResult getForm(@RequestParam String id, HttpServletRequest request) throws Exception {
        return ApiResult.success(finCrdlInfoService.getForm(id));
    }

    /**
     * 获取分录-辅助核算
     */
    @RequestMapping(value = "/getAccAuxList", method = {RequestMethod.POST})
    public ApiResult getAccAuxList(CredentialInformation credentialInformation, HttpServletRequest request) throws Exception {

        if(credentialInformation == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(finCrdlInfoService.getAcctgEntrInfoByCrdl_id(credentialInformation));
    }

    /**
     * 更新金蝶KIS
     */
    @RequestMapping(value = "/updateKis", method = {RequestMethod.POST})
    public ApiResult updateKis(CredentialInformation credentialInformation,HttpServletRequest request) throws Exception {

        if(credentialInformation == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        if (!finCrdlInfoService.updateKisCrdlNo(credentialInformation,tokenService.getToken(request))){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(CrdlInfo crdlInfo, HttpServletRequest request) throws Exception {

        if(crdlInfo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        if (!finCrdlInfoService.delete(crdlInfo,tokenService.getToken(request))){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success();
    }

}
