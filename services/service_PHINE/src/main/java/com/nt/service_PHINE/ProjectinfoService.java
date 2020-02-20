package com.nt.service_PHINE;

import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.dao_PHINE.Vo.ProjectinfoVo;
import com.nt.dao_PHINE.Vo.UserAuthListVo;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.text.ParseException;
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
    List<ProjectListVo> getProjectInfoList(String companyId);

    // 创建项目信息
    ApiResult saveProjectInfo(TokenModel tokenModel, ProjectListVo projectListVo) throws ParseException;

    // 保存项目分配的设备信息
    ApiResult saveResourcesInfo(TokenModel tokenModel, String projectid, String[] deviceidList);

    // 保存平台管理员分配的用户权限信息
    ApiResult saveUserAuthInfo(TokenModel tokenModel, String projectid, String[] useridList);

    // 删除用户权限及管理设备信息
    void delUserAuth(String id);

    // 获取用户权限信息
    List<UserAuthListVo> getUserAuthList(String projectid);

    // 根据条件查询项目信息
    ProjectListVo getProjectInfo(String companyid, String projectid) throws ParseException;

    // 平台项目管理画面根据项目ID获取设备列表
    List<DeviceListVo> getDeviceIdByProjectId(String projectid);

    // 更新项目信息
    ApiResult updateProjectInfo(TokenModel tokenModel, ProjectListVo projectListVo) throws ParseException;

    // 获取项目信息包含项目相关人员
    ProjectinfoVo getProjectinfoById(String projectid);
}