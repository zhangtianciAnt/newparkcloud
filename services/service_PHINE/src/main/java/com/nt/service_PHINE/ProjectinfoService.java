package com.nt.service_PHINE;

import com.nt.dao_PHINE.Project2device;
import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.ProjectListVo;

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
    void saveProjectInfo(Projectinfo projectinfo);

    // 删除用户权限及管理设备信息
    void delUserAuth(String userid);

    // 查询projectid是否存在
    boolean selectProjectIdExist(String projectid);

    // 平台项目管理画面根据项目ID获取设备列表
    List<DeviceListVo> getDeviceIdByProjectId(String projectid);
}
