package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.StaffWorkMonthInfoVo;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentalInsideService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalInsideMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentalInsideServiceImpl implements DepartmentalInsideService {

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private DepartmentalInsideMapper departmentalInsideMapper;

    @Override
    public void insert() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = 0;
        int monthlast = calendar.get(Calendar.MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month >= 1 && month <= 4) {
            if (day >= 10) {
                year = calendar.get(Calendar.YEAR);
            } else {
                year = calendar.get(Calendar.YEAR) - 1;
            }
        } else {
            year = calendar.get(Calendar.YEAR);
        }
        Calendar calnew = Calendar.getInstance();
        int yearnow = calnew.get(Calendar.YEAR);
        List<DepartmentVo> departmentVoList = new ArrayList<>();
        DepartmentVo departmentVo = new DepartmentVo();
        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree orgfu : orgs.getOrgs()) {
            for (OrgTree orgCenter : orgfu.getOrgs()) {
                if (!StringUtils.isNullOrEmpty(orgCenter.getEncoding())) {
                    departmentVo = new DepartmentVo();
                    departmentVo.setDepartmentId(orgCenter.get_id());
                    departmentVo.setDepartmentname(orgCenter.getCompanyname());
                    departmentVo.setDepartmentshortname(orgCenter.getCompanyshortname());
                    departmentVo.setDepartmentEncoding(orgCenter.getEncoding());
                    departmentVo.setDepartmentEn(orgCenter.getCompanyen());
                    departmentVo.setDepartmentType(orgCenter.getType());
                    departmentVo.setDepartmentUserid(orgCenter.getUser());
                    departmentVoList.add(departmentVo);
                } else {
                    for (OrgTree orgGroup : orgCenter.getOrgs()) {
                        departmentVo = new DepartmentVo();
                        departmentVo.setDepartmentId(orgGroup.get_id());
                        departmentVo.setDepartmentname(orgGroup.getCompanyname());
                        departmentVo.setDepartmentshortname(orgGroup.getCompanyshortname());
                        departmentVo.setDepartmentEncoding(orgGroup.getEncoding());
                        departmentVo.setDepartmentEn(orgGroup.getCompanyen());
                        departmentVo.setDepartmentType(orgGroup.getType());
                        departmentVo.setDepartmentUserid(orgGroup.getUser());
                        departmentVoList.add(departmentVo);
                    }
                }
            }
        }

        List<DepartmentalInsideBaseVo> departmentalInsideBaseVoList = departmentalInsideMapper.getBaseInfo(String.valueOf(year));
        List<String> userList = departmentalInsideBaseVoList.stream().map(DepartmentalInsideBaseVo::getNAME).collect(Collectors.toList());
        List<StaffWorkMonthInfoVo> staffWorkMonthInfoVoList = departmentalInsideMapper.getWorkInfo(String.valueOf(year)+ '-' + String.valueOf(month),userList);
        TokenModel tokenModel = new TokenModel();
        if (departmentVoList.size() > 0) {
            for (DepartmentVo dv : departmentVoList) {

            }
        }
    }
}
