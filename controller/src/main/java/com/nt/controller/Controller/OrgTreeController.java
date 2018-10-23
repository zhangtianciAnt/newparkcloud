package com.nt.controller.Controller;

import com.nt.dao_Org.OrgTree;
import com.nt.service_Org.OrgTreeService;
import com.nt.utils.ApiResult;
import com.nt.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/OrgTree")
public class OrgTreeController {

    @Autowired
    private OrgTreeService orgTreeService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        OrgTree orgTree = new OrgTree();
        orgTree.setTenantid(RequestUtils.CurrentTenantId(request));
        orgTree = RequestUtils.CurrentPageOwnerList(request, orgTree);
        return ApiResult.success(orgTreeService.get(orgTree));
    }
}
