package com.nt.service_PHINE;

import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.dao_PHINE.Vo.UserAuthListVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: ProjectinfoService
 * @Description: 项目信息Service接口
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
public interface ProjectinfoService {

    // 平台项目画面获取项目列表
    List<ProjectListVo> getProjectInfoList(String ownerId);

    // 创建项目信息
    void saveProjectInfo(TokenModel tokenModel, Projectinfo projectinfo);

    // 保存项目分配的设备信息
    void saveResourcesInfo(TokenModel tokenModel, String projectid, String[] deviceidList);

    // 保存平台管理员分配的用户权限信息
    void saveUserAuthInfo(TokenModel tokenModel, String projectid, String[] useridList);

    // 删除用户权限及管理设备信息
    void delUserAuth(String projectid, String userid);

    // 查询projectid是否存在
    boolean selectProjectIdExist(String projectid);

    // 获取用户权限信息
    List<UserAuthListVo> getUserAuthList(String projectid);

    // 根据项目ID查询项目信息
    ProjectListVo getProjectInfo(String projectid);

    // 平台项目管理画面根据项目ID获取设备列表
    List<DeviceListVo> getDeviceIdByProjectId(String projectid);

    // 更新项目信息
    void updateProjectInfo(TokenModel tokenModel, Projectinfo projectinfo);
}
