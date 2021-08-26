package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.dao_Pfans.PFANS6000.Coststatisticsdetail;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.IndividualMapper;
import com.nt.service_pfans.PFANS6000.mapper.CoststatisticsdetailMapper;
import com.nt.service_pfans.PFANS6000.mapper.SupplierinforMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/contractapplication")
public class Pfans1026Controller {
    @Autowired
    private SupplierinforMapper supplierinforMapper;
    @Autowired
    private IndividualMapper individualmapper;
    @Autowired
    private ContractapplicationService contractapplicationService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CoststatisticsdetailMapper coststatisticsdetailMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private OrgTreeService orgtreeservice;
    //add-ws-7/22-禅道341任务
    @RequestMapping(value = "/getindividual", method = {RequestMethod.POST})
    public ApiResult getindividual(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Individual individual = new Individual();
        individual.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.getindividual(individual));
    }

    @RequestMapping(value = "/generatesta", method = {RequestMethod.GET})
    public void generateJxls(String individual_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        Individual individual = individualmapper.selectByPrimaryKey(individual_id);
        Supplierinfor Supplierinfor =new Supplierinfor();
        Supplierinfor.setSupchinese(individual.getCustojapanese());
        List<Supplierinfor> supplierinforlist = supplierinforMapper.select(Supplierinfor);
        List<Coststatisticsdetail> coststatisticsdetaillist = new ArrayList<>();
        if(supplierinforlist.size()>0){
            Coststatisticsdetail coststatisticsdetail = new Coststatisticsdetail();
            coststatisticsdetail.setSupplierinforid(supplierinforlist.get(0).getSupplierinfor_id());
            coststatisticsdetail.setDates(individual.getDates());
            coststatisticsdetaillist = coststatisticsdetailMapper.select(coststatisticsdetail);
//            if (coststatisticsdetaillist.size() > 0) {
//                for(Coststatisticsdetail cost :coststatisticsdetaillist){
//                    Query query = new Query();
//                    query.addCriteria(Criteria.where("userinfo.groupid").is(cost.getGroupid()));
//                    CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
//                    if (customerInfo != null) {
//                        cost.setGroupid((customerInfo.getUserinfo().getGroupname()));
//                    }
//                }
//            }
        }
        String last = "";
        String first = "";
        String trr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        if (individual.getDates() != null && individual.getDates() != "") {
            int year = Integer.valueOf(individual.getDates().substring(0, 4));
            int month = Integer.valueOf(individual.getDates().substring(5, 7));
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date lastDate = cal.getTime();
            last = format.format(lastDate);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDate = cal.getTime();
            first = format.format(firstDate);
        }
        data.put("cos", coststatisticsdetaillist);
        data.put("firstday", first);
        data.put("lastday", last);
        data.put("ind", individual);
        data.put("trr", trr);
        data.put("tra", coststatisticsdetaillist);
        ExcelOutPutUtil.OutPutPdf("个别合同书", "gebiehetong.xls", data, response);
    }

    //add-ws-7/22-禅道341任务
    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult selectById(@RequestBody Contractapplication contractapplication, HttpServletRequest request) throws Exception {
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.get(contractapplication));
    }

    //add  ml  20210706   契约番号废弃check   from
    @RequestMapping(value = "/getProject", method = {RequestMethod.GET})
    public ApiResult getProject(String contractnumber, HttpServletRequest request) throws Exception {
        if (contractnumber == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(contractapplicationService.getProject(contractnumber));
    }
    //add  ml  20210706   契约番号废弃check   to

    @RequestMapping(value = "/get2", method = {RequestMethod.POST})
    public ApiResult selectById2(@RequestBody Contractapplication contractapplication, HttpServletRequest request) throws Exception {
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
//        TokenModel tokenModel = tokenService.getToken(request);
//        contractapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.get(contractapplication));
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody List<Contractapplication> contractapplicationlist, HttpServletRequest request) throws Exception {
        if (contractapplicationlist == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(contractapplicationService.getList(contractapplicationlist));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ContractapplicationVo contractapplication, HttpServletRequest request) throws Exception {
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.update(contractapplication, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ContractapplicationVo contractapplication, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.insert(contractapplication, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertBook", method = {RequestMethod.GET})
    public ApiResult insertBook(String contractnumber, String rowindex, String countNumber, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        //upd-ws-7/1-禅道152任务
        Map<String, Object> list = contractapplicationService.insertBook(contractnumber, rowindex, countNumber, tokenModel);
        return ApiResult.success(list);
        //upd-ws-7/1-禅道152任务
    }

    @RequestMapping(value = "/existCheck", method = {RequestMethod.GET})
    public ApiResult existCheck(String contractNumber, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.existCheck(contractNumber));
    }

    @RequestMapping(value = "/existN", method = {RequestMethod.POST})
    public ApiResult existQN(@RequestBody List<String> NapinList, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.existN(NapinList));
    }

    @RequestMapping(value = "/existQ", method = {RequestMethod.POST})
    public ApiResult existQ(@RequestBody List<String> QingqiuList, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.existQ(QingqiuList));
    }

    @RequestMapping(value = "/getPe", method = {RequestMethod.GET})
    public ApiResult getPe(String claimnumber, HttpServletRequest request) throws Exception {
        if (claimnumber == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(contractapplicationService.getPe(claimnumber));
    }

    //add ccm 0725  采购合同chongfucheck
    @RequestMapping(value = "/purchaseExistCheck", method = {RequestMethod.GET})
    public ApiResult purchaseExistCheck(String purnumbers, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.purchaseExistCheck(purnumbers));
    }
    //add ccm 0725  采购合同chongfucheck

    //采购业务数据流程查看详情
    @RequestMapping(value="/getworkfolwPurchaseData",method = {RequestMethod.POST})
    public ApiResult getworkfolwPurchaseData(@RequestBody Award award, HttpServletRequest request) throws Exception{
        if (award == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.getworkfolwPurchaseData(award));
    }
    //采购业务数据流程查看详情

    @RequestMapping(value="/getNapinQinqiu",method = {RequestMethod.POST})
    public ApiResult getNapinQinqiu(@RequestBody Contractnumbercount contractnumbercount, HttpServletRequest request) throws Exception{
        if (contractnumbercount == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.getNapinQinqiu(contractnumbercount));
    }

    /**
     *
     * 合同号申请页面中不同契约数据结转
     */
    @RequestMapping(value = "/dataCarryover", method = {RequestMethod.POST})
    public ApiResult dataCarryover(@RequestBody Contractapplication contractapplication, HttpServletRequest request) throws Exception {
        OrgTree newOrgInfo = orgtreeservice.get(new OrgTree());
        Contractapplication contractapplication1 = new Contractapplication();
        contractapplication1.setContractapplication_id(contractapplication.getContractapplication_id());
        contractapplication1.setGroup_id(contractapplication.getGroup_id());
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
            OrgTree orginfo = orgtreeservice.getOrgInfo(newOrgInfo, contractapplication.getGroup_id());
        contractapplication1.setDeployment(orginfo.getCompanyname());
        contractapplicationService.dataCarryover(contractapplication1,tokenModel);
        return ApiResult.success();
    }
}
