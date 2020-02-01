package com.nt.service_PHINE.mapper;

import com.nt.dao_PHINE.Projectinfo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProjectinfoMapper extends MyMapper<Projectinfo> {

    // 平台项目画面获取项目列表
    List<ProjectListVo> getProjectInfoList();
}
