package com.nt.controller.Controller.PFANS;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartMonthPeo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemeContract;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.DepartmentalInsideService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalInsideMapper;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departmentalinside")
public class Pfans1052Controller {

    @Autowired
    DepartmentalInsideService departmentalInsideService;

    @Autowired
    DepartmentalInsideMapper departmentalInsideMapper;

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
    public void downloadPdf(String years,String depart, HttpServletRequest request, HttpServletResponse resp)  throws Exception {
        List<ThemeContract> filterList = new ArrayList<>();
        List<ThemeContract> filterListAnt = new ArrayList<>();
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
        }



        List<ThemeContract> inThemeList = new ArrayList<>();
        List<ThemeContract> outThemeList = new ArrayList<>();
        if(filterList.size() > 0){
            String finalYears = years;
            String finalMonth = month1;
            filterList.forEach(fil -> {
                String mant = "";
                mant = refMap.get(fil.getContractnumber()) != null
                        ? refMap.get(fil.getContractnumber()) : firstMonth;
                try {
                    ThemeContract cttin = new ThemeContract();
                    cttin = this.upInResult(fil, mant, finalMonth, finalYears, depart, true);
                    inThemeList.add(cttin);
                    ThemeContract cttout = new ThemeContract();
                    cttout = this.upInResult(fil, mant, finalMonth, finalYears, depart, false);
                    outThemeList.add(cttout);
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
        dataReslut.put("comm",departmentalInsideMapper.getComm(depart,yearmonth));
        //社内部门总工数
        List<DepartMonthPeo> departMonthPeo = departmentalInsideMapper.getTotalPeo(years,orginfo.getCompanyname());
        dataReslut.put("totalpeo",departMonthPeo);

//        //社内合同工数详细信息**
//        dataReslut.put();
//        //社外合同工数详细信息**
//        dataReslut.put();


        ExcelOutPutUtil.OutPut("theme别合同收支分析表", "chenbenjiezhuanbiao.xlsx", dataReslut, resp);
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
            }
        }else{
            getcon = departmentalInsideMapper.sumContractOut(upTheme.getContractnumber(),years,depart);
            if(getcon != null) {
                setActualInfo(upTheme, getcon, finmnn, mmnt);
            }
        }
        return upTheme;
    }

    private void setActualInfo(ThemeContract upTheme, ThemeContract getcon, int finmnn, int mmnt) {
        switch (finmnn) {
            case 4:
                getcon.setActual04(getcon.getActual04());
                break;
            case 5:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                break;
            case 6:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                break;
            case 7:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                break;
            case 8:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual08(getcon.getActual08());
                break;
            case 9:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual08(getcon.getActual08());
                getcon.setActual09(getcon.getActual09());
                break;
            case 10:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual08(getcon.getActual08());
                getcon.setActual09(getcon.getActual09());
                getcon.setActual10(getcon.getActual10());
                break;
            case 11:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual08(getcon.getActual08());
                getcon.setActual09(getcon.getActual09());
                getcon.setActual10(getcon.getActual10());
                getcon.setActual11(getcon.getActual11());
                break;
            case 12:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual08(getcon.getActual08());
                getcon.setActual09(getcon.getActual09());
                getcon.setActual10(getcon.getActual10());
                getcon.setActual11(getcon.getActual11());
                getcon.setActual12(getcon.getActual12());
                break;
            case 1:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual08(getcon.getActual08());
                getcon.setActual09(getcon.getActual09());
                getcon.setActual10(getcon.getActual10());
                getcon.setActual11(getcon.getActual11());
                getcon.setActual12(getcon.getActual12());
                getcon.setActual01(getcon.getActual01());
                break;
            case 2:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual08(getcon.getActual08());
                getcon.setActual09(getcon.getActual09());
                getcon.setActual10(getcon.getActual10());
                getcon.setActual11(getcon.getActual11());
                getcon.setActual12(getcon.getActual12());
                getcon.setActual01(getcon.getActual01());
                getcon.setActual02(getcon.getActual02());
                break;
            case 3:
                getcon.setActual04(getcon.getActual04());
                getcon.setActual05(getcon.getActual05());
                getcon.setActual06(getcon.getActual06());
                getcon.setActual07(getcon.getActual07());
                getcon.setActual08(getcon.getActual08());
                getcon.setActual09(getcon.getActual09());
                getcon.setActual10(getcon.getActual10());
                getcon.setActual11(getcon.getActual11());
                getcon.setActual12(getcon.getActual12());
                getcon.setActual01(getcon.getActual01());
                getcon.setActual02(getcon.getActual02());
                getcon.setActual03(getcon.getActual03());
                break;
        }
        switch (mmnt) {
            case 5:
                getcon.setActual04("0");
                break;
            case 6:
                getcon.setActual04("0");
                getcon.setActual05("0");
                break;
            case 7:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                break;
            case 8:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                break;
            case 9:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                getcon.setActual08("0");
                break;
            case 10:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                getcon.setActual08("0");
                getcon.setActual09("0");
                break;
            case 11:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                getcon.setActual08("0");
                getcon.setActual09("0");
                getcon.setActual10("0");
                break;
            case 12:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                getcon.setActual08("0");
                getcon.setActual09("0");
                getcon.setActual10("0");
                getcon.setActual11("0");
                break;
            case 1:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                getcon.setActual08("0");
                getcon.setActual09("0");
                getcon.setActual10("0");
                getcon.setActual11("0");
                getcon.setActual12("0");
                break;
            case 2:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                getcon.setActual08("0");
                getcon.setActual09("0");
                getcon.setActual10("0");
                getcon.setActual11("0");
                getcon.setActual12("0");
                getcon.setActual01("0");
                break;
            case 3:
                getcon.setActual04("0");
                getcon.setActual05("0");
                getcon.setActual06("0");
                getcon.setActual07("0");
                getcon.setActual08("0");
                getcon.setActual09("0");
                getcon.setActual10("0");
                getcon.setActual11("0");
                getcon.setActual12("0");
                getcon.setActual01("0");
                getcon.setActual02("0");
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
