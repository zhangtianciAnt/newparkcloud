package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.ProjectsAndFollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/projects")
public class AOCHUAN3009Controller {

    @Autowired
    private ProjectsService projectsSerivce;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private SupplierbaseinforService supplierbaseinforService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取projectschedule表数据
     */
    @RequestMapping(value = "/getProjectList", method = {RequestMethod.POST})
    public ApiResult getProjectList(HttpServletRequest request) throws Exception {
        return ApiResult.success(projectsSerivce.getProjectList());
    }

    /**
     * 获取记录列表数据
     */
    @RequestMapping(value = "/getFollowUpRecordList", method = {RequestMethod.POST})
    public ApiResult getFollowUpRecordList(@RequestBody Projects projects, HttpServletRequest request) throws Exception {
        return ApiResult.success(projectsSerivce.getFollowUpRecordList(projects));
    }

    /**
     * 获取项目表
     */
    @RequestMapping(value = "/getForm", method = {RequestMethod.GET})
    public ApiResult getForm(@RequestParam String id, HttpServletRequest request) throws Exception {
        return ApiResult.success(projectsSerivce.getForm(id));
    }

    @RequestMapping(value = "/getForSupplier", method = {RequestMethod.GET})
    public ApiResult getForSupplier(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (!StringUtils.isNotBlank(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectsSerivce.getForSupplier(id));
    }

    @RequestMapping(value = "/getForCustomer", method = {RequestMethod.GET})
    public ApiResult getForCustomer(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (!StringUtils.isNotBlank(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectsSerivce.getForCustomer(id));
    }

    /**
     * 获取不在项目表中的产品
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getProdutsExceptUnique", method = {RequestMethod.POST})
    public ApiResult getProdutsExceptUnique(HttpServletRequest request) throws Exception {
        return ApiResult.success(productsService.getProdutsExceptUnique());
    }

    /**
     * 获取不在项目表中的供应商
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSuppliersExceptUnique", method = {RequestMethod.POST})
    public ApiResult getSuppliersExceptUnique(HttpServletRequest request) throws Exception {
        return ApiResult.success(supplierbaseinforService.getSuppliersExceptUnique());
    }

    /**
     * 新建
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ProjectsAndFollowUpRecord projectsAndFollowUpRecord, HttpServletRequest request) throws Exception {

        if (projectsAndFollowUpRecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //随机主键
        String id;

        //INSERT:PROJECTS
        Projects projects = projectsAndFollowUpRecord.getProjectsForm();

        id = UUID.randomUUID().toString();
        projects.setProjects_id(id);

        //存在Check
        if (!projectsSerivce.existCheck(projects)) {
            //唯一性Check
            if (!projectsSerivce.uniqueCheck(projects)) {
                projectsSerivce.insert(projects, tokenService.getToken(request));
            } else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //INSERT:FOLLOWUPRECORD
        List<FollowUpRecord> followUpRecordList = projectsAndFollowUpRecord.getFollowUpRecordList();
        for (FollowUpRecord followUpRecord : followUpRecordList) {

            id = UUID.randomUUID().toString();
            followUpRecord.setFollowuprecord_id(id);
            followUpRecord.setProducts_id(projects.getProducts_id());
            followUpRecord.setSupplier_id(projects.getSupplier_id());

            //存在Check
            if (!projectsSerivce.existCheck(followUpRecord)) {

                projectsSerivce.insert(followUpRecord, tokenService.getToken(request));
            } else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ProjectsAndFollowUpRecord projectsAndFollowUpRecord, HttpServletRequest request) throws Exception {

        if (projectsAndFollowUpRecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:PROJECTS
        Projects projects = projectsAndFollowUpRecord.getProjectsForm();
        //存在Check
        if (projectsSerivce.existCheck(projects)) {
            //唯一性Check
            if (!projectsSerivce.uniqueCheck(projects)) {
                //项目表-更新
                projectsSerivce.update(projects, tokenService.getToken(request));

                //记录表-新建/更新/删除
                List<FollowUpRecord> followUpRecordList = projectsAndFollowUpRecord.getFollowUpRecordList();
                //记录表-全删除
                if (followUpRecordList.isEmpty()) {
                    FollowUpRecord followUpRecord = new FollowUpRecord();
                    followUpRecord.setProducts_id(projects.getProducts_id());
                    followUpRecord.setSupplier_id(projects.getSupplier_id());
                    projectsSerivce.delete(followUpRecord, tokenService.getToken(request));
                } else {
                    Map<String, List<FollowUpRecord>> resultMap = new HashMap<>();
                    //获取记录表
                    List<FollowUpRecord> dbList = projectsSerivce.getFollowUpRecordList(projects);
                    //获取差异部分
                    resultMap = getDiffLst(followUpRecordList, dbList);
                    List<FollowUpRecord> diffList = resultMap.get("diff");
                    List<FollowUpRecord> sameList = resultMap.get("same");

                    //不同部分
                    if (!diffList.isEmpty()) {
                        //新建/删除
                        for (FollowUpRecord item : diffList) {
                            FollowUpRecord followUpRecord = item;

                            //删除
                            if (projectsSerivce.existCheck(followUpRecord)) {
                                followUpRecord.preUpdate(tokenService.getToken(request));
                                projectsSerivce.delete(followUpRecord, tokenService.getToken(request));
                            }
                            //新建
                            else {
                                String id = UUID.randomUUID().toString();
                                followUpRecord.setFollowuprecord_id(id);
                                followUpRecord.setProducts_id(projects.getProducts_id());
                                followUpRecord.setSupplier_id(projects.getSupplier_id());

                                followUpRecord.preInsert(tokenService.getToken(request));
                                projectsSerivce.insert(followUpRecord,tokenService.getToken(request));
                            }
                        }
                    }
                    //相同部分
                    if (!sameList.isEmpty()) {
                        //更新
                        for (FollowUpRecord item : sameList) {
                            FollowUpRecord followUpRecord = item;
                            followUpRecord.setProducts_id(projects.getProducts_id());
                            followUpRecord.setSupplier_id(projects.getSupplier_id());
                            followUpRecord.preUpdate(tokenService.getToken(request));
                            projectsSerivce.update(followUpRecord,tokenService.getToken(request));
                        }
                    }
                }
            } else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody ProjectsAndFollowUpRecord projectsAndFollowUpRecord, HttpServletRequest request) throws Exception {

        if (projectsAndFollowUpRecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //DELETE:PROJECTS
        Projects projects = projectsAndFollowUpRecord.getProjectsForm();
        //存在Check
        if (projectsSerivce.existCheck(projects)) {
            projectsSerivce.delete(projects, tokenService.getToken(request));

            //DELETE:FOLLOWUPRECORD
            List<FollowUpRecord> followUpRecordList = projectsAndFollowUpRecord.getFollowUpRecordList();
            for (FollowUpRecord followUpRecord : followUpRecordList) {

                followUpRecord.setProducts_id(projects.getProducts_id());
                followUpRecord.setSupplier_id(projects.getSupplier_id());

                //存在Check
                if (projectsSerivce.existCheck(followUpRecord)) {
                    projectsSerivce.delete(followUpRecord, tokenService.getToken(request));
                }
            }
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 比较list
     *
     * @param list1
     * @param list2
     * @return
     */
    private Map<String, List<FollowUpRecord>> getDiffLst(List<FollowUpRecord> list1, List<FollowUpRecord> list2) {

        List<FollowUpRecord> maxList = new ArrayList<>();
        List<FollowUpRecord> minList = new ArrayList<>();
        List<FollowUpRecord> diffList = new ArrayList<>();
        List<FollowUpRecord> sameList = new ArrayList<>();


        //区分大小
        if (list1.size() > list2.size()) {
            maxList = list1;
            minList = list2;
        } else {
            minList = list1;
            maxList = list2;
        }

        Map<FollowUpRecord, Integer> map = new HashMap<FollowUpRecord, Integer>(maxList.size());

        //大lst的追加
        for (FollowUpRecord item : maxList) {
            map.put(item, 1);
        }
        //小lst的追加
        for (FollowUpRecord followUpRecord : minList) {
            if (map.get(followUpRecord) != null) {
                map.put(followUpRecord, 2);
                continue;
            }
            diffList.add(followUpRecord);
        }

        //筛选出差异部分
        for (Map.Entry<FollowUpRecord, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diffList.add(entry.getKey());
            } else if (entry.getValue() == 2) {
                sameList.add(entry.getKey());
            }
        }


        Map<String, List<FollowUpRecord>> resultMap = new HashMap<>();

        resultMap.put("diff", diffList);
        resultMap.put("same", sameList);

        return resultMap;
    }
}
