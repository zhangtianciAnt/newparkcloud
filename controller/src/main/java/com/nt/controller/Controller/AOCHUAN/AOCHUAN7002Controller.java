package com.nt.controller.Controller.AOCHUAN;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Account;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierbaseinforMapper;
import com.nt.service_AOCHUAN.AOCHUAN2000.mapper.CustomerbaseinforMapper;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private CustomerbaseinforMapper customerbaseinforMapper;
    @Autowired
    private SupplierbaseinforMapper supplierbaseinforMapper;

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
    //add_fjl_自用导入接口
    @RequestMapping(value = "/import", method = {RequestMethod.POST})
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void importdate() throws Exception {
        //科目代码维护
//        ExcelReader reader = ExcelUtil.getReader("e:/kemubianma.xlsx");
//        ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
//        List<Map<String, Object>> readAll = reader.readAll();
//        for (Map<String, Object> item : readAll) {
//            Account account = new Account();
//            account.setFname(item.get("全名").toString());
//            account.setFnumber(item.get("编码").toString());
//            account.setFunitname(item.get("余额方向").toString());
//            if(item.get("核算维度") != null){
//                account.setFparentid(item.get("核算维度").toString());
//            }
//            account.setFdetail(item.get("明细科目").toString());
//            accountMapper.insert(account);
//        }
        //维护客户编码
        ExcelReader reader = ExcelUtil.getReader("e:/客户_2020111815242353_100006.xlsx");
        List<Map<String, Object>> readAll = reader.readAll();
        for (Map<String, Object> item : readAll) {
            Customerbaseinfor c = new Customerbaseinfor();
            List<Customerbaseinfor> cl = null;
            if(item.get("ERP客户名称") != null){
                c.setCustomernameen(item.get("ERP客户名称").toString());
                cl = customerbaseinforMapper.select(c);
            }
            if(cl != null && cl.size()>0){
                cl.get(0).setCustnumber(item.get("客户编码").toString());
                if(item.get("客户分组") != null){
                    cl.get(0).setNation(item.get("客户分组").toString());
                }
                customerbaseinforMapper.updateByPrimaryKeySelective(cl.get(0));
            } else {
                c.setCustomerbaseinfor_id(UUID.randomUUID().toString());
                c.setStatus("0");
                c.setCreateon(new Date());
                if(item.get("客户分组") != null){
                    c.setNation(item.get("客户分组").toString());
                }
                c.setCustnumber(item.get("客户编码").toString());
                c.setCustomernameen(item.get("客户名称").toString());
                customerbaseinforMapper.insert(c);
            }
        }
        //维护供应商信息
        ExcelReader reader1 = ExcelUtil.getReader("e:/供应商_2020111815202016_100006.xlsx");
        List<Map<String, Object>> readAll1 = reader1.readAll();
        for (Map<String, Object> item : readAll1) {
            Supplierbaseinfor s = new Supplierbaseinfor();
            List<Supplierbaseinfor> cl = null;
            if(item.get("ERP供应商的名称") != null){
                s.setSuppliernamecn(item.get("ERP供应商的名称").toString());
                cl = supplierbaseinforMapper.select(s);
            }
            if(cl != null && cl.size()>0){
                cl.get(0).setSupnumber(item.get("编码").toString());
                supplierbaseinforMapper.updateByPrimaryKeySelective(cl.get(0));
            } else {
                s.setSupplierbaseinfor_id(UUID.randomUUID().toString());
                s.setStatus("0");
                s.setCreateon(new Date());
                s.setSupnumber(item.get("编码").toString());
                s.setSuppliernamecn(item.get("名称").toString());
                supplierbaseinforMapper.insert(s);
            }
        }
    }

}
