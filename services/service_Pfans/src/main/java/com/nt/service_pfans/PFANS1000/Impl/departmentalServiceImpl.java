package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Departmental;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalVo;
import com.nt.dao_Pfans.PFANS6000.PjExternalInjection;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentalService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


@Service
@Transactional(rollbackFor = Exception.class)
public class departmentalServiceImpl implements DepartmentalService {

    private static final Integer INITIALNUM = 1;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private DepartmentalMapper departmentalMapper;

    @Autowired
    private DictionaryService dictionaryService;


    /**
     * @方法名：getExpatureList
     * @描述：获取社内外员工关联theme的合同的项目的工数(定时任务）
     * @创建日期：2021/07/14
     * @作者：ztc
     * @参数：[]
     * @返回值：[]
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void getExpatureList() throws Exception {
        List<Dictionary> dictionaryL = dictionaryService.getForSelect("BP027");
        TokenModel tokenModel = new TokenModel();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = ft.format(new Date());
        String nowYear = nowDate.substring(0, 4);
        String nowMonth = nowDate.substring(5,7);
        String nowY_Month = nowDate.substring(0,7);
        String nowDay = nowDate.substring(8,10);
        if(Integer.parseInt(nowDay) == Integer.parseInt(dictionaryL.get(0).getValue1()) + 1) return;
        String setYears = (Integer.parseInt(nowMonth) < 4 ? String.valueOf(Integer.parseInt(nowYear) - 1) : nowYear);
        List<DepartmentalVo> departmentalVoList = departmentalMapper.getProConInfo(nowY_Month);
        List<String> departGroupFilter = new ArrayList<>();
        departGroupFilter = departmentalVoList.stream().map(DepartmentalVo::getCompanyprojects_id).collect(Collectors.toList());
        departGroupFilter = departGroupFilter.stream().distinct().collect(Collectors.toList());
        Map<String,String> staffWorkSumMap = getStaffWorkSum(departGroupFilter,nowY_Month);
        List<Departmental> departmentalList = new ArrayList<>();
        for (DepartmentalVo item : departmentalVoList) {
            Departmental departmental = new Departmental();
            BeanUtils.copyProperties(item, departmental);
            departmental.setYears(setYears);
            departmental.setYears(nowYear);
            PropertyUtils.setProperty(departmental, "staffcust" + nowMonth, item.getMonthcast());
            departmental.preInsert(tokenModel);
            String keyAnt = departmental.getYears() + departmental.getThemeinfor_id() + departmental.getContractnumber() + departmental.getClaimtype() + departmental.getNumbers() + departmental.getOutcompany();
            departmental.setDepartmental_id(keyAnt);
            Departmental getDepartmental = departmentalMapper.selectByPrimaryKey(keyAnt);
            if(getDepartmental != null){
                BigDecimal old_staffAntDecimal = new BigDecimal(!com.mysql.jdbc.StringUtils.isNullOrEmpty(
                        departmentalMapper.selectByPrimaryKey(keyAnt).getStaffnum()) ? departmentalMapper.selectByPrimaryKey(keyAnt).getStaffnum() : "0");
                BigDecimal new_staffAntDecimal = new BigDecimal(!com.mysql.jdbc.StringUtils.isNullOrEmpty(
                        staffWorkSumMap.get(item.getCompanyprojects_id())) ? staffWorkSumMap.get(item.getCompanyprojects_id()) : "0");
                departmental.setStaffnum(new_staffAntDecimal.add(old_staffAntDecimal).toString());
                BigDecimal old_outStaffNumDecimal = new BigDecimal(!com.mysql.jdbc.StringUtils.isNullOrEmpty(
                        departmentalMapper.selectByPrimaryKey(keyAnt).getOutstaffnum()) ? departmentalMapper.selectByPrimaryKey(keyAnt).getOutstaffnum() : "0");
                BigDecimal new_outStaffNumDecimal = new BigDecimal(item.getOutstaffnum());
                departmental.setOutstaffnum(old_outStaffNumDecimal.add(new_outStaffNumDecimal).toString());
            }else{
                departmental.setStaffnum(staffWorkSumMap.get(item.getCompanyprojects_id()));
            }
            departmentalList.add(departmental);
        }
        departmentalMapper.saveStaffList(departmentalList,nowMonth);
    }

    /**
     * @方法名：getStaffWorkSum
     * @描述：获取社内员工指定项目的合计工数
     * @创建日期：2021/07/14
     * @作者：ztc
     * @参数：[departGroupFilter,nowDate]
     * @返回值：Map<String,String>
     */
    private Map<String,String> getStaffWorkSum(List<String> departGroupFilter,String nowDate) throws Exception{
        Map<String,String> staffSumMap = new HashMap<>();
        departGroupFilter.forEach(item -> {
            String staff = departmentalMapper.getStaffInfo(item,nowDate);
            staffSumMap.put(item,staff);
        });
        return staffSumMap;
    }

    /**
     * @方法名：getStaffWorkSum
     * @描述：根据组织年度查询外驻别部门支出
     * @创建日期：2021/07/14
     * @作者：ztc
     * @参数：[years,group_id]
     * @返回值：List<Departmental>
     */
    @Override
    public List<Departmental> getDepartmental(String years, String group_id) throws Exception{
        Departmental departmental = new Departmental();
        List<Departmental> departmentalList = new ArrayList<>();
        departmental.setYears(years);
        departmental.setDepartment(group_id);
        departmentalList = departmentalMapper.select(departmental);
        departmentalList = departmentalList.stream().sorted(Comparator.comparing(Departmental::getDepartmental_id)).collect(Collectors.toList());
        return departmentalList;
    }
}
