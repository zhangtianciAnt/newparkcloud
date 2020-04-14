package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;

import javax.servlet.http.HttpServletRequest;

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

}
