package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN0000.WorkPlan;
import com.nt.service_AOCHUAN.AOCHUAN0000.WorkPlanService;
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
import java.util.*;

@RestController
@RequestMapping("/index")
public class AOCHUAN0000Controller {

    @Autowired
    private WorkPlanService workPlanService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getWorkPlanList",method={RequestMethod.POST})
    public ApiResult getWorkPlanList(HttpServletRequest request) throws Exception {
        return ApiResult.success(workPlanService.getWorkPlanList());
    }

    /**
     * 更新
     * @param workPlanList
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody List<WorkPlan> workPlanList, HttpServletRequest request) throws Exception {

        //全删除
        if(workPlanList.isEmpty()){

            //获取当天数据
            List<WorkPlan> dbList = workPlanService.selectByToday();

            //删除
            for (WorkPlan item:dbList){

                WorkPlan workPlan = item;
                workPlan.preUpdate(tokenService.getToken(request));

                if(!workPlanService.del(workPlan)){
                    return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                }
            }

        }
        else{
            Map<String,List<WorkPlan>> resultMap = new  HashMap<>();

            //获取当天数据
            List<WorkPlan> dbList = workPlanService.selectByToday();

            //获取差异部分
            resultMap = getDiffLst(workPlanList,dbList);
            List<WorkPlan> diffList = resultMap.get("diff");
            List<WorkPlan> sameList = resultMap.get("same");

            if(!diffList.isEmpty()){
            //新建/删除
            for (WorkPlan item:diffList) {
                WorkPlan workPlan = item;

                //删除
                if(workPlanService.existCheck(workPlan)){
                    workPlan.preUpdate(tokenService.getToken(request));
                    if(!workPlanService.del(workPlan)){
                        return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                    }
                }
                //新建
                else{
                    String id= UUID.randomUUID().toString();
                    workPlan.setWorkplan_id(id);
                    workPlan.preInsert(tokenService.getToken(request));
                    if(!workPlanService.insert(workPlan)){
                        return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                    }
                }
            }
            }

            if(!sameList.isEmpty()) {
                //更新
                for (WorkPlan item : sameList) {
                    WorkPlan workPlan = item;
                    workPlan.preUpdate(tokenService.getToken(request));
                    if (!workPlanService.update(workPlan)) {
                        return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                    }
                }
            }
        }

        return ApiResult.success(workPlanService.getWorkPlanList());
    }

    /**
     * 比较list
     * @param list1
     * @param list2
     * @return
     */
    private Map<String,List<WorkPlan>> getDiffLst(List<WorkPlan> list1,List<WorkPlan> list2){

        List<WorkPlan> maxList = new ArrayList<>();
        List<WorkPlan> minList = new ArrayList<>();
        List<WorkPlan> diffList = new ArrayList<>();
        List<WorkPlan> sameList = new ArrayList<>();


        //区分大小
        if(list1.size() > list2.size()){
            maxList = list1;
            minList = list2;
        }
        else{
            minList = list1;
            maxList = list2;
        }

        Map<WorkPlan,Integer> map = new HashMap<WorkPlan,Integer>(maxList.size());

        //大lst的追加
        for(WorkPlan item: maxList){ map.put(item,1); }
        //小lst的追加
        for (WorkPlan workPlan : minList){
            if(map.get(workPlan) != null){
                map.put(workPlan,2);
                continue;
            }
            diffList.add(workPlan);
        }

        //筛选出差异部分
        for (Map.Entry<WorkPlan,Integer> entry: map.entrySet()){
            if(entry.getValue()==1){
                diffList.add(entry.getKey());
            }
            else if(entry.getValue()==2){
                sameList.add(entry.getKey());
            }
        }


        Map<String,List<WorkPlan>> resultMap = new HashMap<>();

        resultMap.put("diff",diffList);
        resultMap.put("same",sameList);

        return resultMap;
    }
}
