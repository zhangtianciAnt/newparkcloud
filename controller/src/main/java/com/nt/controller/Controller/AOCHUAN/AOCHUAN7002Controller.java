package com.nt.controller.Controller.AOCHUAN;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Account;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN7000.AccountService;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.service_AOCHUAN.AOCHUAN7000.mapper.AccountMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/docurule")
public class AOCHUAN7002Controller {
    @Autowired
    private DocuruleService docuruleService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getList",method={RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Docurule docurule=new Docurule();
        return ApiResult.success(docuruleService.get(docurule));
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult One(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(docuruleService.One(id));
    }

    @RequestMapping(value = "/selectrule",method={RequestMethod.GET})
    public ApiResult selectrule(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(docuruleService.selectrule(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody DocuruleVo docuruleVo, HttpServletRequest request) throws Exception {
        if(docuruleVo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        docuruleService.update(docuruleVo,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult create(@RequestBody DocuruleVo docuruleVo, HttpServletRequest request) throws Exception {
        if(docuruleVo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        docuruleService.insert(docuruleVo,tokenService.getToken(request));
        return ApiResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody DocuruleVo deldocuruleVo, HttpServletRequest request) throws Exception {

        if(deldocuruleVo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        if (!docuruleService.delete(deldocuruleVo,tokenService.getToken(request))){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success();
    }

    @RequestMapping(value="/getaccount" ,method = {RequestMethod.POST})
    public ApiResult getaccount( HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Account account = new Account();
        return ApiResult.success(accountService.get(account));

    }
    //add_fjl_自用
    @RequestMapping(value = "/import", method = {RequestMethod.POST})
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void importdate() throws Exception {
        ExcelReader reader = ExcelUtil.getReader("e:/kemubianma.xlsx");
        ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> readAll = reader.readAll();
        for (Map<String, Object> item : readAll) {
            Account account = new Account();
            account.setFname(item.get("全名").toString());
            account.setFnumber(item.get("编码").toString());
            account.setFunitname(item.get("余额方向").toString());
            if(item.get("核算维度") != null){
                account.setFparentid(item.get("核算维度").toString());
            }
            account.setFdetail(item.get("明细科目").toString());
            accountMapper.insert(account);
        }
    }

}
