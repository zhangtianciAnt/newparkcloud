package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.VO.*;
import com.nt.dao_Org.OrgTree;
import com.nt.service_BASF.StartprogramTrainServices;
import com.nt.service_BASF.mapper.TrainjoinlistMapper;
import com.nt.service_Org.OrgTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: StartprogramTrainServicesImpl
 * @Author: 王哲
 * @Description: 强制/非强制率接口实现类
 * @Date: 2020/4/22 11:07
 * @Version: 1.0
 */
@Service
public class StartprogramTrainServicesImpl implements StartprogramTrainServices {
    @Autowired
    private TrainjoinlistMapper trainjoinlistMapper;
    @Autowired
    private OrgTreeService orgTreeService;

    //获取部门名和通过状态（强制）
    @Override
    public List<StartprogramTrainVo> getDeptThrough(String year) throws Exception {
        List<StartprogramTrainVo> infoList = new ArrayList<>();

        List<TrainjoinlistVo> list = trainjoinlistMapper.selectDeptThrough(year);
        List<TrainjoinlistVo> allList = trainjoinlistMapper.selectAllDeptThrough(year);
        OrgTree orgTree = new OrgTree();
        List<OrgTree> orglist =new ArrayList<>();
        String type = "";
        for (int i = 0; i < list.size(); i++) {
            StartprogramTrainVo startprogramTrainVo = new StartprogramTrainVo();
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);
            type = numberFormat.format((float) list.get(i).getCount() / (float) allList.get(i).getCount() * 100);
            String departmentid = list.get(i).getDepartmentid();
            orglist = orgTreeService.getById(orgTree);
            String name =getDepartmentname(orglist,departmentid);
            if(name != null && name.length() !=0)
            {
                startprogramTrainVo.setDepartment(name);
            }
            else
            {
                continue;
            }
            startprogramTrainVo.setThroughtype(type);
            infoList.add(startprogramTrainVo);
        }
        return infoList;
    }


    @Override
    //获取部门名和通过状态（非强制）
    public List<StartprogramTrainVo> getUnDeptThrough(String year) throws Exception {
        List<StartprogramTrainVo> infoList = new ArrayList<>();

        List<TrainjoinlistVo> list = trainjoinlistMapper.selectUnDeptThrough(year);
        List<TrainjoinlistVo> allList = trainjoinlistMapper.selectUnAllDeptThrough(year);
        OrgTree orgTree = new OrgTree();
        List<OrgTree> orglist =new ArrayList<>();
        String type = "";
        for (int i = 0; i < list.size(); i++) {
            StartprogramTrainVo startprogramTrainVo = new StartprogramTrainVo();
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);
            type = numberFormat.format((float) list.get(i).getCount() / (float) allList.get(i).getCount() * 100);
            String departmentid = list.get(i).getDepartmentid();
            orglist = orgTreeService.getById(orgTree);
            String name =getDepartmentname(orglist,departmentid);
            if(name != null && name.length() !=0)
            {
                startprogramTrainVo.setDepartment(name);
            }
            else
            {
                continue;
            }
            startprogramTrainVo.setThroughtype(type);
            infoList.add(startprogramTrainVo);
        }
        return infoList;
    }

    //通过部门ID获取部门名
    public String getDepartmentname (List<OrgTree> orgs,String departmentid) {

        String depname = "";
        for(int j = 0;j<orgs.size();j++)
        {
            if(orgs.get(j).get_id() !=null)
            {
                if(orgs.get(j).get_id().equals(departmentid))
                {
                    depname =  orgs.get(j).getDepartmentname();
                    break;
                }
                else
                {
                    if(orgs.get(j).getOrgs()!= null && orgs.get(j).getOrgs().size()>0)
                    {
                        depname = getDepartmentname(orgs.get(j).getOrgs(),departmentid);
                    }
                }
            }
            else
            {
                if(orgs.get(j).getOrgs()!= null && orgs.get(j).getOrgs().size()>0)
                {
                    depname = getDepartmentname(orgs.get(j).getOrgs(),departmentid);
                }
            }

        }
        return depname;
    }
}
