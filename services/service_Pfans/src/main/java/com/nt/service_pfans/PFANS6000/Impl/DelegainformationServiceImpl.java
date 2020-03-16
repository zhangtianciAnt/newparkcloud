package com.nt.service_pfans.PFANS6000.Impl;


import cn.hutool.core.date.DateUtil;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectsystemMapper;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import com.nt.service_pfans.PFANS6000.mapper.DelegainformationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class DelegainformationServiceImpl implements DeleginformationService {

    @Autowired
    private ProjectsystemMapper projectsystemMapper;
    @Autowired
    private DelegainformationMapper delegainformationMapper;

    @Override
    public List<DelegainformationVo> getDelegainformation() throws Exception {
        return delegainformationMapper.getDelegainformation();
    }


    @Override
    public void createDeleginformation(Delegainformation delegainformation , TokenModel tokenModel) throws Exception{
        delegainformationMapper.delete(delegainformation);
        //工具
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        DecimalFormat df = new DecimalFormat("0.00");
        // 工数 String类型
        String jobNub_str;
        //工作天数 int类型
        int workDay_i;
        Double jobNub_dbe;
        //获取当前时间
        String thisYear_s = DateUtil.format(new Date(), "YYYY");
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
        Projectsystem projectsystem = new Projectsystem();
        List<Projectsystem> projectsystemListObtain = projectsystemMapper.select(projectsystem);
        //对存入集合的数据进行操作
        for(int i = 0; i < projectsystemListObtain.size(); i ++){
            delegainformation.preInsert(tokenModel);
            delegainformation.setDelegainformation_id(UUID.randomUUID().toString());
            delegainformation.setCompanyprojects_id(projectsystemListObtain.get(i).getCompanyprojects_id());
            delegainformation.setProjectsystem_id(projectsystemListObtain.get(i).getProjectsystem_id());
            delegainformation.setAdmissiontime(projectsystemListObtain.get(i).getAdmissiontime());
            delegainformation.setExittime(projectsystemListObtain.get(i).getExittime());
            delegainformation.setSupplierinfor_id(projectsystemListObtain.get(i).getSuppliernameid());
            //社内外协区分， 1为外协
            if(projectsystemListObtain.get(i).getType().equals("1")){
                //获取该员工的入场时间
                Date admissiontime_d = projectsystemListObtain.get(i).getAdmissiontime();
                //获取员工的退场时间
                Date exitime_d = projectsystemListObtain.get(i).getExittime();
                String admissiontimeMonth_s = DateUtil.format(admissiontime_d, "MM");
                String admissiontimeDay_s = DateUtil.format(admissiontime_d, "dd");
                String exitimeMonth_s = DateUtil.format(exitime_d, "MM");
                String exitimeDay_s = DateUtil.format(exitime_d, "dd");
                //退场时间不为空
                if(projectsystemListObtain.get(i).getExittime() != null) {
                    if (aprilFirst_d.before(admissiontime_d) && exitime_d.before(marchLast_d)) {
                        //本事业年度都在此工作
                        delegainformation.setApril("1.00");
                        delegainformation.setMay("1.00");
                        delegainformation.setJune("1.00");
                        delegainformation.setJuly("1.00");
                        delegainformation.setAugust("1.00");
                        delegainformation.setSeptember("1.00");
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                        //4月1号之后入场，明年3月末之后退场
                    } else if (admissiontime_d.after(aprilFirst_d) && exitime_d.after(marchLast_d)) {
                        delegainformation.setYear(thisYear_s);
                        if (admissiontimeMonth_s.equals("04")) {
                            workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setApril(jobNub_str);
                            delegainformation.setMay("1.00");
                            delegainformation.setJune("1.00");
                            delegainformation.setJuly("1.00");
                            delegainformation.setAugust("1.00");
                            delegainformation.setSeptember("1.00");
                            delegainformation.setOctober("1.00");
                            delegainformation.setNovember("1.00");
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("05")) {
                            workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setMay(jobNub_str);
                            delegainformation.setJune("1.00");
                            delegainformation.setJuly("1.00");
                            delegainformation.setAugust("1.00");
                            delegainformation.setSeptember("1.00");
                            delegainformation.setOctober("1.00");
                            delegainformation.setNovember("1.00");
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("06")) {
                            workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setJune(jobNub_str);
                            delegainformation.setJuly("1.00");
                            delegainformation.setAugust("1.00");
                            delegainformation.setSeptember("1.00");
                            delegainformation.setOctober("1.00");
                            delegainformation.setNovember("1.00");
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("07")) {
                            workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setJuly(jobNub_str);
                            delegainformation.setAugust("1.00");
                            delegainformation.setSeptember("1.00");
                            delegainformation.setOctober("1.00");
                            delegainformation.setNovember("1.00");
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("08")) {
                            workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setAugust(jobNub_str);
                            delegainformation.setSeptember("1.00");
                            delegainformation.setOctober("1.00");
                            delegainformation.setNovember("1.00");
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("09")) {
                            workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setSeptember(jobNub_str);
                            delegainformation.setOctober("1.00");
                            delegainformation.setNovember("1.00");
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("10")) {
                            workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setOctober(jobNub_str);
                            delegainformation.setNovember("1.00");
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("11")) {
                            workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setNovember(jobNub_str);
                            delegainformation.setDecember("1.00");
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("12")) {
                            workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setDecember(jobNub_str);
                            delegainformation.setJanuary("1.00");
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("01")) {
                            workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setJanuary(jobNub_str);
                            delegainformation.setFebruary("1.00");
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("02")) {
                            workDay_i = 28 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 28);
                            jobNub_str = df.format(jobNub_obj);
                            jobNub_dbe = Double.valueOf(jobNub_str);
                            if (jobNub_dbe > 1.00) {
                                jobNub_str = "1.00";
                            }
                            delegainformation.setFebruary(jobNub_str);
                            delegainformation.setMarch("1.00");
                        } else if (admissiontimeMonth_s.equals("03")) {
                            workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                            BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                            jobNub_str = df.format(jobNub_obj);
                            delegainformation.setMarch(jobNub_str);
                        }
                        //4月1号之前入场，明年三月未之前退场
                    } else if (admissiontime_d.before(aprilFirst_d) && exitime_d.before(marchLast_d)) {
                        delegainformation.setYear(thisYear_s);
                        //月份相同
                        int workDay_i_same = Integer.parseInt(exitimeDay_s) - Integer.parseInt(admissiontimeDay_s) + 1;
                        //仅入场时间(大月份)
                        int workDay_i_ad_b = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        //仅入场时间(小月份)
                        int workDay_i_ad_s = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                        //仅退场时间
                        int workDay_i_ex = Integer.parseInt(exitimeDay_s);

                        for(int m = 1; m < 13; m++){
                            if(m >= Integer.parseInt(admissiontimeMonth_s) && m <= Integer.parseInt(exitimeMonth_s)){
                                if(m == 4){
                                    //入退场月份都为4月份
                                    if(admissiontimeMonth_s.equals("04") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setApril(jobNub_str);
                                        //仅入场时间为4月份
                                    }else if(admissiontimeMonth_s.equals("04")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_s / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setApril(jobNub_str);
                                        //仅退场时间为4月份
                                    }else if(exitimeMonth_s.equals("04")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setApril(jobNub_str);
                                        //入退场都不为4月份
                                    }else{
                                        delegainformation.setApril("1.00");
                                    }
                                }else if(m == 5){
                                    //入退场月份都为5月份
                                    if(admissiontimeMonth_s.equals("05") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setMay(jobNub_str);
                                        //仅入场时间为5月份
                                    }else if(admissiontimeMonth_s.equals("05")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_b / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setMay(jobNub_str);
                                        //仅退场时间为5月份
                                    }else if(exitimeMonth_s.equals("05")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setMay(jobNub_str);
                                        //入退场都不为5月份
                                    }else{
                                        delegainformation.setMay("1.00");
                                    }
                                }else if(m == 6){
                                    //入退场月份都为6月份
                                    if(admissiontimeMonth_s.equals("06") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJune(jobNub_str);
                                        //仅入场时间为6月份
                                    }else if(admissiontimeMonth_s.equals("06")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_s / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJune(jobNub_str);
                                        //仅退场时间为6月份
                                    }else if(exitimeMonth_s.equals("06")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJune(jobNub_str);
                                        //入退场都不为6月份
                                    }else{
                                        delegainformation.setJune("1.00");
                                    }
                                }else if(m == 7){
                                    //入退场月份都为7月份
                                    if(admissiontimeMonth_s.equals("07") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJuly(jobNub_str);
                                        //仅入场时间为7月份
                                    }else if(admissiontimeMonth_s.equals("07")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_b / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJuly(jobNub_str);
                                        //仅退场时间为7月份
                                    }else if(exitimeMonth_s.equals("07")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJuly(jobNub_str);
                                        //入退场都不为7月份
                                    }else{
                                        delegainformation.setJuly("1.00");
                                    }
                                }else if(m == 8){
                                    //入退场月份都为8月份
                                    if(admissiontimeMonth_s.equals("08") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setAugust(jobNub_str);
                                        //仅入场时间为8月份
                                    }else if(admissiontimeMonth_s.equals("08")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_b / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setAugust(jobNub_str);
                                        //仅退场时间为8月份
                                    }else if(exitimeMonth_s.equals("08")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setAugust(jobNub_str);
                                        //入退场都不为8月份
                                    }else{
                                        delegainformation.setAugust("1.00");
                                    }
                                }else if(m == 9){
                                    //入退场月份都为9月份
                                    if(admissiontimeMonth_s.equals("09") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setSeptember(jobNub_str);
                                        //仅入场时间为9月份
                                    }else if(admissiontimeMonth_s.equals("09")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_s / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setSeptember(jobNub_str);
                                        //仅退场时间为9月份
                                    }else if(exitimeMonth_s.equals("09")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setSeptember(jobNub_str);
                                        //入退场都不为9月份
                                    }else{
                                        delegainformation.setSeptember("1.00");
                                    }
                                }else if(m == 10){
                                    //入退场月份都为10月份
                                    if(admissiontimeMonth_s.equals("10") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setOctober(jobNub_str);
                                        //仅入场时间为10月份
                                    }else if(admissiontimeMonth_s.equals("10")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_b / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setOctober(jobNub_str);
                                        //仅退场时间为10月份
                                    }else if(exitimeMonth_s.equals("01")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setOctober(jobNub_str);
                                        //入退场都不为10月份
                                    }else{
                                        delegainformation.setOctober("1.00");
                                    }
                                }else if(m == 11){
                                    //入退场月份都为11月份
                                    if(admissiontimeMonth_s.equals("11") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setNovember(jobNub_str);
                                        //仅入场时间为11月份
                                    }else if(admissiontimeMonth_s.equals("11")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_s / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setNovember(jobNub_str);
                                        //仅退场时间为11月份
                                    }else if(exitimeMonth_s.equals("11")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 30);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setNovember(jobNub_str);
                                        //入退场都不为11月份
                                    }else{
                                        delegainformation.setNovember("1.00");
                                    }
                                }else if(m == 12){
                                    //入退场月份都为12月份
                                    if(admissiontimeMonth_s.equals("12") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setDecember(jobNub_str);
                                        //仅入场时间为12月份
                                    }else if(admissiontimeMonth_s.equals("12")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_b / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setDecember(jobNub_str);
                                        //仅退场时间为12月份
                                    }else if(exitimeMonth_s.equals("12")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setDecember(jobNub_str);
                                        //入退场都不为12月份
                                    }else{
                                        delegainformation.setDecember("1.00");
                                    }
                                }else if(m == 1){
                                    //入退场月份都为1月份
                                    if(admissiontimeMonth_s.equals("01") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJanuary(jobNub_str);
                                        //仅入场时间为1月份
                                    }else if(admissiontimeMonth_s.equals("01")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_b / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJanuary(jobNub_str);
                                        //仅退场时间为1月份
                                    }else if(exitimeMonth_s.equals("01")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setJanuary(jobNub_str);
                                        //入退场都不为1月份
                                    }else{
                                        delegainformation.setJanuary("1.00");
                                    }
                                }else if(m == 2){
                                    //入退场月份都为2月份
                                    if(admissiontimeMonth_s.equals("02") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        workDay_i = Integer.parseInt(exitimeDay_s) - Integer.parseInt(admissiontimeDay_s) + 1;
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 28);
                                        jobNub_str = df.format(jobNub_obj);
                                        jobNub_dbe = Double.valueOf(jobNub_str);
                                        delegainformation.setFebruary(jobNub_str);
                                        //仅入场时间为2月份
                                    }else if(admissiontimeMonth_s.equals("02")){
                                        workDay_i = 28 - Integer.parseInt(admissiontimeDay_s);
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 28);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setFebruary(jobNub_str);
                                        //仅退场时间为2月份
                                    }else if(exitimeMonth_s.equals("02")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 28);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setFebruary(jobNub_str);
                                        //入退场都不为2月份
                                    }else{
                                        delegainformation.setFebruary("1.00");
                                    }
                                }else if(m == 3){
                                    //入退场月份都为3月份
                                    if(admissiontimeMonth_s.equals("03") && admissiontimeMonth_s.equals(exitimeMonth_s)){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_same / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setFebruary(jobNub_str);
                                        //仅入场时间为3月份
                                    }else if(admissiontimeMonth_s.equals("03")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ad_b / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setFebruary(jobNub_str);
                                        //仅退场时间为3月份
                                    }else if(exitimeMonth_s.equals("03")){
                                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i_ex / 31);
                                        jobNub_str = df.format(jobNub_obj);
                                        delegainformation.setFebruary(jobNub_str);
                                        //入退场都不为3月份
                                    }else{
                                        delegainformation.setFebruary("1.00");
                                    }
                                }
                            }
                        }
                    }
                } else {//退场时间为空
                    if (aprilFirst_d.before(admissiontime_d)) {
                        delegainformation.setApril("1.00");
                        delegainformation.setMay("1.00");
                        delegainformation.setJune("1.00");
                        delegainformation.setJuly("1.00");
                        delegainformation.setAugust("1.00");
                        delegainformation.setSeptember("1.00");
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("04")) {
                        workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setApril(jobNub_str);
                        delegainformation.setMay("1.00");
                        delegainformation.setJune("1.00");
                        delegainformation.setJuly("1.00");
                        delegainformation.setAugust("1.00");
                        delegainformation.setSeptember("1.00");
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("05")) {
                        workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setMay(jobNub_str);
                        delegainformation.setJune("1.00");
                        delegainformation.setJuly("1.00");
                        delegainformation.setAugust("1.00");
                        delegainformation.setSeptember("1.00");
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("06")) {
                        workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setJune(jobNub_str);
                        delegainformation.setJuly("1.00");
                        delegainformation.setAugust("1.00");
                        delegainformation.setSeptember("1.00");
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("07")) {
                        workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setJuly(jobNub_str);
                        delegainformation.setAugust("1.00");
                        delegainformation.setSeptember("1.00");
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("08")) {
                        workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setAugust(jobNub_str);
                        delegainformation.setSeptember("1.00");
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("09")) {
                        workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setSeptember(jobNub_str);
                        delegainformation.setOctober("1.00");
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("10")) {
                        workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setOctober(jobNub_str);
                        delegainformation.setNovember("1.00");
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("11")) {
                        workDay_i = 30 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 30);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setNovember(jobNub_str);
                        delegainformation.setDecember("1.00");
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("12")) {
                        workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setDecember(jobNub_str);
                        delegainformation.setJanuary("1.00");
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("01")) {
                        workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setJanuary(jobNub_str);
                        delegainformation.setFebruary("1.00");
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("02")) {
                        workDay_i = 28 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 28);
                        jobNub_str = df.format(jobNub_obj);
                        jobNub_dbe = Double.valueOf(jobNub_str);
                        if (jobNub_dbe > 1.00) {
                            jobNub_str = "1.00";
                        }
                        delegainformation.setFebruary(jobNub_str);
                        delegainformation.setMarch("1.00");
                        delegainformation.setYear(thisYear_s);
                    } else if (aprilFirst_d.after(admissiontime_d) && admissiontimeMonth_s.equals("03")) {
                        workDay_i = 31 - Integer.parseInt(admissiontimeDay_s) + 1;
                        BigDecimal jobNub_obj = new BigDecimal((float) workDay_i / 31);
                        jobNub_str = df.format(jobNub_obj);
                        delegainformation.setMarch(jobNub_str);
                        delegainformation.setYear(thisYear_s);
                    }
                }
            }
            delegainformationMapper.insert(delegainformation);
        }
    }
}
