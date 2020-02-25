package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.utils.MyMapper;

import java.util.List;

public interface CompanyProjectsMapper extends MyMapper<CompanyProjects> {

    List<CompanyProjectsVo2> getList();
}
