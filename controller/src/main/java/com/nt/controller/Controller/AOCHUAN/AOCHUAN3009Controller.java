package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/AOCHUAN3009")
public class AOCHUAN3009Controller {

    @Autowired
    private ProjectsService projectsSerivce;

    /**
     * 获取projectschedule表数据
     */
    @RequestMapping(value = "/getProjectList", method = {RequestMethod.POST})
    public ApiResult getProjectList(HttpServletRequest request) throws Exception {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx" );
        Projects projects = new Projects();
        return ApiResult.success(projectsSerivce.getProjectList(projects));

    }
}
