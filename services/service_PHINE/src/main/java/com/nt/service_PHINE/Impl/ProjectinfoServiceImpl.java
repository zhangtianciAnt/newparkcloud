package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Project2device;
import com.nt.dao_PHINE.Project2user;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.dao_PHINE.Vo.UserAuthListVo;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.service_PHINE.mapper.Project2deviceMapper;
import com.nt.service_PHINE.mapper.Project2userMapper;
import com.nt.service_PHINE.mapper.ProjectinfoMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void saveProjectInfo(TokenModel tokenModel, Projectinfo projectinfo) {
        projectinfo.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        projectinfo.setCreateby(tokenModel.getUserId());
        projectinfo.setTenantid(tokenModel.getTenantId());
        projectinfo.setCreateon(new java.sql.Date(System.currentTimeMillis()));
        projectinfoMapper.insert(projectinfo);
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
    public void saveResourcesInfo(TokenModel tokenModel, String projectid, String[] deviceidList) {
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
    public void saveUserAuthInfo(TokenModel tokenModel, String projectid, String[] useridList) {
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
     * @return false:不存在;true:存在
     * @Method selectProjectIdExist
     * @Author MYT
     * @Description 查询projectid是否存在
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID
     **/
    @Override
    public boolean selectProjectIdExist(String projectid) {
        Projectinfo projectinfo = new Projectinfo();
        projectinfo.setProjectid(projectid);
        List<Projectinfo> projectList = projectinfoMapper.select(projectinfo);
        if (projectList == null || projectList.size() == 0) {
            return false;
        } else {
            return true;
        }
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
        for(Project2user userInfo : project2usersList){
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
    public ProjectListVo getProjectInfo(String projectid) {
        return projectinfoMapper.getProjectInfo(projectid);
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
     * @Method updateProjectInfo
     * @Author MYT
     * @Description 根据项目ID更新项目信息
     * @Date 2020/2/6 15:27
     * @Param projectinfo 项目信息
     **/
    @Override
    public void updateProjectInfo(TokenModel tokenModel, Projectinfo projectinfo) {
        projectinfo.setModifyby(tokenModel.getUserId());
        projectinfo.setTenantid(tokenModel.getTenantId());
        projectinfo.setModifyon(new java.sql.Date(System.currentTimeMillis()));
        projectinfoMapper.updateProjectInfo(projectinfo);
    }
}
