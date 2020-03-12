package com.nt.service_pfans.PFANS6000.Impl;


import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.service_pfans.PFANS5000.mapper.ProjectsystemMapper;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DelegainformationServiceImpl implements DeleginformationService {

    @Autowired
    private ProjectsystemMapper projectsystemMapper;


    @Override
    public void createProjectsystem(Projectsystem projectsystem) throws Exception{
        //工具
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //获取当前时间
        Date thisDate_d = new Date();
        String thisDate_s = thisDate_d.toString();
        String thisYear_s = thisDate_s.substring(0,4);
        String initial_s_01 = "0401";
        int thatYear_i = Integer.parseInt(thisYear_s) + 1;
        String thatYear_s = String.valueOf(thatYear_i);
        String initial_s_02 = "0331";
        //今年四月一号
        String aprilFirst_s = thisYear_s + initial_s_01;
        Date aprilFirst_d = sdf.parse(aprilFirst_s);
        //明年3月31日
        String marchLast_s = thatYear_s + initial_s_02;
        Date marchLast_d = sdf.parse(marchLast_s);
        //取出项目体制当中表的数据
        List<Projectsystem> projectsystemListObtain = projectsystemMapper.select(projectsystem);
        //对存入集合的数据进行操作
        for(int i = 0; i < projectsystemListObtain.size(); i ++){
            //社内外协区分， 1为外协
            if(projectsystemListObtain.get(i).getType().equals(1)){
                //退场时间不为空
                if(projectsystemListObtain.get(i).getExittime() != null){
                    //获取该员工的入场时间
                    Date admissiontime_d = projectsystemListObtain.get(i).getAdmissiontime();
                    //获取员工的退场时间
                    Date exitime_d = projectsystemListObtain.get(i).getExittime();
                    if(aprilFirst_d.before(admissiontime_d) && exitime_d.before(marchLast_d)){
                        //本事业年度都在此工作

                    }
                }
            }
        }

    }
}
