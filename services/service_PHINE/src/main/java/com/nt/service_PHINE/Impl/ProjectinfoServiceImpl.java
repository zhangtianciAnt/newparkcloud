package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.service_PHINE.mapper.DeviceinfoMapper;
import com.nt.service_PHINE.mapper.ProjectinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private DeviceinfoMapper deviceinfoMapper;

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
     * @Description 创建项目模块
     * @Date 2020/1/31 15:27
     * @Param TODO:芯片类型未封装
     **/
    @Override
    public void saveProjectInfo(Projectinfo projectinfo) {
        projectinfoMapper.insert(projectinfo);
    }

    /**
     * @Method delUserAuth
     * @Author MYT
     * @Description 删除用户权限及设备信息
     * @Date 2020/1/31 15:27
     * @Param TODO:
     **/
    @Override
    public void delUserAuth(String userid) {

    }
}
