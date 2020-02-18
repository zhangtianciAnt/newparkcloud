package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExpatriatesinforServiceImpl implements ExpatriatesinforService {

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private PricesetMapper pricesetMapper;

    @Override
    public List<Expatriatesinfor> getexpatriatesinfor(Expatriatesinfor expatriatesinfor) throws Exception {
        return expatriatesinforMapper.select(expatriatesinfor);
    }


    @Override
    public Expatriatesinfor getexpatriatesinforApplyOne(String expatriatesinfor_id) throws Exception {
        return expatriatesinforMapper.selectByPrimaryKey(expatriatesinfor_id);
    }

    @Override
    public void updateexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
    }

//    保存按钮
    @Override
    public void updateexpatriatesinfor(List<Expatriatesinfor> expatriatesinfor, TokenModel tokenModel) throws Exception {
        for (int i = 0; i < expatriatesinfor.size(); i++) {
            Expatriatesinfor expatriates = expatriatesinfor.get(i);
            expatriates.preUpdate(tokenModel);
            expatriatesinforMapper.updateByPrimaryKeySelective(expatriates);
        }
    }

    @Override
    public void createexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        expatriatesinfor.preInsert(tokenModel);
        expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
        String yes = "是";
        String no = "否";
        if (expatriatesinfor.getOperationform().equals("BP024001")) {
            expatriatesinfor.setDistriobjects(yes);
            expatriatesinfor.setVenuetarget(yes);
        } else if (expatriatesinfor.getOperationform().equals("BP024002")) {
            expatriatesinfor.setDistriobjects(no);
            expatriatesinfor.setVenuetarget(yes);
        } else {
            expatriatesinfor.setDistriobjects(no);
            expatriatesinfor.setVenuetarget(no);
        }
        expatriatesinforMapper.insert(expatriatesinfor);
        Priceset priceset = new Priceset();
        priceset.setPricesetid(UUID.randomUUID().toString());
        priceset.setUser_id(expatriatesinfor.getExpname());
        pricesetMapper.insert(priceset);
    }

    @Override
    public void setexpatriatesinforApply(List<Expatriatesinfor> expatriatesmation, TokenModel tokenModel) throws Exception {
        for (int j = 0; j < expatriatesmation.size(); j++) {
            Expatriatesinfor expatriatesinfor = expatriatesmation.get(j);
            expatriatesinfor.preInsert(tokenModel);
            //时间模板
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //入场时间
            String admissiontime;
            admissiontime = sdf.format(expatriatesinfor.getAdmissiontime());
            //入场时间——年份
            String admissiontimeYear;
            admissiontimeYear = admissiontime.substring(0, 4);
            int admissiontimeYear_nub;
            admissiontimeYear_nub= Integer.parseInt(admissiontimeYear);
            //入场时间月份
            String admissiontimeMonth;
            admissiontimeMonth = admissiontime.substring(4, 6);
            int admissiontime_month;
            admissiontime_month = Integer.parseInt(admissiontimeMonth);
            //入场日期Date类型
            Date admissiontime_nub = sdf.parse(admissiontime);
            //获取入场时间的日期
            String admissiontimeDay;
            admissiontimeDay = admissiontime.substring(admissiontime.length() - 2, admissiontime.length());
            int admissiontime_day;
            admissiontime_day = Integer.parseInt(admissiontimeDay);

            //退场时间确定
            //获取退场时间的月份
            //判断是否存在退场时间，若存在则规定日期模板
            String exitime;
            exitime = sdf.format(expatriatesinfor.getExitime());
            //退场时间日期类型
            Date exitime_data = sdf.parse(exitime);
            //获取退场时间的年份
            String exitimeYear;
            exitimeYear = exitime.substring(0, 4);
            int exitimeYear_nub = Integer.parseInt(exitimeYear);
            //退场时间的月份
            String getmonth;
            getmonth = exitime.substring(4, 6);
            int get_month;
            get_month = Integer.parseInt(getmonth);
            //获取退场时间的天数
            String getday;
            getday = exitime.substring(exitime.length() - 2, exitime.length());
            int get_day;
            get_day = Integer.parseInt(getday);

            //获取当前时间
            String thisdate = sdf.format(new Date());
            //当前年份
            String thisdate_year = thisdate.substring(0, 4);
            int thisdate_year_nub;
            thisdate_year_nub = Integer.parseInt(thisdate_year);
            Date thisdate_nub = sdf.parse(thisdate);
            String thisdateMonth;
            thisdateMonth = thisdate.substring(4, 6);
            int thisdate_month;
            thisdate_month = Integer.parseInt(thisdateMonth);
            String thisdateDay;
            thisdateDay = thisdate.substring(thisdate.length() - 2, thisdate.length());
            //当前日期
            int thisdate_day;
            thisdate_day = Integer.parseInt(thisdateDay);
            //获取当前的年份
            String newyear = thisdate.substring(0, 4);
            String initial = "0101";
            //当年元旦
            String newday = newyear + initial;
            Date newday_date = sdf.parse(newday);

            //格式化小数
            DecimalFormat df = new DecimalFormat("0.00");

            //thisdate_nub为当前日期，admissiontime_nub为该员工入场日期
            int month_real_ad;//入场月份
            int month_real_ex;//退场月份
            int month_real_nowmonth;//当前时间月份
            //工作天数
            int workDay;
            //月份容器
            int month_real;
            //工数
            String job_nub;
            //考勤月份
            String attendanceMonth_str = null;
            int attendanceMonth_nub;
            //判断考勤月份：
            // 1.若入场月份和退场月份为(今年)则考勤月份=退场月份-入场月份+1；
            //判断是否有退场时间，若有退场时间
            if(exitime_data.after(newday_date)) {
                if (expatriatesinfor.getExitime() != null) {
                    if (admissiontimeYear_nub == thisdate_year_nub && exitimeYear_nub == thisdate_year_nub) {
                        attendanceMonth_nub = get_month - admissiontime_month + 1;
                        attendanceMonth_str = Integer.toString(attendanceMonth_nub);
                        expatriatesinfor.setMonthlength(attendanceMonth_str);
                    } else if (admissiontimeYear_nub != thisdate_year_nub && exitimeYear_nub == thisdate_year_nub) {
                        // 2.若入场月份为去年，退场月份为今年，则考勤月份=退场月份；
                        attendanceMonth_nub = get_month;
                        attendanceMonth_str = Integer.toString(attendanceMonth_nub);
                        expatriatesinfor.setMonthlength(attendanceMonth_str);
                    } else if (admissiontimeYear_nub == thisdate_year_nub && exitimeYear_nub != thisdate_year_nub) {
                        // 3.若入场月份为今年，退场月份非今年，则考勤月份=12-入场月份+1；
                        attendanceMonth_nub = 12 - admissiontime_month + 1;
                        attendanceMonth_str = Integer.toString(attendanceMonth_nub);
                        expatriatesinfor.setMonthlength(attendanceMonth_str);
                    }
                } else {
                    //退场时间为空：只需判断入场时间是否为今年，若为今年且已经入场，考勤月份=当前月份-入场月份+1
                    if (admissiontimeYear_nub == thisdate_year_nub && thisdate_nub.after(admissiontime_nub)) {
                        attendanceMonth_nub = thisdate_month - admissiontime_month + 1;
                        attendanceMonth_str = Integer.toString(attendanceMonth_nub);
                        expatriatesinfor.setMonthlength(attendanceMonth_str);
                    } else if (admissiontimeYear_nub != thisdate_year_nub) {
                        attendanceMonth_nub = thisdate_month;
                        attendanceMonth_str = Integer.toString(attendanceMonth_nub);
                        expatriatesinfor.setMonthlength(attendanceMonth_str);
                    }
                }
                //入场日期admissiontime_nub在今年元旦newday_date之后，说明非今年来此工作
                if (newday_date.after(admissiontime_nub)) {
                    //判断是否有退场时间
                    //若有退场时间
                    if (expatriatesinfor.getExitime() != null) {
                        //判断当前时间与退场时间的先后顺序：
                        if (thisdate_nub.before(exitime_data)) {
                            // 若当前时间在退场时间之前（说明该员工还未退场）则工数用当前时间来算；
                            // 当前时间为一月份
                            //当前时间月份
                            month_real_nowmonth = thisdate_month;
                            //最后一个月工作了多少天
                            workDay = thisdate_day;
                            if (month_real_nowmonth == 1) {
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setJanuary(job_nub);
                            } else if (month_real_nowmonth == 2) {
                                // 当前时间为二月份
                                expatriatesinfor.setJanuary("1.00");
                                job_nub = df.format((float) workDay / 28);
                                float job_nub_flt = Float.valueOf(job_nub);
                                if (job_nub_flt > 1.00) {
                                    job_nub = "1.00";
                                }
                                expatriatesinfor.setFebruary(job_nub);
                            } else if (month_real_nowmonth == 3) {
                                // 当前时间为三月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setMarch(job_nub);
                            } else if (month_real_nowmonth == 4) {
                                // 当前时间为四月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setApril(job_nub);
                            } else if (month_real_nowmonth == 5) {
                                // 当前时间为五月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setMay(job_nub);
                            } else if (month_real_nowmonth == 6) {
                                // 当前时间为六月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setJune(job_nub);
                            } else if (month_real_nowmonth == 7) {
                                // 当前时间为七月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setJuly(job_nub);
                            } else if (month_real_nowmonth == 8) {
                                // 当前时间为八月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setAugust(job_nub);
                            } else if (month_real_nowmonth == 9) {
                                // 当前时间为九月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setSeptember(job_nub);
                            } else if (month_real_nowmonth == 10) {
                                // 当前时间为十月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                expatriatesinfor.setSeptember("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setOctober(job_nub);
                            } else if (month_real_nowmonth == 11) {
                                // 当前时间为十一月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                expatriatesinfor.setSeptember("1.00");
                                expatriatesinfor.setOctober("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setNovember(job_nub);
                            } else if (month_real_nowmonth == 12) {
                                // 当前时间为十二月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                expatriatesinfor.setSeptember("1.00");
                                expatriatesinfor.setOctober("1.00");
                                expatriatesinfor.setNovember("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setDecember(job_nub);
                            }
                        } else {
                            // 若退场时间在当前时间之前（说明该员工已经退场）按则工数用退场时间来算；
                            //退场的月份
                            month_real_nowmonth = get_month;
                            //最后一个月工作了多少天
                            workDay = get_day;
                            if (month_real_nowmonth == 1) {
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setJanuary(job_nub);
                            } else if (month_real_nowmonth == 2) {
                                // 当前时间为二月份
                                expatriatesinfor.setJanuary("1.00");
                                job_nub = df.format((float) workDay / 28);
                                float job_nub_flt = Float.valueOf(job_nub);
                                if (job_nub_flt > 1.00) {
                                    job_nub = "1.00";
                                }
                                expatriatesinfor.setFebruary(job_nub);
                            } else if (month_real_nowmonth == 3) {
                                // 当前时间为三月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setMarch(job_nub);
                            } else if (month_real_nowmonth == 4) {
                                // 当前时间为四月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setApril(job_nub);
                            } else if (month_real_nowmonth == 5) {
                                // 当前时间为五月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setMay(job_nub);
                            } else if (month_real_nowmonth == 6) {
                                // 当前时间为六月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setJune(job_nub);
                            } else if (month_real_nowmonth == 7) {
                                // 当前时间为七月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setJuly(job_nub);
                            } else if (month_real_nowmonth == 8) {
                                // 当前时间为八月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setAugust(job_nub);
                            } else if (month_real_nowmonth == 9) {
                                // 当前时间为九月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setSeptember(job_nub);
                            } else if (month_real_nowmonth == 10) {
                                // 当前时间为十月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                expatriatesinfor.setSeptember("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setOctober(job_nub);
                            } else if (month_real_nowmonth == 11) {
                                // 当前时间为十一月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                expatriatesinfor.setSeptember("1.00");
                                expatriatesinfor.setOctober("1.00");
                                job_nub = df.format((float) workDay / 30);
                                expatriatesinfor.setNovember(job_nub);
                            } else if (month_real_nowmonth == 12) {
                                // 当前时间为十二月份
                                expatriatesinfor.setJanuary("1.00");
                                expatriatesinfor.setFebruary("1.00");
                                expatriatesinfor.setMarch("1.00");
                                expatriatesinfor.setApril("1.00");
                                expatriatesinfor.setMay("1.00");
                                expatriatesinfor.setJune("1.00");
                                expatriatesinfor.setJuly("1.00");
                                expatriatesinfor.setAugust("1.00");
                                expatriatesinfor.setSeptember("1.00");
                                expatriatesinfor.setOctober("1.00");
                                expatriatesinfor.setNovember("1.00");
                                job_nub = df.format((float) workDay / 31);
                                expatriatesinfor.setDecember(job_nub);
                            }
                        }
                    }
                } else if (admissiontimeYear_nub == thisdate_year_nub) {
                    //入场时间为今年！！
                    //1.首先需要判断入场时间与当前时间的先后顺序
                    //1.1，若当前时间在入场时间之前，则说明该员工还未入场工作，则不进行计算
                    //1.2，若当前时间在入场时间之后，则说明该员工已经入场工作，进行计算
                    //1.2.1，当进行计算时，该员工入场的该月份的工数：（入场月的工数=（当前月份的最大日期-该员工入场的日期+1）/该月份的最大日期）
                    //！！注意：还需要判断退场时间：
                    //1.2.1.1，若退场时间与入场时间为同一个月，则该员工本月的工数：（本月的工数=（本月退场日期-本月入场日期+1）/该月份最大日期）
                    //1.2.1.2，若退场时间与入场时间不同一个月，则该员工的工数，入场与退场的月份需计算，之间的几个月工数都为1.00

                    if (expatriatesinfor.getExitime() != null) {
                        //判断退场时间是否为空，不为空!!!
                        if (thisdate_nub.after(admissiontime_nub)) {
                            //说明该员工已经入场工作
                            //判断入退场时间是否为同一个月
                            //工作天数
                            workDay = get_day - admissiontime_day + 1;
                            if (admissiontime_month == get_month) {
                                //为同一个月admissiontime_day入场日期
                                month_real = get_month;
                                if (month_real == 1) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setJanuary(job_nub);
                                } else if (month_real == 2) {
                                    job_nub = df.format((float) workDay / 28);
                                    float job_nub_flt = Float.valueOf(job_nub);
                                    if (job_nub_flt > 1.00) {
                                        job_nub = "1.00";
                                    }
                                    expatriatesinfor.setFebruary(job_nub);
                                } else if (month_real == 3) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setMarch(job_nub);
                                } else if (month_real == 4) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setApril(job_nub);
                                } else if (month_real == 5) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setMay(job_nub);
                                } else if (month_real == 6) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setJune(job_nub);
                                } else if (month_real == 7) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setJuly(job_nub);
                                } else if (month_real == 8) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setAugust(job_nub);
                                } else if (month_real == 9) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setSeptember(job_nub);
                                } else if (month_real == 10) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setOctober(job_nub);
                                } else if (month_real == 11) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setNovember(job_nub);
                                } else if (month_real == 12) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setDecember(job_nub);
                                }
                            } else {
                                //2.其次需要判断退场时间与当前时间的先后顺序
                                // 2.1，若当前时间在退场时间之前，则取当前时间做计算
                                //2.1.1，当进行计算时，该员工该月份的工数：（当前月的工数=当前的日期/该月份的最大日期）
                                // 2.2，若当前时间在退场时间之后，说明该员工已退场，则取该员工退场时间做计算
                                //2.2.1，当进行计算时，该员工退场的该月份的工数：（退场月的工数=该员工退场的日期/该月份的最大日期）
                                //不是同一个月！！！！！
                                month_real_ad = admissiontime_month;
                                month_real_ex = get_month;
                                //计算工作几个月
                                int work_month = month_real_ex - month_real_ad + 1;
                                String workMonth = Integer.toString(work_month);
                                //循环判断
                                for (int i = 1; i < 13; i++) {
                                    if (i >= month_real_ad && i <= month_real_ex) {
                                        if (i == 1) {
                                            workDay = 31 - admissiontime_day + 1;
                                            job_nub = df.format((float) workDay / 31);
                                            expatriatesinfor.setJanuary(job_nub);
                                        } else if (i == 2) {
                                            if (month_real_ad == 2) {
                                                workDay = 28 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 28);
                                                float job_nub_flt = Float.valueOf(job_nub);
                                                if (job_nub_flt > 1.00) {
                                                    job_nub = "1.00";
                                                }
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 2) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 28);
                                                float job_nub_flt = Float.valueOf(job_nub);
                                                if (job_nub_flt > 1.00) {
                                                    job_nub = "1.00";
                                                }
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setFebruary("1.00");
                                            }
                                        } else if (i == 3) {
                                            if (month_real_ad == 3) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 3) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 4) {
                                            if (month_real_ad == 4) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 4) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 5) {
                                            if (month_real_ad == 5) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 5) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 6) {
                                            if (month_real_ad == 6) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 6) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 7) {
                                            if (month_real_ad == 7) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 7) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 8) {
                                            if (month_real_ad == 8) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 8) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 9) {
                                            if (month_real_ad == 9) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 9) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 10) {
                                            if (month_real_ad == 10) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 10) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 11) {
                                            if (month_real_ad == 11) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_ex == 11) {
                                                workDay = get_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 12) {
                                            workDay = get_day;
                                            job_nub = df.format((float) workDay / 31);
                                            expatriatesinfor.setDecember(job_nub);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        //退场时间为空！！！！！
                        month_real_ad = admissiontime_month;
                        //当前时间月份
                        month_real_nowmonth = thisdate_month;
                        //首先，判断入场时间的月份是否和当前月份相同
                        //注意！！需要判断当前时间与入场时间谁前谁后
                        if (thisdate_nub.after(admissiontime_nub)) {
                            if (month_real_ad == month_real_nowmonth) {
                                //月份相同
                                workDay = thisdate_day - admissiontime_day + 1;
                                month_real = month_real_ad;
                                if (month_real == 1) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setJanuary(job_nub);
                                } else if (month_real == 2) {
                                    job_nub = df.format((float) workDay / 28);
                                    float job_nub_flt = Float.valueOf(job_nub);
                                    if (job_nub_flt > 1.00) {
                                        job_nub = "1.00";
                                    }
                                    expatriatesinfor.setFebruary(job_nub);
                                } else if (month_real == 3) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setMarch(job_nub);
                                } else if (month_real == 4) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setApril(job_nub);
                                } else if (month_real == 5) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setMay(job_nub);
                                } else if (month_real == 6) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setJune(job_nub);
                                } else if (month_real == 7) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setJuly(job_nub);
                                } else if (month_real == 8) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setAugust(job_nub);
                                } else if (month_real == 9) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setSeptember(job_nub);
                                } else if (month_real == 10) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setOctober(job_nub);
                                } else if (month_real == 11) {
                                    job_nub = df.format((float) workDay / 30);
                                    expatriatesinfor.setNovember(job_nub);
                                } else if (month_real == 12) {
                                    job_nub = df.format((float) workDay / 31);
                                    expatriatesinfor.setDecember(job_nub);
                                }
                            } else {
                                //入场月份与当前月份不同！！！
                                month_real_ad = admissiontime_month;
                                month_real_nowmonth = thisdate_month;
                                //计算工作几个月
                                int work_month = month_real_nowmonth - month_real_ad + 1;
                                String workMonth = Integer.toString(work_month);
                                //循环判断
                                for (int i = 1; i < 13; i++) {
                                    if (i >= month_real_ad && i <= month_real_nowmonth) {
                                        if (i == 1) {
                                            workDay = 31 - admissiontime_day + 1;
                                            job_nub = df.format((float) workDay / 31);
                                            expatriatesinfor.setJanuary(job_nub);
                                        } else if (i == 2) {
                                            if (month_real_ad == 2) {
                                                workDay = 28 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 28);
                                                float job_nub_flt = Float.valueOf(job_nub);
                                                if (job_nub_flt > 1.00) {
                                                    job_nub = "1.00";
                                                }
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 2) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 28);
                                                float job_nub_flt = Float.valueOf(job_nub);
                                                if (job_nub_flt > 1.00) {
                                                    job_nub = "1.00";
                                                }
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setFebruary("1.00");
                                            }
                                        } else if (i == 3) {
                                            if (month_real_ad == 3) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 3) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 4) {
                                            if (month_real_ad == 4) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 4) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 5) {
                                            if (month_real_ad == 5) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 5) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 6) {
                                            if (month_real_ad == 6) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 6) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 7) {
                                            if (month_real_ad == 7) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 7) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 8) {
                                            if (month_real_ad == 8) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 8) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 9) {
                                            if (month_real_ad == 9) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 9) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 10) {
                                            if (month_real_ad == 10) {
                                                workDay = 31 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 10) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 31);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 11) {
                                            if (month_real_ad == 11) {
                                                workDay = 30 - admissiontime_day + 1;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else if (month_real_nowmonth == 11) {
                                                workDay = thisdate_day;
                                                job_nub = df.format((float) workDay / 30);
                                                expatriatesinfor.setFebruary(job_nub);
                                            } else {
                                                expatriatesinfor.setMarch("1.00");
                                            }
                                        } else if (i == 12) {
                                            workDay = thisdate_day;
                                            job_nub = df.format((float) workDay / 31);
                                            expatriatesinfor.setDecember(job_nub);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> expimport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
//            创建listVo集合方便存储导入信息
            List<Expatriatesinfor> listVo = new ArrayList<Expatriatesinfor>();
//            创建Result结果集的集合
            List<String> Result = new ArrayList<String>();
//            用来接收前台传过来的文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            创建对象f，且为空
            File f = null;
//            创建临时文件
            f = File.createTempFile("temp", null);
//            上传文件
            file.transferTo(f);
//            使用Excel读文件
            ExcelReader reader = ExcelUtil.getReader(f);
//            创建集合存入读的文件
            List<List<Object>> list = reader.read();
//            创建集合存入标准模板
            List<Object> model = new ArrayList<Object>();
//            标准模板
            model.add("姓名");
            model.add("性别");
            model.add("供应商名称");
            model.add("毕业院校");
            model.add("学历");
            model.add("技术分类");
            model.add("Rn");
            model.add("作業形態");
            model.add("作業分類");
            List<Object> key = list.get(0);
//           上传模板与标准模板 校验
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    expatriatesinfor.setExpname(value.get(0).toString());
                    expatriatesinfor.setSex(value.get(1).toString());
                    expatriatesinfor.setSuppliername(value.get(2).toString());
                    expatriatesinfor.setGraduateschool(value.get(3).toString());
                    expatriatesinfor.setEducation(value.get(4).toString());
                    expatriatesinfor.setTechnology(value.get(5).toString());
                    expatriatesinfor.setRn(value.get(6).toString());
                    expatriatesinfor.setOperationform(value.get(7).toString());
                    expatriatesinfor.setJobclassification(value.get(8).toString());
                }
//                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                if (value.size() > 1) {
//                    String date = value.get(1).toString();
//                    String date1 = value.get(1).toString();
//                    date = date.substring(5, 7);
//                    date1 = date1.substring(8, 10);
//                    if (Integer.parseInt(date1) > 31) {
//                        error = error + 1;
//                        Result.add("模板第" + (k - 1) + "行的日期格式错误，请输入正确的日子 ，导入失败");
//                        continue;
//                    }
//                    if (Integer.parseInt(date) > 12) {
//                        error = error + 1;
//                        Result.add("模板第" + (k - 1) + "行的的日期格式错误，请输入正确的月份，导入失败");
//                        continue;
//                    }
//                }
//                String Birth = value.get(3).toString();
//                expatriatesinfor.setBirth(sf.parse(Birth));
//                String Admissiontime = value.get(10).toString();
//                expatriatesinfor.setAdmissiontime(sf.parse(Admissiontime));
                expatriatesinfor.preInsert();
                expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
                expatriatesinforMapper.insert(expatriatesinfor);
                listVo.add(expatriatesinfor);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}



