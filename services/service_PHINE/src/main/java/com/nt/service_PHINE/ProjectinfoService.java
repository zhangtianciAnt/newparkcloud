package com.nt.service_PHINE;

import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import org.springframework.stereotype.Service;

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
@Service
public interface ProjectinfoService {

    // 平台项目画面获取项目列表
    List<ProjectListVo> getProjectInfoList(String ownerId);

    // 创建项目信息
    void saveProjectInfo(Projectinfo projectinfo);
}
