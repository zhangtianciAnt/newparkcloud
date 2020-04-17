package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Linkman;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.service_AOCHUAN.AOCHUAN1000.LinkmanService;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierproductrelationService;
import com.nt.service_AOCHUAN.AOCHUAN2000.CustomerbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/customerbaseinfor")
public class AOCHUAN2001Controller {
    @Autowired
    private CustomerbaseinforService customerbaseinforService;
    @Autowired
    private LinkmanService linkmanService;
    @Autowired
    private SupplierproductrelationService supplierproductrelationService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private TokenService tokenService;

    private String linkmanBaseinfoId;
    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws Exception{
        return ApiResult.success(customerbaseinforService.get());
    }

    @RequestMapping(value = "/getLinkman",method={RequestMethod.GET})
    public ApiResult getLinkman(@RequestParam String baseinfo_id, HttpServletRequest request)throws Exception{
        if(!StringUtils.isNotBlank(baseinfo_id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(linkmanService.getByBaseinforId(baseinfo_id));
    }


    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(customerbaseinforService.getOne(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Customerbaseinfor customerbaseinfor, HttpServletRequest request) throws Exception {
        if(customerbaseinfor == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        customerbaseinforService.update(customerbaseinfor,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/updateLinkman",method={RequestMethod.POST})
    public ApiResult updateLinkman(@RequestBody List<Linkman> linkmans, HttpServletRequest request) throws Exception {
        for(int i = 0;i < linkmans.size();i++){
            if(linkmans.get(i) == null){
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            if(linkmans.get(i).getLinkman_id() == null || "".equals(linkmans.get(i).getLinkman_id())){
                linkmanService.insert(linkmans.get(i),tokenService.getToken(request));
            }else{
                linkmanService.update(linkmans.get(i),tokenService.getToken(request));
            }
        }
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody Customerbaseinfor customerbaseinfor, HttpServletRequest request) throws Exception {
        if(customerbaseinfor == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        linkmanBaseinfoId = customerbaseinforService.insert(customerbaseinfor,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertLinkman",method={RequestMethod.POST})
    public ApiResult insertLinkman(@RequestBody List<Linkman> linkmans, HttpServletRequest request) throws Exception {
        for(int i = 0;i < linkmans.size();i++){
            if(linkmans.get(i) == null){
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            linkmans.get(i).setBaseinfor_id(linkmanBaseinfoId);
            linkmanService.insert(linkmans.get(i),tokenService.getToken(request));
        }
        linkmanBaseinfoId = null;
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        customerbaseinforService.delete(id);
        linkmanService.deleteByByBaseinforId(id);
        return ApiResult.success();
    }
    @RequestMapping(value = "/deleteLinkman",method={RequestMethod.GET})
    public ApiResult deleteLinkman(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        linkmanService.delete(id);
        return ApiResult.success();
    }
    @RequestMapping(value = "/insertProduct",method={RequestMethod.POST})
    public ApiResult insertProduct(@RequestBody Products product, HttpServletRequest request) throws Exception {
        if(product == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        //linkmanBaseinfoId = customerbaseinforService.insert(customerbaseinfor,tokenService.getToken(request));
        product  = productsService.insertForSupplier(product,tokenService.getToken(request));
        return ApiResult.success(product);
    }

    @RequestMapping(value = "/insertSupplierproductrelation",method={RequestMethod.POST})
    public ApiResult insertSupplierproductrelation(@RequestBody Supplierproductrelation[] supplierproductrelation, HttpServletRequest request) throws Exception {
        if(supplierproductrelation == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        for(int i = 0;i < supplierproductrelation.length;i++ ){
            supplierproductrelationService.insert(supplierproductrelation[i],tokenService.getToken(request));
        }
        return ApiResult.success();
    }

    @RequestMapping(value = "/getProductByRelation",method={RequestMethod.GET})
    public ApiResult getProductByRelation(@RequestParam String baseinfo_id,HttpServletRequest request)throws Exception{
        if(!StringUtils.isNotBlank(baseinfo_id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        List<Supplierproductrelation> list =  supplierproductrelationService.getBySupplierbaseinforId(baseinfo_id);
        List<Products> productList = new ArrayList<Products>();
        for(int i = 0;i < list.size();i++){
            Products p = productsService.One(list.get(i).getProducts_id());
            productList.add(p);
        }
        return ApiResult.success(productList);
    }
}
