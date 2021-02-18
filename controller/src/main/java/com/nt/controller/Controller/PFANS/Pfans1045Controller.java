package com.nt.controller.Controller.PFANS;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Pfans.PFANS1000.PolicyContract;
import com.nt.dao_Pfans.PFANS1000.Vo.PolicyContractVo;
import com.nt.service_pfans.PFANS1000.PolicyContractService;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/policycontract")
public class Pfans1045Controller {

    @Autowired
    private PolicyContractService policycontractservice;
    @Autowired
    private PolicyContractMapper policycontractmapper;
    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private TokenService tokenService;

    //UPD-ws-02/06-PSDCD_PFANS_20210205_XQ_078
//    @RequestMapping(value = "/get", method = {RequestMethod.POST})
//    public ApiResult getPolicyContract(@RequestBody PolicyContract policycontract, HttpServletRequest request) throws Exception {
//        SimpleDateFormat sf = new SimpleDateFormat("MM");
//        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
//        List<PolicyContract> policycontractlist2 = new ArrayList<>();
//        String cycle = "";
//        String year = "";
//        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(policycontract.getInformation())) {
//            cycle = policycontract.getInformation().substring(5, 7);
//            year = policycontract.getInformation().substring(0, 4);
//        }
//        PolicyContract policy = new PolicyContract();
//        policy.setOutsourcingcompany(policycontract.getOutsourcingcompany());
//        List<PolicyContract> policycontractlist = policycontractmapper.select(policy);
//        policycontractlist = policycontractlist.stream().filter(item -> (item.getStatus().equals("4"))).collect(Collectors.toList());
//        for (PolicyContract PolicyCon : policycontractlist) {
//            if (PolicyCon.getYearss().equals(year)) {
//                if (cycle.equals("04") || cycle.equals("05") || cycle.equals("06")) {
//                    if (PolicyCon.getCycle().equals("3") || PolicyCon.getCycle().equals("1")) {
//                        policycontractlist2.add(PolicyCon);
//                    }
//                } else if (cycle.equals("09") || cycle.equals("07") || cycle.equals("08")) {
//                    if (PolicyCon.getCycle().equals("4") || PolicyCon.getCycle().equals("1")) {
//                        policycontractlist2.add(PolicyCon);
//                    }
//                } else if (cycle.equals("12") || cycle.equals("10") || cycle.equals("11")) {
//                    if (PolicyCon.getCycle().equals("5") || PolicyCon.getCycle().equals("2")) {
//                        policycontractlist2.add(PolicyCon);
//                    }
//                } else if (cycle.equals("03") || cycle.equals("01") || cycle.equals("02")) {
//                    if (PolicyCon.getCycle().equals("6") || PolicyCon.getCycle().equals("2")) {
//                        policycontractlist2.add(PolicyCon);
//                    }
//                }
//                if (PolicyCon.getCycle().equals("0")) {
//                    policycontractlist2.add(PolicyCon);
//                }
//            }
//        }
//        return ApiResult.success(policycontractlist2);
//
//    }
    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult getPolicyContract(@RequestBody PolicyContract policycontract, HttpServletRequest request) throws Exception {
        List<PolicyContract> policycontractlist2 = new ArrayList<>();
        String year = "";
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(policycontract.getInformation())) {
            year = policycontract.getInformation();
        }
        PolicyContract policy = new PolicyContract();
        policy.setOutsourcingcompany(policycontract.getOutsourcingcompany());
        List<PolicyContract> policycontractlist = policycontractmapper.select(policy);
        policycontractlist = policycontractlist.stream().filter(item -> (item.getStatus().equals("4"))).collect(Collectors.toList());
        for (PolicyContract PolicyCon : policycontractlist) {
            String starttime = PolicyCon.getYearss().substring(0, 7);
            String endtime = PolicyCon.getYearss().substring(10, 17);
            if (Integer.valueOf(starttime.replace("-", "")) <= Integer.valueOf(year.replace("-", "")) && Integer.valueOf(endtime.replace("-", "")) >= Integer.valueOf(year.replace("-", ""))) {
                policycontractlist2.add(PolicyCon);
            }
        }
        return ApiResult.success(policycontractlist2);

    }

    //UPD-ws-02/06-PSDCD_PFANS_20210205_XQ_078
    @RequestMapping(value = "/get2", method = {RequestMethod.GET})
    public ApiResult getPolicyContract2(HttpServletRequest request) throws Exception {
        List<PolicyContract> PolicyContractlist = policycontractmapper.selectAll();
        PolicyContractlist = PolicyContractlist.stream().filter(item -> (item.getStatus().equals("4"))).collect(Collectors.toList());
        return ApiResult.success(PolicyContractlist);
    }

    @RequestMapping(value = "/get3", method = {RequestMethod.GET})
    public ApiResult getPolicyContract3(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PolicyContract policycontract = new PolicyContract();
        policycontract.setOwners(tokenModel.getOwnerList());
        List<PolicyContract> PolicyContractlist = policycontractmapper.select(policycontract);
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

    //UPD-ws-02/06-PSDCD_PFANS_20210205_XQ_078
//    @RequestMapping(value = "/chackcycle", method = {RequestMethod.POST})
//    public ApiResult chackcycle(@RequestBody PolicyContract policycontract, HttpServletRequest request) throws Exception {
//        if (policycontract == null) {
//            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
//        }
//        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
//
//        PolicyContract policy = new PolicyContract();
//        String cycle = policycontract.getCycle();
//        String year = policycontract.getYearss();
//        policy.setOutsourcingcompany(policycontract.getOutsourcingcompany());
//        policy.setYearss(policycontract.getYearss());
//        List<PolicyContract> policycontractlist2 = new ArrayList<>();
//        List<PolicyContract> policycontractlist3 = policycontractmapper.selectAll();
//        policycontractlist3.addAll(0, policycontractlist3);
//        List<PolicyContract> policylist = policycontractmapper.select(policy);
//        if (policylist.size() > 0) {
//            for (PolicyContract PolicyContract : policylist) {
//                if (PolicyContract.getYearss().equals(year)) {
//                    if (PolicyContract.getCycle().equals("0")) {
//                        if (cycle.equals("0") || cycle.equals("1") || cycle.equals("2") || cycle.equals("3") || cycle.equals("4") || cycle.equals("5") || cycle.equals("6")) {
//                            policycontractlist2 = policycontractlist3;
//                            break;
//                        } else {
//                            policycontractlist2 = policylist;
//                        }
//                    } else if (PolicyContract.getCycle().equals("1")) {
//                        if (cycle.equals("3") || cycle.equals("4") || cycle.equals("1")) {
//                            policycontractlist2 = policycontractlist3;
//                            break;
//                        } else {
//                            policycontractlist2 = policylist;
//                        }
//
//                    } else if (PolicyContract.getCycle().equals("2")) {
//                        if (cycle.equals("5") || cycle.equals("6") || cycle.equals("2")) {
//                            policycontractlist2 = policycontractlist3;
//                            break;
//                        } else {
//                            policycontractlist2 = policylist;
//                        }
//
//                    } else if (PolicyContract.getCycle().equals("3")) {
//                        if (cycle.equals("3") || cycle.equals("1")) {
//                            policycontractlist2 = policycontractlist3;
//                            break;
//                        } else {
//                            policycontractlist2 = policylist;
//                        }
//
//                    } else if (PolicyContract.getCycle().equals("4")) {
//                        if (cycle.equals("4") || cycle.equals("1")) {
//                            policycontractlist2 = policycontractlist3;
//                            break;
//                        } else {
//                            policycontractlist2 = policylist;
//                        }
//
//                    } else if (PolicyContract.getCycle().equals("5")) {
//                        if (cycle.equals("5") || cycle.equals("2")) {
//                            policycontractlist2 = policycontractlist3;
//                            break;
//                        } else {
//                            policycontractlist2 = policylist;
//                        }
//
//                    } else if (PolicyContract.getCycle().equals("6")) {
//                        if (cycle.equals("6") || cycle.equals("2")) {
//                            policycontractlist2 = policycontractlist3;
//                            break;
//                        } else {
//                            policycontractlist2 = policylist;
//                        }
//                    }
//                }
//            }
//        } else {
//            policycontractlist2 = policylist;
//        }
//        return ApiResult.success(policycontractlist2);
//    }
    @RequestMapping(value = "/chackcycle", method = {RequestMethod.POST})
    public ApiResult chackcycle(@RequestBody PolicyContract policycontract, HttpServletRequest request) throws Exception {
        if (policycontract == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        PolicyContract policy = new PolicyContract();
        policy.setOutsourcingcompany(policycontract.getOutsourcingcompany());
        List<PolicyContract> policylist = policycontractmapper.select(policy);
        if (policylist.size() > 0) {
            for (PolicyContract list : policylist) {
                String starttime = list.getYearss().substring(0, 7);
                String endtime = list.getYearss().substring(10, 17);
                if (Integer.valueOf(starttime.replace("-", "")) <= Integer.valueOf(policycontract.getCycle().replace("-", "")) && Integer.valueOf(endtime.replace("-", "")) >= Integer.valueOf(policycontract.getYearss().replace("-", ""))) {
                    return ApiResult.success(false);
                } else if (Integer.valueOf(starttime.replace("-", "")) >= Integer.valueOf(policycontract.getCycle().replace("-", "")) && Integer.valueOf(endtime.replace("-", "")) >= Integer.valueOf(policycontract.getYearss().replace("-", ""))) {
                    return ApiResult.success(false);
                } else if (Integer.valueOf(starttime.replace("-", "")) <= Integer.valueOf(policycontract.getCycle().replace("-", "")) && Integer.valueOf(endtime.replace("-", "")) <= Integer.valueOf(policycontract.getYearss().replace("-", ""))) {
                    return ApiResult.success(false);
                } else if (Integer.valueOf(starttime.replace("-", "")) >= Integer.valueOf(policycontract.getCycle().replace("-", "")) && Integer.valueOf(endtime.replace("-", "")) <= Integer.valueOf(policycontract.getYearss().replace("-", ""))) {
                    return ApiResult.success(false);
                }
            }
        }
        return ApiResult.success(true);
    }
    //UPD-ws-02/06-PSDCD_PFANS_20210205_XQ_078
}
