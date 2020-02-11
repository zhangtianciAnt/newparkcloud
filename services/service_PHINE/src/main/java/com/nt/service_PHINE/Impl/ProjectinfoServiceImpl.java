package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Project2device;
import com.nt.dao_PHINE.Project2user;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.dao_PHINE.Vo.ProjectinfoVo;
import com.nt.dao_PHINE.Vo.UserAuthListVo;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.service_PHINE.mapper.Project2deviceMapper;
import com.nt.service_PHINE.mapper.Project2userMapper;
import com.nt.service_PHINE.mapper.ProjectinfoMapper;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.UUID;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: ProjectinfoServiceImpl
 * @Description: 项目信息实现类
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@Service
public class ProjectinfoServiceImpl implements ProjectinfoService {

    @Autowired
    private ProjectinfoMapper projectinfoMapper;

    @Autowired
    private Project2deviceMapper project2deviceMapper;

    @Autowired
    private Project2userMapper project2userMapper;

    /**
     * @return List<ProjectListVo>平台项目信息列表
     * @Method getProjectInfoList
     * @Author SKAIXX
     * @Description 平台项目画面获取项目列表
     * @Date 2020/1/30 15:27
     * @Param TODO:权限部分暂未实装
     **/
    @Override
    public List<ProjectListVo> getProjectInfoList(String ownerId) {
        return projectinfoMapper.getProjectInfoList();
    }

    /**
     * @Method saveProjectInfo
     * @Author MYT
     * @Description 创建项目信息
     * @Date 2020/1/31 15:27
     * @Param projectinfo 页面输入信息
     * TODO:主键ID不知具体插入什么数据，暂时插入UUID
     **/
    @Override
    public ApiResult saveProjectInfo(TokenModel tokenModel, Projectinfo projectinfo) {
        ProjectListVo projectInfo = getProjectInfo(projectinfo.getCompanyid(),projectinfo.getProjectid());
        if(projectInfo != null){
            return ApiResult.fail("项目ID已经存在，请重新输入新的项目ID！");
        }else{
            String id = UUID.randomUUID().toString();
            projectinfo.setId(id);
            projectinfo.setCreateby(tokenModel.getUserId());
            projectinfo.setTenantid(tokenModel.getTenantId());
            projectinfo.setCreateon(new java.sql.Date(System.currentTimeMillis()));
            projectinfoMapper.insert(projectinfo);
            return ApiResult.success(MsgConstants.INFO_01, projectinfo.getId());
        }
    }

    /**
     * @Method updateProjectInfo
     * @Author MYT
     * @Description 根据项目ID更新项目信息
     * @Date 2020/2/6 15:27
     * @Param projectinfo 项目信息
     **/
    @Override
    public ApiResult updateProjectInfo(TokenModel tokenModel, Projectinfo projectinfo) {
        ProjectListVo projectInfo = getProjectInfo("",projectinfo.getId());
        if(projectInfo == null){
            return ApiResult.fail("项目ID不存在，更新数据失败！");
        }else{
            projectinfo.setModifyby(tokenModel.getUserId());
            projectinfo.setTenantid(tokenModel.getTenantId());
            projectinfo.setModifyon(new java.sql.Date(System.currentTimeMillis()));
            projectinfoMapper.updateProjectInfo(projectinfo);
            return ApiResult.success(MsgConstants.INFO_01);
        }
    }

    /**
     * @Method saveResourcesInfo
     * @Author MYT
     * @Description 保存平台管理员分配的资源信息
     * @Date 2020/1/31 15:27
     * @Param projectinfo 页面输入信息
     * TODO:主键ID不知具体插入什么数据，暂时插入UUID
     **/
    @Override
    public ApiResult saveResourcesInfo(TokenModel tokenModel, String projectid, String[] deviceidList) {
        ProjectListVo projectInfo = getProjectInfo("",projectid);
        if(projectInfo == null){
            return ApiResult.fail("项目ID不存在，请先创建项目后再添加设备信息！");
        }else{
            // 插入新的数据
            for (String deviceid : deviceidList) {
                Project2device project2device = new Project2device();
                project2device.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                project2device.setDeviceid(deviceid);
                project2device.setProjectid(projectid);
                project2device.setCreateby(tokenModel.getUserId());
                project2device.setTenantid(tokenModel.getTenantId());
                project2device.setCreateon(new java.sql.Date(System.currentTimeMillis()));
                project2deviceMapper.insert(project2device);
            }
            return ApiResult.success(MsgConstants.INFO_01);
        }
    }

