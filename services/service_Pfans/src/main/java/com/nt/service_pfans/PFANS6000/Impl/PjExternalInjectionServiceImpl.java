package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS6000.PjExternalInjection;
import com.nt.dao_Pfans.PFANS6000.Vo.PjExternalInjectionVo;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS6000.PjExternalInjectionService;
import com.nt.service_pfans.PFANS6000.mapper.PjExternalInjectionMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component("PjExternalInjection")
@Service
@Transactional(rollbackFor = Exception.class)
public class PjExternalInjectionServiceImpl implements PjExternalInjectionService {

    @Autowired
    private PjExternalInjectionMapper pjExternalInjectionMapper;

    @Autowired
    private OrgTreeService orgTreeService;

    //pj别外注费统计定时任务
    //每月日凌晨点
    @Scheduled(cron = "0 10 0 10 * ?")
    public void saveTableinfo() throws Exception {

        //获取当前系统中有效的部门，按照预算编码统计
        OrgTree orgs = orgTreeService.get(new OrgTree());
        DepartmentVo departmentVo = new DepartmentVo();
        List<DepartmentVo> departmentVoList = new ArrayList<>();
//副总
        for (OrgTree orgfu : orgs.getOrgs()) {
            //Center
            for (OrgTree orgCenter : orgfu.getOrgs()) {
                if(!StringUtils.isNullOrEmpty(orgCenter.getEncoding()))
                {
                    departmentVo = new DepartmentVo();
                    departmentVo.setDepartmentId(orgCenter.get_id());
                    departmentVo.setDepartmentname(orgCenter.getCompanyname());
                    departmentVo.setDepartmentshortname(orgCenter.getCompanyshortname());
                    departmentVo.setDepartmentEncoding(orgCenter.getEncoding());
                    departmentVo.setDepartmentEn(orgCenter.getCompanyen());
                    departmentVo.setDepartmentType(orgCenter.getType());
                    departmentVo.setDepartmentUserid(orgCenter.getUser());
                    departmentVoList.add(departmentVo);
                }
                else
                {
                    for(OrgTree orgGroup : orgCenter.getOrgs())
                    {
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


        TokenModel tokenModel = new TokenModel();
        SimpleDateFormat sfYM = new SimpleDateFormat("yyyyMM");
        Calendar lastMonthDate = Calendar.getInstance();
        lastMonthDate.add(Calendar.MONTH, -1);
        String year = sfYM.format(lastMonthDate.getTime());
        for (DepartmentVo departvo : departmentVoList) {
            List<PjExternalInjectionVo> pjlistVo = pjExternalInjectionMapper.getThemeCompany(year, departvo.getDepartmentId());
            PjExternalInjection pjExternal = new PjExternalInjection();
            List<PjExternalInjection> insertpjExternal = new ArrayList<>();
            List<PjExternalInjection> updatepjExternal = new ArrayList<>();
            if (pjlistVo.size() > 0) {
                for (PjExternalInjectionVo pjExternalInjectionVo : pjlistVo) {
                    pjExternal = new PjExternalInjection();
                    pjExternal.setPjexternalinjection_id(UUID.randomUUID().toString());
                    pjExternal.setYears(year.substring(0, 4));
                    pjExternal.setGroup_id(pjExternalInjectionVo.getGroup_id());
                    pjExternal.setThemeinfor_id(pjExternalInjectionVo.getThemeinfor_id());
                    pjExternal.setThemename(pjExternalInjectionVo.getThemename());
                    pjExternal.setDivide(pjExternalInjectionVo.getDivide());
                    pjExternal.setToolsorgs(pjExternalInjectionVo.getToolsorgs());
                    pjExternal.setCompanyprojects_id(pjExternalInjectionVo.getCompanyprojects_id());
                    pjExternal.setProject_name(pjExternalInjectionVo.getProject_name());
                    pjExternal.setCompany(pjExternalInjectionVo.getCompany());
                    pjExternal.setApril("0.00");
                    pjExternal.setMay("0.00");
                    pjExternal.setJune("0.00");
                    pjExternal.setJuly("0.00");
                    pjExternal.setAugust("0.00");
                    pjExternal.setSeptember("0.00");
                    pjExternal.setOctober("0.00");
                    pjExternal.setNovember("0.00");
                    pjExternal.setDecember("0.00");
                    pjExternal.setJanuary("0.00");
                    pjExternal.setFebruary("0.00");
                    pjExternal.setMarch("0.00");
                    if (pjExternalInjectionVo.getMoney() != null) {
                        if (year.substring(4, 6).equals("04")) {
                            pjExternal.setApril(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("05")) {
                            pjExternal.setMay(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("06")) {
                            pjExternal.setJune(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("07")) {
                            pjExternal.setJuly(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("08")) {
                            pjExternal.setAugust(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("09")) {
                            pjExternal.setSeptember(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("10")) {
                            pjExternal.setOctober(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("11")) {
                            pjExternal.setNovember(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("12")) {
                            pjExternal.setDecember(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("01")) {
                            pjExternal.setJanuary(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("02")) {
                            pjExternal.setFebruary(pjExternalInjectionVo.getMoney());
                        } else if (year.substring(4, 6).equals("03")) {
                            pjExternal.setMarch(pjExternalInjectionVo.getMoney());
                        }
                    }

                    PjExternalInjection injection = new PjExternalInjection();
                    injection.setYears(year.substring(0, 4));
                    injection.setCompanyprojects_id(pjExternalInjectionVo.getCompanyprojects_id());
                    injection.setCompany(pjExternalInjectionVo.getCompany());
                    List<PjExternalInjection> injectionList = pjExternalInjectionMapper.select(injection);
                    if (injectionList.size() > 0) {
                        pjExternal.setPjexternalinjection_id(injectionList.get(0).getPjexternalinjection_id());
                        pjExternal.preUpdate(tokenModel);
                        pjExternal.setApril(injectionList.get(0).getApril());
                        pjExternal.setMay(injectionList.get(0).getMay());
                        pjExternal.setJune(injectionList.get(0).getJune());
                        pjExternal.setJuly(injectionList.get(0).getJuly());
                        pjExternal.setAugust(injectionList.get(0).getAugust());
                        pjExternal.setSeptember(injectionList.get(0).getSeptember());
                        pjExternal.setOctober(injectionList.get(0).getOctober());
                        pjExternal.setNovember(injectionList.get(0).getNovember());
                        pjExternal.setDecember(injectionList.get(0).getDecember());
                        pjExternal.setJanuary(injectionList.get(0).getJanuary());
                        pjExternal.setFebruary(injectionList.get(0).getFebruary());
                        pjExternal.setMarch(injectionList.get(0).getMarch());
                        if (pjExternalInjectionVo.getMoney() != null) {
                            if (year.substring(4, 6).equals("04")) {
                                pjExternal.setApril(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("05")) {
                                pjExternal.setMay(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("06")) {
                                pjExternal.setJune(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("07")) {
                                pjExternal.setJuly(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("08")) {
                                pjExternal.setAugust(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("09")) {
                                pjExternal.setSeptember(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("10")) {
                                pjExternal.setOctober(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("11")) {
                                pjExternal.setNovember(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("12")) {
                                pjExternal.setDecember(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("01")) {
                                pjExternal.setJanuary(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("02")) {
                                pjExternal.setFebruary(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("03")) {
                                pjExternal.setMarch(pjExternalInjectionVo.getMoney());
                            }
                        }
                        pjExternal.setTotal(String.valueOf(new BigDecimal(pjExternal.getApril()).add(new BigDecimal(pjExternal.getMay())).add(new BigDecimal(pjExternal.getJune()))
                                .add(new BigDecimal(pjExternal.getJuly())).add(new BigDecimal(pjExternal.getAugust())).add(new BigDecimal(pjExternal.getSeptember())).add(new BigDecimal(pjExternal.getOctober()))
                                .add(new BigDecimal(pjExternal.getNovember())).add(new BigDecimal(pjExternal.getDecember())).add(new BigDecimal(pjExternal.getJanuary())).add(new BigDecimal(pjExternal.getFebruary()))
                                .add(new BigDecimal(pjExternal.getMarch()))));
                        updatepjExternal.add(pjExternal);
                    } else {
                        pjExternal.setTotal(pjExternalInjectionVo.getMoney());
                        pjExternal.preInsert(tokenModel);
                        insertpjExternal.add(pjExternal);
                    }
                }
            }
            if (updatepjExternal.size() > 0) {
                pjExternalInjectionMapper.updatepj(updatepjExternal);
            }
            if (insertpjExternal.size() > 0) {
                pjExternalInjectionMapper.insertpj(insertpjExternal);
            }
        }

    }

    @Override
    public List<PjExternalInjectionVo> getTableinfo(String year, String group_id) throws Exception {
        PjExternalInjection pjExternalInjection = new PjExternalInjection();
        pjExternalInjection.setYears(year);
        pjExternalInjection.setGroup_id(group_id);
        List<PjExternalInjection> pjExternalInjectionList = pjExternalInjectionMapper.select(pjExternalInjection);
        if (pjExternalInjectionList.size() > 0) {
            for (PjExternalInjection injection : pjExternalInjectionList) {
                if(com.nt.utils.StringUtils.isBase64Encode(injection.getProject_name())){
                    injection.setProject_name(Base64.decodeStr(injection.getProject_name()));
                }
            }
        }
        TreeMap<String,List<PjExternalInjection>> injectionList =  pjExternalInjectionList.stream().collect(Collectors.groupingBy(PjExternalInjection :: getCompanyprojects_id,TreeMap::new,Collectors.toList()));
        List<PjExternalInjectionVo> returnlist = new ArrayList<>();
        if (injectionList.size() > 0) {
            for (List<PjExternalInjection> value : injectionList.values()) {
                PjExternalInjectionVo pjExternalVo = new PjExternalInjectionVo();
                pjExternalVo.setThemename(value.get(0).getThemename());
                pjExternalVo.setDivide(value.get(0).getDivide());
                pjExternalVo.setToolsorgs(value.get(0).getToolsorgs());
                pjExternalVo.setCompanyprojects_id(value.get(0).getCompanyprojects_id());
                pjExternalVo.setProject_name(value.get(0).getProject_name());
                pjExternalVo.setCompany("-");
                pjExternalVo.setApril("-");
                pjExternalVo.setMay("-");
                pjExternalVo.setJune("-");
                pjExternalVo.setJuly("-");
                pjExternalVo.setAugust("-");
                pjExternalVo.setSeptember("-");
                pjExternalVo.setOctober("-");
                pjExternalVo.setNovember("-");
                pjExternalVo.setDecember("-");
                pjExternalVo.setJanuary("-");
                pjExternalVo.setFebruary("-");
                pjExternalVo.setMarch("-");
                pjExternalVo.setTotal("-");
                pjExternalVo.setPjExternalInjectionList(value);
                returnlist.add(pjExternalVo);
            }
        }
        return returnlist;
    }

    @Override
    public Object getTableinfoReport(String year, String group_id) throws Exception {
        PjExternalInjection pjExternalInjection = new PjExternalInjection();
        pjExternalInjection.setYears(year);
        pjExternalInjection.setGroup_id(group_id);
        List<PjExternalInjection> pjExternalInjectionList = pjExternalInjectionMapper.select(pjExternalInjection);
        if (pjExternalInjectionList.size() > 0) {
            for (PjExternalInjection injection : pjExternalInjectionList) {
                if(com.nt.utils.StringUtils.isBase64Encode(injection.getProject_name())){
                    injection.setProject_name(Base64.decodeStr(injection.getProject_name()));
                }
            }
        }
        return JSONObject.toJSON(pjExternalInjectionList);
    }
}




