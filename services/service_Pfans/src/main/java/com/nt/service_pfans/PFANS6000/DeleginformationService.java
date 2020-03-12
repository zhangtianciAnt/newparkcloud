package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS5000.Projectsystem;

import java.util.List;

public interface DeleginformationService {

    //取出项目体制数据与项目数据，再对数据进行计算存入到月份表中
    public void createProjectsystem(Projectsystem projectsystem) throws Exception;

}
