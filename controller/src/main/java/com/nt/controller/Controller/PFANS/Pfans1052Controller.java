package com.nt.controller.Controller.PFANS;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartMonthPeo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemeContract;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.DepartmentalInsideService;
import com.nt.service_pfans.PFANS1000.mapper.AwardMapper;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalInsideMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departmentalinside")
public class Pfans1052Controller {

    @Autowired
    DepartmentalInsideService departmentalInsideService;

    @Autowired
    DepartmentalInsideMapper departmentalInsideMapper;

    @Autowired
    CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    AwardMapper awardMapper;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    private OrgTreeService orgtreeService;

    @Autowired
    private TokenService tokenService;

    private static final String firstMonth = "04";

    @PostMapping("/insert")
    public ApiResult insert(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        departmentalInsideService.insert();
        return ApiResult.success();
    }

    @GetMapping("/getTableinfo")
    public ApiResult getTableinfo(@RequestParam String year,@RequestParam String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success(departmentalInsideService.getTableinfo(year, group_id));
    }

    /**
     * 成本结转导出excel
     *
     */
    @RequestMapping(value = "/downloadExcel", method = { RequestMethod.GET })
    public void downloadExcel(String years,String depart, HttpServletRequest request, HttpServletResponse resp)  throws Exception {
        List<ThemeContract> filterList = new ArrayList<>();
        List<ThemeContract> filterListAnt = new ArrayList<>();
        List<ThemeContract> filteroutReList = new ArrayList<>();
        String yearmonth = "";
        Calendar calendar = Calendar.getInstance();
        //当前月份减1
        String month1 = "";
        int month = calendar.get(Calendar.MONTH);
        if(month == 0){
            month = 12;
            years = String.valueOf(Integer.parseInt(years) - 1);
        }
        month1 = String.valueOf(month);
        if (month < 10) {
            month1 = "0" + String.valueOf(month);
        }else{
            month1 = String.valueOf(month);
        }
        yearmonth = years + "-" + month1;
        List<ThemeContract> getContListAnt = departmentalInsideMapper.getContListAnt(years,yearmonth,depart);
        Map<String,String> refMap = new HashMap<>();
        if(getContListAnt.size() > 0){
            Map<String,List<ThemeContract>> groupConAntMap = getContListAnt.stream()
                    .filter(conAnt  -> !StringUtils.isNullOrEmpty(conAnt.getContractnumber())
                            && conAnt.getCompletiondate() != null)
                    .collect(Collectors.groupingBy(ThemeContract :: getContractnumber));
            groupConAntMap.forEach((grcAnt,listCAnt) -> {
                try {
                    //筛选与本月最近的合同回数（验收完了日进行判断）
                    filterListAnt.add(this.filterContAnt(listCAnt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if(filterListAnt.size() > 0){
                filterListAnt.forEach(filAnt -> {
                    String monant = "";
                    int mint = Integer.parseInt(filAnt.getCompletiondate().substring(5,7)) + 1;
                    if (mint < 10) {
                        monant = "0" + String.valueOf(mint);
                    }else{
                        monant = String.valueOf(mint);
                    }
                    refMap.put(filAnt.getContractnumber(),monant);
                });
            }
        }

        List<ThemeContract> getContList = departmentalInsideMapper.getContList(years,yearmonth,depart);
        if(getContList.size() > 0){
            Map<String,List<ThemeContract>> groupConMap = getContList.stream()
                    .filter(con  -> !StringUtils.isNullOrEmpty(con.getContractnumber())
                            && con.getCompletiondate() != null)
                    .collect(Collectors.groupingBy(ThemeContract :: getContractnumber));
            groupConMap.forEach((grc,listC) -> {
                try {
                    //筛选与本月最近的合同回数（验收完了日进行判断）
                    filterList.add(this.filterCont(listC));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
//            if(filterList.size() > 0){
//                filterList.forEach(filAnt -> {
//                    List<Dictionary> typeOfList = null;
//                    try {
//                        typeOfList = dictionaryService.getForSelect("HT014");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    AtomicReference<Boolean> flagAward = new AtomicReference<>(false);
//                    List<Contractapplication> entrList = companyProjectsMapper
//                            .selectCont(filAnt.getContractnumber(),depart,depart);
//                    if(entrList.size() > 0){
//                        List<Dictionary> finalTypeOfList = typeOfList;
//                        entrList.forEach(ent -> {
//                            AtomicReference<Boolean> flag = new AtomicReference<>(false);
//                            finalTypeOfList.forEach(item -> {
//                                if (item.getCode().equals(ent.getContracttype())) {
//                                    flag.set(true);
//                                }
//                            });
//                            if (flag.get()) {
//                                Award award = new Award();
//                                //委托合同号
//                                award.setContractnumber(ent.getContractnumber());
//                                award.setDistinguishbetween("1");
//                                Award find = awardMapper.selectOne(award);
//                                if (find != null && "4".equals(find.getStatus())) {
//                                    flagAward.set(true);
//                                }
//                            }
//                        });
//                    }
//                    if(flagAward.get()){
//                        filteroutReList.add(filAnt);
//                    }
//                });
//            }
        }

        List<ThemeContract> inThemeList = new ArrayList<>();
        if(filterList.size() > 0){
            String finalYears = years;
            String finalMonth = month1;
            String finalYearmonth = yearmonth;
            filterList.forEach(fil -> {
                ThemeContract clonedInfo = new ThemeContract();
                BeanUtils.copyProperties(fil, clonedInfo);
                filteroutReList.add(clonedInfo);
                String mant = "";
                mant = refMap.get(fil.getContractnumber()) != null
                        ? refMap.get(fil.getContractnumber()) : firstMonth;
                try {
                    ThemeContract cttin = new ThemeContract("该当合同投入社员工数", finalYearmonth,"",
                            "","","","","","",
                            "","","","","","","","","","");
                    cttin = this.upInResult(fil, mant, finalMonth, finalYears, depart, true);
                    if(cttin != null){
                        inThemeList.add(cttin);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        List<ThemeContract> outThemeList = new ArrayList<>();
        if(filteroutReList.size() > 0){
            String finalYears = years;
            String finalMonth = month1;
            String finalYearmonth1 = yearmonth;
            filteroutReList.forEach(fil -> {
                String mant = "";
                mant = refMap.get(fil.getContractnumber()) != null
                        ? refMap.get(fil.getContractnumber()) : firstMonth;
                try {
                    ThemeContract cttout = new ThemeContract("该当合同投入社员工数", finalYearmonth1,"",
                            "","","","","","",
                            "","","","","","","","","","");
                    cttout = this.upInResult(fil, mant, finalMonth, finalYears, depart, false);
                    if(cttout != null){
                        outThemeList.add(cttout);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }


        //导出Excel结果集
        Map<String, Object> dataReslut = new HashMap<>();
        //所属部门
        OrgTree newOrgInfo = orgtreeService.get(new OrgTree());
        OrgTree orginfo = orgtreeService.getOrgInfo(newOrgInfo, depart);
        dataReslut.put("department",orginfo.getCompanyname());
        //验收月
        dataReslut.put("yearmonth",yearmonth);
        //社内部门共通工数
        int yeaa = 0;
        int monthaa = calendar.get(Calendar.MONTH) + 1;
        int dayaa = calendar.get(Calendar.DAY_OF_MONTH);
        if(monthaa >= 1 && monthaa < 4) {
            yeaa = calendar.get(Calendar.YEAR) - 1;
        }
        else if(monthaa == 4)
        {
            //时间大于4月10日的，属于新年度，小于10日，属于旧年度
            if(dayaa >= 10)
            {
                yeaa = calendar.get(Calendar.YEAR);
            }
            else
            {
                yeaa = calendar.get(Calendar.YEAR) - 1;
            }
        }
        else
        {
            yeaa = calendar.get(Calendar.YEAR);
        }

        List<DepartMonthPeo> admindepartPo = departmentalInsideMapper.getComm(depart,String.valueOf(yeaa));
        dataReslut.put("comm",admindepartPo);
        //社内部门总工数
        List<DepartMonthPeo> departMonthPeo = departmentalInsideMapper.getTotalPeo(String.valueOf(yeaa),orginfo.getCompanyname());
        dataReslut.put("totalpeo",departMonthPeo);

        //社内合同工数详细信息**
        dataReslut.put("inThemeList",inThemeList);
        //社外合同工数详细信息**
        dataReslut.put("outThemeList",outThemeList);


        ExcelOutPutUtil.OutPut("theme别合同收支分析表", "tbhtshouzhifenxibiao.xlsx", dataReslut, resp);
    }

    ThemeContract filterCont(List<ThemeContract> listC) throws Exception {
        Comparator<ThemeContract> comparator = Comparator.comparing(ThemeContract::getCompletiondate);
        ThemeContract result = listC.stream().min(comparator).get();
        return result;
    }

    ThemeContract filterContAnt(List<ThemeContract> listC) throws Exception {
        Comparator<ThemeContract> comparator = Comparator.comparing(ThemeContract::getCompletiondate);
        ThemeContract result = listC.stream().max(comparator).get();
        return result;
    }

    /*
    * mant 上一个合同有效验收完了月 + 1
    * finalMonth 合同有效验收完了月
    * years 年度
    * depart 部门
    * flag（true 社内 false社外）
    * */
    ThemeContract upInResult(ThemeContract upTheme, String mant, String finalMonth, String years, String depart, Boolean flag) throws Exception {
        ThemeContract getcon = new ThemeContract();
        //取有效结束月
        int finmnn = Integer.parseInt(finalMonth) >= Integer.parseInt(upTheme.getCompletiondate().substring(5,7))
                ? Integer.parseInt(upTheme.getCompletiondate().substring(5,7)) : Integer.parseInt(finalMonth);
        int mmnt = Integer.parseInt(mant);
        if(flag){
            getcon = departmentalInsideMapper.sumContractIn(upTheme.getContractnumber(),years,depart);
            if(getcon != null){
                setActualInfo(upTheme, getcon, finmnn, mmnt);
            }else{
                upTheme = null;
            }
        }else{
            getcon = departmentalInsideMapper.sumContractOut(upTheme.getContractnumber(),years,depart);
            if(getcon != null) {
                setActualInfo(upTheme, getcon, finmnn, mmnt);
            }else{
                upTheme = null;
            }
        }
        return upTheme;
    }

    private void setActualInfo(ThemeContract upTheme, ThemeContract getcon, int finmnn, int mmnt) {
        switch (finmnn) {
            case 4:
                upTheme.setActual04(getcon.getActual04());
                break;
            case 5:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                break;
            case 6:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                break;
            case 7:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                break;
            case 8:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual08(getcon.getActual08());
                break;
            case 9:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual08(getcon.getActual08());
                upTheme.setActual09(getcon.getActual09());
                break;
            case 10:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual08(getcon.getActual08());
                upTheme.setActual09(getcon.getActual09());
                upTheme.setActual10(getcon.getActual10());
                break;
            case 11:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual08(getcon.getActual08());
                upTheme.setActual09(getcon.getActual09());
                upTheme.setActual10(getcon.getActual10());
                upTheme.setActual11(getcon.getActual11());
                break;
            case 12:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual08(getcon.getActual08());
                upTheme.setActual09(getcon.getActual09());
                upTheme.setActual10(getcon.getActual10());
                upTheme.setActual11(getcon.getActual11());
                upTheme.setActual12(getcon.getActual12());
                break;
            case 1:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual08(getcon.getActual08());
                upTheme.setActual09(getcon.getActual09());
                upTheme.setActual10(getcon.getActual10());
                upTheme.setActual11(getcon.getActual11());
                upTheme.setActual12(getcon.getActual12());
                upTheme.setActual01(getcon.getActual01());
                break;
            case 2:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual08(getcon.getActual08());
                upTheme.setActual09(getcon.getActual09());
                upTheme.setActual10(getcon.getActual10());
                upTheme.setActual11(getcon.getActual11());
                upTheme.setActual12(getcon.getActual12());
                upTheme.setActual01(getcon.getActual01());
                upTheme.setActual02(getcon.getActual02());
                break;
            case 3:
                upTheme.setActual04(getcon.getActual04());
                upTheme.setActual05(getcon.getActual05());
                upTheme.setActual06(getcon.getActual06());
                upTheme.setActual07(getcon.getActual07());
                upTheme.setActual08(getcon.getActual08());
                upTheme.setActual09(getcon.getActual09());
                upTheme.setActual10(getcon.getActual10());
                upTheme.setActual11(getcon.getActual11());
                upTheme.setActual12(getcon.getActual12());
                upTheme.setActual01(getcon.getActual01());
                upTheme.setActual02(getcon.getActual02());
                upTheme.setActual03(getcon.getActual03());
                break;
        }
        switch (mmnt) {
            case 5:
                upTheme.setActual04("0");
                break;
            case 6:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                break;
            case 7:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                break;
            case 8:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                break;
            case 9:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                upTheme.setActual08("0");
                break;
            case 10:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                upTheme.setActual08("0");
                upTheme.setActual09("0");
                break;
            case 11:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                upTheme.setActual08("0");
                upTheme.setActual09("0");
                upTheme.setActual10("0");
                break;
            case 12:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                upTheme.setActual08("0");
                upTheme.setActual09("0");
                upTheme.setActual10("0");
                upTheme.setActual11("0");
                break;
            case 1:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                upTheme.setActual08("0");
                upTheme.setActual09("0");
                upTheme.setActual10("0");
                upTheme.setActual11("0");
                upTheme.setActual12("0");
                break;
            case 2:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                upTheme.setActual08("0");
                upTheme.setActual09("0");
                upTheme.setActual10("0");
                upTheme.setActual11("0");
                upTheme.setActual12("0");
                upTheme.setActual01("0");
                break;
            case 3:
                upTheme.setActual04("0");
                upTheme.setActual05("0");
                upTheme.setActual06("0");
                upTheme.setActual07("0");
                upTheme.setActual08("0");
                upTheme.setActual09("0");
                upTheme.setActual10("0");
                upTheme.setActual11("0");
                upTheme.setActual12("0");
                upTheme.setActual01("0");
                upTheme.setActual02("0");
                break;
        }
    }


    /**
     * 积木报表
     * 部门项目别年度统计
     * 21/8/23 scc
     * */
    @GetMapping("/getTableinfoReport")
    public ApiResult getTableinfoReport(@RequestParam String year,@RequestParam String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success("getTableinfoReport",departmentalInsideService.getTableinfoReport(year, group_id));
    }
}
