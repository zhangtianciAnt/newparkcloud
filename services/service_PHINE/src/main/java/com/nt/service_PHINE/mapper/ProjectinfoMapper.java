package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.dao_PHINE.Vo.ProjectinfoVo;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProjectinfoMapper extends MyMapper<Projectinfo> {
    // 平台项目画面获取项目列表
    List<ProjectListVo> getProjectInfoList();

    // 更新项目信息
    void updateProjectInfo(Projectinfo projectinfo);

    // 根据条件查询项目信息
    ProjectListVo getProjectInfo(String companyid,String projectid);

    // 根据条件查询项目信息
    ProjectinfoVo getProjectinfoById(String projectid);
}
