package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Linkman;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Vo.SupplierproductrelationVo;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.service_AOCHUAN.AOCHUAN1000.LinkmanService;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierproductrelationService;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/supplierbaseinfor")
public class AOCHUAN1001Controller {
    @Autowired
    private SupplierbaseinforService supplierbaseinforService;
    @Autowired
    private LinkmanService linkmanService;
    @Autowired
    private SupplierproductrelationService supplierproductrelationService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private TokenService tokenService;

    private String baseinfoId;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws Exception{
        return ApiResult.success(supplierbaseinforService.get());
    }

    @RequestMapping(value = "/getLinkman",method={RequestMethod.GET})
    public ApiResult getLinkman(@RequestParam String baseinfo_id,HttpServletRequest request)throws Exception{
        if(!StringUtils.isNotBlank(baseinfo_id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(linkmanService.getByBaseinforId(baseinfo_id));
    }


    @RequestMapping(value = "/getAllLinkman",method={RequestMethod.GET})
    public ApiResult getAllLinkman(HttpServletRequest request)throws Exception{
        return ApiResult.success(linkmanService.get());
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
    public ApiResult insert(@RequestBody Supplierbaseinfor supplierbaseinfor, HttpServletRequest request) throws Exception {
        if(supplierbaseinfor == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        baseinfoId = supplierbaseinforService.insert(supplierbaseinfor,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertLinkman",method={RequestMethod.POST})
    public ApiResult insertLinkman(@RequestBody List<Linkman> linkmans, HttpServletRequest request) throws Exception {
        String linkmanId = null;
        for(int i = 0;i < linkmans.size();i++){
            if(linkmans.get(i) == null){
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            linkmans.get(i).setBaseinfor_id(baseinfoId);
            if(i == 0){
                linkmanId = linkmanService.insert(linkmans.get(i),tokenService.getToken(request));
            }else{
                linkmanService.insert(linkmans.get(i),tokenService.getToken(request));
            }
        }
        Supplierbaseinfor supplierbaseinfor = new Supplierbaseinfor();
        supplierbaseinfor = supplierbaseinforService.getOne(baseinfoId);
        supplierbaseinfor.setLinkman(linkmanId);
        supplierbaseinforService.update(supplierbaseinfor,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        supplierbaseinforService.delete(id);
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
    public ApiResult insertProduct(@RequestBody Products[] products, HttpServletRequest request) throws Exception {
        if(products == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        //linkmanBaseinfoId = supplierbaseinforService.insert(supplierbaseinfor,tokenService.getToken(request));
        productsService.insertForSupplier(baseinfoId,products,tokenService.getToken(request));
        baseinfoId = null;
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertSupplierproductrelation",method={RequestMethod.POST})
    public ApiResult insertSupplierproductrelation(@RequestBody Supplierproductrelation[] supplierproductrelations, HttpServletRequest request) throws Exception {
        if(supplierproductrelations == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        supplierproductrelationService.deleteByBaseinforId(baseinfoId);
        for(int i = 0;i < supplierproductrelations.length;i++ ){
            supplierproductrelations[i].setSupplierbaseinfor_id(baseinfoId);
            supplierproductrelationService.insert(supplierproductrelations[i],tokenService.getToken(request));
        }
        return ApiResult.success();
    }

    @RequestMapping(value = "/updateSupplierproductrelation",method={RequestMethod.POST})
    public ApiResult updateSupplierproductrelation(@RequestBody SupplierproductrelationVo supplierproductrelationVo, HttpServletRequest request) throws Exception {
        if(supplierproductrelationVo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        supplierproductrelationService.deleteByBaseinforId(supplierproductrelationVo.getBaseinfor_id());
        for(int i = 0;i < supplierproductrelationVo.getSupplierproductrelationList().size();i++ ){
            //supplierproductrelations[i].setSupplierbaseinfor_id(baseinfoId);
            supplierproductrelationService.insert(supplierproductrelationVo.getSupplierproductrelationList().get(i),tokenService.getToken(request));
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
