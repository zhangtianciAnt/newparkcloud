package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Linkman;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.service_AOCHUAN.AOCHUAN1000.LinkmanService;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierbaseinforService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/supplierbaseinfor")
public class AOCHUAN1001Controller {
    @Autowired
    private SupplierbaseinforService supplierbaseinforService;
    @Autowired
    private LinkmanService linkmanService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws Exception{
        return ApiResult.success(supplierbaseinforService.get());
    }

    @RequestMapping(value = "/getLinkman",method={RequestMethod.GET})
    public ApiResult getLinkman(@RequestParam String baseinfor_id,HttpServletRequest request)throws Exception{
        return ApiResult.success(linkmanService.getByBaseinforId(baseinfor_id));
    }


    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(supplierbaseinforService.getOne(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Supplierbaseinfor supplierbaseinfor, HttpServletRequest request) throws Exception {
        if(supplierbaseinfor == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        supplierbaseinforService.update(supplierbaseinfor,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody Supplierbaseinfor supplierbaseinfor, HttpServletRequest request) throws Exception {
        if(supplierbaseinfor == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        supplierbaseinforService.insert(supplierbaseinfor,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertLinkman",method={RequestMethod.POST})
    public ApiResult insertLinkman(@RequestBody List<Linkman> linkmans, HttpServletRequest request) throws Exception {
        for(int i = 0;i < linkmans.size();i++){
            if(linkmans.get(i) == null){
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            linkmanService.insert(linkmans.get(i),tokenService.getToken(request));
        }
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        supplierbaseinforService.delete(id);
        return ApiResult.success();
    }

}
