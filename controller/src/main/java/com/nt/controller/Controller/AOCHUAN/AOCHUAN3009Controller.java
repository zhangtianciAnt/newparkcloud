package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.ProjectsAndFollowUpRecord;
import com.nt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class AOCHUAN3009Controller {

    @Autowired
    private ProjectsService projectsSerivce;

    /**
     * 获取projectschedule表数据
     */
    @RequestMapping(value = "/getProjectList", method = {RequestMethod.POST})
    public ApiResult getProjectList(HttpServletRequest request) throws Exception {
        Projects projects = new Projects();
        return ApiResult.success(projectsSerivce.getProjectList(projects));
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

        Projects projects = projectsAndFollowUpRecord.getProjectsForm();
        List<FollowUpRecord> followUpRecordList = projectsAndFollowUpRecord.getFollowUpRecordList();

        //INSERT:PROJECTS
        projectsSerivce.insert(projects);
        //INSERT:FOLLOWUPRECORD
        for(FollowUpRecord followUpRecord:followUpRecordList){
            followUpRecord.setProduct_nm(projects.getProduct_nm());
            followUpRecord.setProvider(projects.getProvider());
            projectsSerivce.insert(followUpRecord);
        }
        return ApiResult.success();
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Projects projects ,@RequestBody FollowUpRecord followUpRecord, HttpServletRequest request) throws Exception {

        if(projects == null || followUpRecord == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        //UPDATE:PROJECTS
        projectsSerivce.update(projects);
        //UPDATE:FOLLOWUPRECORD
        projectsSerivce.update(followUpRecord);
        return ApiResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody Projects projects, HttpServletRequest request) throws Exception {

        if(projects == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        String product_nm = projects.getProduct_nm();
        String provider = projects.getProvider();

        //DELETE:PROJECTS & FOLLOWUPRECORD
        projectsSerivce.delete(product_nm,provider);
        return ApiResult.success();
    }
}
