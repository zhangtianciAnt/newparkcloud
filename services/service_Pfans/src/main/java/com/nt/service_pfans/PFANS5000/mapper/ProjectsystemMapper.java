package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProjectsystemMapper extends MyMapper<Projectsystem> {
    //add_fjl_07/07 start  PL权限相关
    @Select("select position,exittime from projectsystem where name =  #{name} and position in ('PL','pl','Pl','pL')  order by exittime desc")
    List<Projectsystem> getNamePl(@Param("name") String name);

    @Select("select * from projectsystem")
    List<Projectsystem> getProsysList();
    //add_fjl_07/07 end  PL权限相关

}
