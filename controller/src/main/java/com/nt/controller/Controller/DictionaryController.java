package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.service_Org.DictionaryService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getForSelect",method={RequestMethod.GET})
    public ApiResult getForSelect(String code, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(code)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        return ApiResult.success(dictionaryService.getForSelect(code));
    }

    @RequestMapping(value = "/getForvalue2",method={RequestMethod.GET})
    public ApiResult getForvalue2(String value2, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(value2)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        return ApiResult.success(dictionaryService.getForvalue2(value2));
    }

    @RequestMapping(value = "/all",method={RequestMethod.GET})
    public ApiResult all(HttpServletRequest request) throws Exception {
        return ApiResult.success(dictionaryService.getForSelect(""));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Dictionary dictionary, HttpServletRequest request) throws Exception {
        if (dictionary == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        dictionaryService.updateDictionary(dictionary, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getDictionary",method={RequestMethod.POST})
    public ApiResult getDictionary(@RequestBody Dictionary dictionary, HttpServletRequest request) throws Exception {
        if (dictionary == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(dictionaryService.getDictionary(dictionary));
    }

    @RequestMapping(value = "/upDictionary",method={RequestMethod.POST})
    public ApiResult upDictionary(@RequestBody List<Dictionary> dictionarylist, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        dictionaryService.upDictionary(dictionarylist, tokenModel);
        return ApiResult.success();
    }
}
