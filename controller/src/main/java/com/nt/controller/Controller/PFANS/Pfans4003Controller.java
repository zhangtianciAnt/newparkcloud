package com.nt.controller.Controller.PFANS;

import com.google.common.collect.Iterables;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.Vo.OrgTreeVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS4000.PeoplewareFeeService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/peopleware")
public class Pfans4003Controller {

    @Autowired
    private PeoplewareFeeService peoplewarefeeService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OrgTreeService orgTreeService;

    @RequestMapping(value = "/getPeopleWare", method = {RequestMethod.POST})
    public ApiResult getDepartmental(@RequestBody PeoplewareFee peoplewarefee, HttpServletRequest request) throws Exception {
        if (peoplewarefee == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        //region add scc 8/25 页面rank显示升序排序 from
        List<PeoplewareFee> peopleWareList = peoplewarefeeService.getPeopleWareList(peoplewarefee);
        peopleWareList = peopleWareList.stream().sorted(Comparator.comparing(PeoplewareFee::getRanks)).collect(Collectors.toList());
        List<PeoplewareFee> temp = new ArrayList<>();
        for(PeoplewareFee peo : peopleWareList){
            if(peo.getRanks().equals("R10") || peo.getRanks().equals("R11A") || peo.getRanks().equals("R11B")){
                temp.add(peo);
            }
        }
        if(peopleWareList.size() > 0) {
            for (int i = 0; i < 3; i++) {
                peopleWareList.remove(0);
            }
        }
        peopleWareList.addAll(temp);
        return ApiResult.success(peopleWareList);
        //endregion add scc 8/25 页面rank显示升序排序 to
    }

    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        if ("0".equals(type) ) {
            templateName = "bumenrenjianfei.xlsx";
            fileName = "部门人件费导入模板";
        }
        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }

    @RequestMapping(value = "/import", method = {RequestMethod.POST})
    public ApiResult importUser( HttpServletRequest request) throws Exception {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            List<String> stringList = peoplewarefeeService.importPeopleWare(request, tokenModel);
            List<String> returnList = new ArrayList<String>();
            returnList.add(stringList.get(0));
            if(stringList.size() > 1){
                returnList.add(stringList.get(1));
            }
            return ApiResult.success(returnList);
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
