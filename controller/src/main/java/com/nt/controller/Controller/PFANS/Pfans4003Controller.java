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

        Collections.sort(peopleWareList,new Comparator<PeoplewareFee>(){//先排字母

            @Override
            public int compare(PeoplewareFee o1, PeoplewareFee o2) {
                String ran1 = "";
                String ran2 = "";
                if(o1.getRanks().length() == 3){//R10或R9A
                    if(o1.getRanks().charAt(2) == 'B' || o1.getRanks().charAt(2) == 'C' || o1.getRanks().charAt(2) == 'A'){
                        ran1 = String.valueOf(o1.getRanks().charAt(2));
                    }
                }
                else if(o1.getRanks().length() == 4){//R11A、R4新人、R5新人
                    if(Character.isLetter(o1.getRanks().charAt(3))){
                        ran1 = String.valueOf(o1.getRanks().charAt(3));
                    }else{
                        ran1 = String.valueOf(o1.getRanks().charAt(0));//取R比较，手动写死，使按字母排序时R4新人、R5新人排在前面
                    }
                }
                else if(o1.getRanks().length() == 5){//日本出项者
                    ran1 = "R";//取R比较，手动写死，使按字母排序时日本出项者排在前面
                }
                if(o2.getRanks().length() == 3){//R10或R9A
                    if(o2.getRanks().charAt(2) == 'B' || o2.getRanks().charAt(2) == 'C' || o2.getRanks().charAt(2) == 'A'){
                        ran2 = String.valueOf(o2.getRanks().charAt(2));
                    }
                }
                else if(o2.getRanks().length() == 4){//R11A、R4新人、R5新人
                    if(Character.isLetter(o2.getRanks().charAt(3))){
                        ran2 = String.valueOf(o2.getRanks().charAt(3));
                    }else{
                        ran2 = String.valueOf(o2.getRanks().charAt(0));//取R比较，手动写死，使按字母排序时R4新人、R5新人排在前面
                    }
                }
                else if(o2.getRanks().length() == 5){//日本出项者
                    ran2 = "R";//取R比较，手动写死，使按字母排序时日本出项者排在前面
                }
                return ran2.compareTo(ran1);
            }
        });

        Collections.sort(peopleWareList,new Comparator<PeoplewareFee>(){//再排数字

            @Override
            public int compare(PeoplewareFee o1, PeoplewareFee o2) {
                int RANK1 = 0;
                int RANK2 = 0;
                if(o1.getRanks().length() == 2) {//R3
                    RANK1 = Integer.parseInt(o1.getRanks().substring(o1.getRanks().length() - 1));
                }
                else if(o1.getRanks().length() == 3){//R10或R9A
                    if(o1.getRanks().charAt(2) == 'B' || o1.getRanks().charAt(2) == 'C' || o1.getRanks().charAt(2) == 'A'){
                        RANK1 = Integer.parseInt(o1.getRanks().substring(1,2));
                    }else{
                        RANK1 = Integer.parseInt(o1.getRanks().substring(o1.getRanks().length() - 2));
                    }
                }
                else if(o1.getRanks().length() == 4) {//R11A、R4新人、R5新人
                    if(Character.isDigit(o1.getRanks().charAt(2))){
                        RANK1 = Integer.parseInt(o1.getRanks().substring(1, 3));
                    }else{
                        RANK1 = 0;//取0比较，手动写死，再次按照数字排序时，使R4新人、R5新人排在前面
                    }
                }
                else if(o1.getRanks().length() == 5){//日本出项者
                    RANK1 = 100;//取100比较，手动写死，再次按照数字排序时，使R4新人、R5新人排在前面
                }
                if(o2.getRanks().length() == 2){
                    RANK2 = Integer.parseInt(o2.getRanks().substring(o2.getRanks().length() - 1));
                }
                else if(o2.getRanks().length() == 3){//R10或R9A
                    if(o2.getRanks().charAt(2) == 'B' || o2.getRanks().charAt(2) == 'C' || o2.getRanks().charAt(2) == 'A'){
                        RANK2 = Integer.parseInt(o2.getRanks().substring(1,2));
                    }else{
                        RANK2 = Integer.parseInt(o2.getRanks().substring(o2.getRanks().length() - 2));
                    }
                }
                else if(o2.getRanks().length() == 4){//R11A、R4新人、R5新人
                    if(Character.isDigit(o2.getRanks().charAt(2))){
                        RANK2 = Integer.parseInt(o2.getRanks().substring(1, 3));
                    }else{
                        RANK2 = 0;//取0比较，手动写死，再次按照数字排序时，使R4新人、R5新人排在前面
                    }
                }
                else if(o2.getRanks().length() == 5){//日本出项者
                    RANK2 = 100;//取100比较，手动写死，再次按照数字排序时，使R4新人、R5新人排在前面
                }
                return RANK1 - RANK2;
            }
        });


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
