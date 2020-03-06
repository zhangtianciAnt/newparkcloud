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
        List<Dictionary> CurList = dictionaryService.getForSelect("HT006");
        for(Dictionary item:CurList){
            if(item.getCode().equals(cv.getContract().getCurrencyposition())) {

                cv.getContract().setCurrencyposition(item.getValue1());
            }
        }
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
        Map<String, Object> data = new HashMap<>();
        data.put("cv",cv.getContract());
        data.put("ba1",cv.getNumberCount());
        ExcelOutPutUtil.OutPut(cv.getContract().getContractnumber().toUpperCase()+"_技術契約書(受託)-jp(cn)","qiyueshu_shoutuo.xlsx",data,response);
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