    /**
     * @Method saveUserAuthInfo
     * @Author MYT
     * @Description 保存平台管理员分配的用户权限信息
     * @Date 2020/1/31 15:27
     * @Param projectinfo 页面输入信息
     * TODO:主键ID不知具体插入什么数据，暂时插入UUID
     **/
    @Override
    public ApiResult saveUserAuthInfo(TokenModel tokenModel, String projectid, String[] useridList) {
        ProjectListVo projectInfo = getProjectInfo("",projectid);
        if(projectInfo == null){
            return ApiResult.fail("项目ID不存在，请先创建项目后再添加用户权限信息！");
        }else{
            // 插入新的数据
            for (String userid : useridList) {
                Project2user project2user = new Project2user();
                project2user.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                project2user.setProjectid(projectid);
                project2user.setCreateby(tokenModel.getUserId());
                project2user.setTenantid(tokenModel.getTenantId());
                project2user.setTenantid(tokenModel.getTenantId());
                project2user.setCreateon(new java.sql.Date(System.currentTimeMillis()));
                project2user.setUserid(userid);
                project2userMapper.insert(project2user);
            }
            return ApiResult.success(MsgConstants.INFO_01);
        }
    }

    /**
     * @Method delUserAuth
     * @Author MYT
     * @Description 删除用户权限及设备信息
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID;userid 用户ID
     * TODO:
     **/
    @Override
    public void delUserAuth(String projectid, String userid) {
        // 删除关系表中既有用户数据
        Project2user deleteCondition = new Project2user();
        deleteCondition.setProjectid(projectid);
        deleteCondition.setUserid(userid);
        project2userMapper.delete(deleteCondition);
    }

    /**
     * @return ProjectListVo 项目信息
     * @Method getDeviceIdByProjectId
     * @Author MYT
     * @Description 根据项目ID查询设备ID列表
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID
     **/
    @Override
    public List<UserAuthListVo> getUserAuthList(String projectid) {
        Project2user selectCondition = new Project2user();
        selectCondition.setProjectid(projectid);
        List<Project2user> project2usersList = project2userMapper.select(selectCondition);
        List<UserAuthListVo> userAuthListVoList = new ArrayList<UserAuthListVo>();
        for (Project2user userInfo : project2usersList) {
            UserAuthListVo userAuthListVo = new UserAuthListVo();
            userAuthListVo.setUserid(userInfo.getUserid());
            userAuthListVo.setInfoauth("1");
            userAuthListVo.setFileauth("1");
            userAuthListVo.setAuthmanage("1");
            userAuthListVo.setMachineauth("ALL");
            userAuthListVoList.add(userAuthListVo);
        }
        return userAuthListVoList;
    }

    /**
     * @return ProjectListVo 项目信息
     * @Method getDeviceIdByProjectId
     * @Author MYT
     * @Description 根据项目ID查询设备ID列表
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID
     **/
    @Override
    public ProjectListVo getProjectInfo(String companyid, String projectid) {
        ProjectListVo projectListVo;
        // 查询所属公司中项目ID是否存在
        if (StringUtil.isNotEmpty(companyid)) {
            projectListVo = projectinfoMapper.getProjectInfo(companyid,projectid);
        } else {
            // 根据主键查询项目是否存在
            projectListVo = projectinfoMapper.getProjectInfo("",projectid);
        }
        return projectListVo;
    }

    /**
     * @return List<DeviceListVo> 设备ID列表
     * @Method getDeviceIdByProjectId
     * @Author MYT
     * @Description 根据项目ID查询设备ID列表
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID
     **/
    @Override
    public List<DeviceListVo> getDeviceIdByProjectId(String projectid) {
        return project2deviceMapper.getDeviceIdByProjectId(projectid);
    }

    /**
     * @return ProjectinfoVo 项目信息
     * @Method getProjectinfoById
     * @Author MYT
     * @Description 根据项目ID查询项目信息
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID
     **/
    @Override
    public ProjectinfoVo getProjectinfoById(String projectid) {
        return projectinfoMapper.getProjectinfoById(projectid);
    }

}
