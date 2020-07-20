package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.PolicyContract;
import com.nt.dao_Pfans.PFANS1000.Vo.PolicyContractVo;
import com.nt.service_pfans.PFANS1000.PolicyContractService;
import com.nt.service_pfans.PFANS1000.mapper.PolicyContractMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/policycontract")
public class Pfans1045Controller {

    @Autowired
    private PolicyContractService policycontractservice;
    @Autowired
    private PolicyContractMapper policycontractmapper;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getPolicyContract(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PolicyContract policycontract = new PolicyContract();
        policycontract.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(policycontractservice.getPolicyContract(policycontract));

    }

    @RequestMapping(value = "/get2", method = {RequestMethod.GET})
    public ApiResult getPolicyContract2(HttpServletRequest request) throws Exception {
        List<PolicyContract> PolicyContractlist = policycontractmapper.selectAll();
        PolicyContractlist = PolicyContractlist.stream().filter(item -> (item.getStatus().equals("4"))).collect(Collectors.toList());
        return ApiResult.success(PolicyContractlist);
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody PolicyContract policycontract, HttpServletRequest request) throws Exception {
        if (policycontract == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(policycontractservice.One(policycontract.getPolicycontract_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updatePolicyContract(@RequestBody PolicyContractVo policycontractvo, HttpServletRequest request) throws Exception {
        if (policycontractvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        policycontractservice.updatePolicyContract(policycontractvo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody PolicyContractVo policycontractvo, HttpServletRequest request) throws Exception {
        if (policycontractvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        policycontractservice.insert(policycontractvo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/check", method = {RequestMethod.POST})
    public ApiResult check(@RequestBody PolicyContract policycontract, HttpServletRequest request) throws Exception {
        if (policycontract == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(policycontractservice.check(policycontract, tokenModel));
    }

    @RequestMapping(value = "/chackcycle", method = {RequestMethod.POST})
    public ApiResult chackcycle(@RequestBody PolicyContract policycontract, HttpServletRequest request) throws Exception {
        if (policycontract == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        Map<String, Object> data = new HashMap<>();
        String checkerror = "0";
        String checksuccess = "0";
        List<PolicyContract> policycontractlist = policycontractmapper.selectAll();
        for (PolicyContract pro : policycontractlist) {
            if (pro.getCycle().equals(policycontract.getCycle())) {
                checkerror = "1";
                break;
            } else {
                if (pro.getCycle().equals("0")) {
                    checkerror = "1";
                    break;
                }else if (policycontract.getCycle().equals("0")) {
                    checkerror = "1";
                    break;
                } else {
                    if (pro.getCycle().equals("1") && (policycontract.getCycle().equals("3") || policycontract.getCycle().equals("4"))) {
                        checkerror = "1";
                        break;
                    } else if (pro.getCycle().equals("2") && (policycontract.getCycle().equals("5") || policycontract.getCycle().equals("6"))) {
                        checkerror = "1";
                        break;
                    } else if (policycontract.getCycle().equals("1") && (pro.getCycle().equals("5") || pro.getCycle().equals("6"))) {
                        checkerror = "1";
                        break;
                    } else if (policycontract.getCycle().equals("2") && (pro.getCycle().equals("5") || pro.getCycle().equals("6"))) {
                        checkerror = "1";
                        break;
                    } else {
                        checksuccess = "2";
                    }
                }
            }
        }
        data.put("errorcheck", checkerror);
        data.put("successcheck", checksuccess);
        return ApiResult.success(data);
    }
}
