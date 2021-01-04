package com.nt.controller.Controller;

import com.nt.dao_Org.OrgTree;
import com.nt.service_Org.OrgTreeService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
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
        //权限
//        orgTree.setTenantid(tokenModel.getTenantId());
//        orgTree.setOwners(tokenModel.getOwnerList());
//        orgTree.setIds(tokenModel.getIdList());
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
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        if(orgTree.getCreateby() == null || orgTree.getCreateon() == null){
            orgTree.preInsert(tokenModel);
        }
        orgTree.preUpdate(tokenModel);
        //新建组织架构(Status为1是不生效)
        if(orgTree.getType().equals("0")){
            OrgTree orgTreeold = orgTreeService.getTreeYears(orgTree.getYears(),"0");
            if(orgTreeold != null){
                return ApiResult.fail("新组织已存在");
            }
            orgTree.setStatus("1");
            orgTree.setType("1");
        }
        orgTreeService.save(orgTree);
        return ApiResult.success();
    }

    /**
     * @方法名：getById
     * @描述：获取当前组织机构树形结构
     * @创建日期：2018/12/03
     * @作者：ZHANGYING
     * @参数：[id, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getById", method = {RequestMethod.GET})
    public ApiResult getById(String id, HttpServletRequest request) throws Exception {
        OrgTree orgTree = new OrgTree();
//        TokenModel tokenModel = tokenService.getToken(request);
//        orgTree.setTenantid(tokenModel.getTenantId());
//        orgTree.setOwners(tokenModel.getOwnerList());
        orgTree.set_id(id);
        return ApiResult.success(orgTreeService.getById(orgTree));
    }

    /**
     * @方法名：getTreeYears
     * @描述：获取历史组织架构
     * @创建日期：2020/12/30
     * @作者：GAOBINGBING
     * @参数：[id, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getTreeYears", method = {RequestMethod.GET})
    public ApiResult getTreeYears(String Years, String type, HttpServletRequest request) throws Exception {
        return ApiResult.success(orgTreeService.getTreeYears(Years,type));
    }
}
