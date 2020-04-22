package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finsales")
public class AOCHUAN5001Controller {

    @Autowired
    private FinSalesService finSalesService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取费用表数据
     */
    @RequestMapping(value = "/getFinSalesList", method = {RequestMethod.POST})
    public ApiResult getFinSalesList(HttpServletRequest request) throws Exception {

        FinSales finSales = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales));
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/confirmStatus", method = {RequestMethod.POST})
    public ApiResult confirmStatus(@RequestBody FinSales finSales, HttpServletRequest request) throws Exception {

        if (finSales == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:FIN_SALES
        //存在Check
        if (finSalesService.existCheck(finSales)) {
            //唯一性Check
            if(! finSalesService.uniqueCheck(finSales)) {
                finSalesService.update(finSales, tokenService.getToken(request));
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        FinSales finSales1 = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales));
    }
}
