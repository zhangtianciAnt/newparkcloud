package com.nt.controller.Controller.PFANS;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.ContractService;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contract")
public class Pfans1029Controller {
    @Autowired
    private ContractService contractService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/generateJxls", method = {RequestMethod.POST})
    public void downLoad(@RequestBody Contract contract, HttpServletRequest request, HttpServletResponse response) throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        ContractVo cv = contractService.One(contract.getContract_id());
//        List<Dictionary> CurList = dictionaryService.getForSelect("HT006");
//        for(Dictionary item:CurList){
//            if(item.getCode().equals(cv.getContract().getCurrencyposition())) {
//                cv.getContract().setCurrencyposition(item.getValue1());
//            }
//        }
        List<Dictionary> redList = dictionaryService.getForSelect("PJ080");
        for(Dictionary item:redList){
            if(item.getCode().equals(cv.getContract().getRedelegate())) {
                cv.getContract().setRedelegate(item.getValue1());
            }
        }
        List<Dictionary> subList = dictionaryService.getForSelect("PJ010");
        for(Dictionary item:subList){
            if(item.getCode().equals(cv.getContract().getSubcontract())) {
                cv.getContract().setSubcontract(item.getValue1());
            }
        }
        List<Dictionary> curList = dictionaryService.getForSelect("PG019");
        for(Dictionary item:curList){
            if(item.getCode().equals(cv.getContract().getCurrencyposition())) {
                //PG019001美元；PG019003 人民币
                if (item.getCode().equals("PG019001") || item.getCode().equals("PG019003")) {
                    cv.getContract().setCurrencyposition(item.getValue4());
                } else {
                    cv.getContract().setCurrencyposition(item.getValue1());
                }
            }
        }
        Map<String, Object> data = new HashMap<>();
        //20200427 add by ztc format data start
        //請求日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat str = new SimpleDateFormat("dd/MM/yyyy");
        Date tem_date = null;
        String str_format = "";
        str_format = str.format(cv.getOpeningdate());
        data.put("openingdate", str_format);
        str_format = str.format(cv.getEnddate());
        data.put("enddate", str_format);
        if (cv.getSigningdate() != null) {
            str_format = str.format(cv.getSigningdate());
            data.put("signingdate", str_format);
        }
        //請求金額
        DecimalFormat df = new DecimalFormat("###,###.00");
        BigDecimal bd = new BigDecimal(cv.getClaimamount());
        str_format = df.format(bd);
        data.put("claimamo", str_format);
//        cv.setClaimamount(str_format);
        for (int i = 0; i < cv.getNumberCount().size(); i++) {
            bd = new BigDecimal(cv.getNumberCount().get(i).getClaimamount());
            str_format = df.format(bd);
            cv.getNumberCount().get(i).setClaimamount(str_format);
        }
        //20200427 add by ztc format data end
        data.put("cv",cv.getContract());
        data.put("ba1",cv.getNumberCount());
        if (cv.getContracttype().equals("HT008003") || cv.getContracttype().equals("HT008004") || cv.getContracttype().equals("HT008007") || cv.getContracttype().equals("HT008008")){
            ExcelOutPutUtil.OutPut(cv.getContract().getContractnumber().toUpperCase()+"_役務契約書(受託)-jp(cn)","qiyueshu_yiwushoutuo.xlsx",data,response);
        } else if (cv.getContracttype().equals("HT008001") || cv.getContracttype().equals("HT008002") || cv.getContracttype().equals("HT008005") || cv.getContracttype().equals("HT008006")){
            ExcelOutPutUtil.OutPut(cv.getContract().getContractnumber().toUpperCase()+"_技術契約書(受託)-jp(cn)","qiyueshu_shoutuo.xlsx",data,response);
        }
    }

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Contract contract = new Contract();
        contract.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractService.get(contract));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Contract contract, HttpServletRequest request) throws Exception {
        if (contract == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractService.One(contract.getContract_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Contract contract, HttpServletRequest request) throws Exception{
        if (contract == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractService.update(contract,tokenModel);
        return ApiResult.success();
    }



}
