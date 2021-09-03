package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSONObject;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Departmental;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentalService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalMapper;
import com.nt.utils.BigDecimalUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Component
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
        //add_qhr_20210813 做非空判断
        if (departmentalList.size() > 0) {
            departmentalMapper.saveStaffList(departmentalList,nowMonth);
        }
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
    //update_qhr_20210813 改变后台返回值的东西
    @Override
    public List<DepartmentalVo> getDepartmental(String years, String group_id) throws Exception{
        Departmental departmental = new Departmental();
        List<Departmental> departmentalList = new ArrayList<>();
        departmental.setYears(years);
        departmental.setDepartment(group_id);
        departmentalList = departmentalMapper.select(departmental);
        Map<String, Map<String,List<Departmental>>> filterMap =
                departmentalList.stream()
                        .collect(Collectors.groupingBy(Departmental::getContractnumber,
                                Collectors.groupingBy(Departmental::getNumbers)));
        for(Departmental depart : departmentalList){
            if(depart.getEntrycondition().equals("HT004001")){
                depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "-废弃" + "】");
                depart.setClaimamount("-");
            }else{
                if(filterMap.get(depart.getContractnumber()).size() > 1){
                    depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "】");
                }else{
                    depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getClaimamount() + "】");
                }
            }
        }
        //按照theme分组
        TreeMap<String,List<Departmental>> departList =  departmentalList.stream().collect(Collectors.groupingBy(Departmental :: getThemeinfor_id,TreeMap::new,Collectors.toList()));
        Departmental departHJ = new Departmental();
        List<Departmental> departRelist = new ArrayList<>();
        DepartmentalVo returnVo = new DepartmentalVo();
        List<DepartmentalVo> returnVoList = new ArrayList<>();
        if (departList.size() > 0) {
            //循环按照theme分的组
            for (List<Departmental> value : departList.values()) {
                departRelist = new ArrayList<>();
                BigDecimal AprilT = new BigDecimal(0.00);
                BigDecimal MayT = new BigDecimal(0.00);
                BigDecimal JuneT = new BigDecimal(0.00);
                BigDecimal JulyT = new BigDecimal(0.00);
                BigDecimal AugustT = new BigDecimal(0.00);
                BigDecimal SeptemberT = new BigDecimal(0.00);
                BigDecimal OctoberT = new BigDecimal(0.00);
                BigDecimal NovemberT = new BigDecimal(0.00);
                BigDecimal DecemberT = new BigDecimal(0.00);
                BigDecimal JanuaryT = new BigDecimal(0.00);
                BigDecimal FebruaryT = new BigDecimal(0.00);
                BigDecimal MarchT = new BigDecimal(0.00);
                //将所属一个theme的数据按照主键排序
                value = value.stream().sorted(Comparator.comparing(Departmental::getDepartmental_id)).collect(Collectors.toList());
                //循环每个theme中的数据进行累加
                for (Departmental xiaoJi : value) {
                    AprilT = xiaoJi.getStaffcust04() == null? AprilT.add(new BigDecimal(0.00)) : AprilT.add(new BigDecimal(xiaoJi.getStaffcust04()));
                    MayT = xiaoJi.getStaffcust05() == null? MayT.add(new BigDecimal(0.00)) : MayT.add(new BigDecimal(xiaoJi.getStaffcust05()));
                    JuneT = xiaoJi.getStaffcust06() == null? JuneT.add(new BigDecimal(0.00)) : JuneT.add(new BigDecimal(xiaoJi.getStaffcust06()));
                    JulyT = xiaoJi.getStaffcust07() == null? JulyT.add(new BigDecimal(0.00)) : JulyT.add(new BigDecimal(xiaoJi.getStaffcust07()));
                    AugustT = xiaoJi.getStaffcust08() == null? AugustT.add(new BigDecimal(0.00)) : AugustT.add(new BigDecimal(xiaoJi.getStaffcust08()));
                    SeptemberT = xiaoJi.getStaffcust09() == null? SeptemberT.add(new BigDecimal(0.00)) : SeptemberT.add(new BigDecimal(xiaoJi.getStaffcust09()));
                    OctoberT = xiaoJi.getStaffcust10() == null? OctoberT.add(new BigDecimal(0.00)) : OctoberT.add(new BigDecimal(xiaoJi.getStaffcust10()));
                    NovemberT = xiaoJi.getStaffcust11() == null? NovemberT.add(new BigDecimal(0.00)) : NovemberT.add(new BigDecimal(xiaoJi.getStaffcust11()));
                    DecemberT = xiaoJi.getStaffcust12() == null? DecemberT.add(new BigDecimal(0.00)) : DecemberT.add(new BigDecimal(xiaoJi.getStaffcust12()));
                    JanuaryT = xiaoJi.getStaffcust01() == null? JanuaryT.add(new BigDecimal(0.00)) : JanuaryT.add(new BigDecimal(xiaoJi.getStaffcust01()));
                    FebruaryT = xiaoJi.getStaffcust02() == null? FebruaryT.add(new BigDecimal(0.00)) : FebruaryT.add(new BigDecimal(xiaoJi.getStaffcust02()));
                    MarchT = xiaoJi.getStaffcust03() == null? MarchT.add(new BigDecimal(0.00)) : MarchT.add(new BigDecimal(xiaoJi.getStaffcust03()));
                    //顺便将原数据加到新返回的列表中
                    departRelist.add(xiaoJi);
                }
                //设置每个theme下的合计行
                departHJ = new Departmental();
                departHJ.setStaffcust04(String.valueOf(AprilT));
                departHJ.setStaffcust05(String.valueOf(MayT));
                departHJ.setStaffcust06(String.valueOf(JuneT));
                departHJ.setStaffcust07(String.valueOf(JulyT));
                departHJ.setStaffcust08(String.valueOf(AugustT));
                departHJ.setStaffcust09(String.valueOf(SeptemberT));
                departHJ.setStaffcust10(String.valueOf(OctoberT));
                departHJ.setStaffcust11(String.valueOf(NovemberT));
                departHJ.setStaffcust12(String.valueOf(DecemberT));
                departHJ.setStaffcust01(String.valueOf(JanuaryT));
                departHJ.setStaffcust02(String.valueOf(FebruaryT));
                departHJ.setStaffcust03(String.valueOf(MarchT));

                departHJ.setDepartmental_id(value.get(0).getDepartmental_id() + 1);
                departHJ.setDepartment(value.get(0).getDepartment());
                departHJ.setYears(value.get(0).getYears());
                departHJ.setThemeinfor_id(value.get(0).getThemeinfor_id());
                departHJ.setThemename("合计");
                departHJ.setDivide("—");
                departHJ.setToolsorgs("—");
                departHJ.setContractnumber("—");
                departHJ.setClaimamount("—");
                departHJ.setNumbers("—");
                departHJ.setStaffnum("—");
                departHJ.setOutstaffnum("—");
                departHJ.setOutcompany("—");
                departRelist.add(departHJ);

                returnVo = new DepartmentalVo();
                returnVo.setDepartmentalList(departRelist);
                returnVo.setThemename(value.get(0).getThemename());
                returnVo.setDivide(value.get(0).getDivide());
                returnVo.setToolsorgs(value.get(0).getToolsorgs());
                returnVoList.add(returnVo);
            }
        }
        return returnVoList;
    }


    @Override
    public Object getTable1050infoReport(String year, String group_id) throws Exception {
        Departmental departHJ = new Departmental();
        Departmental departmental = new Departmental();
        List<Departmental> departmentalList = new ArrayList<>();
        departmental.setYears(year);
        departmental.setDepartment(group_id);
        departmentalList = departmentalMapper.select(departmental);
        List<Dictionary> dictionaryDivide = dictionaryService.getForSelect("PJ063");
        Map<String,Dictionary> divideMap = dictionaryDivide.stream().collect(Collectors.toMap(Dictionary::getCode, a -> a,(k1,k2)->k1));
        if(departmentalList.size() > 0){
            Map<String, Map<String,List<Departmental>>> filterMap =
                    departmentalList.stream()
                            .collect(Collectors.groupingBy(Departmental::getContractnumber,
                                    Collectors.groupingBy(Departmental::getNumbers)));
            for(Departmental depart : departmentalList){
                depart.setDivide(divideMap.get(depart.getDivide()).getValue1());
                if(depart.getEntrycondition().equals("HT004001")){
                    depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "-废弃" + "】");
                    depart.setClaimamount("-");
                }else{
                    if(filterMap.get(depart.getContractnumber()).size() > 1){
                        depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "】");
                    }else{
                        depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getClaimamount() + "】");
                    }
                }
            }
        }
        departHJ.setThemename("合计");
        departHJ.setDivide("—");
        departHJ.setToolsorgs("—");
        departHJ.setContractnumber("—");
        departHJ.setClaimamount("—");
        departHJ.setNumbers("—");
        departHJ.setStaffnum("—");
        departHJ.setOutstaffnum("—");
        departHJ.setOutcompany("—");
        departHJ.setStaffcust04(departmentalList.stream()
                .map(Departmental::getStaffcust04)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust05(departmentalList.stream()
                .map(Departmental::getStaffcust05)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust06(departmentalList.stream()
                .map(Departmental::getStaffcust06)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust07(departmentalList.stream()
                .map(Departmental::getStaffcust07)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust08(departmentalList.stream()
                .map(Departmental::getStaffcust08)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust09(departmentalList.stream()
                .map(Departmental::getStaffcust09)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust10(departmentalList.stream()
                .map(Departmental::getStaffcust10)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust11(departmentalList.stream()
                .map(Departmental::getStaffcust11)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust12(departmentalList.stream()
                .map(Departmental::getStaffcust12)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust01(departmentalList.stream()
                .map(Departmental::getStaffcust01)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust02(departmentalList.stream()
                .map(Departmental::getStaffcust02)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departHJ.setStaffcust03(departmentalList.stream()
                .map(Departmental::getStaffcust03)
                .reduce(String.valueOf(BigDecimal.ZERO), BigDecimalUtils::sum));
        departmentalList.add(departHJ);
        departmentalList.sort(Comparator.comparing(Departmental::getThemename));
        return JSONObject.toJSON(departmentalList);
    }

}
