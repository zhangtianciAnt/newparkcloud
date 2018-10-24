package com.nt.controller.Controller;

import com.nt.controller.Start;
import com.nt.dao_Org.OrgTree;
import com.nt.service_Org.OrgTreeService;
import com.nt.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/OrgTree")
public class OrgTreeController {

    @Autowired
    private OrgTreeService orgTreeService;

    private static Logger log = LoggerFactory.getLogger(Start.class);

    /**
     * @方法名：get
     * @描述：获取组织机构树形结构
     * @创建日期：2018/10/24
     * @作者：SKAIXX
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        OrgTree orgTree = new OrgTree();
        orgTree.setTenantid(RequestUtils.CurrentTenantId(request));
        orgTree = RequestUtils.CurrentPageOwnerList(request, orgTree);
        return ApiResult.success(orgTreeService.get(orgTree));
    }

    /**
     * @方法名：save
     * @描述：插入或更新组织机构树形结构
     * @创建日期：2018/10/24
     * @作者：SKAIXX
     * @参数：[orgTree, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ApiResult save(@RequestBody OrgTree orgTree, HttpServletRequest request) throws Exception {
        if (orgTree == null || StringUtils.isEmpty(orgTree) || orgTree.getOrgtrees() == null) {
            log.warn(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        orgTreeService.save(orgTree, request);
        return ApiResult.success();
    }
}
