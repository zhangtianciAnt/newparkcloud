package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.service_PHINE.mapper.ProjectinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: ProjectinfoServiceImpl
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@Service
public class ProjectinfoServiceImpl implements ProjectinfoService {

    @Autowired
    private ProjectinfoMapper projectinfoMapper;

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
}
