package com.nt.controller.Controller;

import com.nt.controller.Start;
import com.nt.dao_Org.OrgTree;
import com.nt.service_Org.OrgTreeService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
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

    @Autowired
    private TokenService tokenService;

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
        TokenModel tokenModel = tokenService.getToken(request);
        orgTree.setTenantid(tokenModel.getTenantId());
        orgTree.setOwners(tokenModel.getOwnerList());
        orgTree.setIds(tokenModel.getIdList());
//        orgTree.setTenantid(RequestUtils.CurrentTenantId(request));
//        orgTree = RequestUtils.CurrentPageOwnerList(request, orgTree);
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
        if (orgTree == null || StringUtils.isEmpty(orgTree)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        if(orgTree.getCreateby() == null || orgTree.getCreateon() == null){
            orgTree.preInsert(tokenModel);
        }
        orgTree.preUpdate(tokenModel);
        orgTreeService.save(orgTree);
        return ApiResult.success();
    }
}
