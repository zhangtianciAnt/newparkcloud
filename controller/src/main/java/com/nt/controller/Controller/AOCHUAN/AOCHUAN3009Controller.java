package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.ProjectsAndFollowUpRecord;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class AOCHUAN3009Controller {

    @Autowired
    private ProjectsService projectsSerivce;

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
    public ApiResult getFollowUpRecordList(@RequestBody FollowUpRecord followUpRecord, HttpServletRequest request) throws Exception {
        return ApiResult.success(projectsSerivce.getFollowUpRecordList(followUpRecord));
    }

    /**
     * 新建
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ProjectsAndFollowUpRecord projectsAndFollowUpRecord, HttpServletRequest request) throws Exception {

        if (projectsAndFollowUpRecord == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //随机主键
        String id;

        //INSERT:PROJECTS
        Projects projects = projectsAndFollowUpRecord.getProjectsForm();

        id= UUID.randomUUID().toString();
        projects.setProjects_id(id);

        //存在Check
        if (!projectsSerivce.existCheck(projects)) {
            //唯一性Check
            if(! projectsSerivce.uniqueCheck(projects)){
            projectsSerivce.insert(projects, tokenService.getToken(request));
            }
            else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //INSERT:FOLLOWUPRECORD
        List<FollowUpRecord> followUpRecordList = projectsAndFollowUpRecord.getFollowUpRecordList();
        for(FollowUpRecord followUpRecord:followUpRecordList){

            id= UUID.randomUUID().toString();
            followUpRecord.setFollowuprecord_id(id);
            followUpRecord.setProducts_id(projects.getProducts_id());
            followUpRecord.setSupplier_id(projects.getSupplier_id());

            //存在Check
            if (!projectsSerivce.existCheck(followUpRecord)) {

                projectsSerivce.insert(followUpRecord,tokenService.getToken(request));
            }else{
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

        if (projectsAndFollowUpRecord == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:PROJECTS
        Projects projects = projectsAndFollowUpRecord.getProjectsForm();
        //存在Check
        if (projectsSerivce.existCheck(projects)) {
            //唯一性Check
            if(! projectsSerivce.uniqueCheck(projects)) {
                projectsSerivce.update(projects, tokenService.getToken(request));

            //UPDATE:FOLLOWUPRECORD
            List<FollowUpRecord> followUpRecordList = projectsAndFollowUpRecord.getFollowUpRecordList();
            for(FollowUpRecord followUpRecord:followUpRecordList){

                followUpRecord.setProducts_id(projects.getProducts_id());
                followUpRecord.setSupplier_id(projects.getSupplier_id());

                //存在Check
                if (projectsSerivce.existCheck(followUpRecord)) {
                    projectsSerivce.update(followUpRecord, tokenService.getToken(request));
                }else{
                    String id= UUID.randomUUID().toString();
                    followUpRecord.setFollowuprecord_id(id);
                    projectsSerivce.insert(followUpRecord,tokenService.getToken(request));
                }
            }
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
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

        if (projectsAndFollowUpRecord == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //DELETE:PROJECTS
        Projects projects = projectsAndFollowUpRecord.getProjectsForm();
        //存在Check
        if (projectsSerivce.existCheck(projects)) {
            projectsSerivce.delete(projects, tokenService.getToken(request));

            //DELETE:FOLLOWUPRECORD
            List<FollowUpRecord> followUpRecordList = projectsAndFollowUpRecord.getFollowUpRecordList();
            for(FollowUpRecord followUpRecord:followUpRecordList){

                followUpRecord.setProducts_id(projects.getProducts_id());
                followUpRecord.setSupplier_id(projects.getSupplier_id());

                //存在Check
                if (projectsSerivce.existCheck(followUpRecord)) {
                    projectsSerivce.delete(followUpRecord, tokenService.getToken(request));
                }
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        return ApiResult.success();
    }
}
